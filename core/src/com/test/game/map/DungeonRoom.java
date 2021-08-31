package com.test.game.map;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.test.game.Teste;
import com.test.game.utils.Constants;
import com.test.game.utils.b2d.BodyBuilder;

import static com.test.game.utils.Constants.PPM;
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
    private boolean isBoss;
    private int isSpecial;
    
    private Texture door_open_top;
    private Texture door_open_bottom;
    private Texture door_open_right;
    private Texture door_open_left;
    private Texture wall_close_top;
    private Texture wall_close_bottom;
    private Texture wall_close_right;
    private Texture wall_close_left;
    
    private Texture semestre;
    
    public DungeonRoom(World world, Vector2 roomCenter, int x, int y) 
    {
    	door_open_top = new Texture(Gdx.files.internal("img\\door_open_top.png"));
        door_open_bottom = new Texture(Gdx.files.internal("img\\door_open_bottom.png"));
        door_open_left = new Texture(Gdx.files.internal("img\\door_open_left.png"));
        door_open_right = new Texture(Gdx.files.internal("img\\door_open_right.png"));
        wall_close_top = new Texture(Gdx.files.internal("img\\wall_close_top.png"));
        wall_close_bottom = new Texture(Gdx.files.internal("img\\wall_close_bottom.png"));
        wall_close_right = new Texture(Gdx.files.internal("img\\wall_close_right.png"));
        wall_close_left = new Texture(Gdx.files.internal("img\\wall_close_left.png"));
    	
        this.world = world;
        this.center = new Vector2(roomCenter.x - (7-x)*720, roomCenter.y - (7-y)*480);
        this.structure = new Array<>();
        this.completed = false;
        this.keep_it_simple = 0;
        this.doors = new Array<>();
        this.switches = new Array<>();
        this.x = x;
        this.y = y;
        this.isSpecial = 0;
        
        if (Teste.player.getSemestre() == 1)
        	    semestre = new Texture(Gdx.files.internal("img\\semestre_1.png"));
        else if (Teste.player.getSemestre() == 2)
          	    semestre = new Texture(Gdx.files.internal("img\\semestre_2.png"));
		else if (Teste.player.getSemestre() == 3)
		        semestre = new Texture(Gdx.files.internal("img\\semestre_3.png"));
        else if (Teste.player.getSemestre() == 4)
			    semestre = new Texture(Gdx.files.internal("img\\semestre_4.png"));
        else if (Teste.player.getSemestre() == 5)
		        semestre = new Texture(Gdx.files.internal("img\\semestre_5.png"));
        else if (Teste.player.getSemestre() == 6)
			    semestre = new Texture(Gdx.files.internal("img\\semestre_6.png"));
        else if (Teste.player.getSemestre() == 7)
		        semestre = new Texture(Gdx.files.internal("img\\semestre_7.png"));
        else if (Teste.player.getSemestre() == 8)
	            semestre = new Texture(Gdx.files.internal("img\\semestre_8.png"));

        initRoomStructure();
    }

    private void initRoomStructure() {
        Filter f = new Filter();
        f.categoryBits = Constants.BIT_WALL;
        f.maskBits = Constants.BIT_ENEMY_BULLET | Constants.BIT_PLAYER | Constants.BIT_BULLET | Constants.BIT_ENEMY | Constants.BIT_SENSOR;

        // Bottom Wall
        structure.add(BodyBuilder.createBox(world, center.x + 199, center.y - 228, 323, 24, true, true));
        structure.add(BodyBuilder.createBox(world, center.x - 199, center.y - 228, 323, 24, true, true));

        // Top Wall
        structure.add(BodyBuilder.createBox(world, center.x + 199, center.y + 228, 323, 35, true, true));
        structure.add(BodyBuilder.createBox(world, center.x - 199, center.y + 228, 323, 35, true, true));

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
        batch.begin();
        batch.draw(semestre , this.center.x-360,this.center.y-240);//-360, -240
        if(attached_rooms[0] == 0) {
            batch.draw(wall_close_top, this.center.x - 45, this.center.y + 183);
        }
        if(attached_rooms[1] == 0) {
            batch.draw(wall_close_bottom,this.center.x - 45, this.center.y - 239);
        }
        if(attached_rooms[2] == 0) {
            batch.draw(wall_close_left,this.center.x - 360, this.center.y - 42);
        }
        if(attached_rooms[3] == 0) {
            batch.draw(wall_close_right,this.center.x + 320, this.center.y - 42);
        }
        if(this.keep_it_simple == 1){
            if(attached_rooms[0] == 1) { // cima
                batch.draw(door_open_top,this.center.x - 39, this.center.y + 195);
            }
            if(attached_rooms[1] == 1) { // baixo
                batch.draw(door_open_bottom,this.center.x - 39, this.center.y - 245);
            }
            if(attached_rooms[2] == 1) { // esquerda
                batch.draw(door_open_left,this.center.x - 362, this.center.y - 42);
            }
            if(attached_rooms[3] == 1) { // direita
                batch.draw(door_open_right,this.center.x + 335, this.center.y - 42);
            }
        }
        batch.end();
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
    
    public void dispose() 
    {
		semestre.dispose();
		door_open_top.dispose();
        door_open_bottom.dispose();
        door_open_right.dispose();
        door_open_left.dispose();
        wall_close_left.dispose();
        wall_close_right.dispose();
        wall_close_top.dispose();
        wall_close_bottom.dispose();
	}
    public boolean isCompleted(){ return completed;}
    public int[] getAttached_rooms() { return attached_rooms; }
    public Vector2 getCenter(){return center; }
    public int isItSimple(){return keep_it_simple; }
    public int amountAttached_rooms(){
        return attached_rooms[0] + attached_rooms[1] + attached_rooms[2] + attached_rooms[3];
    }
    public boolean getBoss(){ return isBoss; }
    public int getIsSpecial(){ return isSpecial;}

    public void setIsSpecial(int valor){ isSpecial = valor;}
    public void setCompleted(){
        completed = true;
    }
    public void setBoss(){ isBoss = true; }
    public void toggleSimple(){keep_it_simple += 1;}
    public void setAttached_rooms(int pos, int rooms){ attached_rooms[pos] = rooms; }

    // TODO: Add Enemy
    // TODO: Hide Room? Necessary for dungeon size?
}
