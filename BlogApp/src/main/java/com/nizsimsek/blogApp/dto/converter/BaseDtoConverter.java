package com.nizsimsek.blogApp.dto.converter;

import com.nizsimsek.blogApp.dto.*;
import com.nizsimsek.blogApp.model.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BaseDtoConverter {

    protected List<PostDto> getPostDtos(List<Post> posts) {
        return posts.stream()
                .map(post -> new PostDto(
                        post.getId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getLikes(),
                        post.getCreatedDate(),
                        post.getUpdatedDate()
                        ))
                .collect(Collectors.toList());
    }

    protected List<CommentDto> getCommentDtos(List<Comment> comments) {
        return comments.stream()
                .map(comment -> new CommentDto(
                        comment.getId(),
                        comment.getContent(),
                        comment.getLikes(),
                        comment.getCreatedDate(),
                        comment.getUpdatedDate()
                        ))
                .collect(Collectors.toList());
    }

    protected List<SubCommentDto> getSubCommentDtos(List<SubComment> subComments) {
        return subComments.stream()
                .map(subComment -> new SubCommentDto(
                        subComment.getId(),
                        subComment.getContent(),
                        subComment.getLikes(),
                        subComment.getCreatedDate(),
                        subComment.getUpdatedDate()
                        ))
                .collect(Collectors.toList());
    }
}
