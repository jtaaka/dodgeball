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

public class CenterOfPlayer extends Sprite {
    private Array<CenterOfPlayerObject> center;
    private ShapeRenderer shape;
    private Vector2 calculatedCenter;
    private Vector2 lastPoint;

    public CenterOfPlayer() {
        super(new Texture("peruspalloTESTI.png"));
        setSize(getWidth() / 300f, getHeight() / 300f);
        setOriginCenter();
        center = new Array<CenterOfPlayerObject>();
        shape = new ShapeRenderer();
        calculatedCenter = new Vector2();
        lastPoint = new Vector2();
    }

    public void modify(float x, float y) {
        final CenterOfPlayerObject object = new CenterOfPlayerObject(x / 3f, y / 3f);
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
            point = new Vector2(Dodgeball.WORLD_WIDTH / 2f + x,Dodgeball.WORLD_HEIGHT / 2f + y);   //OG point = new Vector2(round(x), round(y));
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

    public void calculatedCenter(Vector2 calculatedCenter) {
        this.calculatedCenter = calculatedCenter;
    }

    public void draw(Batch batch, Camera camera) {
        lastPoint.x = Dodgeball.WORLD_WIDTH / 2f;
        lastPoint.y = Dodgeball.WORLD_HEIGHT / 2f;

        for (CenterOfPlayerObject heat : center) {
            shape.setProjectionMatrix(camera.combined);
            shape.begin(ShapeRenderer.ShapeType.Line);
            shape.line(heat.point, lastPoint);
            lastPoint = heat.point;
            shape.end();
        }

        setColor(1,0,0,1);
        setPosition(Dodgeball.WORLD_WIDTH / 2f + calculatedCenter.x / 3f - getWidth() / 2f, Dodgeball.WORLD_HEIGHT / 2f + calculatedCenter.y / 3f - getHeight() / 2f);
        //setPosition(Dodgeball.WORLD_WIDTH / 2f, Dodgeball.WORLD_HEIGHT / 2f);
        batch.begin();
        draw(batch);
        batch.end();
        //Gdx.app.log(getClass().getSimpleName(), "x: " + (Dodgeball.WORLD_WIDTH / 2f + calculatedCenter.x - getWidth() / 2f) + " y: " + (Dodgeball.WORLD_HEIGHT / 2f + calculatedCenter.y - getHeight() / 2f));
    }

    public void dispose() {
        getTexture().dispose();
    }
}
