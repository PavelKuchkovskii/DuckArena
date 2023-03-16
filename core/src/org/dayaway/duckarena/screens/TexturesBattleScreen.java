package org.dayaway.duckarena.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Random;

public class TexturesBattleScreen {

    private final Random random = new Random();

    public final TextureRegion actorPeace = new TextureRegion(new Texture("walk.png"));
    public final TextureRegion[] actors = new TextureRegion[16];
    public final TextureRegion[] bangs = new TextureRegion[6];
    public final TextureRegion map = new TextureRegion(new Texture("WB.png"));
    public final TextureRegion crystal = new TextureRegion(new Texture("crystal.png"));
    public final TextureRegion trap = new TextureRegion(new Texture("trap_adg_map.png"));
    public final TextureRegion trap_cross = new TextureRegion(new Texture("trap_cross.png"));
    public final TextureRegion bang = new TextureRegion(new Texture("bang.png"));
    public final TextureRegion barrel = new TextureRegion(new Texture("barrel.png"));

    public TexturesBattleScreen() {
        initActors();
        initBangs();
    }

    private void initActors() {
        actors[0] = new TextureRegion(new Texture("duck-1.png"));
        actors[1] = new TextureRegion(new Texture("duck-2.png"));
        actors[2] = new TextureRegion(new Texture("duck-3.png"));
        actors[3] = new TextureRegion(new Texture("duck-4.png"));
        actors[4] = new TextureRegion(new Texture("duck-5.png"));
        actors[5] = new TextureRegion(new Texture("duck-6.png"));
        actors[6] = new TextureRegion(new Texture("duck-7.png"));
        actors[7] = new TextureRegion(new Texture("duck-8.png"));
        actors[8] = new TextureRegion(new Texture("duck-9.png"));
        actors[9] = new TextureRegion(new Texture("duck-10.png"));
        actors[10] = new TextureRegion(new Texture("duck-11.png"));
        actors[11] = new TextureRegion(new Texture("duck-12.png"));
        actors[12] = new TextureRegion(new Texture("duck-13.png"));
        actors[13] = new TextureRegion(new Texture("duck-14.png"));
        actors[14] = new TextureRegion(new Texture("duck-15.png"));
        actors[15] = new TextureRegion(new Texture("duck-16.png"));
    }

    private void initBangs() {
        bangs[0] = new TextureRegion(new Texture("bang.png"));
        bangs[1] = new TextureRegion(new Texture("1bang.png"));
        bangs[2] = new TextureRegion(new Texture("2bang.png"));
        bangs[3] = new TextureRegion(new Texture("3bang.png"));
        bangs[4] = new TextureRegion(new Texture("4bang.png"));
        bangs[5] = new TextureRegion(new Texture("5bang.png"));
    }

    public TextureRegion getRandomActor() {
        return actors[random.nextInt(15)];
    }

    public TextureRegion getRandomBang() {
        return bangs[random.nextInt(6)];
    }
}
