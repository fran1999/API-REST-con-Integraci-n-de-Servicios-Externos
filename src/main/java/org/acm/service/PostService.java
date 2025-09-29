package org.acm.service;

import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acm.dto.*;
import org.acm.ext.JsonPlaceholderClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class PostService {

    private static final Logger LOG = Logger.getLogger(PostService.class);

    @Inject
    @RestClient
    JsonPlaceholderClient client;

    @CacheResult(cacheName = "ext-posts")
    public List<PostDto> getExternalPosts() {
        LOG.info("Fetching posts from external API");
        return client.getPosts();
    }

    @CacheResult(cacheName = "ext-comments")
    public List<CommentDto> getExternalComments() {
        LOG.info("Fetching comments from external API");
        return client.getComments();
    }

    @CacheResult(cacheName = "ext-users")
    public List<UserDto> getExternalUsers() {
        LOG.info("Fetching users from external API");
        return client.getUsers();
    }

    public List<MergedPostDto> getMergedPosts() {
        LOG.debug("Merging posts, comments and users...");
        List<PostDto> posts = getExternalPosts();
        List<CommentDto> comments = getExternalComments();
        Map<Integer, List<CommentDto>> commentsByPost = comments.stream()
                .collect(Collectors.groupingBy(c -> c.postId));

        Map<Integer, UserDto> usersById = getExternalUsers().stream()
                .collect(Collectors.toMap(u -> u.id, u -> u));

        List<MergedPostDto> merged = new ArrayList<>(posts.size());
        for (PostDto p : posts) {
            MergedPostDto m = new MergedPostDto();
            m.id = p.id;
            m.title = p.title;
            m.body = p.body;
            m.author = usersById.get(p.userId);
            m.comments = commentsByPost.getOrDefault(p.id, Collections.emptyList());
            merged.add(m);
        }
        LOG.infof("Merged %d posts successfully", merged.size());
        return merged;
    }
    public List<MergedPostDto> getMergedPostsFiltered(Long userId, Integer minComments, String q) {
        List<MergedPostDto> all = getMergedPosts();

        if (userId != null) {
            all = all.stream()
                    .filter(p -> p.author != null && Objects.equals(p.author.id, userId.intValue()))
                    .toList();
        }
        if (minComments != null) {
            all = all.stream()
                    .filter(p -> p.comments != null && p.comments.size() >= minComments)
                    .toList();
        }
        if (q != null && !q.isEmpty()) {
            String needle = q.toLowerCase(Locale.ROOT);
            all = all.stream()
                    .filter(p -> (p.title != null && p.title.toLowerCase(Locale.ROOT).contains(needle)) ||
                            (p.body  != null && p.body.toLowerCase(Locale.ROOT).contains(needle)))
                    .toList();
        }
        return all;
    }

    public void deleteRemotePost(Long id) {
        LOG.warnf("Deleting post with id=%d from external API", id);
        client.deletePost(id);
    }
}
