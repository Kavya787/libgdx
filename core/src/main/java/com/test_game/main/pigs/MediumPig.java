package com.test_game.main.pigs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.test_game.main.birds.Bird;

public class MediumPig extends Pig {
    public MediumPig(World world, Texture texture, float x, float y) {
        super(world, texture, x, y);
        setHealth(50f);  // Less health, easier to destroy
    }


}
