package com.test.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.test.game.utils.Constants;

import static com.test.game.utils.Constants.PPM;

public abstract class Items {

    protected Body body;
    protected World temp;
    protected int[] IDs;
    protected boolean destroy = false;
    protected String name;

    public void createItems(World world, Vector2 pos){
        temp = world;
        BodyDef bDef = new BodyDef();
        bDef.position.set(pos.x/PPM, pos.y/PPM);
        bDef.linearDamping = 0f;
        bDef.fixedRotation = false;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(16/PPM, 16/PPM);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.restitution = 0f;
        fd.density = 1f;
        fd.filter.categoryBits = Constants.BIT_SENSOR;
        fd.filter.maskBits = Constants.BIT_WALL | Constants.BIT_PLAYER;
        body = world.createBody(bDef);
        body.createFixture(fd).setUserData(this);
        shape.dispose();
    }
    public void removeItem(){
        temp.destroyBody(body);
    }
    public String getName(){return name;}
    public boolean getDestroy(){return destroy;}
    public abstract void itemEffect(Player player);
}
