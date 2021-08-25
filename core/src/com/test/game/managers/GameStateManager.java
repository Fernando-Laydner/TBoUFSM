package com.test.game.managers;

import com.test.game.Teste;
import com.test.game.states.*;

import java.util.Stack;

public class GameStateManager {
    private final Teste app;

    private final Stack<GameState> states;

    public enum State {
        MENU,
        DUNGEON,
    }

    public GameStateManager(final Teste app) {
        this.app = app;
        this.states = new Stack<>();
        this.setState(State.MENU);
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

    public void setState(State state) {
        if(states.size() >= 1) {
            states.pop().dispose();
        }
        states.push(getState(state));
    }

    private GameState getState(State state) {
        switch(state) {
            case DUNGEON: return new DungeonState(this);
            case MENU: return new MenuState(this);
        }
        return null;
    }
}
