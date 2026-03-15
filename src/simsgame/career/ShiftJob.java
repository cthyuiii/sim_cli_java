package simsgame.career;

import simsgame.sims.SimCharacter;

public class ShiftJob extends Career {

    private int[] shiftOptions;

    public ShiftJob(CareerPromotion promotion, int[] shiftOptions) {
        super(promotion);
        this.shiftOptions = shiftOptions;
    }

    @Override
    protected int getWorkHours(SimCharacter sim) {
        int shiftAllocate = (int) (Math.random() * shiftOptions.length);
        return shiftOptions[shiftAllocate];
    }

    @Override
    protected double calculatePay(SimCharacter sim, int hours) {
        return hours * getPromotion().getPayRate(getLevel());
    }
}