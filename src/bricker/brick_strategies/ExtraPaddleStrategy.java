/**
 * A collision strategy implementation that triggers the creation of an extra paddle object upon collision.
 * This strategy allows adding an extra paddle to the game under certain conditions, enhancing gameplay dynamics.
 */
package bricker.brick_strategies;

import bricker.game_objects.ExtraPaddle;
import bricker.game_objects.Paddle;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.util.Counter;
import danogl.util.Vector2;
import danogl.gui.rendering.ImageRenderable;

public class ExtraPaddleStrategy implements CollisionStrategy {
    private static final int NO_COLLISIONS = 0;
    private static final int MAX_COLLISIONS = 4;
    private final GameObjectCollection gameObjects;
    private final Vector2 paddleLocation;
    private final Vector2 windowDimensions;
    private final UserInputListener inputListener;
    private final int disFromEnd;
    private final Vector2 paddleDimensions;
    private static Counter collisionCounter = null;
    private final ImageRenderable image;
    private final BasicCollisionStrategy basicCollision;

    /**
     * Constructs an ExtraPaddleStrategy object with the specified parameters.
     *
     * @param gameObjects       The collection of game objects in the game.
     * @param windowDimensions The dimensions of the game window.
     * @param inputListener    The user input listener for controlling the paddle.
     * @param imageReader      The image reader used to load the image for the extra paddle object.
     * @param disFromEnd       The distance from the bottom of the window to place the extra paddle.
     * @param paddleDimensions The dimensions of the extra paddle object.
     * @param paddleImgPath    The file path to the image for the extra paddle object.
     * @param bricksCounter    The counter tracking the number of bricks in the game.
     * @param collisionCounter The counter tracking the number of collisions.
     */
    public ExtraPaddleStrategy(GameObjectCollection gameObjects, Vector2 windowDimensions,
                               UserInputListener inputListener, ImageReader imageReader,
                               int disFromEnd, Vector2 paddleDimensions, String paddleImgPath,
                               Counter bricksCounter, Counter collisionCounter) {
        this.gameObjects = gameObjects;
        this.windowDimensions = windowDimensions;
        this.inputListener = inputListener;
        this.paddleDimensions = paddleDimensions;
        this.paddleLocation = getExtraPaddleLocation();
        this.disFromEnd = disFromEnd;
        this.image = imageReader.readImage(paddleImgPath, false);
        if (ExtraPaddleStrategy.collisionCounter == null) {
            ExtraPaddleStrategy.collisionCounter = collisionCounter;
        }
        basicCollision = new BasicCollisionStrategy(gameObjects, bricksCounter);
    }

    /**
     * Calculates the position to place the extra paddle within the game window.
     *
     * @return The vector representing the position of the extra paddle.
     */
    private Vector2 getExtraPaddleLocation() {
        float x = windowDimensions.x() / 2 - paddleDimensions.x() / 2;
        float y = windowDimensions.y() / 2 - paddleDimensions.y() / 2;
        return new Vector2(x, y);
    }

    /**
     * Handles collision events between game objects.
     * Upon collision, if there are no previous collisions, an extra paddle object is created and added to the game.
     *
     * @param thisObj  The game object on which the collision event is triggered.
     * @param otherObj The game object colliding with 'thisObj'.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        basicCollision.onCollision(thisObj, otherObj);
        if (collisionCounter.value() == NO_COLLISIONS) {
            Paddle paddle = new ExtraPaddle(paddleLocation, paddleDimensions, image, inputListener,
                    windowDimensions, disFromEnd, gameObjects, collisionCounter);
            collisionCounter.increaseBy(MAX_COLLISIONS);
            gameObjects.addGameObject(paddle);
        }
    }
}
