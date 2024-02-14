package bricker.brick_strategies;

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

import java.util.ArrayList;
import java.util.Random;

public class StrategyFactory implements CollisionStrategy {
    private static final int NEXT_ROUND = 1;
    private static final int LAST_STRATEGIES_CHOOSE = 2;
    private static final int SPECIAL_STRATEGIES_AMOUNT = 5;
    private static final int SPECIAL_STRATEGIES_AMOUNT_WITHOUT_DOUBLE = 4;
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
    private static Counter collisionCounter;
    private final ArrayList<Heart> hearsList;
    private final GameManager brickerGameManager;
    private final Vector2 paddleDimensions;
    private final String puddleImgPath;
    private final UserInputListener inputListener;
    private final Ball ball;

    public StrategyFactory(GameObjectCollection gameObjects, Counter bricksCounter,
                           ImageReader imageReader, SoundReader soundReader, float ballSpeed,
                           ArrayList<Puck> puckList, float ballSize,
                           Vector2 heartDimensions, Counter livesCounter, String imagePath,
                           UserInputListener inputListener,
                           Vector2 windowDimensions,
                           int distFromEnd,
                           Vector2 paddleDimensions,
                           String puddleImgPath, Counter collisionCounter,

                           Ball ball,
                           ArrayList<Heart> hearsList,
                           GameManager brickerGameManager) {
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
        this.puddleImgPath = puddleImgPath;
        this.paddleDimensions = paddleDimensions;
        this.distFromEnd = distFromEnd;
        StrategyFactory.collisionCounter = collisionCounter;
        this.hearsList = hearsList;
        this.ball = ball;
        this.brickerGameManager = brickerGameManager;
        this.strategyNums = 1;
    }

    public ArrayList<CollisionStrategy> generateStrategies() {
        ArrayList<CollisionStrategy> collisionStrategies = new ArrayList<>();
        if (new Random().nextBoolean()) {
            collisionStrategies.add(basicStrategy());
        } else {
            addSpecialStrategies(collisionStrategies);
        }
        return collisionStrategies;
    }

    private CollisionStrategy basicStrategy() {
        return new BasicCollisionStrategy(gameObjects, bricksCounter);
    }

    private CollisionStrategy addPucksStrategy() {
        return new AddPucksStrategty(gameObjects, bricksCounter, imageReader, soundReader, ballSpeed,
                puckList, ballSize);
    }

    private CollisionStrategy extraHeartStrategy() {
        return new ExtraLifeStrategy(gameObjects, bricksCounter, imageReader, heartDimensions, imagePath,
                livesCounter, hearsList);
    }

    private CollisionStrategy extraPaddleStrategy() {
        return new ExtraPaddleStrategy(gameObjects, windowDimensions, inputListener, imageReader
                , distFromEnd, paddleDimensions, puddleImgPath, bricksCounter, collisionCounter);
    }

    private CollisionStrategy changeCameraStrategy() {
        return new ChangeCameraStrategy(gameObjects, bricksCounter, brickerGameManager, windowDimensions,
                ball);
    }

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
