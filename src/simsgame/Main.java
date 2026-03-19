// Main.java
package simsgame;

import java.io.IOException;

import simsgame.engine.GameEngine;

public class Main {
    public static void main(String[] args) {
        try {
            // "cmd" starts the command prompt, "/c" tells it to run the command and then terminate
            ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "chcp 65001");
            pb.inheritIO(); // Links the child process output to your main console
            Process process = pb.start();
            process.waitFor(); // Wait for the command to finish before moving on
            
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        GameEngine engine = new GameEngine();
        ConsoleUI ui = new ConsoleUI(engine);
        ui.run();
    }
}
