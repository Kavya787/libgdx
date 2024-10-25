package com.test_game.main.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class WoodBlock extends Block {
    public WoodBlock(float x , float y ) {
        super(new Texture(Gdx.files.internal("woodPlank.png"))); // Assuming you have a texture file named steel_block.png
        setPosition(x,y);
    }
}
