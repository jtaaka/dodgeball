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
import tamk.fi.polttopallopeli.Screens.Menu;

public class SplashScreen implements Screen {
    private Dodgeball host;
    private Texture splashTexture;
    private Image splashImage;
    private Stage stage;

    public SplashScreen (Dodgeball host) {
        this.host = host;

        stage = new Stage();
        splashTexture = new Texture(Gdx.files.internal("splashscreen.png"));
        splashImage = new Image(splashTexture);
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
        splashTexture.dispose();
        stage.dispose();
    }
}
