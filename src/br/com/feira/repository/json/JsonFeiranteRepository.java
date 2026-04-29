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


public class JsonFeiranteRepository implements FeiranteRepository {

    private final Path arquivo = Path.of("feirantes.json");
    private final List<Feirante> feirantes;

    public JsonFeiranteRepository() {
        this.feirantes = carregar();
    }

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

    @Override
    public List<Feirante> listarTodos() {
        return feirantes;
    }

    @Override
    public Optional<Feirante> buscarPorId(Long id) {
        return feirantes.stream()
                .filter(f -> f.getId().equals(id))
                .findFirst();
    }

    @Override
    public void remover(Long id) {
        feirantes.removeIf(f -> f.getId().equals(id));
        salvarArquivo();
    }

    private Long proximoId() {
        return feirantes.stream()
                .mapToLong(Feirante::getId)
                .max()
                .orElse(0L) + 1;
    }

    private void salvarArquivo() {
        StringBuilder json = new StringBuilder();
        json.append("[\n");

        for (int i = 0; i < feirantes.size(); i++) {
            Feirante f = feirantes.get(i);

            json.append("  {\n");
            json.append("    \"id\": ").append(f.getId()).append(",\n");
            json.append("    \"nome\": \"").append(escapar(f.getNome())).append("\",\n");
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
                String descricao = extrair(obj, "descricao");
                boolean ativo = Boolean.parseBoolean(extrair(obj, "ativo"));

                // categoria
                Long catId = Long.parseLong(extrair(obj, "categoria.id"));
                String catNome = extrair(obj, "categoria.nome");
                String catDesc = extrair(obj, "categoria.descricao");

                CategoriaFeirante categoria = new CategoriaFeirante(catId, catNome, catDesc);

                lista.add(new Feirante(id, nome, descricao, ativo, categoria));
            }

            return lista;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar feirantes.", e);
        }
    }

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

    private String escapar(String texto) {
        if (texto == null) return "";
        return texto.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String desescapar(String texto) {
        return texto.replace("\\\"", "\"").replace("\\\\", "\\");
    }
}