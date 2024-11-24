package com.test_game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Catapult extends Actor {
    private static final float PPM = 100f;  // Pixels per meter

    private Texture texture;
    private float textureScaleX = 1.0f;
    private float textureScaleY = 1.0f;
    private float hitboxScaleX = 1.0f;
    private float hitboxScaleY = 1.0f;

    private World world;
    private Body body;
    private float initialX, initialY;

    public Catapult(World world, Texture texture, float x, float y) {
        this.world = world;
        this.texture = texture;
        setPosition(x, y);

        setSize(texture.getWidth(), texture.getHeight());
        createPhysicsBody(x, y);
    }

    private void createPhysicsBody(float x, float y) {
        // Create body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x / PPM, y / PPM);

        // Create the body
        body = world.createBody(bodyDef);
        body.setUserData(this);

        // Create a rectangle shape for the catapult
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(
            (getWidth() * hitboxScaleX / 2) / PPM,
            (getHeight() * hitboxScaleY / 2) / PPM
        );

        // Create fixture - modified to not interact with birds
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true; // Makes it non-solid

        // Set collision filtering
        fixtureDef.filter.categoryBits = 0x0002; // Catapult category
        fixtureDef.filter.maskBits = 0x0000;     // Collides with nothing

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (body != null) {
            Vector2 position = body.getPosition();
            setPosition(
                position.x * PPM - getWidth()/2,
                position.y * PPM - getHeight()/2
            );
            setRotation((float) Math.toDegrees(body.getAngle()));
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture,
            getX(), getY(),                    // Position
            getWidth()/2, getHeight()/2,       // Origin center
            getWidth(), getHeight(),           // Size
            textureScaleX, textureScaleY,      // Scale
            getRotation(),                     // Rotation
            0, 0,                              // Source rectangle position
            texture.getWidth(), texture.getHeight(),  // Source rectangle size
            false, false);                     // Flip
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

    private void updatePhysicsBody() {
        if (body != null) {
            Vector2 position = body.getPosition().cpy();
            float angle = body.getAngle();

            Array<Fixture> fixtures = body.getFixtureList();
            while (fixtures.size > 0) {
                body.destroyFixture(fixtures.get(0));
            }

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(
                (getWidth() * hitboxScaleX / 2) / PPM,
                (getHeight() * hitboxScaleY / 2) / PPM
            );

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.isSensor = true; // Makes it non-solid

            // Set collision filtering
            fixtureDef.filter.categoryBits = 0x0002; // Catapult category
            fixtureDef.filter.maskBits = 0x0000;     // Collides with nothing

            body.createFixture(fixtureDef);
            shape.dispose();

            body.setTransform(position, angle);
        }
    }

    public Body getBody() {
        return body;
    }

    public void dispose() {
        if (body != null) {
            world.destroyBody(body);
            body = null;
        }
    }
}
