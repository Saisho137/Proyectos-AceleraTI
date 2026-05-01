package co.com.arka.secretpoc.r2dbc.config;


public record PostgresqlConnectionProperties(
        String host,
        Integer port,
        String database,
        String username,
        String password) {
}
