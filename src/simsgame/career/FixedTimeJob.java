package simsgame.career;

public abstract class FixedTimeJob extends Career {
    private int fixedHours;

    public FixedTimeJob(CareerPromotion promotion, int fixedHours) {
        super(promotion, 0, 0);
        this.fixedHours = fixedHours;
    }
    
}
