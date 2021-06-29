package learn.solarFarm.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

import learn.solarFarm.data.PanelRepository;
import learn.solarFarm.models.Material;
import learn.solarFarm.models.Panel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest       // 1. Without an argument, @SpringBootTest creates a mock MVC environment.
@AutoConfigureMockMvc // 2. Configure the mock MVC environment.
class PanelControllerTest {

    // 3. Mock PetRepository with Mockito.
    // This ensure we don't need to worry about true data access.
    @MockBean
    PanelRepository repository;

    // 4. Create a field for mock MVC and let Spring Boot inject it.
    @Autowired
    MockMvc mvc;

    @Test
    void shouldGetAll() throws Exception {

        List<Panel> panels = List.of(
                new Panel(1, "Mars", 5, 5, 2019, Material.AMORPHOUS_SI, true),
                new Panel(2, "Moon", 6, 6, 2020, Material.MONO_SI, true),
                new Panel(3, "Venus", 7, 7, 2021, Material.MULTI_SI, true)
        );

        // 5. ObjectMapper is the default JSON serializer for Spring MVC.
        // We use it to generate the expected HTTP response body.
        ObjectMapper jsonMapper = new ObjectMapper();
        String expectedJson = jsonMapper.writeValueAsString(panels);

        // 6. Configure the per-test behavior for mock PetRepository.
        when(repository.findAll()).thenReturn(panels);

        // 7. Send a mock HTTP request and assert facts about the response.
        mvc.perform(get("/panel"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void shouldAdd() throws Exception {
        // 1. Configure per-test mock repository behavior.
        Panel panelIn = new Panel(0, "Mars", 5, 5, 2019, Material.AMORPHOUS_SI, true);
        Panel expected = new Panel(1, "Mars", 5, 5, 2019, Material.AMORPHOUS_SI, true);

        when(repository.add(any())).thenReturn(expected);

        // 2. Generate both input and expected JSON.
        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonIn = jsonMapper.writeValueAsString(panelIn);
        String expectedJson = jsonMapper.writeValueAsString(expected);

        // 3. Build the request.
        var request = post("/panel")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonIn);

        // 4. Send the request and assert.
        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void shouldNotAddEmptySection() throws Exception {
        // 1. No mock behavior required because
        // it never makes it to the repository.
        Panel panelIn = new Panel(0, "", 5, 5, 2020, Material.AMORPHOUS_SI, false);

        // 2. Generate input JSON.
        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonIn = jsonMapper.writeValueAsString(panelIn);

        var request = post("/panel")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonIn);

        // 3. Send the request and assert.
        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }
}