package com.enyoi.simplechatia.domain.port;

import com.enyoi.simplechatia.domain.model.ChatMessage;

import java.util.List;

public interface ChatService {
    String chat(String message);
    String chatWithSystemPrompt(String systemPrompt, String message);
    String chatWithHistory(List<ChatMessage> messages);
}
