package com.test_game.main.Birds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

public class BlackBird extends Bird {
    public BlackBird(){
        super();
    }
    public BlackBird(World world, float x, float y) {
        super(world, x, y, 0.25f);
        this.setType("black");
        this.setDamage(5);
        this.setTexture(new Texture("birdBlack.png"));
    }

    // BlueBird could split into multiple parts (a potential extension)
}
