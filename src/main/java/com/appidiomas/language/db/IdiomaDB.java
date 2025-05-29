package com.appidiomas.language.db;

import com.appidiomas.language.model.Idioma;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IdiomaDB {

    public List<Idioma> listar() {
        List<Idioma> lista = new ArrayList<>();
        String sql = "SELECT * FROM idiomas ORDER BY nome ASC";

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Idioma idioma = new Idioma(
                        rs.getInt("id"),
                        rs.getString("nome")
                );
                lista.add(idioma);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar idiomas: " + e.getMessage());
        }
        return lista;
    }

    public boolean inserir(String nome) {
        String sql = "INSERT INTO idiomas (nome) VALUES (?)";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir idioma: " + e.getMessage());
            return false;
        }
    }

    public boolean atualizar(int id, String nome) {
        String sql = "UPDATE idiomas SET nome = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setInt(2, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar idioma: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM idiomas WHERE id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir idioma: " + e.getMessage());
            return false;
        }
    }
}