package bricker.game_objects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Represents an extra paddle in the game, extending the Paddle class.
 */
public class ExtraPaddle extends Paddle {
    /**
     * Tag used to identify the extra paddle in collisions.
     */
    public static final String EXTRA_PADDLE_TAG = "Extra Paddle";
    /**
     * Counter to keep track of collisions with the extra paddle.
     */
    private final Counter collisionCounter;
    private final GameObjectCollection gameObjects;

    /**
     * Constructs a new ExtraPaddle instance.
     *
     * @param topLeftCorner    Position of the paddle, in window coordinates (pixels). (0,0) is the top-left
     *                         corner of the window.
     * @param dimensions       Width and height of the paddle in window coordinates.
     * @param renderable       The renderable representing the paddle. Can be null if the paddle should not
     *                         be rendered.
     * @param inputListener    The user input listener for handling paddle movement.
     * @param windowDimensions Window dimensions of the game.
     * @param disFromEnd       Represents the border width.
     * @param gameObjects      Collection of game objects in the scene.
     * @param collisionCounter Counter to keep track of collisions with the extra paddle.
     */
    public ExtraPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                       UserInputListener inputListener, Vector2 windowDimensions, int disFromEnd,
                       GameObjectCollection gameObjects, Counter collisionCounter) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions, disFromEnd);
        this.gameObjects = gameObjects;
        setTag(EXTRA_PADDLE_TAG);
        this.collisionCounter = collisionCounter;
    }

    /**
     * Handles collision events with other game objects.
     *
     * @param other     The other game object involved in the collision.
     * @param collision The collision details.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        if (other.getTag().equals(Ball.BALL_TAG) || other.getTag().equals(Puck.PUCK_TAG))
            collisionCounter.decrement();
        System.out.println("Collision number " + (4 - collisionCounter.value()));
        if (collisionCounter.value() == 0) {
            gameObjects.removeGameObject(this);
            System.out.println("Removed from collision");
        }
    }

    /**
     * Updates the state of the extra paddle.
     *
     * @param deltaTime The time passed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (collisionCounter.value() == 0) {
            gameObjects.removeGameObject(this);
            System.out.println("Removed from update");
        }
    }
}
