import br.com.feira.controller.FeiraController;
import br.com.feira.repository.CategoriaFeiranteRepository;
import br.com.feira.repository.FeiranteRepository;
import br.com.feira.repository.json.JsonCategoriaFeiranteRepository;
import br.com.feira.repository.json.JsonFeiranteRepository;
import br.com.feira.service.CategoriaFeiranteService;
import br.com.feira.service.FeiranteService;

public class Main {

    public static void main(String[] args) {
        CategoriaFeiranteRepository categoriaRepository = new JsonCategoriaFeiranteRepository();
        FeiranteRepository feiranteRepository = new JsonFeiranteRepository();

        CategoriaFeiranteService categoriaService =
                new CategoriaFeiranteService(categoriaRepository, feiranteRepository);

        FeiranteService feiranteService =
                new FeiranteService(feiranteRepository, categoriaRepository);

        FeiraController controller = new FeiraController(categoriaService, feiranteService);
        controller.iniciar();
    }
}