package com.test_game.main.blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;

public class Log1 extends Block {
    private static final TextureRegion LOG_TEXTURE_REGION = new TextureRegion(new Texture("Log1.png")); // Set texture region

    public Log1() {}

    public Log1(World world, float x, float y) {
        super(world, x, y, 1.5F, 0.25f);
        this.type = "log";
        this.health = 10;
        this.textureRegion = LOG_TEXTURE_REGION; // Use the texture region
    }
}
