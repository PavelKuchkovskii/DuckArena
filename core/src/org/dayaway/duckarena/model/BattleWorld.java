package org.dayaway.duckarena.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import org.dayaway.duckarena.model.api.IActor;
import org.dayaway.duckarena.model.api.IBang;
import org.dayaway.duckarena.model.api.IPlayer;
import org.dayaway.duckarena.model.api.ISoldier;
import org.dayaway.duckarena.model.api.ITrapRevolute;
import org.dayaway.duckarena.model.api.IWorld;
import org.dayaway.duckarena.screens.BattleScreen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

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

    private final List<ITrapRevolute> trapsRevolute;

    private final List<CircleKiller> circleKillers;

    private final List<IBang> bangs;

    Random random = new Random();

    private final Set<Body> destroy;

    public BattleWorld() {
        this.world = new World(new Vector2(0,0), true);
        this.actors = new ArrayList<>();
        this.crystals = new ArrayList<>();
        this.towers = new ArrayList<>();
        this.destroy = new HashSet<>();

        this.soldiers = new ArrayList<>();

        this.trapsRevolute = new ArrayList<>();

        this.circleKillers = new ArrayList<>();

        this.bangs = new ArrayList<>();

        bots = new ArrayList<>();

        createWorld();
    }


    @Override
    public void createWorld() {

        createMap();

        createPlayer();


        for (int i = 0; i < 3; i++) {
            createBot();
        }

        for (Bot bot : bots) {
            for (int i = 0; i < 1; i++) {
                createSoldier(bot);
            }
        }

        for (int i = 0; i < 1; i++) {
            createSoldier(player);
        }

        for (int i = 0; i < 300; i++) {
            createCrystal(player.getPosition().x, player.getPosition().y, 250f);
        }

        //createTower();

        create();
    }

    public void createPlayer() {
        BodyDef playerDef = new BodyDef();
        playerDef.type = BodyDef.BodyType.DynamicBody;
        playerDef.position.set(-50, -100);

        Body playerBody = world.createBody(playerDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(1f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f;
        playerBody.createFixture(fixtureDef).setUserData("player");

        circle = new CircleShape();
        circle.setRadius(250f);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.isSensor = true;
        playerBody.createFixture(fixtureDef).setUserData("player_sensor");

        circle = new CircleShape();
        circle.setRadius(1f);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.isSensor = true;

        Fixture fixture = playerBody.createFixture(fixtureDef);
        fixture.setUserData("player_mass");


        player = new Player("nickName", fixture, playerBody, BattleScreen.textures.getRandomActor());
        playerBody.setUserData(playerBody);

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

        soldierBody.createFixture(fixtureDef).setUserData(player + "soldier");
        circle.dispose();
    }


    //Создает кристалы, в радиусе вокруг игрока, вне видимости камеры
    @Override
    public void createCrystal() {
        float RADIUS = 250;
        float posX = player.getPosition().x;
        float posY = player.getPosition().y;

        BodyDef crystalDef = new BodyDef();
        crystalDef.type = BodyDef.BodyType.StaticBody;

        float x = random.nextInt(195) + 55;

        if(!random.nextBoolean()) {
            x *= -1;
        }

        x += posX;

        double maxY = Math.sqrt((RADIUS*RADIUS) - (Math.abs(x-posX) * Math.abs(x-posX)));

        try {
            float y = (float) (random.nextInt((int) (maxY * 2)) - maxY) + posY;
            crystalDef.position.set(x, y);
        }catch (Exception e) {
        }

        Body crystalBody = world.createBody(crystalDef);
        //Создаем новый кристалл
        Crystal crystal = new Crystal(crystalBody, BattleScreen.textures.crystal, 10);
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

    //Создает кристаллы, в указанном радиусе вокруг указанной точки
    @Override
    public void createCrystal(float posX, float posY, float RADIUS) {

        BodyDef crystalDef = new BodyDef();
        crystalDef.type = BodyDef.BodyType.StaticBody;

        float x = (random.nextInt((int) (RADIUS * 2)) - RADIUS) + posX;

        double maxY = Math.sqrt((RADIUS*RADIUS) - (Math.abs(x-posX) * Math.abs(x-posX)));

        try {
            float y = (float) (random.nextInt((int) (maxY * 2)) - maxY) + posY;
            crystalDef.position.set(x, y);
        }catch (Exception e) {
        }

        Body crystalBody = world.createBody(crystalDef);
        //Создаем новый кристалл
        Crystal crystal = new Crystal(crystalBody, BattleScreen.textures.crystal, 10);
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
        Tower tower = new Tower(towerBody, BattleScreen.textures.trap);
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

    @Override
    public Bot createBot() {
        BodyDef botDef = new BodyDef();
        botDef.type = BodyDef.BodyType.DynamicBody;
        botDef.position.set(random.nextInt(600) - 300, random.nextInt(600) - 300);

        Body botBody = world.createBody(botDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(1f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f;
        botBody.createFixture(fixtureDef).setUserData("bot");

        circle = new CircleShape();
        circle.setRadius(1f);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.isSensor = true;

        Fixture fixture = botBody.createFixture(fixtureDef);
        fixture.setUserData("bot_mass");

        Bot bot = new Bot("nickName", fixture, botBody, BattleScreen.textures.getRandomActor());
        bots.add(bot);
        botBody.setUserData(botBody);

        circle.dispose();

        return bot;
    }

    public void create() {

        createEdgeTrap(445, 30);
        createCrossTrap(0,0);
        createCrossTrap(-150,-150);
        createCrossTrap(150,150);
        createCrossTrap(-150,150);
        createCrossTrap(150,-150);
        createCircleKiller(-50,-50);
        createCircleKiller(50,50);
        createCircleKiller(-50,50);
    }

    private void createEdgeTrap(float radius, float angleStep) {

        float start = 0;

        for (int i = 0; i < 360/angleStep; i++) {

            float x = (float) (radius * Math.cos(Math.toRadians(start)));
            float y = (float) (radius * Math.sin(Math.toRadians(start)));
            start += angleStep;

            BodyDef defCircle = new BodyDef();
            defCircle.type = BodyDef.BodyType.StaticBody;
            defCircle.position.set(x, y);

            Body bodyCircle = world.createBody(defCircle);

            CircleShape circleShape = new CircleShape();
            circleShape.setRadius(2f);

            bodyCircle.createFixture(circleShape, 0);

            circleShape.dispose();


            BodyDef defPol = new BodyDef();
            defPol.type = BodyDef.BodyType.DynamicBody;
            defPol.position.set(x, y);

            defPol.angle = (float) Math.toRadians(defPol.position.angleDeg() + 90);

            Body bodyPol = world.createBody(defPol);

            PolygonShape pol = new PolygonShape();
            pol.setAsBox(5, 24);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = pol;
            fixtureDef.density = 5f;
            bodyPol.createFixture(fixtureDef).setUserData("trap_edge");

            pol.dispose();


            RevoluteJointDef jointDef = new RevoluteJointDef();
            jointDef.bodyA = bodyCircle;
            jointDef.bodyB = bodyPol;
            jointDef.localAnchorA.set(0,0);
            jointDef.localAnchorB.set(0,-24);

            jointDef.referenceAngle = (float) Math.toRadians(start);
            jointDef.enableLimit = true;
            jointDef.lowerAngle = (float) Math.toRadians(-20);
            jointDef.upperAngle = (float) Math.toRadians(145);
            jointDef.enableMotor = true;
            jointDef.maxMotorTorque = 5_000_000;
            jointDef.motorSpeed = (float) Math.toRadians(360);

            this.trapsRevolute.add(new TrapEdgeMap( (RevoluteJoint) world.createJoint(jointDef), BattleScreen.textures.trap));
        }

    }

    private void createCrossTrap(float posX, float posY) {

        float width = 3f;
        float height = 30f;

        BodyDef defCircle = new BodyDef();
        defCircle.type = BodyDef.BodyType.StaticBody;
        defCircle.position.set(posX, posY);

        Body bodyCircle = world.createBody(defCircle);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(2f);

        bodyCircle.createFixture(circleShape, 0);

        circleShape.dispose();


        BodyDef defPol = new BodyDef();
        defPol.type = BodyDef.BodyType.DynamicBody;
        defPol.position.set(posX, posY);

        Body bodyPol = world.createBody(defPol);

        //Создаем первуя часть креста
        PolygonShape polygonShape = new PolygonShape();
        //Указываем вершины
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(-width,width);
        vertices[1] = new Vector2(-width,height);
        vertices[2] = new Vector2(width,height);
        vertices[3] = new Vector2(width,width);
        polygonShape.set(vertices);

        //Создаем фикстуру
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 5f;
        bodyPol.createFixture(fixtureDef).setUserData("trap_cross");

        //Создаем вторую часть креста
        vertices[0] = new Vector2(width,width);
        vertices[1] = new Vector2(height,width);
        vertices[2] = new Vector2(height,-width);
        vertices[3] = new Vector2(width,-width);

        polygonShape.set(vertices);
        fixtureDef.shape = polygonShape;
        bodyPol.createFixture(fixtureDef).setUserData("trap_cross");

        //Создаем тертью часть креста
        vertices[0] = new Vector2(width,-width);
        vertices[1] = new Vector2(width,-height);
        vertices[2] = new Vector2(-width,-height);
        vertices[3] = new Vector2(-width,-width);

        polygonShape.set(vertices);
        fixtureDef.shape = polygonShape;
        bodyPol.createFixture(fixtureDef).setUserData("trap_cross");

        //Создаем четвертую часть креста
        vertices[0] = new Vector2(-width,-width);
        vertices[1] = new Vector2(-height,-width);
        vertices[2] = new Vector2(-height,width);
        vertices[3] = new Vector2(-width,width);

        polygonShape.set(vertices);
        fixtureDef.shape = polygonShape;
        bodyPol.createFixture(fixtureDef).setUserData("trap_cross");

        polygonShape.dispose();


        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.bodyA = bodyCircle;
        jointDef.bodyB = bodyPol;
        jointDef.localAnchorA.set(0,0);
        jointDef.localAnchorB.set(0,0);

        jointDef.enableMotor = true;
        jointDef.maxMotorTorque = 5_000_000;
        jointDef.motorSpeed = (float) Math.toRadians(360);

        this.trapsRevolute.add(new TrapCrossMap( (RevoluteJoint) world.createJoint(jointDef), BattleScreen.textures.trap_cross));

    }

    public void createCircleKiller(float x, float y) {
        BodyDef circleKillerDef = new BodyDef();
        circleKillerDef.type = BodyDef.BodyType.DynamicBody;
        circleKillerDef.position.set(x, y);


        Body circleKillerBody = world.createBody(circleKillerDef);
        circleKillerBody.setLinearVelocity(x*2, y*2);

        CircleKiller circleKiller = new CircleKiller(circleKillerBody, BattleScreen.textures.crystal);
        circleKillers.add(circleKiller);

        CircleShape circle = new CircleShape();
        circle.setRadius(20f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.restitution = 2f;
        circleKillerBody.createFixture(fixtureDef).setUserData("trap_circle");

        circle.dispose();
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

        actors.addAll(circleKillers);

        actors.addAll(trapsRevolute);

        //Солдаты в конце перед башнями
        actors.addAll(soldiers);

        //Добавляем башни
        actors.addAll(towers);

        actors.addAll(bangs);

        return actors;
    }

    @Override
    public List<ISoldier> getSoldiers() {
        return this.soldiers;
    }

    @Override
    public boolean isExist(Body body) {

        if(body.getUserData() != null) {
            if(body.getUserData() instanceof Crystal) {
                return crystals.contains((Crystal) body.getUserData());
            }

        }
        return false;
    }

    @Override
    public List<ITrapRevolute> getTraps() {
        return this.trapsRevolute;
    }

    @Override
    public List<CircleKiller> getCircleTraps() {
        return this.circleKillers;
    }

    @Override
    public List<IBang> getBangs() {
        return this.bangs;
    }

    @Override
    public void addBang(IBang bang) {
        this.bangs.add(bang);
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
    public Set<Body> getDestroy() {
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
        final float RADIUS = 450f;

        Vector2 circleCentre = new Vector2(0,0);

        //Настройка тела арены
        BodyDef arenaDef = new BodyDef();
        arenaDef.type = BodyDef.BodyType.StaticBody;
        //Устанавливаем позицию арены в мире
        arenaDef.position.set(circleCentre);

        //Создаем тело в мире
        Body arenaBody = world.createBody(arenaDef);
        //Добавляем арену в список арен и присваиваем ее телу
        Arena arena = new Arena(arenaBody, BattleScreen.textures.map);
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
