package org.dayaway.duckarena.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.physics.box2d.ContactListener;

import org.dayaway.duckarena.controller.api.IController;
import org.dayaway.duckarena.controller.utils.JoyStick;
import org.dayaway.duckarena.model.api.IWorld;

public class BattleController implements IController {

    private final IWorld world;
    private final InputProcessor inputProcessor;
    private final ContactListener listener;

    private final PlayerController playerController;
    private final SoldiersController soldiersController;
    private final TowersController towersController;

    private final BotsController botsController;


    private final BangsController bangsController;

    private final JoyStick joyStick;

    public BattleController(IWorld world) {
        this.world = world;
        this.joyStick = new JoyStick(null, null);

        this.inputProcessor = new InputController(this);
        Gdx.input.setInputProcessor(inputProcessor);

        this.listener = new CustomListener(world, this);
        this.world.getWorld().setContactListener(listener);

        this.playerController = new PlayerController(world);
        this.soldiersController = new SoldiersController(world);
        this.towersController = new TowersController(world);

        this.botsController = new BotsController(this);

        this.bangsController = new BangsController(world);
    }

    @Override
    public IWorld getWorld() {
        return this.world;
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {

        playerController.update(dt);

        soldiersController.update(dt);

        towersController.update(dt);

        botsController.update(dt);

        bangsController.update(dt);

        destroy();

        world.getWorld().step(dt, 6, 2);
    }

    @Override
    public JoyStick getJoyStick() {
        return this.joyStick;
    }


    //Уничтожаем объекты добавленные в список на уничтожение
    public void destroy() {
        world.destroy();
    }
}
