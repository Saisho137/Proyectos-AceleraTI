package com.enyoi.simplechatia.infrastructure.gemini;

import com.enyoi.simplechatia.domain.model.ChatMessage;
import com.enyoi.simplechatia.domain.port.ChatService;
import static org.assertj.core.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

import com.google.genai.types.GenerateContentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test para el service adapter Gemini service")
public class GeminiChatServiceTest {
     @Mock(answer = Answers.RETURNS_DEEP_STUBS )
     private GeminiClientWrapper mockClient;
     private final String model = "gemini-2.0-flash";

     private ChatService geminiChatService;

     @BeforeEach
     void setUp() {
         geminiChatService = new GeminiChatService(mockClient, model);
     }

     @Test
     @DisplayName("Validar que implementa la interfaz port ChatService")
     void testImplementaInterfazCorrectPortChatService() {
         assertThat(geminiChatService).isInstanceOf(ChatService.class);
     }

     @Test
     @DisplayName("Validar el chat")
     void testValidaChatConUser() {
         String userMessage = "hola como estás?";
         GenerateContentResponse mockResponse = mock(GenerateContentResponse.class);
         when(mockResponse.text()).thenReturn("muy bien y tu ?");
         when(mockClient.generateContent(eq(model), eq(userMessage), any())).thenReturn(mockResponse);
         String response = geminiChatService.chat(userMessage);
         assertAll(
                 () ->  verify(mockClient,
                         times(1)).generateContent(eq(model), eq(userMessage), any()),
                 () -> verify(mockResponse, times(1)).text(),
                 () -> assertThat(response).isEqualTo("muy bien y tu ?")
         );
     }


     @Test
     @DisplayName("Validar el chat with System Prompt")
     void testValidarConSystemPrompt() {
         String userMessage = "hola como estás?";
         String systemPrompt = "Este es un system prompt";
         GenerateContentResponse mockResponse = mock(GenerateContentResponse.class);
         when(mockResponse.text()).thenReturn("muy bien y tu ?");
         when(mockClient.generateContent(eq(model), eq(userMessage), any())).thenReturn(mockResponse);
         String response = geminiChatService.chatWithSystemPrompt(systemPrompt, userMessage);
         assertAll(
                 () ->  verify(mockClient,
                         times(1)).generateContent(eq(model), eq(userMessage), any()),
                 () -> verify(mockResponse, times(1)).text(),
                 () -> assertThat(response).isEqualTo("muy bien y tu ?")
         );
     }

     @Test
     @DisplayName("Validar el chat with history")
     void testValidarConHistory() {
         List<ChatMessage> messages = List.of(
                 new ChatMessage("id-1", ChatMessage.Role.SYSTEM, "Sistema message", LocalDateTime.now()),
                 new ChatMessage("id-2", ChatMessage.Role.USER, "Sistema usuario", LocalDateTime.now()),
                 new ChatMessage("id-3", ChatMessage.Role.ASSISTANT, "Sistema asistente", LocalDateTime.now()),
                 new ChatMessage("id-4", ChatMessage.Role.USER, "Sistema usuario", LocalDateTime.now())
         );
         GenerateContentResponse mockResponse = mock(GenerateContentResponse.class);
         when(mockResponse.text()).thenReturn("muy bien y tu ?");
         when(mockClient.generateContent(eq(model), Collections.singletonList(any()), any())).thenReturn(mockResponse);
         String response = geminiChatService.chatWithHistory(messages);
         assertAll(
                 () ->  verify(mockClient,
                         times(1)).generateContent(eq(model), Collections.singletonList(any()), any()),
                 () -> verify(mockResponse, times(1)).text(),
                 () -> assertThat(response).isEqualTo("muy bien y tu ?")
         );
     }
}
