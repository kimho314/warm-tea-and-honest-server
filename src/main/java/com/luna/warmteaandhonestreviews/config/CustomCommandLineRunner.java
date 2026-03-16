package com.luna.warmteaandhonestreviews.config;

import com.luna.warmteaandhonestreviews.auth.RoleEnum;
import com.luna.warmteaandhonestreviews.domain.UserEntity;
import com.luna.warmteaandhonestreviews.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomCommandLineRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomCommandLineRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByUsername("NilKim")) {
            userRepository.deleteAll();
            UserEntity user1 = new UserEntity(
                "NilKim",
                passwordEncoder.encode("1234"),
                RoleEnum.ADMIN.getRole()
            );
            userRepository.save(user1);
        }
    }
}
