package com.github.propan.teabuddy.parsers;

import com.github.propan.teabuddy.models.ItemType;
import com.github.propan.teabuddy.models.Store;
import com.github.propan.teabuddy.models.StoreListItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Component
public class ArtOfTeaParser implements StoreParser {

    private final static String BASE_URL = "https://artoftea.ru";

    private static final Pattern categoryPattern = Pattern.compile("https:\\/\\/artoftea.ru\\/([\\w-]+)(\\/([\\w-]+))?\\/[\\w-]+", Pattern.CASE_INSENSITIVE);

    @Override
    public String getStoreName() {
        return "ArtOfTea";
    }

    @Override
    public Stream<String> getStorePages() {
        return Stream.of(String.format("%s/latest-products", BASE_URL));
    }

    @Override
    public List<StoreListItem> parse(String htmlBody) throws DataProcessingException {
        Document body = Jsoup.parse(htmlBody);

        Elements items = body.select(".products-block .product-layout");
        if (items.isEmpty()) {
            throw new DataProcessingException(String.format("No product information found on the page %s", getStoreName()));
        }

        List<StoreListItem> products = new ArrayList<>();

        for (Element item : items) {
            ItemType itemType = ItemType.OTHER;

            Matcher matcher = categoryPattern.matcher(item.select(".product-thumb__name").attr("href"));
            while (matcher.find()) {
                itemType = categoryToItemType(matcher.group(3) == null ? matcher.group(1) : matcher.group(3));
            }

            float itemPrice = -1;
            List<String> extractedPrices = item.select(".options-category option").eachAttr("data-price");
            for (String price : extractedPrices) {
                try {
                    float val = Float.parseFloat(price);
                    if (val > itemPrice) {
                        itemPrice = val;
                    }
                } catch (Exception ignore) {
                    // ignore this bad value
                }
            }

            if (itemPrice < 0) {
                try {
                    itemPrice = Float.parseFloat(item.select(".product-thumb__price").attr("data-price"));
                } catch (Exception ignore) {
                    // ignore this bad value
                }
            }

            StoreListItem product = new StoreListItem(
                    Store.ART_OF_TEA,
                    "art_of_tea",
                    item.select(".product-thumb__name").text(),
                    itemType,
                    item.select(".product-thumb__name").attr("href"),
                    item.select(".product-thumb__image img").attr("src"),
                    normalizePrice(itemPrice)
            );

            if (!product.isValid()) {
                continue;
            }

            products.add(product);
        }

        if (products.isEmpty()) {
            throw new DataProcessingException(String.format("No products found on the %s store page", getStoreName()));
        }

        return products;
    }

    private String normalizePrice(float price) {
        if (price < 0) {
            return "";
        }

        return "₽" + String.format("%.2f", price);
    }

    private ItemType categoryToItemType(String category) {
        return switch (category) {
            case "whitetea" -> ItemType.WHITE_TEA;
            case "yellowtea" -> ItemType.YELLOW_TEA;
            case "redtea" -> ItemType.BLACK_TEA;
            case "xej-cha-chernyj-chaj" -> ItemType.DARK_TEA;
            case "greentea" -> ItemType.GREEN_TEA;
            case "shu-puer" -> ItemType.RIPE_PUER_TEA;
            case "sheng-puer" -> ItemType.RAW_PUER_TEA;
            case "taiwan-oolong", "taiwan-tea" -> ItemType.OOLONG_TEA;
            case "chahai", "chainiki-isinskaya-glina", "cups", "chaban", "tea-toys", "farfor-iz-tszindechzhen", "posuda", "accesories", "gaiwans" -> ItemType.TEAWARE;
            default -> ItemType.OTHER;
        };
    }
}
