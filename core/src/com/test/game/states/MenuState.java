package com.test.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.test.game.Teste;
import com.test.game.managers.GameStateManager;



public class MenuState extends GameState {

	//Menu
	private final Texture background;
	private final Texture playBtn;
	private final Texture playBtn2;


	public MenuState(GameStateManager gsm) {
		super(gsm);

		background = new Texture("img/bg.png");
		playBtn = new Texture("img/playBtn.png");
		playBtn2 = new Texture("img/playBtn2.png");

		camera.position.set(Teste.V_WIDTH/2,Teste.V_HEIGHT/2, 0);
	}


	@Override
	public void update(float delta){
		camera.update();
		batch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void render() {

		Gdx.gl.glClearColor(.0f, .0f, .0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.draw(background, 0, 0, Teste.V_WIDTH, Teste.V_HEIGHT+40);
		batch.draw(playBtn2,(Teste.V_WIDTH / 2) - (playBtn.getWidth() / 2), Teste.V_HEIGHT / 3, playBtn2.getWidth(), playBtn2.getHeight());
		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
			batch.draw(playBtn,(Teste.V_WIDTH / 2) - (playBtn.getWidth() / 2), Teste.V_HEIGHT / 3);
			//gsm.setState(GameStateManager.State.DUNGEON);
		}
		batch.end();
	}


	@Override
	public void dispose() {
		background.dispose();
		playBtn.dispose();
	}

}
