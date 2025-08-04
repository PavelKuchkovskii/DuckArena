package org.dayaway.duckarena.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import org.dayaway.duckarena.model.api.IActor;

public class Crystal implements IActor {

    private final float WIDTH = 3;
    private final float HEIGHT = 3;

    private final Body body;
    private final TextureRegion textureRegion;

    private final long experience;
    private boolean danger = false;

    public Crystal(Body body, TextureRegion textureRegion, long experience) {
        this.body = body;
        this.textureRegion = textureRegion;
        this.experience = experience;
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

    public void setDanger(boolean danger) {
        this.danger = danger;
    }

    public boolean isDanger() {
        return danger;
    }

    public long getExperience() {
        return experience;
    }
}
