package bricker.brick_strategies;

import java.util.ArrayList;

import bricker.gameobjects.Puck;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Random;

/**
 * A collision strategy responsible for adding pucks to the game upon collision with certain game objects.
 * This strategy creates multiple pucks with randomized velocities and adds them to the game world.
 */
public class AddPucksStrategty implements CollisionStrategy {
    /**
     * Path to the image file used for rendering the puck.
     */
    private static final String PUCK_IMG_PATH = "assets/mockBall.png";

    /**
     * Path to the sound file played upon collision with the puck.
     */
    private static final String COLLISION_SOUND_PATH = "assets/blop_cut_silenced.wav";

    /**
     * The ratio of puck dimensions to ball dimensions, used to determine the size of the puck relative to
     * the ball.
     */
    private final static float PUCK_RATIO_FROM_BALL = 0.75f;

    /**
     * The number of pucks added to the game upon collision.
     */
    private static final int PUCKS_TO_ADD = 2;
    private final GameObjectCollection gameObjects;
    private final Renderable image;
    private final Sound sound;
    private final float puckSpeed;
    private final Vector2 puckDimensions;
    private final ArrayList<Puck> puckList;
    private final BasicCollisionStrategy basicCollision;

    /**
     * Constructs an AddPucksStrategy object with the specified parameters.
     *
     * @param gameObjects   The collection of game objects in the game.
     * @param bricksCounter The counter tracking the number of bricks in the game.
     * @param imageReader   The image reader used to load images for game objects.
     * @param soundReader   The sound reader used to load sounds for game objects.
     * @param ballSpeed     The speed of the ball in the game.
     * @param puckList      The list of puck objects in the game.
     * @param ballSize      The size of the ball in the game.
     */
    public AddPucksStrategty(GameObjectCollection gameObjects, Counter bricksCounter,
                             ImageReader imageReader, SoundReader soundReader, float ballSpeed,
                             ArrayList<Puck> puckList, float ballSize) {
        this.gameObjects = gameObjects;
        this.image = imageReader.readImage(PUCK_IMG_PATH, true);
        this.sound = soundReader.readSound(COLLISION_SOUND_PATH);
        this.puckSpeed = ballSpeed;
        this.puckList = puckList;
        this.puckDimensions = new Vector2(ballSize, ballSize).mult(PUCK_RATIO_FROM_BALL);
        basicCollision = new BasicCollisionStrategy(gameObjects, bricksCounter);
    }

    /**
     * Handles collision events between game objects. Upon collision, this strategy creates multiple pucks
     * with randomized velocities and adds them to the game world.
     *
     * @param thisObj  The game object on which the collision event is triggered.
     * @param otherObj The game object colliding with 'thisObj'.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        basicCollision.onCollision(thisObj, otherObj);
        Vector2 puckTopLeftCorner = otherObj.getTopLeftCorner();
        for (int i = 0; i < PUCKS_TO_ADD; i++) {
            Puck puck = new Puck(puckTopLeftCorner, puckDimensions, image, sound);
            setPuckVelocity(puck);
            puckList.add(puck);
            gameObjects.addGameObject(puck);
        }
    }

    /**
     * Sets a random velocity for the given puck object.
     *
     * @param puck The puck object for which the velocity is set.
     */
    private void setPuckVelocity(Puck puck) {
        Random random = new Random();
        double angle = random.nextDouble() * Math.PI;
        float velocityX = (float) Math.cos(angle) * this.puckSpeed;
        float velocityY = (float) Math.sin(angle) * this.puckSpeed;
        puck.setVelocity(new Vector2(velocityX, velocityY));
    }
}
