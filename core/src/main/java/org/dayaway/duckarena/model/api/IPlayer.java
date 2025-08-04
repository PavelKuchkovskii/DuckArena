package org.dayaway.duckarena.model.api;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

import org.dayaway.duckarena.model.Soldier;

import java.util.List;

public interface IPlayer extends IActor{

    String getNickName();

    float getSpeed();

    List<Soldier> getSoldiers();

    void addSoldier(Soldier soldier);

    void setVelocity(Vector2 v);

    Vector2 getVelocity();

    void exp(Body body);

    double getExp();

    ILevel getLevel();

    void nextLevel();

    float getRadius();

    Fixture getRadiusFixture();

    void setMassRadius(float radius);
}
