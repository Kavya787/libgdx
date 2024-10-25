package com.test_game.main.pigs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.test_game.main.birds.Bird;

public class SmallPig extends Pig {
    public SmallPig(float x ,float y) {
        super(new Texture(Gdx.files.internal("small_pig.png")));
        resizeTexture(0.14f, 0.14f);
        setPosition(x,y);
    }
}
