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
import com.test_game.main.Birds.Bird;
import com.test_game.main.Core;
import com.test_game.main.LevelSerializer;
import com.test_game.main.Levels.Level;
import com.test_game.main.Levels.LevelOne;
import com.test_game.main.Pigs.Pig;

public class LoadScreenMenu extends ScreenAdapter {
    private Stage stage;
    private Skin skin;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Core core; // Reference to the main game core to switch screens


    public LoadScreenMenu() {

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

        // Create buttons for Slot 1, Slot 2, and Back
        TextButton slot1Button = new TextButton("Slot 1", skin);
        TextButton slot2Button = new TextButton("Slot 2", skin);
        TextButton backButton = new TextButton("Back", skin);

        table.add(slot1Button).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(slot2Button).fillX().uniformX();
        table.row();
        table.add(backButton).fillX().uniformX();

        // Listeners for buttons
        slot1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Slot 1 loaded");
                LevelSerializer levelSerializer= new LevelSerializer();
                String fileName = "slot1.json";
                Level loadedLevel = levelSerializer.load(fileName);
//                for(Bird bl : loadedLevel.birds){
//                    System.out.println(bl.type);
//                }
//         Set the initial screen (e.g., HomeScreen)
                ((Core) Gdx.app.getApplicationListener()).setScreen(new LevelOne(loadedLevel.birds,loadedLevel.pigs,loadedLevel.buildings,loadedLevel.currentBirdIndex));
                // Set the initial screen (e.g., HomeScreen)
//                core.setScreen(new LevelOne(loadedLevel.birds,loadedLevel.currentBirdIndex));
                // Implement load logic for Slot 1 here
            }
        });

        slot2Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Slot 2 loaded");
                // Implement load logic for Slot 2 here
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Go back to MainMenuScreen
                ((Core) Gdx.app.getApplicationListener()).setScreen(new HomeScreen());
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
