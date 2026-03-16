package com.luna.warmteaandhonestreviews;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Base64Test {

    private static final Logger log = LoggerFactory.getLogger(Base64Test.class);

    @Test
    void encodeTest() {
        String str = "NilKim:1234";
        // Get the bytes of the string (using a specific charset is good practice)
        byte[] originalBytes = str.getBytes(StandardCharsets.UTF_8);

        // Get the Base64 encoder
        Base64.Encoder encoder = Base64.getEncoder();

        // Encode the bytes to a Base64 string
        String encodedString = encoder.encodeToString(originalBytes);

        log.info("encodedString={}", encodedString);
    }
}
