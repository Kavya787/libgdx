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
        levelSerializer = new LevelSerializer();
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Load background texture
        backgroundTexture = new Texture(Gdx.files.internal("homescreen.jpg")); // Adjust the background image path

        // Check for existing save files
        boolean slot1Exists = saveFileExists("slot1.json");
        boolean slot2Exists = saveFileExists("slot2.json");

        // Create buttons for Slot 1, Slot 2, and Back
        slot1Button = new TextButton(slot1Exists ? "Slot 1 (Saved)" : "Slot 1", skin);
        slot2Button = new TextButton(slot2Exists ? "Slot 2 (Saved)" : "Slot 2", skin);
        deleteSlot1Button = new TextButton("Delete Slot 1", skin);
        deleteSlot2Button = new TextButton("Delete Slot 2", skin);
        TextButton backButton = new TextButton("Back", skin);

        // Add buttons to the table layout
        table.add(slot1Button).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(deleteSlot1Button).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);

        table.add(slot2Button).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(deleteSlot2Button).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);

        table.add(backButton).fillX().uniformX();

        // Listeners for save buttons
        slot1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Slot 1 saved");
                String fileName = "slot1.json";
                levelSerializer.save(gameplayScreen, fileName);
                updateButtonLabels(); // Update button labels after saving
                ((Core) Gdx.app.getApplicationListener()).setScreen(new HomeScreen());
            }
        });

        slot2Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Slot 2 saved");
                String fileName = "slot2.json";
                levelSerializer.save(gameplayScreen, fileName);
                updateButtonLabels(); // Update button labels after saving
                ((Core) Gdx.app.getApplicationListener()).setScreen(new HomeScreen());
            }
        });

        // Listeners for delete buttons
        deleteSlot1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                deleteSaveFile("slot1.json");
                updateButtonLabels(); // Update button labels after deletion
            }
        });

        deleteSlot2Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                deleteSaveFile("slot2.json");
                updateButtonLabels(); // Update button labels after deletion
            }
        });

        // Listener for back button
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Go back to MainMenuScreen
                ((Core) Gdx.app.getApplicationListener()).setScreen(new PauseMenuScreen(gameplayScreen));
            }
        });
    }

    private boolean saveFileExists(String fileName) {
        return new File(fileName).exists(); // Check if the save file exists
    }

    private void deleteSaveFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println(fileName + " deleted successfully.");
            } else {
                System.out.println("Failed to delete " + fileName + ".");
            }
        } else {
            System.out.println(fileName + " does not exist.");
        }
    }

    private void updateButtonLabels() {
        if (saveFileExists("slot1.json")) {
            slot1Button.setText("Slot 1 (Saved)");
        } else {
            slot1Button.setText("Slot 1");
        }

        if (saveFileExists("slot2.json")) {
            slot2Button.setText("Slot 2 (Saved)");
        } else {
            slot2Button.setText("Slot 2");
        }
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
