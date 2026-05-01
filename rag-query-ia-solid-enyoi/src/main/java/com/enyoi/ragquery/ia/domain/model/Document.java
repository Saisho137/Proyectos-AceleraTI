package com.enyoi.ragquery.ia.domain.model;

import java.util.List;
import java.util.Map;

public record Document(
        String id,
        String content,
        Map<String, Object> metadata,
        List<Float> embedding
) {
    public String getMetadataValue(String key) {
        Object value = metadata.get(key);
        return value != null ? value.toString() : null;
    }
}
