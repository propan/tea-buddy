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

class ArtOfTeaParserTest {

    @Test
    @Tag("integration")
    void integration() throws IOException {
        ArtOfTeaParser parser = new ArtOfTeaParser();

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
        java.net.URL url = ArtOfTeaParser.class.getResource("artoftea.html");
        assertThat(url).isNotNull();

        java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());

        List<StoreListItem> items = new ArtOfTeaParser().parse(new String(Files.readAllBytes(resPath)));

        assertIterableEquals(Lists.list(
                new StoreListItem(
                        Store.ART_OF_TEA,
                        "art_of_tea",
                        "Классика шу: Мэнхай + Линьцан, 2 блина по 100 гр.",
                        ItemType.RIPE_PUER_TEA,
                        "https://artoftea.ru/puer/shu-puer/klassika-shu-menhay-i-lintsan-2-blina-po-100-g",
                        "https://artoftea.ru/image/cache/catalog/may24/dsc09464-239x239.jpg",
                        "₽1550.00"
                ),
                new StoreListItem(
                        Store.ART_OF_TEA,
                        "art_of_tea",
                        "Юэ Гуан Бай Белый Лунный Свет, 2024 г.",
                        ItemType.WHITE_TEA,
                        "https://artoftea.ru/whitetea/yue-guan-bay-bely-lunny-svet",
                        "https://artoftea.ru/image/cache/catalog/may22/img_7934-239x239.jpg",
                        "₽500.00"
                ),
                new StoreListItem(
                        Store.ART_OF_TEA,
                        "art_of_tea",
                        "Цзюнь Шань Мао Цзянь, весна 2024 г.",
                        ItemType.GREEN_TEA,
                        "https://artoftea.ru/greentea/tszyun-shan-mao-tszyan-vesna-2024-g",
                        "https://artoftea.ru/image/cache/catalog/may24/dsc09400-239x239.jpg",
                        "₽1080.00"
                ),
                new StoreListItem(
                        Store.ART_OF_TEA,
                        "art_of_tea",
                        "Юн Чжень \u200EКрасный Бык, 2023 г.",
                        ItemType.RIPE_PUER_TEA,
                        "https://artoftea.ru/puer/shu-puer/yun-chzhen-krasny-byk-2022-g-blin-357-gr",
                        "https://artoftea.ru/image/cache/catalog/may22/img_7800-239x239.jpg",
                        "₽2950.00"
                ),
                new StoreListItem(
                        Store.ART_OF_TEA,
                        "art_of_tea",
                        "Сы Цзы Чунь",
                        ItemType.OOLONG_TEA,
                        "https://artoftea.ru/oolong/taiwan-oolong/sy-tszy-chun",
                        "https://artoftea.ru/image/cache/catalog/feb20/img_1474-239x239.jpg",
                        "₽780.00"
                ),
                new StoreListItem(
                        Store.ART_OF_TEA,
                        "art_of_tea",
                        "Лю Бао Хэй Ча, 2006 г.",
                        ItemType.DARK_TEA,
                        "https://artoftea.ru/xej-cha-chernyj-chaj/lyu-bao-hey-cha-2006-g",
                        "https://artoftea.ru/image/cache/catalog/may24/dsc09341-239x239.jpg",
                        "₽1350.00"
                ),
                new StoreListItem(
                        Store.ART_OF_TEA,
                        "art_of_tea",
                        "Гунтин в мандарине Сяо Цин Ган Синь Хуэй, 2019 г.",
                        ItemType.RIPE_PUER_TEA,
                        "https://artoftea.ru/puer/shu-puer/guntin-v-mandarine-syao-tsin-gan-sin-huey-2019-g",
                        "https://artoftea.ru/image/cache/catalog/may24/dsc09246-239x239.jpg",
                        "₽680.00"
                ),
                new StoreListItem(
                        Store.ART_OF_TEA,
                        "art_of_tea",
                        "Ми Сян Цзинь Я, весна 2024 г.",
                        ItemType.BLACK_TEA,
                        "https://artoftea.ru/redtea/mi-syan-tszin-ya-vesna-2023-g",
                        "https://artoftea.ru/image/cache/catalog/apr23/img_9860-239x239.jpg",
                        "₽940.00"
                )
        ), items);
    }
}
