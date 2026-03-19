package simsgame.career;

import java.util.Random;
import simsgame.sims.SimCharacter;

/**
 * CommissionJob child class of Career where the pay is based on a base salary plus a commission rate for hours worked, with a random bonus component.
 */
public class CommissionJob extends Career {

    private double basePay;
    private double commissionRate;
    private int commissionHours;
    private Random bonusPay;

    /**
     * Constructs a CommissionJob instance with the specified parameters.
     *
     * @param promotion The career promotion structure for this job.
     * @param basePay The base pay for the job.
     * @param commissionRate The commission rate for the job.
     * @param commissionHours The number of hours eligible for commission.
     */
    public CommissionJob(CareerPromotion promotion, double basePay, double commissionRate, int commissionHours) {
        super(promotion);
        this.basePay = basePay;
        this.commissionRate = commissionRate;
        this.commissionHours = commissionHours;
        this.bonusPay = new Random();
    }

    // =====================================================
    // Core behaviour
    // =====================================================

    /**
     * Returns the number of hours the SimCharacter will work for this job.
     *
     * @param sim The SimCharacter who is working.
     * @return The number of hours worked for commission.
     */
    @Override
    public int getWorkHours(SimCharacter sim) {
        return commissionHours;
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
        double bonus = bonusPay.nextDouble() * 500;
        double pay = basePay + (hours * commissionRate) + bonus;
        return Math.round(pay * 100.0) / 100.0; // round to 2 decimals
    }
}