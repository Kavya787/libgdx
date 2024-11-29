package com.test_game.main;

import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import com.test_game.main.Levels.*;
public class Player implements Serializable {

    private int score;
    private LevelOne levelOne;
    private LevelTwo levelTwo;
    private LevelThree levelThree;
    public Level createLevel(int id){
        if(id==1){
            return new LevelOne();
        }
        if(id==2){
            return new LevelTwo();
        }
        if(id==3){
            return new LevelThree();
        }
        return  null;
    }
    public Player() {
        this.score = 0;
        this.levelOne = (LevelOne) createLevel(1);
        this.levelTwo = (LevelTwo) createLevel(2);
        this.levelThree = (LevelThree) createLevel(3);
    }
    public void setScore(int score) {
        this.score = score;
    }
    public Level getLevel(int levelNumber) {
        switch (levelNumber) {
            case 1: return levelOne;
            case 2: return levelTwo;
            case 3: return levelThree;
            default: throw new IllegalArgumentException("Invalid level number");
        }
    }

    }


























