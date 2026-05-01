package restexample.usecase;

import restexample.adapter.repository.ApiRepository;

/**
 * UseCase para obtener la ubicación basada en la IP pública.
 * Implementa inyección de dependencias para permitir mocks en tests.
 */
public class GetLocationUseCase {
    private final ApiRepository apiRepository;

    /**
     * Constructor que inyecta la dependencia ApiRepository.
     * En tests, se puede inyectar un mock.
     * @param apiRepository Implementación de ApiRepository
     */
    public GetLocationUseCase(ApiRepository apiRepository) {
        this.apiRepository = apiRepository;
    }

    /**
     * Ejecuta la lógica para obtener la ubicación.
     * @return IP pública o "LOCAL_NETWORK" o "SERVICE_UNAVAILABLE"
     */
    public String execute() {
        try {
            String ip = apiRepository.getPublicIp().origin;
            if (ip.startsWith("127.")) {
                return "LOCAL_NETWORK";
            }
            return ip;
        } catch (Exception e) {
            return "SERVICE_UNAVAILABLE";
        }
    }
}
