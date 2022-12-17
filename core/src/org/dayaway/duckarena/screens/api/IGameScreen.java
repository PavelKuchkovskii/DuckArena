package org.dayaway.duckarena.screens.api;

import com.badlogic.gdx.Screen;

import org.dayaway.duckarena.view.api.IRenderer;

public interface IGameScreen extends Screen {

    IRenderer getRender();

    void handleInput();

    void update(float dt);

}
