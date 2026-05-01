package com.enyoi.simplechatia.infrastructure.gemini;

import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;

import java.util.List;

public class GeminiClientWrapper {
    private final Client client;

    public GeminiClientWrapper(Client client) {
        this.client = client;
    }

    public GenerateContentResponse generateContent(String model, String userMessage, GenerateContentConfig config) {
        return client.models.generateContent(model, userMessage, config);
    }

    public GenerateContentResponse generateContent(String model, List<Content> userMessage, GenerateContentConfig config) {
        return client.models.generateContent(model, userMessage, config);
    }
}
