package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * The `Paddle` class represents a game object that serves as a player-controlled paddle in a game scene. It
 * extends the `GameObject` class and includes functionality for user input handling to move the paddle left
 * or right within specified boundaries.
 * The paddle has a defined movement speed, and its position is updated based on user input.
 */
public class Paddle extends GameObject {
    /**
     * Tag used to identify the paddle in the scene.
     */
    public static final String PADDLE_TAG = "Paddle";

    /**
     * Movement speed of the paddle in pixels per second.
     */
    private static final float MOVEMENT_SPEED = 400;
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;
    private final int borderWidth;


    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels). Note that (0,0) is the
     *                      top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case the GameObject
     *                      will not be rendered.
     * @param inputListener TODO document
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener, Vector2 windowDimensions, int disFromEnd) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.borderWidth = disFromEnd;
        setTag(PADDLE_TAG);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDir = Vector2.ZERO;
        if (this.inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            // We can only go left if we're less than ${minDistFromEdge} pixels away from the left edge
            if ((getCenter().x() - getDimensions().x() / 2) - borderWidth > 0) {
                movementDir = movementDir.add(Vector2.LEFT);
            }
        }

        if (this.inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            // We can only go right if we're less than ${minDistFromEdge} pixels away from the right edge
            if ((getCenter().x() + getDimensions().x() / 2) +
                    borderWidth < windowDimensions.x()) {
                movementDir = movementDir.add(Vector2.RIGHT);
            }
        }
        setVelocity(movementDir.mult(MOVEMENT_SPEED));

    }
}
