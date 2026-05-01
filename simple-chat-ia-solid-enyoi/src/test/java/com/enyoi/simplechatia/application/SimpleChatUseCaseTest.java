package com.enyoi.simplechatia.application;

import com.enyoi.simplechatia.domain.model.ChatMessage;
import com.enyoi.simplechatia.domain.model.Conversation;
import com.enyoi.simplechatia.domain.port.ChatService;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@DisplayName("Prueba para el caso de uso de Simple chat use case")
public class SimpleChatUseCaseTest {
    @Mock
    private ChatService mockChatService;

    private SimpleChatUseCase simpleChatUseCase;

    @BeforeEach
    void setUp() {
        simpleChatUseCase = new SimpleChatUseCase(mockChatService);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "null",
            "''"
    }, nullValues = "null")
    @DisplayName("Probar execute con invalida usuer message")
    void testExecuteInvalidaUsuarioMessage(String userMessage) {
        assertThatThrownBy(() -> simpleChatUseCase.execute(userMessage)).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "null, null",
            "'', ''"
    }, nullValues = "null")
    @DisplayName("Probar execute with context con invalida usuer message")
    void testExecuteWithContextInvalidaUserName(String userMessage, String systemPrompt) {
        assertThatThrownBy(() -> simpleChatUseCase.executeWithContext(systemPrompt, userMessage)).isInstanceOf(IllegalArgumentException.class);
    }


    @ParameterizedTest
    @CsvSource(value = {
            "null",
            "''"
    }, nullValues = "null")
    @DisplayName("Probar execute with conversation con invalida usuer message")
    void testExecuteWithConversationInvalidaUserName(String userMessage) {
        Conversation conversation = new Conversation("some-id", new ArrayList<>(), "some system prompt");
        assertThatThrownBy(() -> simpleChatUseCase.executeWithConversation(conversation, userMessage)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Probar la creación de una conversación vacia")
    void testCreateConversationEmpty() {
        String systemPrompt = "Some system prompt";
        UUID mockedConversationId = UUID.randomUUID();
        try (MockedStatic<UUID> mockUUID = Mockito.mockStatic(UUID.class)) {
            mockUUID.when(UUID::randomUUID).thenReturn(mockedConversationId);

            Conversation conversation = simpleChatUseCase.createConversation(systemPrompt);

            assertAll(
                    () -> assertThat(conversation.id()).isEqualTo(mockedConversationId.toString()),
                    () -> assertThat(conversation.messages()).isEmpty(),
                    () -> assertThat(conversation.systemPrompt()).isEqualTo(systemPrompt)
            );
        }
    }

    @Test
    @DisplayName("Test execute con valid user message")
    void testExecuteValidUserMessage() {
        String userMessage = "Hello World!";

        when(mockChatService.chat(eq(userMessage))).thenReturn("Hello World!");

        String response = simpleChatUseCase.execute(userMessage);

        assertAll(
                () -> verify(mockChatService, times(1)).chat(eq(userMessage)),
                () -> assertThat(response).isEqualTo("Hello World!")
        );
    }

    @Test
    @DisplayName("Test execute with context con valid user message y valid systemp prompt")
    void testExecuteWithContextValidUserMessage() {
        String userMessage = "Hello World!";
        String systemPrompt = "Some system prompt";

        when(mockChatService.chatWithSystemPrompt(eq(systemPrompt), eq(userMessage))).thenReturn("Hello World!");

        String response = simpleChatUseCase.executeWithContext(systemPrompt, userMessage);

        assertAll(
                () -> verify(mockChatService, times(1)).chatWithSystemPrompt(eq(systemPrompt), eq(userMessage)),
                () -> assertThat(response).isEqualTo("Hello World!")
        );
    }

    @ParameterizedTest
    @CsvSource(value = {"null", "''"}, nullValues = "null")
    @DisplayName("Test execute with context con valid user message e invalid systemp prompt")
    void textExecuteWithContextValidUserMessageInvalidSystem(String systemPrompt) {
        String userMessage = "Hello World!";

        when(mockChatService.chat(eq(userMessage))).thenReturn("Hello World!");

        String response = simpleChatUseCase.executeWithContext(systemPrompt, userMessage);

        assertAll(
                () -> verify(mockChatService, times(1)).chat(eq(userMessage)),
                () -> verify(mockChatService, times(0)).chatWithSystemPrompt(any(), any()),
                () -> assertThat(response).isEqualTo("Hello World!")
        );
    }

    @Test
    @DisplayName("Test execute with history con valid user message ")
    void testExecuteWithHistoryValidUserMessage() {
        Conversation conversation = new Conversation("some-id", new ArrayList<>(), "some system prompt");
        String userMessage = "Hello World!";

        when(mockChatService.chatWithHistory(any())).thenReturn("soy gemini");

        Conversation updatedConversation = simpleChatUseCase.executeWithConversation(conversation, userMessage);

        assertAll(
                () -> verify(mockChatService, times(1)).chatWithHistory(any()),
                () -> assertThat(Objects.requireNonNull(updatedConversation.getLastMessage()).content()).isEqualTo("soy gemini")
        );
    }
}
