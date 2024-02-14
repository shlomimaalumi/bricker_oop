package bricker.brick_strategies;

import bricker.game_objects.ExtraPaddle;
import bricker.game_objects.Heart;
import bricker.game_objects.Paddle;
import bricker.game_objects.Puck;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.util.Counter;
import danogl.util.Vector2;
import danogl.gui.rendering.ImageRenderable;

import java.util.ArrayList;


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



    public ExtraPaddleStrategy(GameObjectCollection gameObjects, Vector2 windowDimensions,
                               UserInputListener inputListener,
                               ImageReader imageReader, int disFromEnd, Vector2 paddleDimensions,
                               String paddleImgPath, Counter bricksCounter, Counter collisionCounter) {
        super();
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

    private Vector2 getExtraPaddleLocation() {
        float x = windowDimensions.x() / 2 - paddleDimensions.x() / 2;
        float y = windowDimensions.y() / 2 - paddleDimensions.y() / 2;
        return new Vector2(x, y);
    }


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