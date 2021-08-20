package com.test.game.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.test.game.utils.Constants;
import com.test.game.utils.b2d.BodyBuilder;
import com.badlogic.gdx.utils.Array;

import static com.test.game.utils.Constants.PPM;


public class Enemy {
    // Enemy
    private Body body;
    private float hp;
    private float SPEED = 700;

    // Bullets
    private float damage;
    private float atrito;
    private float distancia;
    private float firerate;
    private float shotSpeed;


    public Enemy() {
        body = null;
        hp = 25;

        //Bullet
        atrito = .4f;
        distancia = 10;
        damage = 2;
        shotSpeed = 8;
        firerate = .4f;
    }

    public void createEnemy(World world, Vector2 pos){
        BodyDef bDef = new BodyDef();
        System.out.println(pos);
        bDef.position.set( (pos.x - MathUtils.random(-100,100))/PPM, (pos.y - MathUtils.random(-100,100))/PPM);
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.linearDamping = 20f;

        CircleShape shape = new CircleShape();
        shape.setRadius(16/PPM);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.restitution = 0f;
        fd.density = 1f;
        fd.filter.categoryBits = Constants.BIT_ENEMY;
        fd.filter.maskBits = Constants.BIT_WALL | Constants.BIT_BULLET | Constants.BIT_PLAYER;
        body = world.createBody(bDef);
        body.createFixture(fd).setUserData(this);
        shape.dispose();
    }

    public void spawnXEnemy(World world, int i, Vector2 pos, Array<Enemy> enemies) {
        int k = (int) MathUtils.random(i);
        for (; i > 0; i--) {
            Enemy enemy = new Enemy();
            enemy.createEnemy(world, pos);
            enemies.add(enemy);
        }
    }

    public void takeDamage(Bullet bala){
        hp -= bala.dealDamage();
    }

    public float getHP() {
        return hp;
    }

    public Body getBody(){
        return body;
    }

}

