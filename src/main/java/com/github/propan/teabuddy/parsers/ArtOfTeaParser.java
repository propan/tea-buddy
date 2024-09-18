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

    private final static String BASE_URL = "https://artoftea.ru/latest-products/";

    private static final Pattern categoryPattern = Pattern.compile("https:\\/\\/artoftea.ru\\/([\\w-]+)(\\/([\\w-]+))?\\/[\\w-]+", Pattern.CASE_INSENSITIVE);

    @Override
    public String getStoreName() {
        return "ArtOfTea";
    }

    @Override
    public Stream<String> getStorePages() {
        return Stream.generate(new Supplier<>() {
            private int page = 1;

            @Override
            public String get() {
                return String.format("%s/latest-products/?page=%d", BASE_URL, page++);
            }
        });
    }

    @Override
    public List<StoreListItem> parse(String htmlBody) throws DataProcessingException {
        Document body = Jsoup.parse(htmlBody);

        Elements items = body.select(".product-thumb");
        if (items.isEmpty()) {
            throw new DataProcessingException(String.format("No product information found on the page %s", getStoreName()));
        }

        List<StoreListItem> products = new ArrayList<>();

        for (Element item : items) {
            ItemType itemType = ItemType.OTHER;

            Matcher matcher = categoryPattern.matcher(item.select(".name a").attr("href"));
            while (matcher.find()) {
                itemType = categoryToItemType(matcher.group(3) == null ? matcher.group(1) : matcher.group(3));
            }

            StoreListItem product = new StoreListItem(
                    Store.ART_OF_TEA,
                    "art_of_tea",
                    item.select("h4.name").text(),
                    itemType,
                    item.select(".name a").attr("href"),
                    item.select(".image img").attr("src"),
                    normalizePrice(item.select(".price").text())
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

    private String normalizePrice(String price) {
        if (price == null) {
            return "";
        }
        price = price.replace(" ", "");

        if (!price.endsWith("р.")) {
            return price;
        }

        price = "₽" + price.substring(0, price.length() - 2);
        if (price.contains(".")) {
            return price;
        }

        return price + ".00";
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
            case "taiwan-oolong" -> ItemType.OOLONG_TEA;
            case "chahai", "chainiki-isinskaya-glina", "cups", "chaban" -> ItemType.TEAWARE;
            default -> ItemType.OTHER;
        };
    }
}
