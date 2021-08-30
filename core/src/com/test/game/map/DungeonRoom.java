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
    
    private Texture door_open;
    private Texture door_close;
    
    private Texture semestre_1;
    private Texture semestre_2;
    private Texture semestre_3;
    private Texture semestre_4;
    private Texture semestre_5;
    private Texture semestre_6;
    private Texture semestre_7;
    private Texture semestre_8;
    
    public DungeonRoom(World world, Vector2 roomCenter, int x, int y) 
    {
    	door_open = new Texture(Gdx.files.internal("img\\door_open.png"));
    	door_close = new Texture(Gdx.files.internal("img\\door_close.png"));
    	
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
        
        switch(Teste.player.getSemestre()) 
        {
        	case 1:
        	semestre_1 = new Texture(Gdx.files.internal("img\\semestre_1.png"));
	        case 2:
          	semestre_2 = new Texture(Gdx.files.internal("img\\semestre_2.png"));
		    /*case 3:
		    semestre_3 = new Texture(Gdx.files.internal("img\\semestre_3.png"));
		    case 4:
			semestre_4 = new Texture(Gdx.files.internal("img\\semestre_4.png"));
			case 5:
		    semestre_5 = new Texture(Gdx.files.internal("img\\semestre_5.png"));
			case 6:
			semestre_6 = new Texture(Gdx.files.internal("img\\semestre_6.png"));
			case 7:
		    semestre_7 = new Texture(Gdx.files.internal("img\\semestre_7.png"));
			case 8:
	        semestre_8 = new Texture(Gdx.files.internal("img\\semestre_8.png"));*/
        }
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
            batch.begin();
            if(attached_rooms[0] == 0)
            {
            	//batch(door_close,this.doors.getPosition().x, this.doors.getPosition().y);
            }
            if(!doors.isEmpty()) {
            	if(attached_rooms[0] == 1) {
            		//batch(door_close,this.doors.getPosition().x, this.doors.getPosition().y);
            	}
            		if(attached_rooms[0] == 2) {
            			//batch(door_close,this.doors.getPosition().x, this.doors.getPosition().y);
            		}
            			if(attached_rooms[0] == 3) {
            				//batch(door_close,this.doors.getPosition().x, this.doors.getPosition().y);
            			}
            }
            
            batch.end();
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
