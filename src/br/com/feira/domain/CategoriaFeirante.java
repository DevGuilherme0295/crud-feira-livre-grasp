package br.com.feira.domain;

/**
 * Representa uma categoria de feirante.
 * Responsável por manter seus próprios dados válidos,
 * aplicando o princípio GRASP Information Expert.
 */
public class CategoriaFeirante {

    private Long id;
    private String nome;
    private String descricao;

    public CategoriaFeirante() {
    }

    /**
     * Cria uma categoria de feirante válida.
     *
     * @param id identificador da categoria
     * @param nome nome obrigatório e único no cadastro
     * @param descricao descrição da categoria
     */
    public CategoriaFeirante(Long id, String nome, String descricao) {
        this.id = id;
        setNome(nome);
        this.descricao = descricao;
    }

    /**
     * Valida se o nome da categoria está preenchido.
     *
     * @param nome nome informado
     */
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

    /**
     * Atualiza o nome da categoria após validação.
     *
     * @param nome novo nome da categoria
     */
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