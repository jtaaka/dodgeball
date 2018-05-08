package tamk.fi.polttopallopeli.CampaignLevels;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

import tamk.fi.polttopallopeli.Dodgeball;

/**
 * Defines Level 1 variables.
 *
 * @author  Joni Alanko <joni.alanko@cs.tamk.fi>
 *          Juho Taakala <juho.taakala@cs.tamk.fi>
 * @since   2018.0222
 * @version 1.0
 */
public class Level1 implements Screen {

    /**
     * Defines level template.
     */
    private LevelTemplate levelTemplate;

    /**
     * Defines "main class" as a host.
     */
    private Dodgeball host;

    /**
     * Max ball amount on the field.
     */
    private int MAX_BALL_AMOUNT = 4; // Maksimi määrä palloja kentällä yhtäaikaa. esim: 10

    /**
     * Defines background texture for the level.
     */
    private Texture background;

    /**
     * Defines if the white timer is used or not.
     */
    boolean whiteTimer = false;

    public Level1(Dodgeball host) {
        background = new Texture("background1.png");
        levelTemplate = new LevelTemplate(host, MAX_BALL_AMOUNT, background, whiteTimer);
        this.host = host;

        levelTemplate.BALL_SPAWN_TIMER = 3; // Kauanko odotetaan pallon tuloa alussa (ja jos useampi alussa niin kauanko niiden välillä). SEKUNTTI. esim: 4
        levelTemplate.BALL_SPAWN_COUNT = 2; // Montako palloa lisätään alussa. esim: 3
        levelTemplate.ADD_NEW_BALL_TIME = 10; // Koska lisätään uusi pallo alun jälkeen. SEKUNTTI. esim: 60
        levelTemplate.ACCELERATING_BALL = false; // onko levelissä kiihtyvää palloa. true / false
        levelTemplate.TARGETING_BALL = false; // onko levelissä ennakoivaa palloa. true / false
        levelTemplate.FASTBALL = false; // onko levelissä nopeampaa palloa. true / false
        levelTemplate.HEALINGBALL = false; // onko levelissä parantavaa palloa. true / false
        levelTemplate.timeLimit = 35; //Tätä vaihtamalla vaihtuu kentän ajallinen pituus. Yksikkö on sekuntti. esim: 60
        levelTemplate.nextLevel = "level2"; // Seuraava avautuva kenttä. Esimerkiksi: "level2"
    }

    @Override
    public void show() {
        host.setScreen(levelTemplate);
    }

    @Override
    public void render(float delta) {

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

    }

    @Override
    public void dispose() {

    }
}
