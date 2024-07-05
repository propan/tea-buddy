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

class YunnanSourcingParserTest {

    @Test
    @Tag("integration")
    void integration() throws IOException {
        YunnanSourcingParser parser = new YunnanSourcingParser();

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
        java.net.URL url = YunnanSourcingParser.class.getResource("yunnansourcing.html");
        assertThat(url).isNotNull();

        java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());

        List<StoreListItem> items = new YunnanSourcingParser().parse(new String(Files.readAllBytes(resPath)));

        assertIterableEquals(Lists.list(
                new StoreListItem(
                        Store.YUNNAN_SOURCING,
                        "Gao Jia Shan",
                        "2013 Gao Jia Shan \"Waffle Cut\" Qian Liang Hei Cha Tea - 1 Cake (600 Grams)",
                        ItemType.DARK_TEA,
                        "https://yunnansourcing.com/collections/new-products/products/2013-gao-jia-shan-waffle-cut-qian-liang-hei-cha-tea",
                        "https://yunnansourcing.com/cdn/shop/files/1_0a016156-2a16-4643-a703-f2b44e0dbb67_512x512.jpg?v=1718731689",
                        "72.00$"
                ),
                new StoreListItem(
                        Store.YUNNAN_SOURCING,
                        "Yunnan Sourcing Tea Shop",
                        "Feng Qing Premium \"Black Gold Pearls\" Black Tea - 1 kilogram / Spring 2024",
                        ItemType.BLACK_TEA,
                        "https://yunnansourcing.com/collections/new-products/products/feng-qing-premium-black-gold-pearls-yunnan-black-tea",
                        "https://yunnansourcing.com/cdn/shop/products/thumb1_85d54d7c-b7b6-4ab4-88d1-68e9d4f6a1e9_512x511.jpg?v=1553621466",
                        "109.00$"
                ),
                new StoreListItem(
                        Store.YUNNAN_SOURCING,
                        "Yunnan Sourcing Tea Shop",
                        "Hand-Made Flowering Black Tea Cones from Feng Qing - 1 Kilogram / Spring 2024",
                        ItemType.BLACK_TEA,
                        "https://yunnansourcing.com/collections/new-products/products/hand-made-flowering-black-tea-cones-from-feng-qing",
                        "https://yunnansourcing.com/cdn/shop/products/thumb1_140d5732-84c6-47ee-bfaa-ddf4dab0f9f8_512x510.jpg?v=1560801157",
                        "109.00$"
                ),
                new StoreListItem(
                        Store.YUNNAN_SOURCING,
                        "Yunnan Sourcing Tea Shop",
                        "Zheng Shan Xiao Zhong of Wu Yi Fujian Black Tea - 1 kilogram / Spring 2024",
                        ItemType.BLACK_TEA,
                        "https://yunnansourcing.com/collections/new-products/products/zheng-shan-xiao-zhong-of-wu-yi-fujian-black-tea",
                        "https://yunnansourcing.com/cdn/shop/products/1_11c34554-324f-44f5-91f1-25fa6a168166_512x512.jpg?v=1625850009",
                        "75.00$"
                ),
                new StoreListItem(
                        Store.YUNNAN_SOURCING,
                        "Yunnan Sourcing Tea Shop",
                        "Phoenix Village \"Mi Xiang\" Shui Xian Oolong Tea - 1 kilogram / Spring 2024",
                        ItemType.OOLONG_TEA,
                        "https://yunnansourcing.com/collections/new-products/products/phoenix-village-mi-xiang-shui-xian-oolong-tea",
                        "https://yunnansourcing.com/cdn/shop/products/DSC_1230_a35458ef-7336-4527-afd2-a03133706a0csq_512x512.jpg?v=1661818731",
                        "95.00$"
                ),
                new StoreListItem(
                        Store.YUNNAN_SOURCING,
                        "Yunnan Sourcing Tea Shop",
                        "High Mountain \"Lao Cong Huang Zhi Xiang\" Dan Cong Oolong Tea - 1 Tin (15 Grams) / Spring 2024",
                        ItemType.OOLONG_TEA,
                        "https://yunnansourcing.com/collections/new-products/products/high-mountain-lao-cong-huang-zhi-xiang-dan-cong-oolong-tea",
                        "https://yunnansourcing.com/cdn/shop/files/PXL-20240610_153853481_512x512.jpg?v=1718034659",
                        "16.00$"
                ),
                new StoreListItem(
                        Store.YUNNAN_SOURCING,
                        "Yunnan Sourcing Tea Shop",
                        "Titanium Flask for Brewing Tea - Silver",
                        ItemType.TEAWARE,
                        "https://yunnansourcing.com/collections/new-products/products/titanium-flask-for-brewing-tea",
                        "https://yunnansourcing.com/cdn/shop/files/mmexport1717825960942_512x512.jpg?v=1717847622",
                        "70.00$"
                ),
                new StoreListItem(
                        Store.YUNNAN_SOURCING,
                        "Yunnan Sourcing Tea Shop",
                        "Ice Jade Porcelain \"Spring Bounty\" Gaiwan and Cups - 1 Gaiwan and 2 Cups",
                        ItemType.TEAWARE,
                        "https://yunnansourcing.com/collections/new-products/products/ice-jade-porcelain-spring-bounty-gaiwan-and-cups",
                        "https://yunnansourcing.com/cdn/shop/files/DSC_2337set_512x512.jpg?v=1717610990",
                        "46.00$"
                ),
                new StoreListItem(
                        Store.YUNNAN_SOURCING,
                        "Yunnan Sourcing Tea Shop",
                        "Rainbow Morning Titanium Tea Cups - 6 Cup Set (One Color of Each)",
                        ItemType.TEAWARE,
                        "https://yunnansourcing.com/collections/new-products/products/rainbow-morning-titanium-tea-cups",
                        "https://yunnansourcing.com/cdn/shop/files/1_e8c9434e-648d-4146-b9c1-23a892f0e1ca_512x512.jpg?v=1717609064",
                        "45.00$"
                ),
                new StoreListItem(
                        Store.YUNNAN_SOURCING,
                        "Yunnan Sourcing Tea Shop",
                        "Titanium Strainer With Hardwood Handle - Silver",
                        ItemType.TEAWARE,
                        "https://yunnansourcing.com/collections/new-products/products/titanium-strainer-with-hardwood-handle",
                        "https://yunnansourcing.com/cdn/shop/files/1_2f31ddff-ae4a-4a90-9a80-8828170c2a85_512x512.jpg?v=1717782217",
                        "15.90$"
                )
        ), items);
    }
}
