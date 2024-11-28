// SelectLevelScreen.java
package com.test_game.main.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.test_game.main.Birds.Bird;
import com.test_game.main.Birds.BlackBird;
import com.test_game.main.Birds.YellowBird;
import com.test_game.main.Core;
import com.test_game.main.Levels.*;
import com.test_game.main.Pigs.*;
import com.test_game.main.blocks.*;

import java.util.ArrayList;
import java.util.PrimitiveIterator;

public class SelectLevelScreen extends ScreenAdapter {
    private Stage stage;
    private Skin skin;
    private Texture backgroundTexture;

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        backgroundTexture = new Texture(Gdx.files.internal("selectlevel.jpg")); // Load your image here

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton level1Button = new TextButton("Level 1", skin);
        TextButton level2Button = new TextButton("Level 2", skin);
        TextButton level3Button = new TextButton("Level 3", skin);
        TextButton backButton = new TextButton("Back", skin);

        // Set button sizes
        level1Button.setSize(200, 50);
        level2Button.setSize(200, 50);
        level3Button.setSize(200, 50);
        backButton.setSize(100, 25);

        table.add(level1Button).size(level1Button.getWidth(), level1Button.getHeight()).fillX().uniformX();
        table.row().pad(20, 0, 20, 0);
        table.add(level2Button).size(level2Button.getWidth(), level2Button.getHeight()).fillX().uniformX();
        table.row();
        table.add(level3Button).size(level3Button.getWidth(), level3Button.getHeight()).fillX().uniformX();
        table.row().pad(20, 0, 20, 0);
        table.add(backButton).size(backButton.getWidth(), backButton.getHeight()).fillX().uniformX();

        level1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
//                ArrayList <Bird>birds=new ArrayList<>();
//                ArrayList<Pig>pigs= new ArrayList<>();
//                ArrayList<Block>buildings= new ArrayList<>();
//                World world = new World(new Vector2(0, -9.8f), true);
//                birds.add(new YellowBird(world, 3, 2));
//                birds.add(new BlackBird(world, 1.5f, 1));
//                birds.add(new YellowBird(world, 0.5f, 1));
//                buildings.add(new GlassBlock(world, 10, 2,1,1));
//                buildings.add(new GlassBlock(world, 13, 2,1,1));
//                buildings.add(new GlassBlock(world, 10, 3,1,1));
//                buildings.add(new GlassBlock(world, 13, 3,1,1));
//                pigs.add(new smallPig(world, 10, 4, 0.2f));
//                pigs.add(new smallPig(world, 13, 4, 0.2f));
                ((Core) Gdx.app.getApplicationListener()).setScreen(new LevelOne(true));
            }
        });
        level2Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Core) Gdx.app.getApplicationListener()).setScreen(new LevelTwo(true));
            }
        });
        level3Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Core) Gdx.app.getApplicationListener()).setScreen(new LevelThree(true));
            }
        });
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Core) Gdx.app.getApplicationListener()).setScreen(new HomeScreen());
            }
        });

    }

    @Override
    public void render(float delta) {
        stage.getBatch().begin();
        stage.getBatch().draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getBatch().end();
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
        skin.dispose();
        backgroundTexture.dispose();
    }
}
