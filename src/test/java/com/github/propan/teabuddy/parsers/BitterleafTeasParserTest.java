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

class BitterleafTeasParserTest {

    @Test
    @Tag("integration")
    void integration() throws IOException {
        BitterleafTeasParser parser = new BitterleafTeasParser();

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
        java.net.URL url = BitterleafTeasParser.class.getResource("bitterleafteas.html");
        assertThat(url).isNotNull();

        java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());

        List<StoreListItem> items = new BitterleafTeasParser().parse(new String(Files.readAllBytes(resPath)));

        assertIterableEquals(Lists.list(
                new StoreListItem(
                        Store.BITTERLEAF_TEAS,
                        "bitterleafteas",
                        "White Out: 2024 Yunnan White Tea Set",
                        ItemType.WHITE_TEA,
                        "https://www.bitterleafteas.com/shop/tea/non-puer/white-out-2024-yunnan-white-tea-set",
                        "https://www.bitterleafteas.com/wp-content/uploads/2024/09/white-out-2024-yunnan-white-tea-set-all-500x333.jpg",
                        "73.00$"
                ),
                new StoreListItem(
                        Store.BITTERLEAF_TEAS,
                        "bitterleafteas",
                        "Bright & Stormy 2024 Spring Kunlu Shan White Tea",
                        ItemType.WHITE_TEA,
                        "https://www.bitterleafteas.com/shop/tea/non-puer/bright-stormy-2024-spring-kunlu-shan-white-tea",
                        "https://www.bitterleafteas.com/wp-content/uploads/2024/09/bright-stormy-2024-spring-kunlu-shan-white-tea-1-500x333.jpg",
                        "129.00$"
                ),
                new StoreListItem(
                        Store.BITTERLEAF_TEAS,
                        "bitterleafteas",
                        "Impress Forcibly Through Unexpectedness 2024 Spring Wuliang Shan Baimudan White Tea",
                        ItemType.WHITE_TEA,
                        "https://www.bitterleafteas.com/shop/tea/non-puer/impress-forcibly-through-unexpectedness-2024-spring-wuliang-shan-baimudan-white-tea",
                        "https://www.bitterleafteas.com/wp-content/uploads/2024/09/imprsess-forcibly-through-unexpectedness-2024-spring-wuliang-shan-white-tea-1-500x333.jpg",
                        "153.00$"
                ),
                new StoreListItem(
                        Store.BITTERLEAF_TEAS,
                        "bitterleafteas",
                        "Skinny Dip 2024 Spring Jinggu White Tea",
                        ItemType.WHITE_TEA,
                        "https://www.bitterleafteas.com/shop/tea/non-puer/skinny-dip-2024-spring-jinggu-white-tea",
                        "https://www.bitterleafteas.com/wp-content/uploads/2024/09/skinny-dip-2024-spring-jinggu-white-tea-1-500x333.jpg",
                        "81.00$"
                ),
                new StoreListItem(
                        Store.BITTERLEAF_TEAS,
                        "bitterleafteas",
                        "Chameleon Soda Ash Wood Fired Gaiwan II",
                        ItemType.TEAWARE,
                        "https://www.bitterleafteas.com/shop/teaware/gaiwan/chameleon-soda-ash-wood-fired-gaiwan-ii",
                        "https://www.bitterleafteas.com/wp-content/uploads/2024/09/chameleon-soda-ash-wood-fired-gaiwan-2-8-500x333.jpg",
                        "140.00$"
                ),
                new StoreListItem(
                        Store.BITTERLEAF_TEAS,
                        "bitterleafteas",
                        "Chameleon Soda Ash Wood Fired Gaiwan III",
                        ItemType.TEAWARE,
                        "https://www.bitterleafteas.com/shop/teaware/gaiwan/chameleon-soda-ash-wood-fired-gaiwan-iii",
                        "https://www.bitterleafteas.com/wp-content/uploads/2024/09/chameleon-soda-ash-wood-fired-gaiwan-3-12-500x333.jpg",
                        "140.00$"
                )
        ), items);
    }
}
