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

    // Textures for each pig, block, catapult, and bird
    private Texture smallPigTexture;
    private Texture mediumPigTexture;
    private Texture kingPigTexture;
    private Texture blockTexture;
    private Texture catapultTexture;
    private Texture birdTexture;

    // Blocks
    private Block block1;
    private Block block2;
    private Block block3;

    // Catapult and Bird
    private Catapult catapult;
    private Bird bird;

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load textures for pigs, blocks, catapult, and bird
        smallPigTexture = new Texture(Gdx.files.internal("small_pig.png"));
        mediumPigTexture = new Texture(Gdx.files.internal("medium_pig.png"));
        kingPigTexture = new Texture(Gdx.files.internal("king_pig.png"));
        blockTexture = new Texture(Gdx.files.internal("building.png")); // Assuming you have a texture for blocks
        catapultTexture = new Texture(Gdx.files.internal("catapult.png")); // Texture for the catapult
        birdTexture = new Texture(Gdx.files.internal("red_bird.png"));         // Texture for the bird

        // Create blocks
        block1 = new Block(blockTexture);
        block2 = new Block(blockTexture);
        block3 = new Block(blockTexture);

        // Resize blocks using resizeTexture method (optional)
        block1.resizeTexture(1.0f, 3.0f);
        block2.resizeTexture(1.0f, 3.0f);
        block3.resizeTexture(1.0f, 3.0f);

        // Set position for blocks
        block1.setPosition(425, 50);
        block2.setPosition(475, 50);
        block3.setPosition(525, 50);

        // Add blocks to the stage first (so they appear below the pigs)
        stage.addActor(block1);
        stage.addActor(block2);
        stage.addActor(block3);

        // Create pigs using the textures
        small_pig = new Pig(smallPigTexture);
        medium_pig = new Pig(mediumPigTexture);
        king_pig = new Pig(kingPigTexture);

        // Resize pigs using resizeTexture method
        small_pig.resizeTexture(0.1f, 0.1f);
        medium_pig.resizeTexture(0.1f, 0.1f);
        king_pig.resizeTexture(0.1f, 0.1f);

        // Position pigs on top of the blocks
        small_pig.setPosition(block1.getX(), block1.getY() + block1.getHeight());
        medium_pig.setPosition(block2.getX(), block2.getY() + block2.getHeight());
        king_pig.setPosition(block3.getX(), block3.getY() + block3.getHeight());

        // Add pigs to the stage (above the blocks)
        stage.addActor(small_pig);
        stage.addActor(medium_pig);
        stage.addActor(king_pig);

        // Create the catapult and bird
        catapult = new Catapult(catapultTexture);
        bird = new Bird(birdTexture);

        // Resize the catapult and bird if needed
        catapult.resizeTexture(0.2f, 0.2f); // Optional scaling
        bird.resizeTexture(0.2f, 0.2f);     // Resize bird to half its original size

        // Set position for the catapult
        catapult.setPosition(100, 50); // Position the catapult on the ground

        // Set the bird on top of the catapult
        bird.setPosition(catapult.getX() + (catapult.getWidth() / 2) - (bird.getWidth() / 2),
            catapult.getY() + catapult.getHeight());

        // Add the catapult and bird to the stage
        stage.addActor(catapult);
        stage.addActor(bird);
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
        blockTexture.dispose();
        catapultTexture.dispose();  // Dispose catapult texture
        birdTexture.dispose();      // Dispose bird texture
    }
}
