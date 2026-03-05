package simsgame.sims;
public class Wallet {
    private double balance;

    public Wallet(double amount){
        this.balance = amount;
    }

    public double getBalance(){
        return this.balance;
    }

    public void earn(double amount){
        if (amount<0){
            System.out.println("Unable to earn negative amounts!");
        } else{
            this.balance += amount;
        }
    }

    public void spend(double amount){
        if (amount>this.balance){
            System.out.println("Insufficient funds to spend!");
        } else if (amount<0){
            System.out.println("Unable to spend negative amounts!");
        } else{
            this.balance -= amount;
        }
    }
}
