package com.test.game.Items;

import com.test.game.entities.Items;
import com.test.game.entities.Player;

public class Oculos extends Items {
    // Imagem do item
    private final int ID = 1;


    @Override
    public void itemEffect(Player player) {
        name = "Oculos";
        player.setBouncy(1f);
        destroy = true;
    }
}
