package com.test_game.main.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.test_game.main.Catapult;
import com.test_game.main.Core;
import com.test_game.main.birds.Bird;
import com.test_game.main.birds.BlackBird;
import com.test_game.main.birds.RedBird;
import com.test_game.main.birds.YellowBird;
import com.test_game.main.blocks.Block;
import com.test_game.main.blocks.GlassBlock;
import com.test_game.main.blocks.SteelBlock;
import com.test_game.main.blocks.WoodBlock;
import com.test_game.main.pigs.Pig;
import com.test_game.main.pigs.*;

public class GameplayScreen extends ScreenAdapter {
    private Stage stage;
    private SpriteBatch batch;

    private Pig small_pig;
    private Pig medium_pig;
    private Pig king_pig;

    // Textures for each pig, block, catapult, bird, ground, and background
    private Texture smallPigTexture;
    private Texture mediumPigTexture;
    private Texture kingPigTexture;
    private Texture blockTexture;
    private Texture catapultTexture;
    private Texture birdTexture;
    private Texture backgroundTexture;  // Background image
    private Texture groundTexture;      // Ground image

    // Blocks
    private Block block1;
    private Block block2;
    private Block block3;
    private Block woodBlock;
    // Catapult and Bird
    private Catapult catapult;
    private Bird bird;
    private Bird bird2;
    private Bird bird3;

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);

        // Load textures for pigs, blocks, catapult, bird, ground, and background
        smallPigTexture = new Texture(Gdx.files.internal("small_pig.png"));
        mediumPigTexture = new Texture(Gdx.files.internal("medium_pig.png"));
        kingPigTexture = new Texture(Gdx.files.internal("king_pig.png"));
        blockTexture = new Texture(Gdx.files.internal("building.png"));
        catapultTexture = new Texture(Gdx.files.internal("catapult.png"));
        birdTexture = new Texture(Gdx.files.internal("red_bird.png"));
        backgroundTexture = new Texture(Gdx.files.internal("playScreenbg.jpg")); // Background image
        groundTexture = new Texture(Gdx.files.internal("ground.png"));   // Ground image

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        TextButton pauseButton = new TextButton("Pause", skin);
        pauseButton.setPosition(Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 50);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Core) Gdx.app.getApplicationListener()).setScreen(new PauseMenuScreen(GameplayScreen.this));
            }
        });

        // Calculate the ground height
        float groundHeight = groundTexture.getHeight()-32   ;

        // Create blocks
        block1 = new SteelBlock(425, groundHeight);
        block2 = new GlassBlock(475, groundHeight);
        block3 =new SteelBlock(525, groundHeight);

        small_pig = new SmallPig(block1.getX()-8, block1.getY() + block1.getHeight()-5);
        medium_pig = new MediumPig(block2.getX()-2, block2.getY() + block2.getHeight()+18);
        king_pig = new LargePig(block3.getX()+2, block3.getY() + block3.getHeight()-10);
        woodBlock=new WoodBlock(350,115);
        stage.addActor(block1);
        stage.addActor(block2);
        stage.addActor(block3);
        stage.addActor(woodBlock);
        stage.addActor(pauseButton);

        // Create pigs using the textures
//        small_pig = new SmallPig(block1.getX(), block1.getY() + block1.getHeight());
//        medium_pig = new MediumPig(block2.getX(), block2.getY() + block2.getHeight());
//        king_pig = new LargePig(block3.getX(), block3.getY() + block3.getHeight());

        // Position pigs on top of the blocks
//        small_pig.setPosition(block1.getX(), block1.getY() + block1.getHeight());
//        medium_pig.setPosition(block2.getX(), block2.getY() + block2.getHeight());
//        king_pig.setPosition(block3.getX(), block3.getY() + block3.getHeight());

        // Add pigs to the stage (above the blocks)
        stage.addActor(small_pig);
        stage.addActor(medium_pig);
        stage.addActor(king_pig);

        // Create the catapult and bird
        catapult = new Catapult(catapultTexture);
        bird = new RedBird();
        bird2 = new YellowBird();
        bird3 = new BlackBird();
        // Resize the catapult and bird if needed
        catapult.resizeTexture(0.2f, 0.2f); // Optional scaling
//        bird.resizeTexture(0.2f, 0.2f);     // Resize bird to half its original size

        // Set position for the catapult (just above the ground)
        catapult.setPosition(100, groundHeight);

        // Set the bird on top of the catapult
        bird.setPosition(catapult.getX() + (catapult.getWidth() / 2) - (bird.getWidth() / 2),
            catapult.getY() + catapult.getHeight());
        bird2.setPosition(100,groundHeight);
        bird2.resizeTexture(0.12f, 0.12f);

        bird3.setPosition(60,groundHeight);
        bird3.resizeTexture(0.12f, 0.12f);
        // Add the catapult and bird to the stage
        stage.addActor(catapult);
        stage.addActor(bird);
        stage.addActor(bird2);
        stage.addActor(bird3);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        handleKeyboardInput();
        // Start the batch and draw the background, then the ground
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Draw the background
        batch.draw(groundTexture, 0, 0, Gdx.graphics.getWidth(), groundTexture.getHeight());   // Draw the ground
        batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    private void handleKeyboardInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            ((Core) Gdx.app.getApplicationListener()).setScreen(new WinScreen());
        }else if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            ((Core) Gdx.app.getApplicationListener()).setScreen(new LoseScreen());
        }
    }
    @Override
    public void hide() {
        stage.dispose();
        smallPigTexture.dispose();
        mediumPigTexture.dispose();
        kingPigTexture.dispose();
        blockTexture.dispose();
        catapultTexture.dispose();   // Dispose catapult texture
        birdTexture.dispose();       // Dispose bird texture
        backgroundTexture.dispose(); // Dispose background texture
        groundTexture.dispose();     // Dispose ground texture
    }
}
