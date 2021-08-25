package com.test.game.Items;

import com.badlogic.gdx.math.MathUtils;
import com.test.game.entities.Items;
import com.test.game.entities.Player;


import java.util.Stack;


public class ItemSelect extends Items {

    private static Stack<Integer> items = new Stack<>();

    public static int loadGameItems(){
        items.add(1);
        items.add(2);
        return items.size();
    }

    public static Items itemSelect(){
        int k = MathUtils.random(0, items.size()-1);
        switch (items.get(k)) {
            case 1:
                items.remove(k);
                return new Oculos();
            case 2:
                items.remove(k);
                return new Joystick();
        }
        return null;
    }

    @Override
    public void itemEffect(Player player) { }
}
