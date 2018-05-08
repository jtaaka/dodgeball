package tamk.fi.polttopallopeli.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import tamk.fi.polttopallopeli.Dodgeball;

/**
 * A class for the splash screen.
 *
 * @author  Juho Taakala <juho.taakala@cs.tamk.fi>
 *          Joni Alanko <joni.alanko@cs.tamk.fi>
 * @since   2018.0222
 * @version 1.0
 */
public class SplashScreen implements Screen {

    /**
     * Defines "main class" as a host.
     */
    private Dodgeball host;

    /**
     * Defines a background image for the splash screen.
     */
    private Image splashImage;

    /**
     * Defines a stage for the splash screen.
     */
    private Stage stage;

    /**
     * Constructor for the splash screen.
     *
     * @param host "main class" host.
     */
    public SplashScreen (Dodgeball host) {
        this.host = host;

        stage = new Stage();
        splashImage = new Image(new Texture("splashscreen.png"));
    }

    @Override
    public void show() {
        stage.addActor(splashImage);
        splashImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        splashImage.addAction(Actions.sequence(Actions.alpha(0f),
                Actions.fadeIn(2.0f), Actions.delay(1.5f),
                Actions.fadeOut(2f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        ((Game)Gdx.app.getApplicationListener()).setScreen(new Menu(host));
                    }
                })));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isTouched()) {
            host.setScreen(new Menu(host));
        }

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
