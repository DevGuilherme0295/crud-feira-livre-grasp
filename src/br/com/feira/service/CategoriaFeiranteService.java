package br.com.feira.service;

import br.com.feira.domain.CategoriaFeirante;
import br.com.feira.domain.Feirante;
import br.com.feira.repository.CategoriaFeiranteRepository;
import br.com.feira.repository.FeiranteRepository;

import java.util.List;

/**
 * Serviço responsável pelos casos de uso de CategoriaFeirante.
 */
public class CategoriaFeiranteService {

    private final CategoriaFeiranteRepository categoriaRepository;
    private final FeiranteRepository feiranteRepository;

    public CategoriaFeiranteService(CategoriaFeiranteRepository categoriaRepository,
                                    FeiranteRepository feiranteRepository) {
        this.categoriaRepository = categoriaRepository;
        this.feiranteRepository = feiranteRepository;
    }

    /**
     * Cria uma categoria garantindo nome único.
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
     * Remove categoria somente se não estiver em uso.
     *
     * @param id id da categoria
     */
    public void remover(Long id) {
        boolean emUso = feiranteRepository.listarTodos()
                .stream()
                .map(Feirante::getCategoriaFeirante)
                .anyMatch(categoria -> categoria.getId().equals(id));

        if (emUso) {
            throw new IllegalArgumentException("Não é possível remover categoria em uso por feirante.");
        }

        categoriaRepository.remover(id);
    }
}