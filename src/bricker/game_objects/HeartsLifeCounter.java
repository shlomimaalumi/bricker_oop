package bricker.game_objects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Stack;

/**
 * Represents a counter for displaying the player's remaining lives using hearts.
 */
public class HeartsLifeCounter extends GameObject {
    /**
     * Size of each heart in window coordinates.
     */
    private final Vector2 heartSize;

    /**
     * Top-left corner position of the counter in window coordinates.
     */
    private final Vector2 topLeftCorner;

    private final GameObjectCollection gameObjects;

    /**
     * Renderable representing the heart image.
     */
    private final Renderable heartImage;

    /**
     * Stack of hearts to display the player's remaining lives.
     */
    private final Stack<Heart> heartsStack;

    /**
     * Counter for tracking the player's remaining lives.
     */
    private final Counter lives;

    /**
     * Tag used to identify the heart counter in the scene.
     */
    private static final String HEART_COUNTER_TAG = "Heart counter";

    /**
     * Constructs a new HeartsLifeCounter instance.
     *
     * @param topLeftCorner Top-left corner position of the counter, in window coordinates (pixels). (0,0) is the top-left corner of the window.
     * @param dimensions    Size of each heart in window coordinates.
     * @param heartImage    Renderable representing the heart image. Can be null if the counter should not be rendered.
     * @param lives         Counter for tracking the player's remaining lives.
     * @param gameObjects   Collection of game objects in the scene.
     */
    public HeartsLifeCounter(Vector2 topLeftCorner, Vector2 dimensions, Renderable heartImage, Counter lives,
                             GameObjectCollection gameObjects) {
        super(Vector2.ZERO, Vector2.ZERO, null);
        this.heartSize = dimensions;
        this.topLeftCorner = topLeftCorner;
        this.heartImage = heartImage;
        this.heartsStack = new Stack<>();
        this.gameObjects = gameObjects;

        // Build hearts based on initial lives count
        for (int i = 0; i < lives.value(); i++) {
            buildHeartNumberI(i);
        }

        this.lives = lives;
        setTag(HEART_COUNTER_TAG);
    }

    /**
     * Builds and adds a heart to the counter at the specified index.
     *
     * @param i Index of the heart to build.
     */
    public void buildHeartNumberI(int i) {
        Vector2 curPosition = topLeftCorner.add(heartSize.multX(i).multY(0));
        Heart heart = new Heart(curPosition, this.heartSize, heartImage, lives, gameObjects);
        heartsStack.push(heart);
        gameObjects.addGameObject(heart, Layer.UI);
    }

    /**
     * Removes the last heart from the counter.
     */
    private void removeHeart() {
        Heart heart = heartsStack.pop();
        this.gameObjects.removeGameObject(heart, Layer.UI);
    }

    /**
     * Updates the heart counter based on changes in the player's lives.
     *
     * @param deltaTime The time passed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        // Build hearts for additional lives
        if (this.lives.value() > heartsStack.size()) {
            buildHeartNumberI(heartsStack.size());
        }

        // Remove hearts for lost lives
        if (this.lives.value() < heartsStack.size()) {
            removeHeart();
        }
    }
}
