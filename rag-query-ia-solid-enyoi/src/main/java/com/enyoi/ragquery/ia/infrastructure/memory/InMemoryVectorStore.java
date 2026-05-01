package com.enyoi.ragquery.ia.infrastructure.memory;

import com.enyoi.ragquery.ia.domain.model.Document;
import com.enyoi.ragquery.ia.domain.port.EmbeddingService;
import com.enyoi.ragquery.ia.domain.port.VectorStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryVectorStore implements VectorStore {
    private final Map<String, Document> documents;
    private final EmbeddingService embeddingService;

    public InMemoryVectorStore(EmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
        this.documents = new HashMap<>();
    }

    @Override
    public void store(Document document) {
        documents.put(document.id(), document);
    }

    @Override
    public void storeBatch(List<Document> documents) {
        for (Document document : documents) {
            store(document);
        }
    }

    @Override
    public int size() {
        return documents.size();
    }

    @Override
    public List<Document> similaritySearch(List<Float> queryEmbedding, int topK) {
        if (documents.isEmpty() || queryEmbedding.isEmpty()) {
            return List.of();
        }

        List<ScoredDocument> scoredDocuments = new ArrayList<>();

        for (Document document : documents.values()) {
            if (document.embedding().isEmpty())
                continue;
            scoredDocuments.add(new ScoredDocument(
                    document,
                    cosineSimilarity(queryEmbedding, document.embedding())
            ));
        }

        return scoredDocuments.stream()
                .sorted((d1, d2) ->
                        Double.compare(d2.score, d1.score))
                .limit(topK)
                .map(ScoredDocument::document)
                .toList();
    }

    @Override
    public List<Document> similaritySearch(String query, int topK) {
        List<Float> embeddingQuery = embeddingService.embed(query);
        return similaritySearch(embeddingQuery, topK);
    }

    @Override
    public boolean contains(String documentId) {
        return documents.containsKey(documentId);
    }

    @Override
    public void delete(String documentId) {
        documents.remove(documentId);
    }

    @Override
    public void clear() {
        documents.clear();
    }

    private record ScoredDocument(Document document, double score) {
    }

    private double cosineSimilarity(List<Float> vecA, List<Float> vecB) {
        if (vecA.size() != vecB.size() || vecA.isEmpty()) {
            throw new IllegalArgumentException("Vectors must be of same non-zero length");
        }

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < vecA.size(); i++) {
            dotProduct += vecA.get(i) * vecB.get(i);
            normA += (vecA.get(i) * vecA.get(i));
            normB += (vecB.get(i) * vecB.get(i));
        }

        if (normA == 0 || normB == 0) {
            return 0.0;
        }

        normA = Math.sqrt(normA);
        normB = Math.sqrt(normB);

        return dotProduct / (normA * normB);
    }
}
