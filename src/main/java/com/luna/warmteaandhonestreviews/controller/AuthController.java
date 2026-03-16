package com.luna.warmteaandhonestreviews.controller;

import com.luna.warmteaandhonestreviews.dto.LoginReqDto;
import com.luna.warmteaandhonestreviews.exception.PasswordMismatchException;
import com.luna.warmteaandhonestreviews.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/admin/login")
    public ResponseEntity<Void> login(@RequestBody LoginReqDto loginReqDto) {
        boolean isLoggedIn = userService.login(loginReqDto.username(), loginReqDto.password());
        if (!isLoggedIn) {
            log.warn("Login failed for username: {}", loginReqDto.username());
            throw new PasswordMismatchException("Wrong password.");
        }
        return ResponseEntity.ok().build();
    }
}
