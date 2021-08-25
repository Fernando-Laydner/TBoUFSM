package com.test.game.states;

//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.test.game.Teste;
//import com.test.game.handlers.WorldContactListener;
import com.test.game.handlers.WorldContactListener;
import com.test.game.managers.GameStateManager;


public class MenuState extends GameState {

	//Menu
	private Texture backgraund;
	private Texture playBtn;
	// b2d
	private final Box2DDebugRenderer b2dr;
	private final World world;


	public MenuState(GameStateManager gsm) {
		super(gsm);

		backgraund = new Texture("img/bg.png");
		playBtn = new Texture("img/playBtn.png");

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

		Gdx.gl.glClearColor(.0f, .0f, .0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.draw(backgraund, 0, 0, Teste.V_WIDTH, Teste.V_HEIGHT);
		batch.draw(playBtn,(Teste.V_WIDTH / 2) - (playBtn.getWidth() / 2), Teste.V_HEIGHT / 2 );
		batch.end();
	}


	@Override
	public void dispose() {
		
	}

}
