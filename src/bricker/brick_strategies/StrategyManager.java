package bricker.brick_strategies;

import java.util.ArrayList;
import bricker.game_objects.Puck;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.util.Counter;

public class StrategyManager implements CollisionStrategy{
    private GameObjectCollection gameObjects;
    private Counter bricksCounter;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private int ballSpeed;
    private final float ballSize;
    private ArrayList<Puck> puckList;
    public StrategyManager(GameObjectCollection gameObjects, Counter bricksCounter ,
                ImageReader imageReader, SoundReader soundReader,int ballSpeed,ArrayList<Puck > puckList,float ballSize) {
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

    private CollisionStrategy[] generateStrategies() {
        return new CollisionStrategy[]{addPucksStrategy(), basicStrategy()};
    }

    private CollisionStrategy basicStrategy(){
        return new BasicCollisionStrategy(gameObjects,bricksCounter);
    }

    private CollisionStrategy addPucksStrategy(){
        return new AddPucksStrategty(gameObjects,bricksCounter,imageReader,soundReader,ballSpeed,puckList,ballSize);
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        for (CollisionStrategy strategy: generateStrategies()){
            strategy.onCollision(thisObj,otherObj);
        }

    }
}
