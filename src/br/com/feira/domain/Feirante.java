package br.com.feira.domain;

/**
 * Entidade de domínio que representa um feirante.
 *
 * <p>Padrão GRASP: Information Expert — o próprio Feirante é responsável
 * por validar suas regras de negócio, como nome mínimo, CPF válido
 * e obrigatoriedade de categoria.</p>
 */
public class Feirante {

    private Long id;
    private String nome;
    private String descricao;
    private boolean ativo;
    private CategoriaFeirante categoriaFeirante;
    private String cpf;

    /**
     * Construtor padrão.
     */
    public Feirante() {
    }

    /**
     * Cria um feirante válido.
     *
     * <p>Regras de negócio:
     * - Nome deve ter pelo menos 3 caracteres
     * - CPF deve conter exatamente 11 dígitos numéricos
     * - Categoria é obrigatória</p>
     *
     * @param id identificador do feirante
     * @param nome nome do feirante (mínimo 3 caracteres)
     * @param cpf CPF do feirante (11 dígitos numéricos)
     * @param descricao descrição do feirante
     * @param ativo indica se o feirante está ativo
     * @param categoriaFeirante categoria associada ao feirante
     */
    public Feirante(Long id, String nome, String cpf, String descricao, boolean ativo, CategoriaFeirante categoriaFeirante) {
        this.id = id;
        setNome(nome);
        setCpf(cpf);
        this.descricao = descricao;
        this.ativo = ativo;
        setCategoriaFeirante(categoriaFeirante);
    }

    /**
     * Valida o nome do feirante.
     *
     * <p>Regra de negócio: nome deve ter pelo menos 3 caracteres.</p>
     *
     * @param nome nome informado
     */
    private void validarNome(String nome) {
        if (nome == null || nome.trim().length() < 3) {
            throw new IllegalArgumentException("O nome do feirante deve ter pelo menos 3 caracteres.");
        }
    }

    /**
     * Valida o CPF do feirante.
     *
     * <p>Regra de negócio: CPF deve conter exatamente 11 dígitos numéricos.</p>
     *
     * @param cpf CPF informado
     */
    private void validarCpf(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF deve conter exatamente 11 dígitos numéricos.");
        }
    }

    /**
     * Define o CPF do feirante após validação.
     *
     * @param cpf CPF do feirante
     */
    public void setCpf(String cpf) {
        validarCpf(cpf);
        this.cpf = cpf;
    }

    /**
     * Retorna o CPF do feirante.
     *
     * @return CPF do feirante
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * Valida a categoria do feirante.
     *
     * <p>Regra de negócio: categoria é obrigatória.</p>
     *
     * @param categoriaFeirante categoria informada
     */
    private void validarCategoria(CategoriaFeirante categoriaFeirante) {
        if (categoriaFeirante == null) {
            throw new IllegalArgumentException("A categoria do feirante é obrigatória.");
        }
    }

    /**
     * Retorna o identificador do feirante.
     *
     * @return id do feirante
     */
    public Long getId() {
        return id;
    }

    /**
     * Retorna o nome do feirante.
     *
     * @return nome do feirante
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna a descrição do feirante.
     *
     * @return descrição do feirante
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Indica se o feirante está ativo.
     *
     * @return true se ativo, false caso contrário
     */
    public boolean isAtivo() {
        return ativo;
    }

    /**
     * Retorna a categoria associada ao feirante.
     *
     * @return categoria do feirante
     */
    public CategoriaFeirante getCategoriaFeirante() {
        return categoriaFeirante;
    }

    /**
     * Define o identificador do feirante.
     *
     * @param id novo identificador
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Atualiza o nome do feirante após validação.
     *
     * @param nome novo nome
     */
    public void setNome(String nome) {
        validarNome(nome);
        this.nome = nome.trim();
    }

    /**
     * Define a descrição do feirante.
     *
     * @param descricao nova descrição
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Define se o feirante está ativo.
     *
     * @param ativo situação do feirante
     */
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    /**
     * Atualiza a categoria do feirante após validação.
     *
     * @param categoriaFeirante nova categoria
     */
    public void setCategoriaFeirante(CategoriaFeirante categoriaFeirante) {
        validarCategoria(categoriaFeirante);
        this.categoriaFeirante = categoriaFeirante;
    }
}