package com.test_game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Catapult extends Actor {
    private Texture texture;
    protected Body body;
    public Catapult(World world ) {
        this.texture = new Texture(Gdx.files.internal("catapult.png"));;
        setSize(100, 50);
        BodyDef catapultBodyDef = new BodyDef();
        catapultBodyDef.type = BodyDef.BodyType.StaticBody;
        catapultBodyDef.position.set(5f, 1);
        Body catapultBody = world.createBody(catapultBodyDef);
        PolygonShape catapultShape = new PolygonShape();
        catapultShape.setAsBox(2.4f, 0.5f);
        FixtureDef catapultFixtureDef = new FixtureDef();
        catapultFixtureDef.shape = catapultShape;
        catapultFixtureDef.isSensor = false; // Allows for physical interaction
        catapultFixtureDef.friction=0;
        catapultFixtureDef.restitution=0.3f;
        catapultBody.createFixture(catapultFixtureDef);
        this.body=catapultBody;
        catapultShape.dispose(); // Dispose shape to avoid memory leak
    }
    public void render(SpriteBatch batch) {
//        if(status){
            batch.draw(texture,2.25f, 0.5f, 2, 1.5f);
//            spriteBatch.draw(catapultTexture, 2.5f, 0.5f, 1f, 1.5f); // Adjust position and size as needed
//        }

    }
    public void dispose() {
        texture.dispose(); // Clean up the texture
    }
}
