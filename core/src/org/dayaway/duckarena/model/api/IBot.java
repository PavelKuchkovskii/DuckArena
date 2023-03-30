package org.dayaway.duckarena.model.api;

import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.math.Vector2;

public interface IBot extends IPlayer{

    IActor getGoal();
    void setGoal(IActor actor);

    boolean isEscape();

    void setEscape(boolean escape);

    boolean isDiscovered();

    void setDiscovered(boolean discovered);

    SteeringBehavior<Vector2> getSteeringBehavior();
}
