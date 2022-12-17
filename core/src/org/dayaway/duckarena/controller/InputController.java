package org.dayaway.duckarena.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

import org.dayaway.duckarena.controller.api.IController;
import org.dayaway.duckarena.controller.utils.JoyStick;
import org.dayaway.duckarena.model.api.IPlayer;

public class InputController implements InputProcessor {

    private final IController controller;
    private final IPlayer player;
    private final JoyStick joyStick;

    public InputController(IController controller) {
        this.controller = controller;
        this.player = controller.getWorld().getPlayer();
        this.joyStick = controller.getJoyStick();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        //Как только касаются экрана, устанавливаю в эту точку центральный и внешний круг джойстика
        joyStick.setPositionExternal(Gdx.input.getX(), Gdx.input.getY());
        joyStick.setPositionInternal(Gdx.input.getX(), Gdx.input.getY());
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        //Как только отпускают экран, устанавиваем JoyStick в первоночальное положение
        joyStick.reset();
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        //Если точку на экране двигают, меняю положение центального круга
        joyStick.setPositionInternal(Gdx.input.getX(), Gdx.input.getY());
        //Устанавливаю направление и скорость движения центра масс
        player.setVelocity(joyStick.getVelocity(player.getSpeed()));
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
