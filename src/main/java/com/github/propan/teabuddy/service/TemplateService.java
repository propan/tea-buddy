package com.github.propan.teabuddy.service;

import com.github.propan.teabuddy.models.ItemGroup;
import com.github.propan.teabuddy.models.Contact;
import de.neuland.pug4j.PugConfiguration;
import de.neuland.pug4j.template.PugTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class TemplateService {

    private final PugConfiguration configuration;
    private final EmailComposer composer;

    @Autowired
    public TemplateService(PugConfiguration configuration, EmailComposer composer) {
        this.configuration = configuration;
        this.composer = composer;
    }

    public String renderNotificationEmail(Contact recipient, List<ItemGroup> groups) throws IOException {
        PugTemplate template = configuration.getTemplate("new_items_notification_email.pug");

        String introText = composer.compose(groups);

        return configuration.renderTemplate(template, Map.of(
                "recipient", recipient,
                "groups", groups,
                "introText", introText
        ));
    }

}
