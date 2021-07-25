package com.turong.multitenant.mybatisplus.controller;

import com.turong.multitenant.mybatisplus.convert.UserConvert;
import com.turong.multitenant.mybatisplus.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private UserConvert userConvert;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(
            @RequestHeader("x-tenant-id") final String tenant, @PathVariable final String id) {
        log.debug("Get user with id={}, tenant={}", id, tenant);
        return ResponseEntity.ok(userService.getUser(id));

    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody final UserSaveRequest userSaveRequest) {
        userValidator.validate(userSaveRequest);
        return ResponseEntity.ok(userConvert.toResponse(
                userService.create(userConvert.toUser(userSaveRequest))));
    }

    @DeleteMapping
    public void deleteUserByEmail(@RequestParam final String email) {
        if (StringUtils.isBlank(email)) {
            throw new IllegalArgumentException("Email to delete must be present!");
        }
        userService.deleteByEmail(email);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable final String id) {
        if (StringUtils.isBlank(id)) {
            throw new IllegalArgumentException("Id to delete must be present!");
        }
        userService.deleteById(id);
    }

}
