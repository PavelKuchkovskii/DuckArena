package org.dayaway.duckarena.controller;

import org.dayaway.duckarena.model.api.IBang;
import org.dayaway.duckarena.model.api.IWorld;

import java.util.ArrayList;
import java.util.List;

public class BangsController {

    private final IWorld world;
    private final List<IBang> bangs;

    //Need to remove
    private final List<IBang> tmpList;

    public BangsController(IWorld world) {
        this.world = world;
        this.bangs = world.getBangs();
        this.tmpList = new ArrayList<>();
    }

    public void update(float dt) {

        for (IBang bang : bangs) {
            bang.setFrame(bang.getAnimation().getFrame());
        }

        for (IBang bang : bangs) {
            bang.getAnimation().update(dt);
        }

        remove();

    }

    private void remove() {

        for (IBang bang : bangs) {
            //Если еще не нужно удалять, добавляем во временный лист
            if(!bang.getAnimation().isRemove()) {
                tmpList.add(bang);
            }
        }

        //Очищаем основной лист
        bangs.clear();

        //Добавляем все из временного листа
        bangs.addAll(tmpList);

        //Очищаем временный список
        tmpList.clear();
    }
}
