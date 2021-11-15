package com.nizsimsek.blogApp.service;

import com.nizsimsek.blogApp.dto.UserDto;
import com.nizsimsek.blogApp.dto.converter.UserDtoConverter;
import com.nizsimsek.blogApp.dto.request.*;
import com.nizsimsek.blogApp.exception.GeneralNotFoundException;
import com.nizsimsek.blogApp.model.User;
import com.nizsimsek.blogApp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserDtoConverter userDtoConverter;

    public UserService(UserRepository userRepository,
                       UserDtoConverter userDtoConverter) {
        this.userRepository = userRepository;
        this.userDtoConverter = userDtoConverter;
    }

    public UserDto createUser(CreateUserReq createUserReq) {

        User user = new User(
                createUserReq.getUsername(),
                createUserReq.getEmail(),
                createUserReq.getFirstName(),
                createUserReq.getLastName(),
                createUserReq.getPassword()
        );

        return userDtoConverter.convert(userRepository.save(user));
    }

    public List<UserDto> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(userDtoConverter::convert)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(String id) {

        return userDtoConverter.convert(findUserById(id));
    }

    public UserDto updateUser(String id, UpdateUserReq updateUserReq) {

        User user = findUserById(id);

        User updatedUser = new User(
                user.getId(),
                updateUserReq.getUsername(),
                updateUserReq.getEmail(),
                updateUserReq.getFirstName(),
                updateUserReq.getLastName(),
                updateUserReq.getPassword(),
                user.getPosts(),
                user.getComments(),
                user.getSubComments()
        );

        return userDtoConverter.convert(userRepository.save(updatedUser));
    }

    public void deleteUserById(String id) {
        userRepository.deleteById(id);
    }

    protected User findUserById(String id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new GeneralNotFoundException("User could not find by id : " + id));
    }
}
