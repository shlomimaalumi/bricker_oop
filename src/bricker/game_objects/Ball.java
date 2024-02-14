package bricker.game_objects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;

import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;


public class Ball extends GameObject {
    private final Sound collisionSound;
    private int collisionCounter;
    private static final int INITIAL_COUNTER = 0;
    private static final int COLLISION = 1;
    public static final String BALL_TAG = "Ball";


    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels). Note that (0,0) is the
     *                      top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case the GameObject
     *                      will not be rendered.
     * @param sound         the sound that will be heard during a collision
     */

    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound sound) {
        super(topLeftCorner, dimensions, renderable);
        collisionSound = sound;
        collisionCounter = INITIAL_COUNTER;
        setTag(BALL_TAG);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        if (!other.getTag().equals(Heart.HEART_TAG)) {
            System.out.println("ball collide with:" + other.getTag());
            super.onCollisionEnter(other, collision);
            Vector2 newVel = getVelocity().flipped(collision.getNormal());
            setVelocity(newVel);
            collisionSound.play();
            collisionCounter += COLLISION;
        }
    }

    public int getCollisionCounter() {
        return collisionCounter;
    }


}
