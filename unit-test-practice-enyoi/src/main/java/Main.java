import dbexample.adapter.SQLiteTaskAdapter;
import dbexample.usecase.CreateTaskUseCase;
import dbexample.usecase.ListTaskUseCase;
import restexample.adapter.RestApiAdapter;
import restexample.usecase.GetLocationUseCase;
import restexample.usecase.GetSlideShowUseCase;

public class Main {
    public static void main(String[] args) {
        /*RestApiAdapter adapter = new RestApiAdapter();
        GetLocationUseCase getLocationUseCase = new GetLocationUseCase(adapter);
        GetSlideShowUseCase getSlideShowUseCase = new GetSlideShowUseCase(adapter);

        System.out.println("Respuesta: " + getLocationUseCase.execute());
        System.out.println("Respuesta: " + getSlideShowUseCase.execute());*/

        SQLiteTaskAdapter sqliteTaskAdapter = new SQLiteTaskAdapter();
        CreateTaskUseCase createTaskUseCase = new CreateTaskUseCase(sqliteTaskAdapter);
        ListTaskUseCase listTaskUseCase = new ListTaskUseCase(sqliteTaskAdapter);

        System.out.println("Respuesta: " + createTaskUseCase.execute("Mi tarea 1"));
        System.out.println("Respuesta: " + listTaskUseCase.execute());
    }
}
