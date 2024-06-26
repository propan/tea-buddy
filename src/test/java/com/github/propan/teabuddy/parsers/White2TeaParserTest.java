package com.github.propan.teabuddy.parsers;

import com.github.propan.teabuddy.models.ItemType;
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
import static org.junit.jupiter.api.Assertions.*;

class White2TeaParserTest {

    @Test
    @Tag("integration")
    void integration() throws IOException {
        White2TeaParser parser = new White2TeaParser();

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
        java.net.URL url = White2TeaParser.class.getResource("white2tea.html");
        assertThat(url).isNotNull();

        java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());

        List<StoreListItem> items = new White2TeaParser().parse(new String(Files.readAllBytes(resPath)));

        assertIterableEquals(Lists.list(
                new StoreListItem(
                        "white2tea",
                        "2024 Philtre - 200g",
                        ItemType.RAW_PUER_TEA,
                        "https://white2tea.com/collections/latest-additions/products/2024-philtre",
                        "https://white2tea.com/cdn/shop/files/2024Philtre-Raw-Puer-tea_1600x.jpg?v=1716700025",
                        "46.95$"
                ),
                new StoreListItem(
                        "white2tea",
                        "2024 Philtre Minis - ~7g",
                        ItemType.RAW_PUER_TEA,
                        "https://white2tea.com/collections/latest-additions/products/2024-philtre-minis",
                        "https://white2tea.com/cdn/shop/files/2024PhiltreMinis_1600x.jpg?v=1716699952",
                        "1.95$"
                ),
                new StoreListItem(
                        "white2tea",
                        "2024 Judy - 200g",
                        ItemType.WHITE_TEA,
                        "https://white2tea.com/collections/latest-additions/products/2024-judy",
                        "https://white2tea.com/cdn/shop/files/2024JudyWhiteTea_1600x.jpg?v=1716699819",
                        "73.95$"
                ),
                new StoreListItem(
                        "white2tea",
                        "2024 Judy Minis - ~7g",
                        ItemType.WHITE_TEA,
                        "https://white2tea.com/collections/latest-additions/products/2024-judy-mini",
                        "https://white2tea.com/cdn/shop/files/2024JudyMinis_1600x.jpg?v=1716699726",
                        "2.95$"
                ),
                new StoreListItem(
                        "white2tea",
                        "2024 Teadontlie - 200g",
                        ItemType.RAW_PUER_TEA,
                        "https://white2tea.com/collections/latest-additions/products/2024-teadontlie",
                        "https://white2tea.com/cdn/shop/files/2024Teadontlie-sheng-Puerh-tea_1600x.jpg?v=1716449501",
                        "80.95$"
                ),
                new StoreListItem(
                        "white2tea",
                        "2024 Teadontlie Minis - ~7g",
                        ItemType.RAW_PUER_TEA,
                        "https://white2tea.com/collections/latest-additions/products/2024-teadontlie-minis",
                        "https://white2tea.com/cdn/shop/files/2024TeadontlieMinis_1600x.jpg?v=1716449482",
                        "2.95$"
                ),
                new StoreListItem(
                        "white2tea",
                        "2024 Tihkal - 200g",
                        ItemType.RAW_PUER_TEA,
                        "https://white2tea.com/collections/latest-additions/products/2024-tihkal",
                        "https://white2tea.com/cdn/shop/files/2024TikhalrawPuer-2_1600x.jpg?v=1716449118",
                        "113.95$"
                ),
                new StoreListItem(
                        "white2tea",
                        "2024 Tihkal Minis - ~7g",
                        ItemType.RAW_PUER_TEA,
                        "https://white2tea.com/collections/latest-additions/products/2024-tihkal-minis",
                        "https://white2tea.com/cdn/shop/files/2024TikhalMinis_1600x.jpg?v=1717041162",
                        "4.95$"
                ),
                new StoreListItem(
                        "white2tea",
                        "2024 Penetralia - 200g",
                        ItemType.BLACK_TEA,
                        "https://white2tea.com/collections/latest-additions/products/2024-penetralia",
                        "https://white2tea.com/cdn/shop/files/2024PenetraliaSunDriedBlackTea_1600x.jpg?v=1716446912",
                        "60.95$"
                ),
                new StoreListItem(
                        "white2tea",
                        "2024 Penetralia Minis - ~7g",
                        ItemType.BLACK_TEA,
                        "https://white2tea.com/collections/latest-additions/products/2024-penetralia-minis",
                        "https://white2tea.com/cdn/shop/files/2024PenetraliaMinis_1600x.jpg?v=1716446894",
                        "2.95$"
                )
        ), items);

    }
}
