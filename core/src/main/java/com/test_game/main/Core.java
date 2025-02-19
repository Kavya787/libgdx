package com.test_game.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.test_game.main.Birds.Bird;
import com.test_game.main.Levels.*;
import com.test_game.main.Pigs.Pig;
import com.test_game.main.Screens.SelectLevelScreen;

public class Core extends Game {
//    Player player;
    public SpriteBatch batch;
    private Music bgMusic;
    private boolean isMusicOn;
    @Override
    public void create() {
        batch = new SpriteBatch();
        // Initialize background music
//        player=new Player();
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("background_music.mp3"));
        bgMusic.setLooping(true); // Make the music loop
        isMusicOn = true; // Default state of music is ON
        playMusic(); // Start playing the music when the game starts
        setScreen(new SelectLevelScreen());
    }

    @Override
    public void render() {
        super.render();
        // Render the current screen
    }

    @Override
    public void dispose() {
        batch.dispose();
        bgMusic.dispose(); // Dispose of the music when done
    }

    // Music control methods
    public void playMusic() {
        if (!bgMusic.isPlaying()) {
            bgMusic.play();
        }
    }

    public void pauseMusic() {
        if (bgMusic.isPlaying()) {
            bgMusic.pause();
        }
    }

    public void toggleMusic() {
        if (isMusicOn) {
            pauseMusic(); // Pause the music if it's currently playing
        } else {
            playMusic(); // Play the music if it's not playing
        }
        isMusicOn = !isMusicOn; // Toggle the music state
    }

    public boolean isMusicOn() {
        return isMusicOn; // Return the current state of the music
    }
}
