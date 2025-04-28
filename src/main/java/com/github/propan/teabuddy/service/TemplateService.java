package com.github.propan.teabuddy.service;

import com.github.propan.teabuddy.models.ItemGroup;
import com.github.propan.teabuddy.models.Contact;
import de.neuland.pug4j.PugConfiguration;
import de.neuland.pug4j.template.PugTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public String renderErrorAlertEmail(Exception exception) throws IOException {
        PugTemplate template = configuration.getTemplate("error_alert_email.pug");

        Throwable cause = exception.getCause() == null ? exception : exception.getCause();

        String errorMessage = cause.getMessage() == null || cause.getMessage().isEmpty() ?
                cause.getClass().getCanonicalName() :
                cause.getMessage();

        boolean hasStacktrace = exception.getStackTrace() != null && exception.getStackTrace().length > 0;

        String errorStacktrace = null;

        if (hasStacktrace) {
            errorStacktrace = Arrays.stream(exception.getStackTrace())
                    .map(stackTrace -> "\t" + stackTrace.toString()).collect(Collectors.joining("\n"));
        }

        return configuration.renderTemplate(template, Map.of(
                "errorMessage", errorMessage,
                "hasStacktrace", hasStacktrace,
                "stackTrace", errorStacktrace == null ? "" : errorStacktrace
        ));
    }
}
