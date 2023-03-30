package org.dayaway.duckarena.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

import org.dayaway.duckarena.model.api.ILevel;
import org.dayaway.duckarena.model.api.IPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Player implements IPlayer {

    private final String nickName;
    private float radius;
    private final Fixture fRadius;

    private final TextureRegion textureRegion;

    private final Vector2 velocity;

    private float SPEED = 50;

    private double experience;
    private Stack<ILevel> levels;

    private final float WIDTH = 15;
    private final float HEIGHT = 15;

    private Body body;

    private final List<Soldier> soldiers;

    public Player(String nickName, Fixture fRadius, Body body, TextureRegion textureRegion) {
        this.nickName = nickName;
        this.fRadius = fRadius;
        this.textureRegion = textureRegion;
        this.velocity = new Vector2(0,0);
        this.body = body;
        this.soldiers = new ArrayList<>();
        initLevels();
        this.radius = 4f;
    }

    @Override
    public Body getBody() {
        return this.body;
    }

    @Override
    public float getWidth() {
        return this.WIDTH;
    }

    @Override
    public float getHeight() {
        return this.HEIGHT;
    }

    @Override
    public Vector2 getPosition() {
        return this.body.getPosition();
    }

    @Override
    public String getNickName() {
        return this.nickName;
    }

    @Override
    public float getSpeed() {
        return this.SPEED;
    }

    @Override
    public List<Soldier> getSoldiers() {
        return this.soldiers;
    }

    @Override
    public void addSoldier(Soldier soldier) {
        this.soldiers.add(soldier);
    }

    @Override
    public void setVelocity(Vector2 v) {
        this.velocity.set(v);
    }

    @Override
    public Vector2 getVelocity() {
        return this.velocity;
    }

    @Override
    public void exp(Body body) {
        Crystal crystal = (Crystal) body.getUserData();
        experience += crystal.getExperience();
    }

    @Override
    public double getExp() {
        return this.experience;
    }

    @Override
    public ILevel getLevel() {
        if(levels.empty()) {
            levels.add(new Level((double) (experience * 1.01)));
        }
        return levels.peek();
    }

    @Override
    public void nextLevel() {
        levels.pop();
    }

    @Override
    public float getRadius() {
        return this.radius;
    }

    @Override
    public Fixture getRadiusFixture() {
        return this.fRadius;
    }

    @Override
    public void setMassRadius(float radius) {
        this.radius = radius;
    }

    public void initLevels() {
        this.levels = new Stack<>();
        levels.add(new Level(10));
    }


    @Override
    public TextureRegion getTexture() {
        return this.textureRegion;
    }

    @Override
    public TextureRegion getFrame() {
        return null;
    }

    @Override
    public void setFrame(TextureRegion texture) {

    }
}
