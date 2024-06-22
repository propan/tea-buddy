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
            case "teawares" -> TEAWARE;
            default -> OTHER;
        };
    }
}
