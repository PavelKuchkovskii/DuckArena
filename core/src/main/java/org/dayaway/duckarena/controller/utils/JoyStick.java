package org.dayaway.duckarena.controller.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class JoyStick {

    private final Texture external;
    private final Texture internal;

    private final Vector2 positionExternal;
    private final Vector2 positionInternal;

    private float radiusExternal;
    private float radiusInternal;

    private float ANGLE;
    private boolean VISIBILITY;

    public JoyStick(Texture external, Texture internal) {
        this.external = external;
        this.internal = internal;
        this.positionExternal = new Vector2(Gdx.graphics.getWidth()/2f,Gdx.graphics.getHeight()/2f);
        this.positionInternal = new Vector2(Gdx.graphics.getWidth()/2f,Gdx.graphics.getHeight()/2f);
        this.radiusExternal = 10f;
        this.radiusInternal = 6f;
        this.ANGLE = 0;
        this.VISIBILITY = true;
    }

    //Мы не используем
    public void draw(SpriteBatch batch , OrthographicCamera camera) {

        if(VISIBILITY) {
            batch.draw(external, camera.unproject(new Vector3(positionExternal.x, 0, 0)).x - radiusExternal,
                    camera.unproject(new Vector3(0, positionExternal.y, 0)).y - radiusExternal,
                    radiusExternal * 2, radiusExternal * 2);


            batch.draw(internal, camera.unproject(new Vector3(positionInternal.x, 0, 0)).x - radiusInternal,
                    camera.unproject(new Vector3(0, positionInternal.y, 0)).y - radiusInternal,
                    radiusInternal * 2, radiusInternal * 2);
        }

    }

    public void setPositionExternal(float x, float y) {
        this.positionExternal.set(x, y);
    }

    public void setPositionInternal(float x, float y) {
        limP(x, y);
    }


    //Ограничивает центральный круг внутри внешнего круга
    public void limP(float x, float y) {
        float angle = getAngle(x, y);

        //Получаю границы внешнего круга где центр кргуа 0,0
        //Радиус - это ширина экрана деленная на ширину камеры и умноженная на реальный радиус
        float x1 = (float) ((Gdx.graphics.getWidth()/100)*radiusExternal * Math.cos(Math.toRadians(angle)));
        float y1 = (float) ((Gdx.graphics.getWidth()/100)*radiusExternal * Math.sin(Math.toRadians(angle)));

        //Превожу центральный круг в систему координат внешнего круга на оси коордиант экрана
        float x2 = x - positionExternal.x;
        float y2 = (Gdx.graphics.getHeight() - y) - (Gdx.graphics.getHeight() - positionExternal.y);

        if(angle >= 0 && angle <=90) {
            if(x2 > x1 || y2 > y1) {
                positionInternal.set(positionExternal.x + x1, positionExternal.y - y1);
            }
            else {
                positionInternal.set(x, y);
            }
        }
        else if(angle > 90 && angle <= 180) {
            if(x2 < x1 || y2 > y1) {
                positionInternal.set(positionExternal.x + x1, positionExternal.y - y1);
            }
            else {
                positionInternal.set(x, y);
            }
        }
        else if(angle > 180 && angle <= 270) {
            if(x2 < x1 || y2 < y1) {
                positionInternal.set(positionExternal.x + x1, positionExternal.y - y1);
            }
            else {
                positionInternal.set(x, y);
            }
        }
        else if(angle > 270 && angle <= 360) {
            if(x2 > x1 || y2 < y1) {
                positionInternal.set(positionExternal.x + x1, positionExternal.y - y1);
            }
            else {
                positionInternal.set(x, y);
            }
        }
        else {
            positionInternal.set(x, y);
        }
    }

    //Получаем угол джойстика в координатах внешнего круга
    public float getAngle(float x, float y) {

        //Привожу центральный круг в систему координат внешнего круга на оси коордиант экрана
        float x1 = x - positionExternal.x;
        float y1 = (Gdx.graphics.getHeight() - y) - (Gdx.graphics.getHeight() - positionExternal.y);

        if(x1 >= 0) {
            if(y1 >= 0) {
                //Если будут равны нулю, то получим Nan
                if(y1 != 0 && x1 != 0) {
                    this.ANGLE = (float) Math.toDegrees(Math.atan(y1/x1));
                }
            }
            else {
                this.ANGLE = 360f - (float) Math.toDegrees(Math.atan(Math.abs(y1)/x1));
            }

        }
        else {
            if(y1 >= 0) {
                this.ANGLE = 180f - (float) Math.toDegrees(Math.atan(y1/Math.abs(x1)));
            }
            else {
                this.ANGLE = 180f + (float) Math.toDegrees(Math.atan(Math.abs(y1)/Math.abs(x1)));
            }

        }


        //Проверка на Nan, но проблема исправлена и больше не должна возникать такая ситуация
        if(Float.isNaN(ANGLE)) {
            ANGLE = 0;
        }
        return ANGLE;

    }

    public Vector2 getVelocity(float SPEED) {
        float x = (float) (SPEED * Math.cos(Math.toRadians(ANGLE)));
        float y = (float) (SPEED * Math.sin(Math.toRadians(ANGLE)));

        return new Vector2(x, y);
    }

    //Возвращает JoyStick в первоночальное положение
    public void reset() {
        positionExternal.set(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f);
        positionInternal.set(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f);
    }

    public float getAngle() {
        return ANGLE;
    }

    public void setVISIBILITY(boolean VISIBILITY) {
        this.VISIBILITY = VISIBILITY;
    }

    public void setRadiusExternal(float radiusExternal) {
        this.radiusExternal = radiusExternal;
    }

    public void setRadiusInternal(float radiusInternal) {
        this.radiusInternal = radiusInternal;
    }

    public Texture getExternal() {
        return external;
    }

    public Texture getInternal() {
        return internal;
    }

    public Vector2 getPositionExternal() {
        return positionExternal;
    }

    public Vector2 getPositionInternal() {
        return positionInternal;
    }

    public float getRadiusExternal() {
        return radiusExternal;
    }

    public float getRadiusInternal() {
        return radiusInternal;
    }

}
