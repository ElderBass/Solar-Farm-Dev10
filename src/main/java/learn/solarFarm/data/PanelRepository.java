package learn.solarFarm.data;

import learn.solarFarm.models.Panel;

import java.util.List;

public interface PanelRepository {

    List<Panel> findAll() throws DataAccessException;
    List<Panel> findBySection(String section) throws DataAccessException;
    Panel add(Panel panel) throws DataAccessException;

    Panel findById(int panelId) throws DataAccessException;

    boolean update(Panel panel) throws DataAccessException;
    boolean deleteById(int panelId) throws DataAccessException;
}
