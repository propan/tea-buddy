package com.github.propan.teabuddy.parsers;

import com.github.propan.teabuddy.models.StoreListItem;

import java.util.List;

public interface StoreParser {
    String getStoreName();
    String getStoreUrl();
    List<StoreListItem> parse(String htmlBody) throws DataProcessingException;
}
