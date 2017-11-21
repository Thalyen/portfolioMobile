package br.edu.ifrn.portfolio.modelo;

import com.orm.SugarRecord;

public class Area extends SugarRecord {
    private String id;
    private String nome;

    public Area() { super();}

    public Area(String id, String nome) {
        this.nome = nome;
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}