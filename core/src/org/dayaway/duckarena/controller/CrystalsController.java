package org.dayaway.duckarena.controller;

import com.badlogic.gdx.math.Vector2;

import org.dayaway.duckarena.model.Crystal;
import org.dayaway.duckarena.model.api.IPlayer;
import org.dayaway.duckarena.model.api.ITrapRevolute;
import org.dayaway.duckarena.model.api.IWorld;

import java.util.List;

public class CrystalsController {

    private final IWorld world;
    private final IPlayer player;
    private final List<Crystal> crystals;

    public CrystalsController(IWorld world) {
        this.world = world;
        this.player = world.getPlayer();
        this.crystals = world.getCrystals();
    }

    public void update(float dt) {
        create();

        delete();
    }

    public void create() {
        if(crystals.size() < 100) {
            world.createCrystal();
        }
    }

    private void delete() {
        for (Crystal crystal : crystals) {
            if(getVector(crystal.getPosition(), player.getPosition()) > 250f) {
                world.addToDestroy(crystal.getBody());
            }
            if(getVector(crystal.getPosition(), new Vector2(0,0)) > 450f) {
                world.addToDestroy(crystal.getBody());
            }
        }
    }

    //v1 - crystal
    //v2 - player
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
}
