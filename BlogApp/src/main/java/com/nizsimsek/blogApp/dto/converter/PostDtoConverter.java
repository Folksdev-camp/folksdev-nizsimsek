package com.nizsimsek.blogApp.dto.converter;

import com.nizsimsek.blogApp.dto.PostDto;
import com.nizsimsek.blogApp.dto.UserDto;
import com.nizsimsek.blogApp.model.Post;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public List<PostDto> convertToPostDtos(List<Post> posts) {

        return posts
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
