package com.test_game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PlayScreen extends ScreenAdapter {
    private final Core game;
    private final Viewport gamePort;
    private final Texture texture;

    public PlayScreen(Core game) {
        this.game = game;
        this.gamePort = new FitViewport(800, 600); // Adjust the viewport size as needed
        this.texture = new Texture("badlogic.jpg");
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        game.batch.setProjectionMatrix(gamePort.getCamera().combined);
        game.batch.begin();
        game.batch.draw(texture, 0, 0);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void hide() {
        texture.dispose();
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
