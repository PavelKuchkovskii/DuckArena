package org.dayaway.duckarena.model.api;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import org.dayaway.duckarena.model.Bot;
import org.dayaway.duckarena.model.Crystal;
import org.dayaway.duckarena.model.Tower;
import org.dayaway.duckarena.model.TrapEdgeMap;

import java.util.List;

public interface IWorld {

    void createWorld();

    World getWorld();

    IPlayer getPlayer();

    List<IActor> getActors();

    IActor getArena();

    List<Tower> getTowers();

    List<Body> getDestroy();

    List<Crystal> getCrystals();

    void addToDestroy(Body body);

    void destroy();

    List<Bot> getBots();

    void createSoldier(IPlayer player);

    List<ISoldier> getSoldiers();

    boolean isExist(Body body);

    List<TrapEdgeMap> getTraps();
}
