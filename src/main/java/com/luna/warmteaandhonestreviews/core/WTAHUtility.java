package com.luna.warmteaandhonestreviews.core;

import java.util.Arrays;
import java.util.List;
import org.jspecify.annotations.NonNull;
import tools.jackson.databind.ObjectMapper;

public class WTAHUtility {

    public static @NonNull List<String> convertCategoryJsonToList(@NonNull String categoryJson) {
        ObjectMapper mapper = new ObjectMapper();
        return Arrays.stream(mapper.readValue(categoryJson, String[].class))
            .toList();
    }

    public static String slugify(String title) {
        if (title == null || title.isBlank()) {
            return "";
        }

        return title
            .trim()
            .toLowerCase()
            .replaceAll("[^a-z0-9]+", "-")
            .replaceAll("^-+|-+$", "");
    }
}
