package com.github.propan.teabuddy.parsers;

import com.github.propan.teabuddy.models.ItemType;
import com.github.propan.teabuddy.models.StoreListItem;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class YunnanSourcingParser extends AbstractShopifyParser {

    private final static String BASE_URL = "https://yunnansourcing.com";

    private static final Logger log = LoggerFactory.getLogger(YunnanSourcingParser.class);

    @Override
    public String getStoreName() {
        return "YunnanSourcing";
    }

    @Override
    public String getStoreUrl() {
        return "https://yunnansourcing.com/collections/new-products";
    }

    @Override
    public List<StoreListItem> extractProducts(Map<String, ShopifyUtils.Product> metadata, Element body) throws DataProcessingException {
        Elements items = body.select(".productitem");
        if (items.isEmpty()) {
            throw new DataProcessingException(String.format("No product information found on the page %s", getStoreUrl()));
        }

        List<StoreListItem> products = new ArrayList<>();

        for (Element item : items) {
            String productId = item.select(".yotpo").attr("data-product-id");
            String imageUrl = Utils.makeAbsoluteUrl(BASE_URL, item.select(".productitem__image-container img").attr("src"));
            String storeUrl = Utils.makeAbsoluteUrl(BASE_URL, item.select(".productitem--title a").attr("href"));

            ShopifyUtils.Product meta = metadata.get(productId);
            if (meta == null) {
                continue;
            }

            ShopifyUtils.Variant bestVariant = meta.getBestVariant();

            StoreListItem product = new StoreListItem(
                    meta.vendor, bestVariant.name, ItemType.fromString(meta.type), storeUrl, imageUrl, bestVariant.getDisplayPrice()
            );
            if (!product.isValid()) {
                continue;
            }

            products.add(product);
        }

        return products;
    }
}
