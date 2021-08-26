package com.test.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;
import com.test.game.Teste;
import com.test.game.managers.GameStateManager;



public class MenuState extends GameState{

	//Menu
	private final Texture background;
	private final Texture playBtn;
	private final Texture playBtn2;
	private final Texture exit1;
	private final Texture exit2;

	private final float delay = 0.2f;


	public MenuState(GameStateManager gsm) {
		super(gsm);

		background = new Texture("img/bg.png");
		playBtn = new Texture("img/playBtn.png");
		playBtn2 = new Texture("img/playBtn2.png");

		exit1 = new Texture("img/exit1.png");
		exit2 = new Texture("img/exit2.png");

		camera.position.set(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2, 0);
	}

	@Override
	public void update(float delta){
		camera.update();
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
		batch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(.0f, .0f, .0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		resize(Teste.V_WIDTH, Teste.V_HEIGHT);
		batch.begin();
		batch.draw(background, 0, 0, Teste.V_WIDTH, Teste.V_HEIGHT+40);
		batch.draw(playBtn2,Teste.V_WIDTH / 2 - playBtn2.getWidth()/2, Teste.V_HEIGHT / 3);
		batch.draw(exit2, Teste.V_WIDTH/2 - exit1.getWidth()/2, Teste.V_HEIGHT/3 - exit2.getHeight() - 15);
		if (buttonPressed(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, sizeCorrectX(playBtn2.getWidth()), sizeCorrectY(playBtn2.getHeight()))){
			batch.draw(playBtn,Teste.V_WIDTH / 2 - playBtn2.getWidth()/2, Teste.V_HEIGHT / 3);
			Timer.schedule(new Timer.Task(){
				@Override
				public void run() {
					gsm.setState(GameStateManager.State.DUNGEON);
				}
			}, delay);
		}
		if (buttonPressed(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2 + sizeCorrectY(70), sizeCorrectX(exit2.getWidth()), sizeCorrectY(exit2.getHeight()))){
			batch.draw(exit1,Teste.V_WIDTH / 2 - exit1.getWidth()/2, Teste.V_HEIGHT / 3 - exit1.getHeight() - 15);
			Timer.schedule(new Timer.Task(){
				@Override
				public void run() {
					Gdx.app.exit();
				}
			}, delay);

		}
		batch.end();
	}

	public boolean buttonPressed(float x, float y, float width, float height){
		return Gdx.input.isButtonPressed(Input.Keys.LEFT) && Gdx.input.getX() >=  x - width/2 && Gdx.input.getX() <= x + width/2 && Gdx.input.getY() >= y + height && Gdx.input.getY() <= y + height*2;
	}

	public float sizeCorrectX(float x){
		return x*((Gdx.graphics.getWidth()+80)/Teste.V_WIDTH);
	}

	public float sizeCorrectY(float y){
		return y*(Gdx.graphics.getHeight()/Teste.V_HEIGHT);
	}

	@Override
	public void dispose() {
		background.dispose();
		playBtn.dispose();
	}

}