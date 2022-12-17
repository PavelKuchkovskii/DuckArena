package org.dayaway.duckarena.controller.utils;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class SoldierAnimation {

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

    public SoldierAnimation(int frameCount, float cycleTime) {
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

    public TextureRegion getRightAnimation(TextureRegion peace) {
        return getFrames(peace, false).get(frame);
    }

    public TextureRegion getLeftAnimation(TextureRegion peace) {
        return getFrames(peace, true).get(frame);
    }

    public int getCurrentFrameCount() {
        return frame;
    }

    private Array<TextureRegion> getFrames(TextureRegion textureRegion, boolean inverse) {
        frames.clear();

        int frameWidth = textureRegion.getRegionWidth() / frameCount;

        //Добавляем кадры ходьбы правой анимации
        for (int i = 0; i < frameCount; i++) {
            frames.add(new TextureRegion(textureRegion, i * frameWidth, 0 ,frameWidth, textureRegion.getRegionHeight()));
        }

        //По стандарту все анимации правые, если inverse - значит нужна левая
        if(inverse) {
            //Делаем из правой анимации - левую
            for (int i = 0; i < frames.size; i++) {
                TextureRegion tr = new TextureRegion(frames.get(i));
                tr.flip(true, false);
                frames.set(i, tr);
            }
        }

        return this.frames;
    }

    public boolean isLastFrame() {
        return frame == (frameCount - 1);
    }
}
