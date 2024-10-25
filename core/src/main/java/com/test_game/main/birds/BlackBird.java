package com.test_game.main.birds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class BlackBird extends Bird{
    public BlackBird() {
        super(new Texture(Gdx.files.internal("BlackBird.png")));
    }
}
