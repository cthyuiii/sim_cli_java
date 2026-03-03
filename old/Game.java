import java.util.Scanner;

public class Game {
    private SimCharacter character;
    private Scanner scanner;
    private boolean running;

    public Game() {
        scanner = new Scanner(System.in);
        running = true;
    }

    public void start() {
        displayWelcome();
        createCharacter();
        selectCareer();
        gameLoop();
        displayGameOver();
    }

    private void displayWelcome() {
        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║                                        ║");
        System.out.println("║        TEXT-BASED SIMS GAME            ║");
        System.out.println("║       Family & Career Edition          ║");
        System.out.println("║                                        ║");
        System.out.println("║   Manage your Sim's life and family!   ║");
        System.out.println("║                                        ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("\n");
    }

    private void createCharacter() {
        System.out.print("Enter your Sim's name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            name = "Sim";
        }
        character = new SimCharacter(name);
        System.out.println("\nWelcome, " + name + "! Your new life begins now.");
        System.out.println("Keep your needs balanced, build a career, and start a family!");
    }

    private void selectCareer() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║          CHOOSE YOUR CAREER            ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║  1. Fast Food Worker                   ║");
        System.out.println("║     • Starting Pay: $40/shift (4h)     ║");
        System.out.println("║     • Easy work, quick money           ║");
        System.out.println("║                                        ║");
        System.out.println("║  2. Office Worker                      ║");
        System.out.println("║     • Starting Pay: $60/shift (6h)     ║");
        System.out.println("║     • Balanced career path             ║");
        System.out.println("║                                        ║");
        System.out.println("║  3. Programmer                         ║");
        System.out.println("║     • Starting Pay: $80/shift (5h)     ║");
        System.out.println("║     • High pay, good hours             ║");
        System.out.println("║                                        ║");
        System.out.println("║  4. Artist                             ║");
        System.out.println("║     • Starting Pay: $50/shift (4h)     ║");
        System.out.println("║     • Creative and flexible            ║");
        System.out.println("║                                        ║");
        System.out.println("║  5. Doctor                             ║");
        System.out.println("║     • Starting Pay: $90/shift (8h)     ║");
        System.out.println("║     • Highest pay, longest hours       ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.print("\nChoose your career (1-5): ");
        
        String choice = scanner.nextLine().trim();
        Career career = null;
        
        switch (choice) {
            case "1":
                career = new Career("Fast Food");
                break;
            case "2":
                career = new Career("Office");
                break;
            case "3":
                career = new Career("Programmer");
                break;
            case "4":
                career = new Career("Artist");
                break;
            case "5":
                career = new Career("Doctor");
                break;
            default:
                System.out.println("Invalid choice. Defaulting to Fast Food Worker.");
                career = new Career("Fast Food");
        }
        
        character.setCareer(career);
        System.out.println("\nYou are now a " + career.getCurrentTitle() + "!");
        System.out.println("Work hard to get promotions and earn more money.");
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private void gameLoop() {
        while (running && character.isAlive()) {
            character.displayStatus();
            displayMenu();
            handleInput();
        }
    }

    private void displayMenu() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║              ACTIONS                   ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║  1. Eat                                ║");
        System.out.println("║  2. Sleep                              ║");
        System.out.println("║  3. Shower                             ║");
        System.out.println("║  4. Socialize                          ║");
        System.out.println("║  5. Play Video Games                   ║");
        System.out.println("║  6. Watch TV                           ║");
        System.out.println("║  7. Work (Earn Money)                  ║");
        System.out.println("║  8. Exercise                           ║");
        System.out.println("║  9. Change Career                      ║");
        System.out.println("║  10. Try for Baby                      ║");
        System.out.println("║  11. Care for Children                 ║");
        System.out.println("║  12. View Family                       ║");
        System.out.println("║  13. View Status                       ║");
        System.out.println("║  0. Quit Game                          ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.print("\nChoose an action: ");
    }

    private void handleInput() {
        String input = scanner.nextLine().trim();

        switch (input) {
            case "1":
                handleEat();
                break;
            case "2":
                handleSleep();
                break;
            case "3":
                character.shower();
                break;
            case "4":
                character.socialize();
                break;
            case "5":
                character.playGame();
                break;
            case "6":
                character.watchTV();
                break;
            case "7":
                character.work();
                break;
            case "8":
                character.exercise();
                break;
            case "9":
                handleChangeCareer();
                break;
            case "10":
                character.tryForBaby();
                break;
            case "11":
                character.careForChildren();
                break;
            case "12":
                character.viewFamily();
                break;
            case "13":
                // Status will be displayed at the start of next loop
                break;
            case "0":
                System.out.println("\nThanks for playing! Goodbye!");
                running = false;
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }

        if (running && character.isAlive() && !input.equals("13") && !input.equals("12")) {
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    private void handleEat() {
        System.out.println("\n--- EATING OPTIONS ---");
        System.out.println("1. Snack ($5, +15 hunger, 1 hour)");
        System.out.println("2. Meal ($15, +40 hunger, 2 hours)");
        System.out.println("3. Feast ($30, +70 hunger, +15 fun, +10 social, 3 hours)");
        System.out.print("Choose what to eat: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                character.eat("snack");
                break;
            case "2":
                character.eat("meal");
                break;
            case "3":
                character.eat("feast");
                break;
            default:
                System.out.println("Invalid choice. Action cancelled.");
                break;
        }
    }

    private void handleSleep() {
        System.out.print("\nHow many hours do you want to sleep? (1-12): ");
        try {
            String input = scanner.nextLine().trim();
            int hours = Integer.parseInt(input);
            character.sleep(hours);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }

    private void handleChangeCareer() {
        System.out.println("\n⚠ Changing careers will reset your career progress!");
        System.out.print("Are you sure? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (confirm.equals("yes") || confirm.equals("y")) {
            System.out.println("\nYou quit your job as a " + character.getCareer().getCurrentTitle() + ".");
            selectCareer();
        } else {
            System.out.println("Career change cancelled.");
        }
    }

    private void displayGameOver() {
        if (!character.isAlive()) {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║            GAME OVER                   ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("\nYour Sim survived for " + character.getCurrentDay() + " days.");
            System.out.println("Final money: $" + character.getMoney());
            if (character.hasCareer()) {
                System.out.println("Final career: " + character.getCareer().getCurrentTitle());
                System.out.println("Career level: " + character.getCareer().getLevel() + "/3");
            }
            if (character.getChildCount() > 0) {
                System.out.println("Children: " + character.getChildCount());
                System.out.println("\nYour legacy lives on through your children!");
            }
            System.out.println("\nBetter luck next time!");
        }
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}
