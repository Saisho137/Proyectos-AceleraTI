package restexample.usecase;

import restexample.adapter.repository.ApiRepository;
import restexample.adapter.dto.SlideshowResponse;

/**
 * UseCase para obtener datos de una presentación de slideshow.
 * Implementa inyección de dependencias para permitir mocks en tests.
 */
public class GetSlideShowUseCase {
    private final ApiRepository apiRepository;

    /**
     * Constructor que inyecta la dependencia ApiRepository.
     * En tests, se puede inyectar un mock.
     * @param apiRepository Implementación de ApiRepository
     */
    public GetSlideShowUseCase(ApiRepository apiRepository) {
        this.apiRepository = apiRepository;
    }

    /**
     * Ejecuta la lógica para obtener y procesar datos de slideshow.
     * @return Descripción del slideshow o mensaje de error
     */
    public String execute() {
        try {
            SlideshowResponse data = apiRepository.getSlideshow();

            if (data == null || data.slideshow == null) {
                return "No data available";
            }
            String title = data.slideshow.title;
            String author = data.slideshow.author;

            long detailedSlidesCount = data.slideshow.slides.stream()
                    .filter(slide -> slide.items != null && !slide.items.isEmpty())
                    .count();

            return String.format("Presentation: %s by %s. Detailed slides: %d",
                    title, author, detailedSlidesCount);
        } catch (Exception e) {
            return "Error retrieving slideshow";
        }
    }
}
