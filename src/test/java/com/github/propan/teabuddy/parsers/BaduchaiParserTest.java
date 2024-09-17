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

class BaduchaiParserTest {

    @Test
    @Tag("integration")
    void integration() throws IOException {
        BaduchaiParser parser = new BaduchaiParser();

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
        java.net.URL url = BaduchaiParser.class.getResource("baduchai.html");
        assertThat(url).isNotNull();

        java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());

        List<StoreListItem> items = new BaduchaiParser().parse(new String(Files.readAllBytes(resPath)));

        assertIterableEquals(Lists.list(
                new StoreListItem(
                        Store.BADUCHAI,
                        "baduchai",
                        "Блин шу пу эра НЮ 牛熟普洱饼",
                        ItemType.RIPE_PUER_TEA,
                        "https://baduchai.ru/product/blin-shu-pu-era-nyu-%e7%89%9b%e7%86%9f%e6%99%ae%e6%b4%b1%e9%a5%bc/",
                        "https://baduchai.ru/wp-content/uploads/2024/09/00035562-300x300.jpg",
                        "$35.00"
                ),
                new StoreListItem(
                        Store.BADUCHAI,
                        "baduchai",
                        "Блин Шен Пу Эра Гу Шу Му Шу 古树母树生饼",
                        ItemType.RAW_PUER_TEA,
                        "https://baduchai.ru/product/blin-shen-pu-era-gu-shu-mu-shu-%e5%8f%a4%e6%a0%91%e6%af%8d%e6%a0%91%e7%94%9f%e9%a5%bc/",
                        "https://baduchai.ru/wp-content/uploads/2024/06/00032703-300x300.jpg",
                        "$45.00"
                ),
                new StoreListItem(
                        Store.BADUCHAI,
                        "baduchai",
                        "Блин шу пу эра Жаба 金蟾普洱饼",
                        ItemType.RIPE_PUER_TEA,
                        "https://baduchai.ru/product/blin-shu-pu-era-zhaba-%e9%87%91%e8%9f%be%e6%99%ae%e6%b4%b1%e9%a5%bc/",
                        "https://baduchai.ru/wp-content/uploads/2024/05/00030737-300x300.jpg",
                        "$40.00"
                ),
                new StoreListItem(
                        Store.BADUCHAI,
                        "baduchai",
                        "Да Хун Пао кирпич 2006 года 大红袍砖",
                        ItemType.OOLONG_TEA,
                        "https://baduchai.ru/product/da-hun-pao-kirpich-2006-goda-%e5%a4%a7%e7%ba%a2%e8%a2%8d%e7%a0%96/",
                        "https://baduchai.ru/wp-content/uploads/2023/04/00029339-300x300.jpg",
                        "$26.00"
                ),
                new StoreListItem(
                        Store.BADUCHAI,
                        "baduchai",
                        "Блин шу пуэра Пи Сю 貔貅普洱饼",
                        ItemType.RIPE_PUER_TEA,
                        "https://baduchai.ru/product/blin-shu-puera-pi-syu-%e8%b2%94%e8%b2%85%e6%99%ae%e6%b4%b1%e9%a5%bc/",
                        "https://baduchai.ru/wp-content/uploads/2024/02/00026142-300x300.jpg",
                        "$35.00"
                ),
                new StoreListItem(
                        Store.BADUCHAI,
                        "baduchai",
                        "Блин Шу Пу Эра Чжун Куй 钟馗熟饼",
                        ItemType.RIPE_PUER_TEA,
                        "https://baduchai.ru/product/blin-shu-puera-chzhun-kuj-%e9%92%9f%e9%a6%97%e7%86%9f%e9%a5%bc/",
                        "https://baduchai.ru/wp-content/uploads/2023/12/00025062-300x300.jpg",
                        "$60.00"
                ),
                new StoreListItem(
                        Store.BADUCHAI,
                        "baduchai",
                        "Блин Шу Пу Эра Хао ХЭ 好喝熟普洱饼",
                        ItemType.RIPE_PUER_TEA,
                        "https://baduchai.ru/product/blin-shu-pu-era-hao-he-%e5%a5%bd%e5%96%9d%e7%86%9f%e6%99%ae%e6%b4%b1%e9%a5%bc/",
                        "https://baduchai.ru/wp-content/uploads/2023/07/00018804-300x300.jpg",
                        "$64.00"
                ),
                new StoreListItem(
                        Store.BADUCHAI,
                        "baduchai",
                        "Мао Пао Шу Пу Эр 毛袍熟普",
                        ItemType.RIPE_PUER_TEA,
                        "https://baduchai.ru/product/mao-pao-shu-pu-er-%e6%af%9b%e8%a2%8d%e7%86%9f%e6%99%ae/",
                        "https://baduchai.ru/wp-content/uploads/2023/07/00017774-300x300.jpg",
                        "$24.00"
                ),
                new StoreListItem(
                        Store.BADUCHAI,
                        "baduchai",
                        "Лао Шань Пу Эр блин 357 гр 老善普洱饼",
                        ItemType.RIPE_PUER_TEA,
                        "https://baduchai.ru/product/lao-shan-pu-er-blin-357-gr-%e8%80%81%e5%96%84%e6%99%ae%e6%b4%b1%e9%a5%bc/",
                        "https://baduchai.ru/wp-content/uploads/2023/06/00016714-300x300.jpg",
                        "$36.00"
                ),
                new StoreListItem(
                        Store.BADUCHAI,
                        "baduchai",
                        "Пин Ян Хуан Тан 平阳黄汤",
                        ItemType.GREEN_TEA,
                        "https://baduchai.ru/product/pin-yan-huan-tan-%e5%b9%b3%e9%98%b3%e9%bb%84%e6%b1%a4/",
                        "https://baduchai.ru/wp-content/uploads/2023/05/00015616-300x300.jpg",
                        "$32.00"
                ),
                new StoreListItem(
                        Store.BADUCHAI,
                        "baduchai",
                        "Кирпич Шу Пу Эра Ар Бай У 250 гр 二百五普洱茶砖",
                        ItemType.RIPE_PUER_TEA,
                        "https://baduchai.ru/product/kirpich-shu-pu-era-ar-baj-u-250-gr-%e4%ba%8c%e7%99%be%e4%ba%94%e6%99%ae%e6%b4%b1%e8%8c%b6/",
                        "https://baduchai.ru/wp-content/uploads/2023/04/00012304-300x300.jpg",
                        "$30.00"
                ),
                new StoreListItem(
                        Store.BADUCHAI,
                        "baduchai",
                        "Син Шунь старый Шу Пу Эр 兴顺老熟普",
                        ItemType.RIPE_PUER_TEA,
                        "https://baduchai.ru/product/xing-shun-hao-staryj-shu-pu-er-%e5%85%b4%e9%a1%ba%e8%80%81%e7%86%9f%e6%99%ae/",
                        "https://baduchai.ru/wp-content/uploads/2023/04/00031251-300x300.jpg",
                        "$150.00"
                )
        ), items);
    }
}
