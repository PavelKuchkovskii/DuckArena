package org.dayaway.duckarena.model.api;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import org.dayaway.duckarena.model.Soldier;

import java.util.List;

public interface IPlayer extends IActor{

    float getSpeed();

    List<Soldier> getSoldiers();

    void addSoldier(Soldier soldier);

    void setVelocity(Vector2 v);

    Vector2 getVelocity();

    void exp(Body body);

    double getExp();

    ILevel getLevel();

    void nextLevel();

}
