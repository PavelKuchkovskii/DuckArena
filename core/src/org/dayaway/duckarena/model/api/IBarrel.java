package org.dayaway.duckarena.model.api;


import java.util.Set;

public interface IBarrel extends IActor{

    void activate();
    boolean isActive();
    void explode();
    boolean isExplosion();

    Set<ISoldier> getSoldiers();
    void addSoldier(ISoldier soldier);
    void removeSoldier(ISoldier soldier);
}
