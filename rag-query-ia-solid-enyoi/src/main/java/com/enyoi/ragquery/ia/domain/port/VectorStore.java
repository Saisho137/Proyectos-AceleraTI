package com.enyoi.ragquery.ia.domain.port;

import com.enyoi.ragquery.ia.domain.model.Document;

import java.util.List;

public interface VectorStore {
    void store(Document document);

    void storeBatch(List<Document> documents);

    int size();

    List<Document> similaritySearch(List<Float> queryEmbedding, int topK);

    List<Document> similaritySearch(String query, int topK);

    boolean contains(String documentId);

    void delete(String documentId);

    void clear();
}
