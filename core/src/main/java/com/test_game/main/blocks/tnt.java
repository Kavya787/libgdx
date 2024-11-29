package com.test_game.main.blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;

public class tnt extends Block {
    //    private static final Texture GLASS_TEXTURE = new Texture("tnt.png"); // Set texture
    private static final TextureRegion LOG_TEXTURE_REGION = new TextureRegion(new Texture("tnt.png")); // Set texture region

    public tnt(){

    }
    public tnt(World world, float x, float y) {
        super(world, x, y, 0.5F, 0.5F);
        this.type="tnt";
        this.health = 3;
        this.textureRegion = LOG_TEXTURE_REGION;   // Use the glass texture
    }


}
