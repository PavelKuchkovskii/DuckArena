package org.dayaway.duckarena.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import org.dayaway.duckarena.model.api.IActor;
import org.dayaway.duckarena.model.api.IPlayer;
import org.dayaway.duckarena.model.api.ISoldier;
import org.dayaway.duckarena.model.api.IWorld;
import org.dayaway.duckarena.screens.BattleScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattleWorld implements IWorld {

    private final World world;
    private final List<Arena> arenas;
    private final List<Bridge> bridges;

    //Список актеров инициализируем один раз и перед каждой отдачей очищаем
    private final List<IActor> actors;

    private IPlayer player;

    private final List<Bot> bots;

    private final List<ISoldier> soldiers;

    private final List<Crystal> crystals;
    private final List<Tower> towers;



    Random random = new Random();

    private final List<Body> destroy;

    public BattleWorld() {
        this.world = new World(new Vector2(0,0), true);
        this.arenas = new ArrayList<>();
        this.bridges = new ArrayList<>();
        this.actors = new ArrayList<>();
        this.crystals = new ArrayList<>();
        this.towers = new ArrayList<>();
        this.destroy = new ArrayList<>();

        this.soldiers = new ArrayList<>();

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
        actors.addAll(arenas);

        //Потом добавляем кристаллы
        actors.addAll(crystals);

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
    public List<Arena> getArenas() {
        return this.arenas;
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

    public void createMap() {

        final float RADIUS = 300f;
        //Длина моста
        final float BRIDGE_LENGTH_G = 60;
        //Ширина моста
        final float BRIDGE_WIDTH = 15;

        //ШАГ ОТРИСОВКИ КРУГА ( 1 градус по дефолту)
        final int CIRCLE_STEP = 1;
        //Кол-во точек, вообще нужно 360 что бы полный круг был
        //Но в конце можно просто добавить перввую точку
        final int CIRCLE_POINTS = 360;
        //Угол боковых проходов
        final int BRIDGE_ANGLE = 45;

        //ТОЧКА СОЕДИНЕНИЯ МОСТА
        float start;
        //КОЛ-ВО ТОЧЕК МЕЖДУ НИМИ
        float countBetween;

        //ЦЕНТР КРУГА
        Vector2 circleCentre = new Vector2(0,0);

        //Список вершин формы
        List<Vector2> vertices = new ArrayList<>();

        //Настройка тела - для всех одинаковая
        BodyDef arenaDef = new BodyDef();
        arenaDef.type = BodyDef.BodyType.StaticBody;

        //Фикстура тела, настройка одинаковая для всех кроме формы
        FixtureDef fd = new FixtureDef();
        fd.density = 0;
        fd.friction = 0f;
        fd.restitution = 0f;

//============================ПЕРВЫЙ КРУГ - ЦЕНТРАЛЬНЫЙ=============================================
        //Устанавливаем позицию арены в мире
        arenaDef.position.set(circleCentre);

        //Создаем тело в мире
        Body arena = world.createBody(arenaDef);

        //Добавляем арену в список арен и присваиваем ее телу
        Arena entity = new Arena(arena, BattleScreen.map);
        arenas.add(entity);
        arena.setUserData(entity);

        start = 180 + BRIDGE_WIDTH/2f;
        countBetween = CIRCLE_POINTS - BRIDGE_WIDTH/2f - start + 1;

        for (int i = 0; i < countBetween; i++) {
            float x = circleCentre.x + (float) (RADIUS * Math.cos(Math.toRadians(start)));
            float y = circleCentre.y + (float) (RADIUS * Math.sin(Math.toRadians(start)));

            vertices.add(new Vector2(x,y));
            start += CIRCLE_STEP;
        }

        //Создаем форму
        ChainShape chain = new ChainShape();
        //Добавляем точки телу
        chain.createChain(vertices.toArray(new Vector2[0]));

        //Очищаем список точек
        vertices.clear();

        //Присваиваем фикстуре форму
        fd.shape = chain;

        //Присваиваем фикстуру телу
        arena.createFixture(fd).setUserData("map");
        //ОБЯЗАТЕЛЬНО УНИЧТОЖАЕМ ФОРМУ ПОСЛЕ ПРИСВАИВАНИЯ
        chain.dispose();

        //ВТОРАЯ ЧАСТЬ КРУГА------

        start = BRIDGE_WIDTH/2f;
        countBetween = 180 - BRIDGE_WIDTH/2f - start + 1;

        for (int i = 0; i < countBetween; i++) {
            float x = circleCentre.x + (float) (RADIUS * Math.cos(Math.toRadians(start)));
            float y = circleCentre.y + (float) (RADIUS * Math.sin(Math.toRadians(start)));

            vertices.add(new Vector2(x,y));
            start += CIRCLE_STEP;
        }

        //Создаем форму
        chain = new ChainShape();
        //Добавляем точки телу
        chain.createChain(vertices.toArray(new Vector2[0]));

        //Очищаем список точек
        vertices.clear();

        //Присваиваем фикстуре форму
        fd.shape = chain;

        //Присваиваем фикстуру телу
        arena.createFixture(fd).setUserData("map");
        //ОБЯЗАТЕЛЬНО УНИЧТОЖАЕМ ФОРМУ ПОСЛЕ ПРИСВАИВАНИЯ
        chain.dispose();

//=============================ВТОРОЙ КРУГ - ПРАВЫЙ ЦЕНТРАЛЬНЫЙ=====================================

        //Устанавливаем позицию арены в мире
        arenaDef.position.set(circleCentre.x + RADIUS * 2 + BRIDGE_LENGTH_G,0);

        //Создаем тело в мире
        arena = world.createBody(arenaDef);

        //Добавляем арену в список арен и присваиваем ее телу
        entity = new Arena(arena, BattleScreen.map);
        arenas.add(entity);
        arena.setUserData(entity);

        start = 180 + BRIDGE_WIDTH/2f;
        countBetween = CIRCLE_POINTS - BRIDGE_ANGLE - BRIDGE_WIDTH/2f - start + 1;

        for (int i = 0; i < countBetween; i++) {
            float x = circleCentre.x + (float) (RADIUS * Math.cos(Math.toRadians(start)));
            float y = circleCentre.y + (float) (RADIUS * Math.sin(Math.toRadians(start)));

            vertices.add(new Vector2(x,y));
            start += CIRCLE_STEP;
        }

        //Создаем форму
        chain = new ChainShape();
        //Добавляем точки телу
        chain.createChain(vertices.toArray(new Vector2[0]));

        //Очищаем список точек
        vertices.clear();

        //Присваиваем фикстуре форму
        fd.shape = chain;

        //Присваиваем фикстуру телу
        arena.createFixture(fd).setUserData("map");
        //ОБЯЗАТЕЛЬНО УНИЧТОЖАЕМ ФОРМУ ПОСЛЕ ПРИСВАИВАНИЯ
        chain.dispose();

        //ВТОРАЯ ЧАСТЬ КРУГА------

        start = CIRCLE_POINTS - BRIDGE_ANGLE + BRIDGE_WIDTH/2f;
        countBetween = 90 - BRIDGE_WIDTH + 1;

        for (int i = 0; i < countBetween; i++) {
            float x = circleCentre.x + (float) (RADIUS * Math.cos(Math.toRadians(start)));
            float y = circleCentre.y + (float) (RADIUS * Math.sin(Math.toRadians(start)));

            vertices.add(new Vector2(x,y));
            start += CIRCLE_STEP;
        }

        //Создаем форму
        chain = new ChainShape();
        //Добавляем точки телу
        chain.createChain(vertices.toArray(new Vector2[0]));

        //Очищаем список точек
        vertices.clear();

        //Присваиваем фикстуре форму
        fd.shape = chain;

        //Присваиваем фикстуру телу
        arena.createFixture(fd).setUserData("map");
        //ОБЯЗАТЕЛЬНО УНИЧТОЖАЕМ ФОРМУ ПОСЛЕ ПРИСВАИВАНИЯ
        chain.dispose();

        //ТРЕТЬЯ ЧАСТЬ КРУГА------

        start = BRIDGE_ANGLE + BRIDGE_WIDTH/2f;
        countBetween = 180 - BRIDGE_WIDTH/2f - start + 1;

        for (int i = 0; i < countBetween; i++) {
            float x = circleCentre.x + (float) (RADIUS * Math.cos(Math.toRadians(start)));
            float y = circleCentre.y + (float) (RADIUS * Math.sin(Math.toRadians(start)));

            vertices.add(new Vector2(x,y));
            start += CIRCLE_STEP;
        }

        //Создаем форму
        chain = new ChainShape();
        //Добавляем точки телу
        chain.createChain(vertices.toArray(new Vector2[0]));

        //Очищаем список точек
        vertices.clear();

        //Присваиваем фикстуре форму
        fd.shape = chain;

        //Присваиваем фикстуру телу
        arena.createFixture(fd).setUserData("map");
        //ОБЯЗАТЕЛЬНО УНИЧТОЖАЕМ ФОРМУ ПОСЛЕ ПРИСВАИВАНИЯ
        chain.dispose();

//===================================ТРЕТИЙ КРУГ - ПРАВЫЙ НИЖНИЙ====================================

        //Устанавливаем позицию арены в мире
        arenaDef.position.set(arenaDef.position.x + (float) ((RADIUS * 2 + BRIDGE_LENGTH_G) * Math.cos(Math.toRadians(CIRCLE_POINTS-BRIDGE_ANGLE))),
                arenaDef.position.y + (float) ((RADIUS * 2 + BRIDGE_LENGTH_G) * Math.sin(Math.toRadians(CIRCLE_POINTS-BRIDGE_ANGLE))));

        //Создаем тело в мире
        arena = world.createBody(arenaDef);

        //Добавляем арену в список арен и присваиваем ее телу
        entity = new Arena(arena, BattleScreen.map);
        arenas.add(entity);
        arena.setUserData(entity);

        start = 90 + BRIDGE_ANGLE + BRIDGE_WIDTH/2f;
        countBetween = CIRCLE_POINTS - start + 90 + BRIDGE_ANGLE - BRIDGE_WIDTH/2f + 1;

        for (int i = 0; i < countBetween; i++) {
            float x = circleCentre.x + (float) (RADIUS * Math.cos(Math.toRadians(start)));
            float y = circleCentre.y + (float) (RADIUS * Math.sin(Math.toRadians(start)));

            vertices.add(new Vector2(x,y));
            start += CIRCLE_STEP;
        }

        //Создаем форму
        chain = new ChainShape();
        //Добавляем точки телу
        chain.createChain(vertices.toArray(new Vector2[0]));

        //Очищаем список точек
        vertices.clear();

        //Присваиваем фикстуре форму
        fd.shape = chain;

        //Присваиваем фикстуру телу
        arena.createFixture(fd).setUserData("map");
        //ОБЯЗАТЕЛЬНО УНИЧТОЖАЕМ ФОРМУ ПОСЛЕ ПРИСВАИВАНИЯ
        chain.dispose();

//==================================ЧЕТВЕРТЫЙ КРУГ - ПРАВЫЙ ВЕРХНИЙ=================================

        //Устанавливаем позицию арены в мире
        arenaDef.position.set(RADIUS * 2 + BRIDGE_LENGTH_G + (float) ((RADIUS * 2 + BRIDGE_LENGTH_G) * Math.cos(Math.toRadians(BRIDGE_ANGLE))),
                0 + (float) ((RADIUS * 2 + BRIDGE_LENGTH_G) * Math.sin(Math.toRadians(BRIDGE_ANGLE))));

        //Создаем тело в мире
        arena = world.createBody(arenaDef);

        //Добавляем арену в список арен и присваиваем ее телу
        entity = new Arena(arena, BattleScreen.map);
        arenas.add(entity);
        arena.setUserData(entity);

        start = 180 + BRIDGE_ANGLE + BRIDGE_WIDTH/2f;
        countBetween = CIRCLE_POINTS - BRIDGE_WIDTH + 1;

        for (int i = 0; i < countBetween; i++) {
            float x = circleCentre.x + (float) (RADIUS * Math.cos(Math.toRadians(start)));
            float y = circleCentre.y + (float) (RADIUS * Math.sin(Math.toRadians(start)));

            vertices.add(new Vector2(x,y));
            start += CIRCLE_STEP;
        }

        //Создаем форму
        chain = new ChainShape();
        //Добавляем точки телу
        chain.createChain(vertices.toArray(new Vector2[0]));

        //Очищаем список точек
        vertices.clear();

        //Присваиваем фикстуре форму
        fd.shape = chain;

        //Присваиваем фикстуру телу
        arena.createFixture(fd).setUserData("map");
        //ОБЯЗАТЕЛЬНО УНИЧТОЖАЕМ ФОРМУ ПОСЛЕ ПРИСВАИВАНИЯ
        chain.dispose();

//==========================ПЯТЫЙ КРУГ - ЛЕВЫЙ ЦЕНТРАЛЬНЫЙ==========================================

        //Устанавливаем позицию арены в мире
        arenaDef.position.set(0 - RADIUS * 2 - BRIDGE_LENGTH_G,0);

        //Создаем тело в мире
        arena = world.createBody(arenaDef);

        //Добавляем арену в список арен и присваиваем ее телу
        entity = new Arena(arena, BattleScreen.map);
        arenas.add(entity);
        arena.setUserData(entity);

        start = 180 - BRIDGE_ANGLE + BRIDGE_WIDTH/2f;
        countBetween = 180 + BRIDGE_ANGLE - BRIDGE_WIDTH/2f - start + 1;

        for (int i = 0; i < countBetween; i++) {
            float x = circleCentre.x + (float) (RADIUS * Math.cos(Math.toRadians(start)));
            float y = circleCentre.y + (float) (RADIUS * Math.sin(Math.toRadians(start)));

            vertices.add(new Vector2(x,y));
            start += CIRCLE_STEP;
        }

        //Создаем форму
        chain = new ChainShape();
        //Добавляем точки телу
        chain.createChain(vertices.toArray(new Vector2[0]));

        //Очищаем список точек
        vertices.clear();

        //Присваиваем фикстуре форму
        fd.shape = chain;

        //Присваиваем фикстуру телу
        arena.createFixture(fd).setUserData("map");
        //ОБЯЗАТЕЛЬНО УНИЧТОЖАЕМ ФОРМУ ПОСЛЕ ПРИСВАИВАНИЯ
        chain.dispose();

        //ВТОРАЯ ЧАСТЬ КРУГА------

        start = 0 + BRIDGE_WIDTH/2f;
        countBetween = 180 - BRIDGE_ANGLE - BRIDGE_WIDTH/2f - start + 1;

        for (int i = 0; i < countBetween; i++) {
            float x = circleCentre.x + (float) (RADIUS * Math.cos(Math.toRadians(start)));
            float y = circleCentre.y + (float) (RADIUS * Math.sin(Math.toRadians(start)));

            vertices.add(new Vector2(x,y));
            start += CIRCLE_STEP;
        }

        //Создаем форму
        chain = new ChainShape();
        //Добавляем точки телу
        chain.createChain(vertices.toArray(new Vector2[0]));

        //Очищаем список точек
        vertices.clear();

        //Присваиваем фикстуре форму
        fd.shape = chain;

        //Присваиваем фикстуру телу
        arena.createFixture(fd).setUserData("map");
        //ОБЯЗАТЕЛЬНО УНИЧТОЖАЕМ ФОРМУ ПОСЛЕ ПРИСВАИВАНИЯ
        chain.dispose();

        //ВТОРАЯ ЧАСТЬ КРУГА------

        //ПЯТЫЙ КРУГ - ЛЕВЫЙ ЦЕНТРАЛЬНЫЙ
        start = 180 + BRIDGE_ANGLE + BRIDGE_WIDTH/2f;
        countBetween = CIRCLE_POINTS - BRIDGE_WIDTH/2f - start + 1;

        for (int i = 0; i < countBetween; i++) {
            float x = circleCentre.x + (float) (RADIUS * Math.cos(Math.toRadians(start)));
            float y = circleCentre.y + (float) (RADIUS * Math.sin(Math.toRadians(start)));

            vertices.add(new Vector2(x,y));
            start += CIRCLE_STEP;
        }

        //Создаем форму
        chain = new ChainShape();
        //Добавляем точки телу
        chain.createChain(vertices.toArray(new Vector2[0]));

        //Очищаем список точек
        vertices.clear();

        //Присваиваем фикстуре форму
        fd.shape = chain;

        //Присваиваем фикстуру телу
        arena.createFixture(fd).setUserData("map");
        //ОБЯЗАТЕЛЬНО УНИЧТОЖАЕМ ФОРМУ ПОСЛЕ ПРИСВАИВАНИЯ
        chain.dispose();

//============================ШЕСТОЙ КРУГ - ЛЕВЫЙ ВЕРХНИЙ===========================================

        //Устанавливаем позицию арены в мире
        arenaDef.position.set(arenaDef.position.x + (float) ((RADIUS * 2 + BRIDGE_LENGTH_G) * Math.cos(Math.toRadians(135))),
                arenaDef.position.y + (float) ((RADIUS * 2 + BRIDGE_LENGTH_G) * Math.sin(Math.toRadians(135))));

        //Создаем тело в мире
        arena = world.createBody(arenaDef);

        //Добавляем арену в список арен и присваиваем ее телу
        entity = new Arena(arena, BattleScreen.map);
        arenas.add(entity);
        arena.setUserData(entity);

        start = CIRCLE_POINTS - BRIDGE_ANGLE + BRIDGE_WIDTH/2f;
        countBetween = CIRCLE_POINTS - BRIDGE_WIDTH + 1;

        for (int i = 0; i < countBetween; i++) {
            float x = circleCentre.x + (float) (RADIUS * Math.cos(Math.toRadians(start)));
            float y = circleCentre.y + (float) (RADIUS * Math.sin(Math.toRadians(start)));

            vertices.add(new Vector2(x,y));
            start += CIRCLE_STEP;
        }

        //Создаем форму
        chain = new ChainShape();
        //Добавляем точки телу
        chain.createChain(vertices.toArray(new Vector2[0]));

        //Очищаем список точек
        vertices.clear();

        //Присваиваем фикстуре форму
        fd.shape = chain;

        //Присваиваем фикстуру телу
        arena.createFixture(fd).setUserData("map");
        //ОБЯЗАТЕЛЬНО УНИЧТОЖАЕМ ФОРМУ ПОСЛЕ ПРИСВАИВАНИЯ
        chain.dispose();

//================================СЕДЬМОЙ КРУГ - ЛЕВЫЙ НИЖНИЙ=======================================

        //Устанавливаем позицию арены в мире
        arenaDef.position.set(-RADIUS * 2 - BRIDGE_LENGTH_G + (float) ((RADIUS * 2 + BRIDGE_LENGTH_G) * Math.cos(Math.toRadians(180+BRIDGE_ANGLE))),
                0 + (float) ((RADIUS * 2 + BRIDGE_LENGTH_G) * Math.sin(Math.toRadians(180+BRIDGE_ANGLE))));

        //Создаем тело в мире
        arena = world.createBody(arenaDef);

        //Добавляем арену в список арен и присваиваем ее телу
        entity = new Arena(arena, BattleScreen.map);
        arenas.add(entity);
        arena.setUserData(entity);

        start = BRIDGE_ANGLE + BRIDGE_WIDTH/2f;
        countBetween = CIRCLE_POINTS + BRIDGE_ANGLE - BRIDGE_WIDTH/2f - start + 1;

        for (int i = 0; i < countBetween; i++) {
            float x = circleCentre.x + (float) (RADIUS * Math.cos(Math.toRadians(start)));
            float y = circleCentre.y + (float) (RADIUS * Math.sin(Math.toRadians(start)));

            vertices.add(new Vector2(x,y));
            start += CIRCLE_STEP;
        }

        //Создаем форму
        chain = new ChainShape();
        //Добавляем точки телу
        chain.createChain(vertices.toArray(new Vector2[0]));

        //Очищаем список точек
        vertices.clear();

        //Присваиваем фикстуре форму
        fd.shape = chain;

        //Присваиваем фикстуру телу
        arena.createFixture(fd).setUserData("map");
        //ОБЯЗАТЕЛЬНО УНИЧТОЖАЕМ ФОРМУ ПОСЛЕ ПРИСВАИВАНИЯ
        chain.dispose();

//================================МОСТЫ=============================================================
//================================ПЕРВЫЙ ЛЕВЫЙ НИЖНИЙ ДИАГОНАЛЬНЫЙ==================================

        //Устанавливаем позицию арены в мире
        arenaDef.position.set(arenas.get(6).getPosition().x + (float) ((RADIUS + BRIDGE_LENGTH_G/2f) * Math.cos(Math.toRadians(BRIDGE_ANGLE))),
                arenas.get(6).getPosition().y + (float) ((RADIUS + BRIDGE_LENGTH_G/2f) * Math.sin(Math.toRadians(BRIDGE_ANGLE))));

        //Создаем тело в мире
        arena = world.createBody(arenaDef);

        //НАХОДИМ ЦЕНТР ПЯТОГО КРУГА ОТНОСИТЕЛЬНО НАШЕГО ТЕЛА
        float xb1 = (float) ((BRIDGE_LENGTH_G/2f + RADIUS) * Math.cos(Math.toRadians(BRIDGE_ANGLE)));
        float yb1 = (float) ((BRIDGE_LENGTH_G/2f + RADIUS) * Math.sin(Math.toRadians(BRIDGE_ANGLE)));

        //НАХОДИМ ЦЕНТР СЕДЬМОГО КРУГА ОТНОСИТЕЛЬНО НАШЕГО ТЕЛА
        float xb2 = (float) ((BRIDGE_LENGTH_G/2f + RADIUS) * Math.cos(Math.toRadians(180 + BRIDGE_ANGLE)));
        float yb2 = (float) ((BRIDGE_LENGTH_G/2f + RADIUS) * Math.sin(Math.toRadians(180 + BRIDGE_ANGLE)));

        vertices.add(new Vector2(xb1 + (float) ((RADIUS) * Math.cos(Math.toRadians(180 + BRIDGE_ANGLE - BRIDGE_WIDTH/2f))),
                yb1 + (float) ((RADIUS) * Math.sin(Math.toRadians(180 + BRIDGE_ANGLE - BRIDGE_WIDTH/2f)))));


        vertices.add(new Vector2(xb2 + (float) ((RADIUS) * Math.cos(Math.toRadians(BRIDGE_ANGLE + BRIDGE_WIDTH/2f))),
                yb2 + (float) ((RADIUS) * Math.sin(Math.toRadians(BRIDGE_ANGLE + BRIDGE_WIDTH/2f)))));

        //Создаем форму
        chain = new ChainShape();
        //Добавляем точки телу
        chain.createChain(vertices.toArray(new Vector2[0]));

        //Очищаем список точек
        vertices.clear();

        //Присваиваем фикстуре форму
        fd.shape = chain;

        //Присваиваем фикстуру телу
        arena.createFixture(fd).setUserData("map");
        //ОБЯЗАТЕЛЬНО УНИЧТОЖАЕМ ФОРМУ ПОСЛЕ ПРИСВАИВАНИЯ
        chain.dispose();

        vertices.add(new Vector2(xb1 + (float) ((RADIUS) * Math.cos(Math.toRadians(180 + BRIDGE_ANGLE + BRIDGE_WIDTH/2f))),
                yb1 + (float) ((RADIUS) * Math.sin(Math.toRadians(180 + BRIDGE_ANGLE + BRIDGE_WIDTH/2f)))));


        vertices.add(new Vector2(xb2 + (float) ((RADIUS) * Math.cos(Math.toRadians(BRIDGE_ANGLE - BRIDGE_WIDTH/2f))),
                yb2 + (float) ((RADIUS) * Math.sin(Math.toRadians(BRIDGE_ANGLE - BRIDGE_WIDTH/2f)))));

        //Создаем форму
        chain = new ChainShape();
        //Добавляем точки телу
        chain.createChain(vertices.toArray(new Vector2[0]));

        //Очищаем список точек
        vertices.clear();

        //Присваиваем фикстуре форму
        fd.shape = chain;

        //Присваиваем фикстуру телу
        arena.createFixture(fd).setUserData("map");
        //ОБЯЗАТЕЛЬНО УНИЧТОЖАЕМ ФОРМУ ПОСЛЕ ПРИСВАИВАНИЯ
        chain.dispose();

//================================ВТОРОЙ ПРАВЫЙ ВЕРХНИЙ ДИАГОНАЛЬНЫЙ================================

        //Устанавливаем позицию арены в мире
        arenaDef.position.set(arenas.get(1).getPosition().x + (float) ((RADIUS + BRIDGE_LENGTH_G/2f) * Math.cos(Math.toRadians(BRIDGE_ANGLE))),
                arenas.get(1).getPosition().y + (float) ((RADIUS + BRIDGE_LENGTH_G/2f) * Math.sin(Math.toRadians(BRIDGE_ANGLE))));

        //Создаем тело в мире
        arena = world.createBody(arenaDef);

        //НАХОДИМ ЦЕНТР ЧЕТВЕРТОГО КРУГА ОТНОСИТЕЛЬНО НАШЕГО ТЕЛА
        xb1 = (float) ((BRIDGE_LENGTH_G/2f + RADIUS) * Math.cos(Math.toRadians(BRIDGE_ANGLE)));
        yb1 = (float) ((BRIDGE_LENGTH_G/2f + RADIUS) * Math.sin(Math.toRadians(BRIDGE_ANGLE)));

        //НАХОДИМ ЦЕНТР ВТОРОГО КРУГА ОТНОСИТЕЛЬНО НАШЕГО ТЕЛА
        xb2 = (float) ((BRIDGE_LENGTH_G/2f + RADIUS) * Math.cos(Math.toRadians(180 + BRIDGE_ANGLE)));
        yb2 = (float) ((BRIDGE_LENGTH_G/2f + RADIUS) * Math.sin(Math.toRadians(180 + BRIDGE_ANGLE)));

        vertices.add(new Vector2(xb1 + (float) ((RADIUS) * Math.cos(Math.toRadians(180 + BRIDGE_ANGLE - BRIDGE_WIDTH/2f))),
                yb1 + (float) ((RADIUS) * Math.sin(Math.toRadians(180 + BRIDGE_ANGLE - BRIDGE_WIDTH/2f)))));


        vertices.add(new Vector2(xb2 + (float) ((RADIUS) * Math.cos(Math.toRadians(BRIDGE_ANGLE + BRIDGE_WIDTH/2f))),
                yb2 + (float) ((RADIUS) * Math.sin(Math.toRadians(BRIDGE_ANGLE + BRIDGE_WIDTH/2f)))));

        //Создаем форму
        chain = new ChainShape();
        //Добавляем точки телу
        chain.createChain(vertices.toArray(new Vector2[0]));

        //Очищаем список точек
        vertices.clear();

        //Присваиваем фикстуре форму
        fd.shape = chain;

        //Присваиваем фикстуру телу
        arena.createFixture(fd).setUserData("map");
        //ОБЯЗАТЕЛЬНО УНИЧТОЖАЕМ ФОРМУ ПОСЛЕ ПРИСВАИВАНИЯ
        chain.dispose();

        vertices.add(new Vector2(xb1 + (float) ((RADIUS) * Math.cos(Math.toRadians(180 + BRIDGE_ANGLE + BRIDGE_WIDTH/2f))),
                yb1 + (float) ((RADIUS) * Math.sin(Math.toRadians(180 + BRIDGE_ANGLE + BRIDGE_WIDTH/2f)))));


        vertices.add(new Vector2(xb2 + (float) ((RADIUS) * Math.cos(Math.toRadians(BRIDGE_ANGLE - BRIDGE_WIDTH/2f))),
                yb2 + (float) ((RADIUS) * Math.sin(Math.toRadians(BRIDGE_ANGLE - BRIDGE_WIDTH/2f)))));

        //Создаем форму
        chain = new ChainShape();
        //Добавляем точки телу
        chain.createChain(vertices.toArray(new Vector2[0]));

        //Очищаем список точек
        vertices.clear();

        //Присваиваем фикстуре форму
        fd.shape = chain;

        //Присваиваем фикстуру телу
        arena.createFixture(fd).setUserData("map");
        //ОБЯЗАТЕЛЬНО УНИЧТОЖАЕМ ФОРМУ ПОСЛЕ ПРИСВАИВАНИЯ
        chain.dispose();

//================================ТРЕТИЙ ЛЕВЫЙ ВЕРХНИЙ ДИАГОНАЛЬНЫЙ=================================

        //Устанавливаем позицию арены в мире
        arenaDef.position.set(arenas.get(4).getPosition().x + (float) ((RADIUS + BRIDGE_LENGTH_G/2f) * Math.cos(Math.toRadians(135))),
                arenas.get(4).getPosition().y + (float) ((RADIUS + BRIDGE_LENGTH_G/2f) * Math.sin(Math.toRadians(135))));

        //Создаем тело в мире
        arena = world.createBody(arenaDef);

        //НАХОДИМ ЦЕНТР ШЕСТОГО КРУГА ОТНОСИТЕЛЬНО НАШЕГО ТЕЛА
        xb1 = (float) ((BRIDGE_LENGTH_G/2f + RADIUS) * Math.cos(Math.toRadians(135)));
        yb1 = (float) ((BRIDGE_LENGTH_G/2f + RADIUS) * Math.sin(Math.toRadians(135)));

        //НАХОДИМ ЦЕНТР ПЯТОГО КРУГА ОТНОСИТЕЛЬНО НАШЕГО ТЕЛА
        xb2 = (float) ((BRIDGE_LENGTH_G/2f + RADIUS) * Math.cos(Math.toRadians(CIRCLE_POINTS - BRIDGE_ANGLE)));
        yb2 = (float) ((BRIDGE_LENGTH_G/2f + RADIUS) * Math.sin(Math.toRadians(CIRCLE_POINTS - BRIDGE_ANGLE)));

        vertices.add(new Vector2(xb1 + (float) ((RADIUS) * Math.cos(Math.toRadians(CIRCLE_POINTS - BRIDGE_ANGLE + BRIDGE_WIDTH/2f))),
                yb1 + (float) ((RADIUS) * Math.sin(Math.toRadians(CIRCLE_POINTS - BRIDGE_ANGLE + BRIDGE_WIDTH/2f)))));


        vertices.add(new Vector2(xb2 + (float) ((RADIUS) * Math.cos(Math.toRadians(135 - BRIDGE_WIDTH/2f))),
                yb2 + (float) ((RADIUS) * Math.sin(Math.toRadians(135 - BRIDGE_WIDTH/2f)))));

        //Создаем форму
        chain = new ChainShape();
        //Добавляем точки телу
        chain.createChain(vertices.toArray(new Vector2[0]));

        //Очищаем список точек
        vertices.clear();

        //Присваиваем фикстуре форму
        fd.shape = chain;

        //Присваиваем фикстуру телу
        arena.createFixture(fd).setUserData("map");
        //ОБЯЗАТЕЛЬНО УНИЧТОЖАЕМ ФОРМУ ПОСЛЕ ПРИСВАИВАНИЯ
        chain.dispose();

        vertices.add(new Vector2(xb1 + (float) ((RADIUS) * Math.cos(Math.toRadians(CIRCLE_POINTS - BRIDGE_ANGLE - BRIDGE_WIDTH/2f))),
                yb1 + (float) ((RADIUS) * Math.sin(Math.toRadians(CIRCLE_POINTS - BRIDGE_ANGLE - BRIDGE_WIDTH/2f)))));


        vertices.add(new Vector2(xb2 + (float) ((RADIUS) * Math.cos(Math.toRadians(135 + BRIDGE_WIDTH/2f))),
                yb2 + (float) ((RADIUS) * Math.sin(Math.toRadians(135 + BRIDGE_WIDTH/2f)))));

        //Создаем форму
        chain = new ChainShape();
        //Добавляем точки телу
        chain.createChain(vertices.toArray(new Vector2[0]));

        //Очищаем список точек
        vertices.clear();

        //Присваиваем фикстуре форму
        fd.shape = chain;

        //Присваиваем фикстуру телу
        arena.createFixture(fd).setUserData("map");
        //ОБЯЗАТЕЛЬНО УНИЧТОЖАЕМ ФОРМУ ПОСЛЕ ПРИСВАИВАНИЯ
        chain.dispose();
//================================ЧЕТВЕРТЫЙ ПРАВЫЙ НИЖНИЙ ДИАГОНАЛЬНЫЙ==============================

        //Устанавливаем позицию арены в мире
        arenaDef.position.set(arenas.get(2).getPosition().x + (float) ((RADIUS + BRIDGE_LENGTH_G/2f) * Math.cos(Math.toRadians(135))),
                arenas.get(2).getPosition().y + (float) ((RADIUS + BRIDGE_LENGTH_G/2f) * Math.sin(Math.toRadians(135))));

        //Создаем тело в мире
        arena = world.createBody(arenaDef);

        //НАХОДИМ ЦЕНТР ВТОРОГО КРУГА ОТНОСИТЕЛЬНО НАШЕГО ТЕЛА
        xb1 = (float) ((BRIDGE_LENGTH_G/2f + RADIUS) * Math.cos(Math.toRadians(135)));
        yb1 = (float) ((BRIDGE_LENGTH_G/2f + RADIUS) * Math.sin(Math.toRadians(135)));

        //НАХОДИМ ЦЕНТР ТРЕТИЙ КРУГА ОТНОСИТЕЛЬНО НАШЕГО ТЕЛА
        xb2 = (float) ((BRIDGE_LENGTH_G/2f + RADIUS) * Math.cos(Math.toRadians(CIRCLE_POINTS - BRIDGE_ANGLE)));
        yb2 = (float) ((BRIDGE_LENGTH_G/2f + RADIUS) * Math.sin(Math.toRadians(CIRCLE_POINTS - BRIDGE_ANGLE)));

        vertices.add(new Vector2(xb1 + (float) ((RADIUS) * Math.cos(Math.toRadians(CIRCLE_POINTS - BRIDGE_ANGLE + BRIDGE_WIDTH/2f))),
                yb1 + (float) ((RADIUS) * Math.sin(Math.toRadians(CIRCLE_POINTS - BRIDGE_ANGLE + BRIDGE_WIDTH/2f)))));


        vertices.add(new Vector2(xb2 + (float) ((RADIUS) * Math.cos(Math.toRadians(135 - BRIDGE_WIDTH/2f))),
                yb2 + (float) ((RADIUS) * Math.sin(Math.toRadians(135 - BRIDGE_WIDTH/2f)))));

        //Создаем форму
        chain = new ChainShape();
        //Добавляем точки телу
        chain.createChain(vertices.toArray(new Vector2[0]));

        //Очищаем список точек
        vertices.clear();

        //Присваиваем фикстуре форму
        fd.shape = chain;

        //Присваиваем фикстуру телу
        arena.createFixture(fd).setUserData("map");
        //ОБЯЗАТЕЛЬНО УНИЧТОЖАЕМ ФОРМУ ПОСЛЕ ПРИСВАИВАНИЯ
        chain.dispose();

        vertices.add(new Vector2(xb1 + (float) ((RADIUS) * Math.cos(Math.toRadians(CIRCLE_POINTS - BRIDGE_ANGLE - BRIDGE_WIDTH/2f))),
                yb1 + (float) ((RADIUS) * Math.sin(Math.toRadians(CIRCLE_POINTS - BRIDGE_ANGLE - BRIDGE_WIDTH/2f)))));

        vertices.add(new Vector2(xb2 + (float) ((RADIUS) * Math.cos(Math.toRadians(135 + BRIDGE_WIDTH/2f))),
                yb2 + (float) ((RADIUS) * Math.sin(Math.toRadians(135 + BRIDGE_WIDTH/2f)))));


        //Создаем форму
        chain = new ChainShape();
        //Добавляем точки телу
        chain.createChain(vertices.toArray(new Vector2[0]));

        //Очищаем список точек
        vertices.clear();

        //Присваиваем фикстуре форму
        fd.shape = chain;

        //Присваиваем фикстуру телу
        arena.createFixture(fd).setUserData("map");
        //ОБЯЗАТЕЛЬНО УНИЧТОЖАЕМ ФОРМУ ПОСЛЕ ПРИСВАИВАНИЯ
        chain.dispose();
//================================ПЯТЫЙ ЛЕВЫЙ ГОРИЗОНТАЛЬНЫЙ========================================

        //Устанавливаем позицию арены в мире
        arenaDef.position.set(arenas.get(0).getPosition().x + (float) ((RADIUS + BRIDGE_LENGTH_G/2f) * Math.cos(Math.toRadians(180))),
                arenas.get(0).getPosition().y + (float) ((RADIUS + BRIDGE_LENGTH_G/2f) * Math.sin(Math.toRadians(180))));

        //Создаем тело в мире
        arena = world.createBody(arenaDef);

        //НАХОДИМ ЦЕНТР ПЕРВОГО КРУГА ОТНОСИТЕЛЬНО НАШЕГО ТЕЛА
        xb1 = (float) ((BRIDGE_LENGTH_G/2f + RADIUS) * Math.cos(Math.toRadians(0)));
        yb1 = (float) ((BRIDGE_LENGTH_G/2f + RADIUS) * Math.sin(Math.toRadians(0)));

        //НАХОДИМ ЦЕНТР ПЯТОГО КРУГА ОТНОСИТЕЛЬНО НАШЕГО ТЕЛА
        xb2 = (float) ((BRIDGE_LENGTH_G/2f + RADIUS) * Math.cos(Math.toRadians(180)));
        yb2 = (float) ((BRIDGE_LENGTH_G/2f + RADIUS) * Math.sin(Math.toRadians(180)));

        vertices.add(new Vector2(xb1 + (float) ((RADIUS) * Math.cos(Math.toRadians(180 - BRIDGE_WIDTH/2f))),
                yb1 + (float) ((RADIUS) * Math.sin(Math.toRadians(180 - BRIDGE_WIDTH/2f)))));


        vertices.add(new Vector2(xb2 + (float) ((RADIUS) * Math.cos(Math.toRadians(0 + BRIDGE_WIDTH/2f))),
                yb2 + (float) ((RADIUS) * Math.sin(Math.toRadians(0 + BRIDGE_WIDTH/2f)))));

        //Создаем форму
        chain = new ChainShape();
        //Добавляем точки телу
        chain.createChain(vertices.toArray(new Vector2[0]));

        //Очищаем список точек
        vertices.clear();

        //Присваиваем фикстуре форму
        fd.shape = chain;

        //Присваиваем фикстуру телу
        arena.createFixture(fd).setUserData("map");
        //ОБЯЗАТЕЛЬНО УНИЧТОЖАЕМ ФОРМУ ПОСЛЕ ПРИСВАИВАНИЯ
        chain.dispose();

        vertices.add(new Vector2(xb1 + (float) ((RADIUS) * Math.cos(Math.toRadians(180 + BRIDGE_WIDTH/2f))),
                yb1 + (float) ((RADIUS) * Math.sin(Math.toRadians(180 + BRIDGE_WIDTH/2f)))));


        vertices.add(new Vector2(xb2 + (float) ((RADIUS) * Math.cos(Math.toRadians(CIRCLE_POINTS - BRIDGE_WIDTH/2f))),
                yb2 + (float) ((RADIUS) * Math.sin(Math.toRadians(CIRCLE_POINTS - BRIDGE_WIDTH/2f)))));

        //Создаем форму
        chain = new ChainShape();
        //Добавляем точки телу
        chain.createChain(vertices.toArray(new Vector2[0]));

        //Очищаем список точек
        vertices.clear();

        //Присваиваем фикстуре форму
        fd.shape = chain;

        //Присваиваем фикстуру телу
        arena.createFixture(fd).setUserData("map");
        //ОБЯЗАТЕЛЬНО УНИЧТОЖАЕМ ФОРМУ ПОСЛЕ ПРИСВАИВАНИЯ
        chain.dispose();

//================================ШЕСТОЙ ПРАВЫЙ ГОРИЗОНТАЛЬНЫЙ======================================

        //Устанавливаем позицию арены в мире
        arenaDef.position.set(arenas.get(1).getPosition().x + (float) ((RADIUS + BRIDGE_LENGTH_G/2f) * Math.cos(Math.toRadians(180))),
                arenas.get(1).getPosition().y + (float) ((RADIUS + BRIDGE_LENGTH_G/2f) * Math.sin(Math.toRadians(180))));

        //Создаем тело в мире
        arena = world.createBody(arenaDef);

        //НАХОДИМ ЦЕНТР ВТОРОГО КРУГА ОТНОСИТЕЛЬНО НАШЕГО ТЕЛА
        xb1 = (float) ((BRIDGE_LENGTH_G/2f + RADIUS) * Math.cos(Math.toRadians(0)));
        yb1 = (float) ((BRIDGE_LENGTH_G/2f + RADIUS) * Math.sin(Math.toRadians(0)));

        //НАХОДИМ ЦЕНТР ПЕРВОГО КРУГА ОТНОСИТЕЛЬНО НАШЕГО ТЕЛА
        xb2 = (float) ((BRIDGE_LENGTH_G/2f + RADIUS) * Math.cos(Math.toRadians(180)));
        yb2 = (float) ((BRIDGE_LENGTH_G/2f + RADIUS) * Math.sin(Math.toRadians(180)));

        vertices.add(new Vector2(xb1 + (float) ((RADIUS) * Math.cos(Math.toRadians(180 - BRIDGE_WIDTH/2f))),
                yb1 + (float) ((RADIUS) * Math.sin(Math.toRadians(180 - BRIDGE_WIDTH/2f)))));


        vertices.add(new Vector2(xb2 + (float) ((RADIUS) * Math.cos(Math.toRadians(0 + BRIDGE_WIDTH/2f))),
                yb2 + (float) ((RADIUS) * Math.sin(Math.toRadians(0 + BRIDGE_WIDTH/2f)))));


        //Создаем форму
        chain = new ChainShape();
        //Добавляем точки телу
        chain.createChain(vertices.toArray(new Vector2[0]));

        //Очищаем список точек
        vertices.clear();

        //Присваиваем фикстуре форму
        fd.shape = chain;

        //Присваиваем фикстуру телу
        arena.createFixture(fd).setUserData("map");
        //ОБЯЗАТЕЛЬНО УНИЧТОЖАЕМ ФОРМУ ПОСЛЕ ПРИСВАИВАНИЯ
        chain.dispose();

        vertices.add(new Vector2(xb1 + (float) ((RADIUS) * Math.cos(Math.toRadians(180 + BRIDGE_WIDTH/2f))),
                yb1 + (float) ((RADIUS) * Math.sin(Math.toRadians(180 + BRIDGE_WIDTH/2f)))));


        vertices.add(new Vector2(xb2 + (float) ((RADIUS) * Math.cos(Math.toRadians(CIRCLE_POINTS - BRIDGE_WIDTH/2f))),
                yb2 + (float) ((RADIUS) * Math.sin(Math.toRadians(CIRCLE_POINTS - BRIDGE_WIDTH/2f)))));


        //Создаем форму
        chain = new ChainShape();
        //Добавляем точки телу
        chain.createChain(vertices.toArray(new Vector2[0]));

        //Очищаем список точек
        vertices.clear();

        //Присваиваем фикстуре форму
        fd.shape = chain;

        //Присваиваем фикстуру телу
        arena.createFixture(fd).setUserData("map");
        //ОБЯЗАТЕЛЬНО УНИЧТОЖАЕМ ФОРМУ ПОСЛЕ ПРИСВАИВАНИЯ
        chain.dispose();
//==================================================================================================

        vertices.toArray(new Vector2[0]);
    }

    public Vector2[] createVerticesCircle(float radius) {
        final int STEP = 2;
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
