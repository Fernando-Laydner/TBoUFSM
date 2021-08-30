package com.test.game.states;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.utils.Array;
import com.test.game.Items.ItemSelect;
import com.test.game.Items.SemesterUpgrade;
import com.test.game.Teste;
import com.test.game.entities.Enemy;
import com.test.game.entities.Items;
import com.test.game.entities.Player;
import com.test.game.handlers.WorldContactListener;
import com.test.game.managers.GameStateManager;
import com.test.game.map.DungeonRoom;
import com.test.game.utils.Constants;
import com.test.game.utils.b2d.BodyBuilder;

import static com.test.game.utils.Constants.PPM;
import static com.test.game.utils.b2d.BodyBuilder.*;

import com.test.game.entities.Bullet;

public class DungeonState extends GameState {

    private final Player player;

    // Hud
    private final BitmapFont font;
    private final OrthographicCamera hud;
    private String playerStats = "Loading...";

    // Pause
    private final OrthographicCamera paused;
    private final String pauseType = "Paused";
    private Boolean ispaused = false;

    // Camera tracker stuff
    private final DungeonRoom[][] rooms;
    private final Vector2 pos;

    // Player stuff
    private final Vector2 target;
    private PointLight currentTorch;

    // b2d/lock stuff
    private final RayHandler rays;
    private final Box2DDebugRenderer b2dr;
    private final World world;
    
    // Track entities
    private final Array<Bullet> bullets = new Array<>();
    private final Array<Enemy> enemies = new Array<>();
    private final Array<Items> itemlist = new Array<>();

    public DungeonState(GameStateManager gsm) {
        super(gsm);

        // HUD init
        font = new BitmapFont();
        hud = new OrthographicCamera();
        hud.setToOrtho(false, 720, 480);

        //Pause init
        paused = new OrthographicCamera();
        paused.setToOrtho(false, 720, 480);


        // b2d world init
        world = new World(new Vector2(0f, 0f), false);
        world.setContactListener(new WorldContactListener());
        rays = new RayHandler(world);
        rays.setAmbientLight(1f);
        
        b2dr = new Box2DDebugRenderer();

        // Camera init stuff
        target = new Vector2(0, 0);
        rooms = new DungeonRoom[16][16];



        // Room init
        pos = new Vector2(7, 7);
        generateMap();

        // Player init
        player = new Player(world, rays, Teste.player);
    }

    @Override
    public void update(float delta){

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)){
            if (ispaused)
                ispaused = false;
            else
                ispaused = true;
        }
        if (!ispaused) {
            world.step(1 / 60f, 6, 2);
            Teste.player.addTime(delta);

            // Controller
            player.controller(delta);

            Teste.player.setHp(player);

            // Shooting
            player.shoots(delta, bullets, rays, world);

            // Create Lamp
            if (Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
                createLamp(player.getPosition().scl(32));
            }

            // Room Check
            if (rooms[(int) pos.x][(int) pos.y].isItSimple() == 0 && !rooms[(int) pos.x][(int) pos.y].isCompleted()) {
                Enemy novo = new Enemy();
                if (rooms[(int) pos.x][(int) pos.y].getBoss()){
                    novo.createBoss(world, target);
                    enemies.add(novo);
                }
                rooms[(int) pos.x][(int) pos.y].closeDoors(); // Closes the doors
                novo.spawnXEnemy(world, 0, target, enemies); // Spawn the enemies
                rooms[(int) pos.x][(int) pos.y].toggleSimple(); // Sends next step for saving resources
                rays.setAmbientLight(.4f); // Dims the light
            }

            // Room Transition
            Room_Transition();

            cameraUpdate();
            batch.setProjectionMatrix(camera.combined);
            rays.setCombinedMatrix(camera.combined.cpy().scl(PPM), player.getPosition().x, player.getPosition().y, 720, 500);
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) gsm.setState(GameStateManager.State.MENU);

        if (!ispaused) 
        {
            b2dr.render(world, camera.combined.cpy().scl(PPM));
            rooms[(int) pos.x][(int) pos.y].render(batch);
            rays.updateAndRender();
            
            
            for(Enemy enemy: enemies)
            {
            	enemy.render(batch);
            }

            player.render(batch);
            
            for (Items item: itemlist) {
                item.render(batch);
            }

            Bullet bala = new Bullet(player);
            bala.destroyBullet(world, bullets);


            for(Items temp:itemlist){
                if (temp.getDestroy()){
                    temp.removeItem();
                    itemlist.removeValue(temp, true);
                }
            }

            Enemy dummy = new Enemy();
            dummy.isDead(world, enemies);
            dummy.AI_Selection(world, enemies, bullets, player.getPosition(), rays);

            if (player.getHP() <= 0) {
                Teste.player.toggleKill();
            }

            if (rooms[(int) pos.x][(int) pos.y].isItSimple() == 1 && enemies.isEmpty()) {
                rooms[(int) pos.x][(int) pos.y].setCompleted();
                rooms[(int) pos.x][(int) pos.y].openDoors();
                rooms[(int) pos.x][(int) pos.y].toggleSimple();
                if (rooms[(int) pos.x][(int) pos.y].getBoss()){
                    Items items = ItemSelect.itemSelect();
                    if(items != null) {
                        items.createItems(world, new Vector2(rooms[(int) pos.x][(int) pos.y].getCenter().x, rooms[(int) pos.x][(int) pos.y].getCenter().y - 4 * PPM));
                        itemlist.add(items);
                    }
                    Items semestre = new SemesterUpgrade();
                    semestre.createItems(world, new Vector2(rooms[(int) pos.x][(int) pos.y].getCenter().x, rooms[(int) pos.x][(int) pos.y].getCenter().y + 4 * PPM));
                }
                rays.setAmbientLight(1f);
            }
        }
        else{
            batch.setProjectionMatrix(paused.combined);
            batch.begin();
            font.draw(batch, pauseType, 350, 240);
            batch.end();
        }
        batch.setProjectionMatrix(hud.combined);
        batch.begin();
        font.draw(batch, "Timer:  "+ timer((int)Teste.player.getTime()) + " ", 320, 460);
        font.draw(batch, playerStats, 100, 150);
        playerStats = "HP: " + player.getHP() + "\nSemestre: " + player.getSemestre();
        font.draw(batch, "Room: " + (pos.x + 1) + " " + (pos.y + 1), 100, 110);
        batch.end();
    }

    @Override
    public void dispose() {
        b2dr.dispose();
        world.dispose();
        player.dispose();
    }

    private void cameraUpdate() {
        camera.zoom = 1;
        Vector3 position = camera.position;
        position.x = camera.position.x + (target.x - camera.position.x) * .1f;
        position.y = camera.position.y + (target.y - camera.position.y) * .1f - 1f;
        camera.position.set(position);
        camera.update();
    }

    private String timer(int totalSecs){

        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private void generateMap(){
        int n_rooms = MathUtils.random(9 + Teste.player.getSemestre(),11 + Teste.player.getSemestre());
        Array<Vector2> Available= new Array<>();
        rooms[7][7] = new DungeonRoom(world, target, 7, 7);
        rooms[7][7].setCompleted();
        Available.add(new Vector2(8,8));

        int i = 0;
        while (i < n_rooms){
            int j = MathUtils.random(1, 4), k = MathUtils.random(0, i);
            int x = (int)Available.get(k).x, y = (int)Available.get(k).y;
            switch (j) {
                case 1:
                    if (y - 1 != 0 && rooms[x - 1][y - 2] == null && rooms[x-1][y-1].amountAttached_rooms() <= 3) {
                        rooms[x - 1][y - 2] = new DungeonRoom(world, target, x - 1, y - 2);
                        rooms[x-1][y-1].setAttached_rooms(1, 1);
                        rooms[x-1][y-2].setAttached_rooms(0, 1);
                        //System.out.println("Baixo " + Available.get(i).x + " " + Available.get(i).y);
                        i++;
                        Available.add(new Vector2(x, y - 1));
                    }
                case 2:
                    if (x + 1 != 16 && rooms[x][y - 1] == null && rooms[x-1][y-1].amountAttached_rooms() <= 3) {
                        rooms[x][y - 1] = new DungeonRoom(world, target, x, y - 1);
                        rooms[x-1][y-1].setAttached_rooms(3, 1);
                        rooms[x][y-1].setAttached_rooms(2, 1);
                        //System.out.println("Direita " + Available.get(i).x + " " + Available.get(i).y);
                        i++;
                        Available.add(new Vector2(x + 1, y));
                    }
                case 3:
                    if (y + 1 != 16 && rooms[x - 1][y] == null && rooms[x-1][y-1].amountAttached_rooms() <= 3) {
                        rooms[x - 1][y] = new DungeonRoom(world, target, x - 1, y);
                        rooms[x-1][y-1].setAttached_rooms(0, 1);
                        rooms[x-1][y].setAttached_rooms(1, 1);
                        //System.out.println("Cima " + Available.get(i).x + " " + Available.get(i).y);
                        i++;
                        Available.add(new Vector2(x, y + 1));
                    }
                case 4:
                    if (x - 1 != 0 && rooms[x - 2][y - 1] == null && rooms[x-1][y-1].amountAttached_rooms() <= 3 ) {
                        rooms[x - 2][y - 1] = new DungeonRoom(world, target, x - 2, y - 1);
                        rooms[x-1][y-1].setAttached_rooms(2, 1);
                        rooms[x-2][y-1].setAttached_rooms(3, 1);
                        //System.out.println("Esquerda " + Available.get(i).x + " " + Available.get(i).y);
                        i++;
                        Available.add(new Vector2(x - 1, y));
                    }
            }
        }

        // Place blocks and select potential special room locations
        Array<Vector2> specialrooms = new Array<>();
        Filter f = new Filter();
        f.categoryBits = Constants.BIT_WALL;
        f.maskBits = Constants.BIT_ENEMY_BULLET | Constants.BIT_PLAYER | Constants.BIT_BULLET | Constants.BIT_ENEMY | Constants.BIT_SENSOR;
        i = 0;
        for (Vector2 sala: Available) {
            int[] attached = rooms[(int)sala.x - 1][(int)sala.y - 1].getAttached_rooms();
            int x = (int) (target.x - (8 - sala.x) * 720), y = (int) (target.y - (8 - sala.y) * 480);
            if (attached[3] == 0) {
                createBox(world, x + 348, y, 50/2, 76, true, true).getFixtureList().get(0).setFilterData(f);
            }
            if (attached[2] == 0) {
                createBox(world, x - 348, y, 50/2, 76, true, true).getFixtureList().get(0).setFilterData(f);
            }
            if (attached[0] == 0) {
                createBox(world, x, y + 228, 76, 50/2, true, true).getFixtureList().get(0).setFilterData(f);
            }
            if (attached[1] == 0) {
                createBox(world, x, y - 228, 76, 50/2, true, true).getFixtureList().get(0).setFilterData(f);
            }
            if (rooms[(int)sala.x - 1][(int)sala.y - 1].amountAttached_rooms() == 1 && (int)sala.x != 8 && (int)sala.y != 8){
                specialrooms.add(new Vector2(sala.x, sala.y));
            }
        }

        System.out.println(specialrooms.size);
        // Create the selected special rooms.
        int j = 1, n_specialrooms = 2;
        for (Vector2 sala: specialrooms) {
            int x = (int) (target.x - (8 - sala.x) * 720), y = (int) (target.y - (8 - sala.y) * 480);
            if (j == 1){
                rooms[(int)sala.x - 1][(int)sala.y - 1].setBoss();
                //System.out.println("Sala Boss: [" +sala.x + "][" + sala.y + "]");
                rooms[(int)sala.x - 1][(int)sala.y - 1].setIsSpecial(j);
                j++;
                continue;
            }
            if (j <= n_specialrooms) {
                //System.out.println("Sala especial: [" +sala.x + "][" + sala.y + "]");
                Items items = ItemSelect.itemSelect();
                if(items != null) {
                    items.createItems(world, new Vector2(x, y));
                    itemlist.add(items);
                }
                else{
                    System.out.println("Erro ao criar os itens.");
                }
                rooms[(int)sala.x - 1][(int)sala.y - 1].setCompleted();
                rooms[(int)sala.x - 1][(int)sala.y - 1].setIsSpecial(j);
                j++;
            }
        }

    }

    // [WIP] Spread the worlds gen in a more dendritic way.
    private boolean isNextroom(int fromdir, int tox, int toy){
        if ((fromdir == 1 || fromdir == 2) && rooms[(int)tox + 1][(int)toy - 1] == null && rooms[(int)tox][(int)toy - 1] == null){
            return true;
        }
        if ((fromdir == 3 || fromdir == 4) && rooms[(int)tox - 1][(int)toy + 1] == null && rooms[(int)tox - 1][(int)toy] == null){
            return true;
        }
        return false;
    }

    private void Room_Transition(){
        // Go Right
        if (player.getPosition().x > (target.x + 360) / PPM) {
            target.x += 720;
            if (!bullets.isEmpty()){
                Bullet temp = new Bullet(player);
                temp.clearAllBullets(world, bullets);
            }
            player.getBody().setLinearVelocity(25, 0);
            pos.x += 1;
        }
        // Go Left
        if (player.getPosition().x < (target.x - 360) / PPM) {
            target.x -= 720;
            if (!bullets.isEmpty()){
                Bullet temp = new Bullet(player);
                temp.clearAllBullets(world, bullets);
            }
            player.getBody().setLinearVelocity(-25, 0);
            pos.x -= 1;
        }
        // Go Up
        if (player.getPosition().y > (target.y + 240) / PPM) {
            target.y += 480;
            if (!bullets.isEmpty()){
                Bullet temp = new Bullet(player);
                temp.clearAllBullets(world, bullets);
            }
            player.getBody().setLinearVelocity(0, 25);
            pos.y += 1;
        }
        // Go Down
        if (player.getPosition().y < (target.y - 240) / PPM) {
            target.y -= 480;
            if (!bullets.isEmpty()){
                Bullet temp = new Bullet(player);
                temp.clearAllBullets(world, bullets);
            }
            player.getBody().setLinearVelocity(0, -25);
            pos.y -= 1;
        }
    }

    private void addTorch(Body b) {
        b.setLinearDamping(2f);
        b.getFixtureList().get(0).setRestitution(.4f);
        Filter f = new Filter();
        f.categoryBits = Constants.BIT_SENSOR;
        f.maskBits = Constants.BIT_PLAYER | Constants.BIT_WALL;
        b.getFixtureList().get(0).setFilterData(f);
        currentTorch = new PointLight(rays, 15, new Color(.1f, .5f, .5f, .7f), 2, 0, 0);
        currentTorch.setSoftnessLength(0f);
        currentTorch.attachToBody(b);
    }
    private void createLamp(Vector2 position) {
        Body lamp = BodyBuilder.createCircle(world, position.x, position.y, 4, false, false,
                Constants.BIT_WALL, Constants.BIT_PLAYER, (short) 0);

        Body clamp = BodyBuilder.createBox(world, position.x, (position.y - 16), 2, 2, true, true,
                Constants.BIT_SENSOR, Constants.BIT_SENSOR, (short) 0);

        Body clamp2 = BodyBuilder.createBox(world, position.x, (position.y + 32), 2, 2, true, true,
                Constants.BIT_SENSOR, Constants.BIT_SENSOR, (short) 0);

        DistanceJointDef jDef = new DistanceJointDef();
        jDef.bodyA = clamp2;
        jDef.bodyB = lamp;
        jDef.collideConnected = false;
        jDef.length = 16f / PPM;
        world.createJoint(jDef);

        jDef.bodyA = clamp;
        jDef.dampingRatio = .5f;
        jDef.length = 1.5f / PPM;
        jDef.frequencyHz = 1.40f;
        world.createJoint(jDef);

        addTorch(lamp);
    }
}
