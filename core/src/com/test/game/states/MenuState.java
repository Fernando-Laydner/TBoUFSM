package com.test.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import com.test.game.handlers.WorldContactListener;
import com.test.game.managers.GameStateManager;


public class MenuState extends GameState {

	// b2d/lock stuff
	private final Box2DDebugRenderer b2dr;
	private final World world;


	public MenuState(GameStateManager gsm) {
		super(gsm);



		// b2d world init
		world = new World(new Vector2(0f, 0f), false);
		world.setContactListener(new WorldContactListener());


		b2dr = new Box2DDebugRenderer();
	}

	@Override
	public void update(float delta){



			batch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void render() {

		Gdx.gl.glClearColor(.10f, .25f, .10f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		batch.begin();
		batch.end();
	}

	@Override
	public void dispose() {
		b2dr.dispose();
		world.dispose();
	}

}
