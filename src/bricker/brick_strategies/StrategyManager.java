package bricker.brick_strategies;

import java.util.ArrayList;
import java.util.Random;

import bricker.game_objects.Ball;
import bricker.game_objects.Heart;
import bricker.game_objects.Puck;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.util.Counter;
import danogl.util.Vector2;

public class StrategyManager implements CollisionStrategy {
    private static final String PUCK_IMG_PATH = "assets/mockBall.png";
    private static final String COLLISION_SOUND_PATH = "assets/blop_cut_silenced.wav";
    private static final String PADDLE_IMG_PATH = "assets/paddle.png";
    private static final int NEXT_ROUND = 1;
    private static final int LAST_STRATEGIES_CHOOSE = 2;
    private static final int SPECIEL_STRATEGIES_AMOUNT = 5;
    private static final int SPECIEL_STRATEGIES_AMOUNT_WITHOUT_DOUBLE = 4;
    private int strategyNums;
    private GameObjectCollection gameObjects;
    private Counter bricksCounter;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private float ballSpeed;
    private final float ballSize;
    private ArrayList<Puck> puckList;
    private Vector2 heartDimensions;
    private Counter livesCounter;
    private String imagePath;
    private final ArrayList<Heart> heartList;
    private Vector2 windowDimensions;
    private int distFromEnd;
    private static Counter collisionCounter;
    private final GameManager brickerGameManager;
    private Vector2 paddleDimensions;
    private String puddleImgPath;
    private UserInputListener inputListener;
    private final Ball ball;

    public StrategyManager(GameObjectCollection gameObjects, Counter bricksCounter,
                           ImageReader imageReader, SoundReader soundReader, float ballSpeed,
                           ArrayList<Puck> puckList, float ballSize,
                           Vector2 heartDimensions, Counter livesCounter, String imagePath,
                           ArrayList<Heart> heartList,
                           UserInputListener inputListener,
                           Vector2 windowDimensions,
                           int distFromEnd,
                           Vector2 paddleDimensions,
                           String puddleImgPath, Counter collisionCounter,


                           Ball ball, GameManager brickerGameManager) {
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
        this.heartList = heartList;
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.puddleImgPath = puddleImgPath;
        this.paddleDimensions = paddleDimensions;
        this.distFromEnd = distFromEnd;
        this.collisionCounter = collisionCounter;
        this.ball = ball;
        this.brickerGameManager = brickerGameManager;
        this.strategyNums = 1;
    }

    public ArrayList<CollisionStrategy> generateStrategies() {
        ArrayList<CollisionStrategy> collisionStrategies = new ArrayList<CollisionStrategy>();
        if (new Random().nextBoolean()) {
            collisionStrategies.add(basicStrategy());
        } else {
            addSpiecialsStrategies(collisionStrategies);
        }

        return collisionStrategies;
    }

    private CollisionStrategy basicStrategy() {
        return new BasicCollisionStrategy(gameObjects, bricksCounter);
    }

    private CollisionStrategy addPucksStrategy() {
        return new AddPucksStrategty(gameObjects, bricksCounter, imageReader, soundReader, ballSpeed, puckList,
                ballSize);
    }

    private CollisionStrategy extraHeartStrategy() {
        return new ExtraLifeStrategy(gameObjects, bricksCounter, imageReader, heartDimensions, imagePath, livesCounter);
    }

    private CollisionStrategy extraPaddleStrategy() {
        return new ExtraPaddleStrategy(gameObjects, windowDimensions, inputListener, imageReader
                , distFromEnd, paddleDimensions, puddleImgPath, bricksCounter, collisionCounter);
    }

    private CollisionStrategy changeCameraStrategy() {
        return new ChangeCameraStrategy(gameObjects, bricksCounter, brickerGameManager, windowDimensions, ball);
    }

    private void addSpiecialsStrategies(ArrayList<CollisionStrategy> collisionStrategies) {
        int max_index = SPECIEL_STRATEGIES_AMOUNT;
        if (this.strategyNums == LAST_STRATEGIES_CHOOSE) {
            max_index = SPECIEL_STRATEGIES_AMOUNT_WITHOUT_DOUBLE;
        }
        Random random = new Random();
        int index = random.nextInt(max_index);
        switch (index) {
            case 0 -> collisionStrategies.add(addPucksStrategy());
//            case 1 -> collisionStrategies.add(extraPaddleStrategy());
            case 1 -> collisionStrategies.add(changeCameraStrategy());
            case 2 -> collisionStrategies.add(changeCameraStrategy());
            case 3 -> collisionStrategies.add(changeCameraStrategy());
//            case 3 -> collisionStrategies.add(extraHeartStrategy());
            case 4 -> {
                strategyNums += NEXT_ROUND;
                addSpiecialsStrategies(collisionStrategies);
            }
        }
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        for (CollisionStrategy strategy : generateStrategies()) {
            strategy.onCollision(thisObj, otherObj);
        }

    }
}
