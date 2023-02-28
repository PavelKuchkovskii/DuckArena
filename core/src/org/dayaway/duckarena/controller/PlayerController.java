package org.dayaway.duckarena.controller;

import org.dayaway.duckarena.model.api.IPlayer;
import org.dayaway.duckarena.model.api.IWorld;

public class PlayerController {

    private final IWorld world;
    private final IPlayer player;

    public PlayerController(IWorld world) {
        this.world = world;
        this.player = world.getPlayer();
    }

    public void update(float dt) {
        movePlayer();
        expPlayer();
    }


    //Меняем напарвление центра масс
    public void movePlayer() {
        player.getBody().setLinearVelocity(player.getVelocity());
    }

    //Проверяем набрал ли Player достаточно опыта и если да, переводим на след уровень
    public void expPlayer() {
        if(player.getExp() >= player.getLevel().getExp()) {
            player.nextLevel();

            world.createSoldier(player);
        }

        changeMass();
    }

   //Увеличиваем/уменьшаем радиус сенсора
   private void changeMass() {
       float S = ((3.5f * 2) * (3.5f * 2)) * player.getSoldiers().size();
       float radius = (float) Math.sqrt(S/Math.PI);

       player.getRadiusFixture().getShape().setRadius(radius);
       player.setMassRadius(radius);
   }
}
