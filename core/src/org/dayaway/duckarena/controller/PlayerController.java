package org.dayaway.duckarena.controller;

import com.badlogic.gdx.physics.box2d.Fixture;

import org.dayaway.duckarena.model.Soldier;
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

   private void changeMass() {
       //Увеличиваем радиус сенсора
       for (Fixture fixture : player.getBody().getFixtureList()) {
           if(fixture.getUserData().equals("player_mass")) {
               float S = 0;
               float radius;

               for (int i = 0; i < player.getSoldiers().size(); i++) {
                   S += (3.5f * 2) * (3.5f * 2);
               }

               radius = (float) Math.sqrt(S/Math.PI);
               fixture.getShape().setRadius(radius);
               player.setMassRadius(radius);
           }
       }
   }
}
