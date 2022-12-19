package org.dayaway.duckarena.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;

public class TrapCrossMap extends TrapEdgeMap {

    private final int WIDTH = 75;
    private final int HEIGHT = 75;

    public TrapCrossMap(RevoluteJoint joint, TextureRegion region) {
        super(joint, region);
    }

    @Override
    public Body getBody() {
        return super.getBody();
    }

    @Override
    public TextureRegion getTexture() {
        return super.getTexture();
    }

    @Override
    public TextureRegion getFrame() {
        return super.getFrame();
    }

    @Override
    public void setFrame(TextureRegion frame) {
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
        return getBody().getPosition();
    }

    @Override
    public RevoluteJoint getJoint() {
        return super.getJoint();
    }
}
