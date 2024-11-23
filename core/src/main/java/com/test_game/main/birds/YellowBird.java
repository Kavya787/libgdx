package com.test_game.main.birds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

public class YellowBird extends Bird {
    public YellowBird(World world, Texture texture, float x, float y) {
        super(world, texture, x, y);
        setDamage(12);
    }
}
