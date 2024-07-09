package com.github.propan.teabuddy.models;

import java.util.List;

public record ItemGroup(Store store, List<StoreListItem> items) {

    public static ItemGroup of(Store store, List<StoreListItem> items) {
        return new ItemGroup(store, items);
    }

}
