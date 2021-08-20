package com.test.game.entities;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.test.game.utils.Constants;
import com.badlogic.gdx.utils.Array;
import com.test.game.utils.Speed;
import com.test.game.utils.SteeringUtils;

import box2dLight.RayHandler;

import static com.test.game.utils.Constants.PPM;


public class Enemy {
    // Enemy
    private Body body;
    private float hp;
    private float SPEED = 100;
    private int enemy_type;


    // Bullets
    private float damage;
    private float atrito;
    private float distancia;
    private float firerate;
    private float shotSpeed;
    private int cooldown;

    private int SHOOT_TIMER;


    public Enemy() {
        body = null;
        hp = 25;
        cooldown = 0;

        //Bullet
        atrito = .4f;
        distancia = 10;
        damage = 10;
        shotSpeed = 8;
        firerate = 50f;
    }

    public void createEnemy(World world, Vector2 pos){
        this.enemy_type = 1;
        BodyDef bDef = new BodyDef();
        System.out.println(pos);
        bDef.position.set( (pos.x - MathUtils.random(-100,100))/PPM, (pos.y - MathUtils.random(-100,100))/PPM);
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.linearDamping = 10f;
        bDef.fixedRotation = true;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(16/PPM, 16/PPM);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.restitution = 0f;
        fd.density = 1f;
        fd.filter.categoryBits = Constants.BIT_ENEMY;
        fd.filter.maskBits = Constants.BIT_WALL | Constants.BIT_BULLET | Constants.BIT_PLAYER | Constants.BIT_ENEMY;
        body = world.createBody(bDef);
        body.createFixture(fd).setUserData(this);
        shape.dispose();
    }

    public void createShootingEnemy(World world, Vector2 pos){
        this.enemy_type = 2;
        this.SHOOT_TIMER = 0;
        BodyDef bDef = new BodyDef();
        System.out.println(pos);
        bDef.position.set( (pos.x - MathUtils.random(-100,100))/PPM, (pos.y - MathUtils.random(-100,100))/PPM);
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.linearDamping = 20f;
        bDef.fixedRotation = true;

        CircleShape shape = new CircleShape();
        shape.setRadius(16/PPM);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.restitution = 0f;
        fd.density = 1f;
        fd.filter.categoryBits = Constants.BIT_ENEMY;
        fd.filter.maskBits = Constants.BIT_WALL | Constants.BIT_BULLET | Constants.BIT_PLAYER | Constants.BIT_ENEMY;
        body = world.createBody(bDef);
        body.createFixture(fd).setUserData(this);
        shape.dispose();
    }

    public void spawnXEnemy(World world, int i, Vector2 pos, Array<Enemy> enemies) {
        int k = MathUtils.random(0,i);
        for (; k > 0; k--) {
            Enemy enemy = new Enemy();
            if (MathUtils.random(0,50)%3 == 0){
                enemy.createShootingEnemy(world, pos);
            }
            else {
                enemy.createEnemy(world, pos);
            }
            enemies.add(enemy);
        }
    }

    public void enemiesAI(World world, Array<Enemy> enemies, Array<Bullet> bullets, Vector2 player, RayHandler rays){
        for (Enemy enemy:enemies) {
            Vector2 dir = new Vector2((enemy.getBody().getPosition().x - player.x)*-1, (enemy.getBody().getPosition().y - player.y)*-1);
            enemy.cooldown += MathUtils.random(15);
            float dist = Speed.getSpeed(dir.x, dir.y);
            if (dist <= 15 && enemy.enemy_type == 1) {
                enemy.getBody().applyForce(10 * dir.x, 10 * dir.y, dir.x, dir.y, true);
            }
            else if (dist <= 15 && dist > 10 && enemy.enemy_type == 2){
                enemy.getBody().applyForce(15 * dir.x, 15 * dir.y, dir.x, dir.y, true);
                enemy.SHOOT_TIMER += 1;
            }
            else if (dist <= 10 && enemy.enemy_type == 2 && enemy.SHOOT_TIMER <= enemy.firerate){
                enemy.SHOOT_TIMER += 1;
            }
            else if (dist <= 10 && enemy.enemy_type == 2 && enemy.SHOOT_TIMER >= enemy.firerate){
                enemy.SHOOT_TIMER = 0;
                Bullet bala = new Bullet(enemy);
                bala.createEnemyBullet(world, enemy.getPosition(), (int)-dir.y,(int) -dir.x, rays);
                enemy.getBody().applyForce(SPEED*MathUtils.random(-6f,6f), SPEED*MathUtils.random(-6f,6f), MathUtils.random(-1f,1f), MathUtils.random(-1f,1f), true);
                bullets.add(bala);
            }
            else{
                if (enemy.cooldown > 400){
                    enemy.cooldown = 0;
                    enemy.getBody().applyForce(SPEED*MathUtils.random(-6f,6f), SPEED*MathUtils.random(-6f,6f), MathUtils.random(-1f,1f), MathUtils.random(-1f,1f), true);
                }

            }
        }


    }

    public void isDead(World world, Array<Enemy> enemies){
        for (Enemy enemy:enemies) {
            if (enemy.hp <= 0){
                world.destroyBody(enemy.getBody());
                enemies.removeValue(enemy, true);
            }
        }
    }

    public void takeDamage(Bullet bala){
        hp -= bala.dealDamage();
    }

    public float getHP() {
        return hp;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }
    public float getDistance(){
        return distancia;
    }
    public float getAtrito(){
        return atrito;
    }
    public float getDamage(){
        return damage;
    }
    public float getShotSpeed(){
        return shotSpeed;
    }
    public float getFirerate(){
        return firerate;
    }
    public Body getBody(){
        return body;
    }

}

