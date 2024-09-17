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
                        Store.WHITE2TEA,
                        "white2tea",
                        "2024 Anzac - 200g",
                        ItemType.RAW_PUER_TEA,
                        "https://white2tea.com/collections/latest-additions/products/2024-anzac",
                        "https://white2tea.com/cdn/shop/files/2024AnzacHuangpian.jpg?v=1719468591",
                        "$16.95"
                ),
                new StoreListItem(
                        Store.WHITE2TEA,
                        "white2tea",
                        "2024 Anzac Minis - ~7g",
                        ItemType.RAW_PUER_TEA,
                        "https://white2tea.com/collections/latest-additions/products/2024-anzac-minis",
                        "https://white2tea.com/cdn/shop/files/2024AnzacMinis-huangpian.jpg?v=1719468586",
                        "$0.95"
                ),
                new StoreListItem(
                        Store.WHITE2TEA,
                        "white2tea",
                        "2024 Bellwether - 200g",
                        ItemType.RAW_PUER_TEA,
                        "https://white2tea.com/collections/latest-additions/products/2024-bellwether",
                        "https://white2tea.com/cdn/shop/files/2024-Bellwether-raw-Puer-Tea.jpg?v=1719468152",
                        "$108.95"
                ),
                new StoreListItem(
                        Store.WHITE2TEA,
                        "white2tea",
                        "2024 Bellwether Minis - ~7g",
                        ItemType.RAW_PUER_TEA,
                        "https://white2tea.com/collections/latest-additions/products/2024-bellwether-minis",
                        "https://white2tea.com/cdn/shop/files/2024BellwetherMinis.jpg?v=1719468161",
                        "$4.95"
                ),
                new StoreListItem(
                        Store.WHITE2TEA,
                        "white2tea",
                        "2024 All Fine, All Crime - 200g",
                        ItemType.RAW_PUER_TEA,
                        "https://white2tea.com/collections/latest-additions/products/2024-all-fine-all-crime",
                        "https://white2tea.com/cdn/shop/files/2024-2024Allfineallcrime-sheng-puer-Tea.jpg?v=1719467686",
                        "$78.95"
                ),
                new StoreListItem(
                        Store.WHITE2TEA,
                        "white2tea",
                        "2024 All Fine, All Crime Minis - ~7g",
                        ItemType.RAW_PUER_TEA,
                        "https://white2tea.com/collections/latest-additions/products/2024-all-fine-all-crime-minis",
                        "https://white2tea.com/cdn/shop/files/2024-AllfineallcrimeminisTea.jpg?v=1719467683",
                        "$2.95"
                ),
                new StoreListItem(
                        Store.WHITE2TEA,
                        "white2tea",
                        "2024 Green Hype - 200g",
                        ItemType.RAW_PUER_TEA,
                        "https://white2tea.com/collections/latest-additions/products/2024-green-hype",
                        "https://white2tea.com/cdn/shop/files/2024GreenHype.jpg?v=1719467293",
                        "$33.95"
                ),
                new StoreListItem(
                        Store.WHITE2TEA,
                        "white2tea",
                        "2024 Green Hype Minis - ~7g",
                        ItemType.RAW_PUER_TEA,
                        "https://white2tea.com/collections/latest-additions/products/2024-green-hype-minis",
                        "https://white2tea.com/cdn/shop/files/2024-Tea.jpg?v=1719467282",
                        "$1.95"
                )
        ), items);

    }
}
