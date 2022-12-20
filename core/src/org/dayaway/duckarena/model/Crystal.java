package org.dayaway.duckarena.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import org.dayaway.duckarena.model.api.IActor;

public class Crystal implements IActor {

    private final int WIDTH = 3;
    private final int HEIGHT = 3;

    private final Body body;
    private final Vector2 position;
    private final TextureRegion textureRegion;

    private final long experience;

    public Crystal(Body body, TextureRegion textureRegion, long experience) {
        this.body = body;
        this.position = new Vector2(body.getPosition());
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
    public int getWidth() {
        return this.WIDTH;
    }

    @Override
    public int getHeight() {
        return this.HEIGHT;
    }

    @Override
    public Vector2 getPosition() {
        return new Vector2(this.position);
    }

    public long getExperience() {
        return experience;
    }
}
