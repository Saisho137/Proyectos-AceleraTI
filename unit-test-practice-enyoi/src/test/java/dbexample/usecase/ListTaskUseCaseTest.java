package dbexample.usecase;

import dbexample.adapter.repository.TaskRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ListTaskUseCaseTest {
    @Mock
    private TaskRepository taskRepository;
    // Como es una interfaz, no es necesario indicarle qué debe retornar el mock
    private ListTaskUseCase listTaskUseCase;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        listTaskUseCase = new ListTaskUseCase(taskRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    @DisplayName("Debe retornar la lista de títulos de tareas")
    void testExecute_Successful() {
        List<String> tasksMock = List.of("task1", "task2", "task3");

        when(taskRepository.findAllTitles()).thenReturn(tasksMock);

        List<String> result = listTaskUseCase.execute();

        assertEquals(tasksMock, result);
    }
}