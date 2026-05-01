package com.enyoi.simplechatia.domain.model;

import com.enyoi.simplechatia.domain.model.ChatMessage;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@DisplayName("ChatMessage Model Domain Test")
public class ChatMessageTest {
     @Test
     @DisplayName("Chequear que se tenga implementado el Role dentro de un ChatMessage")
     void testSubEnumTypeInChatMessage() {
         assertThat(ChatMessage.Role.values()).containsAll(
                 List.of(ChatMessage.Role.USER, ChatMessage.Role.ASSISTANT, ChatMessage.Role.SYSTEM)
         );
     }

     @Test
     @DisplayName("Validar que sea un record y pida los diferentes elementos id, role, content, localdatetime")
     void testValidarParametrosDeChatMessage(){
         LocalDateTime localDateTime = LocalDateTime.now();
         Supplier<ChatMessage> chatMessageSupplier = () ->
                 new ChatMessage("some-id", ChatMessage.Role.USER, "some message", localDateTime);
         assertAll("Propiedades del record",
                 () -> assertDoesNotThrow(chatMessageSupplier::get),
                 () -> assertThat(chatMessageSupplier.get()).isInstanceOf(ChatMessage.class),
                 () -> assertThat(chatMessageSupplier.get()).hasFieldOrPropertyWithValue("id", "some-id"),
                 () -> assertThat(chatMessageSupplier.get()).hasFieldOrPropertyWithValue("role", ChatMessage.Role.USER),
                 () -> assertThat(chatMessageSupplier.get()).hasFieldOrPropertyWithValue("content", "some message"),
                 () -> assertThat(chatMessageSupplier.get()).hasFieldOrPropertyWithValue("timestamp", localDateTime)
         );
     }

     @Test
     @DisplayName("Validar que pueda crear un userMessage de manera estática con un contenido")
     void testValidarCrearUserMessage(){
         LocalDateTime localDateTime = LocalDateTime.now();
         UUID uuid = UUID.randomUUID();
         try (
                 MockedStatic<LocalDateTime> mockedLocalDateTime = Mockito.mockStatic(LocalDateTime.class);
                 MockedStatic<UUID> mockedUUID = Mockito.mockStatic(UUID.class)
             ) {
             mockedLocalDateTime.when(LocalDateTime::now).thenReturn(localDateTime);
             mockedUUID.when(UUID::randomUUID).thenReturn(uuid);
             ChatMessage userMessage = ChatMessage.userMessage("Este es un contenido de prueba");

             assertAll(
                     () -> assertThat(userMessage.timestamp()).isEqualTo(localDateTime),
                     () -> assertThat(userMessage.content()).isEqualTo("Este es un contenido de prueba"),
                     () -> assertThat(userMessage.id()).isEqualTo(uuid.toString()),
                     () -> assertThat(userMessage.role()).isEqualTo(ChatMessage.Role.USER)
             );
         }
     }


     @Test
     @DisplayName("Validar que pueda crear un assistantMessage de manera estática con un contenido")
     void testValidarCrearAssistantMessage(){
         LocalDateTime localDateTime = LocalDateTime.now();
         UUID uuid = UUID.randomUUID();
         try (
                 MockedStatic<LocalDateTime> mockedLocalDateTime = Mockito.mockStatic(LocalDateTime.class);
                 MockedStatic<UUID> mockedUUID = Mockito.mockStatic(UUID.class)
         ) {
             mockedLocalDateTime.when(LocalDateTime::now).thenReturn(localDateTime);
             mockedUUID.when(UUID::randomUUID).thenReturn(uuid);
             ChatMessage userMessage = ChatMessage.assistantMessage("Este es un contenido de prueba");

             assertAll(
                     () -> assertThat(userMessage.timestamp()).isEqualTo(localDateTime),
                     () -> assertThat(userMessage.content()).isEqualTo("Este es un contenido de prueba"),
                     () -> assertThat(userMessage.id()).isEqualTo(uuid.toString()),
                     () -> assertThat(userMessage.role()).isEqualTo(ChatMessage.Role.ASSISTANT)
             );
         }
     }

     @Test
     @DisplayName("Validar que pueda crear un systemMessage de manera estática con un contenido")
     void testValidarCrearSystemMessage(){
         LocalDateTime localDateTime = LocalDateTime.now();
         UUID uuid = UUID.randomUUID();
         try (
                 MockedStatic<LocalDateTime> mockedLocalDateTime = Mockito.mockStatic(LocalDateTime.class);
                 MockedStatic<UUID> mockedUUID = Mockito.mockStatic(UUID.class)
         ) {
             mockedLocalDateTime.when(LocalDateTime::now).thenReturn(localDateTime);
             mockedUUID.when(UUID::randomUUID).thenReturn(uuid);
             ChatMessage userMessage = ChatMessage.systemMessage("Este es un contenido de prueba");

             assertAll(
                     () -> assertThat(userMessage.timestamp()).isEqualTo(localDateTime),
                     () -> assertThat(userMessage.content()).isEqualTo("Este es un contenido de prueba"),
                     () -> assertThat(userMessage.id()).isEqualTo(uuid.toString()),
                     () -> assertThat(userMessage.role()).isEqualTo(ChatMessage.Role.SYSTEM)
             );
         }
     }

}
