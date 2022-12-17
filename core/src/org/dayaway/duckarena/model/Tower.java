package org.dayaway.duckarena.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import org.dayaway.duckarena.model.api.ITower;

public class Tower implements ITower {

    private final Body body;
    private final TextureRegion texturePeace;
    private TextureRegion frame;

    private final int WIDTH = 40;
    private final int HEIGHT = 40;

    public Tower(Body body, TextureRegion texturePeace) {
        this.body = body;
        this.texturePeace = texturePeace;
    }

    @Override
    public Body getBody() {
        return this.body;
    }

    @Override
    public TextureRegion getTexture() {
        return this.texturePeace;
    }

    @Override
    public TextureRegion getFrame() {
        return this.frame;
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
