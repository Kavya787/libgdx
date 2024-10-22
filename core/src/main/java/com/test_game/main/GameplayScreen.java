package com.test_game.main;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameplayScreen extends ScreenAdapter {
    private Stage stage;

    private Pig small_pig;
    private Pig medium_pig;
    private Pig king_pig;

    // Textures for each pig type
    private Texture smallPigTexture;
    private Texture mediumPigTexture;
    private Texture kingPigTexture;

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load textures for each pig type
        smallPigTexture = new Texture(Gdx.files.internal("small_pig.png"));
        mediumPigTexture = new Texture(Gdx.files.internal("medium_pig.png"));
        kingPigTexture = new Texture(Gdx.files.internal("king_pig.png"));

        // Create pigs using the textures
        small_pig = new Pig(smallPigTexture);
        medium_pig = new Pig(mediumPigTexture);
        king_pig = new Pig(kingPigTexture);

        // Example positioning for pigs
        small_pig.setPosition(100, 100);
        medium_pig.setPosition(200, 200);
        king_pig.setPosition(300, 300);

        // Add actors to the stage
        stage.addActor(small_pig);
        stage.addActor(medium_pig);
        stage.addActor(king_pig);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
        stage.dispose();
        smallPigTexture.dispose();
        mediumPigTexture.dispose();
        kingPigTexture.dispose();
    }
}
