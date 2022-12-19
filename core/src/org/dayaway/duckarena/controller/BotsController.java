package org.dayaway.duckarena.controller;

import com.badlogic.gdx.math.Vector2;

import org.dayaway.duckarena.controller.api.IController;
import org.dayaway.duckarena.model.Bot;
import org.dayaway.duckarena.model.Crystal;
import org.dayaway.duckarena.model.api.IActor;
import org.dayaway.duckarena.model.api.IPlayer;
import org.dayaway.duckarena.model.api.IWorld;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BotsController {

    private final IWorld world;
    private final List<Bot> bots;
    private final IPlayer player;

    public BotsController(IController controller) {
        this.world = controller.getWorld();
        this.bots = world.getBots();
        this.player = world.getPlayer();
    }

    public void update(float dt) {
        decide();

        move();

        expBot();
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

        for (Bot bot : bots) {

            if(bot.getGoal() != null) {
                v1 = cohesion(bot);

                float x = v1.x;
                float y = v1.y;

                bot.getBody().setLinearVelocity(x, y);
            }
        }

    }

    private Vector2 cohesion(Bot bot) {
        return bot.getGoal().getPosition().sub(bot.getPosition());
    }

    private void decide() {

        for (Bot bot : bots) {

            if(bot.getGoal() == null) {
                List<VectorGoal> goals = new ArrayList<>();

                if(!world.getCrystals().isEmpty()) {
                    for (Crystal crystal : world.getCrystals()) {
                        goals.add(new VectorGoal(getVector(bot.getPosition(), crystal.getPosition()), crystal));
                    }

                    Collections.sort(goals);

                    bot.setGoal(goals.get(0).getGoal());
                }
            }
            else {
                //Если в мире нет Body цели, то цель равна null
                if(!world.isExist(bot.getGoal().getBody())) {
                    bot.setGoal(null);
                }
            }
        }
    }

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