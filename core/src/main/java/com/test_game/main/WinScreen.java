package com.test_game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class WinScreen extends ScreenAdapter {
    private Stage stage;
    private Skin skin;
    private SpriteBatch batch;
    private Texture backgroundTexture;

    public WinScreen() {
        batch = new SpriteBatch();
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Create a label to display the winning message
        Label winLabel = new Label("YOU WON!", skin);
        winLabel.setFontScale(2); // Adjust the size of the text

        // Create a "Go to Home" button
        TextButton goHomeButton = new TextButton("Go to Home", skin);

        // Add components to the table
        table.add(winLabel).expandX().center(); // Center the winning message
        table.row().pad(10, 0, 10, 0); // Add some padding
        table.add(goHomeButton).fillX().uniformX();

        // Listener for the "Go to Home" button
        goHomeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Redirect to HomeScreen
                ((Core) Gdx.app.getApplicationListener()).setScreen(new HomeScreen());
            }
        });

        // Load background texture
        backgroundTexture = new Texture(Gdx.files.internal("playScreenbg.jpg")); // Adjust the background image path
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
