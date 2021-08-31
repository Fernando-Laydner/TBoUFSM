package com.test.game.Items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.test.game.entities.Items;
import com.test.game.entities.Player;

import java.util.Stack;


public class ItemSelect extends Items {

    private static final Stack<Integer> items = new Stack<>();

    public static void loadGameItems()
    {
    	items.add(1);
        items.add(2);
        items.add(3);
        items.add(4);
        items.add(5);
        items.add(6);
        items.add(7);
    }

    public static Items itemSelect() {
        int k;
        if (items.size() > 1) {
            k = MathUtils.random(items.size() - 1);
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
                case 5:
                    items.remove(k);
                    return new Donuts();
                case 6:
                    items.remove(k);
                    return new Livro();
                case 7:
                    items.remove(k);
                    return new Peso();
            }
        }
        return new Donuts(); // Mudar isso aqui só por vida, caso acabe a pool dos itens.
    }

    @Override
    public void render(Batch batch) {

    }

    @Override
    public void itemEffect(Player player) { }
}
