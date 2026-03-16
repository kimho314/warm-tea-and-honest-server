package com.luna.warmteaandhonestreviews.repository;

import com.luna.warmteaandhonestreviews.domain.UserEntity;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {

    Optional<UserEntity> findByUsername(@NonNull String username);

    boolean existsByUsername(@NonNull String username);
}
