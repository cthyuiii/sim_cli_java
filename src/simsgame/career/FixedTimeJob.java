package simsgame.career;

import simsgame.sims.SimCharacter;

/**
 * FixedTimeJob child class of Career where the SimCharacter works a fixed number of hours, and the pay is calculated based on the promotion level's pay rate.
 */
public class FixedTimeJob extends Career {

    private int fixedHours;

    /**
     * Constructs a FixedTimeJob instance with the specified parameters.
     *
     * @param promotion The career promotion structure for this job.
     * @param fixedHours The fixed number of hours for this job.
     */
    public FixedTimeJob(CareerPromotion promotion, int fixedHours) {
        super(promotion);
        this.fixedHours = fixedHours;
    }

    // =====================================================
    // Core behaviour
    // =====================================================

    /**
     * Returns the fixed number of hours the SimCharacter will work for this job.
     *
     * @param sim The SimCharacter who is working.
     * @return The fixed number of hours worked for this job.
     */
    @Override
    public int getWorkHours(SimCharacter sim) {
        return fixedHours;
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
        return fixedHours * getPromotion().getPayRate(getLevel());
    }
}