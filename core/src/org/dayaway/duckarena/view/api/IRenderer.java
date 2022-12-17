package org.dayaway.duckarena.view.api;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.dayaway.duckarena.model.api.IWorld;

public interface IRenderer {

    SpriteBatch getBatch();

    IWorld getWorld();

    void render(float dt);

    void resize(int width, int height);

    void dispose();
}
