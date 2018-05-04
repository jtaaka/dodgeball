package tamk.fi.polttopallopeli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Handles players location logging.
 *
 * @author  Joni Alanko <joni.alanko@cs.tamk.fi>
 *          Juho Taakala <juho.taakala@cs.tamk.fi>
 * @since   2018.0222
 * @version 1.0
 */
public class HeatMap extends Sprite {
    private Array<HeatMapObject> heatMap;

    public HeatMap() {
        super(new Texture("peruspalloTESTI.png"));
        setSize(getWidth() / 200f, getHeight() / 200f);
        setOriginCenter();
        heatMap = new Array<HeatMapObject>();
    }

    /**
     * Sets player location.
     *
     * If location is too close to existing one, adds more color to it.
     *
     * @param x x-location
     * @param y y-location
     */
    public void modify(float x, float y) {
        final HeatMapObject object = new HeatMapObject(x, y);
        //Gdx.app.log(getClass().getSimpleName(), "Wörk MODIFY?" );

        for (HeatMapObject heatMapObject : heatMap) {
            if (heatMapObject.point.equals(object.point)) {
                heatMapObject.addHeatValue();
                //Gdx.app.log(getClass().getSimpleName(), "Wörk IF?" );
                return;
            }
        }

        heatMap.add(object);
    }

    private class HeatMapObject {
        Vector2 point;
        Color color;

        HeatMapObject(float x, float y) {
            point = new Vector2(round(x), round(y));
            color = new Color(0.1f, 0, 0, 0.1f);
            //Gdx.app.log(getClass().getSimpleName(), "Wörk? POINT" + point );
        }

        void addHeatValue() {
            color.add(0.017f, 0, 0, 0.015f);
            //Gdx.app.log(getClass().getSimpleName(), "Wörk? " + color);
        }

        private float round(float value) {
            return (float) Math.round((value) * 2.5f) / 2.5f;
        }
    }

    /**
     * Draws heatmap.
     * @param batch Spritebatch.
     */
    public void draw(Batch batch) {
        batch.begin();
        for (HeatMapObject heat : heatMap) {
            setColor(heat.color);
            setPosition(heat.point.x - getWidth() / 2f, heat.point.y - getHeight() / 2f);
            super.draw(batch);
        }
        batch.end();
    }

    public void dispose() {
        getTexture().dispose();
    }
}
