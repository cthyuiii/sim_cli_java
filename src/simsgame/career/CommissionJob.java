package simsgame.career;
import simsgame.sims.SimCharacter;

public class CommissionJob extends Career {
    private double basePay;
    private double commissionRate;
    private int commissionHours;

    public CommissionJob(CareerPromotion promotion, double basePay, double commissionRate, int commissionHours) {
        super(promotion, 0, 0);
        this.basePay = basePay;
        this.commissionRate = commissionRate;
        this.commissionHours = commissionHours;
    }

    @Override
    protected int getWorkHours(SimCharacter sim) {
        return commissionHours;
    }

    @Override
    protected double calculatePay(SimCharacter sim, int hours) {
        return basePay + (hours * commissionRate);
    }
}
