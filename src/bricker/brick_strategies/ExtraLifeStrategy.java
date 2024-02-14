package bricker.brick_strategies;

import bricker.game_objects.Brick;
import bricker.game_objects.Heart;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.ArrayList;

public class ExtraLifeStrategy implements CollisionStrategy {
    private static final float HALF = 0.5f;
    private final ImageRenderable image;
    private GameObjectCollection gameObjects;
    private final Vector2 heartDimensions;
    private Counter bricksCounter;
    private static final int VEL_Y = 100;
    private static final int VEL_X = 0;
    private static final Vector2 HEART_VELOCITY = new Vector2(VEL_X, VEL_Y);
    private Counter livesCounter;
    private BasicCollisionStrategy basicCollision;

    public ExtraLifeStrategy(GameObjectCollection gameObjects, Counter bricksCounter,
                             ImageReader imageReader, Vector2 heartDimensions, String heartImgPath,
                             Counter livesCounter,ArrayList<Heart> heartsList) {
        super();
        this.bricksCounter = bricksCounter;
        this.gameObjects = gameObjects;
        this.livesCounter = livesCounter;
        this.image = imageReader.readImage(heartImgPath, true);
        this.heartDimensions = heartDimensions;
        basicCollision = new BasicCollisionStrategy(gameObjects, bricksCounter);
        this.heartsList = heartsList;
    }


    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        basicCollision.onCollision(thisObj, otherObj);
        Vector2 topLeftCorner = thisObj.getCenter().add(heartDimensions.multX(-HALF).multY(0));
        Heart heart = new Heart(topLeftCorner, heartDimensions, image,
                livesCounter
                , gameObjects,
                HEART_VELOCITY);
        gameObjects.addGameObject(heart);

    }


}
