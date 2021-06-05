package learn.solarFarm.ui;

import learn.solarFarm.domain.PanelResult;
import learn.solarFarm.models.Material;
import learn.solarFarm.models.Panel;

import java.util.List;
import java.util.Scanner;

public class View {

    private Scanner console = new Scanner(System.in);

    public View() {

    }

    public int displayMenuAndSelect() {
        displayHeader("Main Menu");
        System.out.println("0. Exit");
        System.out.println("1. Display All Panels");
        System.out.println("2. Display Panels By Section");
        System.out.println("3. Add a Panel");
        System.out.println("4. Update a Panel");
        System.out.println("4. Delete a Panel");
        return readInt("Choose [0-4]:", 0, 5);
    }

    public void displayHeader(String message) {
        int length = message.length();
        System.out.println("");
        System.out.println(message);
        System.out.println("=".repeat(length));
    }

    public void printResult(PanelResult result) {
        if (result.isSuccess()) {
            if (result.getPayload() != null) {
                System.out.printf("Panel Id %s added.%n", result.getPayload().getPanelId());
            } else {
                System.out.println("Panel has been deleted");
            }
        } else {
            displayHeader("Errors");
            for (String msg : result.getMessages()) {
                System.out.printf("- %s%n", msg);
            }
        }
    }

    public void printPanels(List<Panel> panels) {

        if (panels == null || panels.size() == 0) {
            System.out.println();
            System.out.println("No Panels Found.");
        } else {
            for (Panel p : panels) {
                System.out.printf("ID: %s, Section: %s, Row: %s, Column: %s, Year: %s, Material: %s, Tracked? %s%n",
                        p.getPanelId(),
                        p.getSection(),
                        p.getRow(),
                        p.getCol(),
                        p.getYear(),
                        p.getMaterial(),
                        p.isTracking());
            }
        }
    }

    public Panel createPanel() {
        Panel panel = new Panel();
        panel.setSection(readRequiredString("Farm Section: "));
        panel.setRow(readInt("Row: "));
        panel.setCol(readInt("Column: "));
        panel.setYear(readInt("Year Installed: "));
        panel.setMaterial(Material.valueOf(readRequiredString(("Material Type: "))));
        panel.setTracking((readRequiredString("Is This Tracked [y/n]? ").equalsIgnoreCase("y") ? true : false));
        return panel;
    }
// TODO might need this but might not, going to try it without first

//    private Material readMaterial(String materialType) {
//        Material material;
//        switch(materialType) {
//            case "Multi-Si":
//                material = Material.
//        }
//    }
    private String readRequiredString(String prompt) {
        String result = null;
        do {
            result = readString(prompt).trim();
            if (result.isEmpty()) {
                System.out.println("Value is required sorry.");
            }
        } while (result.length() == 0);
        return result;
    }

    private int readInt(String prompt, int min, int max) {
        int result = 0;
        do {
            result = readInt(prompt);
            if (result < min || result > max) {
                System.out.println("Value must be between " + min + " and " + max);
            }
        } while (result < min || result > max);
        return result;
    }

    private int readInt(String prompt) {
        int result = 0;
        boolean isValid = false;
        while(!isValid) {
            try {
                result = Integer.parseInt(readRequiredString(prompt));
                isValid = true;
            } catch (NumberFormatException ex) {
                System.out.println("Value must be a number");
            }
        }
        return result;
    }
// TODO convert this to a more relevant readPanelSection() method maybe

//    public OrbiterType readOrbiterType() {
//        System.out.println("Types:");
//        OrbiterType[] values = OrbiterType.values();
//        for (int i = 0; i < values.length; i++) {
//            System.out.println((i + 1) + ": " + values[i]);
//        }
//        int index = readInt("Select [1-5]: ", 1, 5);
//        return values[index-1];
//    }

    private String readString(String prompt) {
        System.out.print(prompt);
        return console.nextLine();
    }

}
