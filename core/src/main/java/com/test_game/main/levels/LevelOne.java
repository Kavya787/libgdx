package com.test_game.main.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.test_game.main.Catapult;
import com.test_game.main.Core;
import com.test_game.main.Ground;
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

import java.util.ArrayList;


public class LevelOne extends Level implements InputProcessor {
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
    private Bird activeBird;  // Current bird ready to launch
    private boolean isDragging = false;
    private Vector2 dragStart = new Vector2();
    private Vector2 dragCurrent = new Vector2();
    private final float MAX_DRAG_DISTANCE = 100f; // Maximum drag distance
    private final float LAUNCH_FORCE_MULTIPLIER = 3.2f; // Adjust this to control launch speed
    private Vector2 worldCoordinates = new Vector2();
    private static final float PPM = 100f; // Pixels per meter
    private static final float DRAG_DAMPING = 2.0f;
    private Vector2 originalPosition = new Vector2();
    private Bird selectedBird = null;
    private boolean isBirdPlacedOnCatapult = false;
    private ArrayList<Block> blocksToDestroy;
    private ArrayList<Pig> pigsToDestroy;
    private ArrayList<Bird> birdsToDestroy;


    // Constants for catapult positioning and sizing
    private static final float CATAPULT_VISUAL_SCALE = 0.5f;
    private static final float CATAPULT_HITBOX_SCALE = 0;
    private static final float CATAPULT_X_OFFSET = 100;
    private static final float CATAPULT_Y_OFFSET = 30;
    private static final float RED_BIRD_VISUAL_SCALE = 0.3f;
    private static final float RED_BIRD_HITBOX_SCALE = 0.25f;
    private static final float YELLOW_BIRD_VISUAL_SCALE = 0.3f;
    private static final float YELLOW_BIRD_HITBOX_SCALE = 0.25f;
    private static final float BLACK_BIRD_VISUAL_SCALE = 0.4f;
    private static final float BLACK_BIRD_HITBOX_SCALE = 0.6f;

    private static final float WOOD_BLOCK_VISUAL_SCALE = 0.6f;
    private static final float WOOD_BLOCK_HITBOX_SCALE = 0.5f;
    private static final float GLASS_BLOCK_VISUAL_SCALE = 0.6f;
    private static final float GLASS_BLOCK_HITBOX_SCALE = 0.6f;
    private static final float STEEL_BLOCK_VISUAL_SCALE = 0.6f;
    private static final float STEEL_BLOCK_HITBOX_SCALE = 0.5f;

    // Add these constants for pig sizing
    private static final float SMALL_PIG_VISUAL_SCALE = 0.6f;
    private static final float SMALL_PIG_HITBOX_SCALE = 0.2f;
    private static final float MEDIUM_PIG_VISUAL_SCALE = 0.75f;
    private static final float MEDIUM_PIG_HITBOX_SCALE = 0.35f;
    private static final float LARGE_PIG_VISUAL_SCALE = 0.9f;
    private static final float LARGE_PIG_HITBOX_SCALE = 0.4f;


    public LevelOne() {
        super();
        loadResources();
        setupUI();
        initializeGameObjects();

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
        blocksToDestroy = new ArrayList<>();
        pigsToDestroy = new ArrayList<>();
        birdsToDestroy = new ArrayList<>();  // Initialize the new list

    }

    private void loadResources() {
        redBirdTexture = new Texture(Gdx.files.internal("red_bird.png"));
        yellowBirdTexture = new Texture(Gdx.files.internal("yellow_bird.png"));
        blackBirdTexture = new Texture(Gdx.files.internal("black_bird.png"));
        catapultTexture = new Texture(Gdx.files.internal("catapult.png"));
        SteelBlockTexture = new Texture(Gdx.files.internal("steel_block.png"));
        GlassBlockTexture = new Texture(Gdx.files.internal("glass_log.png"));
        WoodBlockTexture = new Texture(Gdx.files.internal("wood_square.png"));
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        SmallPigTexture = new Texture(Gdx.files.internal("small_pig.png"));
        MediumPigTexture = new Texture(Gdx.files.internal("medium_pig.png"));
        LargePigTexture = new Texture(Gdx.files.internal("king_pig.png"));
    }

    @Override
    protected void loadTextures() {
        backgroundTexture = new Texture(Gdx.files.internal("playScreenbg.jpg"));
        groundTexture = new Texture(Gdx.files.internal("ground.png"));
    }

    // Modify the handleCollision method to track birds for destruction
    protected void handleCollision(Object objectA, Object objectB) {

        if (objectA instanceof Fixture && Ground.isGround(objectA)) {
            handleGroundCollision(objectB);
        }
        else if (objectB instanceof Fixture && Ground.isGround(objectB)) {
            handleGroundCollision(objectA);
        }

        // Existing Bird and Block collision handling
        if (objectA instanceof Bird && objectB instanceof Block) {
            handleBirdBlockCollision((Bird) objectA, (Block) objectB);

            // Add the bird to destruction list if it's not already destroyed
            Bird bird = (Bird) objectA;
            if (!bird.isDestroyed() && !birdsToDestroy.contains(bird)) {
                birdsToDestroy.add(bird);
            }
        }
        else if (objectA instanceof Block && objectB instanceof Bird) {
            handleBirdBlockCollision((Bird) objectB, (Block) objectA);

            // Add the bird to destruction list if it's not already destroyed
            Bird bird = (Bird) objectB;
            if (!bird.isDestroyed() && !birdsToDestroy.contains(bird)) {
                birdsToDestroy.add(bird);
            }
        }

        // Bird and Pig collision handling
        if (objectA instanceof Bird && objectB instanceof Pig) {
            handleBirdPigCollision((Bird) objectA, (Pig) objectB);

            // Add the bird to destruction list if it's not already destroyed
            Bird bird = (Bird) objectA;
            if (!bird.isDestroyed() && !birdsToDestroy.contains(bird)) {
                birdsToDestroy.add(bird);
            }
        }
        else if (objectA instanceof Pig && objectB instanceof Bird) {
            handleBirdPigCollision((Bird) objectB, (Pig) objectA);

            // Add the bird to destruction list if it's not already destroyed
            Bird bird = (Bird) objectB;
            if (!bird.isDestroyed() && !birdsToDestroy.contains(bird)) {
                birdsToDestroy.add(bird);
            }
        }
    }

    private void handleGroundCollision(Object obj) {
        if (obj instanceof Block) {
            Block block = (Block) obj;
            if (!block.isDestroyed()) {
                block.setHealth(block.getHealth() - 10);

                if (block.getHealth() <= 0 && !block.isDestroyed()) {
                    blocksToDestroy.add(block);
                }
            }
        } else if (obj instanceof Pig) {
            Pig pig = (Pig) obj;
            if (!pig.isDestroyed()) {
                pig.setHealth(pig.getHealth() - 10);

                if (pig.getHealth() <= 0 && !pig.isDestroyed()) {
                    pigsToDestroy.add(pig);
                }
            }
        }
    }

    private void handleBirdPigCollision(Bird bird, Pig pig) {
        // Only process if neither object is destroyed
        if (!bird.isDestroyed() && !pig.isDestroyed()) {
            // Reduce pig health
            pig.setHealth(pig.getHealth() - bird.getDamage());

            // If health is depleted, mark for destruction
            if (pig.getHealth() <= 0 && !pig.isDestroyed()) {
                pigsToDestroy.add(pig);
            }
        }
    }

    private void handleBirdBlockCollision(Bird bird, Block block) {
        // Only process if neither object is destroyed
        if (!bird.isDestroyed() && !block.isDestroyed()) {
            // Reduce block health
            block.setHealth(block.getHealth() - bird.getDamage());

            // If health is depleted, mark for destruction
            if (block.getHealth() <= 0 && !block.isDestroyed()) {
                blocksToDestroy.add(block);
            }
        }
    }

    public void update(float delta) {
        // Update physics
        world.step(delta, 6, 2);

        // After physics step, handle destroyed objects
        for (Block block : blocksToDestroy) {
            block.destroy();
        }
        blocksToDestroy.clear();

        for (Pig pig : pigsToDestroy) {
            pig.destroy();
        }
        pigsToDestroy.clear();

        // Handle bird destruction
        for (Bird bird : birdsToDestroy) {
            bird.destroy();
        }
        birdsToDestroy.clear();
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
        initializeBirds(groundHeight);
        initializeCatapult(groundHeight);
        createStructures(groundHeight);
        spawnPigs(groundHeight);


    }


    private void spawnPigs(float groundHeight) {
        // Same structure X as in createStructures method
        float structureX = Gdx.graphics.getWidth() * 0.7f;
        float baseY = groundHeight;

        // Determine block heights and spacing
        float woodBlockHeight = WoodBlockTexture.getHeight() * WOOD_BLOCK_VISUAL_SCALE;
        float glassBlockHeight = GlassBlockTexture.getHeight() * GLASS_BLOCK_VISUAL_SCALE;
        float steelBlockHeight = SteelBlockTexture.getHeight() * STEEL_BLOCK_VISUAL_SCALE;

        // Pig placement coordinates
        // Base layer (on top of wood blocks)
        float basePigY = baseY + woodBlockHeight;

        // Middle layer (on top of glass blocks)
        float middlePigY = baseY + woodBlockHeight + glassBlockHeight;

        // Top layer (on top of steel block)
        float topPigY = baseY + woodBlockHeight + glassBlockHeight + steelBlockHeight;

        // Spawn pigs with precise vertical positioning
        spawnBasePigs(structureX, basePigY);
        spawnMiddlePigs(structureX, middlePigY);
        spawnTopPig(structureX, topPigY);
    }

    private void spawnBasePigs(float startX, float baseY) {
        // Add two small pigs on the base layer
        float pigSpacing = 60f;

        SmallPig smallPig1 = new SmallPig(world, SmallPigTexture, startX - 90 , ground.getHeight()+15);
        configurePig(smallPig1, SMALL_PIG_VISUAL_SCALE, SMALL_PIG_HITBOX_SCALE);
        stabilizePig(smallPig1);  // Add this line
        pigs.add(smallPig1);
        stage.addActor(smallPig1);

        SmallPig smallPig2 = new SmallPig(world, SmallPigTexture, startX + pigSpacing + 200, ground.getHeight()+15);
        configurePig(smallPig2, SMALL_PIG_VISUAL_SCALE, SMALL_PIG_HITBOX_SCALE);
        stabilizePig(smallPig2);  // Add this line

        pigs.add(smallPig2);
        stage.addActor(smallPig2);
    }

    private void spawnMiddlePigs(float startX, float baseY) {
        // Add a medium pig in the middle layer
        MediumPig mediumPig = new MediumPig(world, MediumPigTexture, startX+60, baseY -30);
        configurePig(mediumPig, MEDIUM_PIG_VISUAL_SCALE, MEDIUM_PIG_HITBOX_SCALE);
        stabilizePig(mediumPig);  // Add this line

        pigs.add(mediumPig);
        stage.addActor(mediumPig);
    }

    private void spawnTopPig(float startX, float baseY) {
        // Add a large pig on top
        LargePig largePig = new LargePig(world, LargePigTexture, startX + 125, baseY-125 );
        configurePig(largePig, LARGE_PIG_VISUAL_SCALE, LARGE_PIG_HITBOX_SCALE);
        stabilizePig(largePig);  // Add this line
        pigs.add(largePig);
        stage.addActor(largePig);
    }

    private void configurePig(Pig pig, float visualScale, float hitboxScale) {
        float visualWidth = pig.getWidth() * visualScale;
        float visualHeight = pig.getHeight() * visualScale;
        pig.setVisualSize(visualWidth, visualHeight);
        pig.setHitboxScale(hitboxScale, hitboxScale);
    }
    private void createStructures(float groundHeight) {
        // Create a simple structure on the right side of the screen
        float structureX = Gdx.graphics.getWidth() * 0.7f; // 70% of screen width
        float baseY = groundHeight + 5f;

        // Create base layer
        createWoodBase(structureX, baseY);

        // Create middle layer - adjusted to be centered
        createGlassLayer(structureX , baseY + 50);

        // Create top layer
        createSteelTop(structureX, baseY + 100);
    }

    private void createWoodBase(float startX, float startY) {
        // Create three wood blocks as base
        float blockSpacing = WoodBlockTexture.getWidth() * WOOD_BLOCK_VISUAL_SCALE;
        float totalWidth = blockSpacing * 3; // Total width of the base

        for (int i = 0; i < 3; i++) {
            WoodBlock woodBlock = new WoodBlock(world, WoodBlockTexture,
                startX + (i * blockSpacing), startY);
            configureBlock(woodBlock, WOOD_BLOCK_VISUAL_SCALE, WOOD_BLOCK_HITBOX_SCALE);
            blocks.add(woodBlock);
            stage.addActor(woodBlock);
        }
    }

    private void createGlassLayer(float startX, float startY) {
        // Calculate widths for centering
        float woodBlockWidth = WoodBlockTexture.getWidth() * WOOD_BLOCK_VISUAL_SCALE;
        float glassBlockWidth = GlassBlockTexture.getWidth() * GLASS_BLOCK_VISUAL_SCALE;
        float glassHitboxWidth = GlassBlockTexture.getWidth() * GLASS_BLOCK_HITBOX_SCALE;

        // Calculate the total width of wood base (3 blocks)
        float woodBaseWidth = woodBlockWidth * 3;

        // Calculate the total width of glass layer (2 blocks)
        float glassLayerWidth = glassBlockWidth + glassHitboxWidth; // Total width is one visual width plus one hitbox width

        // Calculate the offset needed to center the glass layer
        float centeringOffset = (woodBaseWidth - glassLayerWidth) / 2;

        // Position first glass block
        GlassBlock glassBlock1 = new GlassBlock(world, GlassBlockTexture,
            startX + centeringOffset + 70, startY);
        configureBlock(glassBlock1, GLASS_BLOCK_VISUAL_SCALE, GLASS_BLOCK_HITBOX_SCALE);
        blocks.add(glassBlock1);
        stage.addActor(glassBlock1);

        // Position second glass block directly adjacent to the first
        GlassBlock glassBlock2 = new GlassBlock(world, GlassBlockTexture,
            startX + centeringOffset + glassHitboxWidth, startY);
        configureBlock(glassBlock2, GLASS_BLOCK_VISUAL_SCALE, GLASS_BLOCK_HITBOX_SCALE);
        blocks.add(glassBlock2);
        stage.addActor(glassBlock2);
    }

    private void createSteelTop(float startX, float startY) {
        // Calculate widths for centering
        float woodBlockWidth = WoodBlockTexture.getWidth() * WOOD_BLOCK_VISUAL_SCALE;
        float steelBlockWidth = SteelBlockTexture.getWidth() * STEEL_BLOCK_VISUAL_SCALE;

        // Calculate the total width of wood base (3 blocks)
        float woodBaseWidth = woodBlockWidth * 3;

        // Calculate the offset needed to center the steel blocks
        float centeringOffset = (woodBaseWidth - (steelBlockWidth * 2)) / 2;

        // Create first steel block
        SteelBlock steelBlock1 = new SteelBlock(world, SteelBlockTexture,
            startX + centeringOffset - 50, startY);
        configureBlock(steelBlock1, STEEL_BLOCK_VISUAL_SCALE, STEEL_BLOCK_HITBOX_SCALE);
        blocks.add(steelBlock1);
        stage.addActor(steelBlock1);

        // Create second steel block adjacent to the first
        SteelBlock steelBlock2 = new SteelBlock(world, SteelBlockTexture,
            startX + centeringOffset + steelBlockWidth + 40, startY);
        configureBlock(steelBlock2, STEEL_BLOCK_VISUAL_SCALE, STEEL_BLOCK_HITBOX_SCALE);
        blocks.add(steelBlock2);
        stage.addActor(steelBlock2);
    }

    private void configureBlock(Block block, float visualScale, float hitboxScale) {
        float visualWidth = block.texture.getWidth() * visualScale;
        float visualHeight = block.texture.getHeight() * visualScale;
        block.setVisualSize(visualWidth, visualHeight);
        block.setHitboxScale(hitboxScale, hitboxScale);
    }

    private void initializeCatapult(float groundHeight) {
        float catapultY = groundHeight + CATAPULT_Y_OFFSET;
        catapult = new Catapult(world, catapultTexture, CATAPULT_X_OFFSET, catapultY);
        catapult.setVisualSize(
            catapultTexture.getWidth() * CATAPULT_VISUAL_SCALE,
            catapultTexture.getHeight() * CATAPULT_VISUAL_SCALE
        );
        catapult.setHitboxScale(CATAPULT_HITBOX_SCALE, CATAPULT_HITBOX_SCALE);
        stage.addActor(catapult);
    }
    private void stabilizePig(Pig pig) {
        if (pig.getBody() != null) {
            // Set gravity scale to 0 to prevent immediate falling
            pig.getBody().setGravityScale(0);

            // Stop any existing movement
            pig.getBody().setLinearVelocity(0, 0);
            pig.getBody().setAngularVelocity(0);

            // Make the body inactive to prevent physics interactions initially
            pig.getBody().setActive(false);

            // Optional: Add a slight delay to reactivate physics
            // This ensures the pig is placed correctly before physics takes over
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    pig.getBody().setGravityScale(1);
                    pig.getBody().setActive(true);
                }
            }, 0f);  // 0.1 second delay
        }
    }

    private void initializeBirds(float groundHeight) {
        float spacing = 40f;
        float startX = CATAPULT_X_OFFSET - 50;
        float birdY = groundHeight + 5f;

        Bird redBird = new RedBird(world, redBirdTexture, startX, birdY);
        Bird yellowBird = new YellowBird(world, yellowBirdTexture, startX + spacing, birdY);
        Bird blackBird = new BlackBird(world, blackBirdTexture, startX + spacing * 2, birdY);

        configureRedBird(redBird);
        configureYellowBird(yellowBird);
        configureBlackBird(blackBird);

        birds.add(redBird);
        birds.add(yellowBird);
        birds.add(blackBird);

        for (Bird bird : birds) {
            stage.addActor(bird);
        }
    }

    private void placeBirdOnCatapult(Bird bird) {
        float catapultX = catapult.getX() + (catapult.getWidth() * 0.5f);
        float catapultY = catapult.getY() + (catapult.getHeight() * 0.75f);

        if (bird.getBody() != null) {
            bird.getBody().setGravityScale(0);
            bird.getBody().setTransform(catapultX / PPM, catapultY / PPM, 0);
            bird.getBody().setLinearVelocity(0, 0);
            bird.getBody().setAngularVelocity(0);
            bird.getBody().setAwake(true);
            bird.getBody().setActive(true);
            bird.getBody().setLinearDamping(10f);
        }

        bird.setPosition(catapultX - bird.getWidth()/2, catapultY - bird.getHeight()/2);
        selectedBird = bird;
        isBirdPlacedOnCatapult = true;
    }

    private void configureRedBird(Bird bird) {
        float visualWidth = redBirdTexture.getWidth() * RED_BIRD_VISUAL_SCALE;
        float visualHeight = redBirdTexture.getHeight() * RED_BIRD_VISUAL_SCALE;
        bird.setVisualSize(visualWidth, visualHeight);
        bird.setHitboxScale(RED_BIRD_HITBOX_SCALE, RED_BIRD_HITBOX_SCALE);
    }

    private void configureYellowBird(Bird bird) {
        float visualWidth = yellowBirdTexture.getWidth() * YELLOW_BIRD_VISUAL_SCALE;
        float visualHeight = yellowBirdTexture.getHeight() * YELLOW_BIRD_VISUAL_SCALE;
        bird.setVisualSize(visualWidth, visualHeight);
        bird.setHitboxScale(YELLOW_BIRD_HITBOX_SCALE, YELLOW_BIRD_HITBOX_SCALE);
    }

    private void configureBlackBird(Bird bird) {
        float visualWidth = blackBirdTexture.getWidth() * BLACK_BIRD_VISUAL_SCALE;
        float visualHeight = blackBirdTexture.getHeight() * BLACK_BIRD_VISUAL_SCALE;
        bird.setVisualSize(visualWidth, visualHeight);
        bird.setHitboxScale(BLACK_BIRD_HITBOX_SCALE, BLACK_BIRD_HITBOX_SCALE);
    }

    @Override
    public boolean keyDown(int i) { return false; }

    @Override
    public boolean keyUp(int i) { return false; }

    @Override
    public boolean keyTyped(char c) { return false; }

    private void autoSelectNextBird() {
        // Use LibGDX's Timer to delay bird selection
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                // Find the first non-launched bird and place it on the catapult
                for (Bird bird : birds) {
                    if (!bird.isLaunched()) {
                        placeBirdOnCatapult(bird);
                        break;
                    }
                }
            }
        }, 1.5f);  // 2 seconds delay
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 worldPos = screenToWorld(screenX, screenY);

        if (isBirdPlacedOnCatapult && selectedBird != null) {
            Rectangle birdBounds = new Rectangle(
                selectedBird.getX() - selectedBird.getWidth()/2,
                selectedBird.getY() - selectedBird.getHeight()/2,
                selectedBird.getWidth(),
                selectedBird.getHeight()
            );

            if (birdBounds.contains(worldPos.x, worldPos.y)) {
                isDragging = true;
                activeBird = selectedBird;
                dragStart.set(worldPos.x, worldPos.y);
                dragCurrent.set(worldPos.x, worldPos.y);
                originalPosition.set(selectedBird.getX(), selectedBird.getY());

                if (selectedBird.getBody() != null) {
                    selectedBird.getBody().setLinearVelocity(0, 0);
                    selectedBird.getBody().setAngularVelocity(0);
                    selectedBird.getBody().setGravityScale(0);
                    selectedBird.getBody().setLinearDamping(DRAG_DAMPING);
                }
                return true;
            }
        }
        else {
            for (Bird bird : birds) {
                if (!bird.isLaunched()) {
                    Rectangle birdBounds = new Rectangle(
                        bird.getX() - bird.getWidth()/2,
                        bird.getY() - bird.getHeight()/2,
                        bird.getWidth(),
                        bird.getHeight()
                    );

                    if (birdBounds.contains(worldPos.x, worldPos.y)) {
                        placeBirdOnCatapult(bird);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (isDragging && activeBird != null && isBirdPlacedOnCatapult) {
            Vector2 worldPos = screenToWorld(screenX, screenY);
            dragCurrent.set(worldPos.x, worldPos.y);

            Vector2 catapultPos = new Vector2(
                catapult.getX() + catapult.getWidth() * 0.5f,
                catapult.getY() + catapult.getHeight() * 0.75f
            );
            Vector2 dragVector = new Vector2(dragCurrent).sub(catapultPos);

            if (dragVector.len() > MAX_DRAG_DISTANCE) {
                dragVector.nor().scl(MAX_DRAG_DISTANCE);
                dragCurrent.set(catapultPos).add(dragVector);
            }

            if (activeBird.getBody() != null) {
                activeBird.getBody().setTransform(
                    dragCurrent.x / PPM,
                    dragCurrent.y / PPM,
                    activeBird.getBody().getAngle()
                );
                activeBird.getBody().setAwake(true);
                activeBird.getBody().setLinearVelocity(0, 0);
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (isDragging && activeBird != null && isBirdPlacedOnCatapult) {
            Vector2 catapultPos = new Vector2(
                catapult.getX() + catapult.getWidth() * 0.5f,
                catapult.getY() + catapult.getHeight() * 0.75f
            );
            Vector2 dragVector = new Vector2(dragCurrent).sub(catapultPos);
            Vector2 launchVector = dragVector.scl(-1);

            float dragDistance = launchVector.len();
            float forceFactor = Math.min(dragDistance / MAX_DRAG_DISTANCE, 1.0f);
            Vector2 launchForce = launchVector.nor().scl(LAUNCH_FORCE_MULTIPLIER * forceFactor);

            activeBird.getBody().setGravityScale(1);
            activeBird.getBody().setLinearDamping(0);
            activeBird.launch(launchForce.x, launchForce.y);

            // Automatically select the next bird
            autoSelectNextBird();

            isDragging = false;
            activeBird = null;

            return true;
        }

        if (isDragging && activeBird != null) {
            if (activeBird.getBody() != null) {
                activeBird.getBody().setGravityScale(0);
            }
            placeBirdOnCatapult(activeBird);
            isDragging = false;
            activeBird = null;
        }

        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) { return false; }

    @Override
    public boolean scrolled(float v, float v1) { return false; }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) { return false; }

    private Vector2 screenToWorld(float screenX, float screenY) {
        worldCoordinates.set(screenX, Gdx.graphics.getHeight() - screenY);
        return worldCoordinates;
    }

    @Override
    public void show() {
        super.show();
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);

    }



    @Override
    public void dispose() {
        super.dispose();
        redBirdTexture.dispose();
        yellowBirdTexture.dispose();
        blackBirdTexture.dispose();
        skin.dispose();
        SmallPigTexture.dispose();
        MediumPigTexture.dispose();
        LargePigTexture.dispose();
        SteelBlockTexture.dispose();
        GlassBlockTexture.dispose();
        WoodBlockTexture.dispose();

    }
}
