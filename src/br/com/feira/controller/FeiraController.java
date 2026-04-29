package br.com.feira.controller;

import br.com.feira.domain.CategoriaFeirante;
import br.com.feira.domain.Feirante;
import br.com.feira.service.CategoriaFeiranteService;
import br.com.feira.service.FeiranteService;

import java.util.List;
import java.util.Scanner;

/**
 * Controller textual da aplicação.
 * Recebe as opções do menu no console e delega as ações para os serviços.
 */
public class FeiraController {

    private final CategoriaFeiranteService categoriaService;
    private final FeiranteService feiranteService;
    private final Scanner scanner;

    public FeiraController(CategoriaFeiranteService categoriaService, FeiranteService feiranteService) {
        this.categoriaService = categoriaService;
        this.feiranteService = feiranteService;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Inicia o menu principal da aplicação.
     */
    public void iniciar() {
        int opcao;

        do {
            exibirMenu();
            opcao = lerInteiro("Escolha uma opção: ");

            try {
                switch (opcao) {
                    case 1 -> cadastrarCategoria();
                    case 2 -> listarCategorias();
                    case 3 -> cadastrarFeirante();
                    case 4 -> listarFeirantes();
                    case 5 -> buscarFeirantePorId();
                    case 6 -> atualizarFeirante();
                    case 7 -> excluirFeirante();
                    case 8 -> excluirCategoria();
                    case 0 -> System.out.println("Sistema finalizado.");
                    default -> System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }

        } while (opcao != 0);
    }

    private void exibirMenu() {
        System.out.println("\n===== SISTEMA FEIRA LIVRE =====");
        System.out.println("1 - Cadastrar CategoriaFeirante");
        System.out.println("2 - Listar CategoriaFeirante");
        System.out.println("3 - Cadastrar Feirante");
        System.out.println("4 - Listar Feirante");
        System.out.println("5 - Buscar Feirante por id");
        System.out.println("6 - Atualizar Feirante");
        System.out.println("7 - Excluir Feirante");
        System.out.println("8 - Excluir CategoriaFeirante");
        System.out.println("0 - Sair");
    }

    private void cadastrarCategoria() {
        String nome = lerTexto("Nome da categoria: ");
        String descricao = lerTexto("Descrição da categoria: ");

        CategoriaFeirante categoria = categoriaService.criar(nome, descricao);
        System.out.println("Categoria cadastrada com sucesso. ID: " + categoria.getId());
    }

    private void listarCategorias() {
        List<CategoriaFeirante> categorias = categoriaService.listarTodos();

        if (categorias.isEmpty()) {
            System.out.println("Nenhuma categoria cadastrada.");
            return;
        }

        for (CategoriaFeirante c : categorias) {
            System.out.println(c.getId() + " - " + c.getNome() + " | " + c.getDescricao());
        }
    }

    private void cadastrarFeirante() {
        listarCategorias();

        String nome = lerTexto("Nome do feirante: ");
        String descricao = lerTexto("Descrição do feirante: ");
        boolean ativo = lerBooleano("Está ativo? (s/n): ");
        Long categoriaId = lerLong("ID da categoria: ");

        Feirante feirante = feiranteService.criar(nome, descricao, ativo, categoriaId);
        System.out.println("Feirante cadastrado com sucesso. ID: " + feirante.getId());
    }

    private void listarFeirantes() {
        List<Feirante> feirantes = feiranteService.listarTodos();

        if (feirantes.isEmpty()) {
            System.out.println("Nenhum feirante cadastrado.");
            return;
        }

        for (Feirante f : feirantes) {
            System.out.println(
                    f.getId() + " - " +
                            f.getNome() +
                            " | Categoria: " + f.getCategoriaFeirante().getNome() +
                            " | Ativo: " + (f.isAtivo() ? "Sim" : "Não")
            );
        }
    }

    private void buscarFeirantePorId() {
        Long id = lerLong("ID do feirante: ");
        Feirante f = feiranteService.buscarPorId(id);

        System.out.println("ID: " + f.getId());
        System.out.println("Nome: " + f.getNome());
        System.out.println("Descrição: " + f.getDescricao());
        System.out.println("Ativo: " + (f.isAtivo() ? "Sim" : "Não"));
        System.out.println("Categoria: " + f.getCategoriaFeirante().getNome());
    }

    private void atualizarFeirante() {
        listarFeirantes();

        Long id = lerLong("ID do feirante que será atualizado: ");
        listarCategorias();

        String nome = lerTexto("Novo nome: ");
        String descricao = lerTexto("Nova descrição: ");
        boolean ativo = lerBooleano("Está ativo? (s/n): ");
        Long categoriaId = lerLong("Novo ID da categoria: ");

        feiranteService.atualizar(id, nome, descricao, ativo, categoriaId);
        System.out.println("Feirante atualizado com sucesso.");
    }

    private void excluirFeirante() {
        Long id = lerLong("ID do feirante que será removido: ");
        feiranteService.remover(id);
        System.out.println("Feirante removido com sucesso.");
    }

    private void excluirCategoria() {
        Long id = lerLong("ID da categoria que será removida: ");
        categoriaService.remover(id);
        System.out.println("Categoria removida com sucesso.");
    }

    private String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine();
    }

    private int lerInteiro(String mensagem) {
        System.out.print(mensagem);
        int valor = Integer.parseInt(scanner.nextLine());
        return valor;
    }

    private Long lerLong(String mensagem) {
        System.out.print(mensagem);
        return Long.parseLong(scanner.nextLine());
    }

    private boolean lerBooleano(String mensagem) {
        System.out.print(mensagem);
        String resposta = scanner.nextLine();
        return resposta.equalsIgnoreCase("s") || resposta.equalsIgnoreCase("sim");
    }
}