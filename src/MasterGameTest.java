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
        engine.createSim("TestSim", "Female");
        sim = engine.getActiveSim();
        wallet = sim.getWallet();
        needs = sim.getNeeds();
        mood = sim.getMood();
        time = new Time();
    }

    // --- ACTIVITY TESTS ---
    @Test
    public void testEatActivityFullVsBroke() {
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

    // =======================================================================
    // HIGH PRIORITY MISSING TESTS
    // =======================================================================

    // --- WorkActivity - CommissionJob (Type 3) ---
    @Test
    public void testWorkActivityCommissionJob() {
        // Assign CommissionJob (Type 3)
        Career commissionJob = CareerFactory.createCareer(3, "Salesperson");
        sim.setCareer(commissionJob);

        double walletBefore = wallet.getBalance();
        int hourBefore = engine.getCurrentHour();

        WorkActivity work = new WorkActivity();
        work.apply(sim, engine);

        // CommissionJob uses 6 hours
        assertTrue("Wallet should increase after working", wallet.getBalance() > walletBefore);
        assertEquals("Engine time should advance by 6 hours", (hourBefore + 6) % 24, engine.getCurrentHour());
    }

    // --- WorkActivity - No Career Assigned ---
    @Test
    public void testWorkActivityNoCareer() {
        // Ensure sim has no career
        sim.removeCareer();

        double walletBefore = wallet.getBalance();
        int hourBefore = engine.getCurrentHour();

        WorkActivity work = new WorkActivity();
        work.apply(sim, engine);

        // Should fail gracefully - no money earned, no time advanced
        assertEquals("Wallet should not change", walletBefore, wallet.getBalance(), 0.001);
        assertEquals("Time should not advance", hourBefore, engine.getCurrentHour());
    }

    // --- Death Recovery - Hunger Restored Before Timer ---
    @Test
    public void testDeathRecoveryHunger() {
        // Drain hunger to 0
        needs.addHunger(-100);
        sim.checkDeath(engine); // Day 1: marks hungerZeroDay
        assertTrue("Sim should still be alive", sim.isAlive());

        // Restore hunger before the 24-hour timer completes
        needs.addHunger(50); // Hunger back to 50
        sim.checkDeath(engine); // Should reset hungerZeroDay

        // Advance to next day
        engine.advanceTime(24);
        sim.checkDeath(engine);

        assertTrue("Sim should survive after hunger recovery", sim.isAlive());
    }

    @Test
    public void testDeathRecoveryEnergy() {
        // Drain energy to 0
        needs.addEnergy(-100);
        sim.checkDeath(engine); // Day 1: marks energyZeroDay
        assertTrue("Sim should still be alive", sim.isAlive());

        // Restore energy before the 24-hour timer completes
        needs.addEnergy(50); // Energy back to 50
        sim.checkDeath(engine); // Should reset energyZeroDay

        // Advance to next day
        engine.advanceTime(24);
        sim.checkDeath(engine);

        assertTrue("Sim should survive after energy recovery", sim.isAlive());
    }

    // --- Dead Sim Attempting Actions ---
    @Test
    public void testDeadSimCannotPerformActions() {
        // Kill the sim
        needs.addHunger(-100);
        sim.checkDeath(engine);
        engine.advanceTime(24);
        sim.checkDeath(engine);
        assertFalse("Sim should be dead", sim.isAlive());

        // Try to perform an activity
        int needsBefore = needs.getEnergy();
        SleepActivity sleep = new SleepActivity(40, 8);

        GameContext context = engine; // GameContext is GameEngine
        sim.perform(sleep, context);

        // Needs should not change
        assertEquals("Dead sim should not gain energy", needsBefore, needs.getEnergy());
    }

    // --- Career Promotion Cap (Max Level 2) ---
    @Test
    public void testCareerPromotionCap() {
        CareerPromotion promo = new CareerPromotion("CapTest",
            new String[]{"Junior", "Mid", "Senior"},
            new double[]{10, 20, 30},
            new int[]{10, 20, 30}); // Very low thresholds for quick testing
        FixedTimeJob job = new FixedTimeJob(promo, 8);
        sim.setCareer(job);

        // Work to level 1
        job.work(sim, engine, 8); // +10 exp → level 1
        assertEquals("Should be level 1", 1, job.getLevel());

        // Work to level 2
        job.work(sim, engine, 8); // +10 exp → level 2
        job.work(sim, engine, 8); // +10 exp (total 20) → level 2
        assertEquals("Should be level 2", 2, job.getLevel());

        // Try to promote beyond level 2
        job.work(sim, engine, 8); // +10 exp
        job.work(sim, engine, 8); // +10 exp
        job.work(sim, engine, 8); // +10 exp (total 30+)

        assertEquals("Should stay at level 2 (max)", 2, job.getLevel());
    }

    // --- GameEngine Core Methods ---
    @Test
    public void testGameEngineCreateSim() {
        GameEngine testEngine = new GameEngine();
        testEngine.startNewGame();

        assertEquals("Should start with 0 sims", 0, testEngine.getSimCount());

        testEngine.createSim("Alice", "Female");
        assertEquals("Should have 1 sim", 1, testEngine.getSimCount());

        testEngine.createSim("Bob", "Male");
        assertEquals("Should have 2 sims", 2, testEngine.getSimCount());
    }

    @Test
    public void testGameEngineSimNames() {
        GameEngine testEngine = new GameEngine();
        testEngine.startNewGame();

        testEngine.createSim("Alice", "Female");
        testEngine.createSim("Bob", "Male");
        testEngine.createSim("Charlie", "Male");

        List<String> names = testEngine.getSimNames();
        assertEquals("Should have 3 names", 3, names.size());
        assertEquals("First sim should be Alice", "Alice", names.get(0));
        assertEquals("Second sim should be Bob", "Bob", names.get(1));
        assertEquals("Third sim should be Charlie", "Charlie", names.get(2));
    }

    @Test
    public void testGameEngineActiveSimIndex() {
        GameEngine testEngine = new GameEngine();
        testEngine.startNewGame();

        testEngine.createSim("Alice", "Female");
        testEngine.createSim("Bob", "Male");

        // Default should be first sim (0)
        assertEquals("Default active index should be 0", 0, testEngine.getActiveSimIndex());

        // Switch to second sim
        testEngine.setActiveSimIndex(1);
        assertEquals("Active index should be 1", 1, testEngine.getActiveSimIndex());
        assertEquals("Active sim should be Bob", "Bob", testEngine.getActiveSim().getName());
    }

    @Test
    public void testGameEnginePerformActivity() {
        GameEngine testEngine = new GameEngine();
        testEngine.startNewGame();
        testEngine.createSim("TestSim", "Female");

        SimCharacter testSim = testEngine.getActiveSim();
        Needs testNeeds = testSim.getNeeds();

        // Activity 0 is Eat (+25 hunger, -$10, 1h)
        int hungerBefore = testNeeds.getHunger();
        String result = testEngine.performActivity(0);

        assertNotNull("Should return result message", result);
        assertTrue("Hunger should increase", testNeeds.getHunger() > hungerBefore);
    }

    @Test
    public void testGameEngineAssignCareer() {
        GameEngine testEngine = new GameEngine();
        testEngine.startNewGame();
        testEngine.createSim("TestSim", "Female");

        String result = testEngine.assignCareerToActiveSim(1, "Chef");

        assertNotNull("Should return result message", result);
        assertNotNull("Sim should have career", testEngine.getActiveSim().getCareer());
    }

    @Test
    public void testGameEngineTryForBaby() {
        GameEngine testEngine = new GameEngine();
        testEngine.startNewGame();
        testEngine.createSim("Parent", "Female");

        String result = testEngine.tryForBabyActiveSim();

        assertNotNull("Should return result message", result);
        // Result will vary (success or failure based on RNG and needs)
    }

    // --- tryForBaby - Failure Case (Low Needs) ---
    @Test
    public void testTryForBabyFailureLowNeeds() {
        Family family = sim.getFamily();

        // Set needs too low
        needs.addHunger(-70); // Hunger at 10
        needs.addEnergy(-70); // Energy at 10

        String result = family.tryForBaby(sim.getName(), engine.getCurrentDay());

        assertTrue("Should fail due to low needs",
            result.contains("do not feel") || result.contains("insufficient"));
    }

    // =======================================================================
    // MEDIUM PRIORITY MISSING TESTS
    // =======================================================================

    // --- Need Overflow Protection ---
    @Test
    public void testNeedOverflowProtection() {
        needs.addHunger(500); // Try to set hunger to 580
        assertEquals("Hunger should cap at 100", 100, needs.getHunger());

        needs.addEnergy(200); // Try to set energy to 280
        assertEquals("Energy should cap at 100", 100, needs.getEnergy());

        needs.addHygiene(150); // Try to set hygiene to 230
        assertEquals("Hygiene should cap at 100", 100, needs.getHygiene());
    }

    // --- Simultaneous Death Conditions ---
    @Test
    public void testSimultaneousDeathConditions() {
        // Set both hunger and energy to 0
        needs.addHunger(-100);
        needs.addEnergy(-100);

        sim.checkDeath(engine); // Day 1
        assertTrue("Sim should still be alive on day 1", sim.isAlive());

        // Advance to next day
        engine.advanceTime(24);
        sim.checkDeath(engine);

        assertFalse("Sim should be dead", sim.isAlive());
        // Either death message is acceptable (starvation or exhaustion)
    }

    // --- Mood at Exact Boundaries ---
    @Test
    public void testMoodAtExactBoundaries() {
        // Test avg = exactly 60 (boundary between HAPPY and OKAY)
        needs.addHunger(-20); // 60
        needs.addEnergy(-20); // 60
        needs.addHygiene(-20); // 60
        needs.addSocial(-20); // 60
        needs.addFun(-20); // 60
        mood.update(needs);

        // Avg = 60, should be HAPPY (condition is avg >= 60)
        assertEquals("At avg 60, mood should be HAPPY", Mood.moodState.HAPPY, mood.getMoodName());

        // Test avg = exactly 40 (boundary between OKAY and STRESSED)
        needs.addHunger(-20); // 40
        needs.addEnergy(-20); // 40
        needs.addHygiene(-20); // 40
        needs.addSocial(-20); // 40
        needs.addFun(-20); // 40
        mood.update(needs);

        // Avg = 40, should be OKAY (condition is avg >= 40)
        assertEquals("At avg 40, mood should be OKAY", Mood.moodState.OKAY, mood.getMoodName());
    }

    // --- Wallet - Exact Balance Spend ---
    @Test
    public void testWalletExactBalanceSpend() {
        double balance = wallet.getBalance();

        // Spend exactly the balance
        assertTrue("Should be able to spend exact balance", wallet.spend(balance));
        assertEquals("Balance should be 0", 0.0, wallet.getBalance(), 0.001);
    }

    // --- Career Removal ---
    @Test
    public void testCareerRemoval() {
        Career job = CareerFactory.createCareer(1, "Chef");
        sim.setCareer(job);
        assertNotNull("Sim should have career", sim.getCareer());

        sim.removeCareer();
        assertNull("Sim should have no career", sim.getCareer());
    }

    // --- Multiple Children ---
    @Test
    public void testMultipleChildren() {
        Family family = sim.getFamily();

        // Trigger first pregnancy
        String result1 = "";
        while (!result1.contains("Congratulations")) {
            result1 = family.tryForBaby(sim.getName(), 1);
        }

        // Trigger second pregnancy
        String result2 = "";
        while (!result2.contains("Congratulations")) {
            result2 = family.tryForBaby(sim.getName(), 2);
        }

        // Birth for first child on Day 4
        String input1 = "Child1\n";
        System.setIn(new java.io.ByteArrayInputStream(input1.getBytes()));
        family.onNewDay(4);

        // Birth for second child on Day 5
        String input2 = "Child2\n";
        System.setIn(new java.io.ByteArrayInputStream(input2.getBytes()));
        family.onNewDay(5);

        assertEquals("Should have 2 children", 2, family.getChildren().size());

        // Age both children
        family.onNewDay(10);
        assertEquals("First child should be 6 days old", 6, family.getChildren().get(0).getAgeInDays());
        assertEquals("Second child should be 5 days old", 5, family.getChildren().get(1).getAgeInDays());

        System.setIn(System.in);
    }

    // --- Work Experience Accumulation ---
    @Test
    public void testWorkExperienceAccumulation() {
        CareerPromotion promo = new CareerPromotion("ExpTest",
            new String[]{"J", "M", "S"},
            new double[]{10, 20, 30},
            new int[]{50, 100, 150}); // Higher thresholds
        FixedTimeJob job = new FixedTimeJob(promo, 8);
        sim.setCareer(job);

        assertEquals("Should start at level 0", 0, job.getLevel());
        assertEquals("Should start with 0 exp", 0, job.getWorkExperience());

        // Work 3 times without promoting
        job.work(sim, engine, 8); // +10 exp = 10
        assertEquals("Exp should be 10", 10, job.getWorkExperience());
        assertEquals("Should still be level 0", 0, job.getLevel());

        job.work(sim, engine, 8); // +10 exp = 20
        assertEquals("Exp should be 20", 20, job.getWorkExperience());

        job.work(sim, engine, 8); // +10 exp = 30
        assertEquals("Exp should be 30", 30, job.getWorkExperience());
        assertEquals("Should still be level 0", 0, job.getLevel());
    }

    // =======================================================================
    // LOW PRIORITY MISSING TESTS
    // =======================================================================

    // --- Activity Time Reporting ---
    @Test
    public void testActivityHoursReporting() {
        EatActivity eat = new EatActivity(10, 25, 1);
        assertEquals("Eat should take 1 hour", 1, eat.hours());

        SleepActivity sleep = new SleepActivity(40, 8);
        assertEquals("Sleep should take 8 hours", 8, sleep.hours());

        ShowerActivity shower = new ShowerActivity(30, 1);
        assertEquals("Shower should take 1 hour", 1, shower.hours());

        SocialiseActivity social = new SocialiseActivity(25, 10, 2);
        assertEquals("Social should take 2 hours", 2, social.hours());

        PlayActivity play = new PlayActivity(25, 10, 2, 5);
        assertEquals("Play should take 2 hours", 2, play.hours());
    }

    // --- Invalid Activity Index ---
    @Test
    public void testInvalidActivityIndex() {
        GameEngine testEngine = new GameEngine();
        testEngine.startNewGame();
        testEngine.createSim("TestSim");

        // Try invalid activity index
        String result = testEngine.performActivity(99);

        // Should either return null or error message (depends on implementation)
        // At minimum, should not crash
        assertNotNull("Should handle invalid index gracefully", result);
    }

    // --- Zero Amount Transactions ---
    @Test
    public void testZeroAmountTransactions() {
        double balanceBefore = wallet.getBalance();

        wallet.earn(0);
        assertEquals("Balance should not change on zero earn", balanceBefore, wallet.getBalance(), 0.001);

        wallet.spend(0);
        assertEquals("Balance should not change on zero spend", balanceBefore, wallet.getBalance(), 0.001);
    }

    // --- SimCharacter.perform() Direct Test ---
    @Test
    public void testSimCharacterPerformMethod() {
        int energyBefore = needs.getEnergy();

        SleepActivity sleep = new SleepActivity(40, 8);
        GameContext context = engine;

        sim.perform(sleep, context);

        // Energy should increase
        assertTrue("Energy should increase after sleeping", needs.getEnergy() > energyBefore);

        // Mood should update
        assertNotNull("Mood should be updated", sim.getMood().getMoodName());
    }

    // --- Status String Formatting ---
    @Test
    public void testStatusStringFormatting() {
        String status = sim.getStatusString();

        assertNotNull("Status string should not be null", status);
        assertTrue("Should contain name", status.contains("TestSim"));
        assertTrue("Should contain age", status.contains("Age"));
        assertTrue("Should contain career info", status.contains("Career") || status.contains("Unemployed"));
        assertTrue("Should contain money", status.contains("Money"));
        assertTrue("Should contain needs", status.contains("Hunger"));
        assertTrue("Should contain mood", status.contains("Mood"));
    }

    @Test
    public void testStatusStringWithCareer() {
        Career job = CareerFactory.createCareer(1, "Chef");
        sim.setCareer(job);

        String status = sim.getStatusString();

        assertTrue("Status should show career name", status.contains("Chef") || status.contains("Career"));
        assertFalse("Status should not show Unemployed", status.contains("Unemployed"));
    }

    // --- Full Gameplay Loop Integration ---
    @Test
    public void testFullGameplayLoop() {
        GameEngine testEngine = new GameEngine();
        testEngine.startNewGame();
        testEngine.createSim("Player");

        SimCharacter player = testEngine.getActiveSim();
        Needs playerNeeds = player.getNeeds();
        Wallet playerWallet = player.getWallet();

        // 1. Assign career
        testEngine.assignCareerToActiveSim(2, "Developer");
        assertNotNull("Should have career", player.getCareer());

        // 2. Work to earn money
        double moneyBefore = playerWallet.getBalance();
        testEngine.performActivity(5); // Work
        assertTrue("Should earn money", playerWallet.getBalance() > moneyBefore);

        // 3. Eat (costs money)
        moneyBefore = playerWallet.getBalance();
        int hungerBefore = playerNeeds.getHunger();
        testEngine.performActivity(0); // Eat
        assertTrue("Hunger should increase", playerNeeds.getHunger() >= hungerBefore);
        assertTrue("Money should decrease", playerWallet.getBalance() < moneyBefore);

        // 4. Sleep (free)
        int energyBefore = playerNeeds.getEnergy();
        testEngine.performActivity(1); // Sleep
        assertTrue("Energy should increase", playerNeeds.getEnergy() > energyBefore);

        // 5. Verify sim is still alive
        assertTrue("Player should still be alive", player.isAlive());
    }


}