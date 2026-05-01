package restexample.adapter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import restexample.adapter.api.HttpBinApi;
import restexample.adapter.dto.IpResponse;
import restexample.adapter.dto.SlideshowResponse;
import retrofit2.Retrofit;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RestApiAdapterTest {
    @Mock
    private Retrofit retrofit;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) // Sirve para mocks anidados
    private HttpBinApi httpBinApi;
    private RestApiAdapter restApiAdapter;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        when(retrofit.create(HttpBinApi.class)).thenReturn(httpBinApi);
        restApiAdapter = new RestApiAdapter(retrofit);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    @DisplayName("Testear getPublicIp method del RestApiAdapter")
    void testGetPublicIp() throws Exception {
        IpResponse ipResponse = new IpResponse();
        ipResponse.origin = "123.456.789.000";

       /*
        when(httpBinApi.getIp()).thenReturn(Mockito.mock());
        when(httpBinApi.getIp().execute()).thenReturn(Mockito.mock());
        Mala práctica, es fuerza bruta de ejemplo
       */

        when(httpBinApi.getIp().execute().body()).thenReturn(ipResponse);

        IpResponse result = restApiAdapter.getPublicIp();

        assertEquals(ipResponse.origin, result.origin);
    }

    @Test
    @DisplayName("Testear getSlideShow method del RestApiAdapter")
    void testGetSlideShow() throws Exception {
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

        when(httpBinApi.getSlideshow().execute().body()).thenReturn(slideshowResponse);

        SlideshowResponse result = restApiAdapter.getSlideshow();

        assertEquals(slideshowResponse, result);
    }
}