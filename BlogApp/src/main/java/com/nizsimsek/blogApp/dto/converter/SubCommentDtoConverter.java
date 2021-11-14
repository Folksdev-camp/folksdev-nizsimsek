package com.nizsimsek.blogApp.dto.converter;

import com.nizsimsek.blogApp.dto.*;
import com.nizsimsek.blogApp.model.SubComment;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SubCommentDtoConverter {

    public SubCommentDto convert(SubComment subComment) {

        return new SubCommentDto(
                subComment.getId(),
                subComment.getContent(),
                subComment.getLikes(),
                subComment.getCreatedDate(),
                subComment.getUpdatedDate(),
                new UserDto(
                        Objects.requireNonNull(subComment.getAuthor().getId()),
                        subComment.getAuthor().getUsername(),
                        subComment.getAuthor().getEmail(),
                        subComment.getAuthor().getFirstName(),
                        subComment.getAuthor().getLastName()
                ),
                new PostDto(
                        subComment.getPost().getId(),
                        subComment.getPost().getTitle(),
                        subComment.getPost().getContent(),
                        subComment.getPost().getLikes(),
                        subComment.getPost().getCreatedDate()
                ),
                new CommentDto(
                        subComment.getComment().getId(),
                        subComment.getComment().getContent(),
                        subComment.getComment().getLikes(),
                        subComment.getComment().getCreatedDate()
                )
        );
    }
}
