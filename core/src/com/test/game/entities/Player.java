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
    private float hp, mp;
    private float SPEED = 700; //425
    private TextureRegion current;

    private Body body;
    private PointLight light;

    // Animations
    private float animState, attackAnim;
    private TextureRegion frame1, frame2, frame3;
    private Animation<TextureRegion> wLeft;
    private Animation<TextureRegion> wUp;
    private Animation<TextureRegion> wDown;
    private Animation<TextureRegion> aLeft;
    private Animation<TextureRegion> aUp;
    private Animation<TextureRegion> aDown;
    private Animation<TextureRegion> aReturn;
    private boolean dirFlip;
    private boolean attackComplete;
    public float SHOOT_TIMER;

    //Bullets
    private float damage;
    private float atrito;
    private float distancia;
    private float firerate;
    private float shotSpeed;
    private float bouncy;
    private boolean diagonal;


    public Player(){
        attackComplete = true;
        dirFlip = false;
        hp = 100;
        mp = 100;
        SHOOT_TIMER = 0;

        //Bullet
        atrito = .6f;
        distancia = 10;
        damage = 10;
        bouncy = 0f;
        shotSpeed = 8;
        firerate = .4f;
    }



    public Player(World world, RayHandler rays, Player player) {

        BodyDef bDef = new BodyDef();
        bDef.position.set(0, 0);
        bDef.type = BodyDef.BodyType.DynamicBody;
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

        this.attackComplete = player.attackComplete;
        this.dirFlip = player.dirFlip;
        this.hp = player.hp;
        this.mp = player.mp;
        this.SHOOT_TIMER = player.SHOOT_TIMER;

        this.atrito = player.atrito;
        this.distancia = player.distancia;
        this.damage = player.damage;
        this.bouncy = player.bouncy;
        this.shotSpeed = player.shotSpeed;
        this.firerate = player.firerate;
        this.diagonal = player.diagonal;

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

        aReturn = wDown;
        current = wDown.getKeyFrame(animState);
    }

    public void controller(float delta) {
        // Player Controller
        float x = 0, y = 0;

        if(Gdx.input.isKeyJustPressed((Input.Keys.SPACE))) {
            attackComplete = false;
            attackAnim = 0;
        }

        boolean heldAttack = false;
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            heldAttack = true;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isTouched()) {
            x -= 1;
            current = wLeft.getKeyFrame(animState, true);

            if(!heldAttack) {
                aReturn = wLeft;
                dirFlip = false;
                if(current.isFlipX())
                    current.flip(true, false);
            }
        }
        if(Gdx.input.isKeyPressed((Input.Keys.D))) {
            x += 1;
            current = wLeft.getKeyFrame(animState, true);

            if(!heldAttack) {
                aReturn = wLeft;
                dirFlip = true;
                if(!current.isFlipX())
                    current.flip(true, false);
            }
        }
        if(Gdx.input.isKeyPressed((Input.Keys.W))) {
            y += 1;
            current = wUp.getKeyFrame(animState, true);
            if(!heldAttack)
                aReturn = wUp;
        }
        if(Gdx.input.isKeyPressed((Input.Keys.S))) {
            y -= 1;
            current = wDown.getKeyFrame(animState, true);
            if(!heldAttack)
                aReturn = wDown;
        }

        if(!attackComplete) {
            x /= 3;
            y /= 3;
            if(aReturn == wDown)
                current = aDown.getKeyFrame(attackAnim, false);
            else if(aReturn == wUp)
                current = aUp.getKeyFrame(attackAnim, false);
            else if(aReturn == wLeft) {
                if(!dirFlip) {
                    current = aLeft.getKeyFrame(attackAnim, false);
                    if(current.isFlipX()) {
                        current.flip(true, false);
                    }
                } else {
                    current = aLeft.getKeyFrame(attackAnim, false);
                    if(!current.isFlipX()) {
                        current.flip(true, false);
                    }
                }
            }
            attackAnim += delta;
            if(aDown.isAnimationFinished(attackAnim) && !heldAttack) {
                attackComplete = true;
                current = aReturn.getKeyFrame(animState);
            }
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
    public float getMP() {
        return mp;
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
    public TextureRegion getCurrent(){ return current;}

    public void toggleDiagonal(){if(diagonal){diagonal = false;}else{ diagonal = true;}}
    public void takeBulletDamage(Bullet bala){this.hp -= bala.dealDamage();}
    public void takeContactDamage(Enemy enemy){this.hp -= enemy.getDamage();}
    public void setDistance(float distancia){
        this.distancia = distancia;
    }
    public void setAtrito(float atrito){
        this.atrito = atrito;
    }
    public void setDamage(float damage){
        this.damage = damage;
    }
    public void setBouncy(float bouncy){ this.bouncy = bouncy; }
    public void setShotSpeed(float shotSpeed){ this.shotSpeed =  shotSpeed; }
    public void dispose() {
        atlas.dispose();
    }
}
