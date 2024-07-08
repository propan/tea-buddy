package com.github.propan.teabuddy.models;

public record Recipient(String name, String email) {
    public static Recipient of(String name, String email) {
        return new Recipient(name, email);
    }
}
