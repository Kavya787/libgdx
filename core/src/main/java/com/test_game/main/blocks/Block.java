package com.test_game.main.blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;


public class Block extends Actor {

    private static final float PPM = 100f;

    public Texture texture;
    protected Body body;
    protected World world;
    protected boolean isDestroyed = false;
    protected float health;
    protected float density;
    protected float friction;
    protected float restitution;
    protected float textureScaleX = 1.0f;
    protected float textureScaleY = 1.0f;
    protected float hitboxScaleX = 1.0f;
    protected float hitboxScaleY = 1.0f;
    protected float initialX, initialY;


    public Block(World world, Texture texture, float x, float y) {
        this.world = world;
        this.texture = texture;
        this.initialX = x;
        this.initialY = y;
        setSize(texture.getWidth(), texture.getHeight());

        // Default physics properties
        this.density = 1.0f;
        this.friction = 0.4f;
        this.restitution = 0.1f;
        this.health = 100f;

        setDefaultSize();
        createBody(x, y);
    }

    protected void createBody(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / PPM, y / PPM);

        body = world.createBody(bodyDef);

        PolygonShape box = new PolygonShape();
        box.setAsBox(
            (getWidth() * hitboxScaleX / 2) / PPM,
            (getHeight() * hitboxScaleY / 2) / PPM
        );

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;

        body.createFixture(fixtureDef);
        body.setUserData(this);

        box.dispose();
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

            PolygonShape box = new PolygonShape();
            box.setAsBox(
                (getWidth() * hitboxScaleX / 2) / PPM,
                (getHeight() * hitboxScaleY / 2) / PPM
            );

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = box;
            fixtureDef.density = density;
            fixtureDef.friction = friction;
            fixtureDef.restitution = restitution;

            body.createFixture(fixtureDef);
            box.dispose();

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
                getWidth() / 2, getHeight() / 2,
                getWidth(), getHeight(),
                textureScaleX, textureScaleY,
                getRotation(),
                0, 0,
                texture.getWidth(), texture.getHeight(),
                false, false
            );
        }
    }
    public void takeDamage(int damage) {
        if (!isDestroyed) {
            health -= damage;
            if (health <= 0) {
                destroy();
            }
        }
    }

    public boolean canTakeDamage() {
        return !isDestroyed;
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
