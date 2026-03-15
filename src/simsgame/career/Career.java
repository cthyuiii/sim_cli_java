package simsgame.career;

import simsgame.engine.GameContext;
import simsgame.sims.SimCharacter;

public abstract class Career {

    private CareerPromotion promotion;
    private int level;
    private int workExperience;

    public Career(CareerPromotion promotion) {
        this.promotion = promotion;
        this.level = 0;
        this.workExperience = 0;
    }

    public double work(SimCharacter sim, GameContext context) {
        int hours = getWorkHours(sim);

        workExperience += 10;
        checkPromotion();

        return calculatePay(sim, hours);
    }

    protected abstract int getWorkHours(SimCharacter sim);
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

    protected void checkPromotion() {
        int currentReq = promotion.getPromotionRequirement(level);

        if (workExperience >= currentReq && level < 2) {
            level++;
            workExperience = 0;
            System.out.println("Congratulations! You've been promoted to " + promotion.getTitle(level));
        }
    }
}