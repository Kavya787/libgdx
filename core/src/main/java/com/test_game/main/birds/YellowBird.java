package com.test_game.main.birds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class YellowBird extends Bird{
    public YellowBird() {
        super(new Texture(Gdx.files.internal("yellow_bird.png")));
    }
}
