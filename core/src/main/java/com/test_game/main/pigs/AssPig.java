package com.test_game.main.Pigs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.test_game.main.Birds.Bird;

public class AssPig extends Pig{
    public AssPig(){

    }
    public AssPig(World world, float x, float y) {
        super(world, x, y, 0.5f);
        this.type="Ass";
        try{
            this.texture = new Texture("AssPig.png");
            System.out.println("AssPig texture loaded successfully");
        } catch (Exception e) {
            System.err.println("Failed to load AssPig texture: " + e.getMessage());
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (texture != null) {
            if(status){
                Vector2 position = body.getPosition();
                // Only change the size multiplier here
                batch.draw(texture, position.x - radius, position.y - radius, radius * 5, radius * 5);
            }
        }
    }
}



