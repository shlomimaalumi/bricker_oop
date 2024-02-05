package bricker.game_objects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import java.util.Stack;

public class HeartsLifeCounter extends GameObject {
    private final Vector2 heartSize;
    private final Vector2 topLeftCorner;
    private final GameObjectCollection gameObjects;
    private final Renderable heartImage;
    private final Stack<Heart> heartsStack ;
    private final Counter lives;
    private int currentHeartsAmount;
    Private final Stack<Heart>;
    private Counter lives;
    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param heartImage    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public HeartsLifeCounter(Vector2 topLeftCorner, Vector2 dimensions, Renderable heartImage, Counter lives,
    GameObjectCollection gameObjects) {
        super(Vector2.ZERO, Vector2.ZERO, null);
        this.heartSize=dimensions;
        this.topLeftCorner=topLeftCorner;
        this.heartImage=heartImage;
        this.heartsStack = new Stack<>();
        this.gameObjects=gameObjects;
        for (int i=0;i<lives.value();i++)
        {
            bulidHeartNumberI(i);
        }
        this.lives = lives;
    }

    public void bulidHeartNumberI(int i)
    {
        Vector2 curPosiotion = topLeftCorner.add(heartSize.multX(i).multY(0));
        Heart heart = new Heart(curPosiotion,this.heartSize,heartImage);
        gameObjects.addGameObject(heart, Layer.UI);
    }


    private void removeHeart(){
        Heart heart = heartsStack.pop();
        this.gameObjects.removeGameObject(heart, Layer.UI);
    }

    @Override
    public void update(float deltaTime) {
        if (this.lives.value()> heartsStack.size()){
            bulidHeartNumberI(heartsStack.size());
        }
        if (this.lives.value()<heartsStack.size()){
            removeHeart();
        }
    }
}
