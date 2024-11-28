package com.test_game.main.Birds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.io.Serializable;

public abstract class Bird implements Serializable {
    protected   transient Body body;
    protected String type;
    protected   transient Texture texture;
    protected boolean launched;
    private float radius;
    protected boolean status;
    transient World world;
    protected float angle;
    protected float posX, posY;
    protected float velocityX;
    protected float damage;
    protected float velocityY;
    protected float power;

    public Bird(){
    }
    public Bird(World world, float x, float y, float radius) {
        this.radius = radius;
//        this.type=type;
        this.launched = false;
        status=true;
        this.world=world;
        this.angle=45;
        this.power=10;
        // Create the body for the bird
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 7f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.1f;
        this.body = world.createBody(bodyDef);
        this.body.createFixture(fixtureDef);

        shape.dispose();
    }


    public void setBody(Body body) {
        this.body = body;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setLaunched(boolean launched) {
        this.launched = launched;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public void setPower(float power) {
        this.power = power;
    }

    public String getType() {
        return type;
    }

    public Texture getTexture() {
        return texture;
    }

    public float getRadius() {
        return radius;
    }

    public boolean isStatus() {
        return status;
    }

    public World getWorld() {
        return world;
    }

    public float getAngle() {
        return angle;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public float getVelocityX() {
        return velocityX;
    }

    public float getDamage() {
        return damage;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public float getPower() {
        return power;
    }

    public boolean isLaunched() {
        return launched;
    }

    public void launch() {
        if (!launched) {
            float radians = (float) Math.toRadians(angle);
            Vector2 velocity = new Vector2((float) Math.cos(radians) * power*0.9f, (float) Math.sin(radians) * power);
            body.setLinearVelocity(velocity);
            launched = true;
        }
    }

    public void render(SpriteBatch batch) {
        if (texture != null) {
            if(status){
                Vector2 position = body.getPosition();
                batch.draw(texture, position.x - radius, position.y - radius, radius * 4, radius * 4);
            }

//            batch.draw(texture, position.x - 0.25f, position.y - 0.25f, 0.7f, 0.7f);
        }
    }

    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }

    public Body getBody() {
        return body;
    }
    public void updateStatus(){
        status=false;
//        world.destroyBody(body);
    }
    public void specialBoost(){

    }
    public void saveState() {
        if (body != null) {
            Vector2 position = body.getPosition();
            Vector2 velocity = body.getLinearVelocity();
            this.posX = position.x;
            this.posY = position.y;
            this.velocityX = velocity.x;
            this.velocityY = velocity.y;
        }
    }

}
