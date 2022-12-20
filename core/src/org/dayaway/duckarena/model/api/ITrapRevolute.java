package org.dayaway.duckarena.model.api;

import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;

public interface ITrapRevolute extends IActor {

    RevoluteJoint getJoint();
}
