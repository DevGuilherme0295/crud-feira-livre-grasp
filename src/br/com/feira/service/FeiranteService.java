package br.com.feira.service;

import br.com.feira.domain.CategoriaFeirante;
import br.com.feira.domain.Feirante;
import br.com.feira.repository.CategoriaFeiranteRepository;
import br.com.feira.repository.FeiranteRepository;

import java.util.List;


public class FeiranteService {

    private final FeiranteRepository feiranteRepository;
    private final CategoriaFeiranteRepository categoriaRepository;

    public FeiranteService(FeiranteRepository feiranteRepository,
                           CategoriaFeiranteRepository categoriaRepository) {
        this.feiranteRepository = feiranteRepository;
        this.categoriaRepository = categoriaRepository;
    }


    public Feirante criar(String nome, String descricao, boolean ativo, Long categoriaId) {
        CategoriaFeirante categoria = categoriaRepository.buscarPorId(categoriaId)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada."));

        Feirante feirante = new Feirante(null, nome, descricao, ativo, categoria);
        return feiranteRepository.salvar(feirante);
    }


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


    public List<Feirante> listarTodos() {
        return feiranteRepository.listarTodos();
    }


    public Feirante buscarPorId(Long id) {
        return feiranteRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Feirante não encontrado."));
    }


    public void remover(Long id) {
        buscarPorId(id);
        feiranteRepository.remover(id);
    }
}