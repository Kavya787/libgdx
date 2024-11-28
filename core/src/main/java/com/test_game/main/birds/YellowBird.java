package com.test_game.main.Birds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class YellowBird extends Bird {
    public YellowBird(){
        super();
    }
    public YellowBird(World world, float x, float y) {
        super(world, x, y, 0.25f);
        this.type="yellow";
        this.damage=4;
        this.texture = new Texture("birdYellow.png");
    }
    public void specialBoost() {
        if (this.launched) {
            Body bird = body;
            Vector2 currentVelocity = bird.getLinearVelocity();
            if (currentVelocity.len() > 0) {
                bird.setLinearVelocity(currentVelocity.scl(2));
            }
        }
    }

    // RedBird may have unique behavior, but for now, it just inherits from Bird
}
