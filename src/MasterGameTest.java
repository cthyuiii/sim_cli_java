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

    @Before
    public void setUp() {
        engine = new GameEngine();
        engine.createSim("TestSim");
        sim = engine.getActiveSim();
        wallet = sim.getWallet();
        needs = sim.getNeeds();
        mood = sim.getMood();
    }

    @Test
    public void testAllMoodStatesConsolidated() {
        // Defines: {Hunger, Energy, Hygiene, Social, Fun}
        int[][] scenarios = {
            {80, 80, 80, 80, 80},    // HAPPY (Avg 80)
            {50, 50, 50, 50, 50},    // OKAY (Avg 50)
            {25, 25, 25, 25, 25},    // STRESSED (Avg 25)
            {100, 100, 5, 100, 100}  // MISERABLE (Critical < 10)
        };
        Mood.moodState[] expected = {
            Mood.moodState.HAPPY, Mood.moodState.OKAY, 
            Mood.moodState.STRESSED, Mood.moodState.MISERABLE
        };

        for (int i = 0; i < scenarios.length; i++) {
            needs.addHunger(scenarios[i][0] - needs.getHunger());
            needs.addEnergy(scenarios[i][1] - needs.getEnergy());
            needs.addHygiene(scenarios[i][2] - needs.getHygiene());
            needs.addSocial(scenarios[i][3] - needs.getSocial());
            needs.addFun(scenarios[i][4] - needs.getFun());
            
            mood.update(needs);
            assertEquals("Failed at scenario " + expected[i], expected[i], mood.getMoodName());
        }
    }

    @Test
    public void testActivityContractAndAffordability() {
        // 1. Reset time to a known state for the loop
        engine.startNewGame(); // Sets time to 8:00 AM

        Activity[] activities = {
            new EatActivity(10, 25, 1),
            new SleepActivity(40, 8),
            new ShowerActivity(30, 1),
            new SocialiseActivity(25, 10, 2),
            new PlayActivity(25, 10, 2, 0) 
        };

        for (Activity a : activities) {
            int hourBefore = engine.getCurrentHour();
            a.apply(sim, engine);
            
            // Use a 1-hour margin if your engine auto-decays time separately
            int expectedHour = (hourBefore + a.hours()) % 24;
            assertEquals("Time mismatch for " + a.name(), expectedHour, engine.getCurrentHour());
        }

        // 2. Test Affordability Failure (Strict Cost)
        wallet.spend(wallet.getBalance()); // Make sim broke ($0)
        int funBefore = needs.getFun();
        
        // Ensure cost is high (50) so spend() definitely returns false
        new PlayActivity(25, 10, 2, 50).apply(sim, engine);
        
        assertEquals("Fun changed despite being broke!", funBefore, needs.getFun());
    }

    @Test
    public void testCareerAndPromotionSystems() {
        // Factory and Promotion logic
        for (int i = 1; i <= 3; i++) {
            Career c = CareerFactory.createCareer(i, "Job" + i);
            assertNotNull(c);
            
            // Promotion check (True path)
            int initialLevel = c.getLevel();
            c.work(sim, engine, 8); // Adds experience
            assertTrue("Should promote or gain exp", c.getLevel() > initialLevel || c.getWorkExperience() > 0);
        }
        
        // Boundary testing for CareerPromotion arrays
        CareerPromotion cp = new CareerPromotion("Test", new String[]{"Jr"}, new double[]{10}, new int[]{10});
        assertEquals("Should clamp negative level", "Jr", cp.getTitle(-1));
        assertEquals("Should return MAX_VALUE for invalid req", Integer.MAX_VALUE, cp.getPromotionRequirement(5));
    }

    @Test
    public void testLifeAndDeathMechanics() {
        // Exhaustion death (Starvation follows same logic)
        needs.addEnergy(-100);
        sim.checkDeath(engine); // Sets energyZeroDay
        assertTrue(sim.isAlive());

        engine.advanceTime(24);
        sim.checkDeath(engine); // Triggers (day - energyZeroDay >= 1)
        assertFalse("Sim should die after 24h at 0 energy", sim.isAlive());
    }

    @Test
    public void testFamilyAndWorkInput() {
        // Birth and Input Fallbacks
        sim.setCareer(CareerFactory.createCareer(1, "Barista"));
        
        // Mocking "abc" for Work fallback and "Junior" for Baby Name
        String input = "abc\nJunior\n"; 
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));
        
        // Work fallback (Catch block)
        new WorkActivity().apply(sim, engine);
        
        // Birth process
        Family f = sim.getFamily();
        while (!f.isPregnant()) f.tryForBaby(sim.getName(), engine.getCurrentDay());
        f.onNewDay(engine.getCurrentDay() + 3);
        
        assertFalse("Family list should not be empty", f.getChildren().isEmpty());
        System.setIn(new java.io.BufferedInputStream(System.in)); // Reset
    }
}