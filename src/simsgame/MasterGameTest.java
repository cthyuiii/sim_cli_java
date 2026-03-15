package simsgame;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import simsgame.sims.*;
import simsgame.engine.*;
import simsgame.career.*;
import simsgame.activity.*;

public class MasterGameTest {

    private GameEngine engine;
    private SimCharacter sim;
    private Wallet wallet;
    private Needs needs;
    private Mood mood;
    private Time time;

    @Before
    public void setUp() {
        engine = new GameEngine();
        engine.createSim("TestSim");
        sim = engine.getSimNames().size() > 0 ? new SimCharacter("TestSim") : null;
        wallet = sim.getWallet();
        needs = sim.getNeeds();
        mood = sim.getMood();
        time = new Time();
    }

    // --- WALLET TESTS ---
    @Test
    public void testWalletTransactions() {
        wallet.earn(50.0);
        assertEquals(150.0, wallet.getBalance(), 0.001);

        assertTrue("Spend should return true", wallet.spend(100.0));
        assertFalse("Should not spend more than balance", wallet.spend(1000.0));
    }

    @Test
    public void testEarnNegativeAmount() {
        double initialBalance = wallet.getBalance();
        wallet.earn(-50.0);
        assertEquals("Balance should remain unchanged when earning negative", initialBalance, wallet.getBalance(), 0.001);
    }

    @Test
    public void testSpendNegativeAmount() {
        double initialBalance = wallet.getBalance();
        boolean result = wallet.spend(-20.0);
        assertFalse("Spending negative should return false", result);
        assertEquals("Balance should not change when spending negative", initialBalance, wallet.getBalance(), 0.001);
    }

    // --- NEEDS & MOOD TESTS ---
    @Test
    public void testNeedsDecay() {
        int initialHunger = needs.getHunger();
        needs.decay(2); // Hunger decays at 3 per hour
        assertEquals("Hunger should decay by 6 over 2 hours", initialHunger - 6, needs.getHunger());
    }

    @Test
    public void testMoodTransitions() {
        // Default average is 80, so mood should be HAPPY
        mood.update(needs);
        assertEquals(Mood.moodState.HAPPY, mood.getMoodName());

        // Decay needs to a level where mood becomes STRESSED (avg < 40)
        needs.decay(20); 
        mood.update(needs);
        assertEquals("", Mood.moodState.STRESSED, mood.getMoodName());
    }

    // --- TIME TESTS ---
    @Test
    public void testTimeAdvancement() {
        time.advance(5);
        assertEquals(13, time.getCurrentHour());
        
        time.advance(20); // Moves into Day 2
        assertEquals(2, time.getCurrentDay());
        assertEquals(9, time.getCurrentHour());
    }

    // --- ACTIVITY & INTERACTION TESTS ---

    @Test
    public void testEatActivityImpact() {
        // Set hunger low first
        needs.decay(20); // Hunger drops to 20 (80 - 60)
        int hungerBefore = needs.getHunger();
        
        // Perform EatActivity ($10 cost, +25 hunger)
        EatActivity eat = new EatActivity(10, 25, 1);
        eat.apply(sim, engine);
        
        assertEquals("Hunger should increase by 25", hungerBefore + 25, needs.getHunger());
        assertEquals("Wallet should decrease by $10", 90.0, wallet.getBalance(), 0.001);
    }

    @Test
    public void testBrokeEatingFallback() {
        wallet.spend(100.0); // Make sim broke
        needs.decay(20);
        
        EatActivity eat = new EatActivity(10, 30, 1);
        eat.apply(sim, engine);
        
        // If broke, EatActivity gives a smallGain: hungerGain / 3 (30/3 = 10)
        assertTrue("Should still gain some hunger from cheap snack", needs.getHunger() > 20);
        assertEquals("Wallet should stay at 0", 0.0, wallet.getBalance(), 0.001);
    }

    // --- CAREER SYSTEM TESTS ---
    // @Test
    // public void testCareerFactoryAndAssignment() {
    //     // CareerFactory is currently empty in your file
    //     // Assuming implementation of CareerFactory.createCareer(0) returns a Career
    //     Career medicalCareer = CareerFactory.createCareer(0);
        
    //     if (medicalCareer != null) {
    //         sim.setCareer(medicalCareer);
    //         assertNotNull("Sim should have a career assigned", sim.getCareer());
    //         assertEquals(medicalCareer.getCareerName(), sim.getCareer().getCareerName());
    //     }
    // }

    @Test
    public void testPromotionLogic() {
        // This test assumes you have a CareerPromotion setup for a job
        CareerPromotion promo = new CareerPromotion("Test Job", 
            new String[]{"Junior", "Senior"}, 
            new double[]{10.0, 20.0}, 
            new int[]{10, 20});
        
        FixedTimeJob job = new FixedTimeJob(promo, 8);
        
        // Sim starts at Level 0. Work once to gain experience
        job.work(sim, new GameEngine()); // WorkExperience += 10
        job.checkPromotion(); // Reaches requirement of 10
        
        assertEquals("Sim should be promoted to Level 1", 1, job.getLevel());
        assertEquals("Title should be Senior", "Senior", job.getCurrentTitle(1));
    }

    // --- FAMILY SYSTEM TESTS ---
    @Test
    public void testChildAging() {
        Child baby = new Child("Junior", "Male", 1);
        assertEquals(0, baby.getAgeInDays());
        assertTrue("Newborn should need care", baby.getCareStatus());

        // Update age to Day 8
        baby.updateAge(8);
        assertEquals(7, baby.getAgeInDays());
        assertFalse("7-day old child should no longer need care", baby.getCareStatus());
    }

    @Test
    public void testPregnancyLogic() {
        Family family = sim.getFamily();
        // Since tryForBaby uses Math.random(), we test if the state can change
        String result = family.tryForBaby(sim.getName(), 1);
        
        if (result.contains("pregnant")) {
            assertTrue("Family should be in pregnant state", family.isPregnant());
        }
    }
}