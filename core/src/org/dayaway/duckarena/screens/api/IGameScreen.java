package org.dayaway.duckarena.screens.api;

import com.badlogic.gdx.Screen;

import org.dayaway.duckarena.screens.TexturesBattleScreen;
import org.dayaway.duckarena.view.Util.api.IRenderer;

public interface IGameScreen extends Screen {

    IRenderer getRender();

    void handleInput();

    void update(float dt);

    TexturesBattleScreen getTextures();

}
