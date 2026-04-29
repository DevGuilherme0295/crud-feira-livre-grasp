package br.com.feira.repository.json;

import br.com.feira.domain.CategoriaFeirante;
import br.com.feira.repository.CategoriaFeiranteRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class JsonCategoriaFeiranteRepository implements CategoriaFeiranteRepository {

    private final Path arquivo = Path.of("categorias-feirante.json");
    private final List<CategoriaFeirante> categorias;

    public JsonCategoriaFeiranteRepository() {
        this.categorias = carregar();
    }


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


    @Override
    public List<CategoriaFeirante> listarTodos() {
        return categorias;
    }


    @Override
    public Optional<CategoriaFeirante> buscarPorId(Long id) {
        return categorias.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }


    @Override
    public Optional<CategoriaFeirante> buscarPorNome(String nome) {
        return categorias.stream()
                .filter(c -> c.getNome().equalsIgnoreCase(nome))
                .findFirst();
    }


    @Override
    public void remover(Long id) {
        categorias.removeIf(c -> c.getId().equals(id));
        salvarArquivo();
    }

    private Long proximoId() {
        return categorias.stream()
                .mapToLong(CategoriaFeirante::getId)
                .max()
                .orElse(0L) + 1;
    }

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

    private String escapar(String texto) {
        if (texto == null) {
            return "";
        }

        return texto.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String desescapar(String texto) {
        return texto.replace("\\\"", "\"").replace("\\\\", "\\");
    }
}