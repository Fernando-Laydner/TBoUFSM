package com.test.game.Items;

import static com.test.game.utils.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.test.game.Teste;
import com.test.game.entities.Items;
import com.test.game.entities.Player;

public class Livro extends Items {

	 private Texture book;

	    public Livro(){
	        book = new Texture(Gdx.files.internal("img\\livro.png"));
	    }

	    @Override
	    public void render(Batch batch) {
	        batch.begin();
	        batch.draw(book, this.body.getPosition().x*PPM-19, this.body.getPosition().y*PPM-20);
	        batch.end();
	    }

	    @Override
	    public void itemEffect(Player player) {
	        name = "Livro";
	        player.setHoming(true);
	        Teste.player.setHoming(true);
	        player.addShotSpeed(0.2f);
	        Teste.player.addShotSpeed(.2f);
			player.addDamage(5);
			Teste.player.addDamage(5);
	        destroy = true;
	    }

}
