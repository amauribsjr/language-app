package com.appidiomas.language.model;

public class Topico {
    private int id;
    private String nome;
    private int idiomaId;
    private int ordem;
    private boolean estudado;
    private String linkEstudo;

    public Topico(int id, String nome, int idiomaId, int ordem, boolean estudado, String linkEstudo) {
        this.id = id;
        this.nome = nome;
        this.idiomaId = idiomaId;
        this.ordem = ordem;
        this.estudado = estudado;
        this.linkEstudo = linkEstudo;
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

    public int getIdiomaId() {
        return idiomaId;
    }

    public void setIdiomaId(int idiomaId) {
        this.idiomaId = idiomaId;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public boolean isEstudado() {
        return estudado;
    }

    public void setEstudado(boolean estudado) {
        this.estudado = estudado;
    }

    public String getLinkEstudo() {
        return linkEstudo;
    }

    public void setLinkEstudo(String linkEstudo) {
        this.linkEstudo = linkEstudo;
    }

    @Override
    public String toString() {
        return nome;
    }
}