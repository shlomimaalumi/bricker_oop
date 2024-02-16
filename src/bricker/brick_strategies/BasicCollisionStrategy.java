package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

/**
 * A basic implementation of the CollisionStrategy interface that handles collision events involving bricks.
 * When a collision occurs between a brick and another game object, this strategy removes the brick from the
 * game objects collection and decrements a counter tracking the number of bricks.
 */
public class BasicCollisionStrategy implements CollisionStrategy {
    private final GameObjectCollection gameObjects;
    private final Counter bricksCounter;

    /**
     * Constructs a BasicCollisionStrategy object with the specified game object collection and brick
     * counter.
     *
     * @param gameObjects   The collection of game objects in the game.
     * @param bricksCounter The counter tracking the number of bricks in the game.
     */
    public BasicCollisionStrategy(GameObjectCollection gameObjects, Counter bricksCounter) {
        this.gameObjects = gameObjects;
        this.bricksCounter = bricksCounter;
    }

    /**
     * Handles collision events between game objects. When a collision occurs between a brick and another
     * game object, and the brick is successfully removed from the game objects collection, the brick counter
     * is decremented.
     *
     * @param thisObj  The game object on which the collision event is triggered.
     * @param otherObj The game object colliding with 'thisObj'.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        if (thisObj.getTag().equals(Brick.BRICK_TAG) && gameObjects.removeGameObject(thisObj,
                Layer.STATIC_OBJECTS)) {
            bricksCounter.decrement();
        }
    }
}
