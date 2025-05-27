package com.appidiomas.language.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IdiomaService {

    public boolean adicionarIdioma(String nome) {
        String sql = "INSERT INTO idiomas(nome) VALUES(?)";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.executeUpdate();
            System.out.println("Idioma '" + nome + "' adicionado com sucesso!");
            return true;
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                System.err.println("Erro: O idioma '" + nome + "' já existe.");
            } else {
                System.err.println("Erro ao adicionar idioma '" + nome + "': " + e.getMessage());
            }
            return false;
        }
    }

    public List<String> listarIdiomas() {
        List<String> idiomas = new ArrayList<>();
        String sql = "SELECT nome FROM idiomas ORDER BY nome";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                idiomas.add(rs.getString("nome"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar idiomas: " + e.getMessage());
        }
        return idiomas;
    }
}