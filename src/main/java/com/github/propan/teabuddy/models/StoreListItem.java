package com.github.propan.teabuddy.models;

import org.apache.logging.log4j.util.Strings;

import java.util.UUID;

public record StoreListItem(Store store, String vendor, String title, ItemType type, String sourceUrl, String imageUrl,
                            String price) {

    public boolean isValid() {
        return store != null &&
                Strings.isNotBlank(vendor)
                && Strings.isNotBlank(title)
                && type != null
                && Strings.isNotBlank(sourceUrl)
                && Strings.isNotBlank(imageUrl)
                && Strings.isNotBlank(price);
    }

    public UUID id() {
        return UUID.nameUUIDFromBytes(sourceUrl().getBytes());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("store='").append(store).append('\'');
        sb.append("vendor='").append(vendor).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", type=").append(type);
        sb.append(", sourceUrl='").append(sourceUrl).append('\'');
        sb.append(", imageUrl='").append(imageUrl).append('\'');
        sb.append(", price='").append(price).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
