package com.metricai.gamevault.model;

public class Jogo {

    // ATRIBUTOS PRIVADOS
    private int id;
    private String titulo;
    private String plataforma;
    private String genero;
    private int anoLancamento;
    private double nota;
    private boolean zerado;
    public String capaPath;

    // CONSTRUTOR VAZIO
    public Jogo(){
    }

    // CONTRUTOR COMPLETO
    public Jogo(String titulo, String plataforma, String genero, int anoLancamento, double nota, boolean zerado) {
        this.setTitulo(titulo);
        this.setPlataforma (plataforma);
        this.setGenero(genero);
        this.setAnoLancamento(anoLancamento);
        this.setNota(nota);
        this.setZerado(zerado);
    }

    // GETTER e SETTER = [id]
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    // GETTER e SETTER = [titulo]
    public String getTitulo(){
        return titulo;
    }
    public void setTitulo(String titulo){
        this.titulo = titulo;
    }

    // GETTER e SETTER = [plataforma]
    public String getPlataforma(){
        return plataforma;
    }
    public void setPlataforma(String plataforma){
        this.plataforma = plataforma;
    }

    // GETTER E SETTER = [genero]
    public String getGenero(){
        return genero;
    }
    public void setGenero(String genero){
        this.genero = genero;
    }

    // GETTER E SETTER = [anoLancamento]
    public int getAnoLancamento(){
        return anoLancamento;
    }
    public void setAnoLancamento(int anoLancamento){
        this.anoLancamento = anoLancamento;
    }

    // GETTER E SETTER = [nota]
    public double getNota(){
        return nota;
    }
    public void setNota(double nota) {
        if (nota < 0 || nota > 10) {
            System.out.println("Nota inválida! A nota deve ser entre 0 e 10.");
            return;
        }
        this.nota=nota;
    }

    // GETTER E SETTER = [zerado]
    public boolean isZerado(){
        return zerado;
    }
    public void setZerado(boolean zerado){
        this.zerado = zerado;
    }

    // GETTER E SETTER = [capaPath]
    public String getCapaPath(){
        return capaPath;
    }
    public void setCapaPath(String capaPath) {this.capaPath = capaPath;}

    // toString()
    @Override
    public String toString() {
        return " GAME VAULT - Detalhes do Jogo: " +
                "\nTítulo: " + titulo +
                "\nPlataforma: " + plataforma +
                "\nGênero: " + genero +
                "\nAno de Lançamento: " + anoLancamento +
                "\nNota: " + nota +
                "\nZerado: " + (zerado ? "Sim" : "Não");
    }
}