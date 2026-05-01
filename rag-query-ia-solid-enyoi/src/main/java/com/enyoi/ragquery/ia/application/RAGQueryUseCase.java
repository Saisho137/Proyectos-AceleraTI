package com.enyoi.ragquery.ia.application;

import com.enyoi.ragquery.ia.domain.model.Document;
import com.enyoi.ragquery.ia.domain.port.ChatService;
import com.enyoi.ragquery.ia.domain.port.EmbeddingService;
import com.enyoi.ragquery.ia.domain.port.VectorStore;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class RAGQueryUseCase {
    private final EmbeddingService embeddingService;
    private final VectorStore vectorStore;
    private final ChatService chatService;

    private int defaultTopK = 3;
    private static final String RAG_SYSTEM_PROMPT = """
             Eres un asistente experto que responde preguntas basándose ÚNICAMENTE en el contexto proporcionado.
             REGLAS:
                1. Responde SOLO con información del contexto dado
                2. Si la información no está en el contexto, di "No tengo información suficiente para responder esa pregunta"
                3. Cita las fuentes cuando sea posible
                4. Sé conciso pero completo
            
             CONTEXTO:
             %s
            """;
    private static final String NO_CONTEXT_RESPONSE = "No tengo información suficiente para responder esa pregunta. " +
            "No encontré documentos relevantes en mi base de conocimiento.";

    public RAGQueryUseCase(EmbeddingService embeddingService, VectorStore vectorStore, ChatService chatService) {
        this.embeddingService = embeddingService;
        this.vectorStore = vectorStore;
        this.chatService = chatService;
    }

    public String query(String question) {
        validate(question, "La pregunta no puede estar vacía");
        List<Float> questionEmbedding = embeddingService.embed(question);
        List<Document> relevantDocs = vectorStore.similaritySearch(
                questionEmbedding, defaultTopK);

        if (relevantDocs.isEmpty()) return NO_CONTEXT_RESPONSE;

        String context = buildContext(relevantDocs);
        String systemPrompt = String.format(RAG_SYSTEM_PROMPT, context);

        return chatService.chatWithSystemPrompt(systemPrompt, question);
    }

    public String query(String question, int customTopK) {
        setDefaultTopK(customTopK);
        return query(question);
    }

    public RAGResult queryWithSources(String question) {
        validate(question, "La pregunta no puede estar vacía");
        List<Float> questionEmbedding = embeddingService.embed(question);
        List<Document> relevantDocs = vectorStore.similaritySearch(
                questionEmbedding, defaultTopK);

        if (relevantDocs.isEmpty()) return new RAGResult(
                NO_CONTEXT_RESPONSE,
                List.of()
        );

        String context = buildContext(relevantDocs);
        String systemPrompt = String.format(RAG_SYSTEM_PROMPT, context);

        String answer = chatService.chatWithSystemPrompt(systemPrompt, question);
        return new RAGResult(answer, relevantDocs);
    }

    public record RAGResult(String answer, List<Document> sourceDocs) {
    }

    public void ingestDocument(String content, Map<String, Object> metadata) {
        validate(content, "El contenido no puede estar vacío");
        String docId = UUID.randomUUID().toString();
        List<Float> embedding = embeddingService.embed(content);
        Document document = new Document(docId, content, metadata, embedding);

        vectorStore.store(document);
    }

    public void setDefaultTopK(int topK) {
        if (topK <= 0)
            throw new IllegalArgumentException("topK debe ser al menos 1");
        this.defaultTopK = topK;
    }

    private void validate(String data, String message) {
        if (data == null || data.isBlank())
            throw new IllegalArgumentException(message);
    }

    private String buildContext(List<Document> documents) {
        return documents.stream()
                .map( doc -> {
                    String source = doc.getMetadataValue("source");
                    String sourceInfo = (source != null) ? " [Fuente: " + source + "]: \n " : "";
                    return sourceInfo  + "\t" + doc.content();
                })
                .collect(Collectors.joining("\n\n--"));
    }
}
