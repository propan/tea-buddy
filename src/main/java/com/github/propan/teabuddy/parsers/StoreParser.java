package com.github.propan.teabuddy.parsers;

import com.github.propan.teabuddy.models.StoreListItem;

import java.util.List;
import java.util.stream.Stream;

public interface StoreParser {
    String getStoreName();

    public Stream<String> getStorePages();

    List<StoreListItem> parse(String htmlBody) throws DataProcessingException;
}
