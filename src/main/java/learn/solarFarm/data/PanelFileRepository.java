package learn.solarFarm.data;

import learn.solarFarm.models.Panel;

import java.util.List;

public class PanelFileRepository implements PanelRepository {

    @Override
    public List<Panel> findAll() throws DataAccessException {
        return null;
    }

    @Override
    public Panel add(Panel panel) throws DataAccessException {
        return null;
    }

    @Override
    public Panel findById(int panelId) throws DataAccessException {
        return null;
    }

    @Override
    public boolean update(Panel panel) throws DataAccessException {
        return false;
    }

    @Override
    public boolean deleteById(int panelId) throws DataAccessException {
        return false;
    }
}
