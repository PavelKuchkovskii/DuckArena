package org.dayaway.duckarena.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import org.dayaway.duckarena.controller.utils.BangsAnimation;
import org.dayaway.duckarena.model.api.IBang;
import org.dayaway.duckarena.screens.BattleScreen;

public class Bang implements IBang {

    private final float WIDTH;
    private final float HEIGHT;

    private final TextureRegion textureRegion;
    private final Vector2 position;
    private TextureRegion frame;
    private final BangsAnimation animation;

    public Bang(Vector2 position, int width, int height) {
        this.position = new Vector2(position);
        this.textureRegion = BattleScreen.textures.getRandomBang();
        this.animation = new BangsAnimation(9, 0.5f, textureRegion);
        this.WIDTH = width;
        this.HEIGHT = height;
    }

    @Override
    public Body getBody() {
        return null;
    }

    @Override
    public TextureRegion getTexture() {
        return this.textureRegion;
    }

    @Override
    public TextureRegion getFrame() {
        return this.frame;
    }

    @Override
    public void setFrame(TextureRegion texture) {
        this.frame = texture;
    }

    @Override
    public float getWidth() {
        return this.WIDTH;
    }

    @Override
    public float getHeight() {
        return this.HEIGHT;
    }

    @Override
    public Vector2 getPosition() {
        return this.position;
    }

    @Override
    public BangsAnimation getAnimation() {
        return this.animation;
    }
}
