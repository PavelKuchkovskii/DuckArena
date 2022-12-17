package org.dayaway.duckarena.model.api;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public interface IActor {

    Body getBody();

    //Get Actor Texture
    TextureRegion getTexture();

    TextureRegion getFrame();

    void setFrame(TextureRegion frame);

    int getWidth();

    int getHeight();

    Vector2 getPosition();
}
