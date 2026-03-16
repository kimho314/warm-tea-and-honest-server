package com.luna.warmteaandhonestreviews.repository;

import com.luna.warmteaandhonestreviews.domain.BookReviewEntity;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookReviewRepository extends MongoRepository<BookReviewEntity, String> {

    Page<BookReviewEntity> findAllByAdminUserId(@NonNull ObjectId adminUserId,
        @NonNull Pageable pageable);

    Optional<BookReviewEntity> findByAdminUserIdAndId(@NonNull ObjectId adminUserId,
        @NonNull ObjectId id);

    Optional<BookReviewEntity> findByTitle(@NonNull String title);
}
