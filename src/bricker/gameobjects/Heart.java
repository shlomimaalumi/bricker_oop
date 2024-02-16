package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Represents a heart object in the game, extending the GameObject class.
 */
public class Heart extends GameObject {
    /**
     * Tag used to identify the heart in collisions.
     */
    public static final String HEART_TAG = "Heart";

    /**
     * Maximum size for the heart (maximum lives).
     */
    private static final int MAX_SIZE = 4;

    /**
     * Counter to keep track of the player's lives.
     */
    private final Counter lives;

    private final GameObjectCollection gameObjects;

    /**
     * Constructs a new Heart instance.
     *
     * @param topLeftCorner Position of the heart, in window coordinates (pixels). (0,0) is the top-left
     *                      corner of the window.
     * @param dimensions    Width and height of the heart in window coordinates.
     * @param renderable    The renderable representing the heart. Can be null if the heart should not be
     *                      rendered.
     * @param lives         Counter to keep track of the player's lives.
     * @param gameObjects   Collection of game objects in the scene.
     */
    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Counter lives,
                 GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable);
        this.lives = lives;
        this.gameObjects = gameObjects;
        setTag(HEART_TAG);
    }

    /**
     * Constructs a new Heart instance with an initial velocity.
     *
     * @param topLeftCorner Position of the heart, in window coordinates (pixels). (0,0) is the top-left
     *                      corner of the window.
     * @param dimensions    Width and height of the heart in window coordinates.
     * @param renderable    The renderable representing the heart. Can be null if the heart should not be
     *                      rendered.
     * @param lives         Counter to keep track of the player's lives.
     * @param gameObjects   Collection of game objects in the scene.
     * @param velocity      Initial velocity of the heart.
     */
    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 Counter lives, GameObjectCollection gameObjects, Vector2 velocity) {
        this(topLeftCorner, dimensions, renderable, lives, gameObjects);
        setVelocity(velocity);
    }

    /**
     * Handles collision events with other game objects.
     *
     * @param other     The other game object involved in the collision.
     * @param collision The collision details.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(Paddle.PADDLE_TAG)) {
            addLife();
            gameObjects.removeGameObject(this);
        }
    }

    /**
     * Increases the player's lives by one if it is below the maximum size.
     */
    private void addLife() {
        if (lives.value() < MAX_SIZE) {
            lives.increment();
        }
    }
}
