package com.github.propan.teabuddy.parsers;

import com.github.propan.teabuddy.models.ItemType;
import com.github.propan.teabuddy.models.Store;
import com.github.propan.teabuddy.models.StoreListItem;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class EssenceOfTeaParserTest {

    @Test
    @Tag("integration")
    void integration() throws IOException {
        EssenceOfTeaParser parser = new EssenceOfTeaParser();

        java.net.URL url = URI.create(parser.getStorePages().findFirst().orElse("")).toURL();

        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();

        List<StoreListItem> items = parser.parse(response.toString());

        assertThat(items).isNotEmpty();
    }

    @Test
    void parse() throws URISyntaxException, IOException {
        java.net.URL url = EssenceOfTeaParser.class.getResource("essenceoftea.html");
        assertThat(url).isNotNull();

        java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());

        List<StoreListItem> items = new EssenceOfTeaParser().parse(new String(Files.readAllBytes(resPath)));

        assertIterableEquals(Lists.list(
                new StoreListItem(
                        Store.ESSENCE_OF_TEA,
                        "The Essence of Tea",
                        "180ml Chun Yuan Aged Zini By Ma Yong Qiang",
                        ItemType.TEAWARE,
                        "https://essenceoftea.com/products/180ml-chun-yuan-aged-zini-by-ma-yong-qiang-2",
                        "https://essenceoftea.com/cdn/shop/files/c488bdfa6f923991fae462219edec7e6.jpg?v=1726291898&width=533",
                        "$780.00"
                ),
                new StoreListItem(
                        Store.ESSENCE_OF_TEA,
                        "essenceoftea",
                        "105ml XuBian Aged Zini by Ma Yong Qiang",
                        ItemType.TEAWARE,
                        "https://essenceoftea.com/products/105ml-xubian-aged-zini-by-ma-yong-qiang-1",
                        "https://essenceoftea.com/cdn/shop/files/12_46d3d1d7-003c-4d6f-a90b-0e562ff69a60.jpg?v=1726298900&width=533",
                        "$780.00"
                ),
                new StoreListItem(
                        Store.ESSENCE_OF_TEA,
                        "essenceoftea",
                        "85ml Lianzi - Aged Zini by Ma Yong Qiang",
                        ItemType.TEAWARE,
                        "https://essenceoftea.com/products/85ml-lianzi-aged-zini-by-ma-yong-qiang-1",
                        "https://essenceoftea.com/cdn/shop/files/2624c7c78cf7c0cc2551687c0afb8c27.jpg?v=1726293358&width=533",
                        "$780.00"
                ),
                new StoreListItem(
                        Store.ESSENCE_OF_TEA,
                        "The Essence of Tea",
                        "14cm Yuan Dynasty Bowl/Teapot Stand",
                        ItemType.TEAWARE,
                        "https://essenceoftea.com/products/14cm-yuan-dynasty-bowl-teapot-stand",
                        "https://essenceoftea.com/cdn/shop/files/14cm_a975d3e8-a6c5-4b65-9cd8-6a67114f89a7.jpg?v=1725961181&width=533",
                        "$138.00"
                ),
                new StoreListItem(
                        Store.ESSENCE_OF_TEA,
                        "The Essence of Tea",
                        "15.5cm Song Dynasty Bowl/Teapot Stand",
                        ItemType.TEAWARE,
                        "https://essenceoftea.com/products/15-5cmsong-plate",
                        "https://essenceoftea.com/cdn/shop/files/15.5cm-4_bfc5c8cd-b0fb-461d-b2a8-711c5b180fe1.jpg?v=1725961597&width=533",
                        "$138.00"
                ),
                new StoreListItem(
                        Store.ESSENCE_OF_TEA,
                        "The Essence of Tea",
                        "2005 Fuding Panxi Shou Mei White Tea - 300g",
                        ItemType.WHITE_TEA,
                        "https://essenceoftea.com/products/2005-fuding-panxi-shou-mei-white-tea",
                        "https://essenceoftea.com/cdn/shop/files/2005_f7cad661-95a0-4d8b-a911-7838663647f6.jpg?v=1719563172&width=533",
                        "$240.00"
                ),
                new StoreListItem(
                        Store.ESSENCE_OF_TEA,
                        "The Essence of Tea",
                        "ROC Huaning Cup 11-15ml",
                        ItemType.TEAWARE,
                        "https://essenceoftea.com/products/roc-huaning-cup-11-15ml",
                        "https://essenceoftea.com/cdn/shop/files/11-15ml-2.jpg?v=1722410987&width=533",
                        "$16.00"
                ),
                new StoreListItem(
                        Store.ESSENCE_OF_TEA,
                        "The Essence of Tea",
                        "180ml Qing Dynasty Coral Gaiwan B",
                        ItemType.TEAWARE,
                        "https://essenceoftea.com/products/180ml-qing-dynasty-coral-gaiwan-b",
                        "https://essenceoftea.com/cdn/shop/files/B180ml-2.jpg?v=1724233371&width=533",
                        "$190.00"
                )
        ), items);

    }
}
