package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.gameobjects.Heart;
import bricker.gameobjects.Puck;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.ArrayList;
import java.util.Random;

/**
 * A factory class responsible for generating collision strategies based on certain conditions. This class
 * dynamically creates collision strategies to introduce various gameplay dynamics and interactions between
 * game objects.
 */
public class StrategyFactory implements CollisionStrategy {
    /**
     * Constant representing the increment to the strategy number for the next round of special strategies.
     */
    private static final int NEXT_ROUND = 1;

    /**
     * Constant representing the initial value for choosing strategies.
     */
    private static final int INITIAL_STRATEGIES_CHOOSE = 1;

    /**
     * Constant representing the value for the last round of choosing strategies without the possibility of a
     * double strategy.
     */
    private static final int LAST_STRATEGIES_CHOOSE = 3;

    /**
     * Constant representing the total number of special strategies available, including the possibility of a
     * double strategy in a round.
     */
    private static final int SPECIAL_STRATEGIES_AMOUNT = 5;

    /**
     * Constant representing the number of special strategies available in the last round, without the
     * possibility of a double strategy.
     */
    private static final int SPECIAL_STRATEGIES_AMOUNT_WITHOUT_DOUBLE = 4;
    /**
     * counter to count the amount of collisions of the ball with the extra paddle
     */
    private static Counter collisionCounter;
    private int strategyNums;
    private final GameObjectCollection gameObjects;
    private final Counter bricksCounter;
    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private final float ballSpeed;
    private final float ballSize;
    private final ArrayList<Puck> puckList;
    private final Vector2 heartDimensions;
    private final Counter livesCounter;
    private final String imagePath;
    private final Vector2 windowDimensions;
    private final int distFromEnd;
    private final ArrayList<Heart> heartsList;
    private final GameManager brickerGameManager;
    private final Vector2 paddleDimensions;
    private final String paddleImgPath;
    private final UserInputListener inputListener;
    private final Ball ball;

    /**
     * Constructs a StrategyFactory object with the specified parameters.
     *
     * @param gameObjects        The collection of game objects in the game.
     * @param bricksCounter      The counter tracking the number of bricks in the game.
     * @param imageReader        The image reader used to load images for game objects.
     * @param soundReader        The sound reader used to load sounds for game objects.
     * @param ballSpeed          The speed of the ball in the game.
     * @param puckList           The list of puck objects in the game.
     * @param ballSize           The size of the ball in the game.
     * @param heartDimensions    The dimensions of the heart object.
     * @param livesCounter       The counter tracking the number of lives in the game.
     * @param imagePath          The file path to the image for the heart object.
     * @param inputListener      The user input listener for controlling game objects.
     * @param windowDimensions   The dimensions of the game window.
     * @param distFromEnd        The distance from the bottom of the window to place game objects.
     * @param paddleDimensions   The dimensions of the paddle object.
     * @param paddleImgPath      The file path to the image for the paddle object.
     * @param collisionCounter   The counter tracking the number of collisions.
     * @param ball               The ball object in the game.
     * @param heartsList         The list of heart objects in the game.
     * @param brickerGameManager The game manager responsible for managing the game state.
     */
    public StrategyFactory(GameObjectCollection gameObjects, Counter bricksCounter,
                           ImageReader imageReader, SoundReader soundReader, float ballSpeed,
                           ArrayList<Puck> puckList, float ballSize,
                           Vector2 heartDimensions, Counter livesCounter, String imagePath,
                           UserInputListener inputListener, Vector2 windowDimensions, int distFromEnd,
                           Vector2 paddleDimensions, String paddleImgPath, Counter collisionCounter,
                           Ball ball, ArrayList<Heart> heartsList, GameManager brickerGameManager) {
        this.gameObjects = gameObjects;
        this.bricksCounter = bricksCounter;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.ballSpeed = ballSpeed;
        this.puckList = puckList;
        this.ballSize = ballSize;
        this.heartDimensions = heartDimensions;
        this.livesCounter = livesCounter;
        this.imagePath = imagePath;
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.paddleImgPath = paddleImgPath;
        this.paddleDimensions = paddleDimensions;
        this.distFromEnd = distFromEnd;
        StrategyFactory.collisionCounter = collisionCounter;
        this.heartsList = heartsList;
        this.ball = ball;
        this.brickerGameManager = brickerGameManager;
        this.strategyNums = INITIAL_STRATEGIES_CHOOSE;
    }

    /**
     * Generates collision strategies based on certain conditions.
     *
     * @return An ArrayList containing the generated collision strategies.
     */
    public ArrayList<CollisionStrategy> generateStrategies() {
        ArrayList<CollisionStrategy> collisionStrategies = new ArrayList<>();
        if (new Random().nextBoolean()) {
            collisionStrategies.add(basicStrategy());
        } else {
            addSpecialStrategies(collisionStrategies);
        }


        return collisionStrategies;
    }

    /**
     * Creates and returns a basic collision strategy with the current game objects and bricks counter.
     *
     * @return A basic collision strategy.
     */
    private CollisionStrategy basicStrategy() {
        return new BasicCollisionStrategy(gameObjects, bricksCounter);
    }

    /**
     * Creates and returns a collision strategy for adding pucks to the game with the specified parameters.
     *
     * @return A collision strategy for adding pucks.
     */
    private CollisionStrategy addPucksStrategy() {
        return new AddPucksStrategty(gameObjects, bricksCounter, imageReader, soundReader, ballSpeed,
                puckList, ballSize);
    }

    /**
     * Creates and returns a collision strategy for granting extra life with the specified parameters.
     *
     * @return A collision strategy for granting extra life.
     */
    private CollisionStrategy extraHeartStrategy() {
        return new ExtraLifeStrategy(gameObjects, bricksCounter, imageReader, heartDimensions, imagePath,
                livesCounter, heartsList);
    }

    /**
     * Creates and returns a collision strategy for adding an extra paddle with the specified parameters.
     *
     * @return A collision strategy for adding an extra paddle.
     */
    private CollisionStrategy extraPaddleStrategy() {
        return new ExtraPaddleStrategy(gameObjects, windowDimensions, inputListener, imageReader
                , distFromEnd, paddleDimensions, paddleImgPath, bricksCounter, collisionCounter);
    }

    private CollisionStrategy changeCameraStrategy() {
        return new ChangeCameraStrategy(gameObjects, bricksCounter, brickerGameManager, windowDimensions,
                ball);
    }

    /**
     * Creates and adds special collision strategies to the given list based on certain conditions.
     *
     * @param collisionStrategies The list to which special collision strategies are added.
     */
    private void addSpecialStrategies(ArrayList<CollisionStrategy> collisionStrategies) {
        int max_index = SPECIAL_STRATEGIES_AMOUNT;
        if (this.strategyNums == LAST_STRATEGIES_CHOOSE) {
            max_index = SPECIAL_STRATEGIES_AMOUNT_WITHOUT_DOUBLE;
        }
        Random random = new Random();
        int index = random.nextInt(max_index);
        switch (index) {
            case 0 -> collisionStrategies.add(addPucksStrategy());
            case 1 -> collisionStrategies.add(extraPaddleStrategy());
            case 2 -> collisionStrategies.add(changeCameraStrategy());
            case 3 -> collisionStrategies.add(extraHeartStrategy());
            case 4 -> {
                strategyNums += NEXT_ROUND;
                addSpecialStrategies(collisionStrategies);
                addSpecialStrategies(collisionStrategies);
            }
        }
    }


    /**
     * Handles collision events between game objects. For each generated collision strategy, this method
     * triggers the onCollision method to handle the collision.
     *
     * @param thisObj  The game object on which the collision event is triggered.
     * @param otherObj The game object colliding with 'thisObj'.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        for (CollisionStrategy strategy : generateStrategies()) {
            strategy.onCollision(thisObj, otherObj);
        }
    }
}
