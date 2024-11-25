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
    private Vector2 worldCoordinates = new Vector2();
    private static final float PPM = 100f; // Pixels per meter
    private static final float DRAG_DAMPING = 2.0f;
    private Vector2 originalPosition = new Vector2();
    private Bird selectedBird = null;
    private boolean isBirdPlacedOnCatapult = false;

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

    public LevelOne() {
        super();
        loadResources();
        setupUI();
        initializeGameObjects();

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
    }

    private void loadResources() {
        redBirdTexture = new Texture(Gdx.files.internal("red_bird.png"));
        yellowBirdTexture = new Texture(Gdx.files.internal("yellow_bird.png"));
        blackBirdTexture = new Texture(Gdx.files.internal("black_bird.png"));
        catapultTexture = new Texture(Gdx.files.internal("catapult.png"));
        skin = new Skin(Gdx.files.internal("uiskin.json"));
    }

    @Override
    protected void loadTextures() {
        backgroundTexture = new Texture(Gdx.files.internal("playScreenbg.jpg"));
        groundTexture = new Texture(Gdx.files.internal("ground.png"));
    }

    @Override
    protected void handleCollision(Object objectA, Object objectB) {

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

            isDragging = false;
            activeBird = null;
            selectedBird = null;
            isBirdPlacedOnCatapult = false;

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
