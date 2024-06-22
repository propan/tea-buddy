package com.github.propan.teabuddy.parsers;

import com.github.propan.teabuddy.models.StoreListItem;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class YunnanSourcingParserTest {

    @Test
    void parse() throws URISyntaxException, IOException {
        java.net.URL url = YunnanSourcingParser.class.getResource("yunnansourcing.html");
        java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());

        List<StoreListItem> items = new YunnanSourcingParser().parse(new String(Files.readAllBytes(resPath)));
        for (StoreListItem item : items) {
            System.out.println(item);
        }

    }
}
