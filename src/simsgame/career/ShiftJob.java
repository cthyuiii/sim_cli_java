package simsgame.career;

import simsgame.sims.SimCharacter;

/**
 * ShiftJob child class of Career where the SimCharacter works a variable number of hours (4 hours) and the pay is calculated based on the promotion level's pay rate.
 */
public class ShiftJob extends Career {

    /**
     * Constructs a ShiftJob instance with the specified parameters.
     *
     * @param promotion The career promotion structure for this job.
     */
    public ShiftJob(CareerPromotion promotion) {
        super(promotion);
    }

    // =====================================================
    // Core behaviour
    // =====================================================
    
    /**
     * Returns the number of hours the SimCharacter will work for this job.
     *
     * @param sim The SimCharacter who is working.
     * @return The number of hours worked for this job.
     */
    @Override
    public int getWorkHours(SimCharacter sim) {
        return 4; // hours decided by WorkActivity
    }

    /**
     * Calculates the pay for the SimCharacter based on hours worked.
     *
     * @param sim The SimCharacter who is working.
     * @param hours The number of hours worked.
     * @return The calculated pay for the work session.
     */
    @Override
    protected double calculatePay(SimCharacter sim, int hours) {
        return hours * getPromotion().getPayRate(getLevel());
    }
}