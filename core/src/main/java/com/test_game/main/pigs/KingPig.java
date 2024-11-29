package com.test_game.main.Pigs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.test_game.main.Birds.Bird;

public class KingPig extends Pig{
    public KingPig(){

    }
    public KingPig(World world, float x, float y) {
        super(world, x, y, 2f);
        this.type="king";
        this.health = 9;
        this.texture = new Texture("pigKing.png");
    }
}


