package com.appidiomas.language.service;

import com.appidiomas.language.db.DatabaseManager;
import com.appidiomas.language.model.Idioma;
import com.appidiomas.language.model.Topico;
import java.util.List;
import java.util.ArrayList;

public class IdiomaService {
    private DatabaseManager dbManager;
    private TopicoService topicoService;

    public IdiomaService() {
        this.dbManager = new DatabaseManager();
        this.topicoService = new TopicoService();
    }

    public boolean adicionarIdioma(String nome) {
        return dbManager.insertIdioma(nome);
    }

    public List<Idioma> listarIdiomas() {
        List<Idioma> idiomas = dbManager.selectAllIdiomas();
        for (Idioma idioma : idiomas) {
            List<Topico> topicosDoIdioma = topicoService.listarTopicosPorIdioma(idioma.getId());
            idioma.setTotalDeTopicos(topicosDoIdioma.size());
            long estudados = topicosDoIdioma.stream().filter(Topico::isEstudado).count();
            idioma.setTopicosEstudados((int) estudados);
        }
        return idiomas;
    }

    public boolean atualizarIdioma(int id, String novoNome) {
        return dbManager.updateIdioma(id, novoNome);
    }

    public boolean removerIdioma(int id) {
        return dbManager.deleteIdioma(id);
    }
}