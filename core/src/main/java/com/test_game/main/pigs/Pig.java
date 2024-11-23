package com.test_game.main.pigs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.math.Vector2;

public class Pig extends Actor {
    private Texture texture;
    private float textureScaleX = 1.0f;
    private float textureScaleY = 1.0f;
    private Body body;
    private World world;
    private boolean isDestroyed = false;
    private float health = 100f;

    public Pig(World world, Texture texture, float x, float y) {
        this.world = world;
        this.texture = texture;
        setSize(texture.getWidth(), texture.getHeight());

        // Create physics body
        createBody(x, y);
    }

    private void createBody(float x, float y) {
        // Define the body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / 100f, y / 100f); // Convert to physics units

        // Create the body
        body = world.createBody(bodyDef);

        // Create a circle shape for the pig
        CircleShape circle = new CircleShape();
        circle.setRadius((getWidth() / 2) / 100f); // Convert to physics units

        // Create fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.2f;

        // Add fixture to body
        body.createFixture(fixtureDef);
        body.setUserData(this);

        // Dispose of the shape
        circle.dispose();
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
                textureScaleX, textureScaleY,
                getRotation(),
                0, 0,
                texture.getWidth(), texture.getHeight(),
                false, false
            );
        }
    }

    public void resizeTexture(float scaleX, float scaleY) {
        this.textureScaleX = scaleX;
        this.textureScaleY = scaleY;
        setSize(texture.getWidth() * textureScaleX, texture.getHeight() * textureScaleY);

        // Update physics body size if it exists
        if (body != null) {
            // Remove old fixtures
            while (body.getFixtureList().size > 0) {
                body.destroyFixture(body.getFixtureList().get(0));
            }

            // Create new fixture with updated size
            CircleShape circle = new CircleShape();
            circle.setRadius((getWidth() / 2) / 100f);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = circle;
            fixtureDef.density = 1.0f;
            fixtureDef.friction = 0.3f;
            fixtureDef.restitution = 0.2f;

            body.createFixture(fixtureDef);
            circle.dispose();
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
}
