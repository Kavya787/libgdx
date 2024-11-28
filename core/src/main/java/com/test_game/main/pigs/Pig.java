package com.test_game.main.Pigs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public abstract class Pig {
    protected String type;
    protected transient Body body;
    protected float health;
    protected transient Texture texture;
    protected boolean status;
    protected float radius;
    protected float posX, posY;
    protected float velocityX;
    protected float velocityY;
    private transient World  world;
    public Pig(float x ,float y){
        posX=x;
        posY=y;
    }
    public Pig(){

    }
    public Pig(World world, float x, float y,float radius) {
        BodyDef pigDef = new BodyDef();
        pigDef.position.set(x,y); // Pigs positioned between buildings
        pigDef.type = BodyDef.BodyType.DynamicBody;

        Body pig = world.createBody(pigDef);
        CircleShape pigShape = new CircleShape();
        pigShape.setRadius(radius); // Pig size
        this.radius=radius;
        FixtureDef pigFixture = new FixtureDef();
        pigFixture.shape = pigShape;
        pigFixture.density = 13f;
        pigFixture.friction = 1f;
        pigFixture.restitution = 0f; // Bounciness
        velocityX=0;
        velocityY=0;
        pig.createFixture(pigFixture);
        this.body=pig;
        this.body.setLinearVelocity(new Vector2(0,0));
        pigShape.dispose();

        // Set up a simple rectangular block
        this.world=world;
        status=true;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setRadius(float radius) {
        this.radius = radius;
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

    public void setWorld(World world) {
        this.world = world;
    }

    public String getType() {
        return type;
    }

    public Texture getTexture() {
        return texture;
    }

    public boolean isStatus() {
        return status;
    }

    public float getRadius() {
        return radius;
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

    public World getWorld() {
        return world;
    }

    public boolean getStatus(){
        return  status;
    }
    public void statusFalse(){
        this.status=false;
//        world.destroyBody(body);
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
