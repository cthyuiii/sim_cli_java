import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import simsgame.sims.*;
import simsgame.engine.*;
import simsgame.career.*;
import simsgame.activity.*;
import java.util.List;


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
        engine.startNewGame();
        engine.createSim("Tester", "Female");
        sim = engine.getActiveSim();
        wallet = sim.getWallet();
        needs = sim.getNeeds();
        mood = sim.getMood();
        time = new Time();
    }

    // =====================================================
    // Activity Tests
    // =====================================================
    /**
     * Tests EatActivity.java
     * Test both paid meal and free meal fallback
     */
    @Test
    public void testEatActivity() {
        needs.addHunger(-50); // Hunger at 30 (80-50)
        EatActivity eat = new EatActivity(10, 30, 1);
        
        // Paid Meal
        // Checks hunger added and wallet balance decrease
        eat.apply(sim, engine);
        assertEquals(60, needs.getHunger());
        assertEquals(90.0, wallet.getBalance(), 0.001);

        // Free Meal
        // Checks hunger added
        wallet.spend(90.0);
        eat.apply(sim, engine); 
        assertEquals(70, needs.getHunger());
    }

    /**
     * Tests PlayActivity.java
     * Instantiate two PlayActivity
     * One is Unaffordable, Other one is Affordable
     * Check if activity is carried out when
     * sim can afford it
     */
    @Test
    public void testPlayActivity() {
        // Base Values of Sim before activity
        needs.addFun(-50);
        int funBefore = needs.getFun();
        int energyBefore = needs.getEnergy();
        double walletBefore = sim.getWallet().getBalance();

        // Unaffordable Play Activity
        PlayActivity expensivePlay = new PlayActivity(25, 10, 2, 200);
        expensivePlay.apply(sim, engine);
        // Verify activity is not carried out
        assertEquals("Fun should not change when broke", funBefore, sim.getNeeds().getFun());
        assertEquals("Energy should not decrease by 10", energyBefore, needs.getEnergy());
        assertEquals("Wallet should not change when broke", walletBefore, sim.getWallet().getBalance(), 0.001);

        // Affordable Play Activity
        PlayActivity cheapPlay = new PlayActivity(25, 10, 2, 50);
        cheapPlay.apply(sim, engine);
        // Verify activity is carried out
        assertEquals("Fun should increase when paid", funBefore + 25, sim.getNeeds().getFun());
        assertEquals("Energy should decrease by 10", energyBefore - 10, needs.getEnergy());
        assertEquals("Wallet should decrease by cost", walletBefore - 50, sim.getWallet().getBalance(), 0.001);
    }

    /**
     * Tests ShowerActivity.java
     * Check if hygiene need increases
     */
    @Test
    public void testShowerActivity() {
        needs.addHygiene(-50); // Set hygiene to 30
        int hygieneBefore = needs.getHygiene();
        ShowerActivity shower = new ShowerActivity(30, 1);
        shower.apply(sim, engine);
        assertEquals("Hygiene should increase by 30", hygieneBefore + 30, needs.getHygiene());
    }

    /**
     * Tests SleepActivity.java
     * Check if energy increases and
     * hunger decreases
     */
    @Test
    public void testSleepActivity() {
        needs.addEnergy(-50); // Set energy to 30
        int energyBefore = needs.getEnergy();
        int hungerBefore = needs.getHunger();
        SleepActivity sleep = new SleepActivity(40, 8);
        sleep.apply(sim, engine);

        assertEquals("Energy should increase by 40", energyBefore + 40, needs.getEnergy());
        assertEquals("Sleeping should decrease hunger by 10", hungerBefore - 10, needs.getHunger());
    }

    /**
     * Tests SocialiseseActivity.java
     * Check if social and fun increases
     */
    @Test
    public void testSocialiseActivity() {
        needs.addSocial(-50);
        needs.addFun(-50);
        int socialBefore = needs.getSocial();
        int funBefore = needs.getFun();
        SocialiseActivity social = new SocialiseActivity(25, 10, 2);
        social.apply(sim, engine);

        assertEquals("Social should increase by 25", socialBefore + 25, needs.getSocial());
        assertEquals("Fun should increase by 10", funBefore + 10, needs.getFun());
    }

    /**
     * Tests WorkActivity.java
     * Checks for ShiftJobs as rest of jobs have predefined hours
     * Checks for valid hours, invalid input and negative inputs
     */
    @Test
    public void testWorkActivityFullLogicAndFallbacks() {
        sim.setCareer(CareerFactory.createCareer(1, "TestJob"));
        // Mock all three inputs: Success (6), Invalid (abc), and Negative (-5)
        String input = "6\nabc\n-5\n";
        java.io.InputStream originalIn = System.in;
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));
        
        try {
            // Test Valid Input (6)
            WorkActivity work1 = new WorkActivity();
            work1.apply(sim, engine);

            assertEquals("Sim should be busy for 6 hours", 6, sim.getRemainingBusyHours());
            engine.advanceUntilNextAvailableSim();
            assertEquals("Time should be 8 + 6 = 14", 14, engine.getCurrentHour());

            // Test Invalid Input (abc)
            WorkActivity work2 = new WorkActivity();
            work2.apply(sim, engine);
            assertEquals("Should default to 4h on invalid input 'abc'", 4, sim.getRemainingBusyHours());
            engine.advanceUntilNextAvailableSim();
            assertEquals("Time should be 14 + 4 = 18", 18, engine.getCurrentHour());

            // Test Negative Input (-5)
            WorkActivity work3 = new WorkActivity();
            work3.apply(sim, engine);
            assertEquals("Should default to 4h on negative input '-5'", 4, sim.getRemainingBusyHours());
            engine.advanceUntilNextAvailableSim();
            assertEquals("Time should be 18 + 4 = 19", 22, engine.getCurrentHour());

        } finally {
            // Restore StandardInput Stream after testing
            System.setIn(originalIn);
        }
    }

    // =====================================================
    // Career Tests
    // =====================================================
    /**
     * Tests checkPromotion() method inside Career.java
     * Initializes a dummy career progression where
     * 20 experience is required for level 1
     * 30 experience is required for level 2
     * Testing at all thresholds to ensure promotion is only done when
     * experience is met.
     */
    @Test
    public void testCheckPromotion() {
        CareerPromotion promo = new CareerPromotion("Test", 
            new String[]{"Level 0", "Level 1", "Level 2"}, 
            new double[]{10, 20, 30}, 
            new int[]{20, 30, Integer.MAX_VALUE});
        Career job = new FixedTimeJob(promo, 8);

        // Adding 10 workExperience but still not enough for promotion
        job.work(sim, engine, 8); 
        assertEquals("Should stay at Level 0", 0, job.getLevel());

        // Adding 10 more experience to hit the promotion requirement for Level 1
        job.work(sim, engine, 8); 
        assertEquals("Should promote to Level 1", 1, job.getLevel());

        // Adding 30 experience to hit the promotion requirement for Level 2
        job.work(sim, engine, 8);
        job.work(sim, engine, 8);
        job.work(sim, engine, 8);
        assertEquals("Should be at Max Level 2", 2, job.getLevel());
    }

    /**
     * Tests createCareer() method inside CareerFactory.java
     * Create careers in each typing ShiftJob (1),  FixedTimeJob(2), CommissionJob(3)
     * Tries creating career in invalid job type
     * Checks final list to ensure only 3 valid jobs are added
     */
    @Test
    public void testCareerFactoryAndTypes() {
        assertNotNull(CareerFactory.createCareer(1, "Test 1"));
        assertNotNull(CareerFactory.createCareer(2, "Test 2"));
        assertNotNull(CareerFactory.createCareer(3, "Test 3"));
        assertNull(CareerFactory.createCareer(99, "None"));
        List<String> types = CareerFactory.getCareerTypes();
        assertEquals(3, types.size());
    }

    // =====================================================
    // Time Test
    // =====================================================
    /**
     * Tests Time.java
     * See if time progression increments
     * hours and days (24hrs)
     */
    @Test
    public void testTimeAdvancement() {
        time.advance(5);
        assertEquals(13, time.getCurrentHour());
        time.advance(20); 
        assertEquals(2, time.getCurrentDay());
        assertEquals(9, time.getCurrentHour());
    }

    // =====================================================
    // Mood Test
    // =====================================================
    /**
     * Tests Mood.java
     * Test all mood states can be reached
     * Happy (Avg > 60)
     * Okay (40 < Avg < 60)
     * Stressed (Avg < 40)
     * Miserable (If 1 stats < 20)
     */
    @Test
    public void testAllMoodStates() {
        // Test HAPPY
        mood.update(needs); //
        assertEquals(Mood.moodState.HAPPY, mood.getMoodName());

        // Test OKAY
        // Set all to 50: (50*5)/5 = 50
        needs.addHunger(-30); 
        needs.addEnergy(-30);
        needs.addHygiene(-30);
        needs.addSocial(-30);
        needs.addFun(-30);
        mood.update(needs); // Must call update after changing needs
        assertEquals("Mood should be OKAY when average is 50", Mood.moodState.OKAY, mood.getMoodName());

        // Test STRESSED
        // Set all to 20: (20*5)/5 = 20
        needs.addHunger(-30);
        needs.addEnergy(-30);
        needs.addHygiene(-30);
        needs.addSocial(-30);
        needs.addFun(-30);
        mood.update(needs);
        assertEquals("Mood should be STRESSED when average is 20", Mood.moodState.STRESSED, mood.getMoodName());

        // Test MISERABLE
        needs.addHunger(80); // Hunger back to 100
        needs.addEnergy(80); // Energy back to 100
        needs.addHygiene(-15); // Hygiene drops to 5 (Critical)
        mood.update(needs);
        assertEquals("Mood should be MISERABLE if any need is <= 10", Mood.moodState.MISERABLE, mood.getMoodName());
    }

    // =====================================================
    // Needs Tests
    // =====================================================
    /**
     * Tests Needs.java
     * Base needs all set to 80 for new sim
     * Decay accurately decreases values after 2 hours
     * Upper clamping of 100 and lower clamping of 0
     */
    @Test
    public void testAllNeedsDecayAndClamping() {
        // Test Decay
        needs.decay(2); 
        assertEquals("Hunger should decay by 6", 74, needs.getHunger());
        assertEquals("Energy should decay by 4", 76, needs.getEnergy());
        assertEquals("Hygiene should decay by 2", 78, needs.getHygiene());
        assertEquals("Social should decay by 2", 78, needs.getSocial());
        assertEquals("Fun should decay by 2", 78, needs.getFun());

        // Test Upper Clamping
        needs.addHunger(100);
        needs.addEnergy(100);
        needs.addHygiene(100);
        needs.addSocial(100);
        needs.addFun(100);
        assertEquals(100, needs.getHunger());
        assertEquals(100, needs.getEnergy());
        assertEquals(100, needs.getHygiene());
        assertEquals(100, needs.getSocial());
        assertEquals(100, needs.getFun());

        // Test Lower Clamping
        needs.addHunger(-100);
        needs.addEnergy(-100);
        needs.addHygiene(-100);
        needs.addSocial(-100);
        needs.addFun(-100);
        assertEquals(0, needs.getHunger());
        assertEquals(0, needs.getEnergy());
        assertEquals(0, needs.getHygiene());
        assertEquals(0, needs.getSocial());
        assertEquals(0, needs.getFun());
    }

    // --- SIM CHARACTER & DEATH TESTS ---
    /**
     * Tests checkDeath() in SimCharacter.java
     * Drain Hunger to 0 and check if sim is still alive
     * Check sim is still alive after one day passed
     */
    @Test
    public void testDeathFromStarvation() {
        // Drain hunger to 0
        needs.addHunger(-100);
        sim.checkDeath(engine); // Mark Day of 0 Hunger
        assertTrue("Sim should still be alive on the first day of zero hunger", sim.isAlive());

        // Advance time to the next day
        engine.advanceTime(24);
        sim.checkDeath(engine); 
        assertFalse("Sim should be deceased after 24 hours of zero hunger", sim.isAlive());
    }

    /**
     * Tests checkDeath() in SimCharacter.java
     * Drain Energy to 0 and check if sim is still alive
     * Check sim is still alive after one day passed
     */
    @Test
    public void testDeathFromExhaustion() {
        // Drain energy to 0
        needs.addEnergy(-100);
        sim.checkDeath(engine); // Mark Day of 0 Energy
        assertTrue("Sim should still be alive on the first day of zero energy", sim.isAlive());

        // Advance time to the next day
        engine.advanceTime(24);
        sim.checkDeath(engine);
        assertFalse("Sim should be deceased after 24 hours of zero energy", sim.isAlive());
    }

    /**
     * Tests Child Birth and Aging Progress
     * Trigger Pregnancy - Day 1
     * Give Birth - Day 4
     * Test Aging - Day 5 (Baby should be day old)
     * Test Maturing - Day 11 (Baby should be 7 days old)
     * Test Grownn Up - Day 18 (Baby should turn into Sim)
     */
    @Test
    public void testBirthAndAgingProcess() {
        Family family = sim.getFamily();
        
        // Trigger Pregnancy
        String result = "";
        while (!result.contains("Congratulations")) {
            result = family.tryForBaby(sim.getName(), 1);
        }

        // Trigger Birth
        // Mocking input for the scanner
        // Advance to Day 4
        String babyNameInput = "Junior\n";
        System.setIn(new java.io.ByteArrayInputStream(babyNameInput.getBytes()));
        family.onNewDay(4);
        
        assertEquals("Should have 1 child now", 1, family.getChildren().size());
        Child baby = family.getChildren().get(0);
        
        // Test Daily Aging
        // Advance to Day 5
        family.onNewDay(5);
        assertEquals("Child should be 1 day old", 1, baby.getAgeInDays());
        assertTrue("Infant should still need care", baby.getCareStatus());

        // Test Maturing Transition
        // Advance to Day 11
        family.onNewDay(11);
        assertEquals("Child should be 7 days old", 7, baby.getAgeInDays());
        assertFalse("At 7 days, needCare should be false", baby.getCareStatus());

        // Test Grown Up status (Age 14)
        // Advance to Day 18
        family.onNewDay(18);
        assertEquals("Child should be 14 days old", 14, baby.getAgeInDays());
        assertTrue("At 14 days, child should be a grown up", baby.isGrownUp());
        
        // Reset System.in
        System.setIn(System.in);
    }

    // =====================================================
    // Wallet Tests
    // =====================================================
    /**
     * Tests spend() and earn() Wallet.java
     * See if values decrease or increase from both methods
     * Test Negative Earning and Over-Spending balance
     */
    @Test
    public void testWalletFullFunctionality() {
        // Initial Balance = 100
        assertEquals(100.0, wallet.getBalance(), 0.001);

        // Normal Earn
        wallet.earn(50.0);
        assertEquals(150.0, wallet.getBalance(), 0.001);

        //  Negative Earn
        wallet.earn(-50.0);
        assertEquals("Balance should not change after negative earn", 150.0, wallet.getBalance(), 0.001);

        // Successful Spend
        assertTrue("Should allow spending within balance", wallet.spend(100.0));
        assertEquals("Balance should be 50 after spending 100", 50.0, wallet.getBalance(), 0.001);

        // Over-Spending (Should return false and not change balance)
        assertFalse("Should not allow spending more than balance", wallet.spend(1000.0));
        assertEquals("Balance should remain 50.0 after failed over-spend", 50.0, wallet.getBalance(), 0.001);

        // Negative Spend (Should return false and not change balance)
        assertFalse("Should not allow spending negative amounts", wallet.spend(-20.0));
        assertEquals("Balance should remain 50.0 after failed negative spend", 50.0, wallet.getBalance(), 0.001);
    }
}