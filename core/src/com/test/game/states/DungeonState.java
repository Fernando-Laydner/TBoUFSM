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
import com.test.game.entities.Player;
import com.test.game.handlers.WorldContactListener;
import com.test.game.managers.GameStateManager;
import com.test.game.map.DungeonRoom;
import com.test.game.utils.Constants;
import com.test.game.utils.b2d.BodyBuilder;

import com.test.game.utils.Pair;
import static com.test.game.utils.Constants.PPM;
import static com.test.game.utils.b2d.BodyBuilder.*;
import static java.util.Arrays.asList;

import com.test.game.entities.Bullet;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class DungeonState extends GameState {

    private final Player player;

    // Hud
    private final BitmapFont font;
    private final OrthographicCamera hud;
    private String cameraType = "Loading...";

    // Camera tracker stuff
    private final DungeonRoom[][] rooms;
    private final Vector2 pos;

    // Player stuff
    private final Vector2 target;
    private PointLight currentTorch;
    public float SHOOT_TIMER;

    // b2d/lock stuff
    private final RayHandler rays;
    private final Box2DDebugRenderer b2dr;
    private final World world;

    public DungeonState(GameStateManager gsm) {
        super(gsm);

        // HUD init
        font = new BitmapFont();
        hud = new OrthographicCamera();
        hud.setToOrtho(false, 720, 480);

        // b2d world init
        world = new World(new Vector2(0f, 0f), false);
        world.setContactListener(new WorldContactListener());
        rays = new RayHandler(world);
        rays.setAmbientLight(0.4f);
        
        b2dr = new Box2DDebugRenderer();

        // Camera init stuff
        target = new Vector2(0, 0);
        rooms = new DungeonRoom[16][16];

        // Room init
        pos = new Vector2(7, 7);
        generateMap();

        // Player init
        player = new Player(world, rays);
        SHOOT_TIMER = 0;
    }

    private final Array<Bullet> bullets = new Array<>();

    public void shoots(float delta){

        SHOOT_TIMER += delta;

        if(Gdx.input.isKeyPressed(Input.Keys.UP) && SHOOT_TIMER >= player.getShotSpeed()) {
            SHOOT_TIMER = 0;
            Bullet bala = new Bullet(player);
            bala.createBullet(world, player.getPosition(), (int)PPM, 0, rays);
            bullets.add(bala);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && SHOOT_TIMER >= player.getShotSpeed()) {
            SHOOT_TIMER = 0;
            Bullet bala = new Bullet(player);
            bala.createBullet(world, player.getPosition(), 0, -1*(int)PPM, rays);
            bullets.add(bala);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && SHOOT_TIMER >= player.getShotSpeed()) {
            SHOOT_TIMER = 0;
            Bullet bala = new Bullet(player);
            bala.createBullet(world, player.getPosition(), 0, (int)PPM, rays);
            bullets.add(bala);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && SHOOT_TIMER >= player.getShotSpeed()) {
            SHOOT_TIMER = 0;
            Bullet bala = new Bullet(player);
            bala.createBullet(world, player.getPosition(), -1*(int)PPM, 0, rays);
            bullets.add(bala);
        }
    }


    @Override
    public void update(float delta){

        world.step(1 / 60f, 6, 2);

        player.controller(delta);

        // Create Lamp
        if(Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
            createLamp(player.getPosition().scl(32));
        }

        shoots(delta);

        // Room Transition Check
        // Go Right
        if(player.getPosition().x > (target.x + 360) / PPM) {
            target.x += 720;
            pos.x += 1;
        }
        // Go Left
        if(player.getPosition().x < (target.x - 360) / PPM) {
            target.x -= 720;
            pos.x -= 1;
        }
        // Go Up
        if(player.getPosition().y > (target.y + 240) / PPM) {
            target.y += 480;
            pos.y += 1;
        }
        // Go Down
        if(player.getPosition().y < (target.y - 240) / PPM) {
            target.y -= 480;
            pos.y -= 1;
        }

        if(currentTorch != null) {
            currentTorch.setColor(.8f + r, g, .2f, .7f);
            currentTorch.update();
        }
        if(up) {
            r += .03;
            g = MathUtils.random(.7f) - .1f;
            if(r > .9f)
                up = !up;
        } else {
            r -= .03;
            if(r < .4f)
                up = false;
        }
        cameraUpdate();
        batch.setProjectionMatrix(camera.combined);
        rays.setCombinedMatrix(camera.combined.cpy().scl(PPM), player.getPosition().x, player.getPosition().y,720,480);
    }

    private boolean up = true;
    private float r = 0.0f, g = 0.0f;

    @Override
    public void render() {
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        b2dr.render(world, camera.combined.cpy().scl(PPM));

        rooms[(int) pos.x][(int) pos.y].render(batch);
        rays.updateAndRender();
        player.render(batch);

        Bullet bala = new Bullet(player);
        bala.destroyBullet(world, bullets);

        batch.setProjectionMatrix(hud.combined);
        batch.begin();
        font.draw(batch, cameraType, 100, 150);
        font.draw(batch, "Room: " + (pos.x + 1) + " " + (pos.y + 1), 100, 130);
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
        position.y = camera.position.y + (target.y - camera.position.y) * .1f;
        camera.position.set(position);
        cameraType = "Lerp - Room Center";
        camera.update();
    }

    private void generateMap(){
        int n_rooms = MathUtils.random(10,12);
        Array<Pair> Available= new Array<>();
        rooms[7][7] = new DungeonRoom(world, target, 7, 7);
        Available.add(new Pair(8,8));

        int i = 0;
        while (i < n_rooms){
            int j = MathUtils.random(1, 4), k = MathUtils.random(0, i);
            int x = Available.get(k).getX(), y = Available.get(k).getY();
            switch (j) {
                case 1:
                    if (y - 1 != 0 && rooms[x - 1][y - 2] == null && rooms[x-1][y-1].amountAttached_rooms() <= 3) {
                        rooms[x - 1][y - 2] = new DungeonRoom(world, target, x - 1, y - 2);
                        rooms[x-1][y-1].setAttached_rooms(1, 1);
                        rooms[x-1][y-2].setAttached_rooms(0, 1);
                        System.out.println("Baixo " + Available.get(i).getX() + " " + Available.get(i).getY());
                        i++;
                        Available.add(new Pair(x, y - 1));
                    }
                case 2:
                    if (x + 1 != 16 && rooms[x][y - 1] == null && rooms[x-1][y-1].amountAttached_rooms() <= 3) {
                        rooms[x][y - 1] = new DungeonRoom(world, target, x, y - 1);
                        rooms[x-1][y-1].setAttached_rooms(3, 1);
                        rooms[x][y-1].setAttached_rooms(2, 1);
                        System.out.println("Direita " + Available.get(i).getX() + " " + Available.get(i).getY());
                        i++;
                        Available.add(new Pair(x + 1, y));
                    }
                case 3:
                    if (y + 1 != 16 && rooms[x - 1][y] == null && rooms[x-1][y-1].amountAttached_rooms() <= 3) {
                        rooms[x - 1][y] = new DungeonRoom(world, target, x - 1, y);
                        rooms[x-1][y-1].setAttached_rooms(0, 1);
                        rooms[x-1][y].setAttached_rooms(1, 1);
                        System.out.println("Cima " + Available.get(i).getX() + " " + Available.get(i).getY());
                        i++;
                        Available.add(new Pair(x, y + 1));
                    }
                case 4:
                    if (x - 1 != 0 && rooms[x - 2][y - 1] == null && rooms[x-1][y-1].amountAttached_rooms() <= 3) {
                        rooms[x - 2][y - 1] = new DungeonRoom(world, target, x - 2, y - 1);
                        rooms[x-1][y-1].setAttached_rooms(2, 1);
                        rooms[x-2][y-1].setAttached_rooms(3, 1);
                        System.out.println("Esquerda " + Available.get(i).getX() + " " + Available.get(i).getY());
                        i++;
                        Available.add(new Pair(x - 1, y));
                    }
            }
        }

        // Place blocks and select potential special room locations
        Array<Pair> specialrooms = new Array<>();
        i = 0;
        for (Pair sala: Available) {
            System.out.println(Arrays.toString(rooms[sala.getX() - 1][sala.getY() - 1].getAttached_rooms()));
            int[] attached = rooms[sala.getX() - 1][sala.getY() - 1].getAttached_rooms();
            int x = (int) (target.x - (8 - sala.getX()) * 720), y = (int) (target.y - (8 - sala.getY()) * 480);
            if (attached[3] == 0) {
                createBox(world, x + 335, y, 50, 76, true, true);
            }
            if (attached[2] == 0) {
                createBox(world, x - 335, y, 50, 76, true, true);
            }

            if (attached[0] == 0) {
                createBox(world, x, y + 215, 76, 50, true, true);
            }
            if (attached[1] == 0) {
                createBox(world, x, y - 215, 76, 50, true, true);
            }
            if (rooms[sala.getX() - 1][sala.getY() - 1].amountAttached_rooms() == 1 && sala.getX() != 8 && sala.getY()!= 8){
                specialrooms.add(new Pair(sala.getX(), sala.getY()));
                i++;
            }
        }

        // Place lamps on the selected special rooms.
        int j = 0, n_specialrooms = 2, k = MathUtils.random(0, i-n_specialrooms-1);
        for (Pair sala: specialrooms) {
            if (k > n_specialrooms){continue;}
            int x = (int) (target.x - (8 - sala.getX()) * 720), y = (int) (target.y - (8 - sala.getY()) * 480);
            if (j <= n_specialrooms) {
                // Exemplo de seleção das salas.
                createLamp(new Vector2(x, y));
                j++;
            }
        }
    }

    // [WIP] Spread the worlds gen in a more dendritic way.
    private boolean isNextroom(Array<Pair> Available, int dex, int dey){
        for (Pair sala:Available) {
            if (sala.getX() == dex + 1 )
                if (sala.getY() == dey + 1 && rooms[dex][dey] != null && rooms[dex][dey].amountAttached_rooms() > 0 || sala.getY() == dey - 1 && rooms[dex][dey - 2] != null && rooms[dex][dey - 2].amountAttached_rooms() > 0)
                    return true;
            else if (sala.getY() == dex - 1)
                if (sala.getX() == dey - 1 && rooms[dex - 2][dey - 2] != null && rooms[dex - 2][dey - 2].amountAttached_rooms() > 0|| sala.getY() == dey + 1 && rooms[dex - 2][dey] != null && rooms[dex - 2][dey].amountAttached_rooms() > 0)
                    return true;
        }
        return false;
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
