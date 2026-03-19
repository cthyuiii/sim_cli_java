package simsgame.career;

import simsgame.engine.GameContext;
import simsgame.sims.SimCharacter;

/**
 * Career is an abstract class representing a Sim's profession.
 * It manages, Career level and promotion, Work experience and Pay calculation
 * 
 * Specific careers (e.g., Doctor, Artist) will extend this class and implement the abstract methods.
 */
public abstract class Career {

    private CareerPromotion promotion;
    private int level;
    private int workExperience;

    public Career(CareerPromotion promotion) {
        this.promotion = promotion;
        this.level = 0;
        this.workExperience = 0;
    }

    // =====================================================
    // Core behaviour
    // =====================================================

    /**
     * Simulates a work session for the SimCharacter.
     * 
     * @param sim The SimCharacter who is working.
     * @param context The game state for any necessary interactions.
     * @param hours The number of hours the Sim will work.
     * 
     * @return The amount of money earned from this work session.
     */
    public double work(SimCharacter sim, GameContext context, int hours) {

        workExperience += 10;
        checkPromotion();

        return calculatePay(sim, hours);
    }

    /**
     * Checks if the SimCharacter meets the promotion requirements and promotes them if eligible.
     */
    protected void checkPromotion() {
        int currentReq = promotion.getPromotionRequirement(level);

        if (workExperience >= currentReq && level < 2) {
            level++;
            workExperience = 0;
            System.out.println("Congratulations! You've been promoted to " + promotion.getTitle(level));
        }
    }

    // =====================================================
    // Getters
    // =====================================================
    public abstract int getWorkHours(SimCharacter sim);
    protected abstract double calculatePay(SimCharacter sim, int hours);

    protected CareerPromotion getPromotion() {
        return promotion;
    }

    public int getLevel() {
        return level;
    }

    public int getWorkExperience() {
        return workExperience;
    }

    public String getCareerName() {
        return promotion.getCareerName();
    }

    public String getCurrentTitle() {
        return promotion.getTitle(level);
    }

    public double getCurrentPayRate() {
        return promotion.getPayRate(level);
    }

    
}