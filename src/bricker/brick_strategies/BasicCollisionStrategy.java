package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;

public class BasicCollisionStrategy implements CollisionStrategy{

    private final GameObjectCollection gameObjects;

    public BasicCollisionStrategy (GameObjectCollection gameObjects) {
        this.gameObjects = gameObjects;
    }
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        System.out.println("collision with brick detected");
        gameObjects.removeGameObject(thisObj, Layer.STATIC_OBJECTS);
    }
}
