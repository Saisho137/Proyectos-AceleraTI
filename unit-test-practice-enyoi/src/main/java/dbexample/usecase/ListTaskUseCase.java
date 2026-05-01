package dbexample.usecase;

import dbexample.adapter.repository.TaskRepository;
import java.util.List;

public class ListTaskUseCase {
    private final TaskRepository taskRepository;

    public ListTaskUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<String> execute() {
        return taskRepository.findAllTitles();
    }
}
