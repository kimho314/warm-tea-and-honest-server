package com.luna.warmteaandhonestreviews.dto;

import com.luna.warmteaandhonestreviews.domain.CategoryEntity;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public record CategoryDto(String id, String name, LocalDateTime createdAt) {

    public static CategoryDto of(CategoryEntity categoryEntity) {
        return new CategoryDto(
            categoryEntity.getId().toString(),
            categoryEntity.getName(),
            LocalDateTime.ofInstant(categoryEntity.getCreatedAt(), ZoneOffset.UTC)
        );
    }
}
