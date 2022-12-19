package org.dayaway.duckarena.model.api;

import com.badlogic.gdx.physics.box2d.Joint;

public interface ITrap extends IActor {

    Joint getJoint();
}
