package bricker.game_objects;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class AIPaddle extends GameObject {
    private static final float PADDLE_SPEED = 200;
    private static final float THRESHOLD_FOR_MOVEMENT = 20;
    private final int  borderWidth;
    private final Vector2 windowDimensions;
    private final GameObject objectToFollow;
    public static final String AI_PAD_TAG = "AI paddle";


    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public AIPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                    GameObject objectToFollow,Vector2 windowDimensions,int disFromEnd) {
        super(topLeftCorner, dimensions, renderable);
        this.objectToFollow = objectToFollow;
        this.windowDimensions = windowDimensions;
        this.borderWidth = disFromEnd;
        setTag(AI_PAD_TAG);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDir = Vector2.ZERO;
        if(objectToFollow.getCenter().x() < getCenter().x()-THRESHOLD_FOR_MOVEMENT)
            if((getCenter().x() - getDimensions().x()/2) - borderWidth > 0)
                movementDir = Vector2.LEFT;
        if(objectToFollow.getCenter().x() > getCenter().x()+THRESHOLD_FOR_MOVEMENT)
            if((getCenter().x() + getDimensions().x()/2) + borderWidth < windowDimensions.x())
            movementDir = Vector2.RIGHT;
        setVelocity(movementDir.mult(PADDLE_SPEED));
    }
}
