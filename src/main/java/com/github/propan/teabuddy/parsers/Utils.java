package com.github.propan.teabuddy.parsers;

import java.util.regex.Pattern;

public class Utils {

    private static final Pattern widthPattern = Pattern.compile("&width=\\d+");

    public static String makeAbsoluteUrl(String baseUrl, String relativeUrl) {
        return java.net.URI.create(baseUrl).resolve(relativeUrl).toString();
    }

    public static String cleanImageLink(String url) {
        if (url == null) {
            return null;
        }
        return widthPattern.matcher(url).replaceAll("");
    }
}
