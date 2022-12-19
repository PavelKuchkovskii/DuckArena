package org.dayaway.duckarena.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;

import org.dayaway.duckarena.model.api.ITrap;

public class TrapEdgeMap implements ITrap {

    private final RevoluteJoint joint;
    private final TextureRegion textureRegion;

    private final int WIDTH = 40;
    private final int HEIGHT = 60;

    public TrapEdgeMap(RevoluteJoint joint, TextureRegion region) {
        this.joint = joint;
        this.textureRegion = region;
    }

    @Override
    public Body getBody() {
        return joint.getBodyB();
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
        return this.joint;
    }
}
