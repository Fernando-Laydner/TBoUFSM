package com.test.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.test.game.Items.ItemSelect;
import com.test.game.entities.Player;
import com.test.game.managers.GameStateManager;

public class Teste extends ApplicationAdapter {

	// DEBUG
	public static boolean DEBUG = false;

	// Game Information
	public static final String TITLE = "Trabalho";
	public static final int V_WIDTH = 720;
	public static final int V_HEIGHT = 480;
	public static final float SCALE = 1f;

	public static Player player;
	public static int control;

	public static AssetManager assets;

	private OrthographicCamera camera;
	private SpriteBatch batch;

	private GameStateManager gsm;

	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();


		assets = new AssetManager();
		assets.load("img/switch.png", Texture.class);

		player = new Player();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, w/SCALE, h/SCALE );

		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);

		control = 1;
		gsm = new GameStateManager(this);
	}

	@Override
	public void render() {
		if(assets.update()) {
			gsm.update(Gdx.graphics.getDeltaTime());
			gsm.render();
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) && DEBUG){
			ItemSelect.loadGameItems();
			gsm.setState(GameStateManager.State.DUNGEON);
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) && DEBUG){
			gsm.setState(GameStateManager.State.MENU);
		}

		if (player.getSemestre() != control){
			if (player.getSemestre() <= 8) {
				control += 1;
				gsm.setState(GameStateManager.State.DUNGEON);
			}
			else{
				gsm.setState(GameStateManager.State.MENU);
			}
		}

		if (player.isAlive()) {
			player.toggleKill();
			player = new Player();
			ItemSelect.loadGameItems();
			gsm.setState(GameStateManager.State.DUNGEON);
		}
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


