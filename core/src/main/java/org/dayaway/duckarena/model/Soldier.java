package org.dayaway.duckarena.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import org.dayaway.duckarena.model.api.IPlayer;
import org.dayaway.duckarena.model.api.ISoldier;

public class Soldier implements ISoldier {

    private final IPlayer player;

    private final float WIDTH = 7;
    private final float HEIGHT = 7;

    private final Body body;
    private final TextureRegion texture;

    private TextureRegion frame;

    public Soldier(Body body, IPlayer player, TextureRegion texture) {
        this.body = body;
        this.player = player;
        this.texture = texture;
    }

    @Override
    public Body getBody() {
        return this.body;
    }

    @Override
    public TextureRegion getTexture() {
        return this.texture;
    }

    @Override
    public TextureRegion getFrame() {
        return this.frame;
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
        return this.getBody().getPosition();
    }

    @Override
    public void setFrame(TextureRegion texture) {
        this.frame = texture;
    }

    @Override
    public IPlayer getPlayer() {
        return this.player;
    }
}
