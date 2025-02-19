package com.test_game.main.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.test_game.main.Core;
import com.test_game.main.Levels.*;

public class PauseMenuScreen extends ScreenAdapter {
    private Stage stage;
    private Skin skin;
    private Core core;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Level gameplayScreen;

    public PauseMenuScreen(Level gameplayScreen) {
        batch = new SpriteBatch();
        this.gameplayScreen = gameplayScreen;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton resumeButton = new TextButton("Resume", skin);
        TextButton saveGameButton = new TextButton("Save Game", skin);
        TextButton restartButton = new TextButton("Restart Game", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        table.add(resumeButton).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(saveGameButton).fillX().uniformX();
        table.row();
        table.add(restartButton).fillX().uniformX(); // Add Restart Game button
        table.row().pad(10, 0, 10, 0);
        table.add(exitButton).fillX().uniformX();

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Core) Gdx.app.getApplicationListener()).setScreen(gameplayScreen);
            }
        });

        saveGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Core) Gdx.app.getApplicationListener()).setScreen(new SaveScreen(gameplayScreen));
            }
        });

        restartButton.addListener(new ClickListener() { // Add Restart Game functionality
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Core) Gdx.app.getApplicationListener()).setScreen(getLevelType(gameplayScreen.lvl));
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Core) Gdx.app.getApplicationListener()).setScreen(new HomeScreen());
            }
        });

        backgroundTexture = new Texture(Gdx.files.internal("playScreenbg.jpg"));
    }
    public Level getLevelType(String type){
        switch (type) {
            case "one":
                return new LevelOne(true);

            case "two":
                return new LevelTwo(true);

            case "three":
                return new LevelThree(true);

        }
        return  null;
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Draw the background
        batch.end();
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
    }
}
