package br.com.feira.domain;


public class Feirante {

    private Long id;
    private String nome;
    private String descricao;
    private boolean ativo;
    private CategoriaFeirante categoriaFeirante;
    private String cpf;

    public Feirante() {
    }


    public Feirante(Long id, String nome, String cpf, String descricao, boolean ativo, CategoriaFeirante categoriaFeirante) {
        this.id = id;
        setNome(nome);
        setCpf(cpf);
        this.descricao = descricao;
        this.ativo = ativo;
        setCategoriaFeirante(categoriaFeirante);
    }


    private void validarNome(String nome) {
        if (nome == null || nome.trim().length() < 3) {
            throw new IllegalArgumentException("O nome do feirante deve ter pelo menos 3 caracteres.");
        }
    }

    private void validarCpf(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF deve conter exatamente 11 dígitos numéricos.");
        }
    }

    public void setCpf(String cpf) {
        validarCpf(cpf);
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }


    private void validarCategoria(CategoriaFeirante categoriaFeirante) {
        if (categoriaFeirante == null) {
            throw new IllegalArgumentException("A categoria do feirante é obrigatória.");
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

    public boolean isAtivo() {
        return ativo;
    }

    public CategoriaFeirante getCategoriaFeirante() {
        return categoriaFeirante;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public void setNome(String nome) {
        validarNome(nome);
        this.nome = nome.trim();
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }


    public void setCategoriaFeirante(CategoriaFeirante categoriaFeirante) {
        validarCategoria(categoriaFeirante);
        this.categoriaFeirante = categoriaFeirante;
    }
}