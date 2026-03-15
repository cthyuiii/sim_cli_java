// ConsoleUI.java
package simsgame;

import java.util.List;
import java.util.Scanner;
import simsgame.engine.GameEngine;

/**
 * ConsoleUI is PRESENTATION ONLY.
 * - It prints menus, reads input, and calls GameEngine methods.
 * - It should NOT directly modify SimCharacter, Needs, Wallet, etc.
 *
 * Required GameEngine API (implement these in engine/GameEngine.java):
 *   void startNewGame();                         // reset state if needed
 *   void createSim(String name);                 // add a new sim
 *   int getSimCount();
 *   List<String> getSimNames();                  // names in same order as internal list
 *   int getActiveSimIndex();
 *   void setActiveSimIndex(int index);
 *   String getActiveSimStatus();                 // formatted status string for current sim
 *
 *   List<String> getActivityNames();             // for activity menu display
 *   String performActivity(int activityIndex);   // returns result/summary message (or empty)
 *
 *   List<String> getCareerOptions();             // names like "ShiftJob - Barista", etc.
 *   String assignCareerToActiveSim(int type, String title); // returns result/summary
 *
 *   String tryForBabyActiveSim();                // optional: if you implement family actions
 */
public class ConsoleUI {

    private final GameEngine engine;
    private final Scanner sc;

    public ConsoleUI(GameEngine engine) {
        this.engine = engine;
        this.sc = new Scanner(System.in);
    }

    public void run() {
        engine.startNewGame();

        boolean running = true;
        while (running) {
            printHeader("MAIN MENU");
            System.out.println("Active Sim: " + getActiveSimLabel());
            System.out.println("1) Create Sim");
            System.out.println("2) Select Active Sim");
            System.out.println("3) Actions (Activities)");
            System.out.println("4) Career");
            System.out.println("5) Family");
            System.out.println("6) View Active Sim Status");
            System.out.println("0) Quit");

            int choice = readInt("Choose: ", 0, 6);
            switch (choice) {
                case 1 -> createSimFlow();
                case 2 -> selectSimFlow();
                case 3 -> actionsFlow();
                case 4 -> careerFlow();
                case 5 -> familyFlow();
                case 6 -> showStatusFlow();
                case 0 -> running = false;
            }
        }

        System.out.println("\nGoodbye!");
        sc.close();
    }

    // -----------------------------
    // Flows
    // -----------------------------

    private void createSimFlow() {
        printHeader("CREATE SIM");
        String name = readNonEmptyLine("Enter Sim name: ");
        engine.createSim(name);

        // If this is the first sim, auto-select it.
        if (engine.getSimCount() == 1) {
            engine.setActiveSimIndex(0);
        }

        System.out.println("Created Sim: " + name);
        pause();
    }

    private void selectSimFlow() {
        printHeader("SELECT ACTIVE SIM");

        if (engine.getSimCount() == 0) {
            System.out.println("No Sims yet. Create one first.");
            pause();
            return;
        }

        List<String> names = engine.getSimNames();
        for (int i = 0; i < names.size(); i++) {
            String marker = (i == engine.getActiveSimIndex()) ? " (active)" : "";
            System.out.println((i + 1) + ") " + names.get(i) + marker);
        }
        System.out.println("0) Back");

        int choice = readInt("Choose a Sim: ", 0, names.size());
        if (choice == 0) return;

        engine.setActiveSimIndex(choice - 1);
        System.out.println("Active Sim is now: " + getActiveSimLabel());
        pause();
    }

    private void actionsFlow() {
        printHeader("ACTIONS (ACTIVITIES)");

        if (!ensureHasActiveSim()) return;

        List<String> actions = engine.getActivityNames();
        if (actions == null || actions.isEmpty()) {
            System.out.println("No activities registered yet.");
            pause();
            return;
        }

        boolean stay = true;
        while (stay) {
            System.out.println("\nActive Sim: " + getActiveSimLabel());
            System.out.println("Choose an activity:");

            for (int i = 0; i < actions.size(); i++) {
                System.out.println((i + 1) + ") " + actions.get(i));
            }
            System.out.println("9) View Status");
            System.out.println("0) Back");

            int choice = readInt("Choose: ", 0, Math.max(actions.size(), 9));
            if (choice == 0) return;
            if (choice == 9) {
                showStatusFlow();
                continue;
            }

            if (choice >= 1 && choice <= actions.size()) {
                String result = engine.performActivity(choice - 1);
                if (result != null && !result.isBlank()) {
                    System.out.println("\n" + result);
                }
                // Always show status briefly after an action (nice UX)
                System.out.println("\n" + engine.getActiveSimStatus());
                pause();
            } else {
                System.out.println("Invalid option.");
            }
        }
    }

    private void careerFlow() {
        printHeader("CAREER");

        if (!ensureHasActiveSim()) return;

        boolean stay = true;
        while (stay) {
            System.out.println("\nActive Sim: " + getActiveSimLabel());
            System.out.println("1) Assign/Change Career");
            System.out.println("2) View Status");
            System.out.println("0) Back");

            int choice = readInt("Choose: ", 0, 2);
            switch (choice) {
                case 1 -> assignCareerFlow();
                case 2 -> showStatusFlow();
                case 0 -> stay = false;
            }
        }
    }

    private void assignCareerFlow() {
        printHeader("ASSIGN CAREER");

        List<String> options = engine.getCareerOptions();
        if (options == null || options.isEmpty()) {
            System.out.println("No career options available.");
            pause();
            return;
        }

        System.out.println("Choose a job type:");
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ") " + options.get(i));
        }
        System.out.println("0) Back");

        int choice = readInt("Choose a job type: ", 0, options.size());
        if (choice == 0) return;

        String customTitle = readNonEmptyLine("Enter the job title: ");

        String result = engine.assignCareerToActiveSim(choice, customTitle);
        System.out.println(result);
        pause();
    }

    private void familyFlow() {
        printHeader("FAMILY");

        if (!ensureHasActiveSim()) return;

        boolean stay = true;
        while (stay) {
            System.out.println("\nActive Sim: " + getActiveSimLabel());
            System.out.println("1) Try for Baby");
            System.out.println("2) View Status");
            System.out.println("0) Back");

            int choice = readInt("Choose: ", 0, 2);
            switch (choice) {
                case 1 -> {
                    // Optional feature: if not implemented, engine can return a helpful message
                    String msg = engine.tryForBabyActiveSim();
                    if (msg != null && !msg.isBlank()) System.out.println(msg);
                    pause();
                }
                case 2 -> showStatusFlow();
                case 0 -> stay = false;
            }
        }
    }

    private void showStatusFlow() {
        printHeader("ACTIVE SIM STATUS");

        if (!ensureHasActiveSim()) return;

        System.out.println(engine.getActiveSimStatus());
        pause();
    }

    // -----------------------------
    // Helpers
    // -----------------------------

    private boolean ensureHasActiveSim() {
        if (engine.getSimCount() == 0) {
            System.out.println("No Sims yet. Create a Sim first.");
            pause();
            return false;
        }
        int idx = engine.getActiveSimIndex();
        if (idx < 0 || idx >= engine.getSimCount()) {
            // Safety: auto-select first sim
            engine.setActiveSimIndex(0);
        }
        return true;
    }

    private String getActiveSimLabel() {
        if (engine.getSimCount() == 0) return "(none)";
        int idx = engine.getActiveSimIndex();
        List<String> names = engine.getSimNames();
        if (idx < 0 || idx >= names.size()) return "(none)";
        return names.get(idx);
    }

    private void printHeader(String title) {
        System.out.println("\n========================================");
        System.out.println(title);
        System.out.println("========================================");
    }

    private int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                int val = Integer.parseInt(line);
                if (val < min || val > max) {
                    System.out.println("Enter a number between " + min + " and " + max + ".");
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private String readNonEmptyLine(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            if (s != null && !s.trim().isEmpty()) return s.trim();
            System.out.println("Input cannot be empty.");
        }
    }

    private void pause() {
        System.out.print("\nPress Enter to continue...");
        sc.nextLine();
    }
}