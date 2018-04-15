package tamk.fi.polttopallopeli;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Joni on 22.3.2018.
 */

public class CenterOfPlayer extends Sprite {
    private Array<CenterOfPlayerObject> center;
    private ShapeRenderer shape;

    public CenterOfPlayer() {
        super(new Texture("peruspalloTESTI.png"));
        setSize(getWidth() / 300f, getHeight() / 300f);
        setOriginCenter();
        center = new Array<CenterOfPlayerObject>();
        shape = new ShapeRenderer();
    }

    public void modify(float x, float y) {
        final CenterOfPlayerObject object = new CenterOfPlayerObject(x, y);
        //Gdx.app.log(getClass().getSimpleName(), "Wörk MODIFY?" );

        for (CenterOfPlayerObject heatMapObject : center) {
            if (heatMapObject.point.equals(object.point)) {
                heatMapObject.addHeatValue();
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
            point = new Vector2(Dodgeball.WORLD_WIDTH / 2f + x,Dodgeball.WORLD_HEIGHT / 2f + y);   //OG point = new Vector2(round(x), round(y));
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

    public void draw(Batch batch, Camera camera) {
        Vector2 lastPoint = new Vector2();
        lastPoint.x = Dodgeball.WORLD_WIDTH / 2f;
        lastPoint.y = Dodgeball.WORLD_HEIGHT / 2f;
        for (CenterOfPlayerObject heat : center) {
            setColor(heat.color);
            //setPosition(Dodgeball.WORLD_WIDTH / 2f + heat.point.x - getWidth() / 2f, Dodgeball.WORLD_HEIGHT / 2f + heat.point.y - getHeight() / 2f);
            shape.setProjectionMatrix(camera.combined);
            shape.begin(ShapeRenderer.ShapeType.Line);
            shape.line(heat.point, lastPoint);
            lastPoint = heat.point;
            shape.end();
            super.draw(batch);
        }
    }

    public void dispose() {
        getTexture().dispose();
    }
}
