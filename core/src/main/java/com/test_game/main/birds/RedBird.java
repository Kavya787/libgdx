package com.test_game.main.Birds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class RedBird extends Bird {
    public RedBird(){
        super();
    }
    public RedBird(Body body){
        this.body=body;
    }
    public RedBird(World world, float x, float y) {
        super(world, x, y, 0.25f);
        this.setType("red");
        this.setDamage(2);
        this.setTexture(new Texture("birdRed.png"));
    }

}
