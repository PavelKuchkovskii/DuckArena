package org.dayaway.duckarena.controller.utils;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class BangsAnimation {

    private final Array<TextureRegion> frames;

    //Максимальное вреимя отображения фрейма
    private final float maxFrameTime;

    //Текущее время отображения фрейма
    private float currentFrameTime;

    //Число фреймов
    private final int frameCount;

    //Текущий фрейм
    private int frame;

    private final float cycleTime;

    boolean remove;

    public BangsAnimation(int frameCount, float cycleTime, TextureRegion textureRegion) {
        this.frames = new Array<>();

        this.frameCount = frameCount;
        this.cycleTime = cycleTime;
        maxFrameTime = cycleTime / frameCount;
        frame = 0;

        int frameWidth = textureRegion.getRegionWidth() / frameCount;

        //Добавляем кадры ходьбы правой анимации
        for (int i = 0; i < frameCount; i++) {
            frames.add(new TextureRegion(textureRegion, i * frameWidth, 0 ,frameWidth, textureRegion.getRegionHeight()));
        }
    }

    public void update(float dt) {
        currentFrameTime += dt;

        if (currentFrameTime > maxFrameTime) {
            frame++;
            currentFrameTime = 0;
        }
        if (frame >= frameCount) {
            frame = 0;
            remove = true;
        }
    }

    public TextureRegion getFrame() {
        return this.frames.get(frame);
    }

    public boolean isRemove() {
        return remove;
    }
}
