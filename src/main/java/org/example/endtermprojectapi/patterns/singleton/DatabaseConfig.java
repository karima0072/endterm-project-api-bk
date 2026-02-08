package org.example.endtermprojectapi.patterns.singleton;

import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DatabaseConfig {

    private static DatabaseConfig instance;

    private DataSource dataSource;

    private DatabaseConfig() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://localhost:5432/endterm_db");
        ds.setUsername("postgres");
        ds.setPassword("1234");

        this.dataSource = ds;
    }

    public static DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}