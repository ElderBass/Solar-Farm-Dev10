package learn.solarFarm.domain;

import learn.solarFarm.data.DataAccessException;
import learn.solarFarm.data.PanelRepository;
import learn.solarFarm.models.Material;
import learn.solarFarm.models.Panel;

import java.util.ArrayList;
import java.util.List;

public class PanelRepositoryDouble implements PanelRepository {

    private List<Panel> panels = new ArrayList<>();

    public PanelRepositoryDouble() {

        panels.add(new Panel(1, "Mars", 5, 5, 2020, Material.CDTE, true));
        panels.add(new Panel(2, "Moon", 6, 6, 2020, Material.CIGS, true));
        panels.add(new Panel(3, "Venus", 7, 7, 2019, Material.CDTE, false));
        panels.add(new Panel(4, "Mars", 8, 8, 2019, Material.CIGS, false));
        panels.add(new Panel(5, "Moon", 9, 9, 2018, Material.CDTE, true));
    }


    @Override
    public List<Panel> findAll() throws DataAccessException {
        // Always send back a duplicate of the data, not the actual data
        return new ArrayList<>(panels);
    }

    @Override
    public Panel findById(int panelId) throws DataAccessException {
        for (Panel p : panels) {
            if (p.getPanelId() == panelId) {
                return p;
            }
        }
        return null;
    }

    @Override
    public List<Panel> findBySection(String section) {
        List<Panel> results = new ArrayList<>();
        for (Panel p : panels) {
            if (p.getSection().equalsIgnoreCase(section)) {
                results.add(p);
            }
        }
        return results;
    }

    @Override
    public Panel add(Panel panel) throws DataAccessException {
        // No need to add to panels. We're not going to confirm.
        panels.add(panel);
        return panel;
    }

    @Override
    public boolean update(Panel panel) throws DataAccessException {
        return findById(panel.getPanelId()) != null;
    }

    @Override
    public boolean deleteById(int panelId) throws DataAccessException {
        return findById(panelId) != null;
    }
}
