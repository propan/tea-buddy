package com.github.propan.teabuddy.repository;

import com.github.propan.teabuddy.models.StoreListItem;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemsRepository {

    int storeItems(List<StoreListItem> items);

    List<StoreListItem> findNewItemsSince(LocalDateTime ts);

}
