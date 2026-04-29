package br.com.feira.repository.json;

import br.com.feira.domain.CategoriaFeirante;
import br.com.feira.repository.CategoriaFeiranteRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementação do repositório de CategoriaFeirante utilizando persistência em arquivo JSON.
 *
 * <p>Padrão GRASP: Pure Fabrication — esta classe não pertence ao domínio,
 * sendo criada para tratar a persistência dos dados.</p>
 *
 * <p>Padrão GRASP: Indirection — implementa a interface
 * {@link CategoriaFeiranteRepository}, desacoplando o domínio da forma de armazenamento.</p>
 */
public class JsonCategoriaFeiranteRepository implements CategoriaFeiranteRepository {

    private final Path arquivo = Path.of("categorias-feirante.json");
    private final List<CategoriaFeirante> categorias;

    /**
     * Inicializa o repositório carregando os dados do arquivo JSON.
     */
    public JsonCategoriaFeiranteRepository() {
        this.categorias = carregar();
    }

    /**
     * Salva ou atualiza uma categoria.
     *
     * @param categoria categoria a ser persistida
     * @return categoria salva
     */
    @Override
    public CategoriaFeirante salvar(CategoriaFeirante categoria) {
        if (categoria.getId() == null) {
            categoria.setId(proximoId());
            categorias.add(categoria);
        } else {
            for (int i = 0; i < categorias.size(); i++) {
                if (categorias.get(i).getId().equals(categoria.getId())) {
                    categorias.set(i, categoria);
                    break;
                }
            }
        }

        salvarArquivo();
        return categoria;
    }

    /**
     * Retorna todas as categorias cadastradas.
     *
     * @return lista de categorias
     */
    @Override
    public List<CategoriaFeirante> listarTodos() {
        return categorias;
    }

    /**
     * Busca uma categoria pelo id.
     *
     * @param id identificador da categoria
     * @return categoria encontrada ou vazio
     */
    @Override
    public Optional<CategoriaFeirante> buscarPorId(Long id) {
        return categorias.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    /**
     * Busca uma categoria pelo nome.
     *
     * @param nome nome da categoria
     * @return categoria encontrada ou vazio
     */
    @Override
    public Optional<CategoriaFeirante> buscarPorNome(String nome) {
        return categorias.stream()
                .filter(c -> c.getNome().equalsIgnoreCase(nome))
                .findFirst();
    }

    /**
     * Remove uma categoria pelo id.
     *
     * @param id identificador da categoria
     */
    @Override
    public void remover(Long id) {
        categorias.removeIf(c -> c.getId().equals(id));
        salvarArquivo();
    }

    /**
     * Gera o próximo identificador disponível.
     *
     * @return próximo id
     */
    private Long proximoId() {
        return categorias.stream()
                .mapToLong(CategoriaFeirante::getId)
                .max()
                .orElse(0L) + 1;
    }

    /**
     * Salva os dados no arquivo JSON.
     */
    private void salvarArquivo() {
        StringBuilder json = new StringBuilder();
        json.append("[\n");

        for (int i = 0; i < categorias.size(); i++) {
            CategoriaFeirante c = categorias.get(i);

            json.append("  {\n");
            json.append("    \"id\": ").append(c.getId()).append(",\n");
            json.append("    \"nome\": \"").append(escapar(c.getNome())).append("\",\n");
            json.append("    \"descricao\": \"").append(escapar(c.getDescricao())).append("\"\n");
            json.append("  }");

            if (i < categorias.size() - 1) {
                json.append(",");
            }

            json.append("\n");
        }

        json.append("]");

        try {
            Files.writeString(arquivo, json.toString());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar categorias em JSON.", e);
        }
    }

    /**
     * Carrega os dados do arquivo JSON.
     *
     * @return lista de categorias
     */
    private List<CategoriaFeirante> carregar() {
        List<CategoriaFeirante> lista = new ArrayList<>();

        if (!Files.exists(arquivo)) {
            return lista;
        }

        try {
            String conteudo = Files.readString(arquivo)
                    .replace("\n", "")
                    .replace("\r", "")
                    .trim();

            if (conteudo.length() <= 2) {
                return lista;
            }

            conteudo = conteudo.substring(1, conteudo.length() - 1);
            String[] objetos = conteudo.split("\\},\\s*\\{");

            for (String obj : objetos) {
                obj = obj.replace("{", "").replace("}", "").trim();

                Long id = Long.parseLong(extrair(obj, "id"));
                String nome = extrair(obj, "nome");
                String descricao = extrair(obj, "descricao");

                lista.add(new CategoriaFeirante(id, nome, descricao));
            }

            return lista;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar categorias do JSON.", e);
        }
    }

    /**
     * Extrai um campo do JSON manualmente.
     *
     * @param objeto trecho JSON
     * @param campo campo a ser extraído
     * @return valor do campo
     */
    private String extrair(String objeto, String campo) {
        String busca = "\"" + campo + "\":";
        int inicio = objeto.indexOf(busca);

        if (inicio == -1) {
            return "";
        }

        inicio += busca.length();

        if (objeto.charAt(inicio) == ' ') {
            inicio++;
        }

        if (objeto.charAt(inicio) == '"') {
            inicio++;
            int fim = objeto.indexOf("\"", inicio);
            return desescapar(objeto.substring(inicio, fim));
        }

        int fim = objeto.indexOf(",", inicio);
        if (fim == -1) {
            fim = objeto.length();
        }

        return objeto.substring(inicio, fim).trim();
    }

    /**
     * Escapa caracteres especiais para JSON.
     *
     * @param texto texto original
     * @return texto escapado
     */
    private String escapar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    /**
     * Desfaz escape de caracteres.
     *
     * @param texto texto escapado
     * @return texto normal
     */
    private String desescapar(String texto) {
        return texto.replace("\\\"", "\"").replace("\\\\", "\\");
    }
}