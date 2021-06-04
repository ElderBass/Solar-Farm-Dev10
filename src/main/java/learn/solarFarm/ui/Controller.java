package learn.solarFarm.ui;

import learn.solarFarm.data.DataAccessException;
import learn.solarFarm.domain.PanelService;
import learn.solarFarm.models.Panel;

import java.util.List;

public class Controller {

    private View view;
    private PanelService service;

    public Controller(View view, PanelService service) {
        this.view = view;
        this.service = service;
    }

    public void run() {
        view.displayHeader("Welcome To Elon Musk's Solar Farm.");

        try {
            runMenuLoop();
        } catch (DataAccessException ex) {
            view.displayHeader("CRITICAL ERROR:" + ex.getMessage());
        }
        view.displayHeader("Goodbye");
    }

    public void runMenuLoop() throws DataAccessException {
        int option = -1;
        do {
            option = view.displayMenuAndSelect();
            switch(option) {
                case 0:
                    view.displayHeader("Goodbye.");
                    break;
                case 1:
                    displayAllPanels();
                    break;
                case 2:
                    displayPanelsBySection();
                    break;
                case 3:
                    addPanel();
                    break;
                case 4:
                    updatePanel();
                    break;
                case 5:
                    deletePanel();
                    break;
            }
        } while (option != 0);
    }

    private void displayAllPanels() throws DataAccessException {
        List<Panel> panels = service.findAll();
        view.displayHeader("Viewing All Panels");
        view.printPanels(panels);
    }

    private void displayPanelsBySection() throws DataAccessException {

    }

    private Panel addPanel() throws DataAccessException {
        return null;
    }

    private Panel updatePanel() throws DataAccessException {
        return null;
    }

    private void deletePanel() throws DataAccessException {

    }

}
