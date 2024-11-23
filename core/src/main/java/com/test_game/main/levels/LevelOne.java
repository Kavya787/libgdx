package com.test_game.main.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.test_game.main.Catapult;
import com.test_game.main.Core;
import com.test_game.main.Screens.LoseScreen;
import com.test_game.main.Screens.PauseMenuScreen;
import com.test_game.main.Screens.WinScreen;
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

public class LevelOne extends Level {
    private Skin skin;
    private Texture redBirdTexture;
    private Texture yellowBirdTexture;
    private Texture blackBirdTexture;
    private Texture catapultTexture;
    private Texture SmallPigTexture;
    private Texture MediumPigTexture;
    private Texture LargePigTexture;
    private Texture SteelBlockTexture;
    private Texture GlassBlockTexture;
    private Texture WoodBlockTexture;

    public LevelOne() {
        super();
        loadResources();
        setupUI();
        initializeGameObjects();
    }

    private void loadResources() {
        // Load bird textures
        redBirdTexture = new Texture(Gdx.files.internal("red_bird.png"));
        yellowBirdTexture = new Texture(Gdx.files.internal("yellow_bird.png"));
        blackBirdTexture = new Texture(Gdx.files.internal("black_bird.png"));
        catapultTexture = new Texture(Gdx.files.internal("catapult.png"));
        SmallPigTexture = new Texture(Gdx.files.internal("small_pig.png"));
        MediumPigTexture = new Texture(Gdx.files.internal("medium_pig.png"));
        LargePigTexture = new Texture(Gdx.files.internal("king_pig.png"));
        SteelBlockTexture = new Texture(Gdx.files.internal("steelBlock.png"));
        GlassBlockTexture = new Texture(Gdx.files.internal("woodPlank.png"));
        WoodBlockTexture = new Texture(Gdx.files.internal("WoodBlock.png"));


        skin = new Skin(Gdx.files.internal("uiskin.json"));
    }

    @Override
    protected void loadTextures() {
        backgroundTexture = new Texture(Gdx.files.internal("playScreenbg.jpg"));
        groundTexture = new Texture(Gdx.files.internal("ground.png"));
    }



    private void setupUI() {
        TextButton pauseButton = new TextButton("Pause", skin);
        pauseButton.setName("pause");
        pauseButton.setPosition(Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 50);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Core) Gdx.app.getApplicationListener()).setScreen(new PauseMenuScreen(LevelOne.this));
            }
        });
        stage.addActor(pauseButton);
    }

    private void initializeGameObjects() {
        float groundHeight = ground.getHeight();

        // Create and add blocks with physics
        initializeBlocks(groundHeight);

        // Create and add pigs with physics
        initializePigs();

        // Create and add birds with physics
        initializeBirds(groundHeight);

        // Create catapult with physics
        initializeCatapult(groundHeight);
    }

    private void initializeBlocks(float groundHeight) {
        blocks.add(new SteelBlock(world, SteelBlockTexture, 425, groundHeight));
        blocks.add(new GlassBlock(world, GlassBlockTexture, 475, groundHeight));
        blocks.add(new SteelBlock(world, SteelBlockTexture, 525, groundHeight));
        blocks.add(new WoodBlock(world, WoodBlockTexture, 350, 115));

        for (Block block : blocks) {
            stage.addActor(block);
        }
    }

    private void initializePigs() {
        // Create and add pigs, positioned on top of specific blocks
        pigs.add(new SmallPig(world, SmallPigTexture, blocks.get(0).getX() - 8,
            blocks.get(0).getY() + blocks.get(0).getHeight() - 5));
        pigs.add(new MediumPig(world, MediumPigTexture, blocks.get(1).getX() - 2,
            blocks.get(1).getY() + blocks.get(1).getHeight() + 18));
        pigs.add(new LargePig(world, LargePigTexture, blocks.get(2).getX() + 2,
            blocks.get(2).getY() + blocks.get(2).getHeight() - 10));

        for (Pig pig : pigs) {
            stage.addActor(pig);
        }
    }

    private void initializeBirds(float groundHeight) {
        birds.add(new RedBird(world, redBirdTexture, 100, groundHeight));
        birds.add(new YellowBird(world, yellowBirdTexture, 60, groundHeight));
        birds.add(new BlackBird(world, blackBirdTexture, 30, groundHeight));

        birds.get(1).resizeTexture(0.12f, 0.12f);
        birds.get(2).resizeTexture(0.12f, 0.12f);

        for (Bird bird : birds) {
            stage.addActor(bird);
        }
    }

    private void initializeCatapult(float groundHeight) {
        float catapultY = groundHeight + 10; // Adjust the value to set the catapult above the ground
        catapult = new Catapult(world, catapultTexture, 300, catapultY);
        catapult.resizeTexture(0.4f, 0.4f);
        stage.addActor(catapult);

        // Position the first bird on top of the catapult
        Bird firstBird = birds.get(0);
        firstBird.setPosition(
            catapult.getX() + (catapult.getWidth() / 2) - (firstBird.getWidth() / 2),
            catapult.getY() + catapult.getHeight()
        );
    }


    @Override
    protected void handleCollision(Object objectA, Object objectB) {
        // Handle collisions between different game objects
        if (objectA instanceof Bird || objectB instanceof Bird) {
            handleBirdCollision(objectA, objectB);
        }
        checkGameState();
    }

    private void handleBirdCollision(Object objectA, Object objectB) {
        Bird bird = (objectA instanceof Bird) ? (Bird)objectA : (Bird)objectB;
        Object other = (objectA instanceof Bird) ? objectB : objectA;

        if (other instanceof Block) {
            Block block = (Block)other;
            block.takeDamage(bird.getDamage());
        } else if (other instanceof Pig) {
            Pig pig = (Pig)other;
            pig.takeDamage(bird.getDamage());
        }
    }

    private void checkGameState() {
        boolean allPigsDestroyed = pigs.stream().allMatch(Pig::isDestroyed);
        boolean outOfBirds = birds.stream().allMatch(Bird::isLaunched);

        if (allPigsDestroyed) {
            ((Core) Gdx.app.getApplicationListener()).setScreen(new WinScreen());
        } else if (outOfBirds && !allPigsDestroyed) {
            ((Core) Gdx.app.getApplicationListener()).setScreen(new LoseScreen());
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        redBirdTexture.dispose();
        yellowBirdTexture.dispose();
        blackBirdTexture.dispose();
        skin.dispose();
    }
}
