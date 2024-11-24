package com.test_game.main.pigs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Pig extends Actor {
    private static final float PPM = 100f;

    private Texture texture;
    private float textureScaleX = 1.0f;
    private float textureScaleY = 1.0f;
    private float hitboxScaleX = 1.0f;
    private float hitboxScaleY = 1.0f;
    private Body body;
    private World world;
    private boolean isDestroyed = false;
    private float health = 100f;
    private float initialX, initialY;

    public Pig(World world, Texture texture, float x, float y) {
        this.world = world;
        this.texture = texture;
        this.initialX = x;
        this.initialY = y;
        setSize(texture.getWidth(), texture.getHeight());

        setDefaultSize();
        createBody(x, y);
    }

    private void createBody(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / PPM, y / PPM);

        body = world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius((getWidth() * hitboxScaleX / 2) / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.2f;

        body.createFixture(fixtureDef);
        body.setUserData(this);

        circle.dispose();
    }

    public void setVisualSize(float width, float height) {
        setSize(width, height);
        this.textureScaleX = width / texture.getWidth();
        this.textureScaleY = height / texture.getHeight();
        updatePhysicsBody();
    }

    public void setHitboxScale(float scaleX, float scaleY) {
        this.hitboxScaleX = scaleX;
        this.hitboxScaleY = scaleY;
        updatePhysicsBody();
    }

    public void setDefaultSize() {
        setVisualSize(texture.getWidth() * 0.5f, texture.getHeight() * 0.5f);
        setHitboxScale(0.8f, 0.8f);
    }

    private void updatePhysicsBody() {
        if (body != null) {
            Vector2 position = body.getPosition().cpy();
            float angle = body.getAngle();

            Array<Fixture> fixtures = body.getFixtureList();
            while (fixtures.size > 0) {
                body.destroyFixture(fixtures.get(0));
            }

            CircleShape circle = new CircleShape();
            circle.setRadius((getWidth() * hitboxScaleX / 2) / PPM);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = circle;
            fixtureDef.density = 1.0f;
            fixtureDef.friction = 0.3f;
            fixtureDef.restitution = 0.2f;

            body.createFixture(fixtureDef);
            circle.dispose();

            body.setTransform(position, angle);
        }
    }

    public void setAbsolutePosition(float x, float y) {
        this.initialX = x;
        this.initialY = y;
        setPosition(x, y);
        if (body != null) {
            body.setTransform(
                (x + getWidth()/2) / PPM,
                (y + getHeight()/2) / PPM,
                body.getAngle()
            );
        }
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
