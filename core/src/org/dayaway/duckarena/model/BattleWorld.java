package org.dayaway.duckarena.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;

import org.dayaway.duckarena.model.api.IActor;
import org.dayaway.duckarena.model.api.IPlayer;
import org.dayaway.duckarena.model.api.ISoldier;
import org.dayaway.duckarena.model.api.ITrap;
import org.dayaway.duckarena.model.api.IWorld;
import org.dayaway.duckarena.screens.BattleScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattleWorld implements IWorld {

    private final World world;
    private IActor arena;

    //Список актеров инициализируем один раз и перед каждой отдачей очищаем
    private final List<IActor> actors;

    private IPlayer player;

    private final List<Bot> bots;

    private final List<ISoldier> soldiers;

    private final List<Crystal> crystals;

    private final List<Tower> towers;

    private final List<TrapEdgeMap> trapEdgeMaps;


    private RevoluteJoint revoluteJoint;

    Random random = new Random();

    private final List<Body> destroy;

    public BattleWorld() {
        this.world = new World(new Vector2(0,0), true);
        this.actors = new ArrayList<>();
        this.crystals = new ArrayList<>();
        this.towers = new ArrayList<>();
        this.destroy = new ArrayList<>();

        this.soldiers = new ArrayList<>();

        this.trapEdgeMaps = new ArrayList<>();

        bots = new ArrayList<>();

        createWorld();
    }


    @Override
    public void createWorld() {

        createMap();

        createPlayer();
        createBot();

        for (int i = 0; i < 1; i++) {
            createSoldier(player);
        }
        for (int i = 0; i < 1; i++) {
            createSoldier(bots.get(0));
        }


        for (int i = 0; i < 300; i++) {
            createCrystal();
        }

        createTower();

        create();
    }

    public void createPlayer() {
        BodyDef playerDef = new BodyDef();
        playerDef.type = BodyDef.BodyType.DynamicBody;
        playerDef.position.set(0, 0);

        Body playerBody = world.createBody(playerDef);

        player = new Player(playerBody, BattleScreen.actorPeace);
        playerBody.setUserData(playerBody);

        CircleShape circle = new CircleShape();
        circle.setRadius(1f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f;
        playerBody.createFixture(fixtureDef).setUserData("player");

        circle.dispose();
    }

    public void createSoldier(IPlayer player) {
        BodyDef soldierDef = new BodyDef();
        soldierDef.type = BodyDef.BodyType.DynamicBody;
        soldierDef.position.set(player.getPosition().x + random.nextInt(20) - 10, player.getPosition().y + random.nextInt(20) - 10);

        Body soldierBody = world.createBody(soldierDef);
        //Создаем нового солдата
        Soldier soldier = new Soldier(soldierBody, player, player.getTexture());
        //Добавалем его в список у Player
        player.addSoldier(soldier);
        //Добавалем его в общий спсиок ВСЕХ СОЛДТА у World
        soldiers.add(soldier);
        //Присваем его физическому телу
        soldierBody.setUserData(soldier);

        CircleShape circle = new CircleShape();
        circle.setRadius(3.5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f; // Make it bounce a little bit

        soldierBody.createFixture(fixtureDef).setUserData(player.getClass().getName() + "soldier");
        circle.dispose();
    }


    public void createCrystal() {
        BodyDef crystalDef = new BodyDef();
        crystalDef.type = BodyDef.BodyType.StaticBody;
        crystalDef.position.set(random.nextInt(400)-200, random.nextInt(400)-200);

        Body crystalBody = world.createBody(crystalDef);
        //Создаем новый кристалл
        Crystal crystal = new Crystal(crystalBody, BattleScreen.crystal, 10);
        crystals.add(crystal);

        //Присваем его физическому телу
        crystalBody.setUserData(crystal);

        CircleShape circle = new CircleShape();
        circle.setRadius(1.5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f;
        fixtureDef.isSensor = true;

        crystalBody.createFixture(fixtureDef).setUserData("crystal");
        circle.dispose();
    }

    public void createTower() {
        BodyDef towerDef = new BodyDef();
        towerDef.type = BodyDef.BodyType.StaticBody;
        towerDef.position.set(50, 50);

        Body towerBody = world.createBody(towerDef);
        //Создаем новый кристалл
        Tower tower = new Tower(towerBody, BattleScreen.actorPeace);
        towers.add(tower);

        //Присваем его физическому телу
        towerBody.setUserData(tower);

        CircleShape circle = new CircleShape();
        circle.setRadius(20f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f;

        towerBody.createFixture(fixtureDef).setUserData("tower");
    }


    public void createBot() {
        BodyDef botDef = new BodyDef();
        botDef.type = BodyDef.BodyType.DynamicBody;
        botDef.position.set(100, 0);

        Body botBody = world.createBody(botDef);

        Bot bot = new Bot(botBody, BattleScreen.actorAttack);
        bots.add(bot);
        botBody.setUserData(botBody);

        CircleShape circle = new CircleShape();
        circle.setRadius(1f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f;
        botBody.createFixture(fixtureDef).setUserData("bot");

        circle.dispose();
    }

    public void create() {

        createEdgeTrap(-63, -288, -98, 70);
        createEdgeTrap(-160, -248, -118, 50);
        createEdgeTrap(63, 288, -118, 50);


    }

    private void createEdgeTrap(float posX, float posY, float lowerAngle, float upperAngle) {
        BodyDef defCircle = new BodyDef();
        defCircle.type = BodyDef.BodyType.StaticBody;
        defCircle.position.set(posX, posY);

        Body bodyCircle = world.createBody(defCircle);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(2f);

        bodyCircle.createFixture(circleShape, 100);

        circleShape.dispose();


        BodyDef defPol = new BodyDef();
        defPol.type = BodyDef.BodyType.DynamicBody;
        defPol.position.set(posX, posY);
        defPol.angle = (float) Math.toRadians(-90);

        Body bodyPol = world.createBody(defPol);

        PolygonShape pol = new PolygonShape();
        pol.setAsBox(4, 24);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = pol;
        fixtureDef.density = 0.5f;
        bodyPol.createFixture(fixtureDef);

        pol.dispose();


        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.bodyA = bodyCircle;
        jointDef.bodyB = bodyPol;
        jointDef.localAnchorA.set(0,0);
        jointDef.localAnchorB.set(0,-24);
        /*jointDef.referenceAngle = (float) Math.toRadians(0);
        jointDef.enableLimit = true;
        jointDef.lowerAngle = (float) Math.toRadians(lowerAngle);
        jointDef.upperAngle = (float) Math.toRadians(upperAngle);
        jointDef.enableMotor = true;
        jointDef.maxMotorTorque = 5_000_000;
        jointDef.motorSpeed = (float) Math.toRadians(360);*/

        this.revoluteJoint = (RevoluteJoint) world.createJoint(jointDef);
        this.trapEdgeMaps.add(new TrapEdgeMap(revoluteJoint, BattleScreen.trap));
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public IPlayer getPlayer() {
        return this.player;
    }

    @Override
    public List<IActor> getActors() {

        //Всегда очищаем
        actors.clear();

        //Сразу наполняем аренами, они рисуются первыми
        actors.add(arena);

        //Потом добавляем кристаллы
        actors.addAll(crystals);

        actors.addAll(trapEdgeMaps);

        //Солдаты в конце перед башнями
        actors.addAll(soldiers);

        //Добавляем башни
        actors.addAll(towers);

        return actors;
    }

    @Override
    public List<ISoldier> getSoldiers() {
        return this.soldiers;
    }

    @Override
    public boolean isExist(Body body) {
        //создаем пустой массив
        Array<Body> bodies = new Array<>();
        //Получаем в него все Body из мира
        world.getBodies(bodies);

        return bodies.contains(body, true);
    }

    @Override
    public List<TrapEdgeMap> getTraps() {
        return this.trapEdgeMaps;
    }

    @Override
    public RevoluteJoint get() {
        return this.revoluteJoint;
    }

    @Override
    public IActor getArena() {
        return this.arena;
    }

    @Override
    public List<Tower> getTowers() {
        return this.towers;
    }

    @Override
    public List<Body> getDestroy() {
        return this.destroy;
    }

    @Override
    public List<Crystal> getCrystals() {
        return this.crystals;
    }

    @Override
    public void addToDestroy(Body body) {
        destroy.add(body);
    }

    @Override
    public void destroy() {
        for (Body body : destroy) {
            if(body.getUserData() instanceof Crystal) {
                crystals.remove((Crystal) body.getUserData());
            }
            else if(body.getUserData() instanceof Tower) {
                towers.remove((Tower) body.getUserData());
            }
            else if(body.getUserData() instanceof Soldier) {
                //Получаем у солдата Player и удаляем из его списка этого солдата
                ((Soldier) body.getUserData()).getPlayer().getSoldiers().remove((Soldier) body.getUserData());
                this.soldiers.remove((Soldier) body.getUserData());
            }

            world.destroyBody(body);
        }

        destroy.clear();
    }

    @Override
    public List<Bot> getBots() {
        return this.bots;
    }

    private void createMap() {
        final float RADIUS = 300f;

        Vector2 circleCentre = new Vector2(0,0);

        //Настройка тела арены
        BodyDef arenaDef = new BodyDef();
        arenaDef.type = BodyDef.BodyType.StaticBody;
        //Устанавливаем позицию арены в мире
        arenaDef.position.set(circleCentre);

        //Создаем тело в мире
        Body arenaBody = world.createBody(arenaDef);
        //Добавляем арену в список арен и присваиваем ее телу
        Arena arena = new Arena(arenaBody, BattleScreen.map);
        this.arena = arena;
        arenaBody.setUserData(arena);

        //Создаем форму
        ChainShape chain = new ChainShape();
        //Добавляем точки телу
        chain.createChain(createVerticesCircle(RADIUS));

        //Фикстура тела, настройка одинаковая для всех кроме формы
        FixtureDef fd = new FixtureDef();
        fd.shape = chain;
        fd.density = 0;
        fd.friction = 0f;
        fd.restitution = 0f;

        arenaBody.createFixture(fd).setUserData("arena");

        chain.dispose();

    }

    private Vector2[] createVerticesCircle(float radius) {
        final int STEP = 1;
        int start = 0;
        int COUNT_VERTICES = 361;

        Vector2[] vertices = new Vector2[COUNT_VERTICES];

        for (int i = 0; i < COUNT_VERTICES; i++) {
            float x = (float) (radius * Math.cos(Math.toRadians(start)));
            float y = (float) (radius * Math.sin(Math.toRadians(start)));

            vertices[i] = new Vector2(x,y);
            start += STEP;
        }

        return vertices;
    }
}