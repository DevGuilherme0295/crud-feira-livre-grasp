package br.com.feira.repository;

import br.com.feira.domain.Feirante;

import java.util.List;
import java.util.Optional;


public interface FeiranteRepository {

    Feirante salvar(Feirante feirante);

    List<Feirante> listarTodos();

    Optional<Feirante> buscarPorId(Long id);

    void remover(Long id);
}