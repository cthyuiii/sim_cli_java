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
        engine.startNewGame(); //
        engine.createSim("TestSim", "Female"); //
        sim = engine.getActiveSim(); //
        wallet = sim.getWallet(); //
        needs = sim.getNeeds(); //
        mood = sim.getMood(); //
        time = new Time(); //
    }

    // --- ACTIVITY TESTS ---
    @Test
    public void testEatActivity() {
        needs.addHunger(-50); // Hunger at 30
        EatActivity eat = new EatActivity(10, 30, 1);
        
        // Full meal
        eat.apply(sim, engine);
        assertEquals(60, needs.getHunger());
        assertEquals(90.0, wallet.getBalance(), 0.001);

        // Broke fallback
        wallet.spend(90.0);
        eat.apply(sim, engine); 
        assertEquals(70, needs.getHunger()); // gains 30/3 = 10
    }

    @Test
    public void testPlayActivityAffordability() {
        PlayActivity play = new PlayActivity(25, 10, 2, 200); // Too expensive
        int funBefore = needs.getFun();
        play.apply(sim, engine);
        assertEquals(funBefore, needs.getFun());
    }

    @Test
    public void testPlayActivityFree() {
        // Setup a free activity (cost = 0)
        // PlayActivity(funGain, energyCost, hours, cost)
        PlayActivity freePlay = new PlayActivity(20, 5, 1, 0); //
        
        int funBefore = needs.getFun(); //
        double walletBefore = wallet.getBalance(); //

        freePlay.apply(sim, engine); // This will trigger the red 'else' block

        // Verify effects
        assertEquals("Fun should increase", funBefore + 20, needs.getFun()); //
        assertEquals("Wallet should remain unchanged", walletBefore, wallet.getBalance(), 0.001); //
    }

    @Test
    public void testSleepActivity() {
        needs.addEnergy(-50); // Set energy to 30
        int energyBefore = needs.getEnergy();
        int hungerBefore = needs.getHunger();

        // SleepActivity (+40 energy, -10 hunger, 8 hours)
        SleepActivity sleep = new SleepActivity(40, 8);
        sleep.apply(sim, engine);

        assertEquals("Energy should increase by 40", energyBefore + 40, needs.getEnergy());
        assertEquals("Sleeping should decrease hunger by 10", hungerBefore - 10, needs.getHunger());
    }

    @Test
    public void testShowerActivity() {
        needs.addHygiene(-50); // Set hygiene to 30
        int hygieneBefore = needs.getHygiene();

        // ShowerActivity (+30 hygiene, 1 hour)
        ShowerActivity shower = new ShowerActivity(30, 1);
        shower.apply(sim, engine);

        assertEquals("Hygiene should increase by 30", hygieneBefore + 30, needs.getHygiene());
    }

    @Test
    public void testSocialiseActivity() {
        needs.addSocial(-50);
        needs.addFun(-50);
        int socialBefore = needs.getSocial();
        int funBefore = needs.getFun();

        // SocialiseActivity (+25 social, +10 fun, 2 hours)
        SocialiseActivity social = new SocialiseActivity(25, 10, 2);
        social.apply(sim, engine);

        assertEquals("Social should increase by 25", socialBefore + 25, needs.getSocial());
        assertEquals("Fun should increase by 10", funBefore + 10, needs.getFun());
    }

    @Test
    public void testPlayActivitySuccess() {
        needs.addFun(-50);
        needs.addEnergy(-10);
        int funBefore = needs.getFun();
        int energyBefore = needs.getEnergy();
        double walletBefore = wallet.getBalance();

        // PlayActivity (+25 fun, -10 energy, 2 hours, $5 cost)
        PlayActivity play = new PlayActivity(25, 10, 2, 5);
        play.apply(sim, engine);

        assertEquals("Fun should increase by 25", funBefore + 25, needs.getFun());
        assertEquals("Energy should decrease by 10", energyBefore - 10, needs.getEnergy());
        assertEquals("Wallet should decrease by $5", walletBefore - 5, wallet.getBalance(), 0.001);
    }

    @Test
    public void testWorkActivityLogic() {
        // Assign a career first so WorkActivity has a target
        Career job = CareerFactory.createCareer(2, "Programmer"); // FixedTimeJob, 8h
        sim.setCareer(job);
        
        double walletBefore = wallet.getBalance();
        int hourBefore = engine.getCurrentHour();

        // WorkActivity handles the Career.work() call and engine time advancement
        WorkActivity work = new WorkActivity();
        work.apply(sim, engine);
        // sim.perform(work, engine);

        // FixedTimeJob (Type 2) uses fixedHours (8) for pay and time
        assertTrue("Wallet should increase after working", wallet.getBalance() > walletBefore);
        assertEquals("Engine time should advance by 8 hours", (hourBefore + 8) % 24, engine.getCurrentHour());
    }

    @Test
    public void testWorkActivityShiftJobInvalidInput() {
        // 1. Assign a ShiftJob
        Career shift = CareerFactory.createCareer(1, "Barista");
        sim.setCareer(shift);

        // 2. Mock 'abc' input to trigger the RED catch block
        // This forces hoursWorked = 4
        String input = "abc\n";
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));
        
        // We must recreate the activity so it uses the new System.in
        WorkActivity work = new WorkActivity();
        work.apply(sim, engine);

        // Verify fallback hours (4) were used
        assertEquals("Time should advance by fallback 4 hours", 12, engine.getCurrentHour());
        
        // Reset System.in for other tests
        System.setIn(new java.io.BufferedInputStream(System.in));
    }

    @Test
    public void testWorkActivityShiftJobNegativeInput() {
        sim.setCareer(CareerFactory.createCareer(1, "Barista"));
        
        // Mock '-5' input
        String input = "-5\n";
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));
        
        WorkActivity work = new WorkActivity();
        work.apply(sim, engine);

        // hoursWorked <= 0 forces it to 1
        assertEquals("Time should advance by minimum 1 hour", 9, engine.getCurrentHour());
    }

    // --- WALLET TESTS ---
    @Test
    public void testWalletTransactions() {
        wallet.earn(50.0);
        assertEquals(150.0, wallet.getBalance(), 0.001);

        assertTrue(wallet.spend(100.0));
        assertFalse(wallet.spend(1000.0));
    }

    @Test
    public void testWalletLogic() {
        // Test negative earn protection
        double initial = wallet.getBalance();
        wallet.earn(-50);
        assertEquals(initial, wallet.getBalance(), 0.001);

        // Test insufficient funds
        assertFalse(wallet.spend(200));
    }

    @Test
    public void testWalletNegCases() {
        wallet.earn(-50.0);
        assertEquals(100.0, wallet.getBalance(), 0.001);

        assertFalse(wallet.spend(-20.0));
        assertEquals(100.0, wallet.getBalance(), 0.001);
    }

    // --- NEEDS & MOOD TESTS ---
    @Test
    public void testNeedsDecayAndClamping() {
        needs.decay(2); 
        assertEquals(74, needs.getHunger());
        assertEquals(76, needs.getEnergy());

        needs.addHunger(500);
        assertEquals(100, needs.getHunger());

        needs.addHunger(-500);
        assertEquals(0, needs.getHunger());
    }

    @Test
    public void testAllMoodStates() {
        // 1. Test HAPPY (Default stats are 80, Avg 80 >= 60)
        mood.update(needs); //
        assertEquals(Mood.moodState.HAPPY, mood.getMoodName());

        // 2. Test OKAY (Target Avg between 40 and 59)
        // Set all to 50: (50*5)/5 = 50
        needs.addHunger(-30); 
        needs.addEnergy(-30);
        needs.addHygiene(-30);
        needs.addSocial(-30);
        needs.addFun(-30);
        mood.update(needs); // Must call update after changing needs
        assertEquals("Mood should be OKAY when average is 50", Mood.moodState.OKAY, mood.getMoodName());

        // 3. Test STRESSED (Target Avg < 40)
        // Set all to 20: (20*5)/5 = 20
        needs.addHunger(-30);
        needs.addEnergy(-30);
        needs.addHygiene(-30);
        needs.addSocial(-30);
        needs.addFun(-30);
        mood.update(needs);
        assertEquals("Mood should be STRESSED when average is 20", Mood.moodState.STRESSED, mood.getMoodName());

        // 4. Test MISERABLE (Any need <= 10)
        // Even if average is high, one critical need overrides
        needs.addHunger(80); // Hunger back to 100
        needs.addEnergy(80); // Energy back to 100
        needs.addHygiene(-15); // Hygiene drops to 5 (Critical)
        mood.update(needs);
        assertEquals("Mood should be MISERABLE if any need is <= 10", Mood.moodState.MISERABLE, mood.getMoodName());
    }

    // --- TIME TESTS ---
    @Test
    public void testTimeAdvancement() {
        time.advance(5);
        assertEquals(13, time.getCurrentHour());
        time.advance(20); 
        assertEquals(2, time.getCurrentDay());
        assertEquals(9, time.getCurrentHour());
    }
    

    // --- CAREER TESTS ---
    @Test
    public void testCareerFactoryAndTypes() {
        assertNotNull(CareerFactory.createCareer(1, "Chef"));
        assertNotNull(CareerFactory.createCareer(2, "Pilot"));
        assertNotNull(CareerFactory.createCareer(3, "Writer"));
        assertNull(CareerFactory.createCareer(99, "None"));
        List<String> types = CareerFactory.getCareerTypes();
        assertEquals(3, types.size());
    }

    @Test
    public void testPromotionLogic() {
        CareerPromotion promo = new CareerPromotion("Test", 
            new String[]{"J", "S", "M"}, new double[]{10, 20, 30}, new int[]{10, 20, 30});
        FixedTimeJob job = new FixedTimeJob(promo, 8);
        
        job.work(sim, engine, 8); // Adds 10 exp
        assertEquals(1, job.getLevel());
        assertEquals("S", job.getCurrentTitle());
    }

    @Test
    public void testCareerPromotionBoundaries() {
        // Setup promotion with 3 levels (indices 0, 1, 2)
        CareerPromotion promo = new CareerPromotion("BoundaryTest", 
            new String[]{"Junior", "Mid", "Senior"}, 
            new double[]{10.0, 20.0, 30.0}, 
            new int[]{100, 200, 300});

        // 1. Test Negative Levels (Lines 22 & 28)
        // Should clamp to index 0
        assertEquals("Negative level should return index 0 title", "Junior", promo.getTitle(-1));
        assertEquals("Negative level should return index 0 pay", 10.0, promo.getPayRate(-5), 0.001);

        // 2. Test Excessive Levels (Lines 23 & 29)
        // Should clamp to the last index (2)
        assertEquals("Excessive level should return last title", "Senior", promo.getTitle(10));
        assertEquals("Excessive level should return last pay", 30.0, promo.getPayRate(5), 0.001);

        // 3. Test Promotion Requirement Boundaries (Line 34)
        // Should return Integer.MAX_VALUE for invalid indices
        assertEquals("Negative index should be impossible to promote", 
            Integer.MAX_VALUE, promo.getPromotionRequirement(-1));
        assertEquals("Index beyond array should be impossible to promote", 
            Integer.MAX_VALUE, promo.getPromotionRequirement(3));
    }

    // --- SIM CHARACTER & DEATH TESTS ---
    @Test
    public void testDeathFromStarvation() {
        needs.addHunger(-100);
        sim.checkDeath(engine); // Day 1: sets hungerZeroDay
        assertTrue(sim.isAlive());

        engine.advanceTime(24); // Move to Day 2
        sim.checkDeath(engine); 
        assertFalse(sim.isAlive());
    }

    @Test
    public void testDeathFromExhaustion() {
        // 1. Drain energy to 0
        needs.addEnergy(-100);
        
        // 2. First check: should mark the day energy hit zero but remain alive
        sim.checkDeath(engine);
        assertTrue("Sim should still be alive on the first day of zero energy", sim.isAlive());

        // 3. Advance time by 24 hours to transition to the next day
        engine.advanceTime(24);
        
        // 4. Second check: (day - energyZeroDay >= 1) should now be true
        sim.checkDeath(engine);
        
        // 5. Assertions
        assertFalse("Sim should be deceased after 24 hours of zero energy", sim.isAlive());
    }

    @Test
    public void testBirthAndAgingProcess() {
        Family family = sim.getFamily();
        
        // 1. Trigger Pregnancy
        String result = "";
        while (!result.contains("Congratulations")) {
            result = family.tryForBaby(sim.getName(), 1);
        }

        // 2. Trigger Birth on Day 4
        // Mocking input for the scanner
        String babyNameInput = "Junior\n";
        System.setIn(new java.io.ByteArrayInputStream(babyNameInput.getBytes()));
        
        family.onNewDay(4);
        
        assertEquals("Should have 1 child now", 1, family.getChildren().size());
        Child baby = family.getChildren().get(0);
        
        // 3. Test Daily Aging (Day 5)
        family.onNewDay(5);
        assertEquals("Child should be 1 day old", 1, baby.getAgeInDays());
        assertTrue("Infant should still need care", baby.getCareStatus());

        // 4. Test Transition: No longer needs care (Age 7)
        // Advance to Day 11 (Birth Day 4 + 7 days = 11)
        family.onNewDay(11);
        assertEquals("Child should be 7 days old", 7, baby.getAgeInDays());
        assertFalse("At 7 days, needCare should be false", baby.getCareStatus());

        // 5. Test Transition: Grown Up status (Age 14)
        // Advance to Day 18 (Birth Day 4 + 14 days = 18)
        family.onNewDay(18);
        assertEquals("Child should be 14 days old", 14, baby.getAgeInDays());
        assertTrue("At 14 days, child should be a grown up", baby.isGrownUp());

        // Reset System.in
        System.setIn(System.in);
    }


}