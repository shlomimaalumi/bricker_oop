package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a puck in the game, extending the functionality of the Ball class.
 */
public class Puck extends Ball {
    /**
     * Tag used to identify the puck in the scene.
     */
    public static final String PUCK_TAG = "Puck";

    /**
     * Constructs a new Puck instance.
     *
     * @param topLeftCorner Position of the puck, in window coordinates (pixels). Note that (0,0) is the
     *                      top-left corner of the window.
     * @param dimensions    Dimensions of the puck in window coordinates.
     * @param renderable    Renderable representing the puck. Can be null, in which case the GameObject will
     *                      not be rendered.
     * @param sound         Sound that will be heard during a collision with the puck.
     */
    public Puck(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound sound) {
        super(topLeftCorner, dimensions, renderable, sound);
        setTag(PUCK_TAG);
        // Additional setup for the Puck instance, if needed.
    }

    /**
     * Handles collision events when the puck collides with another game object.
     *
     * @param other     The other game object involved in the collision.
     * @param collision Details of the collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        // Additional logic to handle collisions involving the puck.
    }
}
