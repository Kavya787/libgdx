package com.test_game.main.blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.math.Vector2;

public class Block extends Actor {
    protected Texture texture;
    protected Body body;
    protected World world;
    protected boolean isDestroyed = false;
    protected float health;
    protected float density;
    protected float friction;
    protected float restitution;

    public Block(World world, Texture texture, float x, float y) {
        this.world = world;
        this.texture = texture;
        setSize(texture.getWidth(), texture.getHeight());

        // Default physics properties
        this.density = 1.0f;
        this.friction = 0.4f;
        this.restitution = 0.1f;
        this.health = 100f;

        createBody(x, y);
    }

    protected void createBody(float x, float y) {
        // Define the body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / 100f, y / 100f); // Convert to physics units

        // Create the body
        body = world.createBody(bodyDef);

        // Create a box shape
        PolygonShape box = new PolygonShape();
        box.setAsBox(
            (getWidth() / 2) / 100f,  // half-width, convert to physics units
            (getHeight() / 2) / 100f   // half-height, convert to physics units
        );

        // Create fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;

        // Add fixture to body
        body.createFixture(fixtureDef);
        body.setUserData(this);

        // Dispose of the shape
        box.dispose();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (body != null) {
            // Update actor position to match physics body
            Vector2 position = body.getPosition();
            setPosition(
                position.x * 100f - getWidth() / 2,  // Convert back from physics units
                position.y * 100f - getHeight() / 2
            );
            setRotation((float) Math.toDegrees(body.getAngle()));
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!isDestroyed) {
            batch.draw(
                texture,
                getX(), getY(),
                getWidth() / 2, getHeight() / 2,  // Origin for rotation
                getWidth(), getHeight(),
                1, 1,  // Scale
                getRotation(),
                0, 0,
                texture.getWidth(), texture.getHeight(),
                false, false
            );
        }
    }

    public void takeDamage(float damage) {
        health -= damage;
        if (health <= 0 && !isDestroyed) {
            destroy();
        }
    }

    public void destroy() {
        isDestroyed = true;
        if (body != null) {
            world.destroyBody(body);
            body = null;
        }
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }



    public void dispose() {
        if (body != null) {
            world.destroyBody(body);
            body = null;
        }
        if (texture != null) {
            texture.dispose();
        }
    }

    // Getters and setters for physics properties
    public void setPhysicsProperties(float density, float friction, float restitution) {
        this.density = density;
        this.friction = friction;
        this.restitution = restitution;

        // Update fixture if body exists
        if (body != null && body.getFixtureList().size > 0) {
            Fixture fixture = body.getFixtureList().get(0);
            fixture.setDensity(density);
            fixture.setFriction(friction);
            fixture.setRestitution(restitution);
            body.resetMassData();
        }
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getHealth() {
        return health;
    }
}
