package br.com.feira.domain;

/**
 * Entidade de domínio que representa uma categoria de feirante.
 *
 * <p>Padrão GRASP: Information Expert — a própria classe é responsável
 * por garantir a validade dos seus dados, como a obrigatoriedade do nome.</p>
 */
public class CategoriaFeirante {

    private Long id;
    private String nome;
    private String descricao;

    /**
     * Construtor padrão.
     */
    public CategoriaFeirante() {
    }

    /**
     * Cria uma categoria válida.
     *
     * <p>Regra de negócio: o nome da categoria é obrigatório.</p>
     *
     * @param id identificador da categoria
     * @param nome nome da categoria (obrigatório)
     * @param descricao descrição da categoria
     */
    public CategoriaFeirante(Long id, String nome, String descricao) {
        this.id = id;
        setNome(nome);
        this.descricao = descricao;
    }

    /**
     * Valida o nome da categoria.
     *
     * <p>Regra de negócio: CategoriaFeirante.nome não pode ser nulo ou vazio.</p>
     *
     * @param nome nome informado para validação
     */
    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da categoria é obrigatório.");
        }
    }

    /**
     * Retorna o identificador da categoria.
     *
     * @return id da categoria
     */
    public Long getId() {
        return id;
    }

    /**
     * Retorna o nome da categoria.
     *
     * @return nome da categoria
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna a descrição da categoria.
     *
     * @return descrição da categoria
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Atualiza o nome da categoria após validação.
     *
     * <p>Regra de negócio: o nome é obrigatório.</p>
     *
     * @param nome novo nome da categoria
     */
    public void setNome(String nome) {
        validarNome(nome);
        this.nome = nome.trim();
    }

    /**
     * Define o identificador da categoria.
     *
     * @param id novo identificador
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Define a descrição da categoria.
     *
     * @param descricao nova descrição
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}