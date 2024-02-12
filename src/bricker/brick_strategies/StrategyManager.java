package bricker.brick_strategies;

import java.awt.*;
import java.util.ArrayList;

import bricker.game_objects.Puck;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.util.Counter;
import danogl.util.Vector2;

public class StrategyManager implements CollisionStrategy {
    private static final String PUCK_IMG_PATH = "assets/mockBall.png";
    private static final String COLLISION_SOUND_PATH = "assets/blop_cut_silenced.wav";
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

    public StrategyManager(GameObjectCollection gameObjects, Counter bricksCounter,
                           ImageReader imageReader, SoundReader soundReader, float ballSpeed,
                           ArrayList<Puck> puckList, float ballSize,
                           Vector2 heartDimensions, Counter livesCounter, String imagePath) {
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
    }

    public CollisionStrategy[] generateStrategies() {
        return new CollisionStrategy[]{ basicStrategy(),extraHeartStrategy()};
    }

    private CollisionStrategy basicStrategy() {
        return new BasicCollisionStrategy(gameObjects, bricksCounter);
    }

    private CollisionStrategy addPucksStrategy() {
        return new AddPucksStrategty(gameObjects, bricksCounter, imageReader, soundReader, ballSpeed, puckList,
                ballSize);
    }

    private CollisionStrategy extraHeartStrategy() {
        return new ExtraLifeStrategy(gameObjects, bricksCounter, imageReader, heartDimensions, imagePath,livesCounter);
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        for (CollisionStrategy strategy : generateStrategies()) {
            strategy.onCollision(thisObj, otherObj);
        }

    }
}