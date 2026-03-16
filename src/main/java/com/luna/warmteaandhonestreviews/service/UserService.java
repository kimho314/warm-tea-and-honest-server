package com.luna.warmteaandhonestreviews.service;

import com.luna.warmteaandhonestreviews.domain.UserEntity;
import com.luna.warmteaandhonestreviews.exception.UserNotFoundException;
import com.luna.warmteaandhonestreviews.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public String getUserIdByUsername(@NonNull String username) {
        return userRepository.findByUsername(username)
            .map(it -> it.getId().toString())
            .orElseThrow(
                () -> new UserNotFoundException("User not found with username: " + username));
    }

    public UserEntity getUserByUsername(@NonNull String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(
                () -> new UserNotFoundException("User not found with username: " + username));
    }

    public boolean login(String username, String password) {
        try {
            UserEntity user = getUserByUsername(username);
            return passwordEncoder.matches(password, user.getPassword());
        } catch (Exception e) {
            return false;
        }
    }
}
