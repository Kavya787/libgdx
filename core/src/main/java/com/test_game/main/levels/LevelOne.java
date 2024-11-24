package com.test_game.main.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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
    private final float LAUNCH_FORCE_MULTIPLIER = 5f; // Adjust this to control launch speed
    private Vector2 worldCoordinates = new Vector2(); // Add this field
    private static final float PPM = 100f; // Pixels per meter
    private static final float DRAG_DAMPING = 2.0f; // Add damping to reduce oscillation
    private Vector2 originalPosition = new Vector2(); // Store original bird position


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

    // Constants for pigs
    private static final float SMALL_PIG_VISUAL_SCALE = 0.3f;
    private static final float SMALL_PIG_HITBOX_SCALE = 0.4f;
    private static final float MEDIUM_PIG_VISUAL_SCALE = 0.4f;
    private static final float MEDIUM_PIG_HITBOX_SCALE = 0.5f;
    private static final float LARGE_PIG_VISUAL_SCALE = 0.5f;
    private static final float LARGE_PIG_HITBOX_SCALE = 0.6f;


    // New constants for blocks
    private static final float STEEL_BLOCK_VISUAL_SCALE = 1f;
    private static final float STEEL_BLOCK_HITBOX_SCALE = 0.3f;
    private static final float GLASS_BLOCK_VISUAL_SCALE = 0.4f;
    private static final float GLASS_BLOCK_HITBOX_SCALE = 0.9f;
    private static final float WOOD_BLOCK_VISUAL_SCALE = 0.4f;
    private static final float WOOD_BLOCK_HITBOX_SCALE = 0.9f;

    public LevelOne() {
        super();
        loadResources();
        setupUI();
        initializeGameObjects();

        // Set up input processing
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);  // Stage needs to handle UI input
        multiplexer.addProcessor(this);   // This class handles game input
        Gdx.input.setInputProcessor(multiplexer);

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
        // Create blocks with specific positions
        Block steelBlock1 = new SteelBlock(world, SteelBlockTexture, 600, groundHeight);
        Block glassBlock = new GlassBlock(world, GlassBlockTexture, 475, groundHeight);
        Block steelBlock2 = new SteelBlock(world, SteelBlockTexture, 625, groundHeight);
        Block woodBlock = new WoodBlock(world, WoodBlockTexture, 350, 115);

        // Configure each block
        configureSteelBlock(steelBlock1);
        configureGlassBlock(glassBlock);
        configureSteelBlock(steelBlock2);
        configureWoodBlock(woodBlock);

        // Add blocks to the list
        blocks.add(steelBlock1);
        blocks.add(glassBlock);
        blocks.add(steelBlock2);
        blocks.add(woodBlock);

        // Add blocks to the stage
        for (Block block : blocks) {
            stage.addActor(block);
        }
    }

    private void configureSteelBlock(Block block) {
        float visualWidth = SteelBlockTexture.getWidth() * STEEL_BLOCK_VISUAL_SCALE;
        float visualHeight = SteelBlockTexture.getHeight() * STEEL_BLOCK_VISUAL_SCALE;
        block.setVisualSize(visualWidth, visualHeight);
        block.setHitboxScale(STEEL_BLOCK_HITBOX_SCALE, STEEL_BLOCK_HITBOX_SCALE);
    }

    private void configureGlassBlock(Block block) {
        float visualWidth = GlassBlockTexture.getWidth() * GLASS_BLOCK_VISUAL_SCALE;
        float visualHeight = GlassBlockTexture.getHeight() * GLASS_BLOCK_VISUAL_SCALE;
        block.setVisualSize(visualWidth, visualHeight);
        block.setHitboxScale(GLASS_BLOCK_HITBOX_SCALE, GLASS_BLOCK_HITBOX_SCALE);
    }

    private void configureWoodBlock(Block block) {
        float visualWidth = WoodBlockTexture.getWidth() * WOOD_BLOCK_VISUAL_SCALE;
        float visualHeight = WoodBlockTexture.getHeight() * WOOD_BLOCK_VISUAL_SCALE;
        block.setVisualSize(visualWidth, visualHeight);
        block.setHitboxScale(WOOD_BLOCK_HITBOX_SCALE, WOOD_BLOCK_HITBOX_SCALE);
    }


    private void initializePigs() {
        // Create pigs
        Pig smallPig = new SmallPig(world, SmallPigTexture,
            blocks.get(0).getX() - 8,
            blocks.get(0).getY() + blocks.get(0).getHeight() - 5);
        Pig mediumPig = new MediumPig(world, MediumPigTexture,
            blocks.get(1).getX() - 2,
            blocks.get(1).getY() + blocks.get(1).getHeight() + 18);
        Pig largePig = new LargePig(world, LargePigTexture,
            blocks.get(2).getX() + 2,
            blocks.get(2).getY() + blocks.get(2).getHeight() - 10);

        // Configure each pig
        configureSmallPig(smallPig);
        configureMediumPig(mediumPig);
        configureLargePig(largePig);

        // Add pigs to the list
        pigs.add(smallPig);
        pigs.add(mediumPig);
        pigs.add(largePig);

        // Add pigs to the stage
        for (Pig pig : pigs) {
            stage.addActor(pig);
        }
    }

    private void configureSmallPig(Pig pig) {
        float visualWidth = SmallPigTexture.getWidth() * SMALL_PIG_VISUAL_SCALE;
        float visualHeight = SmallPigTexture.getHeight() * SMALL_PIG_VISUAL_SCALE;
        pig.setVisualSize(visualWidth, visualHeight);
        pig.setHitboxScale(SMALL_PIG_HITBOX_SCALE, SMALL_PIG_HITBOX_SCALE);
    }

    private void configureMediumPig(Pig pig) {
        float visualWidth = MediumPigTexture.getWidth() * MEDIUM_PIG_VISUAL_SCALE;
        float visualHeight = MediumPigTexture.getHeight() * MEDIUM_PIG_VISUAL_SCALE;
        pig.setVisualSize(visualWidth, visualHeight);
        pig.setHitboxScale(MEDIUM_PIG_HITBOX_SCALE, MEDIUM_PIG_HITBOX_SCALE);
    }

    private void configureLargePig(Pig pig) {
        float visualWidth = LargePigTexture.getWidth() * LARGE_PIG_VISUAL_SCALE;
        float visualHeight = LargePigTexture.getHeight() * LARGE_PIG_VISUAL_SCALE;
        pig.setVisualSize(visualWidth, visualHeight);
        pig.setHitboxScale(LARGE_PIG_HITBOX_SCALE, LARGE_PIG_HITBOX_SCALE);
    }
    private void initializeCatapult(float groundHeight) {
        float catapultY = groundHeight + CATAPULT_Y_OFFSET;

        // Create the catapult
        catapult = new Catapult(world, catapultTexture, CATAPULT_X_OFFSET, catapultY);

        // Set the visual size
        catapult.setVisualSize(
            catapultTexture.getWidth() * CATAPULT_VISUAL_SCALE,
            catapultTexture.getHeight() * CATAPULT_VISUAL_SCALE
        );

        // Set the hitbox size
        catapult.setHitboxScale(CATAPULT_HITBOX_SCALE, CATAPULT_HITBOX_SCALE);

        // Add to stage
        stage.addActor(catapult);
    }

    private void initializeBirds(float groundHeight) {
        float birdX = CATAPULT_X_OFFSET;  // Same X offset as catapult
        float birdY = CATAPULT_Y_OFFSET + (catapultTexture.getHeight() * CATAPULT_VISUAL_SCALE);

        // Create first bird directly on catapult position
        Bird redBird = new RedBird(world, redBirdTexture, birdX, birdY);
        configureRedBird(redBird);

        // Calculate starting position for remaining birds
        float spacing = 20f;
        float startX = CATAPULT_X_OFFSET + (catapultTexture.getWidth() * CATAPULT_VISUAL_SCALE) + spacing;

        // Create remaining birds on the ground
        Bird yellowBird = new YellowBird(world, yellowBirdTexture, startX, groundHeight + 5f);
        Bird blackBird = new BlackBird(world, blackBirdTexture, startX + yellowBirdTexture.getWidth() * YELLOW_BIRD_VISUAL_SCALE + spacing, groundHeight + 5f);

        configureYellowBird(yellowBird);
        configureBlackBird(blackBird);

        // Add birds to the list
        birds.add(redBird);
        birds.add(yellowBird);
        birds.add(blackBird);

        // Add birds to the stage
        for (Bird bird : birds) {
            stage.addActor(bird);
        }
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



    // New method to position all birds
    private void positionBirds(float groundHeight) {
        if (birds.isEmpty()) return;

        // Position first bird aligned with catapult
        Bird firstBird = birds.get(0);
        float birdX = catapult.getX(); // Directly use catapult's X coordinate
        float birdY = catapult.getY() + catapult.getHeight();
        firstBird.setAbsolutePosition(birdX, birdY);

        // Position remaining birds on the ground
        float spacing = 20f;
        float startX = catapult.getX() + catapult.getWidth() + spacing;

        for (int i = 1; i < birds.size(); i++) {
            Bird bird = birds.get(i);
            float xPosition = startX + ((i - 1) * (bird.getWidth() + spacing));
            float yPosition = groundHeight + 5f;
            bird.setAbsolutePosition(xPosition, yPosition);
        }
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

    // Add this method to handle input processing

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 worldPos = screenToWorld(screenX, screenY);

        if (!birds.isEmpty()) {
            Bird firstUnlaunchedBird = null;
            for (Bird bird : birds) {
                if (!bird.isLaunched()) {
                    firstUnlaunchedBird = bird;
                    break;
                }
            }

            if (firstUnlaunchedBird != null) {
                Rectangle birdBounds = new Rectangle(
                    firstUnlaunchedBird.getX() - firstUnlaunchedBird.getWidth()/2,
                    firstUnlaunchedBird.getY() - firstUnlaunchedBird.getHeight()/2,
                    firstUnlaunchedBird.getWidth(),
                    firstUnlaunchedBird.getHeight()
                );

                if (birdBounds.contains(worldPos.x, worldPos.y)) {
                    isDragging = true;
                    activeBird = firstUnlaunchedBird;
                    dragStart.set(worldPos.x, worldPos.y);
                    dragCurrent.set(worldPos.x, worldPos.y);

                    // Store the original position when starting drag
                    originalPosition.set(activeBird.getX(), activeBird.getY());

                    // Configure physics body for dragging
                    if (activeBird.getBody() != null) {
                        activeBird.getBody().setLinearVelocity(0, 0);
                        activeBird.getBody().setAngularVelocity(0);
                        activeBird.getBody().setGravityScale(0);
                        activeBird.getBody().setLinearDamping(DRAG_DAMPING);
                        activeBird.getBody().setAwake(true);
                        activeBird.getBody().setActive(true);
                    }

                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (isDragging && activeBird != null) {
            Vector2 worldPos = screenToWorld(screenX, screenY);
            dragCurrent.set(worldPos.x, worldPos.y);

            // Calculate drag vector from drag start
            Vector2 dragVector = new Vector2(dragCurrent).sub(dragStart);

            // Limit drag distance if needed
            if (dragVector.len() > MAX_DRAG_DISTANCE) {
                dragVector.nor().scl(MAX_DRAG_DISTANCE);
                dragCurrent.set(dragStart).add(dragVector);
            }

            // Update bird position to follow mouse
            if (activeBird.getBody() != null) {
                // Update physics body position to match mouse position
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
        if (isDragging && activeBird != null) {
            // Calculate launch vector (opposite of drag direction for slingshot effect)
            Vector2 dragVector = new Vector2(dragCurrent).sub(dragStart);
            Vector2 launchVector = dragVector.scl(-1); // Reverse the direction for slingshot effect

            // Scale the launch force based on drag distance
            float dragDistance = launchVector.len();
            float forceFactor = Math.min(dragDistance / MAX_DRAG_DISTANCE, 1.0f);
            Vector2 launchForce = launchVector.nor().scl(LAUNCH_FORCE_MULTIPLIER * forceFactor);

            // Reset physics properties before launch
            activeBird.getBody().setGravityScale(1);
            activeBird.getBody().setLinearDamping(0);

            // Apply launch force in the opposite direction of drag
            activeBird.launch(launchForce.x, launchForce.y);

            // Reset dragging state
            isDragging = false;
            activeBird = null;

            return true;
        }
        return false;
    }
    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }



    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }
    private Vector2 screenToWorld(float screenX, float screenY) {
        // Convert screen coordinates to world coordinates
        worldCoordinates.set(screenX, Gdx.graphics.getHeight() - screenY);
        // Optional: Apply camera transform if you're using a camera
        // camera.unproject(worldCoordinates);
        return worldCoordinates;
    }


    @Override
    public void show() {
        super.show();
        // Make sure input processor is set up correctly
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this);   // Add this class's input processor first
        multiplexer.addProcessor(stage);  // Add stage processor second
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        // Debug drawing for drag line (optional)
        if (isDragging && activeBird != null) {
            // You can add debug rendering here if needed
            // shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            // shapeRenderer.line(dragStart.x, dragStart.y, dragCurrent.x, dragCurrent.y);
            // shapeRenderer.end();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        redBirdTexture.dispose();
        yellowBirdTexture.dispose();
        blackBirdTexture.dispose();
        SmallPigTexture.dispose();
        MediumPigTexture.dispose();
        LargePigTexture.dispose();
        skin.dispose();
    }
}
