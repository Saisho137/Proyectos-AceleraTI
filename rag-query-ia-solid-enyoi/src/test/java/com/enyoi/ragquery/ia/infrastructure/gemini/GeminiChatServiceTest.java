package com.enyoi.ragquery.ia.infrastructure.gemini;

import com.enyoi.ragquery.ia.domain.port.ChatService;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Estas pruebas serán para el ChatService adapter de gemini")
class GeminiChatServiceTest {
    @Mock
    private GeminiClientWrapper mockClient;
    @InjectMocks
    private GeminiChatService geminiChatService;

    @Test
    @DisplayName("Debería instanciar el servicio de chat de Gemini correctamente")
    void shouldInstantiateGeminiChatService() {
        assertNotNull(geminiChatService);
    }

    @Test
    @DisplayName("Validar que esta clase implementa ChatService en el contrato")
    void shouldImplementChatServiceInterface() {
        assertTrue(ChatService.class.isAssignableFrom(GeminiChatService.class));
    }

    @Test
    @DisplayName("Validar generación de mensaje con systemPrompt y userMessage válidos")
    void shouldGenerateMessageWithValidSystemPromptAndUserMessage() {
        String model = "gemini-2.0-flash";
        String systemPrompt = "Eres un asistente útil.";
        String userMessage = "Hola, ¿quién eres?";
        String expected = "Soy Gemini tu asistente virtual";
        GenerateContentResponse geminiResponse = mock(GenerateContentResponse.class);

        when(geminiResponse.text()).thenReturn(expected);
        when(mockClient.generateContent(eq(model), eq(userMessage), any(GenerateContentConfig.class)))
                .thenReturn(geminiResponse);

        String response = geminiChatService.chatWithSystemPrompt(systemPrompt, userMessage);

        assertAll(
                () -> assertEquals(expected, response),
                () -> verify(mockClient, times(1))
                        .generateContent(eq(model), eq(userMessage), any(GenerateContentConfig.class)),
                () -> verify(geminiResponse, times(1)).text()
        );
    }

}