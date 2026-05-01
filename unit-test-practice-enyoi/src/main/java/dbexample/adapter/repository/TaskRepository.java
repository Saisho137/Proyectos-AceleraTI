package dbexample.adapter.repository;

import java.util.List;

/**
 * Interfaz que define el contrato para acceder a las tareas en persistencia.
 * 
 */
public interface TaskRepository {
    /**
     * Guarda una nueva tarea en el repositorio.
     * @param title Título de la tarea
     */
    void save(String title);

    /**
     * Obtiene todos los títulos de tareas almacenadas.
     * @return Lista de títulos de tareas
     */
    List<String> findAllTitles();
}
