package br.com.feira.service;

import br.com.feira.domain.CategoriaFeirante;
import br.com.feira.domain.Feirante;
import br.com.feira.repository.CategoriaFeiranteRepository;
import br.com.feira.repository.FeiranteRepository;

import java.util.List;

/**
 * Serviço responsável pelos casos de uso de Feirante.
 */
public class FeiranteService {

    private final FeiranteRepository feiranteRepository;
    private final CategoriaFeiranteRepository categoriaRepository;

    public FeiranteService(FeiranteRepository feiranteRepository,
                           CategoriaFeiranteRepository categoriaRepository) {
        this.feiranteRepository = feiranteRepository;
        this.categoriaRepository = categoriaRepository;
    }

    /**
     * Cria um feirante associado a uma categoria existente.
     *
     * @param nome nome do feirante
     * @param descricao descrição do feirante
     * @param ativo situação do feirante
     * @param categoriaId id da categoria existente
     * @return feirante criado
     */
    public Feirante criar(String nome, String descricao, boolean ativo, Long categoriaId) {
        CategoriaFeirante categoria = categoriaRepository.buscarPorId(categoriaId)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada."));

        Feirante feirante = new Feirante(null, nome, descricao, ativo, categoria);
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
     * @param id id do feirante
     * @return feirante encontrado
     */
    public Feirante buscarPorId(Long id) {
        return feiranteRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Feirante não encontrado."));
    }

    /**
     * Atualiza os dados de um feirante.
     *
     * @param id id do feirante
     * @param nome novo nome
     * @param descricao nova descrição
     * @param ativo nova situação
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
     * Remove um feirante pelo id.
     *
     * @param id id do feirante
     */
    public void remover(Long id) {
        buscarPorId(id);
        feiranteRepository.remover(id);
    }
}