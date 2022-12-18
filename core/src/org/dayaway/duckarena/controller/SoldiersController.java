package org.dayaway.duckarena.controller;

import com.badlogic.gdx.math.Vector2;

import org.dayaway.duckarena.controller.utils.SoldierAnimation;
import org.dayaway.duckarena.model.api.ISoldier;
import org.dayaway.duckarena.model.api.IWorld;

import java.util.List;

public class SoldiersController{

    private final IWorld world;
    private final List<ISoldier> soldiers;
    private final SoldierAnimation animation;

    public SoldiersController(IWorld world) {
        this.world = world;
        this.soldiers = world.getSoldiers();

        this.animation = new SoldierAnimation(5, 0.5f);
    }

    public IWorld getWorld() {
        return this.world;
    }

    public void update(float dt) {
        move_soldiers();

        rotateSoldier();

        updateChanges();

        animation.update(dt);
    }

    private void move_soldiers() {

        Vector2 v1;

        for (ISoldier soldier : soldiers) {
            v1 = cohesion(soldier);

            //Сразу брали скорость каждого солдата + правила, но для большего контроля берем скорость центра масс
            float x = soldier.getPlayer().getBody().getLinearVelocity().x + v1.x;
            float y = soldier.getPlayer().getBody().getLinearVelocity().y + v1.y;

            soldier.getBody().setLinearVelocity(x,y);
        }

    }

    //Units try to stay as close to each other as possible
    private Vector2 cohesion(ISoldier soldier) {
        return soldier.getPlayer().getPosition().sub(soldier.getPosition());
    }

    //В зависимости от угла движения меняю текстуру соладата
    private void rotateSoldier() {
        //Получаем угол движения центра масс
        for (ISoldier soldier : soldiers) {
            float angle = getAngle(soldier.getPlayer().getVelocity());

            if ( (angle >= 0 && angle <= 90) || (angle > 270) ) {
                soldier.setFrame(animation.getRightAnimation(soldier.getPlayer().getSoldiers().get(0).getTexture()));
            }
            else {
                soldier.setFrame(animation.getLeftAnimation(soldier.getPlayer().getSoldiers().get(0).getTexture()));
            }

        }
    }

    private void updateChanges() {

    }

    //Получаем угол движения относительно себя
    private float getAngle(Vector2 v) {

        //Направление его движения
        float x = v.x;
        float y = v.y;

        float angle = 0;

        if(x >= 0) {
            if(y >= 0) {
                //Если будут равны нулю, то получим Nan
                //Поэтому такие ситуации просто пропускаем и угол остается таким какой он был
                if(y != 0 && x != 0) {
                    angle = (float) Math.toDegrees(Math.atan(y/x));
                }
            }
            else {
                angle = 360f - (float) Math.toDegrees(Math.atan(Math.abs(y)/x));
            }

        }
        else {
            if(y >= 0) {
                angle = 180f - (float) Math.toDegrees(Math.atan(y/Math.abs(x)));
            }
            else {
                angle = 180f + (float) Math.toDegrees(Math.atan(Math.abs(y)/Math.abs(x)));
            }

        }

        //Проверка на Nan, но проблема исправлена и больше не должна возникать такая ситуация
        if(Float.isNaN(angle)) {
            angle = 0;
        }

        return angle;
    }

}
