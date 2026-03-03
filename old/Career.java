public class Career {
    private String careerName;
    private int level;
    private int workExperience;
    private int basePay;
    private int workHours;
    private String[] levelTitles;
    private int[] levelPayRates;
    private int[] promotionRequirements;

    public Career(String careerName) {
        this.careerName = careerName;
        this.level = 1;
        this.workExperience = 0;
        initializeCareerData();
    }

    private void initializeCareerData() {
        switch (careerName.toLowerCase()) {
            case "fast food":
                levelTitles = new String[]{"Crew Member", "Shift Leader", "Store Manager"};
                levelPayRates = new int[]{40, 70, 120};
                promotionRequirements = new int[]{0, 3, 6};
                workHours = 4;
                break;
            case "office":
                levelTitles = new String[]{"Intern", "Junior Associate", "Senior Manager"};
                levelPayRates = new int[]{60, 100, 180};
                promotionRequirements = new int[]{0, 4, 8};
                workHours = 6;
                break;
            case "programmer":
                levelTitles = new String[]{"Junior Developer", "Software Engineer", "Lead Architect"};
                levelPayRates = new int[]{80, 140, 250};
                promotionRequirements = new int[]{0, 5, 10};
                workHours = 5;
                break;
            case "artist":
                levelTitles = new String[]{"Freelance Artist", "Gallery Artist", "Master Artist"};
                levelPayRates = new int[]{50, 110, 200};
                promotionRequirements = new int[]{0, 4, 9};
                workHours = 4;
                break;
            case "doctor":
                levelTitles = new String[]{"Medical Intern", "Resident Doctor", "Chief Surgeon"};
                levelPayRates = new int[]{90, 160, 300};
                promotionRequirements = new int[]{0, 6, 12};
                workHours = 8;
                break;
            default:
                levelTitles = new String[]{"Entry Level", "Mid Level", "Senior Level"};
                levelPayRates = new int[]{50, 100, 180};
                promotionRequirements = new int[]{0, 4, 8};
                workHours = 4;
        }
        basePay = levelPayRates[level - 1];
    }

    public int work() {
        workExperience++;
        checkForPromotion();
        return basePay;
    }

    private void checkForPromotion() {
        if (level < 3 && workExperience >= promotionRequirements[level]) {
            level++;
            basePay = levelPayRates[level - 1];
            System.out.println("\n🎉 PROMOTION! You are now a " + getCurrentTitle() + "!");
            System.out.println("Your new pay rate is $" + basePay + " per shift.");
        }
    }

    public String getCareerName() {
        return careerName;
    }

    public int getLevel() {
        return level;
    }

    public int getWorkExperience() {
        return workExperience;
    }

    public int getBasePay() {
        return basePay;
    }

    public int getWorkHours() {
        return workHours;
    }

    public String getCurrentTitle() {
        return levelTitles[level - 1];
    }

    public int getNextPromotionRequirement() {
        if (level < 3) {
            return promotionRequirements[level];
        }
        return -1; // Max level reached
    }

    public String getCareerInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Career: ").append(careerName).append("\n");
        info.append("Title: ").append(getCurrentTitle()).append(" (Level ").append(level).append("/3)\n");
        info.append("Pay: $").append(basePay).append(" per shift (").append(workHours).append(" hours)\n");
        info.append("Experience: ").append(workExperience).append(" shifts");
        if (level < 3) {
            info.append("\nNext Promotion: ").append(getNextPromotionRequirement()).append(" shifts");
        } else {
            info.append("\n⭐ MAX LEVEL ACHIEVED!");
        }
        return info.toString();
    }
}
