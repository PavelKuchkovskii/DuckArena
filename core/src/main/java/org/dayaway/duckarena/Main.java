package org.dayaway.duckarena;

import com.badlogic.gdx.Game;

import org.dayaway.duckarena.screens.BattleScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    @Override
    public void create () {
        this.setScreen(new BattleScreen());
    }

    @Override
    public void render () {
        super.render();
    }

    @Override
    public void dispose () {
    }
}
