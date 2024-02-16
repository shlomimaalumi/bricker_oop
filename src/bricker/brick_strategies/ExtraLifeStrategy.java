package bricker.brick_strategies;

import bricker.gameobjects.Heart;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.ArrayList;

/**
 * A collision strategy implementation that triggers the creation of an extra life object upon collision.
 * When a collision occurs with an object associated with this strategy, an extra life object is instantiated
 * and added to the game.
 */
public class ExtraLifeStrategy implements CollisionStrategy {
    /**
     * Half value used for calculating the position of the heart object.
     */
    private static final float HALF = 0.5f;
    private final ImageRenderable image;
    private final GameObjectCollection gameObjects;
    private final Vector2 heartDimensions;
    /**
     * Vertical velocity component for the heart object.
     */
    private static final int VEL_Y = 100;

    /**
     * Horizontal velocity component for the heart object.
     */
    private static final int VEL_X = 0;

    /**
     * Velocity vector for the heart object.
     */
    private static final Vector2 HEART_VELOCITY = new Vector2(VEL_X, VEL_Y);
    private final Counter livesCounter;
    private final BasicCollisionStrategy basicCollision;
    private final ArrayList<Heart> heartsList;

    /**
     * Constructs an ExtraLifeStrategy object with the specified parameters.
     *
     * @param gameObjects     The collection of game objects in the game.
     * @param bricksCounter   The counter tracking the number of bricks in the game.
     * @param imageReader     The image reader used to load the image for the extra life object.
     * @param heartDimensions The dimensions of the extra life object.
     * @param heartImgPath    The file path to the image for the extra life object.
     * @param livesCounter    The counter tracking the number of lives in the game.
     * @param heartsList      The list of extra life objects in the game.
     */
    public ExtraLifeStrategy(GameObjectCollection gameObjects, Counter bricksCounter,
                             ImageReader imageReader, Vector2 heartDimensions, String heartImgPath,
                             Counter livesCounter, ArrayList<Heart> heartsList) {
        this.gameObjects = gameObjects;
        this.livesCounter = livesCounter;
        this.image = imageReader.readImage(heartImgPath, true);
        this.heartDimensions = heartDimensions;
        basicCollision = new BasicCollisionStrategy(gameObjects, bricksCounter);
        this.heartsList = heartsList;
    }

    /**
     * Handles collision events between game objects. Upon collision, an extra life object is created at the
     * collision point and added to the game objects collection.
     *
     * @param thisObj  The game object on which the collision event is triggered.
     * @param otherObj The game object colliding with 'thisObj'.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        basicCollision.onCollision(thisObj, otherObj);
        Vector2 topLeftCorner = thisObj.getCenter().add(heartDimensions.multX(-HALF).multY(0));
        Heart heart = new Heart(topLeftCorner, heartDimensions, image, livesCounter,
                gameObjects, HEART_VELOCITY);
        heartsList.add(heart);
        gameObjects.addGameObject(heart);
    }
}
