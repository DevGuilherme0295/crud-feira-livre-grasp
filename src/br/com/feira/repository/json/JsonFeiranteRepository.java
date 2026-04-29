package br.com.feira.repository.json;

import br.com.feira.domain.CategoriaFeirante;
import br.com.feira.domain.Feirante;
import br.com.feira.repository.FeiranteRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementação do repositório de Feirante utilizando persistência em arquivo JSON.
 *
 * <p>Padrão GRASP: Pure Fabrication — classe criada para tratar persistência,
 * não pertence ao domínio.</p>
 *
 * <p>Padrão GRASP: Indirection — implementa a interface {@link FeiranteRepository},
 * desacoplando a lógica de negócio da forma de armazenamento.</p>
 */
public class JsonFeiranteRepository implements FeiranteRepository {

    private final Path arquivo = Path.of("feirantes.json");
    private final List<Feirante> feirantes;

    /**
     * Inicializa o repositório carregando os dados do arquivo JSON.
     */
    public JsonFeiranteRepository() {
        this.feirantes = carregar();
    }

    /**
     * Salva ou atualiza um feirante.
     *
     * @param feirante feirante a ser persistido
     * @return feirante salvo
     */
    @Override
    public Feirante salvar(Feirante feirante) {
        if (feirante.getId() == null) {
            feirante.setId(proximoId());
            feirantes.add(feirante);
        } else {
            for (int i = 0; i < feirantes.size(); i++) {
                if (feirantes.get(i).getId().equals(feirante.getId())) {
                    feirantes.set(i, feirante);
                    break;
                }
            }
        }

        salvarArquivo();
        return feirante;
    }

    /**
     * Retorna todos os feirantes cadastrados.
     *
     * @return lista de feirantes
     */
    @Override
    public List<Feirante> listarTodos() {
        return feirantes;
    }

    /**
     * Busca um feirante pelo id.
     *
     * @param id identificador do feirante
     * @return feirante encontrado ou vazio
     */
    @Override
    public Optional<Feirante> buscarPorId(Long id) {
        return feirantes.stream()
                .filter(f -> f.getId().equals(id))
                .findFirst();
    }

    /**
     * Remove um feirante pelo id.
     *
     * @param id identificador do feirante
     */
    @Override
    public void remover(Long id) {
        feirantes.removeIf(f -> f.getId().equals(id));
        salvarArquivo();
    }

    /**
     * Gera o próximo id disponível.
     *
     * @return próximo identificador
     */
    private Long proximoId() {
        return feirantes.stream()
                .mapToLong(Feirante::getId)
                .max()
                .orElse(0L) + 1;
    }

    /**
     * Salva os dados no arquivo JSON.
     */
    private void salvarArquivo() {
        StringBuilder json = new StringBuilder();
        json.append("[\n");

        for (int i = 0; i < feirantes.size(); i++) {
            Feirante f = feirantes.get(i);

            json.append("  {\n");
            json.append("    \"id\": ").append(f.getId()).append(",\n");
            json.append("    \"nome\": \"").append(escapar(f.getNome())).append("\",\n");
            json.append("    \"cpf\": \"").append(f.getCpf()).append("\",\n");
            json.append("    \"descricao\": \"").append(escapar(f.getDescricao())).append("\",\n");
            json.append("    \"ativo\": ").append(f.isAtivo()).append(",\n");

            json.append("    \"categoria\": {\n");
            json.append("      \"id\": ").append(f.getCategoriaFeirante().getId()).append(",\n");
            json.append("      \"nome\": \"").append(escapar(f.getCategoriaFeirante().getNome())).append("\",\n");
            json.append("      \"descricao\": \"").append(escapar(f.getCategoriaFeirante().getDescricao())).append("\"\n");
            json.append("    }\n");

            json.append("  }");

            if (i < feirantes.size() - 1) {
                json.append(",");
            }

            json.append("\n");
        }

        json.append("]");

        try {
            Files.writeString(arquivo, json.toString());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar feirantes.", e);
        }
    }

    /**
     * Carrega os dados do arquivo JSON.
     *
     * @return lista de feirantes
     */
    private List<Feirante> carregar() {
        List<Feirante> lista = new ArrayList<>();

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
                String cpf = extrair(obj, "cpf");
                String descricao = extrair(obj, "descricao");
                boolean ativo = Boolean.parseBoolean(extrair(obj, "ativo"));

                Long catId = Long.parseLong(extrair(obj, "categoria.id"));
                String catNome = extrair(obj, "categoria.nome");
                String catDesc = extrair(obj, "categoria.descricao");

                CategoriaFeirante categoria = new CategoriaFeirante(catId, catNome, catDesc);

                lista.add(new Feirante(id, nome, cpf, descricao, ativo, categoria));
            }

            return lista;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar feirantes.", e);
        }
    }

    /**
     * Extrai valores de campos do JSON manualmente.
     *
     * @param obj trecho JSON
     * @param campo campo a ser extraído
     * @return valor do campo
     */
    private String extrair(String obj, String campo) {
        String chave = campo.contains(".")
                ? campo.split("\\.")[1]
                : campo;

        String busca = "\"" + chave + "\":";
        int inicio = obj.indexOf(busca);

        if (inicio == -1) return "";

        inicio += busca.length();

        if (obj.charAt(inicio) == ' ') inicio++;

        if (obj.charAt(inicio) == '"') {
            inicio++;
            int fim = obj.indexOf("\"", inicio);
            return desescapar(obj.substring(inicio, fim));
        }

        int fim = obj.indexOf(",", inicio);
        if (fim == -1) fim = obj.length();

        return obj.substring(inicio, fim).trim();
    }

    /**
     * Escapa caracteres especiais para JSON.
     *
     * @param texto texto original
     * @return texto escapado
     */
    private String escapar(String texto) {
        if (texto == null) return "";
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