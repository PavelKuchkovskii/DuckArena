package org.dayaway.duckarena.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import org.dayaway.duckarena.controller.BattleController;
import org.dayaway.duckarena.controller.api.IController;
import org.dayaway.duckarena.model.BattleWorld;
import org.dayaway.duckarena.model.api.IWorld;
import org.dayaway.duckarena.screens.api.IGameScreen;
import org.dayaway.duckarena.view.BattleRenderer;
import org.dayaway.duckarena.view.api.IRenderer;

public class BattleScreen implements IGameScreen {

    private final IWorld world;
    private final IRenderer renderer;
    private final IController controller;
    public static final TextureRegion actorPeace = new TextureRegion(new Texture("walk.png"));
    public static final TextureRegion actorAttack = new TextureRegion(new Texture("attack.png"));
    public static final TextureRegion map = new TextureRegion(new Texture("WB.png"));
    public static final TextureRegion crystal = new TextureRegion(new Texture("red.png"));

    public BattleScreen() {
        this.world = new BattleWorld();
        this.renderer = new BattleRenderer(world);
        this.controller = new BattleController(world);
    }

    @Override
    public void handleInput() {
        controller.handleInput();
    }

    @Override
    public void update(float dt) {
        controller.update(dt);
    }

    @Override
    public void render(float delta) {
        renderer.render(delta);
        handleInput();//ALWAYS HERE
        update(delta);//ALWAYS HERE
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
        this.renderer.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    }

    @Override
    public IRenderer getRender() {
        return renderer;
    }

}
