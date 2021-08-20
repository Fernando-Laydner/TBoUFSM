package com.test.game.handlers;

import com.badlogic.gdx.physics.box2d.*;
import com.test.game.entities.Bullet;
import com.test.game.entities.Enemy;
import com.test.game.map.FloorSwitch;
import com.test.game.entities.Player;

public class WorldContactListener implements ContactListener {

    public WorldContactListener() {
        super();
    }

    // Called when two fixtures start to collide
    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa == null || fb == null) return;
        if(fa.getUserData() == null || fb.getUserData() == null) return;

        if(isSwitchContact(fa, fb)) {
            if(fa.getUserData() instanceof FloorSwitch) {
                ((FloorSwitch) fa.getUserData()).activate();
            } else {
                ((FloorSwitch) fb.getUserData()).activate();
            }
        }

        if (isBulletContact(fa, fb) == 1){
            if(fa.getUserData() instanceof Enemy){
                ((Enemy) fa.getUserData()).takeDamage((Bullet) fb.getUserData());
            }
            else {
                ((Enemy) fb.getUserData()).takeDamage((Bullet) fa.getUserData());
            }
        }
        if (isBulletContact(fa, fb) == 2){
            if(fa.getUserData() instanceof Player){
                ((Player) fa.getUserData()).takeBulletDamage((Bullet) fb.getUserData());
            }
            else {
                ((Player) fb.getUserData()).takeBulletDamage((Bullet) fa.getUserData());
            }
        }

        if (isContactDamage(fa, fb)){
            if(fa.getUserData() instanceof Player){
                ((Player) fa.getUserData()).takeContactDamage((Enemy) fb.getUserData());
            }
            else {
                ((Player) fb.getUserData()).takeContactDamage((Enemy) fa.getUserData());
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa == null || fb == null) return;
        if(fa.getUserData() == null || fb.getUserData() == null) return;

        /*
        if(isSwitchContact(fa, fb)) {
            if(fa.getUserData() instanceof FloorSwitch) {
                ((FloorSwitch) fa.getUserData()).deactivate();
            } else {
                ((FloorSwitch) fb.getUserData()).deactivate();
            }
        }

         */
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    private boolean isSwitchContact(Fixture a, Fixture b) {
        if(a.getUserData() instanceof FloorSwitch || b.getUserData() instanceof FloorSwitch) {
            if(b.getUserData() instanceof Player || a.getUserData() instanceof Player) {
                return true;
            }
        }
        return false;
    }

    private int isBulletContact(Fixture a, Fixture b){
        if (a.getUserData() instanceof Bullet || b.getUserData() instanceof Bullet){
            if(b.getUserData() instanceof Enemy || a.getUserData() instanceof Enemy ){
                return 1;
            }
            else if (b.getUserData() instanceof Player || a.getUserData() instanceof Player){
                return 2;
            }
        }
        return 0;
    }

    private boolean isContactDamage(Fixture a, Fixture b){
        if (a.getUserData() instanceof Player || b.getUserData() instanceof Player){
            if(b.getUserData() instanceof Enemy || a.getUserData() instanceof Enemy ){
                return true;
            }
        }
        return false;
    }
}
