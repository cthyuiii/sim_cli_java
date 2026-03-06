package simsgame.career;
import simsgame.sims.SimCharacter;

public class CommissionJob extends Career {
    private double basePay;
    private double commissionRate;

    public CommissionJob(CareerPromotion promotion, double basePay, double commissionRate) {
        super(promotion, 0, 0);
        this.basePay = basePay;
        this.commissionRate = commissionRate;
    }

    @Override
    protected int getWorkHours(SimCharacter sim) {
        return 1;
    }

    @Override
    protected double calculatePay(SimCharacter sim, int hours) {
        return basePay + (hours * commissionRate);
    }
}
