package com.enyoi.ragquery.ia.infrastructure.gemini;

import com.enyoi.ragquery.ia.domain.port.EmbeddingService;
import com.google.genai.types.ContentEmbedding;
import com.google.genai.types.EmbedContentResponse;
import com.google.genai.types.GenerateContentResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Estas pruebas serán para el adapter de embeddings con gemini")
class GeminiEmbeddingServiceTest {
    @Mock
    private GeminiClientWrapper mockClient;
    @InjectMocks
    private GeminiEmbeddingService geminiEmbeddingService;

    @Test
    @DisplayName("Se debe implementar el contrato")
    void testVerifyInterfaceImplementationGeminiAdapter() {
        assertTrue(EmbeddingService.class.isAssignableFrom(GeminiEmbeddingService.class));
    }

    @Test
    @DisplayName("Validar retorno de lista cuando no tiene el componente embedding")
    void testEmbedReturnsEmptyListWhenNoEmbeddingPresent() {
        String text = "";
        String model = "text-embedding-004";
        EmbedContentResponse mockResponse = mock(EmbedContentResponse.class);
        Optional<List<ContentEmbedding>> mockEmbeddings = Optional.empty();

        when(mockResponse.embeddings()).thenReturn(mockEmbeddings);
        when(mockClient.embedContent(eq(model), eq(text))).thenReturn(mockResponse);

        List<Float> result = geminiEmbeddingService.embed(text);

        assertAll(
                () -> assertTrue(result.isEmpty()),
                () -> verify(mockResponse, times(1)).embeddings(),
                () -> verify(mockClient, times(1)).embedContent(eq(model), eq(text))
        );
    }

    @Test
    @DisplayName("Validar generación de embedding")
    void testValidateEmbeddingGeneration() {
        String text = "Documento 1: Sobre arquitectura de hexagonal.";
        String model = "text-embedding-004";
        List<Float> embeddingValues = List.of(0.1f, 0.2f, 0.3f);
        EmbedContentResponse mockResponse = mock(EmbedContentResponse.class);
        Optional<List<ContentEmbedding>> mockEmbeddings = Optional.of(List.of(
                ContentEmbedding.builder().values(embeddingValues).build()
        ));

        when(mockResponse.embeddings()).thenReturn(mockEmbeddings);
        when(mockClient.embedContent(eq(model), eq(text))).thenReturn(mockResponse);

        List<Float> result = geminiEmbeddingService.embed(text);

        assertAll(
                () -> assertEquals(embeddingValues, result),
                () -> verify(mockResponse, times(3)).embeddings(),
                () -> verify(mockClient, times(1)).embedContent(eq(model), eq(text))
        );
    }

    @Test
    @DisplayName("Validar dimensiones de nuestro modelo de embedding")
    void testValidateGetDimensions() {
        assertEquals(768, geminiEmbeddingService.getDimensions());
    }

    @Test
    @DisplayName("Validar batcch embedding con 3 documentos")
    void testValidateBatchEmbeddingOk() {
        List<String> documents = List.of(
                "Documento 1",
                "Documento 2",
                "Documento 3"
        );
        String model = "text-embedding-004";
        List<Float> embeddingValuesDoc1 = List.of(0.1f, 0.2f, 0.3f);
        List<Float> embeddingValuesDoc2 = List.of(0.3f, 0.2f, 0.31f);
        List<Float> embeddingValuesDoc3 = List.of(0.1f, 0.3f, 0.1f);

        EmbedContentResponse mockResponse1 = mock(EmbedContentResponse.class);
        EmbedContentResponse mockResponse2 = mock(EmbedContentResponse.class);
        EmbedContentResponse mockResponse3 = mock(EmbedContentResponse.class);

        Optional<List<ContentEmbedding>> mockEmbeddings1 = Optional.of(List.of(
                ContentEmbedding.builder().values(embeddingValuesDoc1).build()
        ));
        Optional<List<ContentEmbedding>> mockEmbeddings2 = Optional.of(List.of(
                ContentEmbedding.builder().values(embeddingValuesDoc2).build()
        ));
        Optional<List<ContentEmbedding>> mockEmbeddings3 = Optional.of(List.of(
                ContentEmbedding.builder().values(embeddingValuesDoc3).build()
        ));

        when(mockResponse1.embeddings()).thenReturn(mockEmbeddings1);
        when(mockClient.embedContent(eq(model), eq(documents.get(0)))).thenReturn(mockResponse1);
        when(mockResponse2.embeddings()).thenReturn(mockEmbeddings2);
        when(mockClient.embedContent(eq(model), eq(documents.get(1)))).thenReturn(mockResponse2);
        when(mockResponse3.embeddings()).thenReturn(mockEmbeddings3);
        when(mockClient.embedContent(eq(model), eq(documents.get(2)))).thenReturn(mockResponse3);

        List<List<Float>> result = geminiEmbeddingService.embedBatch(documents);

        assertAll(
                () -> assertEquals(List.of(embeddingValuesDoc1, embeddingValuesDoc2, embeddingValuesDoc3), result),
                () -> verify(mockResponse1, times(3)).embeddings(),
                () -> verify(mockResponse2, times(3)).embeddings(),
                () -> verify(mockResponse3, times(3)).embeddings(),
                () -> verify(mockClient, times(3)).embedContent(eq(model), any())
        );
    }
}