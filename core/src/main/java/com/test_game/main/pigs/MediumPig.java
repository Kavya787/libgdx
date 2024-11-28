package com.test_game.main.Pigs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.test_game.main.Birds.Bird;

public class MediumPig extends Pig{
    public MediumPig(){

    }
    public MediumPig(float x ,float y){
        super(x,y);
    }
    public MediumPig(World world, float x, float y) {
        super(world, x, y, 0.45f);
        this.type="medium";

        this.health = 40;
        this.texture = new Texture("pigMedium.png");
    }
    @Override
    public void render(SpriteBatch batch) {
        if (texture != null) {
            if(status){
                Vector2 position = body.getPosition();
                // Only change the size multiplier here
                batch.draw(texture, position.x - radius, position.y - radius, radius * 2.5f, radius * 2.5f);
            }
        }
    }
}


