package com.github.propan.teabuddy.parsers;

import com.github.propan.teabuddy.models.ItemType;
import com.github.propan.teabuddy.models.Store;
import com.github.propan.teabuddy.models.StoreListItem;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Component
public class EssenceOfTeaParser extends AbstractShopifyParser {

    private final static String BASE_URL = "https://essenceoftea.com";

    private static final Pattern productIdPattern = Pattern.compile(".+-(\\d+)", Pattern.CASE_INSENSITIVE);

    @Override
    public String getStoreName() {
        return "EssenceOfTea";
    }

    @Override
    public Stream<String> getStorePages() {
        return Stream.generate(new Supplier<>() {
            private int page = 1;

            @Override
            public String get() {
                return String.format("%s/collections/new-products?page=%d", BASE_URL, page++);
            }
        });
    }

    @Override
    public List<StoreListItem> extractProducts(Map<String, ShopifyUtils.Product> metadata, Element body) throws DataProcessingException {
        Elements items = body.select(".product-card-wrapper");
        if (items.isEmpty()) {
            throw new DataProcessingException(String.format("No product information found on the page %s", getStoreName()));
        }

        List<StoreListItem> products = new ArrayList<>();

        for (Element item : items) {
            String productId = "";

            Matcher matcher = productIdPattern.matcher(item.select(".card__heading").attr("id"));
            if (matcher.find()) {
                productId = matcher.group(1);
            }

            String imageUrl = Utils.makeAbsoluteUrl(BASE_URL, item.select(".motion-reduce").attr("src"));
            String storeUrl = Utils.makeAbsoluteUrl(BASE_URL, item.select(".card__heading a").attr("href"));

            ShopifyUtils.Product meta = metadata.get(productId);
            if (meta == null) {
                continue;
            }

            ShopifyUtils.Variant bestVariant = meta.getBestVariant();

            StoreListItem product = new StoreListItem(
                    Store.ESSENCE_OF_TEA,
                    meta.vendor,
                    bestVariant.name.trim(),
                    detectItemType(meta.type, bestVariant.name),
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

    public ItemType detectItemType(String itemType, String itemName) {
        String v = itemType.trim().toLowerCase();
        String n = itemName.trim().toLowerCase();
        return switch (v) {
            case "yixing teapot", "coffee & tea cup", "tea tray", "teacups", "gaiwan" -> ItemType.TEAWARE;
            case "red tea" -> ItemType.BLACK_TEA;
            case "puerh tea" -> n.contains("shou") ? ItemType.RIPE_PUER_TEA : ItemType.RAW_PUER_TEA;
            case "liu bao" -> ItemType.DARK_TEA;
            default -> ItemType.fromTitle(n);
        };
    }
}
