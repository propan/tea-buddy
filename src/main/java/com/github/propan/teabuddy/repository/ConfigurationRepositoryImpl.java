package com.github.propan.teabuddy.repository;

import com.github.propan.teabuddy.models.Contact;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ConfigurationRepositoryImpl implements ConfigurationRepository {

    private static final Logger log = LoggerFactory.getLogger(ConfigurationRepositoryImpl.class);

    private Contact sender;
    private final List<Contact> recipients = new ArrayList<>();

    @PostConstruct
    public void init() {
        try {
            InternetAddress address = new InternetAddress(System.getenv("NOTIFICATION_SENDER"));
            sender = Contact.of(address.getPersonal(), address.getAddress());
        } catch (AddressException ex) {
            log.error("Failed to parse NOTIFICATION_SENDER", ex);
        }

        try {
            InternetAddress[] addresses = InternetAddress.parse(System.getenv("NOTIFICATION_RECIPIENTS"));
            for (InternetAddress address : addresses) {
                recipients.add(Contact.of(address.getPersonal(), address.getAddress()));
            }
        } catch (AddressException ex) {
            log.error("Failed to parse NOTIFICATION_RECIPIENTS", ex);
        }

        if (sender == null || recipients.isEmpty()) {
            throw new IllegalStateException("Failed to parse configuration");
        }
    }

    @Override
    public Contact getNotificationSender() {
        return Contact.of(sender.name(), sender.email());
    }

    @Override
    public List<Contact> getNotificationRecipients() {
        return List.copyOf(this.recipients);
    }

}
