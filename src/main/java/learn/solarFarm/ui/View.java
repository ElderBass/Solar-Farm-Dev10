package learn.solarFarm.ui;

import learn.solarFarm.domain.PanelResult;
import learn.solarFarm.models.Material;
import learn.solarFarm.models.Panel;

import java.util.ArrayList;
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
        System.out.println("5. Delete a Panel");
        System.out.println();
        return readInt("Choose an Option [0-5]: ", 0, 5);
    }

    public String selectSectionToDisplay(List<Panel> panels) {
        List<String> sections = new ArrayList<>();
        for (int i = 0; i < panels.size(); i++) {
            if (!sections.contains(panels.get(i).getSection())) {
                sections.add(panels.get(i).getSection());
                continue;
            }
        }
        displaySections(sections);
        return sections.get(readInt("Choose a Section [1-" + sections.size() + "]: ", 1, sections.size()) - 1);
    }

    private void displaySections(List<String> sections) {
        displayHeader("Viewing All Sections");
        for (int i = 0; i < sections.size(); i++) {
            System.out.println((i + 1) + ". " + sections.get(i));
        }
    }

    public void displayHeader(String message) {
        int length = message.length();
        System.out.println("");
        System.out.println(message);
        System.out.println("=".repeat(length));
    }

    public void printResult(PanelResult result) {
        if (result.isSuccess()) {
            System.out.println("Operation Successful.");
        } else {
            displayHeader("Errors");
            for (String msg : result.getMessages()) {
                System.out.printf("- %s%n", msg);
                System.out.println("Could not perform operation. Please try again.");
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
        panel.setMaterial(printMaterialsAndSelect());
        if (readRequiredString("Is This Tracked [y/n]? ").equalsIgnoreCase("y")) {
            panel.setTracking(true);
        } else {
            panel.setTracking(false);
        }
        System.out.println();
        return panel;
    }

    public Panel update(Panel panel) {
        displayHeader("Updating Panel");
        System.out.println();
        System.out.println("Make selection for Material with numberpad.");
        System.out.println("Hit [Enter] to keep other fields at their previous values.");
        System.out.println();
        System.out.println("Previous Material: " + panel.getMaterial().getAbbreviation());
        Material material = printMaterialsAndSelect();
        panel.setMaterial(material);
        String year = readString("Year Installed (" + panel.getYear() + "): ");
        if (year.trim().length() > 0) {
            panel.setYear(Integer.parseInt(year));
        }
        String isTracking = readString("Tracking Panel (" + panel.isTracking() + "): ");
        if (isTracking.trim().length() > 0) {
            if (isTracking.equalsIgnoreCase("true")) {
                panel.setTracking(true);
            } else {
                panel.setTracking(false);
            }
        }
        System.out.println();
        return panel;
    }

    public Panel updatePanel(List<Panel> panels) {
        printPanels(panels);
        if (panels.size() == 0) {
            return null;
        }
        System.out.println();
        int panelId = readInt("Enter ID of Panel You Wish To Update: ");
        for (Panel p : panels) {
            if (p.getPanelId() == panelId) {
                return update(p);
            }
        }
        System.out.println("Panel Id " + panelId + " not found.");
        return null;
    }

    public int deletePanel(List<Panel> panels) {
        printPanels(panels);
        System.out.println();
        return readInt("Enter the ID of the Panel to Delete: ");
    }

    private Material printMaterialsAndSelect() {
        System.out.println("Material Selection:");
        Material[] materials = Material.values();
        Material result = null;
        for (int i = 0; i < materials.length; i++) {
            System.out.println((i+1) + ". " + materials[i].getAbbreviation());
        }
        int choice = readInt("Choose Material [1-5]: ", 1, 5);
        switch (choice) {
            case 1:
                result = Material.MULTI_SI;
                break;
            case 2:
                result = Material.MONO_SI;
                break;
            case 3:
                result = Material.AMORPHOUS_SI;
                break;
            case 4:
                result = Material.CDTE;
                break;
            case 5:
                result = Material.CIGS;
                break;
        }
        return result;
    }

    private String readRequiredString(String prompt) {
        String result = null;
        do {
            result = readString(prompt).trim();
            if (result.isEmpty()) {
                System.out.println("Value is required, sorry.");
            }
        } while (result.length() == 0);
        return result;
    }

    // TODO create a readBoolean method to prompt for tracking and ensure a y or n

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
        while (!isValid) {
            try {
                result = Integer.parseInt(readRequiredString(prompt));
                isValid = true;
            } catch (NumberFormatException ex) {
                System.out.println("Value must be a number");
            }
        }
        return result;
    }

    private String readString(String prompt) {
        System.out.print(prompt);
        return console.nextLine();
    }

}
