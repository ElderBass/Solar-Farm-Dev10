package learn.solarFarm.data;

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

    }

    @Test
    void add() {
    }

    @Test
    void findById() {
    }

    @Test
    void update() {
    }

    @Test
    void deleteById() {
    }
}