package com.test.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.test.game.Teste;
import com.test.game.utils.Constants;
import com.badlogic.gdx.utils.Array;
import com.test.game.utils.Speed;

import box2dLight.RayHandler;

import static com.test.game.utils.Constants.PPM;


public class Enemy {
    // Enemy
    private Body body;
    private float hp;
    private float SPEED;
    private int enemy_type;


    // Bullets
    private float damage;
    private float atrito;
    private float distancia;
    private float firerate;
    private int shotSpeed;
    private int cooldown;

    private int SHOOT_TIMER;

    //inimigos
    
    private Texture ini_bug;
    private Texture ini_polvo;
    private Texture ini_C;
    private Texture ini_php;
    private Texture ini_pyton;
    private Texture ini_codeblocks;
    private Texture ini_linux;
    private Texture ini_haskell;
    private Texture ini_prolog;
    private Texture ini_java;
    

    public Enemy() {
        body = null;
        hp = 25 + 5*Teste.player.getSemestre();
        cooldown = 0;
        SPEED = 7;

        //Bullet
        atrito = .4f;
        distancia = 15;
        damage = 10 + 2*Teste.player.getSemestre();
        shotSpeed = 3;
        firerate = 65f;
        
        //Inimigos
        
        ini_bug = new Texture(Gdx.files.internal("img\\bug.png"));
        ini_polvo= new Texture(Gdx.files.internal("img\\polvo.png"));
        ini_C = new Texture(Gdx.files.internal("img\\C.png"));
        ini_php = new Texture(Gdx.files.internal("img\\php.png"));
        ini_pyton = new Texture(Gdx.files.internal("img\\pyton.png"));
        ini_codeblocks = new Texture(Gdx.files.internal("img\\codeblocks.png"));
        ini_linux = new Texture(Gdx.files.internal("img\\linux.png"));
        ini_haskell = new Texture(Gdx.files.internal("img\\haskell.png"));
        ini_prolog = new Texture(Gdx.files.internal("img\\prolog.png"));
        ini_java = new Texture(Gdx.files.internal("img\\java.png"));
    }

    public void createEnemy(World world, Vector2 pos){
    	
    	
        this.enemy_type = 1;
        firerate = 150;
        BodyDef bDef = new BodyDef();
        bDef.position.set( (pos.x - MathUtils.random(-100,100))/PPM, (pos.y - MathUtils.random(-100,100))/PPM);
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.linearDamping = 0f;
        bDef.fixedRotation = true;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(16/PPM, 16/PPM);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.restitution = 0f;
        //fd.density = 1f;
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
        bDef.linearDamping = .0f;
        bDef.fixedRotation = true;

        CircleShape shape = new CircleShape();
        shape.setRadius(16/PPM);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.restitution = 0f;
        //fd.density = 1f;
        fd.filter.categoryBits = Constants.BIT_ENEMY;
        fd.filter.maskBits = Constants.BIT_WALL | Constants.BIT_BULLET | Constants.BIT_PLAYER | Constants.BIT_ENEMY;
        body = world.createBody(bDef);
        body.createFixture(fd).setUserData(this);
        shape.dispose();
    }

    public void createBoss(World world, Vector2 pos){
        this.enemy_type = 3;
        this.SHOOT_TIMER = 0;
        this.hp = 150;
        this.SPEED = 16;
        BodyDef bDef = new BodyDef();
        System.out.println(pos);
        bDef.position.set( (pos.x - MathUtils.random(-100,100))/PPM, (pos.y - MathUtils.random(-100,100))/PPM);
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.linearDamping = 0f;
        bDef.fixedRotation = true;

        CircleShape shape = new CircleShape();
        shape.setRadius(32/PPM);

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

    public void roaming_AI(Enemy enemy) {
        if (enemy.cooldown > 600){
            enemy.cooldown = 0;
            enemy.body.setLinearDamping(0f);
            //enemy.body.setLinearVelocity(enemy.body.getLinearVelocity().x*-1, enemy.body.getLinearVelocity().y*-1);
            enemy.getBody().applyForce(SPEED*MathUtils.random(-10,10), SPEED*MathUtils.random(-10,10), MathUtils.random(-1,1), MathUtils.random(-1,1), true);
        }
    }

    public boolean following_AI(Enemy enemy, Vector2 dir, float dist_real, float targeting_distance, float keep_distance){
        if (keep_distance != 0){
            enemy.SHOOT_TIMER += 1;
        }
        if (dist_real <= 2){
            enemy.body.setLinearDamping(6f);
            enemy.getBody().applyForce(SPEED * -dir.x, SPEED * -dir.y, -dir.x, -dir.y, true);
            enemy.cooldown = 0;
            return true;
        }
        else if (dist_real <= targeting_distance && dist_real > keep_distance) {
            enemy.body.setLinearDamping(6f);
            enemy.getBody().applyForce(SPEED * dir.x, SPEED * dir.y, dir.x, dir.y, true);
            enemy.cooldown = 0;
            return true;
        }
        return false;
    }

    public boolean shooting_AI(World world, Enemy enemy, Array<Bullet> bullets, Vector2 dir, float dist, RayHandler rays){
        if (enemy.SHOOT_TIMER >= enemy.firerate && dist < 10) {
            enemy.SHOOT_TIMER = 0;
            Bullet bala = new Bullet(enemy);
            bala.createEnemyBullet(world, enemy.getPosition(), (int) -dir.y, (int) -dir.x, rays);
            enemy.body.setLinearVelocity(0,0);
            enemy.body.applyForce(SPEED * MathUtils.random(-1, 1), SPEED * MathUtils.random(-1, 1), MathUtils.random(-1, 1), MathUtils.random(-1, 1), true);
            bullets.add(bala);
            return true;
        }
        enemy.SHOOT_TIMER += 1;
        return false;
    }

    public boolean shooting4_AI(World world, Enemy enemy, Array<Bullet> bullets, RayHandler rays){
        if (enemy.SHOOT_TIMER >= enemy.firerate) {
            enemy.SHOOT_TIMER = 0;
            Bullet bala1 = new Bullet(enemy);
            bala1.createEnemyBullet(world, enemy.getPosition(), (int) shotSpeed, (int) 0, rays);
            bullets.add(bala1);
            Bullet bala2 = new Bullet(enemy);
            bala2.createEnemyBullet(world, enemy.getPosition(), (int) 0, (int) shotSpeed, rays);
            bullets.add(bala2);
            Bullet bala3 = new Bullet(enemy);
            bala3.createEnemyBullet(world, enemy.getPosition(), (int) -shotSpeed, (int) 0, rays);
            bullets.add(bala3);
            Bullet bala4 = new Bullet(enemy);
            bala4.createEnemyBullet(world, enemy.getPosition(), (int) 0, (int) -shotSpeed, rays);
            bullets.add(bala4);
            return true;
        }
        enemy.SHOOT_TIMER += 1;
        return false;
    }

    public boolean shooting8_AI(World world, Enemy enemy, Array<Bullet> bullets, RayHandler rays){
        if (enemy.SHOOT_TIMER >= enemy.firerate) {
            enemy.SHOOT_TIMER = 0;
            Bullet bala1 = new Bullet(enemy);
            bala1.createEnemyBullet(world, enemy.getPosition(), (int) shotSpeed, (int) 0, rays);
            bullets.add(bala1);
            Bullet bala2 = new Bullet(enemy);
            bala2.createEnemyBullet(world, enemy.getPosition(), (int) 0, (int) shotSpeed, rays);
            bullets.add(bala2);
            Bullet bala3 = new Bullet(enemy);
            bala3.createEnemyBullet(world, enemy.getPosition(), (int) -shotSpeed, (int) 0, rays);
            bullets.add(bala3);
            Bullet bala4 = new Bullet(enemy);
            bala4.createEnemyBullet(world, enemy.getPosition(), (int) 0, (int) -shotSpeed, rays);
            bullets.add(bala4);
            Bullet bala5 = new Bullet(enemy);
            bala5.createEnemyBullet(world, enemy.getPosition(), (int) -shotSpeed, (int) -shotSpeed, rays);
            bullets.add(bala5);
            Bullet bala6 = new Bullet(enemy);
            bala6.createEnemyBullet(world, enemy.getPosition(), (int) -shotSpeed, (int) shotSpeed, rays);
            bullets.add(bala6);
            Bullet bala7 = new Bullet(enemy);
            bala7.createEnemyBullet(world, enemy.getPosition(), (int) shotSpeed, (int) shotSpeed, rays);
            bullets.add(bala7);
            Bullet bala8 = new Bullet(enemy);
            bala8.createEnemyBullet(world, enemy.getPosition(), (int) shotSpeed, (int) -shotSpeed, rays);
            bullets.add(bala8);
            return true;
        }
        enemy.SHOOT_TIMER += 1;
        return false;
    }

    public boolean shooting4x_AI(World world, Enemy enemy, Array<Bullet> bullets, RayHandler rays){
        if (enemy.SHOOT_TIMER >= enemy.firerate) {
            enemy.SHOOT_TIMER = 0;
            Bullet bala1 = new Bullet(enemy);
            bala1.createEnemyBullet(world, enemy.getPosition(), (int) shotSpeed, (int) shotSpeed, rays);
            bullets.add(bala1);
            Bullet bala2 = new Bullet(enemy);
            bala2.createEnemyBullet(world, enemy.getPosition(), (int) -shotSpeed, (int) -shotSpeed, rays);
            bullets.add(bala2);
            Bullet bala3 = new Bullet(enemy);
            bala3.createEnemyBullet(world, enemy.getPosition(), (int) -shotSpeed, (int) shotSpeed, rays);
            bullets.add(bala3);
            Bullet bala4 = new Bullet(enemy);
            bala4.createEnemyBullet(world, enemy.getPosition(), (int) shotSpeed, (int) -shotSpeed, rays);
            bullets.add(bala4);
            return true;
        }
        enemy.SHOOT_TIMER += 1;
        return false;
    }

    public boolean charging_AI(){
        // Charges player
        return false;
    }

    public void AI_Selection(World world, Array<Enemy> enemies, Array<Bullet> bullets, Vector2 player, RayHandler rays) {
        for (Enemy enemy : enemies) {
            Vector2 dir = new Vector2((enemy.getBody().getPosition().x - player.x)*-1, (enemy.getBody().getPosition().y - player.y)*-1);
            enemy.cooldown += MathUtils.random(15);
            float dist = Speed.getSpeed(dir.x, dir.y);
            if (enemy.enemy_type == 1){
                if(!following_AI(enemy, dir, dist, 20, 0));
                    roaming_AI(enemy);
            }
            else if (enemy.enemy_type == 2){
                if(!shooting_AI(world, enemy, bullets, dir, dist, rays) && !following_AI(enemy, dir, dist, 17, 8));
                roaming_AI(enemy);
            }
            else if (enemy.enemy_type == 3) {
                roaming_AI(enemy);
                if (MathUtils.random(100) < 30){
                    shooting8_AI(world, enemy, bullets, rays);
                }
                else if (MathUtils.random(100) < 65){
                    shooting4x_AI(world, enemy, bullets, rays);
                }
                shooting4_AI(world, enemy, bullets, rays);
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

    public void render(Batch batch) 
    {
    	 batch.begin();
    	 
         batch.draw(ini_bug, this.body.getPosition().x*PPM, this.body.getPosition().y*PPM);
         batch.draw(ini_polvo, this.body.getPosition().x*PPM, this.body.getPosition().y*PPM);
         batch.draw(ini_C, this.body.getPosition().x*PPM, this.body.getPosition().y*PPM);
         batch.draw(ini_php, this.body.getPosition().x*PPM, this.body.getPosition().y*PPM);
         batch.draw(ini_pyton, this.body.getPosition().x*PPM, this.body.getPosition().y*PPM);
         batch.draw(ini_codeblocks, this.body.getPosition().x*PPM, this.body.getPosition().y*PPM);
         batch.draw(ini_linux, this.body.getPosition().x*PPM, this.body.getPosition().y*PPM);
         batch.draw(ini_haskell, this.body.getPosition().x*PPM, this.body.getPosition().y*PPM);
         batch.draw(ini_prolog, this.body.getPosition().x*PPM, this.body.getPosition().y*PPM);
         batch.draw(ini_java, this.body.getPosition().x*PPM, this.body.getPosition().y*PPM);

         batch.end();
 
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
    public void dispose() 
    {
		ini_bug.dispose();
		ini_C.dispose();
		ini_java.dispose();
		ini_linux.dispose();
		ini_php.dispose();
		ini_pyton.dispose();
		ini_haskell.dispose();
		ini_prolog.dispose();
		ini_polvo.dispose();
		ini_codeblocks.dispose();
	}
}

