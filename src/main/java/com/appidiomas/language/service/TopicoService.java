package com.appidiomas.language.service;

import com.appidiomas.language.db.DatabaseManager;
import com.appidiomas.language.model.Topico;
import java.util.List;

public class TopicoService {
    private DatabaseManager dbManager;

    public TopicoService() {
        this.dbManager = new DatabaseManager();
    }

    public boolean adicionarTopico(String nome, int idiomaId, int ordem, boolean estudado, String linkEstudo) {
        return dbManager.insertTopico(nome, idiomaId, ordem, estudado, linkEstudo);
    }

    public List<Topico> listarTopicosPorIdioma(int idiomaId) {
        return dbManager.selectTopicosByIdioma(idiomaId);
    }

    public boolean atualizarTopico(int id, String nome, int ordem, boolean estudado, String linkEstudo) {
        return dbManager.updateTopico(id, nome, ordem, estudado, linkEstudo);
    }

    public boolean removerTopico(int id) {
        return dbManager.deleteTopico(id);
    }
}