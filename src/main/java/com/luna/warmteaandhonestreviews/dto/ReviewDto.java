package com.luna.warmteaandhonestreviews.dto;

import com.luna.warmteaandhonestreviews.domain.BookReviewEntity;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

public record ReviewDto(String id,
                        String adminUserId,
                        String title,
                        String author,
                        Double rating,
                        Integer page,
                        String language,
                        List<String> categories,
                        LocalDate publishedAt,
                        LocalDate createdAt,
                        String coverImage,
                        String excerpt,
                        String content) {

    public static ReviewDto of(BookReviewEntity bookReviewEntity) {
        return new ReviewDto(bookReviewEntity.getId().toString(),
            bookReviewEntity.getAdminUserId().toString(),
            bookReviewEntity.getTitle(),
            bookReviewEntity.getAuthor(),
            bookReviewEntity.getRating(),
            bookReviewEntity.getPage(),
            bookReviewEntity.getLanguage(),
            bookReviewEntity.getCategories(),
            LocalDate.ofInstant(bookReviewEntity.getPublishedAt(), ZoneOffset.UTC),
            LocalDate.ofInstant(bookReviewEntity.getCreatedAt(), ZoneOffset.UTC),
            bookReviewEntity.getCoverImage(),
            bookReviewEntity.getExcerpt(),
            bookReviewEntity.getContents());
    }

}
