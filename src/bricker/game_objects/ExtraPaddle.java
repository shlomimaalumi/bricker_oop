package bricker.game_objects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class ExtraPaddle extends Paddle {
    public static final String EXTRA_PADDLE_TAG = "Extra Paddle";
    private final Counter collisionCounter;

    private final GameObjectCollection gameObjects;


    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner    Position of the object, in window coordinates (pixels). Note that (0,0) is the
     *                         top-left corner of the window.
     * @param dimensions       Width and height in window coordinates.
     * @param renderable       The renderable representing the object. Can be null, in which case the
     *                         GameObject will not be rendered.
     * @param inputListener    TODO document
     * @param windowDimensions window dimension of the game
     * @param disFromEnd       represent the border width
     */
    public ExtraPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                       UserInputListener inputListener, Vector2 windowDimensions, int disFromEnd
            , GameObjectCollection gameObjects, Counter collisionCounter) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions, disFromEnd);
        this.gameObjects = gameObjects;
        setTag(EXTRA_PADDLE_TAG);
        this.collisionCounter = collisionCounter;

    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        if (other.getTag().equals(Ball.BALL_TAG) || other.getTag().equals(Puck.PUCK_TAG))
            collisionCounter.decrement();
        System.out.println("coliision number " + (4 - collisionCounter.value()));
        if (collisionCounter.value() == 0) {
            gameObjects.removeGameObject(this);
            System.out.println("remove from collision");
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (collisionCounter.value() == 0) {
            gameObjects.removeGameObject(this);
            System.out.println("remove from update");
        }
    }
}
