package com.appidiomas.language.db;

import com.appidiomas.language.model.Topico;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TopicoDB {

    public List<Topico> buscarPorIdioma(int idiomaId) {
        List<Topico> lista = new ArrayList<>();
        String sql = "SELECT * FROM topicos WHERE idioma_id = ? ORDER BY ordem";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idiomaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Topico topico = new Topico(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getInt("idioma_id"),
                        rs.getInt("ordem"),
                        rs.getBoolean("estudado"),
                        rs.getString("link_estudo")
                );
                lista.add(topico);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar tópicos: " + e.getMessage());
        }
        return lista;
    }

    public boolean inserir(Topico topico) {
        String sql = "INSERT INTO topicos (nome, idioma_id, ordem, estudado, link_estudo) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, topico.getNome());
            stmt.setInt(2, topico.getIdiomaId());
            stmt.setInt(3, topico.getOrdem());
            stmt.setBoolean(4, topico.isEstudado());
            stmt.setString(5, topico.getLinkEstudo());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir tópico: " + e.getMessage());
            return false;
        }
    }

    public boolean atualizar(Topico topico) {
        String sql = "UPDATE topicos SET nome = ?, ordem = ?, estudado = ?, link_estudo = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, topico.getNome());
            stmt.setInt(2, topico.getOrdem());
            stmt.setBoolean(3, topico.isEstudado());
            stmt.setString(4, topico.getLinkEstudo());
            stmt.setInt(5, topico.getId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar tópico: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM topicos WHERE id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir tópico: " + e.getMessage());
            return false;
        }
    }
}