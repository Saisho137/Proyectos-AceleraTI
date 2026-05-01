package dbexample.usecase;

import dbexample.adapter.repository.TaskRepository;

public class CreateTaskUseCase {
    private final TaskRepository repository;

    public CreateTaskUseCase(TaskRepository repository) {
        this.repository = repository;
    }

    public boolean execute(String title) {
        if (title == null || title.trim().length() < 3) {
            return false;
        }
        repository.save(title);
        return true;
    }
}
