package org.dayaway.duckarena.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import org.dayaway.duckarena.model.api.IPlayer;
import org.dayaway.duckarena.model.api.ISoldier;

public class Soldier implements ISoldier {

    private final IPlayer player;

    private final int WIDTH = 6;
    private final int HEIGHT = 6;

    private final Body body;
    private final TextureRegion peaceTexture;

    private TextureRegion frame;

    public Soldier(Body body, IPlayer player, TextureRegion peaceTexture) {
        this.body = body;
        this.player = player;
        this.peaceTexture = peaceTexture;
    }

    @Override
    public Body getBody() {
        return this.body;
    }

    @Override
    public TextureRegion getTexture() {
        return this.peaceTexture;
    }

    @Override
    public TextureRegion getFrame() {
        return this.frame;
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
