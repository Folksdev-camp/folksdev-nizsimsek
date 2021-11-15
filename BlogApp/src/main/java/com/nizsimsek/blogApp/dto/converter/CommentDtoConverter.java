package com.nizsimsek.blogApp.dto.converter;

import com.nizsimsek.blogApp.dto.*;
import com.nizsimsek.blogApp.model.Comment;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CommentDtoConverter extends BaseDtoConverter{

    public CommentDto convert(Comment comment) {

        return new CommentDto(
                comment.getId(),
                comment.getContent(),
                comment.getLikes(),
                comment.getDislikes(),
                comment.getCreatedDate(),
                comment.getUpdatedDate(),
                new UserDto(
                        comment.getAuthor().getId(),
                        comment.getAuthor().getUsername(),
                        comment.getAuthor().getEmail(),
                        comment.getAuthor().getFirstName(),
                        comment.getAuthor().getLastName()
                ),
                new PostDto(
                        comment.getPost().getId(),
                        comment.getPost().getTitle(),
                        comment.getPost().getContent(),
                        comment.getPost().getLikes(),
                        comment.getPost().getDislikes(),
                        comment.getPost().getCreatedDate(),
                        comment.getPost().getUpdatedDate(),
                        getCategoryDtos(Objects.requireNonNull(comment.getPost().getCategory()))
                ),
                getSubCommentDtos(Objects.requireNonNull(comment.getSubComments()))
        );
    }
}
