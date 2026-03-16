package com.luna.warmteaandhonestreviews.repository;


import static org.assertj.core.api.Assertions.assertThat;

import com.luna.warmteaandhonestreviews.auth.RoleEnum;
import com.luna.warmteaandhonestreviews.config.MongoDBConfig;
import com.luna.warmteaandhonestreviews.domain.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.context.annotation.Import;

@Import(MongoDBConfig.class)
@DataMongoTest
public class UserRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(UserRepositoryTest.class);
    @Autowired
    UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void saveTest() {
        // given
        UserEntity userEntity = new UserEntity("hoseopi", "1234", RoleEnum.ADMIN.name());

        // when
        UserEntity saved = userRepository.save(userEntity);

        // then
        assertThat(saved).isNotNull();
        log.info("saved={}", saved);
        assertThat(saved.getUsername()).isEqualTo("hoseopi");
        assertThat(saved.getPassword()).isEqualTo("1234");
    }
}
