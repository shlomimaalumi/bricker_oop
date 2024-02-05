package bricker.main;

import bricker.brick_strategies.BasicCollisionStrategy;
import bricker.brick_strategies.CollisionStrategy;
import bricker.game_objects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;
// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class BrickerGameManager extends GameManager {
    private static final String PADDLE_IMG_PATH = "assets/paddle.png";
    private static final String BRICK_ING_PATH = "assets/brick.png";
    private static final String BALL_IMG_PATH = "assets/ball.png";
    private static final String HEART_IMG_PATH = "assets/heart.png";
    private static final String BACKGROUND_IMG_PATH = "assets/DARK_BG2_small.jpeg";
    private static final String COLLISION_SOUND_PATH = "assets/blop_cut_silenced.wav";
    private static final String WINDOW_TITLE = "bricker";
    private static final String WINNING_ANNOUNCEMENT = "You win!\n";
    private static final String LOSING_ANNOUNCEMENT = "You lose!\n";
    private static final String PLAY_AGAIN_ASKING_MSG = "Play again?";
    private static final Color BORDER_COLOR = Color.cyan;
    private static final int BORDER_WIDTH = 10;
    private static final int PADDLE_HEIGHT = 20;
    private static final int PADDLE_WIDTH = 150;
    private static final int INIT_LIVES = 3;
    private static final int BRICKS_IN_ROW = 8;
    private static final int BRICKS_IN_COL = 7;
    private static final int BRICKS_HEIGHT = 15;
    private static final int SPACE_BETWEEN_BRICKS = 5;
    private static final int BALL_RADIUS = 35;
    private static final int GRAPHIC_COUNTER_SIZE = 40;
    private static final float BALL_SPEED = 250;
    private static final float BASIC_SPACE = 20;
    private static final float HALF = 0.5f;
    private static final float WINDOW_X_LEN = 700;
    private static final float WINDOW_Y_LEN = 500;
    private static final int INITAL_LIFE = 3;
    //    private Counter lifeCounter = new Counter(INIT_LIVES);
    private static final Renderable BORDER_RENDERABLE =
            new RectangleRenderable(new Color(80, 140, 250));
    private Ball ball;
    private Vector2 windowDimentions;
    private WindowController windowController;
    private UserInputListener inputListener;
    private final int bricksInRow;
    private final int bricksInCol;
    private Counter lives;
    private SoundReader soundReader;
    private ImageReader imageReader;
    private TextRenderable textRenderable;
    private GameObject numericCounter;
    private Counter bricksCounter;
//    private CollisionStrategy collisionStrategy;

    public BrickerGameManager(String windowTitle, Vector2 windowDimentions) {
        super(windowTitle, windowDimentions);
        this.bricksInRow = BRICKS_IN_ROW;
        this.bricksInCol = BRICKS_IN_COL;
        this.lives = new Counter(INIT_LIVES);
        this.bricksCounter=new Counter((bricksInRow*bricksInCol));
    }

    public BrickerGameManager(String windowTitle, Vector2 windowDimentions, int bricksInRow, int bricksInCol) {
        super(windowTitle, windowDimentions);
        this.bricksInRow = bricksInRow;
        this.bricksInCol = bricksInCol;
        this.lives = new Counter(INIT_LIVES);
        this.bricksCounter=new Counter((bricksInRow * bricksInCol));
    }


    public void initializeGame(ImageReader imageReader, SoundReader
            soundReader, UserInputListener inputListener,
                               WindowController windowController) {
        //initialization
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowController = windowController;
        this.inputListener = inputListener;
        this.windowDimentions = windowController.getWindowDimensions();
        this.imageReader = imageReader;
        this.soundReader = soundReader;
//        this.lives=3;
        createObjects(imageReader, soundReader);
    }

    private void createObjects(ImageReader imageReader, SoundReader soundReader) {
        createBall(imageReader, soundReader, windowController);
        createPaddle(imageReader, windowDimentions, inputListener);
//        createAIPaddle(imageReader, windowDimentions);
        createBricks(imageReader, inputListener, windowDimentions,
                new BasicCollisionStrategy(this.gameObjects(),bricksCounter));
        createBorders(windowDimentions);
        createBackground(imageReader, windowDimentions);
        createNumericCounter();
        createHeartsCounter(imageReader);


    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkForGameEnd();
    }

    private void createHeartsCounter(ImageReader imageReader)
    {

        Vector2 dimensions = new Vector2(GRAPHIC_COUNTER_SIZE, GRAPHIC_COUNTER_SIZE);
        Vector2 beginHeartsPos = new Vector2(BORDER_WIDTH+ GRAPHIC_COUNTER_SIZE, windowDimentions.y()
                - 2 * (BASIC_SPACE + PADDLE_HEIGHT));
        Renderable heartImage = imageReader.readImage(HEART_IMG_PATH,true);
        HeartsLifeCounter heartsLifeCounter=new HeartsLifeCounter(beginHeartsPos,dimensions,heartImage,lives
        , gameObjects());
        gameObjects().addGameObject(heartsLifeCounter);
    }

    private void createNumericCounter() {
        Vector2 dimentions = new Vector2(GRAPHIC_COUNTER_SIZE, GRAPHIC_COUNTER_SIZE);
        Vector2 topLeftCorner = new Vector2(BORDER_WIDTH, windowDimentions.y() - 2 *
                (BASIC_SPACE + PADDLE_HEIGHT));
        NumericLifeCounter numericLifeCounter = new NumericLifeCounter(topLeftCorner, dimentions, lives, gameObjects());
        gameObjects().addGameObject(numericLifeCounter);
    }

    private void createBall(ImageReader imageReader, SoundReader soundReader, WindowController windowController) {
        Renderable ballImage =
                imageReader.readImage(BALL_IMG_PATH, true);
        Sound collisionSound = soundReader.readSound(COLLISION_SOUND_PATH);
        ball = new Ball(
                Vector2.ZERO, new Vector2(BALL_RADIUS, BALL_RADIUS), ballImage, collisionSound);

        Vector2 windowDimensions = windowController.getWindowDimensions();
//        ball.setCenter(windowDimensions.mult(HALF));
        gameObjects().addGameObject(ball);
        setRandomBallVelocityInCenter();
//
//        float ballVelX = BALL_SPEED;
//        float ballVelY = BALL_SPEED;
//        Random rand = new Random();
//        if (rand.nextBoolean())
//            ballVelX *= -1;
//        if (rand.nextBoolean())
//            ballVelY *= -1;
//        ball.setVelocity(new Vector2(ballVelX, ballVelY));
    }

    private void createPaddle(ImageReader imageReader, Vector2 windowDimensions, UserInputListener inputListener) {
        Renderable paddleImage = imageReader.readImage(
                PADDLE_IMG_PATH, false);
        Paddle paddle = new Paddle(
                Vector2.ZERO,
                new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                paddleImage, inputListener, windowDimensions, BORDER_WIDTH);
        paddle.setCenter(
                new Vector2(windowDimensions.x() / 2, (int) windowDimensions.y() - BASIC_SPACE));
        gameObjects().addGameObject(paddle);
    }

    private void createAIPaddle(ImageReader imageReader, Vector2 windowDimensions) {
        Renderable paddleImage = imageReader.readImage(
                PADDLE_IMG_PATH, false);
        GameObject aiPaddle = new AIPaddle(
                Vector2.ZERO, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT), paddleImage,
                ball, windowDimensions, BORDER_WIDTH);
        aiPaddle.setCenter(
                new Vector2(windowDimensions.x() / 2, BASIC_SPACE));
        gameObjects().addGameObject(aiPaddle);
    }

    private void createBorders(Vector2 windowDimensions) {
        gameObjects().addGameObject(
                new GameObject(
                        Vector2.ZERO,
                        new Vector2(BORDER_WIDTH, windowDimensions.y()), null), Layer.STATIC_OBJECTS);
    gameObjects().addGameObject(
            new GameObject(
                    Vector2.ZERO,
                    new Vector2(windowDimensions.x(), BORDER_WIDTH),
                    null)
    );
        gameObjects().addGameObject(
                new GameObject(
                        new Vector2(windowDimensions.x() - BORDER_WIDTH, 0),
                        new Vector2(BORDER_WIDTH, windowDimensions.y()), null), Layer.STATIC_OBJECTS);
    }

    private void createBrick(Renderable brickImage, Vector2 brickVector, Vector2 windowDimentions,
                             CollisionStrategy collisionStrategy) {
        Brick brick = new Brick(brickVector, windowDimentions, brickImage, collisionStrategy);
        gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
    }


    private void createBricks(ImageReader imageReader, UserInputListener inputListener,
                              Vector2 windowDimentions, CollisionStrategy collisionStrategy) {
        //TODO take care of this calc
        Renderable brickImage =
                imageReader.readImage(BRICK_ING_PATH, false);
        float brickWidth = (windowDimentions.x() - 3 * BORDER_WIDTH - (bricksInRow - 1)
                * SPACE_BETWEEN_BRICKS) / bricksInRow;
        float brickHeight = BRICKS_HEIGHT;
        Vector2 brickVector = new Vector2(brickWidth, brickHeight);
        for (int i = 0; i < bricksInRow; i++) {
            for (int j = 0; j < bricksInCol; j++) {
                float x = 2 * BORDER_WIDTH + i * brickWidth + (i - 1) * SPACE_BETWEEN_BRICKS;
                float y = 2 * BORDER_WIDTH + j * brickHeight + (j - 1) * SPACE_BETWEEN_BRICKS;
                Vector2 location = new Vector2(x, y);
                createBrick(brickImage, location, brickVector, collisionStrategy);
            }
        }
    }

    private void askToPlayAgain(String prompt) {
        if (windowController.openYesNoDialog(prompt)) {
            lives.increaseBy(INIT_LIVES - lives.value());
//            int x=bricksInRow*bricksInCol-bricksCounter.value();
//            System.out.println("reset to: " +x);
//            bricksCounter.increaseBy(bricksInRow*bricksInCol-bricksCounter.value());

            windowController.resetGame();
        } else {
            windowController.closeWindow();
        }
    }

    private void checkForGameEnd() {
        double ballHeight = ball.getCenter().y();
        String prompt = "";
        if (bricksCounter.value()==0 || inputListener.isKeyPressed(KeyEvent.VK_W)) {
            bricksCounter.increaseBy(bricksInRow*bricksInCol-bricksCounter.value());
            prompt += WINNING_ANNOUNCEMENT;
        }
        if (ballHeight > windowDimentions.y()) {
//            bricksCounter.increaseBy(bricksInRow*bricksInCol-bricksCounter.value());
            lives.decrement();
            if (lives.value() == 0) {
                prompt += LOSING_ANNOUNCEMENT;
            } else {
                windowController.resetGame();
            }
        }
        if (!prompt.isEmpty()) {
            prompt += PLAY_AGAIN_ASKING_MSG;
            askToPlayAgain(prompt);
        }
    }

    private void createBackground(ImageReader imageReader, Vector2 windowDimensions) {
        Renderable backgroundImage =
                imageReader.readImage(BACKGROUND_IMG_PATH, false);
        GameObject backgroundObj = new GameObject(Vector2.ZERO, windowDimensions, backgroundImage);
        gameObjects().addGameObject(backgroundObj, Layer.BACKGROUND);
        //DARK_BG2_small
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            new BrickerGameManager(WINDOW_TITLE, new Vector2(WINDOW_X_LEN, WINDOW_Y_LEN)).run();
        } else {
            int bricksInRow = Integer.parseInt(args[0]);
            int bricksInCol = Integer.parseInt(args[1]);
            new BrickerGameManager(WINDOW_TITLE, new Vector2(WINDOW_X_LEN, WINDOW_Y_LEN),
                    bricksInRow, bricksInCol).run();
        }
    }
}
