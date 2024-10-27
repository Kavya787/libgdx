package com.test_game.main.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.test_game.main.Core;
import com.test_game.main.levels.*;
public class SaveScreen extends ScreenAdapter {
    private Stage stage;
    private Skin skin;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Level gameplayScreen;
    public SaveScreen(Level gameplayScreen) {
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

        // Create buttons for Slot 1, Slot 2, and Back
        TextButton slot1Button = new TextButton("Slot 1", skin);
        TextButton slot2Button = new TextButton("Slot 2", skin);
        TextButton backButton = new TextButton("Back", skin);

        // Add buttons to the table layout
        table.add(slot1Button).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(slot2Button).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(backButton).fillX().uniformX();

        // Listeners for buttons
        slot1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Slot 1 saved");
                // Implement save logic for Slot 1 here
                ((Core) Gdx.app.getApplicationListener()).setScreen(new HomeScreen());
            }
        });

        slot2Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Slot 2 saved");
                // Implement save logic for Slot 2 here
                ((Core) Gdx.app.getApplicationListener()).setScreen(new HomeScreen());
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Go back to MainMenuScreen
                ((Core) Gdx.app.getApplicationListener()).setScreen(new PauseMenuScreen(gameplayScreen));
            }
        });

        // Load background texture
        backgroundTexture = new Texture(Gdx.files.internal("homescreen.jpg")); // Adjust the background image path
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
        backgroundTexture.dispose(); // Dispose of the background texture
    }
}
