package com.luna.warmteaandhonestreviews.core;

import java.util.Arrays;
import java.util.List;
import org.jspecify.annotations.NonNull;
import tools.jackson.databind.ObjectMapper;

public class JsonUtility {

    public static @NonNull List<String> convertCategoryJsonToList(@NonNull String categoryJson) {
        ObjectMapper mapper = new ObjectMapper();
        return Arrays.stream(mapper.readValue(categoryJson, String[].class))
            .toList();
    }
}
