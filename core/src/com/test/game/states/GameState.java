package com.test.game.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.test.game.Teste;
import com.test.game.managers.GameStateManager;

public abstract class GameState {

    // References
    protected GameStateManager gsm;
    protected Teste app;
    protected SpriteBatch batch;
    protected OrthographicCamera camera;

    protected GameState(GameStateManager gsm) {
        this.gsm = gsm;
        this.app = gsm.application();
        batch = app.getBatch();
        camera = app.getCamera();
    }

    public void resize(int w, int h) {
        camera.setToOrtho(false, w, h+40);
    }

    public abstract void update(float delta);
    public abstract void render();
    public abstract void dispose();
}
