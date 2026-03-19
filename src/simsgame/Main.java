// Main.java
package simsgame;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import simsgame.engine.GameEngine;

public class Main {
    public static void main(String[] args) {
        // Force Java's standard output to use UTF-8
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.out.println("UTF-8 not supported on this system.");
        }
        
        GameEngine engine = new GameEngine();
        ConsoleUI ui = new ConsoleUI(engine);
        ui.run();
    }
}
