package com.github.propan.teabuddy.service;

import com.github.propan.teabuddy.models.ItemGroup;
import com.github.propan.teabuddy.models.Store;
import com.github.propan.teabuddy.models.StoreListItem;
import com.github.propan.teabuddy.repository.ConfigurationRepository;
import com.github.propan.teabuddy.repository.ItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService {

    private final EmailService emailService;
    private final ItemsRepository repository;
    private final ConfigurationRepository configuration;

    @Autowired
    public NotificationService(EmailService emailService, ConfigurationRepository configuration, ItemsRepository repository) {
        this.emailService = emailService;
        this.configuration = configuration;
        this.repository = repository;
    }

    @Scheduled(cron = "0 0 9 * * *", zone = "Europe/Helsinki")
    public void sendNewItemsNotification() {
        List<StoreListItem> items = this.repository.findNewItemsSince(LocalDateTime.now().minusHours(24));

        if (items.isEmpty()) {
            return;
        }

        Map<Store, List<StoreListItem>> storeItems = new HashMap<>();

        for (StoreListItem item : items) {
            storeItems.computeIfAbsent(item.store(), k -> new ArrayList<>()).add(item);
        }

        List<ItemGroup> groups = new ArrayList<>();
        for (Map.Entry<Store, List<StoreListItem>> entry : storeItems.entrySet()) {
            groups.add(ItemGroup.of(entry.getKey(), entry.getValue()));
        }

        groups.sort((g1, g2) -> g2.store().compareTo(g1.store()));

        this.emailService.sendNotificationEmail(
                this.configuration.getNotificationSender(),
                this.configuration.getNotificationRecipients(),
                groups
        );
    }

    public void sendErrorNotification(Exception exception) {
        emailService.sendErrorNotificationEmail(
                this.configuration.getNotificationSender(),
                this.configuration.getErrorNotificationRecipient(),
                exception
        );
    }
}
