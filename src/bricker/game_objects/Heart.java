package bricker.game_objects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class Heart extends GameObject {
    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels). Note that (0,0) is the
     * top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case the GameObject
     * will not be rendered.
     */

    public static final String HEART_TAG = "Heart";
    private static final int MAX_SIZE = 4;
    private Counter lives;
    private final GameObjectCollection gameObjects;

    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Counter lives,
                 GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable);
        this.lives = lives;
        this.gameObjects = gameObjects;
        setTag(HEART_TAG);
    }

    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable
            , Counter lives, GameObjectCollection gameObjects, Vector2 velocity) {
        this(topLeftCorner, dimensions, renderable, lives, gameObjects);
        setVelocity(velocity);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(Paddle.PADDLE_TAG)) {
            addLife();
            gameObjects.removeGameObject(this);
        }
    }

    private void addLife() {
        if (lives.value() < MAX_SIZE) {
            lives.increment();
        }
    }

}
