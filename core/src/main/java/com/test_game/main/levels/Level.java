package com.test_game.main.levels;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.Vector2;
import com.test_game.main.Catapult;
import com.test_game.main.Core;
import com.test_game.main.Ground;
import com.test_game.main.birds.Bird;
import com.test_game.main.blocks.Block;
import com.test_game.main.pigs.Pig;
import com.test_game.main.Screens.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Level extends ScreenAdapter {
    protected Stage stage;
    protected SpriteBatch batch;
    protected World world;
    protected Box2DDebugRenderer debugRenderer;
    protected static final float PPM = 100f;

    protected List<Pig> pigs;
    protected List<Block> blocks;
    protected List<Bird> birds;
    protected Catapult catapult;

    protected Texture backgroundTexture;
    protected Texture groundTexture;
    protected Ground ground;

    public Level() {
        // Initialize Box2D world with gravity
        world = new World(new Vector2(0, -9.81f), true);
        debugRenderer = new Box2DDebugRenderer();

        // Initialize basic components
        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();

        // Initialize collections
        pigs = new ArrayList<>();
        blocks = new ArrayList<>();
        birds = new ArrayList<>();

        // Set up collision detection
        setupCollisionListener();

        // Set input processor
        Gdx.input.setInputProcessor(stage);

        // Load textures
        loadTextures();

        // Create ground
        initializeGround();
    }

    protected void loadTextures() {
        backgroundTexture = new Texture("background.png"); // Update path as needed
        groundTexture = new Texture("ground.png"); // Update path as needed
    }

    protected void initializeGround() {
        float groundHeight = groundTexture.getHeight();
        ground = new Ground(groundTexture, world, 0, 0, Gdx.graphics.getWidth(), groundHeight);
        stage.addActor(ground);
    }



    protected void setupCollisionListener() {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Object userDataA = contact.getFixtureA().getBody().getUserData();
                Object userDataB = contact.getFixtureB().getBody().getUserData();
                handleCollision(userDataA, userDataB);
            }

            @Override
            public void endContact(Contact contact) {}

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {}

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {}
        });
    }

    protected abstract void handleCollision(Object objectA, Object objectB);

    protected void handleKeyboardInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            ((Core) Gdx.app.getApplicationListener()).setScreen(new WinScreen());
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            ((Core) Gdx.app.getApplicationListener()).setScreen(new LoseScreen());
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update physics world
        world.step(1/60f, 6, 2);

        // Handle input
        handleKeyboardInput();

        // Draw background and ground
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        // Update and draw stage (includes ground and other actors)
        stage.act(Math.min(delta, 1/30f));
        stage.draw();

        // Debug render physics bodies
        debugRenderer.render(world, stage.getCamera().combined.scl(PPM));
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);

        // Update ground width
        ground.updateWidth(width);

        // Update pause button position if it exists
        for (var actor : stage.getActors()) {
            if (actor instanceof TextButton && "pause".equals(actor.getName())) {
                actor.setPosition(width - 100, height - 50);
                break;
            }
        }
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        world.dispose();
        debugRenderer.dispose();
        backgroundTexture.dispose();
        groundTexture.dispose();
    }
}
