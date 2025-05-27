package com.appidiomas.language.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:./data/idiomas.db";
    private static final String DATA_FOLDER_PATH = "./data";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void createTables() {
        File dataFolder = new File(DATA_FOLDER_PATH);
        if (!dataFolder.exists()) {
            if (dataFolder.mkdirs()) {
                System.out.println("Pasta 'data' criada com sucesso em: " + dataFolder.getAbsolutePath());
            } else {
                System.err.println("Erro: Não foi possível criar a pasta 'data'. Verifique as permissões.");
                return;
            }
        }

        String sql = "CREATE TABLE IF NOT EXISTS idiomas (" + "id INTEGER PRIMARY KEY AUTOINCREMENT," + "nome TEXT NOT NULL UNIQUE" + ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabela 'idiomas' verificada/criada com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao criar/verificar tabelas: " + e.getMessage());
        }
    }
}