package com.nizsimsek.blogApp.controller;

import com.nizsimsek.blogApp.dto.UserDto;
import com.nizsimsek.blogApp.dto.request.CreateUserReq;
import com.nizsimsek.blogApp.dto.request.UpdateUserReq;
import com.nizsimsek.blogApp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserReq createUserReq) {
        return ResponseEntity.ok(userService.createUser(createUserReq));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String id, @Valid @RequestBody UpdateUserReq updateUserReq) {
        return ResponseEntity.ok(userService.updateUser(id, updateUserReq));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable String id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("User deleted..");
    }
}
