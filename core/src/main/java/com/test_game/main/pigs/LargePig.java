package com.test_game.main.pigs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.test_game.main.birds.Bird;

public class LargePig extends Pig {
    public LargePig(float x ,float y) {
        super(new Texture(Gdx.files.internal("king_pig.png")));
        resizeTexture(0.19f, 0.19f);
        setPosition(x,y);
    }
}
