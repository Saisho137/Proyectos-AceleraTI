package com.enyoi.simplechatia.domain.model;

import java.util.ArrayList;
import java.util.List;

public record Conversation(
        String id,
        List<ChatMessage> messages,
        String systemPrompt
) {
    public ChatMessage getLastMessage() {
        if (messages.isEmpty()) return null;
        return messages.get(messages.size() - 1);
    }

    public List<ChatMessage> getUserMessages() {
        return messages.stream()
                .filter(message -> message.role() == ChatMessage.Role.USER)
                .toList();
    }

    public List<ChatMessage> getAssistantMessages() {
        return messages.stream()
                .filter(message -> message.role() == ChatMessage.Role.ASSISTANT)
                .toList();
    }

    public List<ChatMessage> getSystemMessages() {
        return messages.stream()
                .filter(message -> message.role() == ChatMessage.Role.SYSTEM)
                .toList();
    }

    public Conversation addMessage(ChatMessage newMessage) {
        List<ChatMessage> messages = new ArrayList<>(this.messages);
        messages.add(newMessage);
        return new Conversation(
                this.id,
                messages,
                this.systemPrompt
        );
    }
}
