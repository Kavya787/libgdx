package com.test_game.main.birds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Bird extends Actor {
    private static final float PPM = 100f; // Pixels per meter


    protected Texture texture;
    protected float textureScaleX = 1.0f;
    protected float textureScaleY = 1.0f;
    protected float hitboxScaleX = 1.0f;  // New: separate scale for hitbox
    protected float hitboxScaleY = 1.0f;  // New: separate scale for hitbox
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
        setDefaultSize(); // Set default sizes
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
                textureScaleX, textureScaleY,
                getRotation(),
                0, 0,
                texture.getWidth(), texture.getHeight(),
                false, false);
        }
    }

    public void launch(float forceX, float forceY) {
        if (!isLaunched) {
            // Apply force to the physics body
            body.setType(BodyDef.BodyType.DynamicBody); // Make the bird dynamic
            body.applyLinearImpulse(
                new Vector2(forceX, forceY),
                body.getWorldCenter(),
                true
            );
            isLaunched = true;
        }
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

    // New method to set absolute position
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

    // New method to update physics body
    private void updatePhysicsBody() {
        if (body != null) {
            // Store the current position
            Vector2 position = body.getPosition().cpy();
            float angle = body.getAngle();

            // Remove old fixtures
            Array<Fixture> fixtures = body.getFixtureList();
            while (fixtures.size > 0) {
                body.destroyFixture(fixtures.get(0));
            }

            // Create new circle fixture with updated size
            CircleShape circle = new CircleShape();
            circle.setRadius((getWidth() * hitboxScaleX / 2) / PPM);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = circle;
            fixtureDef.density = 1.0f;
            fixtureDef.friction = 0.4f;
            fixtureDef.restitution = 0.2f;

            body.createFixture(fixtureDef);
            circle.dispose();

            // Restore position and angle
            body.setTransform(position, angle);
        }
    }

    // New method to set default size
    public void setDefaultSize() {
        // Set visual size to 50% of original texture size
        setVisualSize(texture.getWidth() * 0.5f, texture.getHeight() * 0.5f);
        // Set hitbox to 80% of visual size
        setHitboxScale(0.8f, 0.8f);
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



    public boolean isDestroyed() {
        return isDestroyed;
    }

    public boolean hasUsedAbility() {
        return hasUsedAbility;
    }

    public void useAbility() {
        hasUsedAbility = true;
    }


    public boolean isLaunched() {
        return isLaunched;
    }


    public void dispose() {
        if (body != null) {
            world.destroyBody(body);
            body = null;
        }
    }
}
