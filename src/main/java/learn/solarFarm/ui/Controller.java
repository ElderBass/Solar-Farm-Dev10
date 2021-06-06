package learn.solarFarm.ui;

import learn.solarFarm.data.DataAccessException;
import learn.solarFarm.domain.PanelResult;
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
        view.displayHeader("Exiting Program. Have a nice day!");
    }

    public void runMenuLoop() throws DataAccessException {
        int option = -1;
        do {
            option = view.displayMenuAndSelect();
            switch(option) {
                case 0:
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
        view.displayHeader("Select a Section");
        List<Panel> panels = service.findAll();
        String section = view.selectSectionToDisplay(panels);
        List<Panel> panelsBySection = service.findBySection(section);
        view.displayHeader("Viewing Panels in Section \"" + section + "\"");
        view.printPanels(panelsBySection);
    }

    private void addPanel() throws DataAccessException {
        view.displayHeader("Add a Panel");
        Panel panel = view.createPanel();
        PanelResult result = service.add(panel);
        view.printResult(result);
    }

    private void updatePanel() throws DataAccessException {
        view.displayHeader("Update a Panel");
        List<Panel> panels = service.findAll();
        Panel panel = view.updatePanel(panels);
        PanelResult result = service.update(panel);
        view.printResult(result);
    }

    private void deletePanel() throws DataAccessException {
        view.displayHeader("Delete a Panel");
        List<Panel> panels = service.findAll();
        int panelId = view.deletePanel(panels);
        PanelResult result = service.deleteById(panelId);
        view.printResult(result);
    }

}
