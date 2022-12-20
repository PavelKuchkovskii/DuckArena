package org.dayaway.duckarena.controller;

import org.dayaway.duckarena.controller.utils.TowersAnimation;
import org.dayaway.duckarena.model.Tower;
import org.dayaway.duckarena.model.api.IPlayer;
import org.dayaway.duckarena.model.api.IWorld;

import java.util.List;

public class TowersController {

    private final IWorld world;
    private final IPlayer player;
    private final List<Tower> towers;
    private final TowersAnimation animation;

    public TowersController(IWorld world) {
        this.world = world;
        this.player = world.getPlayer();
        this.towers = world.getTowers();

        this.animation = new TowersAnimation(7, 0.5f);
    }

    public void update(float dt) {
        animate();

        updateChanges();

        animation.update(dt);
    }

    public void animate() {
        for (Tower tower : towers) {
            tower.setFrame(animation.getAnimation(tower.getTexture()));
        }
    }

    //Проверяем здоровье и уничтожаем если у башни закончилось здоровье
    private void updateChanges() {

    }
}
