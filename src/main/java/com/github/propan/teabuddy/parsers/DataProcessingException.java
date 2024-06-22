package com.github.propan.teabuddy.parsers;

import java.io.IOException;

public class DataProcessingException extends IOException {
    public DataProcessingException(String message) {
        super(message);
    }

    public DataProcessingException(String message, Exception e) {
        super(message, e);
    }
}
