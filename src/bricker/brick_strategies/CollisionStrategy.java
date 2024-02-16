package bricker.brick_strategies;

import danogl.GameObject;

/**
 * An interface defining collision behavior between game objects. Implementations of this interface are
 * responsible for defining what happens when two game objects collide.
 */
public interface CollisionStrategy {

    /**
     * Defines the action to be taken when a collision occurs between two game objects.
     *
     * @param thisObj  The game object on which the collision event is triggered.
     * @param otherObj The game object colliding with 'thisObj'.
     */
    void onCollision(GameObject thisObj, GameObject otherObj);
}
