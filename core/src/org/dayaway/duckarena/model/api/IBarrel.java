package org.dayaway.duckarena.model.api;

public interface IBarrel extends IActor{

    void activate();
    boolean isActive();
    void explode();
    boolean isExplosion();
}
