package bricker.game_objects;

import danogl.GameObject;
import danogl.util.Counter;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents a counter for displaying the player's remaining lives using numeric values.
 */
public class NumericLifeCounter extends GameObject {
    private final GameObjectCollection gameObjects;

    private final Vector2 topLeftCorner;

    private final Vector2 dimensions;

    private final Counter lives;

    private int currentLives;

    private TextRenderable renderable;

    private GameObject numericCounter;

    /**
     * Tag used to identify the numeric counter in the scene.
     */
    private static final String NUMERIC_COUNTER_TAG = "Numeric counter";

    /**
     * Constructs a new NumericLifeCounter instance.
     *
     * @param topLeftCorner Top-left corner position of the counter, in window coordinates (pixels). (0,0) is the top-left corner of the window.
     * @param dimensions    Dimensions of the counter in window coordinates.
     * @param lives         Counter for tracking the player's remaining lives.
     * @param gameObjects   Collection of game objects in the scene.
     */
    public NumericLifeCounter(Vector2 topLeftCorner, Vector2 dimensions, Counter lives,
                              GameObjectCollection gameObjects) {
        super(Vector2.ZERO, Vector2.ZERO, null);
        this.lives = lives;
        this.currentLives = lives.value();
        this.renderable = new TextRenderable(String.valueOf(lives.value()));
        renderable.setColor(Color.green);
        this.numericCounter = new GameObject(topLeftCorner, dimensions, renderable);
        this.gameObjects = gameObjects;
        gameObjects.addGameObject(numericCounter, Layer.UI);
        this.topLeftCorner = topLeftCorner;
        this.dimensions = dimensions;
        setTag(NUMERIC_COUNTER_TAG);
    }

    /**
     * Updates the numeric counter based on changes in the player's lives.
     *
     * @param deltaTime The time passed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Update numeric counter if the number of lives has changed
        if (lives.value() != currentLives) {
            gameObjects.removeGameObject(numericCounter, Layer.UI);
            renderable = new TextRenderable(String.valueOf(lives.value()));
            this.numericCounter = new GameObject(topLeftCorner, dimensions, renderable);
            this.gameObjects.addGameObject(numericCounter, Layer.UI);
            Color color = switch (lives.value()) {
                case 1 -> Color.red;
                case 2 -> Color.yellow;
                default -> Color.green;
            };
            renderable.setColor(color);
            this.currentLives = lives.value();
        }

        // Update color based on the number of lives
        Color color = switch (lives.value()) {
            case 1 -> Color.red;
            case 2 -> Color.yellow;
            default -> Color.green;
        };
        renderable.setColor(color);
    }
}
