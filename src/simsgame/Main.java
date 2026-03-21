// Main.java
package simsgame;

import simsgame.engine.GameEngine;

// Main class is the entry point of the application. It initializes the GameEngine and starts the ConsoleUI.
public class Main {
    public static void main(String[] args) {
        GameEngine engine = new GameEngine();
        ConsoleUI ui = new ConsoleUI(engine);
        ui.run();
    }
}
