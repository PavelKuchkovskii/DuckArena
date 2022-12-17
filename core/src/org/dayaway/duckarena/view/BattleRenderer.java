package org.dayaway.duckarena.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import org.dayaway.duckarena.Util.Box2DCustomDebugRenderer;
import org.dayaway.duckarena.model.api.IActor;
import org.dayaway.duckarena.model.api.IWorld;
import org.dayaway.duckarena.view.api.IRenderer;

public class BattleRenderer implements IRenderer {

    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final IWorld world;

    Box2DCustomDebugRenderer renderer = new Box2DCustomDebugRenderer();

    private final BitmapFont exp;

    public BattleRenderer(IWorld world) {
        this.batch = new SpriteBatch();
        this.world = world;
        this.camera = new OrthographicCamera(100, 100 * ((float) Gdx.graphics.getHeight()/Gdx.graphics.getWidth()));
        this.camera.position.set(camera.viewportWidth/2f, camera.viewportHeight/2f, 0);

        this.exp = new BitmapFont();
        this.exp.getData().setScale(0.2f);
        this.exp.setUseIntegerPositions(false);
    }

    @Override
    public void render(float dt) {
        clearScreen();

        //camera.position.set(world.getPlayer().getPosition().x, world.getPlayer().getPosition().y, 0);
        camera.position.set(world.getBots().get(0).getPosition().x, world.getBots().get(0).getPosition().y, 0);

        camera.update();

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        for (IActor actor : world.getActors()) {
            if(actor.getFrame() != null) {
                batch.draw(actor.getFrame(), actor.getPosition().x - actor.getWidth() / 2f, actor.getPosition().y - actor.getHeight() / 2f,
                        actor.getWidth(), actor.getHeight());
            }
        }

        //Рисуем опыт
        exp.draw(batch,"Exp: " + world.getPlayer().getExp() + "/" + world.getPlayer().getLevel().getExp(), camera.unproject(new Vector3(0,0,0)).x,
                camera.unproject(new Vector3(0,0,0)).y);

        batch.end();

        renderer.render(world.getWorld(), camera.combined);
        world.getWorld().step(1/60f, 6, 1);

        //System.out.println(Gdx.graphics.getFramesPerSecond());

        /*
        System.out.println(batch.renderCalls);
        System.out.println(batch.maxSpritesInBatch);*/
    }

    @Override
    public void resize(int width, int height) {
        camera.position.set(camera.viewportWidth/2f, camera.viewportHeight/2f, 0);
    }

    @Override
    public void dispose() {

    }

    @Override
    public SpriteBatch getBatch() {
        return this.batch;
    }

    @Override
    public IWorld getWorld() {
        return this.world;
    }

    public void clearScreen() {
        Gdx.gl.glClearColor(247, 215, 116, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
