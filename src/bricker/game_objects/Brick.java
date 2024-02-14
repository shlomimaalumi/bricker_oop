package bricker.game_objects;

import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.ArrayList;

public class Brick extends GameObject {
    public static final String BRICK_TAG = "Brick";


    private ArrayList<CollisionStrategy> collisionStrategies;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels). Note that (0,0) is the
     *                      top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case the GameObject
     *                      will not be rendered.
     */

    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 ArrayList<CollisionStrategy> collisionStrategies) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategies = collisionStrategies;
        setTag(BRICK_TAG);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        for (CollisionStrategy strategy : collisionStrategies)
            strategy.onCollision(this, other);

//        collisionStrategy.onCollision(this,other);

    }


}

