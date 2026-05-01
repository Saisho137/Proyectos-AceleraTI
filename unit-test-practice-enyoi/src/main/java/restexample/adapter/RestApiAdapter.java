package restexample.adapter;

import restexample.adapter.api.HttpBinApi;
import restexample.adapter.dto.SlideshowResponse;
import restexample.adapter.dto.IpResponse;
import restexample.adapter.repository.ApiRepository;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.Objects;

/**
 * Adaptador REST que implementa ApiRepository.
 * Realiza llamadas HTTP usando Retrofit.
 * 
 * Con inyección de dependencias: permite mockear en tests.
 */
public class RestApiAdapter implements ApiRepository {
    private final HttpBinApi api;

    /**
     * Constructor que inyecta la instancia de Retrofit.
     * Permite usar un Retrofit mockeado en tests.
     * @param retrofit Instancia de Retrofit
     */
    public RestApiAdapter(Retrofit retrofit) {
        this.api = retrofit.create(HttpBinApi.class);
    }

    /**
     * Constructor alternativo que crea su propia instancia de Retrofit.
     * Úsalo solo en producción, no en tests.
     */
    public RestApiAdapter() {
        this(createDefaultRetrofit());
    }

    /**
     * Crea una instancia por defecto de Retrofit para producción.
     * Separado en método estático para poder inyectarlo.
     */
    private static Retrofit createDefaultRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://httpbin.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    
    @Override
    public IpResponse getPublicIp() throws Exception {
        return Objects.requireNonNull(api.getIp().execute().body());
    }

    @Override
    public SlideshowResponse getSlideshow() throws Exception {
        return api.getSlideshow().execute().body();
    }
}
