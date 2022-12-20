package org.dayaway.duckarena.model.api;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import org.dayaway.duckarena.model.Bot;
import org.dayaway.duckarena.model.Crystal;
import org.dayaway.duckarena.model.Tower;

import java.util.List;
import java.util.Set;

public interface IWorld {

    void createWorld();

    World getWorld();

    IPlayer getPlayer();

    List<IActor> getActors();

    IActor getArena();

    List<Tower> getTowers();

    Set<Body> getDestroy();

    List<Crystal> getCrystals();

    void addToDestroy(Body body);

    void destroy();

    List<Bot> getBots();

    Bot createBot();

    void createSoldier(IPlayer player);

    void createCrystal();

    void createCrystal(float posX, float posY);

    List<ISoldier> getSoldiers();

    boolean isExist(Body body);

    List<ITrapRevolute> getTraps();

    List<IBang> getBangs();

    void addBang(IBang bang);
}
