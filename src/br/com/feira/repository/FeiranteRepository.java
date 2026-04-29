package br.com.feira.repository;

import br.com.feira.domain.Feirante;

import java.util.List;
import java.util.Optional;

/**
 * Interface que define o contrato de persistência para Feirante.
 *
 * <p>Padrão GRASP: Indirection — desacopla o domínio da implementação
 * de persistência.</p>
 */

public interface FeiranteRepository {

    Feirante salvar(Feirante feirante);

    List<Feirante> listarTodos();

    Optional<Feirante> buscarPorId(Long id);

    void remover(Long id);
}