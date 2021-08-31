package com.test.game.Items;

import static com.test.game.utils.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.test.game.Teste;
import com.test.game.entities.Items;
import com.test.game.entities.Player;

public class Donuts extends Items
{
	 private Texture donut;

	    public Donuts(){
	        donut = new Texture(Gdx.files.internal("img\\donots.png"));
	    }

	    @Override
	    public void render(Batch batch) {
	        batch.begin();
	        batch.draw(donut, this.body.getPosition().x*PPM-19, this.body.getPosition().y*PPM-20);
	        batch.end();
	    }

	    @Override
	    public void itemEffect(Player player) {
	        name = "Donuts";
			player.addHp(50);
			Teste.player.addHp(50);
	        destroy = true;
	    }
}
