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
        double pay = calculatePay(sim, hours);
        return pay;
    };

    protected abstract int getWorkHours(SimCharacter sim);
    protected abstract double calculatePay(SimCharacter sim, int hours);

    private void checkPromotion() {

    }

    public int getLevel() { return level; }
    public String getCurrentTitle(int level) { return promotion.getTitle(getLevel()); }
    public String getCareerName() { return promotion.getCareerName(); }
    public int getPromotionRequirement() { return promotion.getPromotionRequirement(getLevel()); }
    private void changeCareer() {}
}

