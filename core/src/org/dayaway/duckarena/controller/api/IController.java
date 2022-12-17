package org.dayaway.duckarena.controller.api;

import org.dayaway.duckarena.controller.utils.JoyStick;
import org.dayaway.duckarena.model.api.IWorld;

public interface IController {

    IWorld getWorld();

    void handleInput();

    void update(float dt);

    JoyStick getJoyStick();
}
