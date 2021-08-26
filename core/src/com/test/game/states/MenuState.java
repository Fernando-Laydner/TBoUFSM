package com.test.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Timer;
import com.test.game.Teste;
import com.test.game.entities.Player;
import com.test.game.managers.GameStateManager;



public class MenuState extends GameState{

	//Menu
	private final Texture background;
	private final Texture playBtn;
	private final Texture playBtn2;
	private final Texture exit1;
	private final Texture exit2;
	private int selected;

	// Hud
	//private final BitmapFont font;
	//private final OrthographicCamera hud;

	private final float delay = 0.2f;


	public MenuState(GameStateManager gsm) {
		super(gsm);

		selected = 0;
		background = new Texture("img/bg.png");
		playBtn = new Texture("img/playBtn.png");
		playBtn2 = new Texture("img/playBtn2.png");
		exit1 = new Texture("img/exit1.png");
		exit2 = new Texture("img/exit2.png");

		// HUD init
		//font = new BitmapFont();
		//font.setColor(.65f, .15f, .1f, 1f);
		//hud = new OrthographicCamera();
		//hud.setToOrtho(false, 720, 480);

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

		//batch.setProjectionMatrix(hud.combined);
		batch.begin();
		//if (Teste.control >= 8){
			//font.draw(batch, "Parabéns passar do 8° Semestre!", 300, 50 );
		//}
		batch.draw(background, 0, 0, Teste.V_WIDTH, Teste.V_HEIGHT+40);
		batch.draw(playBtn2,Teste.V_WIDTH / 2 - playBtn2.getWidth()/2, Teste.V_HEIGHT / 3);
		batch.draw(exit2, Teste.V_WIDTH/2 - exit1.getWidth()/2, Teste.V_HEIGHT/3 - exit2.getHeight() - 15);
		if (buttonPressed(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, sizeCorrectX(playBtn2.getWidth()), sizeCorrectY(playBtn2.getHeight()))){
			selected = 1;
		}
		if (buttonPressed(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2 + sizeCorrectY(70), sizeCorrectX(exit2.getWidth()), sizeCorrectY(exit2.getHeight()))){
			selected = 2;
		}
		if (selected == 1){
			batch.draw(playBtn,Teste.V_WIDTH / 2 - playBtn2.getWidth()/2, Teste.V_HEIGHT / 3);
			Timer.schedule(new Timer.Task(){
				@Override
				public void run() {
					Teste.control = 1;
				}
			}, delay);
			Teste.player = new Player();
			gsm.setState(GameStateManager.State.DUNGEON);
		}
		if (selected == 2){
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

	private String timer(int totalSecs){

		int hours = totalSecs / 3600;
		int minutes = (totalSecs % 3600) / 60;
		int seconds = totalSecs % 60;

		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}

	@Override
	public void dispose() {
		background.dispose();
		playBtn.dispose();
	}

}