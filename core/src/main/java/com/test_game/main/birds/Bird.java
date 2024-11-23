package com.test_game.main.birds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.Vector2;

public class Bird extends Actor {
    private static final float PPM = 100f; // Pixels per meter

    protected Texture texture;
    protected float textureScaleX = 1.0f;
    protected float textureScaleY = 1.0f;
    protected float textureX = 0.0f;
    protected float textureY = 0.0f;

    // Physics properties
    protected World world;
    protected Body body;
    protected boolean isLaunched = false;
    protected float initialX, initialY;

    // Bird properties
    protected int damage = (int) 10f; // Default damage value
    protected boolean isDestroyed = false;
    protected boolean hasUsedAbility = false;

    public Bird(World world, Texture texture, float x, float y) {
        this.world = world;
        this.texture = texture;
        this.initialX = x;
        this.initialY = y;

        // Set size for the actor
        setSize(texture.getWidth(), texture.getHeight());
        resizeTexture(0.2f, 0.2f);

        // Create physics body
        createPhysicsBody(x, y);
    }

    protected void createPhysicsBody(float x, float y) {
        // Create body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / PPM, y / PPM);

        // Create the body
        body = world.createBody(bodyDef);
        body.setUserData(this);

        // Create circle shape for the bird
        CircleShape circle = new CircleShape();
        circle.setRadius((getWidth() / 2) / PPM);

        // Create fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.2f;

        body.createFixture(fixtureDef);

        // Disable physics until launched
        body.setActive(false);

        // Dispose of the shape
        circle.dispose();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (body != null) {
            // Update actor position based on physics body
            Vector2 position = body.getPosition();
            setPosition(
                position.x * PPM - getWidth() / 2,
                position.y * PPM - getHeight() / 2
            );
            setRotation((float) Math.toDegrees(body.getAngle()));

            // Check if bird is off screen or stopped
            if (isLaunched) {
                Vector2 velocity = body.getLinearVelocity();
                if (isOffScreen() || (velocity.len() < 0.1f && body.getAngularVelocity() < 0.1f)) {
                    isDestroyed = true;
                }
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!isDestroyed) {
            batch.draw(texture,
                getX(), getY(),
                getWidth() / 2, getHeight() / 2,
                getWidth(), getHeight(),
                1, 1,
                getRotation(),
                0, 0,
                texture.getWidth(), texture.getHeight(),
                false, false);
        }
    }

    public void launch(float velocityX, float velocityY) {
        if (!isLaunched) {
            body.setActive(true);
            body.setLinearVelocity(velocityX, velocityY);
            isLaunched = true;
        }
    }

    public void reset() {
        if (body != null) {
            body.setTransform(initialX / PPM, initialY / PPM, 0);
            body.setLinearVelocity(0, 0);
            body.setAngularVelocity(0);
            body.setActive(false);
            isLaunched = false;
            isDestroyed = false;
            hasUsedAbility = false;
        }
    }

    public void resizeTexture(float scaleX, float scaleY) {
        this.textureScaleX = scaleX;
        this.textureScaleY = scaleY;
        setSize(texture.getWidth() * textureScaleX, texture.getHeight() * textureScaleY);

        // Update physics body size if it exists
        if (body != null) {
            for (Fixture fixture : body.getFixtureList()) {
                CircleShape shape = (CircleShape) fixture.getShape();
                shape.setRadius((getWidth() / 2) / PPM);
            }
        }
    }

    private boolean isOffScreen() {
        return getX() < -getWidth() || getX() > com.badlogic.gdx.Gdx.graphics.getWidth() + getWidth() ||
            getY() < -getHeight() || getY() > com.badlogic.gdx.Gdx.graphics.getHeight() + getHeight();
    }

    // Getters and setters
    public Body getBody() {
        return body;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public boolean isLaunched() {
        return isLaunched;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public boolean hasUsedAbility() {
        return hasUsedAbility;
    }

    public void useAbility() {
        hasUsedAbility = true;
    }

    public void dispose() {
        if (body != null) {
            world.destroyBody(body);
            body = null;
        }
    }
}
