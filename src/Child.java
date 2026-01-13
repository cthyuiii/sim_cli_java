public class Child {
    private String name;
    private int ageInDays;
    private int birthDay;
    private String ageStage;
    private boolean needsCare;
    
    // Age stage constants
    private static final int BABY_MAX = 2;
    private static final int TODDLER_MAX = 5;
    private static final int CHILD_MAX = 10;
    private static final int TEEN_MAX = 15;
    
    public Child(String name, int currentDay) {
        this.name = name;
        this.ageInDays = 0;
        this.birthDay = currentDay;
        this.needsCare = true;
        updateAgeStage();
    }
    
    // Update age based on current game day
    public void updateAge(int currentDay) {
        ageInDays = currentDay - birthDay;
        updateAgeStage();
    }
    
    private void updateAgeStage() {
        String previousStage = ageStage;
        
        if (ageInDays <= BABY_MAX) {
            ageStage = "Baby";
            needsCare = true;
        } else if (ageInDays <= TODDLER_MAX) {
            ageStage = "Toddler";
            needsCare = true;
        } else if (ageInDays <= CHILD_MAX) {
            ageStage = "Child";
            needsCare = false;
        } else if (ageInDays <= TEEN_MAX) {
            ageStage = "Teen";
            needsCare = false;
        } else {
            ageStage = "Young Adult";
            needsCare = false;
        }
        
        // Notify if age stage changed
        if (previousStage != null && !previousStage.equals(ageStage)) {
            System.out.println("\n🎂 " + name + " has grown up! They are now a " + ageStage + "!");
        }
    }
    
    public String getName() {
        return name;
    }
    
    public int getAgeInDays() {
        return ageInDays;
    }
    
    public String getAgeStage() {
        return ageStage;
    }
    
    public boolean needsCare() {
        return needsCare;
    }
    
    public String getAgeEmoji() {
        switch (ageStage) {
            case "Baby": return "👶";
            case "Toddler": return "🧒";
            case "Child": return "👦";
            case "Teen": return "🧑";
            case "Young Adult": return "👨";
            default: return "👤";
        }
    }
    
    public String getInfo() {
        StringBuilder info = new StringBuilder();
        info.append(getAgeEmoji()).append(" ");
        info.append(name).append(" - ");
        info.append(ageStage).append(" (");
        info.append(ageInDays).append(" days old)");
        
        if (needsCare) {
            info.append(" [Needs Care]");
        }
        
        // Show next milestone
        if (ageInDays <= BABY_MAX) {
            info.append(" → Toddler in ").append(BABY_MAX - ageInDays + 1).append(" days");
        } else if (ageInDays <= TODDLER_MAX) {
            info.append(" → Child in ").append(TODDLER_MAX - ageInDays + 1).append(" days");
        } else if (ageInDays <= CHILD_MAX) {
            info.append(" → Teen in ").append(CHILD_MAX - ageInDays + 1).append(" days");
        } else if (ageInDays <= TEEN_MAX) {
            info.append(" → Young Adult in ").append(TEEN_MAX - ageInDays + 1).append(" days");
        }
        
        return info.toString();
    }
    
    public boolean isYoungAdult() {
        return ageStage.equals("Young Adult");
    }
}
