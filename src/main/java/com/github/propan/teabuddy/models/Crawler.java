package com.github.propan.teabuddy.models;

import com.github.propan.teabuddy.parsers.DataProcessingException;

import java.io.IOException;
import java.util.UUID;

public record Crawler(UUID id, String className) {

    public enum Result {
        SUCCESS,
        REQUEST_TIMEOUT,
        HTTP_ERROR,
        DATA_PROCESSING_ERROR,
        UNKNOWN_ERROR
    }

    public record ExecutionResult(Result result, String details) {

        public static ExecutionResult success(String format, Object... args) {
            return new ExecutionResult(Result.SUCCESS, String.format(format, args));
        }

        public static ExecutionResult from(Exception ex) {
            return switch (ex) {
                case DataProcessingException v -> new ExecutionResult(Result.DATA_PROCESSING_ERROR, v.getMessage());
                case InterruptedException v -> new ExecutionResult(Result.REQUEST_TIMEOUT, v.getMessage());
                case IOException v -> new ExecutionResult(Result.HTTP_ERROR, v.getMessage());
                default -> new ExecutionResult(Result.UNKNOWN_ERROR, ex.getMessage());
            };
        }

    }
}
