package com.github.propan.teabuddy.repository;

import com.github.propan.teabuddy.models.Contact;

import java.util.List;

public interface ConfigurationRepository {

    public Contact getNotificationSender();

    public List<Contact> getNotificationRecipients();

    public Contact getErrorNotificationRecipient();
}
