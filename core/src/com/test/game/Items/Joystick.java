package com.test.game.Items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.test.game.Teste;
import com.test.game.entities.Items;
import com.test.game.entities.Player;

public class Joystick extends Items {
        // Imagem do item
        private final int ID = 2;

        public Joystick(){

        }

    @Override
    public void render(Batch batch) {

    }

    @Override
        public void itemEffect(Player player) {
            name = "Joystick";
            Teste.player.toggleDiagonal();
            player.toggleDiagonal();
            destroy = true;
        }
}
