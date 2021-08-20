package com.test.game.map;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.test.game.entities.Player;
import com.test.game.utils.Constants;
import com.test.game.utils.b2d.BodyBuilder;

import static com.test.game.utils.b2d.BodyBuilder.createBox;

public class DungeonRoom {

    private World world;

    private int x, y;
    private Vector2 center;
    private Array<Body> structure;
    private Array<FloorSwitch> switches;
    private int[] attached_rooms = {0,0,0,0};
    private boolean completed;
    private int keep_it_simple;
    private Array<Body> doors;
    private Array<Player> enemies;

    public DungeonRoom(World world, Vector2 roomCenter, int x, int y) {
        this.world = world;
        this.center = new Vector2(roomCenter.x - (7-x)*720, roomCenter.y - (7-y)*480);
        this.structure = new Array<Body>();
        this.completed = false;
        this.keep_it_simple = 0;
        this.doors = new Array<>();
        this.switches = new Array<FloorSwitch>();
        this.x = x;
        this.y = y;

        initRoomStructure();
    }

    private void initRoomStructure() {
        Filter f = new Filter();
        f.categoryBits = Constants.BIT_WALL;
        f.maskBits = Constants.BIT_ENEMY_BULLET | Constants.BIT_PLAYER | Constants.BIT_BULLET | Constants.BIT_ENEMY;

        // Bottom Wall
        structure.add(BodyBuilder.createBox(world, center.x + 199, center.y - 228, 323, 24, true, true));
        structure.add(BodyBuilder.createBox(world, center.x - 199, center.y - 228, 323, 24, true, true));

        // Top Wall
        structure.add(BodyBuilder.createBox(world, center.x + 199, center.y + 228, 323, 24, true, true));
        structure.add(BodyBuilder.createBox(world, center.x - 199, center.y + 228, 323, 24, true, true));

        // Right wall
        structure.add(BodyBuilder.createBox(world, center.x + 348, center.y + 127, 24, 178, true, true));
        structure.add(BodyBuilder.createBox(world, center.x + 348, center.y - 127, 24, 178, true, true));

        // Left wall
        structure.add(BodyBuilder.createBox(world, center.x - 348, center.y + 127, 24, 178, true, true));
        structure.add(BodyBuilder.createBox(world, center.x - 348, center.y - 127, 24, 178, true, true));

        for (Body parede: structure){
            parede.getFixtureList().get(0).setFilterData(f);
        }

    }

    public void createTestLock() {
        int i = MathUtils.random(1);

        switches.add(new FloorSwitch(world, center.x + MathUtils.random(280f) - 140f, center.y + MathUtils.random(280f) - 140f, this, i == 1));
    }

    public void render(Batch batch) {
        for(FloorSwitch fs : switches) {
            fs.draw(batch);
        }
    }

    public void closeDoors(){
        int x = (int) (center.x), y = (int) (center.y);
        Filter f = new Filter();
        f.categoryBits = Constants.BIT_WALL;
        f.maskBits = Constants.BIT_ENEMY_BULLET | Constants.BIT_PLAYER | Constants.BIT_BULLET | Constants.BIT_ENEMY;

        if (attached_rooms[3] == 1) {
            doors.add(createBox(world, x + 355, y, 30, 76, true, true));
        }
        if (attached_rooms[2] == 1) {
            doors.add(createBox(world, x - 355, y, 30, 76, true, true));
        }

        if (attached_rooms[0] == 1) {
            doors.add(createBox(world, x, y + 235, 76, 30, true, true));
        }
        if (attached_rooms[1] == 1) {
            doors.add(createBox(world, x, y - 235, 76, 30, true, true));
        }
        for (Body door: doors){
            door.getFixtureList().get(0).setFilterData(f);
        }
    }

    public void openDoors(){
        for (Body door: doors) {
            world.destroyBody(door);
        }
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public boolean isCompleted(){ return completed;}
    public int[] getAttached_rooms() { return attached_rooms; }
    public Vector2 getCenter(){return center; }
    public int isItSimple(){return keep_it_simple; }
    public int amountAttached_rooms(){
        int total = 0;
        for (int room:attached_rooms) { total = total + room;}
        return total;
    }

    public void setCompleted(){
        completed = true;
    }
    public void toggleSimple(){keep_it_simple += 1;}
    public void setAttached_rooms(int pos, int rooms){ attached_rooms[pos] = rooms; }

    // TODO: Add Enemy
    // TODO: Hide Room? Necessary for dungeon size?
}
