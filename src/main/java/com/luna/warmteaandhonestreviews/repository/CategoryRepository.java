package com.luna.warmteaandhonestreviews.repository;

import com.luna.warmteaandhonestreviews.domain.CategoryEntity;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<CategoryEntity, String> {

    boolean existsByName(@NonNull String name);

    Optional<CategoryEntity> findByName(@NonNull String name);
}
