package com.appidiomas.language.db;

import com.appidiomas.language.model.Idioma;
import com.appidiomas.language.model.Topico;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:appidiomas.db";

    public DatabaseManager() {
        createTables();
    }

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
        return conn;
    }

    private void createTables() {
        String createIdiomasTable = "CREATE TABLE IF NOT EXISTS idiomas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL UNIQUE" +
                ");";

        String createTopicosTable = "CREATE TABLE IF NOT EXISTS topicos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL," +
                "idioma_id INTEGER NOT NULL," +
                "ordem INTEGER NOT NULL," +
                "estudado BOOLEAN NOT NULL DEFAULT 0," +
                "link_estudo TEXT," +
                "FOREIGN KEY (idioma_id) REFERENCES idiomas(id) ON DELETE CASCADE," +
                "UNIQUE (nome, idioma_id)" +
                ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createIdiomasTable);
            stmt.execute(createTopicosTable);
        } catch (SQLException e) {
            System.err.println("Erro ao criar tabelas: " + e.getMessage());
        }
    }

    public boolean insertIdioma(String nome) {
        String sql = "INSERT INTO idiomas(nome) VALUES(?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir idioma: " + e.getMessage());
            return false;
        }
    }

    public List<Idioma> selectAllIdiomas() {
        List<Idioma> idiomas = new ArrayList<>();
        String sql = "SELECT id, nome FROM idiomas ORDER BY nome";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                idiomas.add(new Idioma(rs.getInt("id"), rs.getString("nome")));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao selecionar idiomas: " + e.getMessage());
        }
        return idiomas;
    }

    public boolean updateIdioma(int id, String novoNome) {
        String sql = "UPDATE idiomas SET nome = ? WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, novoNome);
            pstmt.setInt(2, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar idioma: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteIdioma(int id) {
        String sql = "DELETE FROM idiomas WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar idioma: " + e.getMessage());
            return false;
        }
    }

    public boolean insertTopico(String nome, int idiomaId, int ordem, boolean estudado, String linkEstudo) {
        String sql = "INSERT INTO topicos(nome, idioma_id, ordem, estudado, link_estudo) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setInt(2, idiomaId);
            pstmt.setInt(3, ordem);
            pstmt.setBoolean(4, estudado);
            pstmt.setString(5, linkEstudo);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir tópico: " + e.getMessage());
            return false;
        }
    }

    public List<Topico> selectTopicosByIdioma(int idiomaId) {
        List<Topico> topicos = new ArrayList<>();
        String sql = "SELECT id, nome, idioma_id, ordem, estudado, link_estudo FROM topicos WHERE idioma_id = ? ORDER BY ordem";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idiomaId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                topicos.add(new Topico(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getInt("idioma_id"),
                        rs.getInt("ordem"),
                        rs.getBoolean("estudado"),
                        rs.getString("link_estudo")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao selecionar tópicos por idioma: " + e.getMessage());
        }
        return topicos;
    }

    public boolean updateTopico(int id, String nome, int ordem, boolean estudado, String linkEstudo) {
        String sql = "UPDATE topicos SET nome = ?, ordem = ?, estudado = ?, link_estudo = ? WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setInt(2, ordem);
            pstmt.setBoolean(3, estudado);
            pstmt.setString(4, linkEstudo);
            pstmt.setInt(5, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar tópico: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteTopico(int id) {
        String sql = "DELETE FROM topicos WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar tópico: " + e.getMessage());
            return false;
        }
    }
}