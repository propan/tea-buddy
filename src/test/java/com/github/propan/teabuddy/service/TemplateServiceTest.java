package com.github.propan.teabuddy.service;

import com.github.propan.teabuddy.models.*;
import com.github.propan.teabuddy.utils.BaseTestCase;
import de.neuland.pug4j.PugConfiguration;
import de.neuland.pug4j.spring.template.SpringTemplateLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class TemplateServiceTest extends BaseTestCase {

    @Mock
    private EmailComposer composer;

    private TemplateService templateService;

    @BeforeEach
    void setUp() {
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();

        SpringTemplateLoader templateLoader = new SpringTemplateLoader();
        templateLoader.setResourceLoader(resourceLoader);
        templateLoader.setTemplateLoaderPath("classpath:/templates/");

        PugConfiguration configuration = new PugConfiguration();
        configuration.setPrettyPrint(true);
        configuration.setCaching(true);
        configuration.setTemplateLoader(templateLoader);

        templateService = new TemplateService(configuration, composer);
    }

    @Test
    void renderNotificationEmail() throws IOException {
        List<ItemGroup> groups = List.of(
                new ItemGroup(
                        Store.WHITE2TEA,
                        List.of(
                                new StoreListItem(
                                        Store.WHITE2TEA,
                                        "white2tea",
                                        "2024 Philtre - 200g",
                                        ItemType.RAW_PUER_TEA,
                                        "https://white2tea.com/collections/latest-additions/products/2024-philtre",
                                        "https://white2tea.com/cdn/shop/files/2024Philtre-Raw-Puer-tea_1600x.jpg?v=1716700025",
                                        "46.95$"
                                ),
                                new StoreListItem(
                                        Store.WHITE2TEA,
                                        "white2tea",
                                        "2024 Philtre Minis - ~7g",
                                        ItemType.RAW_PUER_TEA,
                                        "https://white2tea.com/collections/latest-additions/products/2024-philtre-minis",
                                        "https://white2tea.com/cdn/shop/files/2024PhiltreMinis_1600x.jpg?v=1716699952",
                                        "1.95$"
                                )
                        )
                )
        );

        when(composer.compose(groups)).thenReturn(EmailComposer.DEFAULT_OUTPUT);

        String result = templateService.renderNotificationEmail(
                Contact.of("Dale Cooper", "bob@twinpeaks.com"),
                groups
        );

        java.net.URL url = TemplateServiceTest.class.getResource("notification.html");
        assertThat(url).isNotNull();

        assertThat(result.strip()).isEqualTo(Files.readString(Paths.get(url.getPath())).strip());
    }

    @Test
    void renderErrorAlertEmail() throws IOException {
        String result = templateService.renderErrorAlertEmail(new NullPointerException("This is a test exception!"));

        java.net.URL url = TemplateServiceTest.class.getResource("alert.html");
        assertThat(url).isNotNull();

        assertThat(result.strip()).isEqualTo(Files.readString(Paths.get(url.getPath())).strip());
    }
}
