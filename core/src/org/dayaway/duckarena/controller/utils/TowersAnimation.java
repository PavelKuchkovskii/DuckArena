package org.dayaway.duckarena.controller.utils;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class TowersAnimation {

    private final Array<TextureRegion> frames;

    //Максимальное вреимя отображения фрейма
    private float maxFrameTime;

    //Текущее время отображения фрейма
    private float currentFrameTime;

    //Число фреймов
    private final int frameCount;

    //Текущий фрейм
    private int frame;

    private final float cycleTime;

    public TowersAnimation(int frameCount, float cycleTime) {
        this.frames = new Array<>();

        this.frameCount = frameCount;
        this.cycleTime = cycleTime;
        maxFrameTime = cycleTime / frameCount;
        frame = 0;
    }

    public void update(float dt) {
        currentFrameTime += dt;

        if (currentFrameTime > maxFrameTime) {
            frame++;
            currentFrameTime = 0;
        }
        if (frame >= frameCount) {
            frame = 0;
        }
    }

    public TextureRegion getAnimation(TextureRegion peace) {
        return getFrames(peace).get(frame);
    }

    private Array<TextureRegion> getFrames(TextureRegion textureRegion) {
        frames.clear();

        int frameWidth = textureRegion.getRegionWidth() / frameCount;

        //Добавляем кадры ходьбы правой анимации
        for (int i = 0; i < frameCount; i++) {
            frames.add(new TextureRegion(textureRegion, i * frameWidth, 0 ,frameWidth, textureRegion.getRegionHeight()));
        }

        return this.frames;
    }

    public boolean isLastFrame() {
        return frame == (frameCount - 1);
    }

    public int getCurrentFrameCount() {
        return frame;
    }

}
