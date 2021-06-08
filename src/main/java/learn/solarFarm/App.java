package learn.solarFarm;

import learn.solarFarm.data.PanelFileRepository;
import learn.solarFarm.data.PanelRepository;
import learn.solarFarm.domain.PanelService;
import learn.solarFarm.ui.Controller;
import learn.solarFarm.ui.View;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@ComponentScan
public class App {

    public static void main(String[] args) {
        // 1. We pass the App.class, this class, as a constructor argument.
        ApplicationContext context = new AnnotationConfigApplicationContext(App.class);

        Controller controller = context.getBean(Controller.class);
        // Run the app!
        controller.run();
    }
}
