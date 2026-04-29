package br.com.feira.repository;

import br.com.feira.domain.CategoriaFeirante;

import java.util.List;
import java.util.Optional;

/**
 * Interface que define o contrato de persistência para CategoriaFeirante.
 *
 * <p>Padrão GRASP: Indirection — desacopla o domínio da implementação
 * de persistência.</p>
 */

public interface CategoriaFeiranteRepository {

    CategoriaFeirante salvar(CategoriaFeirante categoria);

    List<CategoriaFeirante> listarTodos();

    Optional<CategoriaFeirante> buscarPorId(Long id);

    Optional<CategoriaFeirante> buscarPorNome(String nome);

    void remover(Long id);
}