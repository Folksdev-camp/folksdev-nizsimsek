package com.nizsimsek.blogApp.dto.converter;

import com.nizsimsek.blogApp.dto.*;
import com.nizsimsek.blogApp.model.Post;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PostDtoConverter extends BaseDtoConverter {

    public PostDto convert(Post post) {

        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getLikes(),
                post.getDislikes(),
                post.getCreatedDate(),
                post.getUpdatedDate(),
                getCategoryDtos((Objects.requireNonNull(post.getCategory()))),
                new UserDto(
                        Objects.requireNonNull(post.getAuthor().getId()),
                        post.getAuthor().getUsername(),
                        post.getAuthor().getEmail(),
                        post.getAuthor().getFirstName(),
                        post.getAuthor().getLastName()
                ),
                getCommentDtos(Objects.requireNonNull(post.getComments()))
        );
    }
}
