package com.test.game.Items;

import static com.test.game.utils.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.test.game.Teste;
import com.test.game.entities.Items;
import com.test.game.entities.Player;

public class Coffee extends Items {
	 // Imagem do item
    
    private Texture coff;

    public Coffee(){
        coff = new Texture(Gdx.files.internal("img\\Coffee.png"));
    }

    @Override
    public void render(Batch batch) {
        batch.begin();
        batch.draw(coff, this.body.getPosition().x*PPM-19, this.body.getPosition().y*PPM-20);
        batch.end();
    }

    @Override
    public void itemEffect(Player player) {
        name = "Coffee";
        player.addShotSpeed(2);
        player.addFirerate(0.2f);
        Teste.player.addShotSpeed(2);
        Teste.player.addFirerate(0.2f);
        destroy = true;
    }
}
