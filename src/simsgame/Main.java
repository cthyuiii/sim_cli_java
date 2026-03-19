// Main.java
package simsgame;

import simsgame.engine.GameEngine;

public class Main {
    public static void main(String[] args) {
        GameEngine engine = new GameEngine();
        ConsoleUI ui = new ConsoleUI(engine);
        ui.run();
    }
}
