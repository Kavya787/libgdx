package com.test_game.main.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class SteelBlock extends Block {
    public SteelBlock(float x , float y ) {
        super(new Texture(Gdx.files.internal("SteelBlock.png"))); // Assuming you have a texture file named steel_block.png
        setPosition(x,y);
//        resizeTexture(0.16f, 0.1f);
    }
}
