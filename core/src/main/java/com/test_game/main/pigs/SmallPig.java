package com.test_game.main.Pigs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.test_game.main.Birds.Bird;

public class smallPig extends Pig{
    public smallPig(){

    }
    public smallPig(float x ,float y){
        super(x,y);
    }
    public smallPig(World world, float x, float y) {
        super(world, x, y, 0.25f);
        this.type="small";
        this.health = 5;
        this.texture = new Texture("pigSmall.png");
    }
}


