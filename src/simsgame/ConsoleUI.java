// ConsoleUI.java
package simsgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import simsgame.engine.GameEngine;
import simsgame.sims.SimCharacter;
import simsgame.sims.Needs;
import simsgame.career.Career;

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

    // =====================================================
    // ANSI Color Constants
    // =====================================================

    public static final String RESET = "\033[0m";
    public static final String BOLD = "\033[1m";
    public static final String DIM = "\033[2m";

    // Foreground colors
    public static final String GREEN = "\033[92m";
    public static final String YELLOW = "\033[93m";
    public static final String RED = "\033[91m";
    public static final String CYAN = "\033[96m";
    public static final String MAGENTA = "\033[95m";
    public static final String BLUE = "\033[94m";
    public static final String WHITE = "\033[97m";
    public static final String GRAY = "\033[90m";

    // Background colors
    public static final String BG_RED = "\033[101m";
    public static final String BG_GREEN = "\033[102m";

    // =====================================================
    // Activity Metadata (Hardcoded from ActivityRegistry)
    // =====================================================

    private static class ActivityInfo {
        String icon, name, effects;
        int hours, cost;
        ActivityInfo(String icon, String name, int hours, int cost, String effects) {
            this.icon = icon; this.name = name; this.hours = hours;
            this.cost = cost; this.effects = effects;
        }
    }

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

    public ConsoleUI(GameEngine engine) {
        this.engine = engine;
        this.sc = new Scanner(System.in);
    }

    // =====================================================
    // Main Loop
    // =====================================================

    public void run() {
        engine.startNewGame();

        boolean running = true;
        while (running) {
            printDashboard();

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

        System.out.println("\n" + CYAN + "Goodbye! Thanks for playing! 🎮" + RESET);
        sc.close();
    }

    // =====================================================
    // Dashboard View
    // =====================================================

    private void printDashboard() {
        clearScreen();
        printBorder("double", "🏠  THE SIMS - TEXT EDITION  🏠");
        System.out.println();
        printStatusCard();
        System.out.println();
        printQuickActions();
        System.out.println();
        printWarnings();
        printFooterMenu();
    }

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
        int nameLen = name.length();
        int moneyStr = formatMoney(money).length();
        int padding = Math.max(0, 36 - nameLen - moneyStr);
        System.out.printf("│ Name: %s%-20s%s  💰 Money: %s%s%s%s│\n",
            BOLD, name, RESET,
            BLUE + BOLD, formatMoney(money), RESET,
            " ".repeat(padding));

        // Career and Time
        int careerLen = career.length();
        String timeStr = formatTime(hour);
        int timePadding = Math.max(0, 24 - careerLen);
        System.out.printf("│ Career: %-25s 📅 Day %s%d%s, Hour %s%d%s (%s)%s│\n",
            career,
            CYAN, day, RESET,
            CYAN, hour, RESET,
            timeStr,
            " ".repeat(timePadding > 0 ? 1 : 0));

        // Mood
        String moodIcon = getMoodIcon(mood);
        String moodColor = getMoodColor(mood);
        int moodPadding = Math.max(0, 49 - mood.length());
        System.out.printf("│ Mood: %s %s%s%s%s│\n",
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

            String suffix = "";
            if (!affordable) suffix = GRAY + DIM + " (Can't afford)" + RESET;
            else if (!enabled) suffix = GRAY + DIM + " (No career)" + RESET;

            // Build the action line
            String actionLine = String.format("%d) %s %s (%s) - %s → %s%s",
                i + 1, info.icon, info.name, hoursStr, costStr, info.effects, suffix);

            // Calculate padding to reach 60 chars (accounting for emojis being 2 chars wide)
            int visualLen = actionLine.length() - countAnsiCodes(suffix);
            int padding = Math.max(0, 58 - visualLen);

            System.out.printf("│ %s%s%s%s%s│\n",
                dim, color, actionLine, " ".repeat(padding), RESET);
        }

        printBorder("bottom", "");
    }

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

    private void printFooterMenu() {
        System.out.println();
        System.out.println("7) 💼 Career Management    8) 👶 Family Options    9) 👤 Manage Sims");
        System.out.println("0) 🚪 Quit Game");
        System.out.println();
    }

    // =====================================================
    // Quick Action Handler
    // =====================================================

    private void handleQuickAction(int choice) {
        if (choice < 1 || choice > 6) return;
        if (!ensureHasActiveSim()) return;

        int activityIndex = choice - 1;
        ActivityInfo info = ACTIVITY_METADATA[activityIndex];

        // Check affordability
        if (info.cost > 0 && getSimMoney() < info.cost) {
            System.out.println();
            System.out.printf("%s⚠️  You can't afford %s (costs %s)%s\n",
                RED + BOLD, info.name, formatMoney(info.cost), RESET);
            pause();
            return;
        }

        // Special handling for Work
        if (choice == 6) {
            SimCharacter sim = engine.getActiveSim();
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

        // Show result
        System.out.println();
        printBorder("section", "ACTION RESULT");
        int resultLen = result.length();
        int resultPadding = Math.max(0, 56 - resultLen);
        System.out.printf("│ %s%s%s%s│\n",
            CYAN + BOLD, result, RESET + " ".repeat(resultPadding), RESET);
        printBorder("bottom", "");

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
                System.out.printf("%d) %s%s\n", i + 1, names.get(i), marker);
            }

            System.out.println();
            System.out.println((names.size() + 1) + ") Create New Sim");
            System.out.println("0) Back to Dashboard");
            System.out.println();

            int choice = readInt("Choose: ", 0, names.size() + 1);

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

    private void createSimFlow() {
        printHeader("CREATE SIM");
        String name = readNonEmptyLine("Enter Sim name: ");
        engine.createSim(name);

        // If this is the first sim, auto-select it.
        if (engine.getSimCount() == 1) {
            engine.setActiveSimIndex(0);
        }

        System.out.println();
        printSuccess("Created Sim: " + name);
        pause();
    }

    // =====================================================
    // Career Flow (Enhanced)
    // =====================================================

    private void careerFlow() {
        printHeader("CAREER MANAGEMENT");

        if (!ensureHasActiveSim()) return;

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

        String customTitle = readNonEmptyLine("Enter the job title: ");

        String result = engine.assignCareerToActiveSim(choice, customTitle);
        System.out.println();
        printSuccess(result);
        pause();
    }

    // =====================================================
    // Family Flow (Enhanced)
    // =====================================================

    private void familyFlow() {
        printHeader("FAMILY");

        if (!ensureHasActiveSim()) return;

        boolean stay = true;
        while (stay) {
            System.out.println("\n" + BOLD + "Active Sim: " + getActiveSimLabel() + RESET);
            System.out.println();
            System.out.println("1) Try for Baby");
            System.out.println("2) View Status");
            System.out.println("0) Back");

            int choice = readInt("Choose: ", 0, 2);
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
                case 0 -> stay = false;
            }
        }
    }

    // =====================================================
    // Status Flow
    // =====================================================

    private void showStatusFlow() {
        printHeader("ACTIVE SIM STATUS");

        if (!ensureHasActiveSim()) return;

        System.out.println(engine.getActiveSimStatus());
        pause();
    }

    // =====================================================
    // UI Component Methods
    // =====================================================

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private String getColorForNeed(int value) {
        if (value > 60) return GREEN;
        if (value > 30) return YELLOW;
        return RED;
    }

    private String getNeedStatus(int value) {
        if (value >= 80) return "Excellent";
        if (value >= 60) return "Good";
        if (value >= 40) return "Okay";
        if (value >= 20) return "Low";
        return "CRITICAL!";
    }

    private String getMoodIcon(String moodName) {
        return switch(moodName.toUpperCase()) {
            case "HAPPY" -> "😊";
            case "OKAY" -> "😐";
            case "STRESSED" -> "😰";
            case "MISERABLE" -> "😱";
            default -> "😶";
        };
    }

    private String getMoodColor(String mood) {
        return switch(mood.toUpperCase()) {
            case "HAPPY" -> GREEN;
            case "OKAY" -> YELLOW;
            case "STRESSED" -> MAGENTA;
            case "MISERABLE" -> RED;
            default -> WHITE;
        };
    }

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

    private String formatMoney(double amount) {
        return String.format("$%.2f", amount);
    }

    private String formatTime(int hour) {
        int displayHour = (hour % 12 == 0) ? 12 : hour % 12;
        String ampm = (hour < 12) ? "AM" : "PM";
        return String.format("%d:00 %s", displayHour, ampm);
    }

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

        System.out.printf("│ %s %-8s %s %s%3d%s  %-15s │\n",
            icon, label, bar.toString(),
            color, value, RESET,
            DIM + "(" + status + ")" + RESET);
    }

    private String getWorkHoursEstimate(Career career) {
        String className = career.getClass().getSimpleName();
        return switch(className) {
            case "ShiftJob" -> "4-8h";
            case "FixedTimeJob" -> "8h";
            case "CommissionJob" -> "6h";
            default -> "?h";
        };
    }

    private int countAnsiCodes(String str) {
        int count = 0;
        int i = 0;
        while (i < str.length()) {
            if (str.charAt(i) == '\033' && i + 1 < str.length() && str.charAt(i + 1) == '[') {
                int end = str.indexOf('m', i);
                if (end != -1) {
                    count += (end - i + 1);
                    i = end + 1;
                } else {
                    i++;
                }
            } else {
                i++;
            }
        }
        return count;
    }

    // =====================================================
    // Data Fetcher Methods
    // =====================================================

    private double getSimMoney() {
        SimCharacter sim = engine.getActiveSim();
        return sim == null ? 0.0 : sim.getWallet().getBalance();
    }

    private String getSimCareer() {
        SimCharacter sim = engine.getActiveSim();
        if (sim == null || sim.getCareer() == null) return "Unemployed";
        return sim.getCareer().getCurrentTitle();
    }

    private String getSimMood() {
        SimCharacter sim = engine.getActiveSim();
        if (sim == null) return "UNKNOWN";
        return sim.getMood().getMoodName().toString();
    }

    private int getCurrentDay() {
        return engine.getCurrentDay();
    }

    private int getCurrentHour() {
        return engine.getCurrentHour();
    }

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

    private String getActiveSimLabel() {
        if (engine.getSimCount() == 0) return "(none)";
        int idx = engine.getActiveSimIndex();
        List<String> names = engine.getSimNames();
        if (idx < 0 || idx >= names.size()) return "(none)";
        return names.get(idx);
    }

    private void printHeader(String title) {
        clearScreen();
        System.out.println("\n" + CYAN + "========================================");
        System.out.println(title);
        System.out.println("========================================" + RESET);
    }

    private void printSuccess(String message) {
        System.out.printf("%s✓ %s%s\n", GREEN + BOLD, message, RESET);
    }

    private void printError(String message) {
        System.out.printf("%s⚠️  %s%s\n", RED + BOLD, message, RESET);
    }

    private void printInfo(String message) {
        System.out.printf("%sℹ️  %s%s\n", CYAN, message, RESET);
    }

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

    private String readNonEmptyLine(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            if (s != null && !s.trim().isEmpty()) return s.trim();
            System.out.println(RED + "Input cannot be empty." + RESET);
        }
    }

    private void pause() {
        System.out.print("\n" + DIM + "Press Enter to continue..." + RESET);
        sc.nextLine();
    }
}
