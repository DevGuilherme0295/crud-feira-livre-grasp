package br.com.feira.service;

import br.com.feira.domain.CategoriaFeirante;
import br.com.feira.domain.Feirante;
import br.com.feira.repository.CategoriaFeiranteRepository;
import br.com.feira.repository.FeiranteRepository;

import java.util.List;

/**
 * Serviço responsável pelos casos de uso de CategoriaFeirante.
 *
 * <p>Padrão GRASP: Controller — coordena as operações da aplicação.</p>
 *
 * <p>Padrão GRASP: Indirection — depende das interfaces de repositório,
 * desacoplando da persistência.</p>
 */
public class CategoriaFeiranteService {

    private final CategoriaFeiranteRepository categoriaRepository;
    private final FeiranteRepository feiranteRepository;

    /**
     * Construtor com injeção de dependências.
     *
     * @param categoriaRepository repositório de categorias
     * @param feiranteRepository repositório de feirantes
     */
    public CategoriaFeiranteService(CategoriaFeiranteRepository categoriaRepository,
                                    FeiranteRepository feiranteRepository) {
        this.categoriaRepository = categoriaRepository;
        this.feiranteRepository = feiranteRepository;
    }

    /**
     * Cria uma nova categoria garantindo nome único.
     *
     * <p>Regra de negócio: CategoriaFeirante.nome deve ser único.</p>
     *
     * @param nome nome da categoria
     * @param descricao descrição da categoria
     * @return categoria criada
     */
    public CategoriaFeirante criar(String nome, String descricao) {
        if (categoriaRepository.buscarPorNome(nome).isPresent()) {
            throw new IllegalArgumentException("Já existe uma categoria com esse nome.");
        }

        CategoriaFeirante categoria = new CategoriaFeirante(null, nome, descricao);
        return categoriaRepository.salvar(categoria);
    }

    /**
     * Lista todas as categorias cadastradas.
     *
     * @return lista de categorias
     */
    public List<CategoriaFeirante> listarTodos() {
        return categoriaRepository.listarTodos();
    }

    /**
     * Remove uma categoria somente se não estiver em uso.
     *
     * <p>Regra de negócio: não permitir remover categoria associada a feirante.</p>
     *
     * @param id identificador da categoria
     */
    public void remover(Long id) {
        boolean emUso = feiranteRepository.listarTodos()
                .stream()
                .map(Feirante::getCategoriaFeirante)
                .anyMatch(c -> c.getId().equals(id));

        if (emUso) {
            throw new IllegalArgumentException("Não é possível remover categoria em uso.");
        }

        categoriaRepository.remover(id);
    }
}