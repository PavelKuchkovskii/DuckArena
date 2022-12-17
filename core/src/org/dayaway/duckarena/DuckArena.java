package org.dayaway.duckarena;

import com.badlogic.gdx.Game;

import org.dayaway.duckarena.screens.BattleScreen;

public class DuckArena extends Game {

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
