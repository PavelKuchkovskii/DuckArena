package org.dayaway.duckarena.controller;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.WorldManifold;

import org.dayaway.duckarena.model.Bang;
import org.dayaway.duckarena.model.Crystal;
import org.dayaway.duckarena.model.api.ISoldier;
import org.dayaway.duckarena.model.api.IWorld;
import org.dayaway.duckarena.screens.BattleScreen;

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

                //Добавляем анимацию взрыва
                world.addBang(new Bang(contact.getFixtureA().getBody().getPosition(), BattleScreen.textures.bang));
            }
            //С КРИСТАЛЛАМИ
            else if(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals("crystal")) {
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
                world.addBang(new Bang(contact.getFixtureA().getBody().getPosition(), BattleScreen.textures.bang));
                world.addBang(new Bang(contact.getFixtureB().getBody().getPosition(), BattleScreen.textures.bang));
            }

            //С ЛЮБЫМИ ловушками
            else if(contact.getFixtureB().getUserData() != null && ((String) contact.getFixtureB().getUserData()).contains("trap")) {
                //Уничтожаем солдата
                world.addToDestroy(contact.getFixtureA().getBody());
                world.addBang(new Bang(contact.getFixtureA().getBody().getPosition(), BattleScreen.textures.bang));
            }
        }

        //Если башня сталкивается
        else if(contact.getFixtureA().getUserData() != null && contact.getFixtureA().getUserData().equals("tower")) {
            //С СОЛДАТАМИ
            if(contact.getFixtureB().getUserData() != null && ((String) contact.getFixtureB().getUserData()).contains("soldier")) {
                //Уничтожаем солдата
                world.addToDestroy(contact.getFixtureB().getBody());
                //Добавляем анимацию взрыва
                world.addBang(new Bang(contact.getFixtureB().getBody().getPosition(), BattleScreen.textures.bang));
            }

            //С КРИСТАЛЛАМИ-НЕ РАБОТАЕТ!!!!
            else if(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals("crystal")) {
                //Ставим метку что Кристалл опасный
                ((Crystal) contact.getFixtureB().getBody().getUserData()).setDanger(true);
                System.out.println("!!!!!B!");
            }
        }

        //Если ЛЮБАЯ ловушка сталкивается
        else if(contact.getFixtureA().getUserData() != null && ((String) contact.getFixtureA().getUserData()).contains("trap")) {
            //С СОЛДАТАМИ
            if(contact.getFixtureB().getUserData() != null && ((String) contact.getFixtureB().getUserData()).contains("soldier")) {
                //Уничтожаем солдата
                world.addToDestroy(contact.getFixtureB().getBody());
                //Добавляем анимацию взрыва
                world.addBang(new Bang(contact.getFixtureB().getBody().getPosition(), BattleScreen.textures.bang));
            }

            //С КРИСТАЛЛАМИ
            else if(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals("crystal")) {
                //Ставим метку что Кристалл опасный
                ((Crystal) contact.getFixtureB().getBody().getUserData()).setDanger(true);
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
            else if(contact.getFixtureB().getUserData() != null && ((String) contact.getFixtureB().getUserData()).contains("trap")) {
                //Ставим метку что Кристалл опасный
                ((Crystal) contact.getFixtureA().getBody().getUserData()).setDanger(true);
            }
            //С БАШНЯМИ-НЕ РАБОТАЕТ!!!!
            else if(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals("tower")) {
                //Ставим метку что Кристалл опасный
                ((Crystal) contact.getFixtureA().getBody().getUserData()).setDanger(true);
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

        //Если кристалл перестает сталкиваться
        if(contact.getFixtureA().getUserData() != null && contact.getFixtureA().getUserData().equals("crystal")) {
            //С ЛЮБОЙ ловушкой
            if(contact.getFixtureB().getUserData() != null && ((String) contact.getFixtureB().getUserData()).contains("trap")) {

                //Если c шаром, то ставим метку что он безопасен
                if (contact.getFixtureB().getUserData() != null && ((String) contact.getFixtureB().getUserData()).contains("circle")) {
                    //Ставим метку что Кристалл безопасный
                    ((Crystal) contact.getFixtureA().getBody().getUserData()).setDanger(false);
                }
                //Иначе удаялем кристалл
                else {
                    //Добавляем кристал в список на уничтожение
                    world.addToDestroy(contact.getFixtureA().getBody());
                }
            }

            //С БАШНЯМИ
            else if(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals("tower")) {
                //Ставим метку что Кристалл безопасный
                ((Crystal) contact.getFixtureA().getBody().getUserData()).setDanger(false);
            }
        }

        //Если любая ловушка перестает сталкиваться
        else if(contact.getFixtureA().getUserData() != null && ((String) contact.getFixtureA().getUserData()).contains("trap")) {
            //С КРИСТАЛЛАМИ
            if(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals("crystal")) {

                //Если ловушка шар
                if(contact.getFixtureA().getUserData() != null && ((String) contact.getFixtureA().getUserData()).contains("circle")) {
                    //Ставим метку что Кристалл безопасный
                    ((Crystal) contact.getFixtureB().getBody().getUserData()).setDanger(false);
                }
                //Иначе удаялем кристалл
                else {
                    //Добавляем кристал в список на уничтожение
                    world.addToDestroy(contact.getFixtureB().getBody());
                }

            }
        }

        //Если башня сталкивается
        else if(contact.getFixtureA().getUserData() != null && contact.getFixtureA().getUserData().equals("tower")) {
            //С КРИСТАЛЛАМИ
            if(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals("crystal")) {
                //Ставим метку что Кристалл безопасный
                ((Crystal) contact.getFixtureB().getBody().getUserData()).setDanger(false);
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

        WorldManifold manifold = contact.getWorldManifold();
        for(int j = 0; j < manifold.getNumberOfContactPoints(); j++){

            //Если Player(Центр масс) контактирует
            if(contact.getFixtureA().getUserData() != null && contact.getFixtureA().getUserData().equals("player")){

                //С солдатами или башнями или центром масс бота
                if(contact.getFixtureB().getUserData() != null) {
                    if(((String) contact.getFixtureB().getUserData()).contains("soldier") ||
                            contact.getFixtureB().getUserData().equals("tower") || contact.getFixtureB().getUserData().equals("bot")) {
                        //Отключаем этот контакт
                        contact.setEnabled(false);
                    }
                }
            }

            //Если Bot(Центр масс) контактирует
            else if(contact.getFixtureA().getUserData() != null && contact.getFixtureA().getUserData().equals("bot")){

                //С солдатами или башнями
                if(contact.getFixtureB().getUserData() != null) {
                    if(((String) contact.getFixtureB().getUserData()).contains("soldier") ||
                            contact.getFixtureB().getUserData().equals("tower") || contact.getFixtureB().getUserData().equals("player")) {
                        //Отключаем этот контакт
                        contact.setEnabled(false);
                    }
                }
            }
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
