package org.dayaway.duckarena.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import org.dayaway.duckarena.model.api.IActor;

public class CircleKiller implements IActor {


    private final Body body;
    private final TextureRegion textureRegion;
    private TextureRegion frame;

    private final int WIDTH = 40;
    private final int HEIGHT = 40;

    public CircleKiller(Body body, TextureRegion textureRegion) {
        this.body = body;
        this.textureRegion = textureRegion;
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
        this.frame = texture;
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
}
