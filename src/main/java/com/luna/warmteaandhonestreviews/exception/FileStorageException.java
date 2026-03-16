package com.luna.warmteaandhonestreviews.exception;

import org.jspecify.annotations.Nullable;

public class FileStorageException extends RuntimeException {

    public FileStorageException(@Nullable String message) {
        super(message);
    }
}
