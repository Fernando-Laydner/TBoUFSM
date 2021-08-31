package com.test.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
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
    private float shotspeed;
    private float cooldown;

    private Texture textura;

    public Bullet(Player player){
        b = null;
        currentTorch = null;
        bullet = null;
        distance = player.getDistance();
        atrito = player.getAtrito();
        damage = player.getDamage();
        bouncy = player.getBouncy();
        shotspeed = player.getShotSpeed();
        cooldown = 0;
    }

    public Bullet(Enemy enemy){
        b = null;
        currentTorch = null;
        bullet = null;
        distance = enemy.getDistance();
        atrito = enemy.getAtrito();
        damage = enemy.getDamage();
        shotspeed = enemy.getShotSpeed();
    }

    public void createBullet(World world, Player player, int vert, int horiz, RayHandler rays){
        BodyDef bDef = new BodyDef();
        bDef.position.set(player.getPosition().x + Integer.signum(horiz), player.getPosition().y + Integer.signum(vert));
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.linearDamping = atrito;

        CircleShape shape = new CircleShape();
        shape.setRadius(4/PPM);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.restitution = bouncy;
        fd.density = 1f;
        fd.filter.categoryBits = Constants.BIT_BULLET;
        fd.filter.maskBits = Constants.BIT_WALL | Constants.BIT_ENEMY;
        b = world.createBody(bDef);
        b.createFixture(fd).setUserData(this);

        bullet = new Vector2(shotspeed*Integer.signum(horiz) + player.getBody().getLinearVelocity().x*2/PPM, shotspeed*Integer.signum(vert) + player.getBody().getLinearVelocity().y*2/PPM);
        b.setLinearVelocity(bullet);

        currentTorch = new PointLight(rays, 15, new Color(.4f, .1f, .6f, .7f), 2, 0, 0);
        currentTorch.attachToBody(b);
        textura = new Texture((Gdx.files.internal("img\\tiro_2.png")));
    }


    public void createEnemyBullet(World world, Vector2 position, int vert, int horiz, RayHandler rays){
        BodyDef bDef = new BodyDef();
        bDef.position.set(position.x, position.y);
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.linearDamping = atrito;

        CircleShape shape = new CircleShape();
        shape.setRadius(4/PPM);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.restitution = 0;
        fd.density = 1f;
        fd.filter.categoryBits = Constants.BIT_ENEMY_BULLET;
        fd.filter.maskBits = Constants.BIT_WALL | Constants.BIT_PLAYER;
        b = world.createBody(bDef);
        b.createFixture(fd).setUserData(this);

        bullet = new Vector2(-shotspeed*horiz, -shotspeed*vert);
        b.setLinearVelocity(bullet);

        currentTorch = new PointLight(rays, 15, new Color(.6f, .1f, .1f, .7f), 2, 0, 0);
        currentTorch.setSoftnessLength(0f);
        currentTorch.attachToBody(b);
        textura = new Texture((Gdx.files.internal("img\\tiro_2.png")));
    }

    public void destroyBullet(World world, Array<Bullet> bullets){
        for (Bullet bala: bullets) {
            if (Speed.getSpeed(bala.b.getLinearVelocity().x, bala.b.getLinearVelocity().y) < distance) {
                bala.currentTorch.remove(true);
                world.destroyBody(bala.b);
                bullets.removeValue(bala, true);
            }
        }
    }

    public void clearAllBullets(World world, Array<Bullet> bullets){
        while (!bullets.isEmpty())
            for (Bullet bala: bullets) {
                bala.currentTorch.remove(true);
                world.destroyBody(bala.b);
                bullets.removeValue(bala, true);
            }
    }

    public float dealDamage(){
        this.b.setLinearDamping(5000f);
        return damage;
    }

    public void render(Batch batch) {
        batch.begin();
        batch.draw(textura, this.b.getPosition().x*PPM-19, this.b.getPosition().y*PPM-20);
        batch.end();
    }

    // [WIP] Homing for the bullets.
    public void Homing(Array<Enemy> enemies){
        float dist, dist_final = 0;
        Vector2 dir = new Vector2(0,0), dir_temp = new Vector2(0,0);
        for (Enemy enemy: enemies) {
            dir_temp.set(bullet.x - enemy.getBody().getPosition().x*-1, (bullet.y - enemy.getBody().getPosition().y)*-1);
            dist = Speed.getSpeed(dir_temp.x, dir_temp.y);
            System.out.println("dir.x");
            if (dist < dist_final){
                dir = dir_temp;
            }
        }

        //this.b.applyForce(500 * dir.x, 500 * dir.y, dir.x, dir.y, true);
        this.b.setLinearVelocity((500 * dir.x)*PPM, (500 * dir.y)*PPM);
    }
}
