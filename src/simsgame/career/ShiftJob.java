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
        return shiftOptions[0];
    }

    @Override
    protected double calculatePay(SimCharacter sim, int hours) {
        return hours * sim.getCareer().getCurrentPayRate(sim.getCareer().getLevel());
    }
}
