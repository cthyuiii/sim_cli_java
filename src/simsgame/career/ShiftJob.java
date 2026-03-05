package simsgame.career;

public abstract class ShiftJob extends Career {
    private int[] shiftOptions;

    public ShiftJob(CareerPromotion promotion, int[] shiftOptions) {
        super(promotion, 0, 0);
        this.shiftOptions = shiftOptions;
    }
    
}
