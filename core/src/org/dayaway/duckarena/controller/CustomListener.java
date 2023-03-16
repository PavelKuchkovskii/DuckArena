package org.dayaway.duckarena.controller;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.WorldManifold;

import org.dayaway.duckarena.model.Bang;
import org.dayaway.duckarena.model.api.ISoldier;
import org.dayaway.duckarena.model.api.IWorld;

public class CustomListener implements ContactListener {

    private final IWorld world;

    public CustomListener(IWorld world, BattleController controller) {
        this.world = world;
    }

    @Override
    public void beginContact(Contact contact) {

        //Если солдат сталкивается
        if(contact.getFixtureA().getUserData() != null && ((String) contact.getFixtureA().getUserData()).contains("soldier")) {
            //С КРИСТАЛЛАМИ
            if(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals("crystal")) {
                //Добавляем Player опыт из кристалла
                ((ISoldier) contact.getFixtureA().getBody().getUserData()).getPlayer().exp(contact.getFixtureB().getBody());
                //Добавляем кристал в список на уничтожение
                world.addToDestroy(contact.getFixtureB().getBody());
            }

            //С ДРУГИМИ СОЛДАТАМИ
            else if(contact.getFixtureB().getUserData() != null && ((String) contact.getFixtureB().getUserData()).contains("soldier")
            && !contact.getFixtureA().getUserData().equals(contact.getFixtureB().getUserData())) {
                //Уничтожаем солдата
                world.addToDestroy(contact.getFixtureA().getBody());
                world.addToDestroy(contact.getFixtureB().getBody());

                //Добавляем анимацию взрыва на оюоих солдат
                world.addBang(new Bang(contact.getFixtureA().getBody().getPosition()));
                world.addBang(new Bang(contact.getFixtureB().getBody().getPosition()));
            }

            //С ЛЮБЫМИ ловушками
            else if(contact.getFixtureB().getUserData() != null && ((String) contact.getFixtureB().getUserData()).contains("trap")) {
                //Уничтожаем солдата
                world.addToDestroy(contact.getFixtureA().getBody());
                world.addBang(new Bang(contact.getFixtureA().getBody().getPosition()));
            }
        }

        //Если ЛЮБАЯ ловушка сталкивается
        else if(contact.getFixtureA().getUserData() != null && ((String) contact.getFixtureA().getUserData()).contains("trap")) {
            //С СОЛДАТАМИ
            if(contact.getFixtureB().getUserData() != null && ((String) contact.getFixtureB().getUserData()).contains("soldier")) {
                //Уничтожаем солдата
                world.addToDestroy(contact.getFixtureB().getBody());
                //Добавляем анимацию взрыва
                world.addBang(new Bang(contact.getFixtureB().getBody().getPosition()));
            }

            //С КРИСТАЛЛАМИ
            else if(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals("crystal") && !contact.getFixtureA().getUserData().equals("trap_edge")) {
                //Добавляем кристал в список на уничтожение
                world.addToDestroy(contact.getFixtureB().getBody());
            }
        }

        //Если кристалл сталкивается
        else if(contact.getFixtureA().getUserData() != null && contact.getFixtureA().getUserData().equals("crystal")) {
            //С СОЛДАТАМИ
            if(contact.getFixtureB().getUserData() != null && ((String) contact.getFixtureB().getUserData()).contains("soldier")) {
                //Добавляем Player опыт из кристалла
                ((ISoldier) contact.getFixtureB().getBody().getUserData()).getPlayer().exp(contact.getFixtureA().getBody());
                //Добавляем кристал в список на уничтожение
                world.addToDestroy(contact.getFixtureA().getBody());
            }
            //С ЛЮБОЙ ловушкой
            else if(contact.getFixtureB().getUserData() != null && ((String) contact.getFixtureB().getUserData()).contains("trap") && !contact.getFixtureB().getUserData().equals("trap_edge")) {
                //Добавляем кристал в список на уничтожение
                world.addToDestroy(contact.getFixtureA().getBody());
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

        WorldManifold manifold = contact.getWorldManifold();
        for(int j = 0; j < manifold.getNumberOfContactPoints(); j++){

            if(contact.getFixtureA().getUserData() != null) {

                //Если Player или Bot(Центр масс) контактирует
                if(contact.getFixtureA().getUserData().equals("player") || contact.getFixtureA().getUserData().equals("bot")) {

                    if(contact.getFixtureB().getUserData() != null) {
                        //Контактирует с солдатами, бочками или другими центрами масс
                        if(((String) contact.getFixtureB().getUserData()).contains("soldier")
                                || contact.getFixtureB().getUserData().equals("bot")
                                || contact.getFixtureB().getUserData().equals("player")) {
                            //Отключаем этот контакт
                            contact.setEnabled(false);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
