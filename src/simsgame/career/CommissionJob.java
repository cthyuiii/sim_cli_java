package simsgame.career;

import java.util.Random;
import simsgame.sims.SimCharacter;

public class CommissionJob extends Career {

    private double basePay;
    private double commissionRate;
    private int commissionHours;
    private Random bonusPay;

    public CommissionJob(CareerPromotion promotion, double basePay, double commissionRate, int commissionHours) {
        super(promotion);
        this.basePay = basePay;
        this.commissionRate = commissionRate;
        this.commissionHours = commissionHours;
        this.bonusPay = new Random();
    }

    @Override
    public int getWorkHours(SimCharacter sim) {
        return commissionHours;
    }

    @Override
    protected double calculatePay(SimCharacter sim, int hours) {
        double bonus = bonusPay.nextDouble() * 500;
        double pay = basePay + (hours * commissionRate) + bonus;
        return Math.round(pay * 100.0) / 100.0; // round to 2 decimals
    }
}