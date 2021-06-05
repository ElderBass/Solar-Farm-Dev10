package learn.solarFarm.domain;

import learn.solarFarm.data.DataAccessException;
import learn.solarFarm.data.PanelRepository;
import learn.solarFarm.models.Material;
import learn.solarFarm.models.Panel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PanelServiceTest {

    // TODO - set up repository double, establish known good state in here, test findAll and add right away

    private PanelRepository repository = new PanelRepositoryDouble();
    PanelService service = new PanelService(repository);

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
        PanelResult actual = makeResult("Panel Material is Required.");
        Panel panel = new Panel(0, "Test Section", 5, 5, 2020, null, true);
        PanelResult expected = service.add(panel);
        assertEquals(actual, expected);
    }

    @Test
    void shouldNotAddRowOutOfBounds() throws DataAccessException {
        PanelResult actual = makeResult("Panel Rows and Columns Must Be Between 1 and 250.");
        Panel panel = new Panel(0, "Test Section", 500, 5, 2020, Material.AMORPHOUS_SI, true);
        PanelResult expected = service.add(panel);
        assertEquals(actual, expected);

        panel = new Panel(0, "Test Section", 0, 5, 2020, Material.AMORPHOUS_SI, true);
        expected = service.add(panel);
        assertEquals(actual, expected);
    }

    @Test
    void shouldNotAddColumnOutOfBounds() throws DataAccessException {
        PanelResult actual = makeResult("Panel Rows and Columns Must Be Between 1 and 250.");
        Panel panel = new Panel(0, "Test Section", 5, 500, 2020, Material.AMORPHOUS_SI, true);
        PanelResult expected = service.add(panel);
        assertEquals(actual, expected);

        panel = new Panel(0, "Test Section", 5, 0, 2020, Material.AMORPHOUS_SI, true);
        expected = service.add(panel);
        assertEquals(actual, expected);
    }

    @Test
    void shouldNotAddYearFromFuture() throws DataAccessException {
        PanelResult actual = makeResult("Panels Cannot Be Installed In the Future. (Or can they...??)");
        Panel panel = new Panel(0, "Test Section", 5, 50, 2030, Material.AMORPHOUS_SI, true);
        PanelResult expected = service.add(panel);
        assertEquals(actual, expected);
    }


    private PanelResult makeResult(String message) {
        PanelResult result = new PanelResult();
        result.addErrorMessage(message);
        return result;
    }
}