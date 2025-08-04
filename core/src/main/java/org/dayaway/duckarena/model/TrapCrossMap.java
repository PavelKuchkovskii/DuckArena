package org.dayaway.duckarena.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;

import org.dayaway.duckarena.model.api.IObstacle;
import org.dayaway.duckarena.model.api.ITrapRevolute;

public class TrapCrossMap implements ITrapRevolute, IObstacle {

    private final RevoluteJoint joint;
    private TextureRegion textureRegion;

    private final float WIDTH = 75;
    private final float HEIGHT = 75;

    public TrapCrossMap(RevoluteJoint joint, TextureRegion region) {
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
    public float getWidth() {
        return this.WIDTH;
    }

    @Override
    public float getHeight() {
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
