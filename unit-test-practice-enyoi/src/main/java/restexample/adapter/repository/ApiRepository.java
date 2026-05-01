package restexample.adapter.repository;

import restexample.adapter.dto.IpResponse;
import restexample.adapter.dto.SlideshowResponse;

/**
 * Interfaz que define el contrato para acceder a APIs REST.
 * Permite inversión de dependencias e inyección de mocks en tests.
 */
public interface ApiRepository {
    /**
     * Obtiene la dirección IP pública.
     * @return Objeto IpResponse con la IP
     * @throws Exception Si ocurre un error en la llamada
     */
    IpResponse getPublicIp() throws Exception;

    /**
     * Obtiene datos de presentación de slideshow.
     * @return Objeto SlideshowResponse con los datos
     * @throws Exception Si ocurre un error en la llamada
     */
    SlideshowResponse getSlideshow() throws Exception;
}
