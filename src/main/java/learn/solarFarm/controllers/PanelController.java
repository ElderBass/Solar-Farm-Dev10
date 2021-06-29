package learn.solarFarm.controllers;

import learn.solarFarm.data.DataAccessException;
import learn.solarFarm.domain.PanelResult;
import learn.solarFarm.domain.PanelService;
import learn.solarFarm.models.Panel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://initial-domain.com"})
@RequestMapping("/panel") // 2. Base URL
public class PanelController {

    private final PanelService service;

    // 3. Auto-inject PetService
    public PanelController(PanelService service) {
        this.service = service;
    }

    @GetMapping
    public List<Panel> findAll() throws DataAccessException {
        return service.findAll();
    }

    @GetMapping("/{panelId}")
    public ResponseEntity<Panel> findById(@PathVariable int panelId) throws DataAccessException {
        Panel panel = service.findById(panelId);
        if (panel == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(panel, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Panel> add(@RequestBody Panel panel) throws DataAccessException {
        PanelResult result = service.add(panel);
        if (!result.isSuccess()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
    }

    @PutMapping("/{panelId}")
    public ResponseEntity<Void> update(@PathVariable int panelId, @RequestBody Panel panel) throws DataAccessException {

        // id conflict. stop immediately.
        if (panelId != panel.getPanelId()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

       PanelResult result = service.update(panel);
        if (!result.isSuccess()) {
            for (String error : result.getMessages()) {
                if (error.startsWith("Could Not Find Panel With ID")) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                } else {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{panelId}")
    public ResponseEntity<Void> delete(@PathVariable int panelId) throws DataAccessException {
        if (service.deleteById(panelId).isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
