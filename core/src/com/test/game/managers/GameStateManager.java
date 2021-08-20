package com.test.game.managers;

import com.test.game.Teste;
import com.test.game.states.*;

import java.util.Stack;

public class GameStateManager {
    private final Teste app;

    private Stack<GameState> states;

    public enum State {
        MENU,
        DUNGEON,
    }

    public GameStateManager(final Teste app) {
        this.app = app;
        this.states = new Stack<GameState>();
        this.setState(State.DUNGEON); // Mudar aqui dps que tiver um menu ou quando tu tiver fazendo testes E tem que adicionar lá em baixo tbm.
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
            //Tem que adicionar o case do menu aqui em baixo tbm
        }
        return null;
    }
}
