package learn.solarFarm.data;

import learn.solarFarm.models.Material;
import learn.solarFarm.models.Panel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


//import learn.solarFarm.data.DataAccessException;
//import learn.solarFarm.data.PanelFileRepository;
//import learn.solarFarm.data.PanelRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

class PanelFileRepositoryTest {

    static final String TEST_PATH = "./data/solar-panels-test.csv";
    static final String SEED_PATH = "./data/solar-panels-seed.csv";
    PanelRepository repository = new PanelFileRepository(TEST_PATH);

    @BeforeEach
    void setup() throws DataAccessException, IOException {
        Files.copy(Paths.get(SEED_PATH), Paths.get(TEST_PATH), StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindAll() throws DataAccessException {
        int actual = repository.findAll().size();
        assertEquals(5, actual);
    }

    @Test
    void shouldFindPanelsBySection() throws DataAccessException {
        int actual = repository.findBySection("Mars").size();
        assertEquals(2, actual);

        actual = repository.findBySection("Moon").size();
        assertEquals(2, actual);
    }

    @Test
    void shouldAdd() throws DataAccessException {
        Panel panel = new Panel(6, "Test Section", 5, 5, 2020, Material.AMORPHOUS_SI, true);
        Panel actual = repository.add(panel);

        assertNotNull(panel);
        assertEquals(6, actual.getPanelId());
    }

    @Test
    void shouldUpdateExistingPanel() throws DataAccessException {
        List<Panel> panels = repository.findAll();
        Panel panel = panels.get(0); // 1, Moon, 5, 5, 2020, CDTE, true;
        panel.setTracking(false);
        panel.setRow(10);
        panel.setCol(10);
        panel.setSection("Venus");

        assertTrue(repository.update(panel));
        assertEquals(10, panels.get(0).getRow());
        assertEquals("Venus", panels.get(0).getSection());
    }

    @Test
    void shouldNotUpdateNonexistentPanel() throws DataAccessException {
        Panel panel = new Panel();
        panel.setPanelId(20);
        assertFalse(repository.update(panel));
    }

    @Test
    void shouldDeleteExistingPanel() throws DataAccessException {

        // Delete first entry in our seed file = 1, Moon, 5, 5, 2020, CDTE, true;
        boolean result = repository.deleteById(1);
        assertTrue(result);

        List<Panel> panels = repository.findAll();

        // Also check if the new first entry in the seed file is indeed the former 2nd entry by comparing IDs
        assertEquals(4, panels.size());
    }

    @Test
    void shouldNotDeleteNonexistentPanel() throws DataAccessException {
        boolean deleted = repository.deleteById(12);
        assertFalse(deleted);
    }
}