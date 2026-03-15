package simsgame.career;

public class CareerPromotion {

    private String careerName;
    private String[] titles;
    private double[] payRates;
    private int[] reqs;

    public CareerPromotion(String careerName, String[] titles, double[] payRates, int[] reqs) {
        this.careerName = careerName;
        this.titles = titles;
        this.payRates = payRates;
        this.reqs = reqs;
    }

    public String getCareerName() {
        return careerName;
    }

    public String getTitle(int level) {
        if (level < 0) level = 0;
        if (level >= titles.length) level = titles.length - 1;
        return titles[level];
    }

    public double getPayRate(int level) {
        if (level < 0) level = 0;
        if (level >= payRates.length) level = payRates.length - 1;
        return payRates[level];
    }

    public int getPromotionRequirement(int level) {
        if (level < 0 || level >= reqs.length) return Integer.MAX_VALUE;
        return reqs[level];
    }
}