package simsgame.career;

import simsgame.sims.SimCharacter;

public class ShiftJob extends Career {

    public ShiftJob(CareerPromotion promotion) {
        super(promotion);
    }

    @Override
    public int getWorkHours(SimCharacter sim) {
        return 4; // hours decided by WorkActivity
    }

    @Override
    protected double calculatePay(SimCharacter sim, int hours) {
        return hours * getPromotion().getPayRate(getLevel());
    }
}