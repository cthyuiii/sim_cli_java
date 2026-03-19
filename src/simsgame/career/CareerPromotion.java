package simsgame.career;

/**
 * CareerPromotion encapsulates the promotion structure for a career, including titles, pay rates, and promotion requirements.
 * It provides getter methods to retrieve the appropriate title, pay rate, and promotion requirement based on the Sim's current level.
 */
public class CareerPromotion {

    private String careerName;
    private String[] titles;
    private double[] payRates;
    private int[] reqs;

    /**
     * Constructs a CareerPromotion instance with the specified career name, titles, pay rates, and promotion requirements.
     *
     * @param careerName The name of the career.
     * @param titles An array of titles corresponding to each promotion level.
     * @param payRates An array of pay rates corresponding to each promotion level.
     * @param reqs An array of promotion requirements (work experience) for each level.
     */
    public CareerPromotion(String careerName, String[] titles, double[] payRates, int[] reqs) {
        this.careerName = careerName;
        this.titles = titles;
        this.payRates = payRates;
        this.reqs = reqs;
    }

    // =====================================================
    // Getters
    // =====================================================
    
    public String getCareerName() {
        return careerName;
    }

    /**
     * Retrieves the title for a specific promotion level.
     *
     * @param level The promotion level (0-based).
     * @return The title for the specified promotion level.
     */
    public String getTitle(int level) {
        if (level < 0) level = 0;
        if (level >= titles.length) level = titles.length - 1;
        return titles[level];
    }

    /**
     * Retrieves the pay rate for a specific promotion level.
     *
     * @param level The promotion level (0-based).
     * @return The pay rate for the specified promotion level.
     */
    public double getPayRate(int level) {
        if (level < 0) level = 0;
        if (level >= payRates.length) level = payRates.length - 1;
        return payRates[level];
    }

    /**
     * Retrieves the promotion requirement (work experience) for a specific promotion level.
     *
     * @param level The promotion level (0-based).
     * @return The promotion requirement for the specified promotion level.
     */
    public int getPromotionRequirement(int level) {
        if (level < 0 || level >= reqs.length) return Integer.MAX_VALUE;
        return reqs[level];
    }
}