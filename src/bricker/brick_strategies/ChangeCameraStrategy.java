/**
 * A collision strategy implementation that changes the camera perspective upon collision with the ball.
 * This strategy is responsible for adjusting the camera to follow the ball and widening the frame upon collision,
 * providing a dynamic viewing experience.
 */
package bricker.brick_strategies;

import bricker.game_objects.Ball;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;

public class ChangeCameraStrategy implements CollisionStrategy {
    /**
     * Factor used for adjusting the window dimensions when changing the camera.
     */
    private static final float FACTOR = 1.2f;
    private final Vector2 windowDimensions;
    private final GameManager gameManager;
    private final Ball ball;
    private final BasicCollisionStrategy basicCollision;
    /**
     * The last collision counter value when a camera change was triggered.
     */
    private static int lastHit = Integer.MIN_VALUE;

    /**
     * Constructs a ChangeCameraStrategy object with the specified parameters.
     *
     * @param gameObjects      The collection of game objects in the game.
     * @param bricksCounter    The counter tracking the number of bricks in the game.
     * @param gameManager      The game manager responsible for managing the game state.
     * @param windowDimensions The dimensions of the game window.
     * @param ball             The ball game object.
     */
    public ChangeCameraStrategy(GameObjectCollection gameObjects, Counter bricksCounter,
                                GameManager gameManager, Vector2 windowDimensions, Ball ball) {
        this.windowDimensions = windowDimensions;
        this.gameManager = gameManager;
        this.basicCollision = new BasicCollisionStrategy(gameObjects, bricksCounter);
        this.ball = ball;
    }

    /**
     * Sets the camera to follow the ball and adjusts the frame upon collision.
     */
    public void setCameraAfterBall() {
        gameManager.setCamera(
                new Camera(
                        ball, // Object to follow
                        Vector2.ZERO, // Follow the center of the object
                        windowDimensions.mult(FACTOR), // Widen the frame a bit
                        windowDimensions // Share the window dimensions
                )
        );

    }

    /**
     * Retrieves the collision counter value at the last hit.
     *
     * @return The collision counter value at the last hit.
     */
    public static int getCollisionCounterInHit() {
        return lastHit;
    }

    /**
     * Handles collision events between game objects.
     * Upon collision with the ball, this strategy triggers the camera adjustment if the camera is not already set.
     *
     * @param thisObj  The game object on which the collision event is triggered.
     * @param otherObj The game object colliding with 'thisObj'.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        basicCollision.onCollision(thisObj, otherObj);
        if (this.gameManager.camera() == null && otherObj.getTag().equals(Ball.BALL_TAG)) {
            lastHit = ball.getCollisionCounter();
            setCameraAfterBall();
        }
    }
}
