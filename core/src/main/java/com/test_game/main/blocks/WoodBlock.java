package com.test_game.main.blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

public class WoodBlock extends Block {
    private static final Texture GLASS_TEXTURE = new Texture("WoodBlock.png"); // Set texture
    public WoodBlock(){

    }
    public WoodBlock(World world, float x, float y) {
        super(world, x, y,0.5F,0.5F);
        this.type="wood";
        this.health = 10;
        this.texture = GLASS_TEXTURE;  // Use the glass texture
    }


}
