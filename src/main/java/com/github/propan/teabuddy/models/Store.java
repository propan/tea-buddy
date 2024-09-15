package com.github.propan.teabuddy.models;

public enum Store {

    WHITE2TEA("White2Tea"),
    YUNNAN_SOURCING("Yunnan Sourcing"),
    BITTERLEAF_TEAS("Bitterleaf Teas");

    private final String name;

    Store(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
