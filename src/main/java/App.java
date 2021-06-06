import learn.solarFarm.data.PanelFileRepository;
import learn.solarFarm.data.PanelRepository;
import learn.solarFarm.domain.PanelService;
import learn.solarFarm.ui.Controller;
import learn.solarFarm.ui.View;

public class App {

    public static void main(String[] args) {

        PanelRepository repository = new PanelFileRepository("./data/solar-panels.csv");
        PanelService service = new PanelService(repository);
        View view = new View();

        Controller controller = new Controller(view, service);

        controller.run();
    }
}
