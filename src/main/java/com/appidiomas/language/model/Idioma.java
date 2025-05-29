package com.appidiomas.language.model;

public class Idioma {
    private int id;
    private String nome;
    private int totalDeTopicos;
    private int topicosEstudados;

    public Idioma(int id, String nome) {
        this.id = id;
        this.nome = nome;
        this.totalDeTopicos = 0;
        this.topicosEstudados = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getTotalDeTopicos() {
        return totalDeTopicos;
    }

    public void setTotalDeTopicos(int totalDeTopicos) {
        this.totalDeTopicos = totalDeTopicos;
    }

    public int getTopicosEstudados() {
        return topicosEstudados;
    }

    public void setTopicosEstudados(int topicosEstudados) {
        this.topicosEstudados = topicosEstudados;
    }

    @Override
    public String toString() {
        return nome;
    }
}