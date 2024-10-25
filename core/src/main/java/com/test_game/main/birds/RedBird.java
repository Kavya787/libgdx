package com.test_game.main.birds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class RedBird extends Bird{
    public RedBird() {
        super(new Texture(Gdx.files.internal("red_bird.png")));
    }
}
