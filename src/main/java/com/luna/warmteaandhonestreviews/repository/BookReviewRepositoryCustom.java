package com.luna.warmteaandhonestreviews.repository;

import com.luna.warmteaandhonestreviews.domain.BookReviewEntity;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BookReviewRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    public BookReviewRepositoryCustom(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<BookReviewEntity> findReviews(
        @Nullable ObjectId id,
        @Nullable Instant createdAt,
        @NonNull Integer limit
    ) {
        MongoCollection<Document> collection = mongoTemplate.getCollection("book_reviews");
        Document filter = new Document();
        if (id != null && createdAt != null) {
            filter = new Document("$or", List.of(
                new Document("createdAt", new Document("$lt", createdAt)),
                new Document("createdAt", createdAt)
                    .append("_id", new Document("$lt", id))
            ));
        }

        List<Document> docs = collection.find(filter)
            .sort(Sorts.descending("createdAt", "_id"))
            .limit(limit)
            .into(new java.util.ArrayList<>());
        return docs.stream()
            .map(doc -> mongoTemplate.getConverter().read(BookReviewEntity.class, doc))
            .collect(Collectors.toList());
    }
}
