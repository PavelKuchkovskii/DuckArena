package org.dayaway.duckarena.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import org.dayaway.duckarena.model.api.IBarrel;
import org.dayaway.duckarena.model.api.ISoldier;

import java.util.HashSet;
import java.util.Set;


public class Barrel implements IBarrel {

    private final int WIDTH = 10;
    private final int HEIGHT = 10;

    private final Body body;
    private final TextureRegion textureRegion;

    private boolean active;
    private boolean explosion;

    private Set<ISoldier> soldiers;

    public Barrel(Body body, TextureRegion textureRegion) {
        this.body = body;
        this.textureRegion = textureRegion;
        this.soldiers = new HashSet<>();
    }

    @Override
    public Body getBody() {
        return this.body;
    }

    @Override
    public TextureRegion getTexture() {
        return this.textureRegion;
    }

    @Override
    public TextureRegion getFrame() {
        return this.textureRegion;
    }

    @Override
    public void setFrame(TextureRegion texture) {

    }
    @Override
    public int getWidth() {
        return this.WIDTH;
    }

    @Override
    public int getHeight() {
        return this.HEIGHT;
    }

    @Override
    public Vector2 getPosition() {
        return this.body.getPosition();
    }

    @Override
    public void activate() {
        this.active = true;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public void explode() {
        this.explosion = true;
    }

    @Override
    public boolean isExplosion() {
        return this.explosion;
    }

    @Override
    public Set<ISoldier> getSoldiers() {
        return this.soldiers;
    }

    @Override
    public void addSoldier(ISoldier soldier) {
        this.soldiers.add(soldier);
    }

    @Override
    public void removeSoldier(ISoldier soldier) {
        this.soldiers.remove(soldier);
    }
}
