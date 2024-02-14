package bricker.brick_strategies;

import bricker.game_objects.Ball;
import danogl.GameManager;
import danogl.GameObject;

import danogl.collisions.GameObjectCollection;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;

public class ChangeCameraStrategy implements CollisionStrategy {
    private static final int MAX_COLLISIONS = 4;
    private final Vector2 windowDimensions;
    private final GameManager gameManager;
    private final Ball ball;
    private final BasicCollisionStrategy basicCollision;
    private static int lastHit = Integer.MIN_VALUE;

    public ChangeCameraStrategy(GameObjectCollection gameObjects,
                                Counter bricksCounter, GameManager gameManager,
                                Vector2 windowDimensions, Ball ball) {
        this.windowDimensions = windowDimensions;
        this.gameManager = gameManager;
        this.basicCollision = new BasicCollisionStrategy(gameObjects, bricksCounter);
        this.ball = ball;
    }

    public void setCameraAfterBall() {
        gameManager.setCamera(
                new Camera(
                        ball, //object to follow
                        Vector2.ZERO, //follow the center of the object
                        windowDimensions.mult(1.2f), //widen the frame a bit
                        windowDimensions //share the window dimensions
                )
        );

    }

    public static int getCollisionCounterInHit() {
        return lastHit;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        basicCollision.onCollision(thisObj, otherObj);
        if (this.gameManager.camera() == null && otherObj.getTag().equals(Ball.BALL_TAG)) {
            lastHit = ball.getCollisionCounter();
            setCameraAfterBall();
        }
    }
}
