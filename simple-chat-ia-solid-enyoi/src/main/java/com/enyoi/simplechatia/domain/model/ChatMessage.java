package com.enyoi.simplechatia.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatMessage(
        String id,
        Role role,
        String content,
        LocalDateTime timestamp
) {
    public enum Role {
        USER,
        ASSISTANT,
        SYSTEM
    }

    public static ChatMessage userMessage(String content) {
        return new ChatMessage(
                UUID.randomUUID().toString(),
                Role.USER,
                content,
                LocalDateTime.now()
        );
    }

    public static ChatMessage assistantMessage(String content) {
        return new ChatMessage(
                UUID.randomUUID().toString(),
                Role.ASSISTANT,
                content,
                LocalDateTime.now()
        );
    }

    public static ChatMessage systemMessage(String content) {
        return new ChatMessage(
                UUID.randomUUID().toString(),
                Role.SYSTEM,
                content,
                LocalDateTime.now()
        );
    }
}
