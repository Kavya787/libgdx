package com.test_game.main;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.test_game.main.Birds.Bird;
import com.test_game.main.Pigs.Pig;
import com.test_game.main.blocks.Block;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.List;

public class CollisionHandler implements ContactListener {

    ArrayList<Pig> pigs;
    ArrayList<Block> buildings;
    ArrayList<Bird> birds;
    ArrayList<Body>bodiesToDestroy;
    int currentBirdIndex=0;
    public CollisionHandler(ArrayList<Bird> pigs,ArrayList<Block> build,ArrayList<Bird> birds){
        this.pigs=pigs;
        this.buildings=build;
        this.birds=birds;
        bodiesToDestroy=new ArrayList<>();
    }
    public void updateCurrentBirdIndex(int cur){
        this.currentBirdIndex=cur;
    }
    public void clearBodies(){
        bodiesToDestroy.clear();
    }
    @Override
    public void beginContact(Contact contact) {
        // Handle collisions between birds and buildings
        for (int i = 0; i < buildings.size(); i++) {
            Body building = buildings.get(i).getBody();
            Body birdBody=birds.get(currentBirdIndex).getBody();
            if(!buildings.get(i).getStatus()){
                continue;
            }
            // Check if the collision involves a bird and a building
            boolean birdCollided = false;
            Body bird = null;

            if (contact.getFixtureA().getBody() == building && (contact.getFixtureB().getBody()==birdBody)) {
                bird = contact.getFixtureB().getBody();
                birdCollided = true;
            } else if (contact.getFixtureB().getBody() == building && (contact.getFixtureA().getBody()==birdBody)) {
                bird = contact.getFixtureA().getBody();
                birdCollided = true;
            }

            // If a bird collided with the building, apply force
            if (birdCollided && bird != null) {
                // Get the velocity of the bird
                Vector2 birdVelocity = bird.getLinearVelocity();

                // Apply a stronger impulse based on the bird's speed
                float impulseMagnitude = birdVelocity.len() * 0.5f; // Increase multiplier for more force
                Vector2 impulse = new Vector2(birdVelocity.x * impulseMagnitude, birdVelocity.y * impulseMagnitude);

                // Apply the impulse to the building
                building.applyLinearImpulse(impulse, building.getWorldCenter(), true);

                // Reduce building health
                int damage = 20; // Set the damage the bird causes on the building
                buildings.get(i).reduceHealth(birds.get(currentBirdIndex).getDamage());
                if(buildings.get(i).getHealth()<=0){
                    bodiesToDestroy.add(building);
                    buildings.get(i).statusFalse();
                }

            }
            // Handling Building vs Building collision: Apply force to the other building if they touch
            if ((contact.getFixtureA().getBody() == building && contact.getFixtureA().getBody() != birdBody) ||
                (contact.getFixtureB().getBody() == building && contact.getFixtureA().getBody() != birdBody)) {

                // Apply force to the building when it collides with another object (e.g., other buildings)
                Vector2 forceDirection = new Vector2(10f, 0); // Horizontal force to push buildings away
                building.applyForceToCenter(forceDirection, true); // Apply force to the center of the building
            }
        }
        for (int i = 0; i < pigs.size(); i++) {
            Body pig = pigs.get(i).getBody();
            Body birdBody=birds.get(currentBirdIndex).getBody();
            // Check if the collision involves a bird hitting a pig
            if (contact.getFixtureA().getBody() == pig && (contact.getFixtureB().getBody()==birdBody)) {
                Body bird = contact.getFixtureB().getBody();
                applyDamageToPig(i, bird, birds.get(currentBirdIndex).getDamage());
            } else if (contact.getFixtureB().getBody() == pig && (contact.getFixtureA().getBody()==birdBody)) {
                Body bird = contact.getFixtureA().getBody();
                applyDamageToPig(i, bird, birds.get(currentBirdIndex).getDamage());
            }

            // Check if a building falls on a pig
//            if ((contact.getFixtureA().getBody() == pig && (contact.getFixtureB().getBody()!=birdBody)) ||
//                (contact.getFixtureB().getBody() == pig && (contact.getFixtureA().getBody()!=birdBody))) {
//                Body block = contact.getFixtureA().getBody() == pig ? contact.getFixtureB().getBody() : contact.getFixtureA().getBody();
//                applyDamageToPigByBlock(i, block,2);
//            }
        }

    }
    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // Get the bodies involved in the contact
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        // Check for collisions between pigs and blocks/buildings
        for (int i = 0; i < pigs.size(); i++) {
            Body pig = pigs.get(i).getBody();

            // Determine if the pig is one of the bodies involved in the collision
            if (bodyA == pig || bodyB == pig) {
                // Identify the other body (block/building) involved in the collision
                Body otherBody = (bodyA == pig) ? bodyB : bodyA;

                // Check if the collision impulse is significant
                float maxImpulse = 0f;
                for (float normalImpulse : impulse.getNormalImpulses()) {
                    maxImpulse = Math.max(maxImpulse, normalImpulse);
                }
                System.out.println("max impulse is -> "+maxImpulse);
                // Define a threshold for significant collision force
                float damageThreshold = 5f; // Adjust this value based on game physics

                if (maxImpulse > damageThreshold) {
                    // Apply damage to the pig based on the impulse
                    applyDamageToPigByBlock(i, otherBody, 2);
                }
            }
        }
    }

    private void applyDamageToPig(int pigIndex, Body bird,float damage) {
        Vector2 birdVelocity = bird.getLinearVelocity();
//        float damage = birdVelocity.len() * 0.5f; // Apply damage based on bird velocity
        pigs.get(pigIndex).reduceHealth(damage);
//            .set(pigIndex, pigHealth.get(pigIndex) - (int)damage);

        if (pigs.get(pigIndex).getHealth() <= 0) {
            bodiesToDestroy.add(pigs.get(pigIndex).getBody()); // Destroy the pig
        }
    }

    private void applyDamageToPigByBlock(int pigIndex, Body block,float dam) {
        // Apply damage when a block falls on the pig
        Vector2 blockVelocity = block.getLinearVelocity();
        float damage = blockVelocity.len() * 0.3f; // Less damage from block impact

//        pigHealth.set(pigIndex, pigHealth.get(pigIndex) - (int)damage);
//
//        if (pigHealth.get(pigIndex) <= 0) {
//            bodiesToDestroy.add(pigs.get(pigIndex)); // Destroy the pig
//        }
        pigs.get(pigIndex).reduceHealth(dam);
//            .set(pigIndex, pigHealth.get(pigIndex) - (int)damage);

        if (pigs.get(pigIndex).getHealth() <= 0) {
            bodiesToDestroy.add(pigs.get(pigIndex).getBody()); // Destroy the pig
        }
    }
    public ArrayList<Body> getDestroy(){
        return bodiesToDestroy;
    }

}


