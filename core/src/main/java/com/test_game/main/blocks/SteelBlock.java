package com.test_game.main.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

public class SteelBlock extends Block {
    public SteelBlock(World world, Texture texture, float x, float y) {
        super(world, texture, x, y);
        setPosition(x,y);

        // Set lower health for glass block - more fragile than standard blocks
        setHealth(40f);  // Reduced from default 100f to represent fragility

    }

    public SteelBlock(int i, float groundHeight) {
        super();
    }
}
