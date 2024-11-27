package com.test_game.main.birds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

public class RedBird extends Bird {
    public RedBird(World world, Texture texture, float x, float y) {
        super(world, texture, x, y);
        setDamage(15);
    }
}
