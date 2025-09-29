package org.acm.dto;

import java.util.List;

public class MergedPostDto {
    public Integer id;
    public String title;
    public String body;
    public UserDto author;
    public List<CommentDto> comments;
}
