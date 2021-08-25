package com.test.game.Items;

import com.test.game.entities.Items;
import com.test.game.entities.Player;

public class Joystick extends Items {
        // Imagem do item
        private final int ID = 2;

        @Override
        public void itemEffect(Player player) {
            name = "Joystick";
            player.toggleDiagonal();
            destroy = true;
        }
}
