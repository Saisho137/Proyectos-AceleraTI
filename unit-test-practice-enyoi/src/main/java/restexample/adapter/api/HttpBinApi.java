package restexample.adapter.api;

import restexample.adapter.dto.IpResponse;
import restexample.adapter.dto.SlideshowResponse;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Interfaz de Retrofit para consumir APIs HTTP.
 * Usada por RestApiAdapter para hacer llamadas REST.
 */
public interface HttpBinApi {
    @GET("ip")
    Call<IpResponse> getIp();

    @GET("json")
    Call<SlideshowResponse> getSlideshow();
}
