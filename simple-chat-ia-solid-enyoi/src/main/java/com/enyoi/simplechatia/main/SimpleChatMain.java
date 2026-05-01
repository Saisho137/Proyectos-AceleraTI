package com.enyoi.simplechatia.main;

import com.enyoi.simplechatia.application.SimpleChatUseCase;
import com.enyoi.simplechatia.domain.model.ChatMessage;
import com.enyoi.simplechatia.domain.model.Conversation;
import com.enyoi.simplechatia.infrastructure.gemini.GeminiChatService;
import com.enyoi.simplechatia.infrastructure.gemini.GeminiClientWrapper;
import com.google.genai.Client;

import java.util.Scanner;

public class SimpleChatMain {
    public static void main(String[] args) {
        System.out.println("MODULO 1: SIMPLE CHAT CON GEMINI");
        System.out.println();

        String apiKey = System.getenv("GOOGLE_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            System.out.println("ERROR: Debes configurar la variable de entorno GOOGLE_API_KEY");
            System.out.println();
        }


        Client client = Client.builder().apiKey(apiKey).build();
        String model = "gemini-2.0-flash";
        GeminiClientWrapper geminiClientWrapper = new GeminiClientWrapper(client);
        GeminiChatService chatService = new GeminiChatService(geminiClientWrapper, model);
        SimpleChatUseCase simpleChatUseCase = new SimpleChatUseCase(chatService);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n┌─────────────────────────────────────────┐");
            System.out.println("│  Elige una opción:                      │");
            System.out.println("│  1. Chat simple (una pregunta)          │");
            System.out.println("│  2. Chat con contexto del sistema       │");
            System.out.println("│  3. Chat con historial (conversación)   │");
            System.out.println("│  4. Salir                               │");
            System.out.println("└─────────────────────────────────────────┘");
            System.out.print("Opción: ");

            String option = scanner.nextLine().trim();

            switch (option) {
                case "1" -> simpleChat(simpleChatUseCase, scanner);
                case "2" -> chatWithContext(simpleChatUseCase, scanner);
                case "3" -> chatWithHistory(simpleChatUseCase, scanner);
                case "4" -> {
                    System.out.println("\n Saliendo...");
                    scanner.close();
                    System.exit(0);
                }
                default -> System.out.println("Opción no válida");
            }
        }
    }

    private static void simpleChat(SimpleChatUseCase simpleChatUseCase, Scanner scanner) {
        System.out.println("CHAT SIMPLE");
        System.out.println("-------------------------------------------------");
        System.out.println("Tu mensaje: ");
        String message = scanner.nextLine();

        if (message.isBlank()) {
            System.out.println("El mensaje no puede estar vacio");
            return;
        }

        System.out.println("\nProcesando...\n");

        try {
            String response = simpleChatUseCase.execute(message);
            System.out.println("Gemini:");
            System.out.println(response);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void chatWithContext(SimpleChatUseCase simpleChatUseCase, Scanner scanner) {
        System.out.println("CHAT CONTEXT");
        System.out.println("-------------------------------------------------");
        System.out.println("(El contexto define como debe comportarse la IA)");
        System.out.println("Contexto del sistema: ");
        String context = scanner.nextLine();

        System.out.println("Tu mensaje:");
        String message = scanner.nextLine();

        if (context.isBlank() || message.isBlank()) {
            System.out.println("El mensaje no puede estar vacio");
            return;
        }

        System.out.println("\nProcesando...\n");

        try {
            String response = simpleChatUseCase.executeWithContext(context, message);
            System.out.println("Gemini (con contexto: " + context.substring(0, Math.min(30, context.length())) + "...):");
            System.out.println(response);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void chatWithHistory(SimpleChatUseCase simpleChatUseCase, Scanner scanner) {
        System.out.println("CHAT CON HISTORIAL (CONVERSACIÓN)");
        System.out.println("-------------------------------------------------");
        System.out.println("(Escribe 'salir' para terminar la conversación)");
        System.out.println();

        Conversation conversation = simpleChatUseCase.createConversation(
                "Eres un asistente amigable que ayuda con preguntas sobre programación."
        );

        while (true) {
            System.out.println("Tú: ");
            String message = scanner.nextLine();

            if (message.equalsIgnoreCase("salir")) {
                System.out.println("\nResumen de la conversación:");
                System.out.println("\tMensajes intercambiados: " + conversation.messages().size());
                break;
            }

            if (message.isBlank()) continue;

            System.out.println("\nProcesando...\n");

            try {
                conversation = simpleChatUseCase.executeWithConversation(conversation, message);

                ChatMessage lastMessage = conversation.messages().get(conversation.messages().size() - 1);
                System.out.println("Gemini: " + lastMessage.content());
                System.out.println();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

        }
    }
}
