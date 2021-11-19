package com.nizsimsek.blogApp.controller;

import com.nizsimsek.blogApp.IntegrationTestSupport;
import com.nizsimsek.blogApp.dto.request.CreateUserReq;
import com.nizsimsek.blogApp.dto.request.UpdateUserReq;
import com.nizsimsek.blogApp.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerIT extends IntegrationTestSupport {

    @Test
    void testGetUsers_shouldReturnEmptyList() throws Exception {

        this.mockMvc.perform(get("/v1/users/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<User> userList = userRepository.findAll();

        assertEquals(0, userList.size());
    }

    @Test
    void testGetUsers_shouldReturnUserDtoList() throws Exception {

        userRepository.save(generateUser(1));

        this.mockMvc.perform(get("/v1/users/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<User> userList = userRepository.findAll();

        assertEquals(1, userList.size());
    }

    @Test
    void testGetUserById_whenUserIdNotExist_shouldReturnGeneralNotFoundException() throws Exception {

        this.mockMvc.perform(get("/v1/users/not-exist-id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetUserById_whenUserIdExist_shouldReturnUserDto() throws Exception {

        User user = userRepository.save(generateUser(1));

        this.mockMvc.perform(get("/v1/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        User getUser = userRepository.findById(Objects.requireNonNull(user.getId())).orElse(null);

        assertEquals(user, getUser);
    }

    @Test
    void testCreateUser_whenCreateUserReqInValid_shouldReturn400BadRequest() throws Exception {

        CreateUserReq createUserReq = new CreateUserReq(
                "",
                "",
                "",
                "",
                ""
        );

        this.mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(createUserReq)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", notNullValue()))
                .andExpect(jsonPath("$.email", notNullValue()))
                .andExpect(jsonPath("$.firstName", notNullValue()))
                .andExpect(jsonPath("$.lastName", notNullValue()))
                .andExpect(jsonPath("$.password", notNullValue()));
    }

    @Test
    void testCreateUser_whenCreateUserReqValid_shouldReturnUserDto() throws Exception {

        CreateUserReq createUserReq = generateCreateUserReq();

        this.mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(createUserReq)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("username")))
                .andExpect(jsonPath("$.email", is("username@email.com")))
                .andExpect(jsonPath("$.firstName", is("firstName")))
                .andExpect(jsonPath("$.lastName", is("lastName")))
                .andExpect(jsonPath("$.password", is("password")));

        List<User> userList = userRepository.findAll();

        assertEquals(1, userList.size());
    }

    @Test
    void testUpdateUser_whenUpdateUserReqInValid_shouldReturn400BadRequest() throws Exception {

        UpdateUserReq updateUserReq = new UpdateUserReq(
                "",
                "",
                "",
                "",
                ""
        );

        this.mockMvc.perform(put("/v1/users/not-exist-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updateUserReq)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", notNullValue()))
                .andExpect(jsonPath("$.email", notNullValue()))
                .andExpect(jsonPath("$.firstName", notNullValue()))
                .andExpect(jsonPath("$.lastName", notNullValue()))
                .andExpect(jsonPath("$.password", notNullValue()));
    }

    @Test
    void testUpdateUser_whenUpdateUserReqValid_shouldReturnUserDto() throws Exception {

        UpdateUserReq updateUserReq = new UpdateUserReq(
                "username",
                "username@email.com",
                "firstName",
                "lastName",
                "password"
        );
        User user = userRepository.save(generateUser());

        this.mockMvc.perform(put("/v1/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(updateUserReq)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.firstName", is(user.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(user.getLastName())))
                .andExpect(jsonPath("$.password", is(user.getPassword())));

        User updatedUser = userRepository.findById(Objects.requireNonNull(user.getId())).orElse(null);

        assertEquals(updatedUser, user);
    }

    @Test
    void testDeleteUserById_whenUserIdExist_shouldReturnString() throws Exception {

        User user = userRepository.save(generateUser());

        this.mockMvc.perform(delete("/v1/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("User deleted.."));
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }
}