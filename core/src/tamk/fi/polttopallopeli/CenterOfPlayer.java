package tamk.fi.polttopallopeli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Handles players center point and lean logging.
 *
 * @author  Joni Alanko <joni.alanko@cs.tamk.fi>
 *          Juho Taakala <juho.taakala@cs.tamk.fi>
 * @since   2018.0222
 * @version 1.0
 */
public class CenterOfPlayer extends Sprite {
    private Array<CenterOfPlayerObject> center;
    private ShapeRenderer shape;
    private Vector2 calculatedCenter;
    private Vector2 lastPoint;
    Texture background;

    public CenterOfPlayer() {
        super(new Texture("tahtain.png"));
        setSize(getWidth() / 300f, getHeight() / 300f);
        background = new Texture("taulu.png");

        setOriginCenter();
        center = new Array<CenterOfPlayerObject>();
        shape = new ShapeRenderer();
        calculatedCenter = new Vector2();
        lastPoint = new Vector2();
    }

    /**
     * Sets player leaning.
     * @param x x-location
     * @param y y-location
     */
    public void modify(float x, float y) {
        final CenterOfPlayerObject object = new CenterOfPlayerObject(x / 6f, y / 6f);
        //Gdx.app.log(getClass().getSimpleName(), "Wörk MODIFY?" );

        for (CenterOfPlayerObject heatMapObject : center) {
            if (heatMapObject.point.equals(object.point)) {
                //heatMapObject.addHeatValue();
                //Gdx.app.log(getClass().getSimpleName(), "Wörk IF?" );
                return;
            }
        }

        center.add(object);
    }

    private class CenterOfPlayerObject {
        Vector2 point;
        Color color;

        CenterOfPlayerObject(float x, float y) {
            point = new Vector2(Dodgeball.WORLD_WIDTH / 25f + background.getWidth() / 200f / 2f + x,Dodgeball.WORLD_HEIGHT / 2f + y);   //OG point = new Vector2(round(x), round(y));
            //color = new Color(0.1f, 0, 0, 0.1f);
            //Gdx.app.log(getClass().getSimpleName(), "Wörk? POINT" + point );
        }

        void addHeatValue() {
            //color.add(0.017f, 0, 0, 0.015f);
            //Gdx.app.log(getClass().getSimpleName(), "Wörk? " + color);
        }

        private float round(float value) {
            return (float) Math.round((value) * 2.5f) / 2.5f;
        }
    }

    /**
     * Sets calculated centerpoint of player.
     * @param calculatedCenter is players center point.
     */
    public void calculatedCenter(Vector2 calculatedCenter) {
        this.calculatedCenter = calculatedCenter;
    }

    /**
     * Draws players movement directions and center point.
     *
     * Also draws background image where movement directions and center point are focused.
     *
     * @param batch Spritebatch.
     * @param camera world camera.
     */
    public void draw(Batch batch, Camera camera) {
        lastPoint.x = Dodgeball.WORLD_WIDTH / 25f + background.getWidth() / 200f / 2f;
        lastPoint.y = Dodgeball.WORLD_HEIGHT / 2f;

        batch.begin();
        batch.draw(background, Dodgeball.WORLD_WIDTH / 25f, Dodgeball.WORLD_HEIGHT / 4f, background.getWidth() / 200f, background.getHeight() / 200f);
        batch.end();

        for (CenterOfPlayerObject heat : center) {
            shape.setProjectionMatrix(camera.combined);
            shape.begin(ShapeRenderer.ShapeType.Line);
            shape.setColor(Color.BLACK);
            shape.line(heat.point, lastPoint);
            lastPoint = heat.point;
            shape.end();
        }

        setColor(1,0,0,1);
        setPosition(Dodgeball.WORLD_WIDTH / 25f + background.getWidth() / 200f / 2f + calculatedCenter.x / 3f - getWidth() / 2f, Dodgeball.WORLD_HEIGHT / 2f + calculatedCenter.y / 3f - getHeight() / 2f);
        batch.begin();
        draw(batch);
        batch.end();
        //Gdx.app.log(getClass().getSimpleName(), "x: " + (Dodgeball.WORLD_WIDTH / 2f + calculatedCenter.x - getWidth() / 2f) + " y: " + (Dodgeball.WORLD_HEIGHT / 2f + calculatedCenter.y - getHeight() / 2f));
    }

    public void dispose() {
        getTexture().dispose();
    }
}
