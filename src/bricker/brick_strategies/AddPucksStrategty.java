package bricker.brick_strategies;

import java.util.ArrayList;

import bricker.game_objects.Puck;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Random;


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

    private void setPuckVelocity(Puck puck) {
        Random random = new Random();
        double angle = random.nextDouble() * Math.PI;
        float velocityX = (float) Math.cos(angle) * this.puckSpeed;
        float velocityY = (float) Math.sin(angle) * this.puckSpeed;
        puck.setVelocity(new Vector2(velocityX, velocityY));
    }
}
