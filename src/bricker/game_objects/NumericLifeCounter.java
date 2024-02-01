package bricker.game_objects;

import danogl.GameObject;
import danogl.util.Counter;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class NumericLifeCounter extends GameObject {
    private  TextRenderable renderable;
    private  GameObject numericCounter;
    private Counter lives;

//    private final TextRenderable textRenderable;
    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     */

    public NumericLifeCounter(Vector2 topLeftCorner, Vector2 dimensions ,Counter lives,GameObjectCollection gameObjects) {
        super(Vector2.ZERO, Vector2.ZERO, null);
        this.lives = lives;
        this.renderable = new TextRenderable(String.valueOf(lives.value()));
        renderable.setColor(Color.green);
        this.numericCounter = new GameObject(topLeftCorner, dimensions, renderable);
        gameObjects.addGameObject(numericCounter, Layer.UI);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
//        renderable=new TextRenderable(String.valueOf(lives.value()));
        Color color = switch (lives.value()) {
            case 1 -> Color.red;
            case 2 -> Color.yellow;
            default -> Color.green;
        };
        renderable.setColor(color);
    }
}
