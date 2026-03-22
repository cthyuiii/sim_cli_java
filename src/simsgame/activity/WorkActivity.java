package simsgame.activity;

import java.util.Scanner;
import simsgame.career.Career;
import simsgame.career.ShiftJob;
import simsgame.engine.GameContext;
import simsgame.sims.SimCharacter;

/**
 * Work Activity.
 * Sim earns money and gains work experience.
 * Assigned career calculates pay and advance game time based on employment type.
 */
public class WorkActivity implements Activity {

    /** Scanner to capture user input for flexible work hours for ShiftJobs. */
    private Scanner scanner = new Scanner(System.in);

    // =====================================================
    // Getters
    // =====================================================
    @Override
    public String name() { return "Work"; }
    @Override
    public int hours() { return 0; } // handled manually here

    // =====================================================
    // Core behaviour
    // =====================================================
    /**
     * Executes work action.
     * If the Sim has a ShiftJob, it prompts for manual hour input. 
     * For other careers, retrieves predefined hours. 
     * Processes the work session through Career object, updates the Sim's wallet, and advances time.
     * @param sim Sim that is working.
     * @param context Printing notifications and advancing time.
     */
    @Override
    public void apply(SimCharacter sim, GameContext context) {
        Career career = sim.getCareer();

        if (career == null) {
            context.notify(sim.getName() + " has no career yet. Assign a job first.");
            return;
        }

        int hoursWorked;

        if (career instanceof ShiftJob) {
            System.out.print("Enter number of hours to work: ");
            String input = "";
            try {
                if (scanner.hasNextLine()) {
                    input = scanner.nextLine();
                    hoursWorked = Integer.parseInt(input);
                } else {
                    hoursWorked = 4;
                }
            } catch (NumberFormatException e) {
                hoursWorked = 4;
            }

            if (hoursWorked <= 0) {
                hoursWorked = 1;
            }
        } else {
            hoursWorked = career.getWorkHours(sim);
        }

        double pay = career.work(sim, context, hoursWorked);

        if (pay > 0) {
            sim.getWallet().earn(pay);
        }

        sim.assignBusyTime(hoursWorked);

        context.notify(sim.getName() + " started working for " + hoursWorked +
            " hour(s) as a " + career.getCareerName() +
            " and will earn $" + Math.round(pay) + ".");
    }
}