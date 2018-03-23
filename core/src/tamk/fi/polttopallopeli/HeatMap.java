package tamk.fi.polttopallopeli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Joni on 22.3.2018.
 */

class HeatMap {
    private Array<HeatMapObject> heatMap;

    public HeatMap() {
        heatMap = new Array<HeatMapObject>();

    }

    public void modify(float x, float y) {
        final HeatMapObject object = new HeatMapObject(x, y);
        Gdx.app.log(getClass().getSimpleName(), "Wörk MODIFY?" );

        for (HeatMapObject heatMapObject : heatMap) {
            if (heatMapObject.point.equals(object.point)) {
                heatMapObject.addHeatValue();
                Gdx.app.log(getClass().getSimpleName(), "Wörk IF?" );
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
            color = new Color(0, 1, 1, 0);
            Gdx.app.log(getClass().getSimpleName(), "Wörk? POINT" + point );
        }

        void addHeatValue() {
            color.add(0.02f, 0, 0, 0.2f);
            Gdx.app.log(getClass().getSimpleName(), "Wörk? " + color);
        }

        private float round(float value) {
            return (float) Math.round(value);
        }
    }
}