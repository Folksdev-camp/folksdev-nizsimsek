package com.nizsimsek.blogApp.dto.converter;

import com.nizsimsek.blogApp.dto.UserDto;
import com.nizsimsek.blogApp.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class UserDtoConverter extends BaseDtoConverter {

    public UserDto convert(User user) {

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                getPostDtos(Objects.requireNonNull(user.getPosts())),
                getCommentDtos(Objects.requireNonNull(user.getComments())),
                getSubCommentDtos(Objects.requireNonNull(user.getSubComments()))
        );
    }

    public List<UserDto> convertToUserDtos(List<User> users) {

        return users
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
