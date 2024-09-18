package com.github.propan.teabuddy.parsers;

import com.github.propan.teabuddy.models.ItemType;
import com.github.propan.teabuddy.models.Store;
import com.github.propan.teabuddy.models.StoreListItem;
import com.github.propan.teabuddy.service.CrawlerService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Component
public class BaduchaiParser implements StoreParser {

    private static final Logger log = LoggerFactory.getLogger(BaduchaiParser.class);

    private final static String BASE_URL = "https://baduchai.ru";

    private static final Pattern categorysPattern = Pattern.compile("product_cat-([\\w-]+)", Pattern.CASE_INSENSITIVE);

    @Override
    public String getStoreName() {
        return "BaduChai";
    }

    @Override
    public Stream<String> getStorePages() {
        return Stream.generate(new Supplier<>() {
            private int page = 1;

            @Override
            public String get() {
                return String.format("%s/product-category/chaj/page/%d", BASE_URL, page++);
            }
        });
    }

    @Override
    public List<StoreListItem> parse(String htmlBody) throws DataProcessingException {
        Document body = Jsoup.parse(htmlBody);

        Elements items = body.select(".product");
        if (items.isEmpty()) {
            throw new DataProcessingException(String.format("No product information found on the page %s", getStoreName()));
        }

        List<StoreListItem> products = new ArrayList<>();

        for (Element item : items) {
            ItemType itemType = ItemType.OTHER;

            Matcher categorydMatcher = categorysPattern.matcher(item.attr("class"));
            while (categorydMatcher.find()) {
                ItemType t = categoryToItemType(categorydMatcher.group(1));
                if (!ItemType.OTHER.equals(t)) {
                    itemType = t;
                    break;
                } else {
                    log.warn("unknown category: {}", categorydMatcher.group(1));
                }
            }

            if (ItemType.OTHER.equals(itemType)) {
                itemType = ItemType.fromTitle(item.select(".woocommerce-loop-product__title").text());
            }

            StoreListItem product = new StoreListItem(
                    Store.BADUCHAI,
                    "baduchai",
                    item.select(".woocommerce-loop-product__title").text(),
                    itemType,
                    item.select(".woocommerce-LoopProduct-link").attr("href"),
                    item.select(".attachment-woocommerce_thumbnail").attr("src"),
                    item.select(".woocommerce-Price-amount").text()
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

    private ItemType categoryToItemType(String category) {
        return switch (category) {
            case "belyj" -> ItemType.WHITE_TEA;
            case "krasnyj" -> ItemType.BLACK_TEA;
            case "zelenyj" -> ItemType.GREEN_TEA;
            case "shu-puer" -> ItemType.RIPE_PUER_TEA;
            case "shen-puer" -> ItemType.RAW_PUER_TEA;
            case "ulun", "kollekczionnye-utyosnye-uluny", "chetyre-znamenityh-chajnyh-kusta", "tajvanskie-uluny", "te-guan-in", "chzhun-czzu-ho-glubokaya-prozharka" -> ItemType.OOLONG_TEA;
            default -> ItemType.OTHER;
        };
    }
}
