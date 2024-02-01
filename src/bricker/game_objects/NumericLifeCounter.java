package bricker.game_objects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class NumericLifeCounter extends GameObject {
    private final TextRenderable textRenderable;
    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    private int lives;

    public NumericLifeCounter(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, int lives,
                              GameObjectCollection objectCollection) {
        super(topLeftCorner, dimensions, renderable);
        this.lives=lives;
        this.textRenderable=new TextRenderable(String.valueOf(lives));
        textRenderable.setColor(Color.green);
        GameObject numericCounter =new GameObject(topLeftCorner,dimensions, textRenderable);
        objectCollection.addGameObject(numericCounter, Layer.UI);
//        drawGraphic();
    }


    private void drawGraphic(){
        Color color;
        switch (lives) {
            case 1:
                color = Color.red;
                break;
            case 2:
                color = Color.yellow;
                break;
            default:
                color = Color.green;
        }
        textRenderable.setString(String.valueOf(lives));
        textRenderable.setColor(color);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);


    }
}
