package com.test.game.Items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.test.game.Teste;
import com.test.game.entities.Items;
import com.test.game.entities.Player;

import static com.test.game.utils.Constants.PPM;

public class Oculos extends Items {
    // Imagem do item
  
    private final Texture tex;

    public Oculos(){
        tex = new Texture(Gdx.files.internal("img\\Oculos.png"));
    }

    @Override
    public void render(Batch batch) {
        batch.begin();
        batch.draw(tex, this.body.getPosition().x*PPM-19, this.body.getPosition().y*PPM-20);
        batch.end();
    }

    @Override
    public void itemEffect(Player player) {
        name = "Oculos";
        player.setBouncy(1f);
        Teste.player.setBouncy(1f);
        destroy = true;
    }
}
