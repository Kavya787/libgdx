package com.test_game.main.levels;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.test_game.main.Catapult;
import com.test_game.main.Core;
import com.test_game.main.birds.Bird;
import com.test_game.main.birds.BlackBird;
import com.test_game.main.birds.RedBird;
import com.test_game.main.birds.YellowBird;
import com.test_game.main.blocks.Block;
import com.test_game.main.blocks.GlassBlock;
import com.test_game.main.blocks.SteelBlock;
import com.test_game.main.blocks.WoodBlock;
import com.test_game.main.pigs.Pig;
import com.test_game.main.pigs.*;
import com.test_game.main.Screens.*;
import java.util.ArrayList;
import java.util.List;

public class Level extends ScreenAdapter {
    Stage stage;
    SpriteBatch batch;

    // Lists for Pigs, Birds, and Blocks
    List<Pig> pigs;
    List<Block> blocks;
    List<Bird> birds;

    Texture birdTexture;
    Texture backgroundTexture;
    Texture groundTexture;

    Catapult catapult;

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        handleKeyboardInput();

        // Start the batch and draw the background, then the ground
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(groundTexture, 0, 0, Gdx.graphics.getWidth(), groundTexture.getHeight());
        batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
        // Update pause button position
        TextButton pauseButton = (TextButton) stage.getActors().first();
        pauseButton.setPosition(width - 100, height - 50);
    }

    private void handleKeyboardInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            ((Core) Gdx.app.getApplicationListener()).setScreen(new WinScreen());
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            ((Core) Gdx.app.getApplicationListener()).setScreen(new LoseScreen());
        }
    }

    @Override
    public void hide() {
        stage.dispose();
        birdTexture.dispose();
        backgroundTexture.dispose();
        groundTexture.dispose();
    }
}
