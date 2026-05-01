package dbexample.adapter;

import dbexample.adapter.repository.TaskRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de TaskRepository usando SQLite.
 * En tests, este adaptador puede ser reemplazado por un mock.
 */
public class SQLiteTaskAdapter implements TaskRepository {
    private final String url;

    /**
     * Constructor que inyecta la URL de la BD.
     * Permite cambiar la BD en tests si es necesario.
     * @param url URL de conexión JDBC a SQLite
     */
    public SQLiteTaskAdapter(String url) {
        this.url = url;
    }

    /**
     * Constructor por defecto con URL estándar de producción.
     */
    public SQLiteTaskAdapter() {
        this("jdbc:sqlite:src/main/resources/db.sqlite");
    }

    @Override
    public void save(String title) {
        String sql = "INSERT INTO tasks(title) VALUES(?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<String> findAllTitles() {
        List<String> titles = new ArrayList<>();
        String sql = "SELECT title FROM tasks";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                titles.add(rs.getString("title"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return titles;
    }
}
