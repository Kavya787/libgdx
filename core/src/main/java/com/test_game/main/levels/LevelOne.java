package com.test_game.main.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.test_game.main.Catapult;
import com.test_game.main.Core;
//import com.test_game.main.Screens.GameplayScreen;
import com.test_game.main.Screens.PauseMenuScreen;
import com.test_game.main.birds.Bird;
import com.test_game.main.birds.BlackBird;
import com.test_game.main.birds.RedBird;
import com.test_game.main.birds.YellowBird;
import com.test_game.main.blocks.Block;
import com.test_game.main.blocks.GlassBlock;
import com.test_game.main.blocks.SteelBlock;
import com.test_game.main.blocks.WoodBlock;
import com.test_game.main.pigs.LargePig;
import com.test_game.main.pigs.MediumPig;
import com.test_game.main.pigs.Pig;
import com.test_game.main.pigs.SmallPig;

import java.util.ArrayList;

public class LevelOne extends Level{
    public void show() {
        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);


        birdTexture = new Texture(Gdx.files.internal("red_bird.png"));
        backgroundTexture = new Texture(Gdx.files.internal("playScreenbg.jpg"));
        groundTexture = new Texture(Gdx.files.internal("ground.png"));

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        TextButton pauseButton = new TextButton("Pause", skin);
        pauseButton.setPosition(Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 50);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Core) Gdx.app.getApplicationListener()).setScreen(new PauseMenuScreen(LevelOne.this));
            }
        });
        stage.addActor(pauseButton);

        float groundHeight = groundTexture.getHeight() - 32;

        // Initialize lists for pigs, blocks, and birds
        pigs = new ArrayList<>();
        blocks = new ArrayList<>();
        birds = new ArrayList<>();

        // Create and add blocks
        blocks.add(new SteelBlock(425, groundHeight));
        blocks.add(new GlassBlock(475, groundHeight));
        blocks.add(new SteelBlock(525, groundHeight));
        blocks.add(new WoodBlock(350, 115));

        for (Block block : blocks) {
            stage.addActor(block);
        }

        // Create and add pigs, positioned on top of specific blocks
        pigs.add(new SmallPig(blocks.get(0).getX() - 8, blocks.get(0).getY() + blocks.get(0).getHeight() - 5));
        pigs.add(new MediumPig(blocks.get(1).getX() - 2, blocks.get(1).getY() + blocks.get(1).getHeight() + 18));
        pigs.add(new LargePig(blocks.get(2).getX() + 2, blocks.get(2).getY() + blocks.get(2).getHeight() - 10));

        for (Pig pig : pigs) {
            stage.addActor(pig);
        }

        // Create and add birds
        birds.add(new RedBird());
        birds.add(new YellowBird());
        birds.add(new BlackBird());

        // Set bird positions and sizes
        birds.get(0).setPosition(100, groundHeight); // red bird
        birds.get(1).setPosition(60, groundHeight); // yellow bird
        birds.get(2).setPosition(30, groundHeight); // black bird

        birds.get(1).resizeTexture(0.12f, 0.12f);
        birds.get(2).resizeTexture(0.12f, 0.12f);

        for (Bird bird : birds) {
            stage.addActor(bird);
        }

        // Create catapult and set its position
        catapult = new Catapult();
        catapult.setPosition(100, groundHeight);
        catapult.resizeTexture(0.2f, 0.2f);
        stage.addActor(catapult);

        // Position the first bird on top of the catapult
        birds.get(0).setPosition(
            catapult.getX() + (catapult.getWidth() / 2) - (birds.get(0).getWidth() / 2),
            catapult.getY() + catapult.getHeight());
    }
}
