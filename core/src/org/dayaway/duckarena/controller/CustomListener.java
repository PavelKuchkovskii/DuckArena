package org.dayaway.duckarena.controller;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.WorldManifold;

import org.dayaway.duckarena.model.api.ISoldier;
import org.dayaway.duckarena.model.api.IWorld;

public class CustomListener implements ContactListener {

    private final IWorld world;
    private final BattleController controller;

    public CustomListener(IWorld world, BattleController controller) {
        this.world = world;
        this.controller = controller;
    }

    @Override
    public void beginContact(Contact contact) {

        //Если солдат сталкивается
        if(contact.getFixtureA().getUserData() != null && ((String) contact.getFixtureA().getUserData()).contains("soldier")) {
            //С БАШНЯМИ
            if(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals("tower")) {
                //Уничтожаем солдата
                world.addToDestroy(contact.getFixtureA().getBody());
            }
            //С КРИСТАЛЛАМИ
            else if(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals("crystal")) {
                //Добавляем кристал в список на уничтожение
                world.addToDestroy(contact.getFixtureB().getBody());
                //Добавляем Player опыт из кристалла
                ((ISoldier) contact.getFixtureA().getBody().getUserData()).getPlayer().exp(contact.getFixtureB().getBody());
            }

            //С ДРУГИМИ СОЛДАТАМИ
            if(contact.getFixtureB().getUserData() != null && ((String) contact.getFixtureB().getUserData()).contains("soldier")
            && !contact.getFixtureA().getUserData().equals(contact.getFixtureB().getUserData())) {
                //Уничтожаем солдата
                world.addToDestroy(contact.getFixtureA().getBody());
                world.addToDestroy(contact.getFixtureB().getBody());
            }
        }

        //Если башня сталкивается
        else if(contact.getFixtureA().getUserData() != null && contact.getFixtureA().getUserData().equals("tower")) {
            //С СОЛДАТАМИ
            if(contact.getFixtureB().getUserData() != null && ((String) contact.getFixtureA().getUserData()).contains("soldier")) {
                //Уничтожаем солдата
                world.addToDestroy(contact.getFixtureB().getBody());
            }
        }

        //Если кристалл сталкивается
        else if(contact.getFixtureA().getUserData() != null && contact.getFixtureA().getUserData().equals("crystal")) {
            //С СОЛДАТАМИ
            if(contact.getFixtureB().getUserData() != null && ((String) contact.getFixtureB().getUserData()).contains("soldier")) {
                //Добавляем кристал в список на уничтожение
                world.addToDestroy(contact.getFixtureA().getBody());
                //Добавляем Player опыт из кристалла
                ((ISoldier) contact.getFixtureB().getBody().getUserData()).getPlayer().exp(contact.getFixtureA().getBody());
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

            //Если Player(Центр масс) контактирует
            if(contact.getFixtureA().getUserData() != null && contact.getFixtureA().getUserData().equals("player")){

                //С солдатами или башнями или центром масс бота
                if(contact.getFixtureB().getUserData() != null && ((String) contact.getFixtureB().getUserData()).contains("soldier") ||
                        contact.getFixtureB().getUserData().equals("tower") || contact.getFixtureB().getUserData().equals("bot")) {
                    //Отключаем этот контакт
                    contact.setEnabled(false);
                }
            }

            //Если Bot(Центр масс) контактирует
            if(contact.getFixtureA().getUserData() != null && contact.getFixtureA().getUserData().equals("bot")){

                //С солдатами или башнями
                if(contact.getFixtureB().getUserData() != null && ((String) contact.getFixtureB().getUserData()).contains("soldier") ||
                        contact.getFixtureB().getUserData().equals("tower") || contact.getFixtureB().getUserData().equals("player")) {
                    //Отключаем этот контакт
                    contact.setEnabled(false);
                }
            }
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
