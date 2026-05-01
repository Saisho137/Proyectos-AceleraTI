package restexample.usecase;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import restexample.adapter.dto.IpResponse;
import restexample.adapter.repository.ApiRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetLocationUseCaseTest {
    @Mock
    private ApiRepository mockApiRepository;
    private GetLocationUseCase getLocationUseCase;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        /* mockApiRepository = mock(ApiRepository.class);
        Usando Mockito @Mock para crear el mock */
        closeable = MockitoAnnotations.openMocks(this);
        getLocationUseCase = new GetLocationUseCase(mockApiRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    @Tag("Tag-GetLocationUseCase")
    @DisplayName("Debe retornar LOCAL_NETWORK para IPs locales")
    void testExecute_Local() throws Exception {
        // Arrange
        IpResponse fakeIpResponse = new IpResponse();
        fakeIpResponse.origin = "127.1.1.0";
        when(mockApiRepository.getPublicIp()).thenReturn(fakeIpResponse);

        // Act
        String result = getLocationUseCase.execute();

        // Assert
        assertEquals("LOCAL_NETWORK", result);
    }

    @Test
    @Tag("Tag-GetLocationUseCase")
    @DisplayName("Debe retornar la IP mandada para IPs no locales")
    void testExecute_NonLocal() throws Exception {
        // Arrange
        IpResponse fakeIpResponse = new IpResponse();
        fakeIpResponse.origin = "201.128.1.1";
        when(mockApiRepository.getPublicIp()).thenReturn(fakeIpResponse);

        // Act
        String result = getLocationUseCase.execute();

        // Assert
        assertEquals("201.128.1.1", result);
    }

    @Test
    @Tag("Tag-GetLocationUseCase")
    @DisplayName("Debe retornar SERVICE_UNAVAILABLE si hay un error")
    void testExecute_Error() throws Exception {
        // Arrange
        IpResponse fakeIpResponse = new IpResponse();
        fakeIpResponse.origin = "201.128.1.1";
        when(mockApiRepository.getPublicIp()).thenThrow(new Exception());

        // Act
        String result = getLocationUseCase.execute();

        // Assert
        assertEquals("SERVICE_UNAVAILABLE", result);
    }

}