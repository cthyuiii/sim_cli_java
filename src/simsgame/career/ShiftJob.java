package simsgame.career;

import simsgame.sims.SimCharacter;

public class ShiftJob extends Career {
    private int[] shiftOptions;

    public ShiftJob(CareerPromotion promotion, int[] shiftOptions) {
        super(promotion, 0, 0);
        this.shiftOptions = shiftOptions;
    }

    @Override
    protected int getWorkHours(SimCharacter sim) {
        int shiftAllocate = (int) Math.random() * shiftOptions.length;

        return shiftOptions[shiftAllocate];
    }

    @Override
    protected double calculatePay(SimCharacter sim, int hours) {
        return Math.max(3.0, hours) * getPromotion().getPayRate(getLevel());
    }
}
