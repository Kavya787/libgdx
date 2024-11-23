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

    private World world;
    private Body body;
    private float initialX, initialY;

    public Catapult(World world, Texture texture, float x, float y) {
        this.world = world;
        this.texture = texture;
        this.initialX = x;
        this.initialY = y;

        setSize(texture.getWidth(), texture.getHeight());
        createPhysicsBody(x, y);
    }

    private void createPhysicsBody(float x, float y) {
        // Create body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;  // Catapult doesn't move
        bodyDef.position.set(x / PPM, y / PPM);

        // Create the body
        body = world.createBody(bodyDef);
        body.setUserData(this);

        // Create a rectangle shape for the catapult
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((getWidth() / 2) / PPM, (getHeight() / 2) / PPM);

        // Create fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.2f;

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (body != null) {
            Vector2 position = body.getPosition();
            setPosition(
                position.x * PPM - getWidth() / 2,
                position.y * PPM - getHeight() / 2
            );
            setRotation((float) Math.toDegrees(body.getAngle()));
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
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

    public void resizeTexture(float scaleX, float scaleY) {
        this.textureScaleX = scaleX;
        this.textureScaleY = scaleY;
        setSize(texture.getWidth() * textureScaleX, texture.getHeight() * textureScaleY);

        // Update physics body size if it exists
        if (body != null) {
            // Remove old fixtures
            Array<Fixture> fixtures = body.getFixtureList();
            for (Fixture fixture : fixtures) {
                body.destroyFixture(fixture);
            }

            // Create new fixture with updated size
            PolygonShape shape = new PolygonShape();
            shape.setAsBox((getWidth() / 2) / PPM, (getHeight() / 2) / PPM);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 1.0f;
            fixtureDef.friction = 0.4f;
            fixtureDef.restitution = 0.2f;

            body.createFixture(fixtureDef);
            shape.dispose();
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
