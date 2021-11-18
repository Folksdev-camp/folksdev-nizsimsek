package com.nizsimsek.blogApp.service;

import com.nizsimsek.blogApp.TestSupport;
import com.nizsimsek.blogApp.dto.UserDto;
import com.nizsimsek.blogApp.dto.converter.UserDtoConverter;
import com.nizsimsek.blogApp.dto.request.CreateUserReq;
import com.nizsimsek.blogApp.dto.request.UpdateUserReq;
import com.nizsimsek.blogApp.exception.GeneralNotFoundException;
import com.nizsimsek.blogApp.model.User;
import com.nizsimsek.blogApp.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceTest extends TestSupport {

    private UserRepository userRepository;
    private UserDtoConverter userDtoConverter;

    private UserService userService;

    @BeforeEach
    void setUp() {

        userRepository = mock(UserRepository.class);
        userDtoConverter = mock(UserDtoConverter.class);

        userService = new UserService(userRepository, userDtoConverter);
    }

    @Test
    void testCreateUser_whenCreateUserReqValid_shouldReturnUserDto() {

        CreateUserReq createUserReq = generateCreateUserReq();
        User user = generateUser();
        UserDto userDto = generateUserDto();

        when(userRepository.save(user)).thenReturn(user);
        when(userDtoConverter.convert(user)).thenReturn(userDto);

        UserDto result = userService.createUser(createUserReq);

        assertEquals(userDto, result);

        verify(userRepository).save(user);
        verify(userDtoConverter).convert(user);
    }

    @Test
    void testGetAllUsers_shouldReturnUserDtos() {

        List<User> users = generateUsers();
        List<UserDto> userDtos = generateUserDtos();

        when(userRepository.findAll()).thenReturn(users);
        when(userDtoConverter.convertToUserDtos(users)).thenReturn(userDtos);

        List<UserDto> result = userService.getAllUsers();

        assertEquals(userDtos, result);

        verify(userRepository).findAll();
        verify(userDtoConverter).convertToUserDtos(users);
    }

    @Test
    void testGetUserById_whenIdNotExist_shouldThrowGeneralNotFoundException() {

        when(userRepository.findById("id")).thenThrow(GeneralNotFoundException.class);

        assertThrows(GeneralNotFoundException.class, () -> userService.getUserById("id"));

        verify(userRepository).findById("id");
        verifyNoInteractions(userDtoConverter);
    }

    @Test
    void testGetUserById_whenIdExist_shouldReturnUserDto() {

        User user = generateUser();
        UserDto userDto = generateUserDto();

        when(userRepository.findById("id")).thenReturn(java.util.Optional.ofNullable(user));
        assert user != null;
        when(userDtoConverter.convert(user)).thenReturn(userDto);

        UserDto result = userService.getUserById("id");

        assertEquals(userDto, result);

        verify(userRepository).findById("id");
        verify(userDtoConverter).convert(user);
    }

    @Test
    void testUpdateUser_whenUpdateUserReqInValid_shouldThrowGeneralNotFoundException() {

        UpdateUserReq updateUserReq = generateUpdateUserReq();

        when(userRepository.findById("id")).thenThrow(GeneralNotFoundException.class);

        assertThrows(GeneralNotFoundException.class, () -> userService.updateUser("id", updateUserReq));

        verify(userRepository).findById("id");
        verifyNoInteractions(userDtoConverter);
    }

    @Test
    void testUpdateUser_whenUpdateUserReqValid_shouldReturnUserDto() {
        UpdateUserReq updateUserReq = generateUpdateUserReq();
        User user = generateUser();
        UserDto userDto = generateUserDto();

        when(userRepository.findById("id")).thenReturn(java.util.Optional.ofNullable(user));
        assert user != null;
        when(userRepository.save(user)).thenReturn(user);
        when(userDtoConverter.convert(user)).thenReturn(userDto);

        UserDto result = userService.updateUser("id", updateUserReq);

        assertEquals(userDto, result);

        verify(userRepository).findById("id");
        verify(userRepository).save(user);
        verify(userDtoConverter).convert(user);
    }

    @Test
    void testDeleteUserById_whenIdNotExist_shouldThrowNotFoundException() {

        when(userRepository.findById("id")).thenThrow(GeneralNotFoundException.class);

        assertThrows(GeneralNotFoundException.class, () -> userService.getUserById("id"));

        verify(userRepository).findById("id");
        verifyNoInteractions(userDtoConverter);
    }

    @Test
    void testDeleteUserById_whenIdExist() {

        userService.deleteUserById("id");

        verify(userRepository).deleteById("id");
    }

    @AfterEach
    void tearDown() {
    }
}