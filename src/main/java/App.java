import learn.solarFarm.data.PanelFileRepository;
import learn.solarFarm.data.PanelRepository;
import learn.solarFarm.domain.PanelService;
import learn.solarFarm.ui.Controller;
import learn.solarFarm.ui.View;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

    public static void main(String[] args) {

        ApplicationContext container = new ClassPathXmlApplicationContext("dependency-configuration.xml");

        Controller controller = container.getBean(Controller.class);

        controller.run();
    }
}
