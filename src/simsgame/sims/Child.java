package simsgame.sims;

public class Child {
    private String name;
    private int ageInDays;
    private String gender;
    private int birthDay;
    private boolean needCare;

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
    }

}
