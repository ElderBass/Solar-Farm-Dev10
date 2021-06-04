package learn.solarFarm.ui;

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
