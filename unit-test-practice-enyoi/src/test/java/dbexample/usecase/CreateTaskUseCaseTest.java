package dbexample.usecase;

import dbexample.adapter.repository.TaskRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CreateTaskUseCaseTest {
    @Mock
    private TaskRepository taskRepository;
    // Como es una interfaz, no es necesario indicarle qué debe retornar el mock
    private CreateTaskUseCase createTaskUseCase;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        createTaskUseCase = new CreateTaskUseCase(taskRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @ParameterizedTest
    @DisplayName("Probar que se creen tareas con más de 3 caracteres")
    @CsvSource({
            "Example Task",
            "Tarea 1",
    })
    void execute_Successful(String task) {
        assertTrue(createTaskUseCase.execute(task));
    }

    @ParameterizedTest
    @DisplayName("Probar que se creen tareas con más de 3 caracteres")
    @CsvSource(value = {
            "null",
            "a",
            "aa",
    }, nullValues = {"null"})
    void execute_NullOrInvalid(String task) {
        assertFalse(createTaskUseCase.execute(task));
    }
}