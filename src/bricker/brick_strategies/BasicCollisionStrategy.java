package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

public class BasicCollisionStrategy implements CollisionStrategy{

    private final GameObjectCollection gameObjects;
    private final Counter bricksCounter;

    public BasicCollisionStrategy (GameObjectCollection gameObjects, Counter bricksCounter) {
        this.gameObjects = gameObjects;
        this.bricksCounter=bricksCounter;
    }
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {

        bricksCounter.decrement();
        gameObjects.removeGameObject(thisObj, Layer.STATIC_OBJECTS);
    }
}
