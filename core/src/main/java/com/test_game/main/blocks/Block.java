package com.test_game.main.blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public abstract class Block {
    transient protected Body body;
    protected String type;
    protected float health;
    transient protected TextureRegion textureRegion;
    protected boolean status;
    private float hx ,hy;
    transient World world;
    protected float posX, posY;
    protected float velocityX;
    protected float velocityY;
    public Block(){

    }
    public Block(World world, float x, float y,float hx, float hy ) {
        this.world=world;
        this.posX=x;
        this.posY=y;
        BodyDef bodyDef = new BodyDef();
        status=true;
        bodyDef.position.set(x, y);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        this.hx=hx;
        this.hy=hy;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(hx,hy); // Standard size
        velocityX=0;
        velocityY=0;
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 13f;
        fixtureDef.friction= 0.9f;
        fixtureDef.restitution = 0f;
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

    public void setHealth(float health) {
        this.health = health;
    }

    public void setTexture(TextureRegion texture) {
        this.textureRegion = texture;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setHx(float hx) {
        this.hx = hx;
    }

    public void setHy(float hy) {
        this.hy = hy;
    }

    public void setWorld(World world) {
        this.world = world;
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

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public String getType() {
        return type;
    }

    public TextureRegion getTexture() {
        return textureRegion;
    }

    public boolean isStatus() {
        return status;
    }

    public float getHx() {
        return hx;
    }

    public float getHy() {
        return hy;
    }

    public World getWorld() {
        return world;
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

    public float getVelocityY() {
        return velocityY;
    }

    public boolean getStatus(){
        return  status;
    }
    public void statusFalse(){
        this.status=false;
    }
    public void render(SpriteBatch batch) {
        if (status) {
            Vector2 position = body.getPosition();
            float angle = body.getAngle() * (180f / (float) Math.PI); // Convert radians to degrees
            batch.draw(textureRegion,
                position.x - hx, position.y - hy, // Bottom-left corner
                hx, hy,                         // Origin (center for rotation)
                2 * hx, 2 * hy,                 // Width and height
                1, 1,                           // Scale
                angle);                         // Rotation
        }
    }

    public void reduceHealth(float damage) {
        this.health-=damage;
        if(health<=0){
            status=false;

        }
    }
    public float getHealth() {
        return health;
    }

    public boolean isDestroyed() {
        return health <= 0;
    }

    public Body getBody() {
        return body;
    }

    public boolean isInvolvedInCollision(Contact contact) {
        // Check if this block is part of the collision
        return contact.getFixtureA().getBody() == body || contact.getFixtureB().getBody() == body;
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
