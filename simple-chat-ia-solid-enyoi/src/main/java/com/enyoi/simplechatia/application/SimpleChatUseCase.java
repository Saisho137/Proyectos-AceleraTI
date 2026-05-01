package com.enyoi.simplechatia.application;

import com.enyoi.simplechatia.domain.model.ChatMessage;
import com.enyoi.simplechatia.domain.model.Conversation;
import com.enyoi.simplechatia.domain.port.ChatService;

import java.util.List;
import java.util.UUID;

public class SimpleChatUseCase {
    private final ChatService chatService;

    public SimpleChatUseCase(ChatService chatService) {
        this.chatService = chatService;
    }

    public String execute(String message) {
        if (isNullOrEmpty(message)) {
            throw new IllegalArgumentException("User message cannot be null or empty");
        }
        return chatService.chat(message);
    }

    public String executeWithContext(String systemPrompt, String message) {
        if (isNullOrEmpty(message)) {
            throw new IllegalArgumentException("User message cannot be null or empty");
        }
        if (isNullOrEmpty(systemPrompt)) {
            return execute(message);
        }
        return chatService.chatWithSystemPrompt(systemPrompt, message);
    }

    public Conversation executeWithConversation(Conversation conversation, String message) {
        if (isNullOrEmpty(message)) {
            throw new IllegalArgumentException("User message cannot be null or empty");
        }
        if (conversation == null) {
            throw new IllegalArgumentException("Conversation cannot be null");
        }

        Conversation newConversation = conversation.addMessage(
                ChatMessage.userMessage(message)
        );

        String response = chatService.chatWithHistory(newConversation.messages());

        return newConversation.addMessage(ChatMessage.assistantMessage(response));
    }

    public Conversation createConversation(String systemPrompt) {
        if (isNullOrEmpty(systemPrompt)) {
            throw new IllegalArgumentException("System prompt cannot be null or empty");
        }
        return new Conversation(
                UUID.randomUUID().toString(),
                List.of(),
                systemPrompt
        );
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.isBlank();
    }
}
