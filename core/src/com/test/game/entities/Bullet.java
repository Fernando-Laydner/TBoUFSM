package com.test.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.test.game.utils.Constants;
import com.test.game.utils.Speed;
import com.test.game.utils.b2d.BodyBuilder;
import com.badlogic.gdx.utils.Array;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import static com.test.game.utils.Constants.PPM;

public class Bullet {

    private Body b;
    private PointLight currentTorch;
    private Vector2 bullet;
    private float distance;
    private float atrito;
    private float damage;
    private float bouncy;

    public Bullet(Player player){
        b = null;
        currentTorch = null;
        bullet = null;
        distance = player.getDistance();
        atrito = player.getAtrito();
        damage = player.getDamage();
        bouncy = player.getBouncy();
    }

    public void createBullet(World world, Vector2 position, int vert, int horiz, RayHandler rays){
        b = BodyBuilder.createCircle(world, position.x*PPM + Integer.signum(horiz)*16, position.y*PPM + Integer.signum(vert)*16, 4, false, false,
                Constants.BIT_WALL, Constants.BIT_PLAYER, (short) 0);
        b.setLinearDamping(atrito);
        b.getFixtureList().get(0).setRestitution(bouncy);
        bullet = new Vector2(distance*Integer.signum(horiz), distance*Integer.signum(vert));
        b.setLinearVelocity(bullet);
        Filter f = new Filter();
        f.categoryBits = Constants.BIT_SENSOR;
        f.maskBits = Constants.BIT_PLAYER | Constants.BIT_WALL;
        b.getFixtureList().get(0).setFilterData(f);
        currentTorch = new PointLight(rays, 15, new Color(.4f, .1f, .6f, .7f), 2, 0, 0);
        currentTorch.setSoftnessLength(0f);
        currentTorch.attachToBody(b);
    }

    public void destroyBullet(World world, Array<Bullet> bullets){
        for (Bullet bala: bullets) {
            if (Speed.getSpeed(bala.b.getLinearVelocity().x, bala.b.getLinearVelocity().y) < 14) {
                bala.currentTorch.setDistance(0f);
                world.destroyBody(bala.b);
                bala.bullet = null;
                bullets.removeValue(bala, true);
            }
        }
    }
}
