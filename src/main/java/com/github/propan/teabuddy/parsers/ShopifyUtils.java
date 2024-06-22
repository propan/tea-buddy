package com.github.propan.teabuddy.parsers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ShopifyUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final Pattern pattern = Pattern.compile("var meta = (\\{.*});", Pattern.MULTILINE);

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Variant {
        String id;
        int price;
        String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNormalizedName() {
            return StringUtils.join(Arrays.stream(StringUtils.split(this.name)).map(StringUtils::capitalize).toList(), " ");
        }

        public String getDisplayPrice() {
            return String.format("%d.%02d$", price / 100, price % 100);
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", "{", "}")
                    .add("id='" + id + "'")
                    .add("price=" + price)
                    .add("name='" + name + "'")
                    .toString();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Product {
        String id;
        String vendor;
        String type;
        List<Variant> variants;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getVendor() {
            return vendor;
        }

        public void setVendor(String vendor) {
            this.vendor = vendor;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Variant> getVariants() {
            return variants;
        }

        public void setVariants(List<Variant> variants) {
            this.variants = variants;
        }

        public Variant getBestVariant() {
            return variants.stream().max(Comparator.comparingInt(v -> v.price)).orElse(null);
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", "{", "}")
                    .add("id='" + id + "'")
                    .add("vendor='" + vendor + "'")
                    .add("type='" + type + "'")
                    .add("variants=" + variants)
                    .toString();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class ProductList {
        public List<Product> products;

        public List<Product> getProducts() {
            return products;
        }

        public void setProducts(List<Product> products) {
            this.products = products;
        }
    }

    public static Map<String, Product> extractProductsMeta(Document doc) throws JsonProcessingException {
        for (Element script : doc.head().select("script")) {
            String content = script.html();
            if (content.contains("var meta = ")) {
                final Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {
                    return mapper.readValue(matcher.group(1), ProductList.class).products.stream().collect(Collectors.toMap(Product::getId, p -> p));
                }
            }
        }

        return Map.of();
    }
}
