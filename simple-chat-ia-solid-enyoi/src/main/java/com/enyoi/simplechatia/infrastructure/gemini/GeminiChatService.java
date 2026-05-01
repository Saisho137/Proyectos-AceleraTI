package com.enyoi.simplechatia.infrastructure.gemini;

import com.enyoi.simplechatia.domain.model.ChatMessage;
import com.enyoi.simplechatia.domain.port.ChatService;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.Part;

import java.util.ArrayList;
import java.util.List;

public class GeminiChatService implements ChatService {
    private final GeminiClientWrapper geminiClientWrapper;
    private final String model;

    public GeminiChatService(GeminiClientWrapper geminiClientWrapper, String model) {
        this.geminiClientWrapper = geminiClientWrapper;
        this.model = model;
    }

    @Override
    public String chat(String message) {
        GenerateContentConfig config = GenerateContentConfig.builder()
                .temperature(0.7f)
                .maxOutputTokens(2048)
                .build();

        return geminiClientWrapper.generateContent(model, message, config).text();
    }

    @Override
    public String chatWithSystemPrompt(String systemPrompt, String message) {
        GenerateContentConfig config = GenerateContentConfig.builder()
                .temperature(0.7f)
                .maxOutputTokens(2048)
                .systemInstruction(Content.fromParts(Part.fromText(systemPrompt)))
                .build();

        return geminiClientWrapper.generateContent(model, message, config).text();
    }

    @Override
    public String chatWithHistory(List<ChatMessage> messages) {
        // Desde el Main, se pasaría la lista de mensajes sacada por ejemplo, de Redis Cache

        List<Content> contents = new ArrayList<>();

        for (ChatMessage message : messages) {
            String role = message.role() == ChatMessage.Role.USER ? "user" : "model"; // Para OpenAI, sería 'assistant'
            contents.add(
                    Content.builder()
                            .role(role)
                            .parts(Part.fromText(message.content()))
                            .build()
            );
        }

        GenerateContentConfig config = GenerateContentConfig.builder()
                .temperature(0.7f)
                .maxOutputTokens(2048)
                .build();

        return geminiClientWrapper.generateContent(model, contents, config).text();
    }
}
