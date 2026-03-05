package simsgame.career;

public abstract class CommissionJob extends Career {
    private double basePay;
    private double commissionRate;

    public CommissionJob(CareerPromotion promotion, double basePay, double commissionRate) {
        super(promotion, 0, 0);
        this.basePay = basePay;
        this.commissionRate = commissionRate;
    }
}
