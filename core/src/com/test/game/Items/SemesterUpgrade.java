package com.test.game.Items;

import com.test.game.Teste;
import com.test.game.entities.Items;
import com.test.game.entities.Player;

public class SemesterUpgrade extends Items {


    @Override
    public void itemEffect(Player player) {
        Teste.player.changeSemestre();
    }
}
