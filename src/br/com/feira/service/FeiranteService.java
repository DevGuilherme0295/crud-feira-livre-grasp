package br.com.feira.service;

import br.com.feira.domain.CategoriaFeirante;
import br.com.feira.domain.Feirante;
import br.com.feira.repository.CategoriaFeiranteRepository;
import br.com.feira.repository.FeiranteRepository;

import java.util.List;

/**
 * Serviço responsável pelos casos de uso de Feirante.
 *
 * <p>Padrão GRASP: Controller — coordena operações da aplicação.</p>
 *
 * <p>Padrão GRASP: Creator — responsável por criar objetos Feirante,
 * pois possui os dados necessários.</p>
 *
 * <p>Padrão GRASP: Indirection — utiliza interfaces de repositório.</p>
 */
public class FeiranteService {

    private final FeiranteRepository feiranteRepository;
    private final CategoriaFeiranteRepository categoriaRepository;

    /**
     * Construtor com injeção de dependências.
     *
     * @param feiranteRepository repositório de feirantes
     * @param categoriaRepository repositório de categorias
     */
    public FeiranteService(FeiranteRepository feiranteRepository,
                           CategoriaFeiranteRepository categoriaRepository) {
        this.feiranteRepository = feiranteRepository;
        this.categoriaRepository = categoriaRepository;
    }

    /**
     * Cria um novo feirante.
     *
     * <p>Regra de negócio: categoria deve existir.</p>
     *
     * @param nome nome do feirante
     * @param cpf CPF do feirante
     * @param descricao descrição
     * @param ativo status
     * @param categoriaId id da categoria
     * @return feirante criado
     */
    public Feirante criar(String nome, String cpf, String descricao, boolean ativo, Long categoriaId) {

        CategoriaFeirante categoria = categoriaRepository.buscarPorId(categoriaId)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada."));

        Feirante feirante = new Feirante(null, nome, cpf, descricao, ativo, categoria);

        return feiranteRepository.salvar(feirante);
    }

    /**
     * Lista todos os feirantes.
     *
     * @return lista de feirantes
     */
    public List<Feirante> listarTodos() {
        return feiranteRepository.listarTodos();
    }

    /**
     * Busca feirante por id.
     *
     * @param id identificador
     * @return feirante encontrado
     */
    public Feirante buscarPorId(Long id) {
        return feiranteRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Feirante não encontrado."));
    }

    /**
     * Atualiza dados de um feirante.
     *
     * @param id id do feirante
     * @param nome novo nome
     * @param descricao nova descrição
     * @param ativo status
     * @param categoriaId nova categoria
     * @return feirante atualizado
     */
    public Feirante atualizar(Long id, String nome, String descricao, boolean ativo, Long categoriaId) {

        Feirante feirante = buscarPorId(id);

        CategoriaFeirante categoria = categoriaRepository.buscarPorId(categoriaId)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada."));

        feirante.setNome(nome);
        feirante.setDescricao(descricao);
        feirante.setAtivo(ativo);
        feirante.setCategoriaFeirante(categoria);

        return feiranteRepository.salvar(feirante);
    }

    /**
     * Remove um feirante.
     *
     * @param id identificador do feirante
     */
    public void remover(Long id) {
        buscarPorId(id);
        feiranteRepository.remover(id);
    }
}