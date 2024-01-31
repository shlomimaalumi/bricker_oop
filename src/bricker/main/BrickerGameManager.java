package bricker.main;

import bricker.game_objects.AIPaddle;
import bricker.game_objects.Ball;
import bricker.game_objects.Brick;
import bricker.game_objects.Paddle;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Border;
import danogl.util.Vector2;

import java.awt.*;
import java.util.Random;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class BrickerGameManager extends GameManager {
    private static final String PADDLE_IMG_PATH = "assets/paddle.png";
    private static final String BRICK_ING_PATH = "assets/brick.png";
    private static final String BALL_IMG_PATH = "assets/ball.png";
    private static final String BACKGROUND_IMG_PATH = "assets/DARK_BG2_small.jpeg";
    private static final String COLLISION_SOUND_PATH = "assets/blop_cut_silenced.wav";
    private static final String WINDOW_TITLE = "bricker";
    private static final String WINNING_ANNOUNCEMENT = "win!\n";
    private static final String LOSING_ANNOUNCEMENT = "lose!\n";
    private static final String PLAY_AGAIN_ASKING_MSG = "Play again?";
    private static final Color BORDER_COLOR = Color.cyan;
    private static final int BORDER_WIDTH = 10;
    private static final int PADDLE_HEIGHT = 20;
    private static final int PADDLE_WIDTH = 150;
    private static final int INIT_LIVES = 3;
    private static final int BRICKS_IN_ROW = 8;
    private static final int BRICKS_IN_COL = 5;
    private static final int SPACE_BETWEEN_BRICKS = 5;
    private static final int BALL_RADIUS = 35;
    private static final float BALL_SPEED = 250;
    private static final float BASIC_SPACE = 30;
    private static final float HALF = 0.5f;
    private static final float WINDOW_X_LEN = 700;
    private static final float WINDOW_Y_LEN = 500;
    private int lifeCounter = INIT_LIVES;
    private static final Renderable BORDER_RENDERABLE =
            new RectangleRenderable(new Color(80, 140, 250));
    private Ball ball;
    private Vector2 windowDimentions;
    private WindowController windowController;
    private UserInputListener inputListener;

    public BrickerGameManager(String windowTitle, Vector2 windowDimentions) {
        super(windowTitle, windowDimentions);
    }

    //    @Override
//    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
//        super.initializeGame(imageReader, soundReader, inputListener, windowController);
//        this.windowController = windowController;
//        this.inputListener = inputListener;
//        this.windowDimentions = windowController.getWindowDimensions();
//        createBall(imageReader, soundReader,windowController);
//        createPaddle(imageReader);
//        //add ball
//        //add paddles
//        //add bricks
//
//    }
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader
            soundReader, UserInputListener inputListener,
                               WindowController windowController) {
        //initialization
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowController = windowController;
        this.inputListener = inputListener;
        this.windowDimentions = windowController.getWindowDimensions();

//        createBricks(imageReader, inputListener, windowDimentions);

        //create paddles
        // create objects
        createBall(imageReader, soundReader, windowController);
        createPaddle(imageReader, windowDimentions, inputListener);
        createAIPaddle(imageReader, windowDimentions);
        createBorders(windowDimentions);
        createBackground(imageReader,windowDimentions);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkForGameEnd();
    }


    private void createBall(ImageReader imageReader, SoundReader soundReader, WindowController windowController) {
        Renderable ballImage =
                imageReader.readImage(BALL_IMG_PATH, true);
        Sound collisionSound = soundReader.readSound(COLLISION_SOUND_PATH);
        ball = new Ball(
                Vector2.ZERO, new Vector2(BALL_RADIUS, BALL_RADIUS), ballImage, collisionSound);

        Vector2 windowDimensions = windowController.getWindowDimensions();
        ball.setCenter(windowDimensions.mult(HALF));
        gameObjects().addGameObject(ball);

        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean())
            ballVelX *= -1;
        if (rand.nextBoolean())
            ballVelY *= -1;
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
    }


    private void createPaddle(ImageReader imageReader, Vector2 windowDimensions, UserInputListener inputListener) {
        Renderable paddleImage = imageReader.readImage(
                PADDLE_IMG_PATH, false);
        Paddle paddle = new Paddle(
                Vector2.ZERO,
                new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                paddleImage, inputListener,windowDimensions,BORDER_WIDTH);
        paddle.setCenter(
                new Vector2(windowDimensions.x() / 2, (int) windowDimensions.y() - BASIC_SPACE));
        gameObjects().addGameObject(paddle);
    }

    private void createAIPaddle(ImageReader imageReader, Vector2 windowDimensions) {
        Renderable paddleImage = imageReader.readImage(
                PADDLE_IMG_PATH, false);
        GameObject aiPaddle = new AIPaddle(
                Vector2.ZERO, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT), paddleImage,
                ball);
        aiPaddle.setCenter(
                new Vector2(windowDimensions.x() / 2, BASIC_SPACE));
        gameObjects().addGameObject(aiPaddle);
    }


//    private void createBorders() {
//        gameObjects().addGameObject(Border.atDirection(Vector2.RIGHT, windowDimentions, BORDER_WIDTH,
//                BORDER_COLOR));
//        gameObjects().addGameObject(Border.atDirection(Vector2.LEFT, windowDimentions, BORDER_WIDTH,
//                BORDER_COLOR));
//    }
private void createBorders(Vector2 windowDimensions) {
    gameObjects().addGameObject(
            new GameObject(
                    Vector2.ZERO,
                    new Vector2(BORDER_WIDTH, windowDimensions.y()),
                    null)
    );
//    gameObjects().addGameObject(
//            new GameObject(
//                    Vector2.ZERO,
//                    new Vector2(windowDimensions.x(), BORDER_WIDTH),
//                    null)
//    );
    gameObjects().addGameObject(
            new GameObject(
                    new Vector2(windowDimensions.x() - BORDER_WIDTH, 0),
                    new Vector2(BORDER_WIDTH, windowDimensions.y()),
                    null)
    );
}

    private void createBrick(Renderable brickImage, Vector2 brickVector, Vector2 windowDimentions) {
        Brick brick = new Brick(brickVector, windowDimentions, brickImage);
        gameObjects().addGameObject(brick);
    }


    private void createBricks(ImageReader imageReader, UserInputListener inputListener,
                              Vector2 windowDimentions) {
        //TODO take care of this calc
        Renderable brickImage =
                imageReader.readImage(BRICK_ING_PATH, false);
        float brickWidth = (windowDimentions.x() - 3 * BORDER_WIDTH - (BRICKS_IN_ROW - 1)
                * SPACE_BETWEEN_BRICKS) / BRICKS_IN_ROW;
        float brickHeight = 20;
        Vector2 brickVector = new Vector2(brickWidth, brickHeight);
        for (int i = 0; i < BRICKS_IN_ROW; i++) {
            for (int j = 0; j < BRICKS_IN_COL; j++) {
                float x = 2 * BORDER_WIDTH + i * brickWidth + (i - 1) * SPACE_BETWEEN_BRICKS;
                float y = 2 * BORDER_WIDTH + j * brickHeight + (j - 1) * SPACE_BETWEEN_BRICKS;
                Vector2 location = new Vector2(x, y);
                createBrick(brickImage, location, brickVector);

            }
        }
    }

    private void askToPlayAgain(String prompt) {
        if (windowController.openYesNoDialog(prompt))
            windowController.resetGame();
        else
            windowController.closeWindow();
    }

    // TODO add paddle life
    //    private void onBallFall() {
    //        this.lifeCounter--;
    //        if (this.lifeCounter == 0)
    //            gameLost();
    //
    //    }`


    private void checkForGameEnd() {
        double ballHeight = ball.getCenter().y();
        String prompt = "";
        if (ballHeight < 0) {
            prompt += WINNING_ANNOUNCEMENT;
        }
        if (ballHeight > windowDimentions.y()) {
            prompt += LOSING_ANNOUNCEMENT;
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
        new BrickerGameManager(WINDOW_TITLE, new Vector2(WINDOW_X_LEN, WINDOW_Y_LEN)).run();
    }
}
