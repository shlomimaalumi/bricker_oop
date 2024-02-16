package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a ball in the game. Extends GameObject class.
 */
public class Ball extends GameObject {
    /**
     * Initial value for the collision counter.
     */
    private static final int INITIAL_COUNTER = 0;

    /**
     * Value to increment the collision counter on each collision.
     */
    private static final int COLLISION = 1;

    /**
     * Tag used to identify the ball in collisions.
     */
    public static final String BALL_TAG = "Ball";
    private final Sound collisionSound;
    private int collisionCounter;


    /**
     * Constructs a new Ball instance.
     *
     * @param topLeftCorner Position of the ball, in window coordinates (pixels). (0,0) is the top-left
     *                      corner of the window.
     * @param dimensions    Width and height of the ball in window coordinates.
     * @param renderable    The renderable representing the ball. Can be null if the ball should not be
     *                      rendered.
     * @param sound         The sound played during a collision.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound sound) {
        super(topLeftCorner, dimensions, renderable);
        collisionSound = sound;
        collisionCounter = INITIAL_COUNTER;
        setTag(BALL_TAG);
    }

    /**
     * Handles collision events with other game objects.
     *
     * @param other     The other game object involved in the collision.
     * @param collision The collision details.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        if (!other.getTag().equals(Heart.HEART_TAG)) {
            super.onCollisionEnter(other, collision);
            Vector2 newVel = getVelocity().flipped(collision.getNormal());
            setVelocity(newVel);
            collisionSound.play();
            collisionCounter += COLLISION;
        }
    }

    /**
     * Gets the current collision counter value.
     *
     * @return The number of collisions the ball has experienced.
     */
    public int getCollisionCounter() {
        return collisionCounter;
    }
}
