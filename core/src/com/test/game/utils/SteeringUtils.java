package com.test.game.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;

public final class SteeringUtils {

    private SteeringUtils () {
    }

    public static float vectorToAngle (Vector2 vector) {
        return (float)MathUtils.atan2(-vector.x, vector.y);
    }

    public static Vector2 angleToVector (Vector2 outVector, float angle) {
        outVector.x = -(float)MathUtils.sin(angle);
        outVector.y = (float)MathUtils.cos(angle);
        return outVector;
    }
}
