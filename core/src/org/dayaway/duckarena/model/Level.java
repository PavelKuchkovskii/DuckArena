package org.dayaway.duckarena.model;

import org.dayaway.duckarena.model.api.ILevel;

public class Level implements ILevel {

    private final long experience;

    public Level(long experience) {
        this.experience = experience;
    }

    @Override
    public long getExp() {
        return this.experience;
    }
}
