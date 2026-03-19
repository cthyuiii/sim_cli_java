// ConsoleUI.java
package simsgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import simsgame.career.Career;
import simsgame.engine.GameEngine;
import simsgame.sims.Needs;
import simsgame.sims.SimCharacter;

/**
 * ConsoleUI is the user interface class for the sims game.
 * It handles all user interactions, including displaying menus and processing input.
 */
public class ConsoleUI {
    // =====================================================
    // ANSI Color Constants
    // =====================================================

    public static final String RESET = "\033[0m";
    public static final String BOLD = "\033[1m";
    public static final String DIM = "\033[2m";

    /** Foreground colors */
    public static final String GREEN = "\033[92m";
    public static final String YELLOW = "\033[93m";
    public static final String RED = "\033[91m";
    public static final String CYAN = "\033[96m";
    public static final String MAGENTA = "\033[95m";
    public static final String BLUE = "\033[94m";
    public static final String WHITE = "\033[97m";
    public static final String GRAY = "\033[90m";

    /** Background colors */
    public static final String BG_RED = "\033[101m";
    public static final String BG_GREEN = "\033[102m";

    /** Activity information container to hold metadata for quick actions. */
    private static class ActivityInfo {
        String icon, name, effects;
        int hours, cost;
        ActivityInfo(String icon, String name, int hours, int cost, String effects) {
            this.icon = icon; this.name = name; this.hours = hours;
            this.cost = cost; this.effects = effects;
        }
    }

    /** Activity metadata array that holds each activity's information. */
    private final ActivityInfo[] ACTIVITY_METADATA = {
        new ActivityInfo("🍖", "Eat", 1, 10, "+25 Hunger"),
        new ActivityInfo("⚡", "Sleep", 8, 0, "+40 Energy"),
        new ActivityInfo("🚿", "Shower", 1, 0, "+30 Hygiene"),
        new ActivityInfo("👥", "Social", 2, 0, "+25 Social, +10 Fun"),
        new ActivityInfo("🎮", "Play", 2, 5, "+25 Fun, -10 Energy"),
        new ActivityInfo("💼", "Work", -1, 0, "Varies by career")
    };

    // =====================================================
    // Data Container Class
    // =====================================================

    /** Needs data container to showcase the needs of a SimCharacter. */
    private static class NeedsData {
        int hunger, energy, hygiene, social, fun;
        NeedsData(int h, int e, int hy, int s, int f) {
            hunger = h; energy = e; hygiene = hy; social = s; fun = f;
        }
    }

    // =====================================================
    // Core Fields
    // =====================================================
    private final GameEngine engine;
    private final Scanner sc;

    /**
     * Constructs a ConsoleUI instance with the specified GameEngine.
     *
     * @param engine The GameEngine instance to use.
     */
    public ConsoleUI(GameEngine engine) {
        this.engine = engine;
        this.sc = new Scanner(System.in);
    }

    // =====================================================
    // Main Loop
    // =====================================================

    /** Main game loop where the game runs until the user quits. */
    public void run() {
        engine.startNewGame();

        boolean running = true;
        while (running) {
            printDashboard();

            // Check if it's first run (no sims) - restrict options
            boolean firstRun = (engine.getSimCount() == 0);

            if (firstRun) {
                // First run: only allow 9 (create sim) or 0 (quit)
                int choice = readInt("Choose action [0 or 9]: ", 0, 9);
                if (choice == 9) {
                    simManagementFlow();
                } else if (choice == 0) {
                    running = false;
                } else {
                    System.out.println(RED + "⚠️  Please create a Sim first (option 9) or quit (0)." + RESET);
                    pause();
                }
            } else {
                // Normal gameplay
                int choice = readInt("Choose action [0-9]: ", 0, 9);

                if (choice >= 1 && choice <= 6) {
                    handleQuickAction(choice);
                } else {
                    switch (choice) {
                        case 7 -> careerFlow();
                        case 8 -> familyFlow();
                        case 9 -> simManagementFlow();
                        case 0 -> running = false;
                    }
                }
            }
        }

        System.out.println("\n" + CYAN + "Goodbye! Thanks for playing! 🎮" + RESET);
        sc.close();
    }

    // =====================================================
    // Dashboard View
    // =====================================================

    /**
     * Prints the dashboard view for the active SimCharacter.
     */
    private void printDashboard() {
        clearScreen();
        printBorder("double", "🏠  THE SIMS - TEXT EDITION  🏠");
        System.out.println();

        // Check if first run - show welcome message
        if (engine.getSimCount() == 0) {
            printWelcomeScreen();
            return;
        }

        // Check if active sim is dead - show death screen
        SimCharacter activeSim = engine.getActiveSim();
        if (activeSim != null && !activeSim.isAlive()) {
            printDeathScreen();
            return;
        }

        // Normal dashboard
        printStatusCard();
        System.out.println();
        printQuickActions();
        System.out.println();
        printWarnings();
        printFooterMenu();
    }

    /**
     * Prints the welcome screen for new players.
     */
    private void printWelcomeScreen() {
        printBorder("section", "WELCOME TO THE SIMS!");
        System.out.println("│" + " ".repeat(62) + "│");
        System.out.println("│  " + CYAN + "Welcome to The Sims - Text Edition!" + RESET + " ".repeat(25) + "│");
        System.out.println("│" + " ".repeat(62) + "│");
        System.out.println("│  " + WHITE + "Create your first Sim to begin your journey." + RESET + " ".repeat(16) + "│");
        System.out.println("│  " + WHITE + "Manage their needs, build a career, and live life!" + RESET + " ".repeat(10) + "│");
        System.out.println("│" + " ".repeat(62) + "│");
        printBorder("bottom", "");
        System.out.println();
        System.out.println(GREEN + BOLD + "9) 👤 Create Your First Sim" + RESET);
        System.out.println("0) 🚪 Quit Game");
        System.out.println();
    }

    /**
     * Prints the death screen for a deceased SimCharacter.
     */
    private void printDeathScreen() {
        SimCharacter sim = engine.getActiveSim();
        if (sim == null) return;

        printBorder("section", "");
        System.out.println("│" + " ".repeat(62) + "│");
        // "💀 R.I.P. 💀" = 11 chars + 2 emojis (each counts as 2) = 13 visual chars
        // Center it: (62 - 13) / 2 = 24.5, so 24 spaces on left, 25 on right
        System.out.println("│" + " ".repeat(25) + RED + BOLD + "💀 R.I.P. 💀" + RESET + " ".repeat(25) + "│");
        System.out.println("│" + " ".repeat(62) + "│");

        // "[Name] has passed away..." = name.length() + 20
        String deathMsg = sim.getName() + " has passed away...";
        int msgPadding = (62 - deathMsg.length()) / 2;
        int rightPadding = 62 - deathMsg.length() - msgPadding;
        System.out.printf("│%s%s%s%s%s│\n",
            " ".repeat(msgPadding),
            RED + BOLD,
            deathMsg,
            RESET,
            " ".repeat(rightPadding));

        System.out.println("│" + " ".repeat(62) + "│");
        System.out.println("│  " + GRAY + "Their life has come to an end. They can no longer" + RESET + " ".repeat(11) + "│");
        System.out.println("│  " + GRAY + "perform any actions." + RESET + " ".repeat(40) + "│");
        System.out.println("│" + " ".repeat(62) + "│");
        printBorder("bottom", "");
        System.out.println();

        // Show final status
        NeedsData needs = getSimNeeds();
        System.out.println(DIM + "Final Status:" + RESET);
        System.out.println(DIM + "  Hunger:  " + (needs.hunger <= 0 ? RED + needs.hunger + " (STARVED)" : needs.hunger) + RESET);
        System.out.println(DIM + "  Energy:  " + (needs.energy <= 0 ? RED + needs.energy + " (EXHAUSTED)" : needs.energy) + RESET);
        System.out.println(DIM + "  Hygiene: " + needs.hygiene + RESET);
        System.out.println(DIM + "  Social:  " + needs.social + RESET);
        System.out.println(DIM + "  Fun:     " + needs.fun + RESET);
        System.out.println();

        // Next steps for player
        System.out.println(YELLOW + "Options:" + RESET);
        System.out.println("9) 👤 Manage Sims (switch to another Sim or create new)");
        System.out.println("0) 🚪 Quit Game");
        System.out.println();
    }

    /**
     * Prints the status card for the active SimCharacter.
     */
    private void printStatusCard() {
        if (!ensureHasActiveSim()) {
            System.out.println(YELLOW + "⚠️  No Sim created. Please create a Sim first (option 9)." + RESET);
            return;
        }

        String name = getActiveSimLabel();
        double money = getSimMoney();
        String career = getSimCareer();
        String mood = getSimMood();
        int day = getCurrentDay();
        int hour = getCurrentHour();
        NeedsData needs = getSimNeeds();

        printBorder("section", "STATUS CARD");

        // Header info - Name and Money
        // Visual: "Name: " (6) + name + "  💰 Money: " (11 + emoji=2 = 13) + money + " " (1)
        String moneyDisplay = formatMoney(money);
        int line1Visual = 6 + name.length() + 13 + moneyDisplay.length() + 1;
        int namePadding = Math.max(0, 64 - line1Visual);
        System.out.printf("│ Name: %s%s%s%s💰 Money: %s%s%s %s│\n",
            BOLD, name, RESET,
            " ".repeat(namePadding),
            BLUE + BOLD, moneyDisplay, RESET,
            "");

        // Career and Time
        // Visual: "Career: " (8) + career + " 📅 Day " (9) + day + ", Hour " (7) + hour + " (" (2) + time + ") " (2)
        String dayStr = String.valueOf(day);
        String hourStr = String.valueOf(hour);
        String timeStr = formatTime(hour);
        int line2Visual = 8 + career.length() + 9 + dayStr.length() + 7 + hourStr.length() + 2 + timeStr.length() + 2;
        int careerPadding = Math.max(0, 63 - line2Visual);
        System.out.printf("│ Career: %s%s📅 Day %s%s%s, Hour %s%s%s (%s) %s│\n",
            career,
            " ".repeat(careerPadding),
            CYAN, day, RESET,
            CYAN, hour, RESET,
            timeStr, "");

        // Mood
        // Visual: "Mood: " (6) + emoji (2) + " " (1) + mood + " " (1)
        String moodIcon = getMoodIcon(mood);
        String moodColor = getMoodColor(mood);
        int line3Visual = 6 + 2 + 1 + mood.length() + 1;
        int moodPadding = Math.max(0, 61 - line3Visual);
        System.out.printf("│ Mood: %s %s%s%s %s│\n",
            moodIcon,
            moodColor + BOLD, mood, RESET,
            " ".repeat(moodPadding));

        printBorder("divider", "");

        // Progress bars
        printProgressBar("🍖", "Hunger", needs.hunger, 100);
        printProgressBar("⚡", "Energy", needs.energy, 100);
        printProgressBar("🚿", "Hygiene", needs.hygiene, 100);
        printProgressBar("👥", "Social", needs.social, 100);
        printProgressBar("🎮", "Fun", needs.fun, 100);

        printBorder("bottom", "");
    }

    /**
     * Prints the quick actions menu for the active SimCharacter.
     */
    private void printQuickActions() {
        printBorder("section", "QUICK ACTIONS");

        double money = getSimMoney();
        SimCharacter sim = engine.getActiveSim();
        boolean hasCareer = sim != null && sim.getCareer() != null;

        for (int i = 0; i < ACTIVITY_METADATA.length; i++) {
            ActivityInfo info = ACTIVITY_METADATA[i];
            boolean affordable = (info.cost == 0) || (money >= info.cost);
            boolean enabled = (i != 5) || hasCareer;

            String color = (affordable && enabled) ? WHITE : GRAY;
            String dim = (affordable && enabled) ? "" : DIM;

            String costStr = info.cost > 0 ? formatMoney(info.cost) : "Free";
            String hoursStr = (i == 5 && hasCareer) ? getWorkHoursEstimate(sim.getCareer()) :
                              (info.hours > 0 ? info.hours + "h" : "0h");

            String suffixText = "";
            int suffixLen = 0;
            if (!affordable) {
                suffixText = " (Can't afford)";
                suffixLen = 15;
            } else if (!enabled) {
                suffixText = " (No career)";
                suffixLen = 12;
            }

            // Calculate visual length
            // Format: "N) emoji name (Xh) - $cost → effects"
            // Visual: N (1-2 digits) + ") " (2) + emoji (2) + " " (1) + name + " (" (2) + hours + ") - " (4) + cost + " → " (3) + effects
            int numLen = (i + 1) >= 10 ? 2 : 1;
            int lineVisual = numLen + 2 + 2 + 1 + info.name.length() + 2 + hoursStr.length() + 4 + costStr.length() + 3 + info.effects.length() + suffixLen;
            int padding = Math.max(0, 60 - lineVisual);

            if (!affordable || !enabled) {
                System.out.printf("│ %s%s%d) %s %s (%s) - %s → %s%s%s%s%s │\n",
                    dim, color, i + 1, info.icon, info.name, hoursStr, costStr, info.effects,
                    GRAY + DIM, suffixText, RESET, " ".repeat(padding));
            } else {
                System.out.printf("│ %s%s%d) %s %s (%s) - %s → %s%s%s │\n",
                    dim, color, i + 1, info.icon, info.name, hoursStr, costStr, info.effects,
                    RESET, " ".repeat(padding));
            }
        }

        printBorder("bottom", "");
    }

    /**
     * Prints the warnings for the active SimCharacter's needs.
     */
    private void printWarnings() {
        NeedsData needs = getSimNeeds();
        List<String> criticalNeeds = new ArrayList<>();
        List<String> lowNeeds = new ArrayList<>();

        if (needs.hunger <= 10) criticalNeeds.add("🍖 Hunger");
        else if (needs.hunger <= 30) lowNeeds.add("🍖 Hunger");

        if (needs.energy <= 10) criticalNeeds.add("⚡ Energy");
        else if (needs.energy <= 30) lowNeeds.add("⚡ Energy");

        if (needs.hygiene <= 10) criticalNeeds.add("🚿 Hygiene");
        else if (needs.hygiene <= 30) lowNeeds.add("🚿 Hygiene");

        if (needs.social <= 10) criticalNeeds.add("👥 Social");
        else if (needs.social <= 30) lowNeeds.add("👥 Social");

        if (needs.fun <= 10) criticalNeeds.add("🎮 Fun");
        else if (needs.fun <= 30) lowNeeds.add("🎮 Fun");

        if (!criticalNeeds.isEmpty()) {
            System.out.println();
            System.out.printf("%s🚨 EMERGENCY: %s critically low! IMMEDIATE ACTION REQUIRED!%s\n",
                BG_RED + WHITE + BOLD,
                String.join(", ", criticalNeeds),
                RESET);
        }

        if (!lowNeeds.isEmpty()) {
            System.out.println();
            System.out.printf("%s⚠️  WARNING: %s declining. Address soon!%s\n",
                YELLOW + BOLD,
                String.join(", ", lowNeeds),
                RESET);
        }

        // Smart recommendations
        if (needs.energy <= 30) {
            System.out.printf("%sℹ️  Recommendation: Press %s2%s to Sleep (8h, +40 Energy)%s\n",
                CYAN, BOLD, CYAN, RESET);
        }
        if (needs.hunger <= 30 && getSimMoney() >= 10) {
            System.out.printf("%sℹ️  Recommendation: Press %s1%s to Eat (1h, +25 Hunger)%s\n",
                CYAN, BOLD, CYAN, RESET);
        }
        if (needs.hygiene <= 30) {
            System.out.printf("%sℹ️  Recommendation: Press %s3%s to Shower (1h, +30 Hygiene)%s\n",
                CYAN, BOLD, CYAN, RESET);
        }
    }

    /**
     * Prints the footer menu with navigation options.
     */
    private void printFooterMenu() {
        System.out.println();
        System.out.println("7) 💼 Career Management    8) 👶 Family Options    9) 👤 Manage Sims");
        System.out.println("0) 🚪 Quit Game");
        System.out.println();
    }

    // =====================================================
    // Quick Action Handler
    // =====================================================

    /**
     * Handles quick action selection for the active SimCharacter. Activities include Eat, Sleep, Shower, Socialize, Play, and Work. Each activity checks for affordability and special conditions before execution, then provides feedback on the results and time advancement.
     *
     * @param choice The selected action choice (1-6).
     */
    private void handleQuickAction(int choice) {
        if (choice < 1 || choice > 6) return;
        if (!ensureHasActiveSim()) return;

        // Check if sim is alive
        SimCharacter sim = engine.getActiveSim();
        if (sim != null && !sim.isAlive()) {
            System.out.println();
            System.out.printf("%s⚠️  %s is deceased and cannot perform actions.%s\n",
                RED + BOLD, sim.getName(), RESET);
            System.out.println(CYAN + "Please switch to another Sim (option 9) or quit (0)." + RESET);
            pause();
            return;
        }

        int activityIndex = choice - 1;
        ActivityInfo info = ACTIVITY_METADATA[activityIndex];

        // Check affordability
        // Allow Eat to proceed even if broke, because EatActivity handles free-food fallback
        boolean isEatAction = activityIndex == 0;

        if (!isEatAction && info.cost > 0 && getSimMoney() < info.cost) {
            System.out.println();
            System.out.printf("%s⚠️  You can't afford %s (costs %s)%s\n",
            RED + BOLD, info.name, formatMoney(info.cost), RESET);
            pause();
            return;
        }

        // Special handling for Work
        if (choice == 6) {
            if (sim == null || sim.getCareer() == null) {
                System.out.println();
                System.out.printf("%s⚠️  No career assigned! Visit Career Management (7)%s\n",
                    RED + BOLD, RESET);
                pause();
                return;
            }
        }

        // Store old state
        int oldDay = getCurrentDay();
        int oldHour = getCurrentHour();
        NeedsData oldNeeds = getSimNeeds();

        // Perform activity
        String result = engine.performActivity(activityIndex);

        // Show result of the action
        System.out.println();
        printBorder("section", "ACTION RESULT");
        int resultLen = result.length();
        int resultPadding = Math.max(0, 61 - resultLen);
        System.out.printf("│ %s%s%s%s│\n",
            CYAN + BOLD, result, RESET + " ".repeat(resultPadding), RESET);
        printBorder("bottom", "");

        // handle baby naming after time advanced
        handlePendingBirths();

        // Time advancement feedback
        int hoursAdvanced = (getCurrentDay() - oldDay) * 24 + (getCurrentHour() - oldHour);
        System.out.printf("\n%s⏰ Time advanced: %d hours %s(Day %d, Hour %d → Day %d, Hour %d)%s\n",
            CYAN, hoursAdvanced, DIM, oldDay, oldHour, getCurrentDay(), getCurrentHour(), RESET);

        // Needs decay feedback
        NeedsData newNeeds = getSimNeeds();
        System.out.printf("%s📉 Needs decay: %d Hunger, %d Energy, %d Hygiene, %d Social, %d Fun%s\n\n",
            YELLOW,
            Math.max(0, oldNeeds.hunger - newNeeds.hunger),
            Math.max(0, oldNeeds.energy - newNeeds.energy),
            Math.max(0, oldNeeds.hygiene - newNeeds.hygiene),
            Math.max(0, oldNeeds.social - newNeeds.social),
            Math.max(0, oldNeeds.fun - newNeeds.fun),
            RESET);

        pause();
    }

    // =====================================================
    // Sim Management Flow
    // =====================================================

    /**
     * Handles the Sim Management flow, allowing players to view existing Sims, switch active Sims, and create new Sims. The flow includes checks for deceased Sims and provides visual indicators for active and deceased Sims in the list.
     */
    private void simManagementFlow() {
        clearScreen();
        printBorder("double", "SIM MANAGEMENT");
        System.out.println();

        if (engine.getSimCount() == 0) {
            System.out.println(CYAN + "No Sims created yet. Let's create your first Sim!" + RESET);
            System.out.println();
            createSimFlow();
            return;
        }

        boolean stay = true;
        while (stay) {
            System.out.println("\n" + BOLD + "Active Sim: " + getActiveSimLabel() + RESET);
            System.out.println();

            List<String> names = engine.getSimNames();
            for (int i = 0; i < names.size(); i++) {
                String marker = (i == engine.getActiveSimIndex()) ?
                    GREEN + " ★ (active)" + RESET : "";

                // Check if this sim is dead
                engine.setActiveSimIndex(i);
                SimCharacter checkSim = engine.getActiveSim();
                String deathMarker = (checkSim != null && !checkSim.isAlive()) ?
                    RED + " 💀 (deceased)" + RESET : "";

                System.out.printf("%d) %s%s%s\n", i + 1, names.get(i), marker, deathMarker);
            }
            // Restore original active sim
            if (engine.getSimCount() > 0) {
                engine.setActiveSimIndex(Math.min(engine.getActiveSimIndex(), engine.getSimCount() - 1));
            }

            System.out.println();
            System.out.println((names.size() + 1) + ") Create New Sim");
            System.out.println("0) Back to Dashboard");
            System.out.println();

            int choice = readInt("Choose: ", 0, names.size() + 1);

            // Handle choices with checks for deceased sims
            if (choice == 0) {
                stay = false;
            } else if (choice == names.size() + 1) {
                createSimFlow();
            } else {
                engine.setActiveSimIndex(choice - 1);
                System.out.println();
                System.out.printf("%s✓ Active Sim is now: %s%s\n",
                    GREEN + BOLD, getActiveSimLabel(), RESET);
                pause();
                stay = false;
            }
        }
    }

    /**
     * Handles the creation of a new SimCharacter.
     */
    private void createSimFlow() {
        printHeader("CREATE SIM");
        String name = readNonEmptyLine("Enter Sim name: ");
        String gender = readGender();
        engine.createSim(name, gender);

        // If this is the first sim, auto-select it.
        if (engine.getSimCount() == 1) {
            engine.setActiveSimIndex(0);
        }

        System.out.println();
        printSuccess("Created Sim: " + name + " (" + gender + ")");
        pause();
    }

    /**
     * Reads the gender input for a new SimCharacter.
     *
     * @return The gender as a String ("Male" or "Female").
     */
    private String readGender() {
        while (true) {
            String input = readNonEmptyLine("Enter gender (Male/Female): ");
            if (input.equalsIgnoreCase("male")) return "Male";
            if (input.equalsIgnoreCase("female")) return "Female";
            System.out.println("Please enter Male or Female.");
        }
    }

    // =====================================================
    // Career Flow
    // =====================================================

    /**
     * Handles the career management flow for the active SimCharacter.
     */
    private void careerFlow() {
        printHeader("CAREER MANAGEMENT");

        if (!ensureHasActiveSim()) return;

        // Check if sim is alive
        SimCharacter sim = engine.getActiveSim();
        if (sim != null && !sim.isAlive()) {
            System.out.println();
            printError(sim.getName() + " is deceased and cannot manage careers.");
            pause();
            return;
        }

        boolean stay = true;
        while (stay) {
            System.out.println("\n" + BOLD + "Active Sim: " + getActiveSimLabel() + RESET);
            System.out.println(CYAN + "Career: " + getSimCareer() + RESET);
            System.out.println();
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

    /**
     * Handles the assignment of a career to the active SimCharacter.
     */
    private void assignCareerFlow() {
        printHeader("ASSIGN CAREER");

        List<String> options = engine.getCareerOptions();
        if (options == null || options.isEmpty()) {
            System.out.println(RED + "No career options available." + RESET);
            pause();
            return;
        }

        System.out.println("Choose a job type:");
        for (int i = 0; i < options.size(); i++) {
            System.out.println(BLUE + (i + 1) + ") " + options.get(i) + RESET);
        }
        System.out.println("0) Back");

        int choice = readInt("Choose a job type: ", 0, options.size());
        if (choice == 0) return;

        String customTitle = readJobTitle("Enter the job title: ");

        String result = engine.assignCareerToActiveSim(choice, customTitle);
        System.out.println();
        printSuccess(result);
        pause();
    }

    /**
     * Reads the job title input for a new SimCharacter.
     *
     * @return The job title as a String.
     */
    private String readJobTitle(String prompt) {
        while (true) {
            String input = readNonEmptyLine(prompt);

            if (!input.matches("[A-Za-z ]+")) {
                System.out.println("Job title must contain letters only.");
                continue;
            }

            return input.trim();
        }
    }

    // =====================================================
    // Family Flow
    // =====================================================

    /**
     * Handles the family management flow for the active SimCharacter.
     */
    private void familyFlow() {
        printHeader("FAMILY");

        if (!ensureHasActiveSim()) return;

        // Check if sim is alive
        SimCharacter sim = engine.getActiveSim();
        if (sim != null && !sim.isAlive()) {
            System.out.println();
            printError(sim.getName() + " is deceased and cannot have family interactions.");
            pause();
            return;
        }

        boolean stay = true;
        while (stay) {
            System.out.println("\n" + BOLD + "Active Sim: " + getActiveSimLabel() + RESET);
            System.out.println();
            System.out.println("1) Try for Baby");
            System.out.println("2) View Current Sim Status");
            System.out.println("3) View Family Overview");
            System.out.println("0) Back");

            int choice = readInt("Choose: ", 0, 3);
            switch (choice) {
                case 1 -> {
                    String msg = engine.tryForBabyActiveSim();
                    System.out.println();
                    if (msg != null && !msg.isBlank()) {
                        if (msg.contains("Congratulations") || msg.contains("successful")) {
                            printSuccess(msg);
                        } else if (msg.contains("insufficient") || msg.contains("do not feel")) {
                            printError(msg);
                        } else {
                            printInfo(msg);
                        }
                    }
                    pause();
                }
                case 2 -> showStatusFlow();
                case 3 -> showFamilyOverview();
                case 0 -> stay = false;
            }
        }
    }

    /**
     * Shows the family overview for the active SimCharacter.
     */
    private void showFamilyOverview() {

        printHeader("FAMILY OVERVIEW");

        SimCharacter sim = engine.getActiveSim();
        if (sim == null) return;

        System.out.println(BOLD + "Active Sim: " + sim.getName() + RESET);
        System.out.println();

        // Pregnancy status
        if (sim.getFamily().isPregnant()) {
            System.out.println(YELLOW + "🤰 Currently Pregnant" + RESET);
        } else {
            System.out.println(GRAY + "Not Pregnant" + RESET);
        }

        System.out.println();

        // Children
        var children = sim.getFamily().getChildren();

        if (children.isEmpty()) {
            System.out.println(GRAY + "No children yet." + RESET);
        } else {
            System.out.println(CYAN + BOLD + "Children:" + RESET);

            for (var child : children) {
                System.out.printf(
                    "👶 %s | Age: %d days | Gender: %s | Needs Care: %s\n",
                    child.getName(),
                    child.getAgeInDays(),
                    child.getGender(),
                    child.getCareStatus() ? "Yes" : "No"
                );
            }
        }

        System.out.println();

        // Other Sims
        System.out.println(MAGENTA + BOLD + "Other Sims in World:" + RESET);

        for (String name : engine.getSimNames()) {
            if (!name.equals(sim.getName())) {
                System.out.println("👤 " + name);
            }
        }

        System.out.println();
        pause();
    }

    // =====================================================
    // Status Flow
    // =====================================================

    /**
     * Shows the status of the active SimCharacter.
     */
    private void showStatusFlow() {
        printHeader("ACTIVE SIM STATUS");

        if (!ensureHasActiveSim()) return;

        System.out.println(engine.getActiveSimStatus());
        pause();
    }

    // =====================================================
    // UI Component Methods
    // =====================================================

    /**
     * Clears the console screen.
     */
    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Gets the text color for a given need value.
     *
     * @param value The need value (0-100).
     * @return The color code as a String.
     */
    private String getColorForNeed(int value) {
        if (value > 60) return GREEN;
        if (value > 30) return YELLOW;
        return RED;
    }

    /**
     * Gets the status label for a given need value.
     *
     * @param value The need value (0-100).
     * @return The status label as a String.
     */
    private String getNeedStatus(int value) {
        if (value >= 80) return "Excellent";
        if (value >= 60) return "Good";
        if (value >= 40) return "Okay";
        if (value >= 20) return "Low";
        return "CRITICAL!";
    }

    /**
     * Gets the mood icon for a given mood name.
     *
     * @param moodName The name of the mood.
     * @return The mood icon as a String.
     */
    private String getMoodIcon(String moodName) {
        return switch(moodName.toUpperCase()) {
            case "HAPPY" -> "😊";
            case "OKAY" -> "😐";
            case "STRESSED" -> "😰";
            case "MISERABLE" -> "😱";
            default -> "😶";
        };
    }

    /**
     * Gets the text color for a given mood.
     *
     * @param mood The name of the mood.
     * @return The text color as a String.
     */
    private String getMoodColor(String mood) {
        return switch(mood.toUpperCase()) {
            case "HAPPY" -> GREEN;
            case "OKAY" -> YELLOW;
            case "STRESSED" -> MAGENTA;
            case "MISERABLE" -> RED;
            default -> WHITE;
        };
    }

    /**
     * Prints a border for a UI section.
     *
     * @param type  The type of border (e.g., "double", "section", "bottom", "divider").
     * @param title The title to display (if any).
     */
    private void printBorder(String type, String title) {
        if (type.equals("double")) {
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            if (!title.isEmpty()) {
                System.out.printf("║  %-58s  ║\n", title);
                System.out.println("╚══════════════════════════════════════════════════════════════╝");
            }
        } else if (type.equals("section")) {
            System.out.println("┌──────────────────────────────────────────────────────────────┐");
            if (!title.isEmpty()) {
                System.out.printf("│  %-58s  │\n", title);
            }
        } else if (type.equals("bottom")) {
            System.out.println("└──────────────────────────────────────────────────────────────┘");
        } else if (type.equals("divider")) {
            System.out.println("├──────────────────────────────────────────────────────────────┤");
        }
    }

    /**
     * Formats a monetary value as a String.
     *
     * @param amount The amount to format.
     * @return The formatted String.
     */
    private String formatMoney(double amount) {
        return String.format("$%.2f", amount);
    }

    /**
     * Formats a time value as a String.
     *
     * @param hour The hour to format.
     * @return The formatted String.
     */
    private String formatTime(int hour) {
        int displayHour = (hour % 12 == 0) ? 12 : hour % 12;
        String ampm = (hour < 12) ? "AM" : "PM";
        return String.format("%d:00 %s", displayHour, ampm);
    }

    /**
     * Prints a progress bar for a given need.
     *
     * @param icon   The icon to display.
     * @param label  The label for the need.
     * @param value  The current value of the need.
     * @param max    The maximum value of the need.
     */
    private void printProgressBar(String icon, String label, int value, int max) {
        int barWidth = 20;
        int filled = (value * barWidth) / max;
        String color = getColorForNeed(value);
        String status = getNeedStatus(value);

        StringBuilder bar = new StringBuilder(color + "[");
        for (int i = 0; i < barWidth; i++) {
            bar.append(i < filled ? "█" : "─");
        }
        bar.append("]" + RESET);

        // Visual calculation: emoji (2) + " " (1) + label (8) + " " (1) + "[" (1) + bar (20) + "]" (1) + " " (1) + value (3) + "  (" (3) + status + ") " (2)
        // = 2 + 1 + 8 + 1 + 1 + 20 + 1 + 1 + 3 + 3 + status.length() + 2 = 43 + status.length()
        int lineVisual = 2 + 1 + 8 + 1 + 1 + 20 + 1 + 1 + 3 + 3 + status.length() + 2;
        int padding = Math.max(0, 61 - lineVisual);

        System.out.printf("│ %s %-8s %s %s%3d%s  (%s) %s│\n",
            icon, label, bar.toString(), color, value, RESET, status, " ".repeat(padding));
    }

    /**
     * Gets the work hours estimate for a given career.
     *
     * @param career The career to evaluate.
     * @return The estimated work hours as a String.
     */
    private String getWorkHoursEstimate(Career career) {
        String className = career.getClass().getSimpleName();
        return switch(className) {
            case "ShiftJob" -> "4-8h";
            case "FixedTimeJob" -> "8h";
            case "CommissionJob" -> "6h";
            default -> "?h";
        };
    }

    // =====================================================
    // Data Fetcher Methods
    // =====================================================

    /**
     * Gets the current money balance of the active SimCharacter.
     *
     * @return The money balance as a double.
     */
    private double getSimMoney() {
        SimCharacter sim = engine.getActiveSim();
        return sim == null ? 0.0 : sim.getWallet().getBalance();
    }

    /**
     * Gets the current career of the active SimCharacter.
     *
     * @return The career title as a String.
     */
    private String getSimCareer() {
        SimCharacter sim = engine.getActiveSim();
        if (sim == null || sim.getCareer() == null) return "Unemployed";
        return sim.getCareer().getCurrentTitle();
    }

    /**
     * Gets the current mood of the active SimCharacter.
     *
     * @return The mood name as a String.
     */
    private String getSimMood() {
        SimCharacter sim = engine.getActiveSim();
        if (sim == null) return "UNKNOWN";
        return sim.getMood().getMoodName().toString();
    }

    /**
     * Gets the current day of the simulation.
     *
     * @return The current day as an int.
     */
    private int getCurrentDay() {
        return engine.getCurrentDay();
    }

    /**
     * Gets the current hour of the simulation.
     *
     * @return The current hour as an int.
     */
    private int getCurrentHour() {
        return engine.getCurrentHour();
    }

    /**
     * Gets the current needs of the active SimCharacter.
     *
     * @return A NeedsData object containing the current needs values.
     */
    private NeedsData getSimNeeds() {
        SimCharacter sim = engine.getActiveSim();
        if (sim == null) return new NeedsData(0, 0, 0, 0, 0);

        Needs needs = sim.getNeeds();
        return new NeedsData(
            needs.getHunger(),
            needs.getEnergy(),
            needs.getHygiene(),
            needs.getSocial(),
            needs.getFun()
        );
    }

    // =====================================================
    // Helper Methods
    // =====================================================

    /**
     * Ensures there is an active SimCharacter.
     *
     * @return True if there is an active SimCharacter, false otherwise.
     */
    private boolean ensureHasActiveSim() {
        if (engine.getSimCount() == 0) {
            return false;
        }
        int idx = engine.getActiveSimIndex();
        if (idx < 0 || idx >= engine.getSimCount()) {
            // Safety: auto-select first sim
            engine.setActiveSimIndex(0);
        }
        return true;
    }

    /**
     * Gets the label of the active SimCharacter.
     *
     * @return The name of the active SimCharacter, or "(none)" if no active Sim exists.
     */
    private String getActiveSimLabel() {
        if (engine.getSimCount() == 0) return "(none)";
        int idx = engine.getActiveSimIndex();
        List<String> names = engine.getSimNames();
        if (idx < 0 || idx >= names.size()) return "(none)";
        return names.get(idx);
    }

    /**
     * Prints a header for a UI section.
     *
     * @param title The title to display.
     */
    private void printHeader(String title) {
        clearScreen();
        System.out.println("\n" + CYAN + "========================================");
        System.out.println(title);
        System.out.println("========================================" + RESET);
    }

    /**
     * Prints a success message.
     *
     * @param message The message to display.
     */
    private void printSuccess(String message) {
        System.out.printf("%s✓ %s%s\n", GREEN + BOLD, message, RESET);
    }

    /**
     * Prints an error message.
     *
     * @param message The message to display.
     */
    private void printError(String message) {
        System.out.printf("%s⚠️  %s%s\n", RED + BOLD, message, RESET);
    }

    /**
     * Prints an informational message.
     *
     * @param message The message to display.
     */
    private void printInfo(String message) {
        System.out.printf("%sℹ️  %s%s\n", CYAN, message, RESET);
    }

    /**
     * Reads an integer from the console within a specified range.
     *
     * @param prompt The prompt message to display.
     * @param min    The minimum acceptable value.
     * @param max    The maximum acceptable value.
     * @return The validated integer input.
     */
    private int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                int val = Integer.parseInt(line);
                if (val < min || val > max) {
                    System.out.println(RED + "Enter a number between " + min + " and " + max + "." + RESET);
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                System.out.println(RED + "Please enter a valid integer." + RESET);
            }
        }
    }

    /**
     * Reads a non-empty line from the console.
     *
     * @param prompt The prompt message to display.
     * @return The validated non-empty String input.
     */
    private String readNonEmptyLine(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            if (s != null && !s.trim().isEmpty()) return s.trim();
            System.out.println(RED + "Input cannot be empty." + RESET);
        }
    }

    /**
     * Pauses the console until the user presses Enter.
     */
    private void pause() {
        System.out.print("\n" + DIM + "Press Enter to continue..." + RESET);
        sc.nextLine();
    }

    /**
     * Handles any pending births for the active SimCharacter.
     */
    private void handlePendingBirths() {
        if (!engine.hasPendingNewborns()) return;

        for (var baby : engine.getPendingNewborns()) {
            printHeader("NEW BABY");
            System.out.println("A baby was born!");
            System.out.println("Gender: " + baby.getGender());

            String babyName = readNonEmptyLine("Enter a name for the baby: ");
            engine.nameNewborn(baby, babyName);

            System.out.println("Congratulations! " + babyName + " was born!");
        }

        engine.getPendingNewborns().clear();
    }
}
