package simsgame.sims;

/**
 * Manages financial balance of Sim.
 * Track, increase, and decrease a Sim's available funds 
 * Preventing invalid transactions such as negative 
 * amounts or spending beyond the current balance.
 */
public class Wallet {
    private double balance;

    /**
     * Constructs a new Wallet with an initial balance.
     * @param amount The starting amount of money for the Sim.
     */
    public Wallet(double amount){
        this.balance = amount;
    }

    public double getBalance(){
        return balance;
    }

    /**
     * Adds money to wallet balance.
     * Includes safety check to ensure only positive amounts earned.
     * @param amount Amount of money to add.
     */
    public void earn(double amount){
        if (amount<0){
            System.out.println("Unable to earn negative amounts!");
        } else{
            balance += amount;
        }
    }

    /**
     * Deducts money from balance if sufficient.
     * Checks for both negative input and whether the Sim has 
     * enough money to cover the cost.
     * @param amount Amount of money to spend.
     * @return true iftransaction was successful; false if funds were 
     * insufficient or amount was negative.
     */
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
