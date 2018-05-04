package tamk.fi.polttopallopeli.CampaignLevels;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

import tamk.fi.polttopallopeli.Dodgeball;

/**
 * Defines Level 9 variables.
 *
 * @author  Joni Alanko <joni.alanko@cs.tamk.fi>
 *          Juho Taakala <juho.taakala@cs.tamk.fi>
 * @since   2018.0222
 * @version 1.0
 */
public class Level9 implements Screen {
    private LevelTemplate levelTemplate;
    private Dodgeball host;
    int MAX_BALL_AMOUNT = 8; // Maksimi määrä palloja kentällä yhtäaikaa. esim: 10
    private Texture background;
    boolean whiteTimer = false;

    public Level9(Dodgeball host) {
        background = new Texture("marmoribg.png");
        levelTemplate = new LevelTemplate(host, MAX_BALL_AMOUNT, background, whiteTimer);
        this.host = host;

        levelTemplate.BALL_SPAWN_TIMER = 2; // Kauanko odotetaan pallon tuloa alussa (ja jos useampi alussa niin kauanko niiden välillä). SEKUNTTI. esim: 4
        levelTemplate.BALL_SPAWN_COUNT = 4; // Montako palloa lisätään alussa. esim: 3
        levelTemplate.ADD_NEW_BALL_TIME = 7; // Koska lisätään uusi pallo alun jälkeen. SEKUNTTI. esim: 60
        levelTemplate.ACCELERATING_BALL = true; // onko levelissä kiihtyvää palloa. true / false
        levelTemplate.TARGETING_BALL = true; // onko levelissä ennakoivaa palloa. true / false
        levelTemplate.FASTBALL = true; // onko levelissä nopeampaa palloa. true / false
        levelTemplate.HEALINGBALL = true; // onko levelissä parantavaa palloa. true / false
        levelTemplate.timeLimit = 105; //Tätä vaihtamalla vaihtuu kentän ajallinen pituus. Yksikkö on sekuntti. esim: 60
        levelTemplate.nextLevel = "level10"; // Seuraava avautuva kenttä. Esimerkiksi: "level2"
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
