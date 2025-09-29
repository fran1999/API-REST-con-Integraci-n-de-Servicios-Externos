package org.acm.ext;

import org.acm.dto.*;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@RegisterRestClient(configKey = "jsonplaceholder")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface JsonPlaceholderClient {

    @GET @Path("/posts")
    List<PostDto> getPosts();

    @GET @Path("/comments")
    List<CommentDto> getComments();

    @GET @Path("/users")
    List<UserDto> getUsers();

    @DELETE @Path("/posts/{id}")
    void deletePost(@PathParam("id") Long id);
}
