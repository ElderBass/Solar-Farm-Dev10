package learn.solarFarm.domain;

import learn.solarFarm.data.DataAccessException;
import learn.solarFarm.data.PanelRepository;
import learn.solarFarm.models.Material;
import learn.solarFarm.models.Panel;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PanelServiceTest {

    private PanelRepository repository = new PanelRepositoryDouble();
    PanelService service = new PanelService(repository);

    @Test
    void shouldFindBySection() throws DataAccessException {
        List<Panel> panels = service.findBySection("Mars");
        assertEquals(2, panels.size());

        panels = service.findBySection("Moon");
        assertEquals(2, panels.size());
    }

    @Test
    void shouldFindById() throws DataAccessException {
        Panel panel = service.findById(1);
        assertEquals("Mars", panel.getSection());
    }

    @Test
    void shouldAddValidPanel() throws DataAccessException {
        Panel validPanel = new Panel(0, "Test Section", 5, 5, 2020, Material.AMORPHOUS_SI, true);
        PanelResult result = service.add(validPanel);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotAddNull() throws DataAccessException {
        PanelResult expected = makeResult("Panel Cannot Be Null.");
        PanelResult actual = service.add(null);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddEmptySection() throws DataAccessException {
        PanelResult expected = makeResult("Panel Section is Required.");
        Panel panel = new Panel(0, "", 5, 5, 2020, Material.AMORPHOUS_SI, true);
        PanelResult actual = service.add(panel);
        assertEquals(expected, actual);

        panel = new Panel(0, null, 5, 5, 2020, Material.AMORPHOUS_SI, true);
        actual = service.add(panel);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddEmptyMaterial() throws DataAccessException {
        PanelResult expected = makeResult("Panel Material is Required.");
        Panel panel = new Panel(0, "Test Section", 5, 5, 2020, null, true);
        PanelResult actual = service.add(panel);
        assertEquals(actual, expected);
    }

    @Test
    void shouldNotAddRowOutOfBounds() throws DataAccessException {
        PanelResult expected = makeResult("Panel Rows and Columns Must Be Between 1 and 250.");
        Panel panel = new Panel(0, "Test Section", 500, 5, 2020, Material.AMORPHOUS_SI, true);
        PanelResult actual = service.add(panel);
        assertEquals(actual, expected);

        panel = new Panel(0, "Test Section", 0, 5, 2020, Material.AMORPHOUS_SI, true);
        actual = service.add(panel);
        assertEquals(actual, expected);
    }

    @Test
    void shouldNotAddColumnOutOfBounds() throws DataAccessException {
        PanelResult expected = makeResult("Panel Rows and Columns Must Be Between 1 and 250.");
        Panel panel = new Panel(0, "Test Section", 5, 500, 2020, Material.AMORPHOUS_SI, true);
        PanelResult actual = service.add(panel);
        assertEquals(actual, expected);

        panel = new Panel(0, "Test Section", 5, 0, 2020, Material.AMORPHOUS_SI, true);
        actual = service.add(panel);
        assertEquals(actual, expected);
    }

    @Test
    void shouldNotAddYearFromFuture() throws DataAccessException {
        PanelResult expected = makeResult("Panels Cannot Be Installed In the Future. (Or can they...??)");
        Panel panel = new Panel(0, "Test Section", 5, 50, 2030, Material.AMORPHOUS_SI, true);
        PanelResult actual = service.add(panel);
        assertEquals(actual, expected);
    }

    @Test
    void shouldNotAddDuplicatePanel() throws DataAccessException {
        PanelResult expected = makeResult("Panel already installed in this location.");
        // The location of this panel (section/row/col) is the same as the first entry in our seed/test file
        Panel panel = new Panel(0, "Mars", 5, 5, 2020, Material.AMORPHOUS_SI, true);
        PanelResult actual = service.add(panel);
        assertEquals(actual, expected);
    }

    @Test
    void shouldUpdateExistingPanel() throws DataAccessException {
        // Updating first entry = 1, Mars, 5 ,5, 2020, CDTE, true
        Panel panel = new Panel(1, "Moon", 10, 10, 2020, Material.CDTE, false);
        PanelResult result = service.update(panel);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotUpdateNonexistentPanel() throws DataAccessException {
        Panel panel = new Panel(20, "Mars", 5, 6, 2020, Material.AMORPHOUS_SI, false);
        PanelResult result = service.update(panel);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotUpdatePanelWithIdLessThanEqualToZero() throws DataAccessException {
        Panel panel = new Panel(0, "Mars", 5, 6, 2020, Material.AMORPHOUS_SI, false);
        PanelResult result = service.update(panel);
        assertFalse(result.isSuccess());
    }

    // TODO need all those other test cases for update..? Can't update without [field], etc.?

    @Test
    void shouldDeleteExistingPanel() throws DataAccessException {
        PanelResult result = service.deleteById(1);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotDeleteNonexistentPanel() throws DataAccessException {
        PanelResult result = service.deleteById(100);
        assertFalse(result.isSuccess());
    }


    private PanelResult makeResult(String message) {
        PanelResult result = new PanelResult();
        result.addErrorMessage(message);
        return result;
    }
}