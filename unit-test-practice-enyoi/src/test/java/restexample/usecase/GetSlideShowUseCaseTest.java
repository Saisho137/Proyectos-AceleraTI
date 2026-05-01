package restexample.usecase;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import restexample.adapter.dto.SlideshowResponse;
import restexample.adapter.repository.ApiRepository;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GetSlideShowUseCaseTest {
    @Mock
    private ApiRepository mockApiRepository;
    private GetSlideShowUseCase getSlideShowUseCase;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        getSlideShowUseCase = new GetSlideShowUseCase(mockApiRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    @DisplayName("Debe retornar el conteo correcto de slides detallados")
    void testExecute_Successful() throws Exception {
        SlideshowResponse slideshowResponse = new SlideshowResponse();
        slideshowResponse.slideshow = new SlideshowResponse.SlideshowData();
        slideshowResponse.slideshow.title = "Title";
        slideshowResponse.slideshow.author = "Author";
        slideshowResponse.slideshow.date = "2018-04-04";

        SlideshowResponse.Slide slide1 = new SlideshowResponse.Slide();
        slide1.items = List.of();
        SlideshowResponse.Slide slide2 = new SlideshowResponse.Slide();
        slide2.items = List.of("Item 1", "", "Item 2", "Item 3");
        SlideshowResponse.Slide slide3 = new SlideshowResponse.Slide();
        slide3.items = null;

        slideshowResponse.slideshow.slides = List.of(
                slide1,
                slide2,
                slide3
        );

        when(mockApiRepository.getSlideshow()).thenReturn(slideshowResponse);

        String result = getSlideShowUseCase.execute();

        assertEquals("Presentation: Title by Author. Detailed slides: 1", result);
    }

    @ParameterizedTest
    @DisplayName("Test con datos nulos")
    @MethodSource("testExecute_NoData")
    void testExecute_NoData(SlideshowResponse fake) throws Exception {
        when(mockApiRepository.getSlideshow()).thenReturn(fake);

        String result = getSlideShowUseCase.execute();

        assertEquals("No data available", result);
    }

    @Test
    @DisplayName("Test con un error lanzado por el repositorio")
    void testExecute_Error() throws Exception {
        when(mockApiRepository.getSlideshow()).thenThrow(new Exception());

        String result = getSlideShowUseCase.execute();

        assertEquals("Error retrieving slideshow", result);
    }

    private static Stream<Arguments> testExecute_NoData() {
        SlideshowResponse slideshowResponse = new SlideshowResponse();
        slideshowResponse.slideshow = null;

        return Stream.of(
                null,
                Arguments.of(slideshowResponse)
        );
    }
}