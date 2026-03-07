package simsgame.career;
import java.util.Random;
import simsgame.sims.SimCharacter;

public class CommissionJob extends Career {
    private double basePay;
    private double commissionRate;
    private int commissionHours;
    private Random bonusPay;

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
        double bonus = bonusPay.nextDouble(500); // Random bonus up to $500);
        return basePay + (hours * commissionRate) + bonus;
    }
}
