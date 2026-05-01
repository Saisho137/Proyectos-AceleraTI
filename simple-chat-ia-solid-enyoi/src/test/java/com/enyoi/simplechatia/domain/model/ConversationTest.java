package com.enyoi.simplechatia.domain.model;

import com.enyoi.simplechatia.domain.model.ChatMessage;
import com.enyoi.simplechatia.domain.model.Conversation;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@DisplayName("Tests para modelo de dominio Conversation")
public class ConversationTest {

    @Test
    @DisplayName("Test parametros de modelo de dominio Conversation id, list de chatmessages, systemprompt")
    void testParametrosModeloDominio() {
        List<ChatMessage> messages = List.of(
                new ChatMessage("id-1", ChatMessage.Role.SYSTEM, "Sistema message", LocalDateTime.now()),
                new ChatMessage("id-2", ChatMessage.Role.USER, "Sistema usuario", LocalDateTime.now()),
                new ChatMessage("id-3", ChatMessage.Role.ASSISTANT, "Sistema asistente", LocalDateTime.now()),
                new ChatMessage("id-4", ChatMessage.Role.USER, "Sistema usuario", LocalDateTime.now())
        );
        Supplier<Conversation> conversationSupplier = () -> new Conversation("some-id", messages, "Eres un agente inteligente");

        assertAll(
                () -> assertDoesNotThrow(conversationSupplier::get),
                () -> assertThat(conversationSupplier.get().id()).isEqualTo("some-id"),
                () -> assertThat(conversationSupplier.get().systemPrompt()).isEqualTo("Eres un agente inteligente"),
                () -> assertThat(conversationSupplier.get().messages()).isEqualTo(messages)
        );
    }

    @Test
    @DisplayName("Test que devuelva el ultimo mensaje de la conversación")
    void testDevuelveUltimoMensajeDeLaConversacion() {
        List<ChatMessage> messages = List.of(
                new ChatMessage("id-1", ChatMessage.Role.SYSTEM, "Sistema message", LocalDateTime.now()),
                new ChatMessage("id-2", ChatMessage.Role.USER, "Sistema usuario", LocalDateTime.now()),
                new ChatMessage("id-3", ChatMessage.Role.ASSISTANT, "Sistema asistente", LocalDateTime.now()),
                new ChatMessage("id-4", ChatMessage.Role.USER, "Sistema usuario", LocalDateTime.now())
        );
        Conversation conversation = new Conversation("some-id", messages, "Eres un agente inteligente");

        assertThat(conversation.getLastMessage()).isEqualTo(messages.get(3));

        List<ChatMessage> updatedMessages = new ArrayList<>(messages);
        ChatMessage newLastMessage = new ChatMessage("id-5", ChatMessage.Role.ASSISTANT, "Sistema asistente", LocalDateTime.now());
        updatedMessages.add(newLastMessage);

        Conversation updatedConversation = new Conversation("some-id", updatedMessages, "Eres un agente inteligente");
        assertThat(updatedConversation.getLastMessage()).isEqualTo(newLastMessage);
    }

    @Test
    @DisplayName("Test que devuelva solo los mensajes de tipo user message")
    void testDevuelveLosMensajesTipoUser() {
        List<ChatMessage> messages = List.of(
                new ChatMessage("id-1", ChatMessage.Role.SYSTEM, "Sistema message", LocalDateTime.now()),
                new ChatMessage("id-2", ChatMessage.Role.USER, "Sistema usuario", LocalDateTime.now()),
                new ChatMessage("id-3", ChatMessage.Role.ASSISTANT, "Sistema asistente", LocalDateTime.now()),
                new ChatMessage("id-4", ChatMessage.Role.USER, "Sistema usuario", LocalDateTime.now())
        );
        Conversation conversation = new Conversation("some-id", messages, "Eres un agente inteligente");

        assertThat(conversation.getUserMessages()).allSatisfy(m -> assertThat(m.role()).isEqualTo(ChatMessage.Role.USER));
    }

    @Test
    @DisplayName("Test que no devuelve mensajes")
    void testDevuelveUnaListaVaciaConMessagesVacios() {
        List<ChatMessage> messages = List.of();
        Conversation conversation = new Conversation("some-id", messages, "Eres un agente inteligente");
        assertThat(conversation.getUserMessages()).isEmpty();
    }

    @Test
    @DisplayName("Test que devuelva solo los mensajes de tipo assistant message")
    void testDevuelveLosMensajesTipoAssistant() {
        List<ChatMessage> messages = List.of(
                new ChatMessage("id-1", ChatMessage.Role.SYSTEM, "Sistema message", LocalDateTime.now()),
                new ChatMessage("id-2", ChatMessage.Role.USER, "Sistema usuario", LocalDateTime.now()),
                new ChatMessage("id-3", ChatMessage.Role.ASSISTANT, "Sistema asistente", LocalDateTime.now()),
                new ChatMessage("id-4", ChatMessage.Role.USER, "Sistema usuario", LocalDateTime.now())
        );
        Conversation conversation = new Conversation("some-id", messages, "Eres un agente inteligente");

        assertThat(conversation.getAssistantMessages()).allSatisfy(m -> assertThat(m.role()).isEqualTo(ChatMessage.Role.ASSISTANT));
    }

    @Test
    @DisplayName("Test que devuelva solo los mensajes de tipo system message")
    void testDevuelveLosMensajesTipoSystem() {
        List<ChatMessage> messages = List.of(
                new ChatMessage("id-1", ChatMessage.Role.SYSTEM, "Sistema message", LocalDateTime.now()),
                new ChatMessage("id-2", ChatMessage.Role.USER, "Sistema usuario", LocalDateTime.now()),
                new ChatMessage("id-3", ChatMessage.Role.ASSISTANT, "Sistema asistente", LocalDateTime.now()),
                new ChatMessage("id-4", ChatMessage.Role.USER, "Sistema usuario", LocalDateTime.now())
        );
        Conversation conversation = new Conversation("some-id", messages, "Eres un agente inteligente");

        assertThat(conversation.getSystemMessages()).allSatisfy(m -> assertThat(m.role()).isEqualTo(ChatMessage.Role.SYSTEM));
    }

    @Test
    @DisplayName("Test que cada que agregue un nuevo mensaje a la conversación me devuelva una copia de la conversación")
    void testAnadirMensajeAConversacionNuevaConversacion() {
        List<ChatMessage> messages = List.of(
                new ChatMessage("id-1", ChatMessage.Role.SYSTEM, "Sistema message", LocalDateTime.now()),
                new ChatMessage("id-2", ChatMessage.Role.USER, "Sistema usuario", LocalDateTime.now()),
                new ChatMessage("id-3", ChatMessage.Role.ASSISTANT, "Sistema asistente", LocalDateTime.now()),
                new ChatMessage("id-4", ChatMessage.Role.USER, "Sistema usuario", LocalDateTime.now())
        );
        Conversation conversation = new Conversation("some-id", messages, "Eres un agente inteligente");

        ChatMessage newLastMessage = new ChatMessage("id-5", ChatMessage.Role.ASSISTANT, "Sistema asistente", LocalDateTime.now());
        Conversation updatedConversation = conversation.addMessage(newLastMessage);

        assertAll(
                () -> assertThat(updatedConversation.id()).isEqualTo(conversation.id()),
                () -> assertThat(updatedConversation.systemPrompt()).isEqualTo(conversation.systemPrompt()),
                () -> assertThat(updatedConversation.messages()).isNotEqualTo(conversation.messages()),
                () -> assertThat(updatedConversation.messages()).contains(newLastMessage),
                () -> assertThat(updatedConversation).isNotEqualTo(conversation)
        );

    }

}
