package com.test_game.main.pigs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.test_game.main.birds.Bird;

public class MediumPig extends Pig {
    public MediumPig(float x ,float y) {
        super(new Texture(Gdx.files.internal("medium_pig.png")));
        resizeTexture(0.17f, 0.17f);
        setPosition(x,y);
    }
}
