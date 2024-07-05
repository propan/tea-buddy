package com.github.propan.teabuddy.models;

import java.util.UUID;

public record Crawler(UUID id, String className) {

    public enum Result {
        SUCCESS,
        REQUEST_TIMEOUT,
        HTTP_ERROR,
        DATA_PROCESSING_ERROR
    }

    public record ExecutionResult(Result result, String details) {}
}
