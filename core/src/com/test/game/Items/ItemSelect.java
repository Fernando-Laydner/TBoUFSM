package com.test.game.Items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.test.game.entities.Items;
import com.test.game.entities.Player;

import java.util.Stack;


public class ItemSelect extends Items {

    private static Stack<Integer> items = new Stack<>();

    public static void loadGameItems()
    {
    	items.add(1);
        items.add(2);
        items.add(3);
        items.add(4);
    }

    public static Items itemSelect()
    {
        int k = MathUtils.random(0, items.size()-1);
        switch (items.get(k)) {
            case 1:
                items.remove(k);
                return new Oculos();
            case 2:
                items.remove(k);
                return new Joystick();
            case 3:
                items.remove(k);
                return new Coffee();
            case 4:
                items.remove(k);
                return new PcGamer();
           
        }
        return new Joystick(); // Mudar isso aqui só por vida, caso acabe a pool dos itens.
    }

    @Override
    public void render(Batch batch) {

    }

    @Override
    public void itemEffect(Player player) { }
}
