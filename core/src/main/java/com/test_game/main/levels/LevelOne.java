package com.test_game.main.Levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.test_game.main.Birds.*;
import com.test_game.main.CollisionHandler;
import com.test_game.main.Core;
import com.test_game.main.Pigs.MediumPig;
import com.test_game.main.Pigs.Pig;
import com.test_game.main.Pigs.smallPig;
import com.test_game.main.Screens.WinScreen;
import com.test_game.main.blocks.*;
//import com.test_game.main.blocks.SteelBlock;
//import com.test_game.main.blocks.WoodBlock;

import java.util.ArrayList;

public class LevelOne extends Level {
    private float bird2PushRadius = 5f;
    private float bird2PushForce = 500f;

    public LevelOne(){

    }
    public LevelOne(boolean flag){
        this.initialize=flag;

    }
    public LevelOne(ArrayList<Bird>tempBirds,ArrayList<Pig>tempPigs,ArrayList<Block>tempBlocks,int idx){
        this.birds=tempBirds;
        this.pigs=tempPigs;
        this.buildings=tempBlocks;
        this.currentBirdIndex=idx;
        this.initialize=false;
    }
//    public LevelOne(ArrayList<Bird>tempBirds){
//        this.birds=tempBirds;
//    }

    public LevelOne(ArrayList<Pig> pigs) {
        this.pigs=pigs;
    }

    @Override
    public void show() {
        if(!gamePaused){
            super.show();
            if(initialize){
                this.lvl="one";
                createBirds();
                createBuildings();
                createPigs();
            }
//            System.out.println("len"+pigs.size());
            collisionHandler=new CollisionHandler(pigs,buildings,birds);
            world.setContactListener(collisionHandler);
        }
    }
    private void createBirds() {
        birds = new ArrayList<>();
        birds.add(new YellowBird(world, 3, 2));
        birds.add(new RedBird(world, 1.5f, 1));
        birds.add(new RedBird(world, 0.5f, 1));
    }
    private void createPigs(){
        pigs = new ArrayList<>();
        pigs.add(new smallPig(world, 12.8f, 2.75f));
//        pigs.add(new MediumPig(world, 12.8f, 4.25f));
    }

    private void createBuildings() {
        buildings = new ArrayList<>();
        buildings.add(new WoodBlock(world,12f,1.5f));
        buildings.add(new tnt(world,12f,3f));
        buildings.add(new WoodBlock(world,14f,1.5f));
        buildings.add(new WoodBlock(world,14f,3f));
        buildings.add(new Log1(world,13f,2.25f));
        buildings.add(new Log1(world,13f,3.75f));

//        buildings.add(new WoodBlock(world, 10.5f, 1.5f));
//        buildings.add(new WoodBlock(world, 10.5f, 3f));
//        buildings.add(new GlassBlock(world, 11.5f, 1.5f));
//        buildings.add(new GlassBlock(world, 12.5f, 1.5f));
//        buildings.add(new SteelBlock(world, 12.5f, 3f));
//        buildings.add(new GlassBlock(world, 13.5f, 1.5f));
//        buildings.add(new GlassBlock(world, 14.5f, 1.5f));
//        buildings.add(new SteelBlock(world, 14.5f, 3f));
       // buildings.add(new SteelBlock(world, 12.5f, 3f));
    }

    @Override
    public void render(float delta) {
        if(currentBirdIndex>=3)((Core) Gdx.app.getApplicationListener()).setScreen(new WinScreen());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        handleInput();
        renderBg();
        for(Bird bl:birds){
            if(!bl.isStatus()){
                bodiesToDestroy.add(bl.getBody());
            }
        }
        for(Pig bl:pigs){
            if(!bl.isStatus()){
                bodiesToDestroy.add(bl.getBody());
            }
        }
        for(Block bl:buildings){
            if(!bl.isStatus()){
                bodiesToDestroy.add(bl.getBody());
            }
        }

//        for(int i =0;i<currentBirdIndex;i++){
//            if(birds.get(i).getBody()!=null){
//                bodiesToDestroy.add(birds.get(i).getBody());
//                birds.get(i).status=false;
//            }

//        }
//        String fileName = "level.json";
//        levelSerializer.save(this, fileName);
        collisionHandler.updateCurrentBirdIndex(currentBirdIndex);
        if (bird2PushActive && birds.get(currentBirdIndex).getType().equals("black")) {
            applyPushEffect(birds.get(currentBirdIndex).getBody());
            bird2PushActive = false;
        }
        world.step(1 / 60f, 6, 2);
        // Safely destroy queued bodies
        super.render(delta);
        // Update camera
        camera.update();

        // Render debug view
        debugRenderer.render(world, camera.combined);
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        catapult.render(spriteBatch);

        for (Bird bird : birds) {
            bird.render(spriteBatch);
        }
        for(Block bl : buildings){
            bl.render(spriteBatch);
        }
        for (Pig pi:pigs){
            pi.render(spriteBatch);
        }
        spriteBatch.end();
        if (!birds.get(currentBirdIndex).isLaunched() && currentBirdIndex < birds.size()) {
            drawTrajectory();
        }
        handleLaunch();
        for (int i = 0; i < buildings.size(); i++) {
            System.out.println("Building " + (i + 1) + " Health: " + buildings.get(i).getHealth());
        }
        for (int i = 0; i < pigs.size(); i++) {
            System.out.println("Pigs " + (i + 1) + " Health: " + pigs.get(i).getHealth());
        }
    }


    private void applyPushEffect(Body bird) {
        // Create a push effect around the bird (affects nearby bodies)
        for (Block blo : buildings) {
            Body building=blo.getBody();
            Vector2 direction = building.getPosition().sub(bird.getPosition()).nor();
            if (building.getPosition().dst(bird.getPosition()) < bird2PushRadius) {
                blo.reduceHealth(2);
                if(blo.getHealth()<=0){
                    bodiesToDestroy.add(building);
                }

                building.applyForceToCenter(direction.scl(bird2PushForce), true);

            }
        }
    }

    @Override
    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
        shapeRenderer.dispose();
    }
}
