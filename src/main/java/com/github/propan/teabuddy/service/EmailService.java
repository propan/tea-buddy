package com.github.propan.teabuddy.service;

import com.github.propan.teabuddy.models.Contact;
import com.github.propan.teabuddy.models.ItemGroup;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.transactional.SendContact;
import com.mailjet.client.transactional.SendEmailsRequest;
import com.mailjet.client.transactional.TrackOpens;
import com.mailjet.client.transactional.TransactionalEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final TemplateService templateService;
    private final MailjetClient client;

    @Autowired
    public EmailService(TemplateService templateService, MailjetClient client) {
        this.templateService = templateService;
        this.client = client;
    }

    public void sendNotificationEmail(Contact from, List<Contact> to, List<ItemGroup> groups) {
        Integer totalItems = groups.stream().map(g -> g.items().size()).reduce(Integer::sum).orElse(0);

        SendEmailsRequest.SendEmailsRequestBuilder requestBuilder = SendEmailsRequest.builder();

        for (Contact recipient : to) {
            try {
                String emailBody = this.templateService.renderNotificationEmail(recipient, groups);

                TransactionalEmail message = TransactionalEmail.builder()
                        .to(new SendContact(recipient.email(), recipient.name()))
                        .from(new SendContact(from.email(), from.name()))
                        .htmlPart(emailBody)
                        .subject(String.format("Alert: Discovered %d new items", totalItems))
                        .trackOpens(TrackOpens.ENABLED)
                        .build();

                requestBuilder.message(message);
            } catch (Exception e) {
                log.error("Failed to render email", e);
                continue;
            }

            try {
                requestBuilder.build().sendWith(this.client);
            } catch (MailjetException e) {
                log.error("Failed to send email", e);
            }
        }
    }
}
