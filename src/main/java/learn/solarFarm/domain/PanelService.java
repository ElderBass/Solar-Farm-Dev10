package learn.solarFarm.domain;

import learn.solarFarm.data.DataAccessException;
import learn.solarFarm.data.PanelRepository;
import learn.solarFarm.models.Panel;

import java.util.List;
import java.util.Objects;

public class PanelService {

    private final PanelRepository repository;

    public PanelService(PanelRepository repository) {
        this.repository = repository;
    }

    public List<Panel> findAll() throws DataAccessException {
        return repository.findAll();
    }

    public PanelResult add(Panel panel) throws DataAccessException {
        PanelResult result = validate(panel);
        // TODO Validate, check for duplicates, and set it as payload if it passes checks
        if (!result.isSuccess()) {
            return result;
        }

        // check for duplicate
        List<Panel> panels = repository.findAll();
        for (Panel e : panels) {
            if (panel.getSection().equals(e.getSection()) && e.getRow() == panel.getRow() && e.getCol() == panel.getCol()) {
                result.addErrorMessage("Panel already installed in this location.");
                return result;
            }
        }
        panel = repository.add(panel);
        result.setPayload(panel);
        return result;
    }

    private PanelResult validate(Panel panel) {
        PanelResult result = new PanelResult();
        if (panel == null) {
            result.addErrorMessage("Panel Cannot Be Null.");
        }
        if (panel.getSection().isEmpty() || panel.getSection() == null) {
            result.addErrorMessage("Panel Section is Required.");
        }
        if (panel.getRow() < 1 || panel.getRow() > 250) {
            result.addErrorMessage("Panel Rows and Columns Must Be Between 1 and 250.");
        }
        if (panel.getCol() < 1 || panel.getCol() > 250) {
            result.addErrorMessage("Panel Rows and Columns Must Be Between 1 and 250.");
        }
        if (panel.getYear() > 2021) {
            result.addErrorMessage("Panel's Cannot Be Installed In the Future. (Or can they...??)");
        }
        if (panel.getMaterial() == null) { // Honestly probably won't even need this since I will guarantee they make an entry in View
            result.addErrorMessage("Panel Material is Required.");
        }
        // TODO figure out if I need to validate isTracking. Again, this will probably get taken care of before it gets here
        return result;
    }
}
