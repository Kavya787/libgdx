package com.test_game.main.Levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.test_game.main.Birds.Bird;
import com.test_game.main.Birds.BlackBird;
import com.test_game.main.Birds.RedBird;
import com.test_game.main.Birds.YellowBird;
import com.test_game.main.Catapult;
import com.test_game.main.CollisionHandler;
import com.test_game.main.Core;
import com.test_game.main.LevelSerializer;
import com.test_game.main.Pigs.*;
import com.test_game.main.Screens.LoseScreen;
import com.test_game.main.Screens.PauseMenuScreen;
import com.test_game.main.Screens.WinScreen;
import com.test_game.main.blocks.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Level  extends ScreenAdapter implements Serializable {
    transient OrthographicCamera camera;
    transient ShapeRenderer shapeRenderer;
     float bird2PushRadius = 5f;
     float bird2PushForce = 400f;
     transient LevelSerializer levelSerializer;
    transient Catapult catapult;
    transient World world;
    transient Box2DDebugRenderer debugRenderer;
    transient  Texture backgroundTexture,groundTexture;
    transient ArrayList<Body> bodiesToDestroy = new ArrayList<>();
    transient ArrayList<Body>destroyedBodies = new ArrayList<>();
    transient SpriteBatch spriteBatch;
    public ArrayList<Pig> pigs;
    public ArrayList<Block> buildings;
    boolean initialize;
    boolean gamePaused;
    public ArrayList<Bird> birds;
    public int currentBirdIndex = 0;
     boolean bird2PushActive=false;
    transient  CollisionHandler collisionHandler;
    public void show(){

        gamePaused=false;
        camera = new OrthographicCamera();
        levelSerializer = new LevelSerializer();
        camera.setToOrtho(false, 16, 9); // 16x9 meters viewport
        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
        backgroundTexture = new Texture("playScreenbg.jpg");
        groundTexture = new Texture("ground.png");
        world = new World(new Vector2(0, -10f), true);
        if(!initialize){
            ArrayList<Bird>birdTemp=new ArrayList<>();
            for(Bird bi:birds){
                Bird  birdT=getTypeBird(bi.getType(),world, bi.getPosX(), bi.getPosY());
                birdT.getBody().setLinearVelocity(new Vector2(bi.getVelocityX(), bi.getVelocityY()));
                birdT.setStatus(bi.isStatus());
                birdT.setLaunched(bi.isLaunched());
                birdT.setDamage(bi.getDamage());
                birdTemp.add(birdT);
                }
            birds=birdTemp;
            ArrayList<Pig>pigTemp=new ArrayList<>();
            for(Pig bi:pigs){
                Pig pigT=getTypePig(bi.getType(),world, bi.getPosX(), bi.getPosY());
                pigT.setStatus(bi.isStatus());
                pigT.getBody().setLinearVelocity(new Vector2(bi.getVelocityX(), bi.getVelocityY()));
                pigT.setHealth(bi.getHealth());
                pigTemp.add(pigT);
            }
            pigs=pigTemp;
            ArrayList<Block>blockTemp=new ArrayList<>();
            for(Block bl:buildings){
                Block blTemp=getTypeBlock(bl.getType(),world, bl.getPosX(), bl.getPosY());
                blTemp.setStatus(bl.isStatus());
                blTemp.setHealth(bl.getHealth());
                blTemp.getBody().setLinearVelocity(new Vector2(bl.getVelocityX(), bl.getVelocityY()));
                blockTemp.add(blTemp);
            }
            buildings=blockTemp;
        }
        debugRenderer = new Box2DDebugRenderer();
        catapult= new Catapult(world);
        createBoundaries();
        createCatapult();
    }
    public Pig getTypePig(String type,World world,float x, float y){
        switch (type) {
            case "king":
                return new KingPig(world, x, y);
            case "medium":
                return new MediumPig(world, x, y);
            case "small":
                return new smallPig(world, x, y);
            case "Ass":
                return new AssPig(world,x,y);
        }
        return null;
    }
    public Bird getTypeBird(String type,World world,float x, float y){
        switch (type) {
            case "black":
                return new BlackBird(world, x, y);
            case "yellow":
                return new YellowBird(world, x, y);
            case "red":
                return new RedBird(world, x, y);
        }
        return null;
    }
    public Block getTypeBlock(String type,World world,float x, float y){
        switch (type) {
            case "wood":
                return new WoodBlock(world, x, y);
            case "steel":
                return new SteelBlock(world, x, y);
            case "glass":
                return new GlassBlock(world, x, y);
            case "log":
                return new Log1(world,x,y);
            case "tnt":
                return new tnt(world,x,y);
        }
        return null;
    }
    private void createBoundaries() {
        // Create ground (very thin boundary)
        BodyDef groundDef = new BodyDef();
        groundDef.position.set(8, 0.5f); // Center of the ground
        groundDef.type = BodyDef.BodyType.StaticBody;
        Body ground = world.createBody(groundDef);

        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(8, 0.5f); // Thin horizontal ground (16 meters wide, 1 meter thick)

// Create fixture definition with friction
        FixtureDef groundFixtureDef = new FixtureDef();
        groundFixtureDef.shape = groundShape;
        groundFixtureDef.friction = 0.4f; // Set friction for the ground (value between 0 and 1)
        groundFixtureDef.restitution=0;

// Apply the fixture to the ground
        ground.createFixture(groundFixtureDef);

// Dispose of the shape to free memory
        groundShape.dispose();


        // Create top boundary (very thin boundary)
        BodyDef topWallDef = new BodyDef();
        topWallDef.position.set(8, 8.9f); // Center of the top boundary
        topWallDef.type = BodyDef.BodyType.StaticBody;
        Body topWall = world.createBody(topWallDef);

        PolygonShape topShape = new PolygonShape();
        topShape.setAsBox(8, 0.05f); // Thin horizontal wall (16 meters wide, 0.1 meters thick)
        topWall.createFixture(topShape, 0);
        topShape.dispose();

        // Create left boundary (very thin boundary)
        BodyDef leftWallDef = new BodyDef();
        leftWallDef.position.set(0.1f, 4.5f); // Center of the left wall
        leftWallDef.type = BodyDef.BodyType.StaticBody;
        Body leftWall = world.createBody(leftWallDef);

        PolygonShape leftShape = new PolygonShape();
        leftShape.setAsBox(0.05f, 4.5f); // Thin vertical wall (9 meters tall, 0.1 meters thick)
        leftWall.createFixture(leftShape, 0);
        leftShape.dispose();

        // Create right boundary (very thin boundary)
        BodyDef rightWallDef = new BodyDef();
        rightWallDef.position.set(15.9f, 4.5f); // Center of the right wall
        rightWallDef.type = BodyDef.BodyType.StaticBody;
        Body rightWall = world.createBody(rightWallDef);

        PolygonShape rightShape = new PolygonShape();
        rightShape.setAsBox(0.05f, 4.5f); // Thin vertical wall (9 meters tall, 0.1 meters thick)
        rightWall.createFixture(rightShape, 0);
        rightShape.dispose();


    }
    public void render(float delta){
        saveState();
        destroyQueuedBodies();
    }
    public void handleInput() {
        // Handle changing the angle and power of the bird using arrow keys and up/down
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            birds.get(currentBirdIndex).setAngle(birds.get(currentBirdIndex).getAngle() + 1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            birds.get(currentBirdIndex).setAngle(birds.get(currentBirdIndex).getAngle() - 1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            birds.get(currentBirdIndex).setPower(birds.get(currentBirdIndex).getPower() + 0.1f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            birds.get(currentBirdIndex).setPower(birds.get(currentBirdIndex).getPower() - 0.1f);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
            this.gamePaused=true;
            ((Core) Gdx.app.getApplicationListener()).setScreen(new PauseMenuScreen(this));
        }
        // Bird 1 Speed Boost Power-up (Activate with '1' key)
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) && birds.get(currentBirdIndex).getType().equals("yellow") ) {
            birds.get(currentBirdIndex).specialBoost();
        }
        // Bird 2 Push Power-up (Activate with '2' key)
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            bird2PushActive = true;
        }
    }
    private void saveState() {
        for (Bird bird : birds) {
            bird.saveState();
        }
        for(Pig pi:pigs){
            pi.saveState();
        }
        for(Block bl:buildings){
            bl.saveState();
        }
    }

    public void drawTrajectory() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 0, 0, 1);
        float angle= birds.get(currentBirdIndex).getAngle();
        float power= birds.get(currentBirdIndex).getPower();
        // Predict trajectory
        float radians = (float) Math.toRadians(angle);
        float initialVelocityX = (float) Math.cos(radians) * power;
        float initialVelocityY = (float) Math.sin(radians) * power;
        float timeStep = 0.1f;
        float time = 0;
        Vector2 position = new Vector2(birds.get(currentBirdIndex).getBody().getPosition());

        for (int i = 0; i < 30; i++) { // Simulate 50 steps of the trajectory
            float x = position.x + initialVelocityX * time;
            float y = position.y + initialVelocityY * time - 0.5f * 9.8f * time * time; // Gravity
            if (y < 0) break; // Stop drawing if it hits the ground
            if (i > 0) {
                shapeRenderer.line(
                    position.x + initialVelocityX * (time - timeStep),
                    position.y + initialVelocityY * (time - timeStep) - 0.5f * 9.8f * (time - timeStep) * (time - timeStep),
                    x, y);
            }
            time += timeStep;
        }

        shapeRenderer.end();
    }
    private void destroyQueuedBodies() {
        for (Body body : bodiesToDestroy) {
            if(!destroyedBodies.contains(body)){
                destroyedBodies.add(body);
                world.destroyBody(body);
            }

        }
        for (Body body : collisionHandler.getDestroy()) {
            if(!destroyedBodies.contains(body)){
                destroyedBodies.add(body);
                world.destroyBody(body);
            }
        }
        collisionHandler.clearBodies();
        bodiesToDestroy.clear();
    }
    private void createCatapult() {
    }
    public void renderBg(){

        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, 16, 9); // Draw background covering the entire screen
        spriteBatch.draw(groundTexture, 0, 0, 16, 1);
        spriteBatch.end();
    }
    public void handleWin(){
        boolean pigLeft=false;
        for(Pig pi:pigs){
            if (pi.getStatus()) {
                pigLeft = true;
                break;
            }
        }
        if(!pigLeft){
            ((Core) Gdx.app.getApplicationListener()).setScreen(new WinScreen());
        }
        else{
            if(currentBirdIndex==2){

                ((Core) Gdx.app.getApplicationListener()).setScreen(new LoseScreen());
            }
        }
    }
    public void handleLaunch(){
//        System.out.println(currentBirdIndex);
//        System.out.println("current ----------------------");
        if (!birds.get(currentBirdIndex).isLaunched() && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            birds.get(currentBirdIndex).launch();
        }
        if (birds.get(currentBirdIndex).isLaunched() && currentBirdIndex < birds.size()) {
            Body currentBird = birds.get(currentBirdIndex).getBody();
            Vector2 velocity = currentBird.getLinearVelocity();

            if (velocity.len() < 1f) {  // Threshold to check if the bird has stopped
                bodiesToDestroy.add(currentBird);
                birds.get(currentBirdIndex).updateStatus();
                if(currentBirdIndex==2){
                    handleWin();
                }
                else{
                    currentBirdIndex++;
                    birds.get(currentBirdIndex).getBody().setTransform(3, 2, 0); // Reset position to catapult
                }

            }
        }
    }
}
