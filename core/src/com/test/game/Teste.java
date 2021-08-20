package com.test.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.test.game.managers.GameStateManager;

public class Teste extends ApplicationAdapter {

	// DEBUG
	public static boolean DEBUG = false;

	// Game Information
	public static final String TITLE = "Trabalho";
	public static final int V_WIDTH = 720;
	public static final int V_HEIGHT = 480;
	public static final float SCALE = 1f;

	public static boolean dead;

	public static AssetManager assets;

	private OrthographicCamera camera;
	private SpriteBatch batch;

	private GameStateManager gsm;

	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		//ashley = new Engine();

		assets = new AssetManager();
		assets.load("img/switch.png", Texture.class);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, w/SCALE, h-10/SCALE );

		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);

		gsm = new GameStateManager(this);
	}

	@Override
	public void render() {
		if(assets.update()) {
			gsm.update(Gdx.graphics.getDeltaTime());
			gsm.render();
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) || dead) {
			dead = false;
			gsm.setState(GameStateManager.State.DUNGEON);
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
	}

	@Override
	public void resize(int width, int height) {
		gsm.resize((int) (720/SCALE), (int) (480/ SCALE));
	}

	@Override
	public void dispose() {
		gsm.dispose();
		batch.dispose();
	}

	public OrthographicCamera getCamera() {
		return camera;
	}
	public SpriteBatch getBatch() {
		return batch;
	}
}


