package simsgame.sims;
public class Wallet {
    private double balance;

    public Wallet(double amount){
        this.balance = amount;
    }

    public double getBalance(){
        return balance;
    }

    public void earn(double amount){
        if (amount<0){
            System.out.println("Unable to earn negative amounts!");
        } else{
            balance += amount;
        }
    }

    public boolean spend(double amount){
        if (amount>balance){
            System.out.println("Insufficient funds to spend!");
            return false;
        } else if (amount<0){
            System.out.println("Unable to spend negative amounts!");
            return false;
        } else{
            balance -= amount;
            return true;
        }
    }
}
