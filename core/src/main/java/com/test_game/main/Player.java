package com.test_game.main;

import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import com.test_game.main.levels.*;
public class Player implements Serializable {

    private int score;
    private com.test_game.main.Levels.LevelOne levelOne;
   // private LevelTwo levelTwo;
  //  private LevelThree levelThree;

    public Player() {
        this.score = 0;

        // Initialize each level
        this.levelOne = new LevelOne() {
            @Override
            protected void handleCollision(Object objectA, Object objectB) {
                
            }
        };
       // this.levelTwo = new LevelTwo();
        //this.levelThree = new LevelThree();
    }



    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Level getLevel(int levelNumber) {
        switch (levelNumber) {
            case 1: return levelOne;
            //case 2: return levelTwo;
           // case 3: return levelThree;
            default: throw new IllegalArgumentException("Invalid level number");
        }
    }

    // Save the game state to a file
    public void saveGame() {

    }

    // Load the game state from a file
    public static Player loadGame(String playerName) {
        return null;
    }

    private abstract class LevelOne extends com.test_game.main.Levels.LevelOne {
    }
}


























//
//public void saveGame() {
//    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(playerName + "_savegame.ser"))) {
//        out.writeObject(this);
//        System.out.println("Game saved successfully!");
//    } catch (IOException e) {
//        System.out.println("Error saving game: " + e.getMessage());
//    }
//}
//
//// Load the game state from a file
//public static Player loadGame(String playerName) {
//    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(playerName + "_savegame.ser"))) {
//        Player loadedPlayer = (Player) in.readObject();
//        // Reinitialize any transient assets
//        loadedPlayer.levelOne.initializeAssets();
//        loadedPlayer.levelTwo.initializeAssets();
//        loadedPlayer.levelThree.initializeAssets();
//        System.out.println("Game loaded successfully!");
//        return loadedPlayer;
//    } catch (IOException | ClassNotFoundException e) {
//        System.out.println("Error loading game: " + e.getMessage());
//        return null;
//    }
