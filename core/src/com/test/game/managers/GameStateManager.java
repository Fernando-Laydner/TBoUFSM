package com.test.game.managers;

import com.test.game.Teste;
import com.test.game.states.*;

import java.util.Stack;

public class GameStateManager {

    // Application Reference
    private final Teste app;

    private Stack<GameState> states;

    public GameStateManager(final Teste app) {
        this.app = app;
        this.states = new Stack<>();
        this.setState();
    }

    public Teste application() {
        return app;
    }

    public void update(float delta) {
        states.peek().update(delta);
    }

    public void render() {
        states.peek().render();
    }

    public void dispose() {
        for(GameState gs : states) {
            gs.dispose();
        }
        states.clear();
    }

    public void resize(int w, int h) {
        states.peek().resize(w, h);
    }

    public void setState() {
        if(states.size() >= 1) {
            states.pop().dispose();
        }
        states.push(new DungeonState(this));
    }
}
