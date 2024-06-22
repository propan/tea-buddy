package com.github.propan.teabuddy.parsers;

public class Utils {

    public static String makeAbsoluteUrl(String baseUrl, String relativeUrl) {
        return java.net.URI.create(baseUrl).resolve(relativeUrl).toString();
    }

}
