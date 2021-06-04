package learn.solarFarm.data;

import learn.solarFarm.models.Material;
import learn.solarFarm.models.Panel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class PanelFileRepository implements PanelRepository {


    private static final String DELIMITER = ",";
    private static final String DELIMITER_REPLACEMENT = "@@@";
    private static final String HEADER = "panel_id,section,row,col,year,material,isTracked";
    private final String filePath;

    public PanelFileRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Panel> findAll() throws DataAccessException {
        List<Panel> allPanels = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] fields = line.split(",", -1);
                if (fields.length == 7) {
                    Panel panel = new Panel();
                    panel.setPanelId(Integer.parseInt(fields[0]));
                    panel.setSection(fields[1]);
                    panel.setRow(Integer.parseInt(fields[2]));
                    panel.setCol(Integer.parseInt(fields[3]));
                    panel.setYear(Integer.parseInt(fields[4]));
                    panel.setMaterial(Material.valueOf(fields[5]));
                    panel.setTracking(Boolean.getBoolean(fields[6])); // not sure if this getBoolean is what I need
                    allPanels.add(panel);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace(); // not sure if I need to do this here...
        }
        return allPanels;
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

    private int getNextId(List<Panel> allPanels) {
        int nextId = 0;
        for (Panel p : allPanels) {
            nextId = Math.max(nextId, p.getPanelId());
        }
        return nextId + 1;
    }

    private void writeAll(List<Panel> panels) throws DataAccessException {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.println(HEADER);
            for (Panel p : panels) {
                writer.println(serialize(p));
            }
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage(), ex);
        }
    }

    private String serialize(Panel panel) {
        return String.format("%s,%s,%s,%s,%s",
                panel.getPanelId(),
                panel.getSection(),
                panel.getRow(),
                panel.getCol(),
                panel.getYear(),
                panel.getMaterial(),
                panel.isTracking());
    }

//    private Encounter deserialize(String line) {
//        String[] fields = line.split(DELIMITER, -1);
//        if (fields.length == 5) {
//            Encounter encounter = new Encounter();
//            encounter.setEncounterId(Integer.parseInt(fields[0]));
//            encounter.setType(EncounterType.valueOf(fields[1]));
//            encounter.setWhen(restore(fields[2]));
//            encounter.setDescription(restore(fields[3]));
//            encounter.setOccurrences(Integer.parseInt(fields[4]));
//            return encounter;
//        }
//        return null;
//    }
}
