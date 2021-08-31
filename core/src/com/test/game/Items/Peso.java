package com.test.game.Items;

import static com.test.game.utils.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.test.game.Teste;
import com.test.game.entities.Items;
import com.test.game.entities.Player;

public class Peso extends Items {

	 private Texture peso;

	    public Peso(){
	        peso = new Texture(Gdx.files.internal("img\\peso.png"));
	    }

	    @Override
	    public void render(Batch batch) {
	        batch.begin();
	        batch.draw(peso, this.body.getPosition().x*PPM-19, this.body.getPosition().y*PPM-20);
	        batch.end();
	    }

	    @Override
	    public void itemEffect(Player player) {
	        name = "Peso";
	        player.setSPEED(-50);
	        Teste.player.setSPEED(-50);
			player.addDamage(15);
			Teste.player.addDamage(15);
	        destroy = true;
	    }

}
