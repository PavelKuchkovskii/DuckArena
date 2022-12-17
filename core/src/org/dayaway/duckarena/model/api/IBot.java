package org.dayaway.duckarena.model.api;

public interface IBot extends IPlayer{

    IActor getGoal();
    void setGoal(IActor actor);

    boolean isEscape();

    void setEscape(boolean escape);

    boolean isDiscovered();

    void setDiscovered(boolean discovered);
}
