package org.dayaway.duckarena.controller;

import com.badlogic.gdx.math.Vector2;

import org.dayaway.duckarena.controller.api.IController;
import org.dayaway.duckarena.model.Bot;
import org.dayaway.duckarena.model.CircleKiller;
import org.dayaway.duckarena.model.Crystal;
import org.dayaway.duckarena.model.api.IActor;
import org.dayaway.duckarena.model.api.IPlayer;
import org.dayaway.duckarena.model.api.ITrapRevolute;
import org.dayaway.duckarena.model.api.IWorld;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BotsController {

    private final IWorld world;
    private final List<Bot> bots;
    private final IPlayer player;
    private final Random random;

    public BotsController(IController controller) {
        this.world = controller.getWorld();
        this.bots = world.getBots();
        this.player = world.getPlayer();
        this.random = new Random();
    }

    public void update(float dt) {
        decide();

        move();

        expBot();

        updateChanges();
    }

    //Проверяем набрал ли Bot достаточно опыта и если да, переводим на след уровень
    public void expBot() {

        for (Bot bot : bots) {

            if(bot.getExp() >= bot.getLevel().getExp()) {
                bot.nextLevel();

                world.createSoldier(bot);
            }
        }

    }

    private void move() {

        Vector2 v1;
        Vector2 v2;

        for (Bot bot : bots) {

            if(bot.getGoal() != null) {
                v1 = cohesion(bot);
                v2 = rule2(bot);

                float x = bot.getBody().getLinearVelocity().x + v1.x + v2.x;
                float y = bot.getBody().getLinearVelocity().y + v1.y + v2.y;

                bot.getBody().setLinearVelocity(correctionVelocity(x,y));
            }
            else {
                bot.getBody().setLinearVelocity(0,0);
            }
        }

    }

    private Vector2 cohesion(Bot bot) {
        return bot.getGoal().getPosition().sub(bot.getPosition());
    }

    //Корректируем скорость центра масс ботов, делаем их равными скорости плеера
    private Vector2 correctionVelocity(float x, float y) {
        float angle = 0;

        if(x >= 0) {
            if(y >= 0) {
                //Если будут равны нулю, то получим Nan
                //Поэтому такие ситуации просто пропускаем и угол остается таким какой он был
                if(y != 0 && x != 0) {
                    angle = (float) Math.toDegrees(Math.atan(y/x));
                }
            }
            else {
                angle = 360f - (float) Math.toDegrees(Math.atan(Math.abs(y)/x));
            }

        }
        else {
            if(y >= 0) {
                angle = 180f - (float) Math.toDegrees(Math.atan(y/Math.abs(x)));
            }
            else {
                angle = 180f + (float) Math.toDegrees(Math.atan(Math.abs(y)/Math.abs(x)));
            }

        }

        float x1 = (float) (world.getPlayer().getSpeed() * Math.cos(Math.toRadians(angle)));
        float y1 = (float) (world.getPlayer().getSpeed() * Math.sin(Math.toRadians(angle)));

        return new Vector2(x1, y1);

    }

    //Меняем направление движения в зависимости от наличия опасных мест
    private Vector2 rule2(Bot bot) {
        Vector2 c = new Vector2(0,0);

        for (ITrapRevolute trap : world.getTraps()) {
            if(getVector(bot.getPosition(), trap.getPosition()) < 60) {

                /*if(bot.getPosition().x < trap.getPosition().x) {
                    c.x -= 2000;
                }
                else {
                    c.x += 2000;
                }

                if(bot.getPosition().y < trap.getPosition().y) {
                    c.y -= 2000;
                }
                else {
                    c.y += 2000;
                }*/

                c.x = bot.getPosition().x - trap.getPosition().x;
                c.y = bot.getPosition().y - trap.getPosition().y;
            }

        }
        for (CircleKiller trap : world.getCircleTraps()) {
            if(getVector(bot.getPosition(), trap.getPosition()) < 50) {

                /*if(bot.getPosition().x < trap.getPosition().x) {
                    c.x -= 2000;
                }
                else {
                    c.x += 2000;
                }

                if(bot.getPosition().y < trap.getPosition().y) {
                    c.y -= 2000;
                }
                else {
                    c.y += 2000;
                }*/

                c.x = bot.getPosition().x - trap.getPosition().x;
                c.y = bot.getPosition().y - trap.getPosition().y;
            }
        }
        return c;
    }

    private void decide() {

        for (Bot bot : bots) {

            if(bot.getGoal() == null) {
                List<VectorGoal> goals = new ArrayList<>();

                if(!world.getCrystals().isEmpty()) {
                    for (Crystal crystal : world.getCrystals()) {
                        //Если кристалл безопасный отслеживаем расстояние до него
                        if(!crystal.isDanger()) {
                            goals.add(new VectorGoal(getVector(bot.getPosition(), crystal.getPosition()), crystal));
                        }

                    }

                    Collections.sort(goals);

                    if(!goals.isEmpty()) {
                        bot.setGoal(goals.get(0).getGoal());
                        bot.setTimeStartGoal(System.currentTimeMillis());
                    }
                }
            }
            else {
                //Если в мире нет Body цели, то цель равна null
                if(bot.getGoal().getBody().getUserData() == null
                        || !world.isExist(bot.getGoal().getBody())
                        || ((Crystal) bot.getGoal().getBody().getUserData()).isDanger()) {
                    bot.setGoal(null);
                }
                else if((System.currentTimeMillis() - bot.getTimeStartGoal()) > 1000) {
                    ((Crystal) bot.getGoal()).setDanger(true);
                    bot.setGoal(null);
                }
            }


        }
    }

    private void updateChanges() {

        for (Bot bot : bots) {
            //Если у бота закончились солдаты, телепортируем центр масс в другое место и добавляем ему новых солдат
            if(bot.getSoldiers().size() == 0) {
                //Если у Игрока еще есть солдаты
                if(!player.getSoldiers().isEmpty()) {
                    //на месте гибели бота добавляем кристаллы
                    for (int i = 0; i < 50; i++) {
                        world.createCrystal(bot.getBody().getPosition().x, bot.getBody().getPosition().y, 33);
                    }
                }

                bot.getBody().setTransform(new Vector2(random.nextInt(600)-300, random.nextInt(600)-300),0);
                bot.setGoal(null);

                for (int i = 0; i < player.getSoldiers().size(); i++) {
                    world.createSoldier(bot);
                }
            }
        }
    }

    //Получаем расстояние от обкъета1 до объекта2
    //v1 - bot
    //v2 - goal
    float getVector(Vector2 vector1, Vector2 vector2) {
        float leg1;
        float leg2;

        if(vector1.x > vector2.x) {
            leg1 = Math.abs(vector1.x - vector2.x);
        }
        else {
            leg1 = Math.abs(vector2.x - vector1.x);
        }

        if(vector1.y > vector2.y) {
            leg2 = Math.abs(vector1.y - vector2.y);
        }
        else {
            leg2 = Math.abs(vector2.y - vector1.y);
        }

        return (float) Math.sqrt((leg1 * leg1) + (leg2 * leg2));
    }

    private class VectorGoal implements Comparable<VectorGoal>{
        private final float vector;
        private final IActor goal;

        public VectorGoal(float vector, IActor goal) {
            this.vector = vector;
            this.goal = goal;
        }

        public float getVector() {
            return vector;
        }

        public IActor getGoal() {
            return goal;
        }

        @Override
        public int compareTo(VectorGoal o) {

            if(this.vector - o.getVector() < 0) {
                return -1;
            }

            else if(this.vector - o.getVector() > 0) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }
}
