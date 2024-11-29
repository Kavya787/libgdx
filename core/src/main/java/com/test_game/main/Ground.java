package com.test_game.main;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.physics.box2d.*;

public class Ground extends Actor {
    private Texture texture;
    private Body body;
    private World world;
    private static final float PPM = 100f;

    // Add a unique identifier for ground collision detection
    public static final String GROUND_IDENTIFIER = "GROUND";

    public Ground(Texture texture, World world, float x, float y, float width, float height) {
        this.texture = texture;
        this.world = world;
        setSize(width, height);
        setPosition(x, y);
        createPhysicsBody();
    }

    private void createPhysicsBody() {
        if (body != null) {
            world.destroyBody(body);
        }

        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(
            (getX() + getWidth() / 2) / PPM,
            (getY() + getHeight() / 2) / PPM
        );

        body = world.createBody(groundBodyDef);

        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(
            getWidth() / 2 / PPM,
            getHeight() / 2 / PPM
        );

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = groundBox;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.1f;

        Fixture fixture = body.createFixture(fixtureDef);

        // Set a unique identifier for ground collision
        fixture.setUserData(GROUND_IDENTIFIER);

        groundBox.dispose();

        body.setUserData(this);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    public void updateWidth(float width) {
        setSize(width, getHeight());
        createPhysicsBody();
    }

    public Body getBody() {
        return body;
    }

    // Optional: Method to easily check if an object is ground
    public static boolean isGround(Object obj) {
        if (obj instanceof Fixture) {
            Fixture fixture = (Fixture) obj;
            return GROUND_IDENTIFIER.equals(fixture.getUserData());
        }
        return false;
    }
}
