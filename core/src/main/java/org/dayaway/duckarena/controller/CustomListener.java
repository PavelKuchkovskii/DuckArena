package org.dayaway.duckarena.controller;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.WorldManifold;

import org.dayaway.duckarena.model.Bang;
import org.dayaway.duckarena.model.Barrel;
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

            //С Радиусом бочки
            else if(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals("barrel_radius")) {
                //Добавляем солдата в радиус взрыва
                ((Barrel) contact.getFixtureB().getBody().getUserData()).addSoldier((ISoldier) contact.getFixtureA().getBody().getUserData());
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
        }

        //Если ЛЮБАЯ ловушка сталкивается
        else if(contact.getFixtureA().getUserData() != null && ((String) contact.getFixtureA().getUserData()).contains("trap")) {
            //С КРИСТАЛЛАМИ
            if(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals("crystal") && !contact.getFixtureA().getUserData().equals("trap_edge")) {
                //Добавляем кристал в список на уничтожение
                world.addToDestroy(contact.getFixtureB().getBody());
            }
        }

        //Если радиус бочки сталкивается
        else if(contact.getFixtureA().getUserData() != null && contact.getFixtureA().getUserData().equals("barrel_radius")) {
            //С СОЛДАТАМИ
            if(contact.getFixtureB().getUserData() != null && ((String) contact.getFixtureB().getUserData()).contains("soldier")) {
                //Добавляем солдата в радиус взрыва
                ((Barrel) contact.getFixtureA().getBody().getUserData()).addSoldier((ISoldier) contact.getFixtureB().getBody().getUserData());
            }
        }



    }

    @Override
    public void endContact(Contact contact) {

        //Если солдат перестает сталкиваеться
        if(contact.getFixtureA().getUserData() != null && ((String) contact.getFixtureA().getUserData()).contains("soldier")) {
            //С Радиусом бочки
            if(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals("barrel_radius")) {
                //Удаляем солдата из радиуса взрыва
                ((Barrel) contact.getFixtureB().getBody().getUserData()).removeSoldier((ISoldier) contact.getFixtureA().getBody().getUserData());
            }
        }

        //Если радиус взрыва перестает соприкосаться с солдатом
        else if(contact.getFixtureA().getUserData() != null && contact.getFixtureA().getUserData().equals("barrel_radius")) {
            //С СОЛДАТАМИ
            if(contact.getFixtureB().getUserData() != null && ((String) contact.getFixtureB().getUserData()).contains("soldier")) {
                //Удаляем солдата из радиуса взрыва
                ((Barrel) contact.getFixtureA().getBody().getUserData()).removeSoldier((ISoldier) contact.getFixtureB().getBody().getUserData());
            }
        }

        //Если бочка перестает соприкосаться
        else if(contact.getFixtureA().getUserData() != null && contact.getFixtureA().getUserData().equals("barrel")) {
            //с Шаром
            if(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals("trap_circle")) {
                world.addToDestroy(contact.getFixtureA().getBody());
                world.addBang(new Bang(contact.getFixtureA().getBody().getPosition(), 50 ,50));
            }
        }
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

                //Если солдат сталкивается
                else if(contact.getFixtureA().getUserData() != null && ((String) contact.getFixtureA().getUserData()).contains("soldier")) {

                    //С ДРУГИМИ СОЛДАТАМИ
                    if(contact.getFixtureB().getUserData() != null && ((String) contact.getFixtureB().getUserData()).contains("soldier")
                            && !contact.getFixtureA().getUserData().equals(contact.getFixtureB().getUserData())) {
                        contact.setEnabled(false);
                        //Уничтожаем солдата
                        world.addToDestroy(contact.getFixtureA().getBody());
                        world.addToDestroy(contact.getFixtureB().getBody());

                        //Добавляем анимацию взрыва на оюоих солдат
                        world.addBang(new Bang(contact.getFixtureA().getBody().getPosition(), 7,7));
                        world.addBang(new Bang(contact.getFixtureB().getBody().getPosition(), 7, 7));
                    }
                }

                //Если ЛЮБАЯ ловушка сталкивается
                else if(contact.getFixtureA().getUserData() != null && ((String) contact.getFixtureA().getUserData()).contains("trap")) {

                    //С СОЛДАТАМИ
                    if(contact.getFixtureB().getUserData() != null && ((String) contact.getFixtureB().getUserData()).contains("soldier")) {
                        contact.setEnabled(false);
                        //Уничтожаем солдата
                        world.addToDestroy(contact.getFixtureB().getBody());
                        //Добавляем анимацию взрыва
                        world.addBang(new Bang(contact.getFixtureB().getBody().getPosition(), 7, 7));
                    }

                    //С бочками и это не шар
                    else if(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals("barrel")  && !contact.getFixtureA().getUserData().equals("trap_circle")) {
                        contact.setEnabled(false);
                        world.addToDestroy(contact.getFixtureB().getBody());
                    }

                }

                //Если бочка сталкивается
                else if(contact.getFixtureA().getUserData() != null && contact.getFixtureA().getUserData().equals("barrel")) {
                    //С центрами масс
                    if(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals("player") || contact.getFixtureB().getUserData().equals("bot")) {
                        contact.setEnabled(false);
                    }

                    //С солдатами
                    else if(contact.getFixtureB().getUserData() != null && ((String) contact.getFixtureB().getUserData()).contains("soldier")) {

                        //ACTIVATE BARREL
                        world.addToDestroy(contact.getFixtureA().getBody());
                        world.addBang(new Bang(contact.getFixtureA().getBody().getPosition(), 50 ,50));

                        for (ISoldier soldier : ((Barrel) contact.getFixtureA().getBody().getUserData()).getSoldiers()) {
                            world.addToDestroy(soldier.getBody());

                            world.addBang(new Bang(soldier.getPosition(), 7 ,7));
                        }

                    }

                    //С ловушками и это не шар
                    else if(contact.getFixtureB().getUserData() != null && ((String) contact.getFixtureB().getUserData()).contains("trap") && !contact.getFixtureB().getUserData().equals("trap_circle")) {
                        contact.setEnabled(false);
                        world.addToDestroy(contact.getFixtureA().getBody());
                    }
                }
            }
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
