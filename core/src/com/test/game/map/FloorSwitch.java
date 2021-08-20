package com.test.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.utils.Array;
import com.test.game.Teste;
import com.test.game.utils.Constants;

import static com.test.game.utils.Constants.PPM;

public class FloorSwitch {

    private World world;
    private Body sensor;
    private Texture tex;
    private TextureRegion on, off;
    private boolean activated, flips;
    private DungeonRoom control;

    public FloorSwitch(World world, float x, float y, DungeonRoom control, boolean flips) {
        this.world = world;
        this.control = control;
        this.activated = false;
        this.flips = flips;

        // Setup textures
        tex = new Texture(Gdx.files.internal("img/switch.png"));
        on = new TextureRegion(tex);
        off = new TextureRegion(tex);

        on.setRegion(0, 0, 16, 16);
        off.setRegion(16, 0, 16, 16);

        initSwitch(x, y);
    }

    private void initSwitch(float x, float y) {
        BodyDef bDef = new BodyDef();
        bDef.position.set(x / PPM, y / PPM);

        sensor = world.createBody(bDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(9 / PPM);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.isSensor = true;
        fd.filter.categoryBits = Constants.BIT_SENSOR;
        fd.filter.maskBits = Constants.BIT_PLAYER;
        sensor.createFixture(fd).setUserData(this);
        shape.dispose();
    }

    public void draw(Batch batch) {
        batch.begin();
        if(on != null && off != null) {
            batch.draw(!activated? on : off,
                    sensor.getPosition().x * PPM - 16, sensor.getPosition().y * PPM - 16,
                    0, 0, 16, 16, 2, 2, 0);
        }
        batch.end();
    }

    public Body getSensor() {
        return sensor;
    }

    public void activate() {
        if(!activated) {
            activated = true;
            control.setCompleted();
        }
    }
}
