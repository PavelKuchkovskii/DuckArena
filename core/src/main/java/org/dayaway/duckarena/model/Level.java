package org.dayaway.duckarena.model;

import org.dayaway.duckarena.model.api.ILevel;

public class Level implements ILevel {

    private final double experience;

    public Level(double experience) {
        this.experience = experience;
    }

    @Override
    public double getExp() {
        return this.experience;
    }
}
