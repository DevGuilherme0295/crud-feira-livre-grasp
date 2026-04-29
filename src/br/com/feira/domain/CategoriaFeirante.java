package br.com.feira.domain;


public class CategoriaFeirante {

    private Long id;
    private String nome;
    private String descricao;

    public CategoriaFeirante() {
    }


    public CategoriaFeirante(Long id, String nome, String descricao) {
        this.id = id;
        setNome(nome);
        this.descricao = descricao;
    }


    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da categoria é obrigatório.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }


    public void setNome(String nome) {
        validarNome(nome);
        this.nome = nome.trim();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}