package bricker.brick_strategies;

import bricker.game_objects.ExtraPaddle;
import bricker.game_objects.Paddle;
import danogl.GameObject;
import bricker.game_objects.Brick;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.util.Counter;
import danogl.util.Vector2;
import danogl.gui.rendering.ImageRenderable;


public class ExtraPaddleStrategy implements CollisionStrategy {
    private static final int NO_COLLISIONS = 0;
    private static final int MAX_COLLISIONS = 4;
    private GameObjectCollection gameObjects;
    private final Vector2 paddleLocation;
    private final Vector2 windowDimensions;
    private final UserInputListener inputListener;
    private final int disFromEnd;
    private final Vector2 paddleDimensions;
    private final Counter bricksCounter;
    private final ImageReader imageReader;
    private Paddle paddle;
    private static Paddle LOGGER_PADDLE = null;
    private static Counter collisionCounter = null;
    private final ImageRenderable image;


    public ExtraPaddleStrategy(GameObjectCollection gameObjects, Vector2 windowDimensions,
                               UserInputListener inputListener,
                               ImageReader imageReader, int disFromEnd, Vector2 paddleDimensions,
                               String paddleImgPath, Counter bricksCounter, Counter collisionCounter) {
        super();
        this.gameObjects = gameObjects;
        this.windowDimensions = windowDimensions;
        this.inputListener = inputListener;
        this.paddleDimensions = paddleDimensions;
        this.bricksCounter = bricksCounter;
        this.paddleLocation = getExtraPaddleLocation();
        this.disFromEnd = disFromEnd;
        this.imageReader = imageReader;
        this.image = imageReader.readImage(paddleImgPath, false);
        if (this.collisionCounter == null) {
            this.collisionCounter = collisionCounter;
        }


    }

    private Vector2 getExtraPaddleLocation() {
        float x = windowDimensions.x() / 2 - paddleDimensions.x() / 2;
        float y = windowDimensions.y() / 2 - paddleDimensions.y() / 2;
        return new Vector2(x, y);
    }


    public void onCollision(GameObject thisObj, GameObject otherObj) {
        if (thisObj.getTag().equals(Brick.BRICK_TAG) && gameObjects.removeGameObject(thisObj, Layer.STATIC_OBJECTS)) {
            bricksCounter.decrement();
        }
        if (collisionCounter.value() == NO_COLLISIONS) {
            paddle = new ExtraPaddle(paddleLocation, paddleDimensions, image, inputListener, windowDimensions,
                    disFromEnd, gameObjects, collisionCounter);
            collisionCounter.increaseBy(MAX_COLLISIONS);
            gameObjects.addGameObject(paddle);
        }
    }

}