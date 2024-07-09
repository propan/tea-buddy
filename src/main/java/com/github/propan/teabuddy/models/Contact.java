package com.github.propan.teabuddy.models;

public record Contact(String name, String email) {
    public static Contact of(String name, String email) {
        return new Contact(name, email);
    }
}
