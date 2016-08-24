// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP102 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP102 Assignment
 * Name:
 * Usercode:
 * ID:
 */

import ecs100.*;
import java.awt.Color;

/** FrogSnakeGame: Completion
 *  Game with two frogs that race to get to the bug first in order to eat it, and must avoid snakes.
 *  (uses keys: player 1: W/A/S/D for up/left/down/right; controls the "light" frog
 *              player 2: I/J/K/L for up/left/down/right; controls the "dark" frog
 *  Frogs move around at a constant speed in an arena with an enclosing wall.
 *  When one of the frog reaches the bug, the frog eats it (grows in size),
 *       and a new bug appears at a random position within the arena walls.
 *
 *  The game has two snakes that constantly move in a random direction until they hits a wall.
 *    In which case the snake changes direction.
 * When a frog touches a snakes, it shrinks back to its original size.
 *
 *  Controls:
 *  - key to start (space)
 *  - keys to turn the two frogs  (w/a/s/d  and i/j/k/l)
 *
 *  Display:
 *   program constantly shows
 *   - the arena, the bug,  the frogs and the snakes
 *
 *  Constants:
 *    This class should contain constants specifying the various parameters of
 *    the game, including the geometry of the arena and obstacle.
 */

public class FrogSnakeGame{

    // Constants for the Geometry of the game.
    // (You may change or add to these if you wish)

    public static final int ArenaSize = 400;
    public static final int LeftWall = 30;
    public static final int RightWall = LeftWall+ArenaSize;
    public static final int TopWall = 50;
    public static final int BotWall = TopWall+ArenaSize;
    public static final int BugSize = 15;

    public static final int DELAY = 20;  // milliseconds to delay each step.

    // Fields to store the two frogs 
    private Frog frog1;
    private Frog frog2;

    // Fields to store the two snakes
    private Snake snake1;
    private Snake snake2;

    // Fields to position of the centre of the bug
    private double xBug;
    private double yBug;

    //Fields to store statistic values
    private int highScore = 0;
    private String highScorePlayer = null;

    /** Constructor
     * Set up the GUI,
     * Draw the arena and the bug
     */
    public FrogSnakeGame(){
        UI.initialise();
        UI.setImmediateRepaint(false);
        UI.setKeyListener(this::doKey);
        UI.addButton("Quit", UI::quit);
        UI.setDivider(0);
        UI.printMessage("Move mouse to arena and press Space to start");
        this.drawArenaBug();
        UI.repaintGraphics();
    }

    /**
     * Respond to keys.
     * the space key should reset the game to have two new frogs
     * the w/a/s/d keys should make the light frog turn up, to the left, down or to the right
     * the i/j/k/l keys should make the dark frog turn up, to the left, down or to the right
     */
    public void doKey(String key){
        if (key.equals("Space")) {
            restartGame();
        }
        else if (key.equals("w") ){
            this.frog1.turnUp();
        }
        else if (key.equals("a") ){
            this.frog1.turnLeft();
        }
        if (key.equals("s") ){
            this.frog1.turnDown();
        }
        else if (key.equals("d") ){
            this.frog1.turnRight();
        }
        else if (key.equals("i") ){
            this.frog2.turnUp();
        }
        else if (key.equals("j") ){
            this.frog2.turnLeft();
        }
        if (key.equals("k") ){
            this.frog2.turnDown();
        }
        else if (key.equals("l") ){
            this.frog2.turnRight();
        }

    }

    /**
     * Reset the game with two new frogs in the starting positions,
     *   and two snakes at a random position.
     * Loop forever
     *  - move each frog one step,
     *  - move each snake one step,
     *  - if any frog has caught the bug, it grows, and a new bug appears somewhere else.
     *  - if any frog was caught by a snake, it shrinks back to its original position.
     *  - redraw the game (frogs, snakes, arena, and bug)
     */
    private void restartGame(){
        // make sure that the current game ends (if it is running)
        this.frog1 = null;
        this.frog2 = null;
        this.snake1 = null;
        this.snake2 = null;
        UI.sleep(3*DELAY);

        // puts a bug at a random position
        this.newBug();

        // make new frogs
        this.frog1 = new Frog(LeftWall+30, (TopWall+BotWall)/2, "Up", "light");
        this.frog2 = new Frog(RightWall-30, (TopWall+BotWall)/2, "Left", "dark");

        // add 2 snakes
        this.snake1 = new Snake ();
        this.snake2 = new Snake ();

        // call redraw
        this.redraw();
        
        // resets high score variables
        this.highScore = 0;
        this.highScorePlayer = null;

        // Run the game
        while (this.frog1!=null && this.frog2!=null) {
            this.frog1.move();
            this.frog2.move();
            this.snake1.move();
            this.snake2.move();
            if (this.frog1.touching(xBug, yBug)) {
                this.frog1.grow();
                this.newBug();
            }
            else if (this.frog2.touching(xBug, yBug)) {
                this.frog2.grow();
                this.newBug();
            }
            if (this.frog1.touching(this.snake1.getX(), this.snake1.getY()) ||
            this.frog1.touching(this.snake2.getX(), this.snake2.getY()) ) {
                this.frog1.shrink();
            }
            if (this.frog2.touching(this.snake1.getX(), this.snake1.getY()) ||
            this.frog2.touching(this.snake2.getX(), this.snake2.getY()) ) {
                this.frog2.shrink();
            }
            this.redraw();
            UI.sleep(DELAY);
        }

    }

    /**
     * Redraws
     * - the arena
     * - the bug
     * - the two frogs
     * - the two snakes
     */
    private void redraw(){
        UI.clearGraphics();
        this.drawArenaBug();
        this.frog1.draw();
        this.frog2.draw();
        this.snake1.draw();
        this.snake2.draw();
        UI.repaintGraphics();

        this.drawStatistics();
    }

    /**
     * Draws the number of mosquotors eaten for each frog and which player is winning.
     */
    private void drawStatistics(){
        /** Displays how many mosquitos have bee eaten by each frog (without touching a snake). */
        UI.drawString("Mosquitos eaten by light frog: " + Integer.toString(frog1.getMosquitoCount()), this.LeftWall + 15, 20);
        UI.drawString("Mosquitos eaten by dark frog: " + Integer.toString(frog2.getMosquitoCount()), this.LeftWall + 15, 40);

        /** Displays which player is currently winning. */
        String winningPlayer = null;
        if (frog1.getMosquitoCount() == frog2.getMosquitoCount()) {
            winningPlayer = "Tie!";
        } else if (frog1.getMosquitoCount() > frog2.getMosquitoCount()) {
            winningPlayer = "Light frog";
        } else if (frog1.getMosquitoCount() < frog2.getMosquitoCount()) {
            winningPlayer = "Dark frog";
        }
        UI.drawString("Player currently winning: " + winningPlayer, this.LeftWall + 210, 20);

        /** Displays the highest score reached so far and by who. */
        if (frog1.getMosquitoCount() > this.highScore) {
            this.highScore = frog1.getMosquitoCount();
            this.highScorePlayer = "light frog";
        }
        if (frog2.getMosquitoCount() > this.highScore) {
            this.highScore = frog2.getMosquitoCount();
            this.highScorePlayer = "dark frog";
        }
        if (this.highScore > 0) {
            UI.drawString("High score of " + this.highScore + " by " + this.highScorePlayer + ".", this.LeftWall + 210, 40);
        } else {
            UI.drawString("No high score yet.", this.LeftWall + 210, 40);
        }
    }

    /**
     * Draw the arena as a rectangle with a bug in it
     */
    private void drawArenaBug(){
        UI.setColor(Color.black);
        UI.setLineWidth(2);
        UI.drawRect(LeftWall, TopWall, ArenaSize, ArenaSize);
        UI.setLineWidth(1);
        UI.drawImage("bug.png", xBug-BugSize/2, yBug-BugSize/2, BugSize, BugSize);
    }

    /**
     * Defines new position for the bug and draws it
     */
    private void newBug(){
        xBug = LeftWall + (Math.random() * ((RightWall - BugSize) - LeftWall));
        yBug = TopWall + (Math.random() * ((BotWall - BugSize) - TopWall));
    }

    /**
     * Create a new FrogSnakeGame object (which will set up the interface)
     * and then call the run method on it, which will start the game running
     */
    public static void main(String[] arguments){
        new FrogSnakeGame();
    }   

}
