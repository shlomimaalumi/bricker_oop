package bricker.gameobjects;

import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.ArrayList;

/**
 * Represents a brick in the game. Extends GameObject class.
 */
public class Brick extends GameObject {
    /**
     * Tag used to identify the brick in collisions.
     */
    public static final String BRICK_TAG = "Brick";

    /**
     * List of collision strategies applied to the brick on collision with other game objects.
     */
    private final ArrayList<CollisionStrategy> collisionStrategies;

    /**
     * Constructs a new Brick instance.
     *
     * @param topLeftCorner       Position of the brick, in window coordinates (pixels). (0,0) is the
     *                            top-left corner of the window.
     * @param dimensions          Width and height of the brick in window coordinates.
     * @param renderable          The renderable representing the brick. Can be null if the brick should not
     *                            be rendered.
     * @param collisionStrategies List of collision strategies applied to the brick.
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 ArrayList<CollisionStrategy> collisionStrategies) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategies = collisionStrategies;
        setTag(BRICK_TAG);
    }

    /**
     * Handles collision events with other game objects.
     *
     * @param other     The other game object involved in the collision.
     * @param collision The collision details.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        for (CollisionStrategy strategy : collisionStrategies) {
            strategy.onCollision(this, other);
        }
    }
}
