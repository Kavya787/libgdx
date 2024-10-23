package com.test_game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class HomeScreen extends ScreenAdapter {
    private Stage stage;
    private Skin skin;
    private Texture backgroundTexture;

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        backgroundTexture = new Texture(Gdx.files.internal("homescreen.jpg"));
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton newGameButton = new TextButton("New Game", skin);
        TextButton loadGameButton = new TextButton("Load Saved Game", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        table.add(newGameButton).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(loadGameButton).fillX().uniformX();
        table.row();
        table.add(exitButton).fillX().uniformX();

        newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Core) Gdx.app.getApplicationListener()).setScreen(new SelectLevelScreen());
            }
        });



        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void render(float delta) {
        stage.getBatch().begin();
        stage.getBatch().draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getBatch().end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
        stage.dispose();
        skin.dispose();
        backgroundTexture.dispose();
    }
}

