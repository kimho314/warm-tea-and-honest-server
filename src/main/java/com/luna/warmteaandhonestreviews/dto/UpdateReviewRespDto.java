package com.luna.warmteaandhonestreviews.dto;

import com.luna.warmteaandhonestreviews.domain.BookReviewEntity;

public record UpdateReviewRespDto(String id) {

    public static UpdateReviewRespDto of(BookReviewEntity bookReviewEntity) {
        return new UpdateReviewRespDto(bookReviewEntity.getId().toString());
    }
}
