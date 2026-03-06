package simsgame.career;
import simsgame.engine.GameContext;
import simsgame.sims.SimCharacter;

public abstract class Career {
    private CareerPromotion promotion;
    private int level;
    private int workExperience;

    public Career(CareerPromotion promotion, int level, int workExperience) {
        this.promotion = promotion;
        this.level = level;
        this.workExperience = workExperience;
    }

    public double work(SimCharacter sim, GameContext context) {
        int hours = getWorkHours(sim);
        workExperience += hours;
        double pay = calculatePay(sim, hours);
        return pay;
    };

    protected abstract int getWorkHours(SimCharacter sim);
    protected abstract double calculatePay(SimCharacter sim, int hours);

    public int getLevel() { return level; }
    public String getCurrentTitle(int level) { return promotion.getTitle(level); }
    public String getCareerName() { return promotion.getCareerName(); }
    public double getCurrentPayRate(int level) { return promotion.getPayRate(level); }
    public int getCurrentPromotionRequirement(int level) { return promotion.getPromotionRequirement(level); }

    // public void checkPromotion() {
    //     if (workExperience >= getCurrentPromotionRequirement(level)) {
    //         level += 1;
    //     }
    // }
}

