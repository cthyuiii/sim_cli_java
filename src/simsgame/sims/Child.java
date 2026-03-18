package simsgame.sims;

public class Child {
    private String name;
    private int ageInDays;
    private String gender;
    private int birthDay;
    private boolean needCare;

    public String getName() {
        return name;
    }

    public int getAgeInDays() {
        return ageInDays;
    }

    public String getGender() {
        return gender;
    }

    public int getBirthDay() {
        return birthDay;
    }

    public Child(String name, String gender, int currentDay) {
        this.name = name;
        this.ageInDays = 0;
        this.gender = gender;
        this.birthDay = currentDay;
        this.needCare = true;
    }

    public boolean getCareStatus() { return needCare; }
    public void updateCareStatus(boolean care) {
        needCare = care;
    }

    public String getInfo() {
        return "Name: " + name + 
            ", Age: " + ageInDays + " days" +
            ", Gender: " + gender + 
            ", Birthday: Day " + birthDay + 
            ", Needs Care: " + needCare;
    }

    public void updateAge(int currentDay) {
        ageInDays = currentDay - birthDay;
        
        if (ageInDays >= 7) {
            needCare = false;
        }
    }

    public boolean isGrownUp() {
        return ageInDays >= 14;
    }
}
