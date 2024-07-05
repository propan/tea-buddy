package com.github.propan.teabuddy.parsers;

import com.github.propan.teabuddy.models.ItemType;
import com.github.propan.teabuddy.models.Store;
import com.github.propan.teabuddy.models.StoreListItem;
import org.apache.commons.lang3.StringUtils;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Component
public class White2TeaParser extends AbstractShopifyParser {

    private final static String BASE_URL = "https://white2tea.com";

    private static final Logger log = LoggerFactory.getLogger(White2TeaParser.class);

    @Override
    public String getStoreName() {
        return "white2tea";
    }

    @Override
    public Stream<String> getStorePages() {
        return Stream.generate(new Supplier<>() {
            private int page = 1;

            @Override
            public String get() {
                return String.format("%s/collections/latest-additions?page=%d", BASE_URL, page++);
            }
        });
    }

    @Override
    public List<StoreListItem> extractProducts(Map<String, ShopifyUtils.Product> metadata, Element body) throws DataProcessingException {
        Elements items = body.select("div.product-wrap");
        if (items.isEmpty()) {
            throw new DataProcessingException(String.format("No product information found on the page %s", getStorePages()));
        }

        List<StoreListItem> products = new ArrayList<>();

        for (Element item : items) {
            if (item.parent() == null) {
                continue;
            }

            String productId = Arrays.
                    stream(StringUtils.split(item.parent().attribute("class").getValue(), " "))
                    .map(StringUtils::trim)
                    .filter(s -> s.startsWith("product-"))
                    .map(s -> StringUtils.stripStart(s, "product-"))
                    .findFirst()
                    .orElse("");

            String imageUrl = Utils.makeAbsoluteUrl(BASE_URL, item.select(".image-element__wrap img").attr("data-src"));
            String storeUrl = Utils.makeAbsoluteUrl(BASE_URL, item.select("a.product-info__caption").attr("href"));

            ShopifyUtils.Product meta = metadata.get(productId);
            if (meta == null) {
                continue;
            }

            ShopifyUtils.Variant bestVariant = meta.getBestVariant();

            StoreListItem product = new StoreListItem(
                    Store.WHITE2TEA,
                    meta.vendor,
                    bestVariant.getNormalizedName(),
                    ItemType.fromString(meta.type),
                    storeUrl,
                    imageUrl,
                    bestVariant.getDisplayPrice()
            );

            if (!product.isValid()) {
                continue;
            }

            products.add(product);
        }

        return products;
    }

}
