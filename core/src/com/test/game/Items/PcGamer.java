package com.test.game.Items;

import static com.test.game.utils.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.test.game.Teste;
import com.test.game.entities.Items;
import com.test.game.entities.Player;

public class PcGamer extends Items {
	 // Imagem do item
    
    private Texture pc;

    public PcGamer(){
        pc = new Texture(Gdx.files.internal("img\\pcGamer.png"));
    }

    @Override
    public void render(Batch batch) {
        batch.begin();
        batch.draw(pc, this.body.getPosition().x*PPM-19, this.body.getPosition().y*PPM-20);
        batch.end();
    }

    @Override
    public void itemEffect(Player player) {
        name = "PcGamer";
        player.addHp(25);
        Teste.player.addHp(25);
        player.addDamage(5);
        Teste.player.addDamage(5);
        player.addFirerate(0.2f);
        Teste.player.addFirerate(0.2f);
        destroy = true;
    }
}
