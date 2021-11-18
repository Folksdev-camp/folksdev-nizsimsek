package com.nizsimsek.blogApp.dto.converter;

import com.nizsimsek.blogApp.dto.CommentDto;
import com.nizsimsek.blogApp.dto.SubCommentDto;
import com.nizsimsek.blogApp.dto.UserDto;
import com.nizsimsek.blogApp.model.SubComment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class SubCommentDtoConverter extends BaseDtoConverter {

    public SubCommentDto convert(SubComment subComment) {

        return new SubCommentDto(
                subComment.getId(),
                subComment.getContent(),
                subComment.getLikes(),
                subComment.getDislikes(),
                subComment.getCreatedDate(),
                subComment.getUpdatedDate(),
                new UserDto(
                        Objects.requireNonNull(subComment.getAuthor().getId()),
                        subComment.getAuthor().getUsername(),
                        subComment.getAuthor().getEmail(),
                        subComment.getAuthor().getFirstName(),
                        subComment.getAuthor().getLastName()
                ),
                new CommentDto(
                        subComment.getComment().getId(),
                        subComment.getComment().getContent(),
                        subComment.getComment().getLikes(),
                        subComment.getComment().getDislikes(),
                        subComment.getComment().getCreatedDate()
                )
        );
    }

    public List<SubCommentDto> convertToSubCommentDtos(List<SubComment> subComments) {

        return subComments
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
