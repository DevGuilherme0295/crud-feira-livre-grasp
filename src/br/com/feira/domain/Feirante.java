package br.com.feira.domain;

/**
 * Representa um feirante da feira livre.
 * A própria entidade conhece e valida suas regras de estado,
 * aplicando o princípio GRASP Information Expert.
 */
public class Feirante {

    private Long id;
    private String nome;
    private String descricao;
    private boolean ativo;
    private CategoriaFeirante categoriaFeirante;

    public Feirante() {
    }

    /**
     * Cria um feirante válido associado a uma categoria.
     *
     * @param id identificador do feirante
     * @param nome nome obrigatório com pelo menos 3 caracteres
     * @param descricao descrição do feirante
     * @param ativo indica se o feirante está ativo
     * @param categoriaFeirante categoria obrigatória associada ao feirante
     */
    public Feirante(Long id, String nome, String descricao, boolean ativo, CategoriaFeirante categoriaFeirante) {
        this.id = id;
        setNome(nome);
        this.descricao = descricao;
        this.ativo = ativo;
        setCategoriaFeirante(categoriaFeirante);
    }

    /**
     * Valida o nome do feirante.
     *
     * @param nome nome informado
     */
    private void validarNome(String nome) {
        if (nome == null || nome.trim().length() < 3) {
            throw new IllegalArgumentException("O nome do feirante deve ter pelo menos 3 caracteres.");
        }
    }

    /**
     * Valida se o feirante possui categoria.
     *
     * @param categoriaFeirante categoria informada
     */
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

    /**
     * Atualiza o nome do feirante após validação.
     *
     * @param nome novo nome do feirante
     */
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

    /**
     * Atualiza a categoria do feirante após validação.
     *
     * @param categoriaFeirante nova categoria do feirante
     */
    public void setCategoriaFeirante(CategoriaFeirante categoriaFeirante) {
        validarCategoria(categoriaFeirante);
        this.categoriaFeirante = categoriaFeirante;
    }
}