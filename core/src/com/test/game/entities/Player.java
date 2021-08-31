package com.test.game.entities;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.test.game.utils.Constants;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;


import static com.test.game.utils.Constants.PPM;

public class Player {

    private TextureAtlas atlas;
    private float hp;
    private float SPEED = 425;
    private TextureRegion current;
    private int semestre;
    private Body body;
    private PointLight light;
    private float time;

    // Animations
    private float animState;
    private TextureRegion frame1, frame2, frame3;
    private Animation<TextureRegion> wLeft;
    private Animation<TextureRegion> wUp;
    private Animation<TextureRegion> wDown;
    private Animation<TextureRegion> aLeft;
    private Animation<TextureRegion> aUp;
    private Animation<TextureRegion> aDown;
    private boolean dirFlip;

    //Bullets
    private float damage;
    private float atrito;
    private float distancia;
    private float firerate;
    private float shotSpeed;
    private float bouncy;
    private boolean diagonal;
    private boolean dead;
    public float SHOOT_TIMER;
    public boolean isHoming;


    public Player(){
    	dirFlip = false;
        dead = false;
        hp = 100;
        SHOOT_TIMER = 0;
        semestre = 1;
        time = 0;

        //Bullet
        atrito = .6f;
        distancia = 10;
        damage = 10;
        bouncy = 0f;
        shotSpeed = 8;
        firerate = .4f;
        isHoming = false;
    }



    public Player(World world, RayHandler rays, Player player) {
        BodyDef bDef = new BodyDef();
        bDef.position.set(0, 0);
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.fixedRotation = true;
        bDef.linearDamping = 20f;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(9/PPM, 16/PPM);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.restitution = 0f;
        fd.density = 1f;
        fd.filter.categoryBits = Constants.BIT_PLAYER;
        fd.filter.maskBits = Constants.BIT_WALL | Constants.BIT_PLAYER | Constants.BIT_ENEMY | Constants.BIT_ENEMY_BULLET | Constants.BIT_SENSOR;
        body = world.createBody(bDef);
        body.createFixture(fd).setUserData(this);

        light = new PointLight(rays, 200, new Color(1f, 1f, 1f, .9f), 7, 0, 0);
        light.setSoftnessLength(0f);
        light.attachToBody(body);

        this.dirFlip = player.dirFlip;
        this.hp = player.hp;
        this.semestre = player.semestre;
        this.SHOOT_TIMER = player.SHOOT_TIMER;

        this.atrito = player.atrito;
        this.distancia = player.distancia;
        this.damage = player.damage;
        this.bouncy = player.bouncy;
        this.shotSpeed = player.shotSpeed;
        this.firerate = player.firerate;
        this.diagonal = player.diagonal;
        this.isHoming = player.isHoming;

        initAnimations();
    }

    private void initAnimations() {
        animState = 0;

        atlas = new TextureAtlas(Gdx.files.internal("img/link.txt"));
        frame1 = atlas.findRegion("down1");
        frame2 = atlas.findRegion("down2");
        wDown = new Animation<>(.175f, frame1, frame2);
        wDown.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        frame1 = atlas.findRegion("left1");
        frame2 = atlas.findRegion("left2");
        wLeft = new Animation<>(.175f, frame1, frame2);
        wLeft.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        frame1 = atlas.findRegion("up1");
        frame2 = atlas.findRegion("up2");
        wUp = new Animation<>(.175f, frame1, frame2);
        wUp.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        frame1 = atlas.findRegion("attack_down1");
        frame2 = atlas.findRegion("attack_down2");
        frame3 = atlas.findRegion("attack_down3");
        aDown = new Animation<>(.045f, frame1, frame2, frame3);
        aDown.setPlayMode(Animation.PlayMode.NORMAL);

        frame1 = atlas.findRegion("attack_up1");
        frame2 = atlas.findRegion("attack_up2");
        frame3 = atlas.findRegion("attack_up3");
        aUp = new Animation<>(.045f, frame1, frame2, frame3);
        aUp.setPlayMode(Animation.PlayMode.NORMAL);

        frame1 = atlas.findRegion("attack_left1");
        frame2 = atlas.findRegion("attack_left2");
        frame3 = atlas.findRegion("attack_left3");
        aLeft = new Animation<>(.045f, frame1, frame2, frame3);
        aLeft.setPlayMode(Animation.PlayMode.NORMAL);

        current = wDown.getKeyFrame(animState);
    }

    public void controller(float delta) {
        // Player Controller
        float x = 0, y = 0;

        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            x -= 1;
            current = wLeft.getKeyFrame(animState, true);
            
            dirFlip = false;
            if(current.isFlipX())
                current.flip(true, false);
        }
        if(Gdx.input.isKeyPressed((Input.Keys.D))) {
            x += 1;
            current = wLeft.getKeyFrame(animState, true);
            dirFlip = true;
            
            if(!current.isFlipX())
                current.flip(true, false);
        }
        if(Gdx.input.isKeyPressed((Input.Keys.W))) {
            y += 1;
            current = wUp.getKeyFrame(animState, true);
        }
        if(Gdx.input.isKeyPressed((Input.Keys.S))) {
            y -= 1;
            current = wDown.getKeyFrame(animState, true);
        }

        // Dampening check
        if(x != 0) {
            body.setLinearVelocity(x * SPEED * delta, body.getLinearVelocity().y);
        }
        if(y != 0) {
            body.setLinearVelocity(body.getLinearVelocity().x, y * SPEED * delta);
        }
        // Walking
        if(y != 0 || x != 0) {
            animState += delta;
        }
    }

    public void shoots(float delta, Array<Bullet> bullets, RayHandler rays, World world){
        SHOOT_TIMER += delta;
        int x = 0, y = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            x += 1;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            x -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            y += 1;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            y -= 1;
        }

        if((x !=0 || y != 0) && SHOOT_TIMER >= firerate && diagonal) {
            SHOOT_TIMER = 0;
            Bullet bala = new Bullet(this);
            bala.createBullet(world, this, x, y, rays);
            bullets.add(bala);
        }
        if(x != 0 && SHOOT_TIMER >= firerate && !diagonal){
            SHOOT_TIMER = 0;
            Bullet bala = new Bullet(this);
            bala.createBullet(world, this, x, 0, rays);
            bullets.add(bala);
        }
        if(y != 0 && SHOOT_TIMER >= firerate && !diagonal){
            SHOOT_TIMER = 0;
            Bullet bala = new Bullet(this);
            bala.createBullet(world, this, 0, y, rays);
            bullets.add(bala);
        }

    }

    public void render(Batch batch) {
        float w = current.getRegionWidth();
        float h = current.getRegionHeight();
        batch.begin();
        batch.draw(current, body.getPosition().x * Constants.PPM - w, body.getPosition().y * Constants.PPM - h,
                0, 0, w, h, 2, 2, 0);
        batch.end();
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
    public float getBouncy(){
        return bouncy;
    }
    public Body getBody(){
        return body;
    }
    public boolean isDiagonal(){return diagonal;}
    public boolean isAlive(){return dead;}
    public TextureRegion getCurrent(){ return current;}
    public int getSemestre(){return semestre;}
    public float getTime(){return time;}

    public void setSPEED(float speed){ SPEED += speed;}
    public void setHoming(boolean bool) {isHoming = bool;}
    public void changeSemestre(){semestre += 1;}
    public void toggleDiagonal(){ diagonal = !diagonal; }
    public void takeBulletDamage(Bullet bala){this.hp -= bala.dealDamage();}
    public void takeContactDamage(Enemy enemy){this.hp -= enemy.getDamage();}
    public void addHp(float hp){this.hp += hp;}
    public void setHp(Player player){this.hp = player.hp;}
    public void addDistance(float distancia){
        this.distancia += distancia;
    }
    public void addAtrito(float atrito){
        this.atrito += atrito;
    }
    public void addDamage(float damage){
        this.damage += damage;
    }
    public void addFirerate(float firerate) {this.firerate += firerate;}
    public void addTime(float delta){time += delta;}
    public void setBouncy(float bouncy){ this.bouncy = bouncy; }
    public void addShotSpeed(float shotSpeed){ this.shotSpeed +=  shotSpeed; }
    public void toggleKill(){ dead = !dead; }
    public void dispose() {
        atlas.dispose();
    }
}
