package com.github.propan.teabuddy.parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.propan.teabuddy.models.StoreListItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.Map;

public abstract class AbstractShopifyParser implements StoreParser {

    @Override
    public Map<String, String> getHeaders() {
        return Map.of();
    }

    @Override
    public List<StoreListItem> parse(String htmlBody) throws DataProcessingException {
        Document doc = Jsoup.parse(htmlBody);

        Map<String, ShopifyUtils.Product> metadata;
        try {
            metadata = ShopifyUtils.extractProductsMeta(doc);
        } catch (JsonProcessingException e) {
            throw new DataProcessingException(String.format("Failed to extract products meta from store %s", getStoreName()), e);
        }

        List<StoreListItem> products = extractProducts(metadata, doc.body());

        if (products.isEmpty()) {
            throw new DataProcessingException(String.format("No products found on the %s store page", getStoreName()));
        }

        return products;
    }

    public abstract List<StoreListItem> extractProducts(Map<String, ShopifyUtils.Product> metadata, Element body) throws DataProcessingException;
}
