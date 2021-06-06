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
                    if (fields[6].equalsIgnoreCase("true")) {
                        panel.setTracking(true);
                    } else {
                        panel.setTracking(false);
                    }
                    allPanels.add(panel);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace(); // not sure if I need to do this here...
        }
        return allPanels;
    }

    @Override
    public List<Panel> findBySection(String section) throws DataAccessException {
        List<Panel> allPanels = findAll();
        List<Panel> sectionPanels = new ArrayList<>();
        for (Panel p : allPanels) {
            if (p.getSection().equals(section)) {
                sectionPanels.add(p);
            }
        }
        return sectionPanels;
    }

    @Override
    public Panel add(Panel panel) throws DataAccessException {
        List<Panel> panels = findAll();
        panel.setPanelId(getNextId(panels));
        panels.add(panel);
        writeAll(panels);
        return panel;
    }

    @Override
    public Panel findById(int panelId) throws DataAccessException {
        List<Panel> panels = findAll();
        for (Panel p : panels) {
            if (p.getPanelId() == panelId) {
                return p;
            }
        }
        return null;
    }

    @Override
    public boolean update(Panel panel) throws DataAccessException {
        List<Panel> panels = findAll();
        for (int i = 0; i < panels.size(); i++) {
            if (panels.get(i).getPanelId() == panel.getPanelId()) {
                panels.set(i, panel);
                writeAll(panels);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteById(int panelId) throws DataAccessException {
        List<Panel> panels = findAll();
        for (int i = 0; i < panels.size(); i++) {
            if (panels.get(i).getPanelId() == panelId) {
                panels.remove(i);
                writeAll(panels);
                return true;
            }
        }
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
        return String.format("%s,%s,%s,%s,%s,%s,%s",
                panel.getPanelId(),
                panel.getSection(),
                panel.getRow(),
                panel.getCol(),
                panel.getYear(),
                panel.getMaterial(),
                panel.isTracking());
    }

}
