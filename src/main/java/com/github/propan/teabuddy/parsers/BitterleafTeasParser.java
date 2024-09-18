package com.github.propan.teabuddy.parsers;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.propan.teabuddy.models.ItemType;
import com.github.propan.teabuddy.models.Store;
import com.github.propan.teabuddy.models.StoreListItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Component
public class BitterleafTeasParser implements StoreParser {

    private static final ObjectMapper mapper = new ObjectMapper();

    private final static String BASE_URL = "https://www.bitterleafteas.com";

    private static final Pattern metaDataPattern = Pattern.compile("window.pysWooProductData\\[\\s?(\\d+)\\s?]\\s?=\\s?(\\{.*\\});", Pattern.MULTILINE);
    private static final Pattern classPattern = Pattern.compile("post-(\\d+)", Pattern.CASE_INSENSITIVE);

    @Override
    public String getStoreName() {
        return "BitterleafTeas";
    }

    @Override
    public Stream<String> getStorePages() {
        return Stream.generate(new Supplier<>() {
            private int page = 1;

            @Override
            public String get() {
                return String.format("%s/page/%d", BASE_URL, page++);
            }
        });
    }

    @Override
    public List<StoreListItem> parse(String htmlBody) throws DataProcessingException {
        Document body = Jsoup.parse(htmlBody);

        Map<String, Item> meta;
        try {
            meta = extractProductsMeta(htmlBody);
        } catch (JsonProcessingException e) {
            throw new DataProcessingException(String.format("Failed to extract products meta from store %s", getStoreName()), e);
        }

        Elements items = body.select(".product");
        if (items.isEmpty()) {
            throw new DataProcessingException(String.format("No product information found on the page %s", getStoreName()));
        }

        List<StoreListItem> products = new ArrayList<>();

        for (Element item : items) {
            String imageUrl = "";

            String productId = "";
            Matcher itemIdMatcher = classPattern.matcher(item.attr("class"));
            if (itemIdMatcher.find()) {
                productId = itemIdMatcher.group(1);
            }

            ItemType itemType = ItemType.OTHER;
            if (!productId.isEmpty()) {
                Item itemMeta = meta.get(productId);
                if (itemMeta != null) {
                    itemType = itemMeta.getItemType();
                }
            }

            String itemName = item.select("h3").text();

            if (ItemType.OTHER.equals(itemType)) {
                itemType = ItemType.fromTitle(itemName);
            }

            Element productImage = item.select(".product-image img").first();
            if (productImage != null) {
                imageUrl = productImage.attr("src");
            }

            String price = "";
            Element productPrice = item.select(".price .amount").last();
            if (productPrice != null) {
                price = productPrice.text();
            }

            StoreListItem product = new StoreListItem(
                    Store.BITTERLEAF_TEAS,
                    "bitterleafteas",
                    itemName,
                    itemType,
                    item.select("a.product-loop-title").attr("href"),
                    imageUrl,
                    price
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProductMeta {
        public GA ga;

        public GA getGa() {
            return ga;
        }

        public void setGa(GA ga) {
            this.ga = ga;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GA {
        private Params params;

        public Params getParams() {
            return params;
        }

        public void setParams(Params params) {
            this.params = params;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Params {
        public List<Item> items;

        public List<Item> getItems() {
            return items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        public String id;
        public String name;
        public int quantity;
        @JsonAlias("item_category")
        public String itemCategory;
        @JsonAlias("item_category2")
        public String itemCategory2;
        @JsonAlias("item_category3")
        public String itemCategory3;
        @JsonAlias("item_category4")
        public String itemCategory4;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getItemCategory() {
            return itemCategory;
        }

        public void setItemCategory(String itemCategory) {
            this.itemCategory = itemCategory;
        }

        public String getItemCategory2() {
            return itemCategory2;
        }

        public void setItemCategory2(String itemCategory2) {
            this.itemCategory2 = itemCategory2;
        }

        public String getItemCategory3() {
            return itemCategory3;
        }

        public void setItemCategory3(String itemCategory3) {
            this.itemCategory3 = itemCategory3;
        }

        public String getItemCategory4() {
            return itemCategory4;
        }

        public void setItemCategory4(String itemCategory4) {
            this.itemCategory4 = itemCategory4;
        }

        public ItemType getItemType() {
            ItemType itemType = ItemType.fromString(itemCategory);
            if (!ItemType.OTHER.equals(itemType)) {
                return itemType;
            }
            itemType = ItemType.fromString(itemCategory2);
            if (!ItemType.OTHER.equals(itemType)) {
                return itemType;
            }
            itemType = ItemType.fromString(itemCategory3);
            if (!ItemType.OTHER.equals(itemType)) {
                return itemType;
            }
            itemType = ItemType.fromString(itemCategory4);
            if (!ItemType.OTHER.equals(itemType)) {
                return itemType;
            }
            return ItemType.OTHER;
        }
    }

    public static Map<String, Item> extractProductsMeta(String doc) throws JsonProcessingException {
        Map<String, Item> metaData = new HashMap<>();

        Matcher matcher = metaDataPattern.matcher(doc);
        while (matcher.find()) {
            ProductMeta product = mapper.readValue(matcher.group(2), ProductMeta.class);
            if (product == null || product.getGa() == null || product.getGa().getParams() == null || product.getGa().getParams().getItems() == null) {
                continue;
            }

            for (Item item : product.getGa().getParams().getItems()) {
                metaData.put(item.id, item);
            }
        }

        return metaData;
    }
}
