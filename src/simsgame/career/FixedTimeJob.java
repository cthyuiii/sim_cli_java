package simsgame.career;

import simsgame.sims.SimCharacter;

public class FixedTimeJob extends Career {
    private int fixedHours;

    public FixedTimeJob(CareerPromotion promotion, int fixedHours) {
        super(promotion, 0, 0);
        this.fixedHours = fixedHours;
    }

    @Override
    protected int getWorkHours(SimCharacter sim) {
        return fixedHours;
    }

    @Override
    protected double calculatePay(SimCharacter sim, int hours) {
        return fixedHours * getPromotion().getPayRate(getLevel());
    }
}
