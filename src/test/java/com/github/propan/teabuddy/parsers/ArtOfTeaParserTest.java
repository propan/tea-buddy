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
import java.net.URLConnection;
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

        URLConnection connection = url.openConnection();

        parser.getHeaders().forEach(connection::setRequestProperty);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
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
                        "Шу КАА",
                        ItemType.RIPE_PUER_TEA,
                        "https://artoftea.ru/puer/shu-puer/kaa-2013-g",
                        "https://artoftea.ru/image/cache/catalog/dec24/dsc03536-220x230.jpg",
                        "₽1980.00"
                ),
                new StoreListItem(
                        Store.ART_OF_TEA,
                        "art_of_tea",
                        "Много всего за всего ничего, 6х20 гр.",
                        ItemType.OTHER,
                        "https://artoftea.ru/chaynyye-nabory/mnogo-vsego-za-vsego-nichego",
                        "https://artoftea.ru/image/cache/catalog/daria%20март/DSC04806-220x230.jpg",
                        "₽900.00"
                ),
                new StoreListItem(
                        Store.ART_OF_TEA,
                        "art_of_tea",
                        "Юэ Гуан Бай Белый Лунный Свет",
                        ItemType.WHITE_TEA,
                        "https://artoftea.ru/whitetea/yue-guan-bay-bely-lunny-svet",
                        "https://artoftea.ru/image/cache/catalog/may22/img_7934-220x230.jpg",
                        "₽250.00"
                ),
                new StoreListItem(
                        Store.ART_OF_TEA,
                        "art_of_tea",
                        "Гуй Фэй Наложница Императора",
                        ItemType.OOLONG_TEA,
                        "https://artoftea.ru/oolong/taiwan-oolong/guy-fey-ulun",
                        "https://artoftea.ru/image/cache/catalog/nov23/dsc03857-220x230.jpg",
                        "₽6050.00"
                ),
                new StoreListItem(
                        Store.ART_OF_TEA,
                        "art_of_tea",
                        "Жи Юэ Тань Рубиновый №18",
                        ItemType.OOLONG_TEA,
                        "https://artoftea.ru/taiwan-tea/sun-moon-lake-rubinivyi-18",
                        "https://artoftea.ru/image/cache/catalog/nov23/dsc04838-220x230.jpg",
                        "₽150.00"
                ),
                new StoreListItem(
                        Store.ART_OF_TEA,
                        "art_of_tea",
                        "Набор Ночь Вдвоём s121, стекло, 3 предмета",
                        ItemType.TEAWARE,
                        "https://artoftea.ru/posuda/nabory?product_id=6246",
                        "https://artoftea.ru/image/cache/catalog/mar25/DSC04627%20копия-220x230.jpg",
                        "₽2980.00"
                ),
                new StoreListItem(
                        Store.ART_OF_TEA,
                        "art_of_tea",
                        "Фигурка Кот Мусс 628, глина, 7 см",
                        ItemType.TEAWARE,
                        "https://artoftea.ru/accesories/tea-toys?product_id=6247",
                        "https://artoftea.ru/image/cache/catalog/mar25/DSC04611-220x230.jpg",
                        "₽1700.00"
                ),
                new StoreListItem(
                        Store.ART_OF_TEA,
                        "art_of_tea",
                        "Фигурка Кот Тофу 627, глина, 4 см",
                        ItemType.TEAWARE,
                        "https://artoftea.ru/accesories/tea-toys?product_id=6249",
                        "https://artoftea.ru/image/cache/catalog/mar25/DSC04619-220x230.jpg",
                        "₽1800.00"
                )
        ), items);
    }
}
