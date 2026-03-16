package com.luna.warmteaandhonestreviews.dto;

import com.luna.warmteaandhonestreviews.domain.BookReviewEntity;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import org.bson.types.ObjectId;

public record SaveReviewReqDto(String adminUserId,
                               String title,
                               String author,
                               Double rating,
                               Integer page,
                               String language,
                               List<String> categories,
                               LocalDate publishedAt,
                               String excerpt,
                               String coverImage,
                               String contents) {

    public BookReviewEntity toEntity() {
        return new BookReviewEntity(new ObjectId(adminUserId),
            title,
            author,
            rating,
            page,
            language,
            categories,
            publishedAt.atStartOfDay(ZoneOffset.UTC).toInstant(),
            coverImage,
            contents,
            excerpt);
    }
}
