package com.github.propan.teabuddy.service;

import com.github.propan.teabuddy.models.*;
import com.github.propan.teabuddy.repository.ConfigurationRepository;
import com.github.propan.teabuddy.repository.ItemsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private EmailService emailService;
    @Mock
    private ItemsRepository repository;
    @Mock
    private ConfigurationRepository configuration;

    @InjectMocks
    private NotificationService notificationService;

    @AfterEach
    public void tearDown() {
        Mockito.verifyNoMoreInteractions(emailService, repository, configuration);
    }

    @Test
    void sendNewItemsNotification() {
        List<StoreListItem> items = List.of(
                new StoreListItem(
                        Store.WHITE2TEA,
                        "test-vendor",
                        "test-title",
                        ItemType.BLACK_TEA,
                        "test-url",
                        "test-image",
                        "10$"
                ),
                new StoreListItem(
                        Store.YUNNAN_SOURCING,
                        "test-vendor",
                        "test-title",
                        ItemType.BLACK_TEA,
                        "test-url",
                        "test-image",
                        "10$"
                )
        );
        when(repository.findNewItemsSince(Mockito.any())).thenReturn(items);

        when(configuration.getNotificationSender()).thenReturn(Contact.of("Dale Cooper", "bob@twinpeaks.com"));
        when(configuration.getNotificationRecipients()).thenReturn(
                List.of(Contact.of("Sheriff Truman", "bob@twinpeaks.com"))
        );

        notificationService.sendNewItemsNotification();

        verify(repository, times(1)).findNewItemsSince(Mockito.any());
        verify(configuration, times(1)).getNotificationSender();
        verify(configuration, times(1)).getNotificationRecipients();

        verify(emailService, times(1)).sendNotificationEmail(
                Contact.of("Dale Cooper", "bob@twinpeaks.com"),
                List.of(Contact.of("Sheriff Truman", "bob@twinpeaks.com")),
                List.of(
                        ItemGroup.of(Store.YUNNAN_SOURCING, List.of(items.get(1))),
                        ItemGroup.of(Store.WHITE2TEA, List.of(items.get(0)))
                )
        );

    }
}
