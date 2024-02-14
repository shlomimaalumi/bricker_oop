package bricker.main;

import danogl.components.CoordinateSpace;

import bricker.brick_strategies.*;
import bricker.game_objects.*;

import java.util.ArrayList;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import java.util.Random;


/**
 * Manages the game logic for a simple Brick Breaker game.
 */
public class BrickerGameManager extends GameManager {
// Image paths for game elements
    /**
     * Path to the paddle image.
     */
    private static final String PADDLE_IMG_PATH = "assets/paddle.png";

    /**
     * Path to the brick image.
     */
    private static final String BRICK_IMG_PATH = "assets/brick.png";

    /**
     * Path to the ball image.
     */
    private static final String BALL_IMG_PATH = "assets/ball.png";

    /**
     * Path to the heart image.
     */
    private static final String HEART_IMG_PATH = "assets/heart.png";

    /**
     * Path to the background image.
     */
    private static final String BACKGROUND_IMG_PATH = "assets/DARK_BG2_small.jpeg";

    // Sound path for collision
    /**
     * Path to the collision sound.
     */
    private static final String COLLISION_SOUND_PATH = "assets/blop_cut_silenced.wav";

    // Game window title
    /**
     * Title of the game window.
     */
    private static final String WINDOW_TITLE = "bricker";

    // Messages displayed to the player
    /**
     * Winning announcement message displayed to the player.
     */
    private static final String WINNING_ANNOUNCEMENT = "You win!\n";

    /**
     * Losing announcement message displayed to the player.
     */
    private static final String LOSING_ANNOUNCEMENT = "You lose!\n";

    /**
     * Message asking the player if they want to play again.
     */
    private static final String PLAY_AGAIN_ASKING_MSG = "Play again?";

    // Dimensions and sizes for game elements
    /**
     * Width of the border.
     */
    private static final int BORDER_WIDTH = 10;

    /**
     * Height of the border.
     */
    private static final int BORDER_HEIGHT = 10;

    /**
     * Height of the paddle.
     */
    private static final int PADDLE_HEIGHT = 20;

    /**
     * Width of the paddle.
     */
    private static final int PADDLE_WIDTH = 150;

    /**
     * Initial number of lives.
     */
    private static final int INIT_LIVES = 3;

    /**
     * Number of bricks in a row.
     */
    private static final int BRICKS_IN_ROW = 8;

    /**
     * Number of bricks in a column.
     */
    private static final int BRICKS_IN_COL = 7;

    /**
     * Height of the bricks.
     */
    private static final int BRICKS_HEIGHT = 15;

    /**
     * Space between bricks.
     */
    private static final int SPACE_BETWEEN_BRICKS = 5;

    /**
     * Radius of the ball.
     */
    private static final int BALL_RADIUS = 20;

    /**
     * Size of the graphic counter.
     */
    private static final int GRAPHIC_COUNTER_SIZE = 40;

    // Ball movement properties
    /**
     * Speed of the ball.
     */
    private static final float BALL_SPEED = 250;

    /**
     * Number of ball hits required to reset the camera.
     */
    private static final int BALL_HITS_TO_RESET_CAMERA = 4;

    /**
     * Offset for ball hits.
     */
    private static final int HIT_OFFSET = 1;

    // Miscellaneous constants
    /**
     * Basic space constant.
     */
    private static final float BASIC_SPACE = 20;

    /**
     * Half constant.
     */
    private static final float HALF = 0.5f;

    /**
     * X length of the game window.
     */
    private static final float WINDOW_X_LEN = 700;

    /**
     * Y length of the game window.
     */
    private static final float WINDOW_Y_LEN = 500;


    private Ball ball;
    private Vector2 windowDimentions;
    private WindowController windowController;
    private UserInputListener inputListener;
    private final int bricksInRow;
    private final int bricksInCol;
    private final Counter lives;
    private final ArrayList<Puck> puckList;
    private final ArrayList<Heart> heartList;
    private final Counter bricksCounter;
    private static final Counter collisionCounter = new Counter(0);

    /**
     * Constructor for initializing the game manager with default settings.
     *
     * @param windowTitle      The title of the game window.
     * @param windowDimensions The dimensions of the game window.
     */

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
        this.bricksInRow = BRICKS_IN_ROW;
        this.bricksInCol = BRICKS_IN_COL;
        this.lives = new Counter(INIT_LIVES);
        this.puckList = new ArrayList<>();
        this.heartList = new ArrayList<>();
        this.bricksCounter = new Counter(bricksInRow * bricksInCol);
    }

    /**
     * Constructor for initializing the game manager with custom brick layout.
     *
     * @param windowTitle      The title of the game window.
     * @param windowDimensions The dimensions of the game window.
     * @param bricksInRow      The number of bricks in each row.
     * @param bricksInCol      The number of bricks in each column.
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int bricksInRow,
                              int bricksInCol) {
        super(windowTitle, windowDimensions);
        this.bricksInRow = bricksInRow;
        this.bricksInCol = bricksInCol;
        this.lives = new Counter(INIT_LIVES);
        this.puckList = new ArrayList<>();
        this.heartList = new ArrayList<>();
        this.bricksCounter = new Counter(bricksInRow * bricksInCol);
    }

    /**
     * Initializes the game by setting up the window and creating game objects.
     *
     * @param imageReader      The image reader for loading game images.
     * @param soundReader      The sound reader for loading game sounds.
     * @param inputListener    The user input listener for handling player input.
     * @param windowController The window controller for managing the game window.
     */
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowController = windowController;
        this.inputListener = inputListener;
        this.windowDimentions = windowController.getWindowDimensions();
        createObjects(imageReader, soundReader);
    }

    /**
     * Creates various game objects such as ball, paddle, bricks, borders, background, and counters.
     *
     * @param imageReader The image reader for loading game images.
     * @param soundReader The sound reader for loading game sounds.
     */
    private void createObjects(ImageReader imageReader, SoundReader soundReader) {
        createBall(imageReader, soundReader);
        createPaddle(imageReader, windowDimentions, inputListener);
        Vector2 heartDimentions = new Vector2(GRAPHIC_COUNTER_SIZE, GRAPHIC_COUNTER_SIZE);
        StrategyFactory strategyFactory = new StrategyFactory(gameObjects(), bricksCounter, imageReader,
                soundReader, BALL_SPEED, puckList, BALL_RADIUS, heartDimentions, lives, HEART_IMG_PATH,
                inputListener, windowDimentions, BORDER_WIDTH, createPaddleDimension(), PADDLE_IMG_PATH,
                collisionCounter, ball, heartList, this);
        createBricks(imageReader, windowDimentions, strategyFactory);
        createBorders(windowDimentions);
        createBackground(imageReader, windowDimentions);
        createNumericCounter();
        createHeartsCounter(imageReader);
    }

    /**
     * Creates a counter for displaying remaining lives using heart images.
     *
     * @param imageReader The image reader for loading game images.
     */
    private void createHeartsCounter(ImageReader imageReader) {
        Vector2 dimensions = new Vector2(GRAPHIC_COUNTER_SIZE, GRAPHIC_COUNTER_SIZE);
        Vector2 beginHeartsPos = new Vector2(BORDER_WIDTH + GRAPHIC_COUNTER_SIZE,
                windowDimentions.y() - 2 * (BASIC_SPACE + PADDLE_HEIGHT));
        Renderable heartImage = imageReader.readImage(HEART_IMG_PATH, true);
        HeartsLifeCounter heartsLifeCounter = new HeartsLifeCounter(beginHeartsPos, dimensions, heartImage
                , lives, gameObjects());
        gameObjects().addGameObject(heartsLifeCounter);
    }

    /**
     * Creates a numeric counter for displaying remaining lives.
     */

    private void createNumericCounter() {
        Vector2 dimentions = new Vector2(GRAPHIC_COUNTER_SIZE, GRAPHIC_COUNTER_SIZE);
        Vector2 topLeftCorner = new Vector2(BORDER_WIDTH,
                windowDimentions.y() - 2 * (BASIC_SPACE + PADDLE_HEIGHT));
        NumericLifeCounter numericLifeCounter = new NumericLifeCounter(topLeftCorner, dimentions, lives,
                gameObjects());
        gameObjects().addGameObject(numericLifeCounter);
    }


    /**
     * Sets a random initial velocity for the ball and places it in the center of the game window.
     */
    private void setRandomBallVelocityInCenter() {
        Vector2 windowDimensions = windowController.getWindowDimensions();
        this.ball.setCenter(windowDimensions.mult(HALF));
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean())
            ballVelX *= -1;
        if (rand.nextBoolean())
            ballVelY *= -1;
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
    }

    /**
     * Creates the ball object with its associated image and sound.
     *
     * @param imageReader The image reader for loading game images.
     * @param soundReader The sound reader for loading game sounds.
     */

    private void createBall(ImageReader imageReader, SoundReader soundReader) {
        Renderable ballImage = imageReader.readImage(BALL_IMG_PATH, true);
        Sound collisionSound = soundReader.readSound(COLLISION_SOUND_PATH);
        ball = new Ball(Vector2.ZERO, new Vector2(BALL_RADIUS, BALL_RADIUS), ballImage, collisionSound);
        gameObjects().addGameObject(ball);
        setRandomBallVelocityInCenter();

    }

    /**
     * Creates the paddle object with its associated image and input listener.
     *
     * @param imageReader      The image reader for loading game images.
     * @param windowDimensions The dimensions of the game window.
     * @param inputListener    The user input listener for handling player input.
     */
    private void createPaddle(ImageReader imageReader, Vector2 windowDimensions,
                              UserInputListener inputListener) {
        Renderable paddleImage = imageReader.readImage(PADDLE_IMG_PATH, false);
        Paddle paddle = new Paddle(Vector2.ZERO, createPaddleDimension(), paddleImage, inputListener,
                windowDimensions, BORDER_WIDTH);
        paddle.setCenter(new Vector2(windowDimensions.x() / 2, (int) windowDimensions.y() - BASIC_SPACE));
        gameObjects().addGameObject(paddle);
    }

    /**
     * Creates dimensions for the paddle object.
     *
     * @return The dimensions of the paddle.
     */
    private Vector2 createPaddleDimension() {
        return new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT);
    }

    /**
     * Creates borders around the game window.
     *
     * @param windowDimensions The dimensions of the game window.
     */
    private void createBorders(Vector2 windowDimensions) {
        gameObjects().addGameObject(new GameObject(Vector2.ZERO, new Vector2(BORDER_WIDTH,
                windowDimensions.y()), null), Layer.STATIC_OBJECTS);
        gameObjects().addGameObject(new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(),
                BORDER_HEIGHT), null));
        gameObjects().addGameObject(new GameObject(new Vector2(windowDimensions.x() - BORDER_WIDTH, 0),
                new Vector2(BORDER_WIDTH, windowDimensions.y()), null), Layer.STATIC_OBJECTS);
    }

    /**
     * Creates a single brick with specified image, position, and dimensions.
     *
     * @param brickImage       The image for the brick.
     * @param brickVector      The position of the brick.
     * @param windowDimensions The dimensions of the game window.
     * @param strategyFactory  The strategy factory for generating brick strategies.
     */
    private void createBrick(Renderable brickImage, Vector2 brickVector, Vector2 windowDimensions,
                             StrategyFactory strategyFactory) {
        Brick brick = new Brick(brickVector, windowDimensions, brickImage,
                strategyFactory.generateStrategies());
        gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
    }

    /**
     * Creates a grid of bricks based on the specified layout.
     *
     * @param imageReader      The image reader for loading game images.
     * @param windowDimensions The dimensions of the game window.
     * @param strategyFactory  The strategy factory for generating brick strategies.
     */
    private void createBricks(ImageReader imageReader, Vector2 windowDimensions,
                              StrategyFactory strategyFactory) {
        //TODO take care of this calc
        Renderable brickImage = imageReader.readImage(BRICK_IMG_PATH, false);
        float brickWidth =
                (windowDimensions.x() - 3 * BORDER_WIDTH - (bricksInRow - 1) * SPACE_BETWEEN_BRICKS)
                        / bricksInRow;
        float brickHeight = BRICKS_HEIGHT;
        Vector2 brickVector = new Vector2(brickWidth, brickHeight);
        for (int i = 0; i < bricksInRow; i++) {
            for (int j = 0; j < bricksInCol; j++) {
                float x = 2 * BORDER_WIDTH + i * brickWidth + (i - 1) * SPACE_BETWEEN_BRICKS;
                float y = 2 * BORDER_WIDTH + j * brickHeight + (j - 1) * SPACE_BETWEEN_BRICKS;
                Vector2 location = new Vector2(x, y);
                createBrick(brickImage, location, brickVector, strategyFactory);
            }
        }
    }

    /**
     * Checks the position of each puck and removes any that have gone beyond the game window.
     */

    private void checkPucks() {
        for (Puck puck : puckList) {
            if (puck.getTopLeftCorner().y() > windowDimentions.y()) {
                gameObjects().removeGameObject(puck);
            }
        }
    }

    /**
     * Clears all existing pucks from the game.
     */
    private void clearPucks() {
        for (Puck puck : puckList) {
            gameObjects().removeGameObject(puck);
        }
        this.puckList.clear();

    }

    /**
     * Checks the position of each heart and removes any that have gone beyond the game window.
     */

    private void checkHearts() {
        for (Heart heart : heartList) {
            if (heart.getTopLeftCorner().y() > windowDimentions.y()) {
                gameObjects().removeGameObject(heart);
            }
        }
    }

    /**
     * Clears all existing hearts from the game.
     */

    private void clearHears() {
        for (Heart heart : heartList) {
            gameObjects().removeGameObject(heart);
        }
        this.puckList.clear();
    }

    /**
     * Clears hearts, pucks, and resets the collision counter.
     */
    private void clearGame() {
        clearHears();
        clearPucks();
        collisionCounter.reset();
    }

    /**
     * Checks if the ball hits a certain number of times, then resets the camera.
     */
    private void checkForResetCamera() {
        if (ChangeCameraStrategy.getCollisionCounterInHit() + BALL_HITS_TO_RESET_CAMERA + HIT_OFFSET <=
                ball.getCollisionCounter()) {
            this.setCamera(null);
        }
    }

    /**
     * Asks the player if they want to play again and resets the game if they choose to do so.
     *
     * @param prompt The message to display in the play-again dialog.
     */
    private void askToPlayAgain(String prompt) {
        if (windowController.openYesNoDialog(prompt)) {
            clearGame();
            lives.increaseBy(INIT_LIVES - lives.value());
            bricksCounter.increaseBy(bricksInRow * bricksInCol - bricksCounter.value());
            windowController.resetGame();
        } else {
            windowController.closeWindow();
        }
    }

    /**
     * Checks for the end of the game (winning or losing) and prompts the player accordingly.
     */
    private void checkForGameEnd() {
        double ballHeight = ball.getCenter().y();
        String prompt = "";
        if (bricksCounter.value() == 0 || inputListener.isKeyPressed(KeyEvent.VK_W)) {
            prompt = handleWinning();
        }
        if (ballHeight > windowDimentions.y()) {
            prompt = handleLosingRound();
        }
        if (!prompt.isEmpty()) {
            prompt += PLAY_AGAIN_ASKING_MSG;
            askToPlayAgain(prompt);
        }
    }

    /**
     * Handles the game-winning scenario.
     *
     * @return The winning announcement message.
     */
    String handleWinning() {
        return WINNING_ANNOUNCEMENT;
    }

    /**
     * Handles the scenario when a round is lost, updating lives and resetting the game if needed.
     *
     * @return The losing announcement message, if lives are depleted.
     */
    String handleLosingRound() {
        //TODO delete this comment
        String prompt = "";
        lives.decrement();
        System.out.println(lives.value());
        if (lives.value() == 0) {
            prompt += LOSING_ANNOUNCEMENT;
            windowController.resetGame();
        } else {
            setRandomBallVelocityInCenter();
        }
        return prompt;
    }

    /**
     * Creates the background object with the specified image and dimensions.
     *
     * @param imageReader      The image reader for loading game images.
     * @param windowDimensions The dimensions of the game window.
     */
    private void createBackground(ImageReader imageReader, Vector2 windowDimensions) {
        Renderable backgroundImage = imageReader.readImage(BACKGROUND_IMG_PATH, false);
        GameObject backgroundObj = new GameObject(Vector2.ZERO, windowDimensions, backgroundImage);
        backgroundObj.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(backgroundObj, Layer.BACKGROUND);
    }

    /**
     * Overrides the update method to include specific game logic.
     *
     * @param deltaTime The time passed since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkPucks();
        checkHearts();
        checkForResetCamera();
        checkForGameEnd();
    }

    /**
     * Entry point for running the game.
     *
     * @param args Command-line arguments (optional: bricksInRow and bricksInCol).
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            new BrickerGameManager(WINDOW_TITLE, new Vector2(WINDOW_X_LEN, WINDOW_Y_LEN)).run();
        } else {
            int bricksInRow = Integer.parseInt(args[0]);
            int bricksInCol = Integer.parseInt(args[1]);
            new BrickerGameManager(WINDOW_TITLE, new Vector2(WINDOW_X_LEN, WINDOW_Y_LEN), bricksInRow,
                    bricksInCol).run();
        }
    }
}
