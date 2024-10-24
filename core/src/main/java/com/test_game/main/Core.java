package com.test_game.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.ApplicationListener;


public class Core extends Game {
    public SpriteBatch batch;
    private Music bgMusic;
    private boolean isMusicOn;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // Initialize background music
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("background_music.mp3"));
        bgMusic.setLooping(true); // Make the music loop
        isMusicOn = true; // Default state of music is ON
        playMusic(); // Start playing the music when the game starts

        // Set the initial screen (e.g., HomeScreen)
        setScreen(new HomeScreen(this));
    }

    @Override
    public void render() {
        super.render(); // Render the current screen
    }

    @Override
    public void dispose() {
        batch.dispose();
        bgMusic.dispose(); // Dispose of the music when done
    }

    // Music control methods
    public void playMusic() {
        if (!bgMusic.isPlaying()) {
            bgMusic.play(); // Play the music if it's not already playing
        }
    }

    public void pauseMusic() {
        if (bgMusic.isPlaying()) {
            bgMusic.pause(); // Pause the music if it's currently playing
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
