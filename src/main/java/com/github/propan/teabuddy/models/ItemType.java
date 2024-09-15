package com.github.propan.teabuddy.models;

public enum ItemType {

    RAW_PUER_TEA,
    RIPE_PUER_TEA,
    WHITE_TEA,
    OOLONG_TEA,
    GREEN_TEA,
    BLACK_TEA,
    YELLOW_TEA,
    DARK_TEA,
    TEAWARE,
    OTHER;

    public static ItemType fromString(String s) {
        String val = s.toLowerCase()
                .replace("puerh", "puer")
                .replace("pu-erh", "puer");

        return switch (val) {
            case "raw puer tea" -> RAW_PUER_TEA;
            case "ripe puer tea" -> RIPE_PUER_TEA;
            case "white tea" -> WHITE_TEA;
            case "oolong tea" -> OOLONG_TEA;
            case "green tea" -> GREEN_TEA;
            case "black tea" -> BLACK_TEA;
            case "yellow tea" -> YELLOW_TEA;
            case "hei cha", "dark tea" -> DARK_TEA;
            case "teaware", "teawares" -> TEAWARE;
            default -> OTHER;
        };
    }

    public static ItemType fromTitle(String s) {
        String val = s.toLowerCase()
                .replace("puerh", "puer")
                .replace("pu-erh", "puer");

        if (val.contains("raw puer tea")) {
            return RAW_PUER_TEA;
        } else if (val.contains("ripe puer tea")) {
            return RIPE_PUER_TEA;
        } else if (val.contains("white tea")) {
            return WHITE_TEA;
        } else if (val.contains("oolong tea")) {
            return OOLONG_TEA;
        } else if (val.contains("green tea")) {
            return GREEN_TEA;
        } else if (val.contains("black tea")) {
            return BLACK_TEA;
        } else if (val.contains("yellow tea")) {
            return YELLOW_TEA;
        } else if (val.contains("hei cha") || val.contains("dark tea")) {
            return DARK_TEA;
        } else if (val.contains("teaware")) {
            return TEAWARE;
        } else {
            return OTHER;
        }
    }
}
