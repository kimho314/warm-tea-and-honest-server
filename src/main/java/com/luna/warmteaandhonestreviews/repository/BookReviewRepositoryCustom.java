package com.luna.warmteaandhonestreviews.repository;

import com.luna.warmteaandhonestreviews.domain.BookReviewEntity;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
            .into(new ArrayList<>());
        return docs.stream()
            .map(doc -> mongoTemplate.getConverter().read(BookReviewEntity.class, doc))
            .collect(Collectors.toList());
    }

    public Page<BookReviewEntity> getBookReviews(String category, Pageable pageable) {
        Query query = new Query();

        if (category != null && !category.isBlank()) {
            query.addCriteria(Criteria.where("categories").in(category));
        }
        long total = mongoTemplate.count(query, BookReviewEntity.class);
        query.with(pageable);
        List<BookReviewEntity> reviews = mongoTemplate.find(query, BookReviewEntity.class);

        return new PageImpl<>(reviews, pageable, total);
    }

    public BookReviewEntity update(
        @NonNull String id,
        @Nullable String title,
        @Nullable String author,
        @Nullable Double rating,
        @Nullable String contents,
        @Nullable String publishedAt,
        @Nullable Integer page,
        @Nullable String language,
        @Nullable String excerpt,
        @Nullable String imageUrl
    ) {
        Query query = new Query(Criteria.where("_id").is(new ObjectId(id)));
        Update update = new Update();
        if (title != null) {
            update.set("title", title);
        }
        if (author != null) {
            update.set("author", author);
        }
        if (rating != null) {
            update.set("rating", rating);
        }
        if (contents != null) {
            update.set("contents", contents);
        }
        if (publishedAt != null) {
            update.set("publishedAt",
                LocalDate.parse(publishedAt).atStartOfDay().toInstant(ZoneOffset.UTC));
        }
        if (page != null) {
            update.set("page", page);
        }
        if (language != null) {
            update.set("language", language);
        }
        if (excerpt != null) {
            update.set("excerpt", excerpt);
        }
        if (imageUrl != null) {
            update.set("imageUrl", imageUrl);
        }

        return mongoTemplate.findAndModify(
            query,
            update,
            FindAndModifyOptions.options().returnNew(true),
            BookReviewEntity.class
        );
    }
}
