package com.test.game.utils;

public class Speed {
    public static float getSpeed(float x, float y){
        return (float) Math.sqrt(Math.exp(Math.abs(x)) + Math.exp(Math.abs(y)));
    }
}
