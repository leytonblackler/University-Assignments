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

/** FrogGame: Core
 *  Game with two frogs that race to get to the bug first in order to eat it.
 *  (uses keys: player 1: W/A/S/D for up/left/down/right; controls the "light" frog
 *              player 2: I/J/K/L for up/left/down/right; controls the "dark" frog
 *  Frogs move around at a constant speed in an arena with an enclosing wall.
 *  When one of the frog reaches the bug, the frog eats it (grows in size),
 *       and a new bug appears at a random position within the arena walls.
 *
 *  Controls:
 *  - key to start (space)
 *  - keys to turn the two frogs  (w/a/s/d  and i/j/k/l)
 *
 *  Display:
 *   program constantly shows
 *   - the arena, the bug and the frogs
 *
 *  Constants:
 *    This class should contain constants specifying the various parameters of
 *    the game, including the geometry of the arena and obstacle.
 */

public class FrogGame{

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

    // Fields to position of the centre of the bug
    private double xBug;
    private double yBug;

    /** Constructor
     * Set up the GUI,
     * Draw the arena and the bug
     */
    public FrogGame(){
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
     * Reset the game with two new frogs in the starting positions.
     * Loop forever
     *  - move each frog one step,
     *  - if any frog has caught the bug, it grows, and a new bug appears somewhere else.
     *  - redraw the game (frogs, arena, and bug)
     */
    private void restartGame(){
        // make sure that the current game ends (if it is running)
        this.frog1 = null;
        this.frog2 = null;
        UI.sleep(3*DELAY);

        // puts a bug at a random position
        this.newBug();

        // make new frogs and call redraw 
        this.frog1 = new Frog(LeftWall+30, (TopWall+BotWall)/2, "Up", "light");
        this.frog2 = new Frog(RightWall-30, (TopWall+BotWall)/2, "Left", "dark");
        this.redraw();

        // Run the game
        while (this.frog1!=null && this.frog2!=null) {
            this.frog1.move();
            this.frog2.move();
            if (this.frog1.touching(xBug, yBug)) {
                this.frog1.grow();
                this.newBug();
            }
            else if (this.frog2.touching(xBug, yBug)) {
                this.frog2.grow();
                this.newBug();
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
     */
    private void redraw(){
        UI.clearGraphics();
        this.drawArenaBug();
        this.frog1.draw();
        this.frog2.draw();
        UI.repaintGraphics();
    }

    /**
     * Draw the arena as a rectangle with a bug in it
     */
    private void drawArenaBug(){
        UI.setColor(Color.black);
        UI.setLineWidth(2);
        UI.drawRect(LeftWall, TopWall, ArenaSize, ArenaSize);
        //UI.drawImage("pond.jpg", LeftWall, TopWall, ArenaSize, ArenaSize);
        UI.setLineWidth(1);
        UI.drawImage("bug.png", xBug-BugSize/2, yBug-BugSize/2, BugSize, BugSize);
    }

    /**
     * Defines new position for the bug and draws it
     */
    private void newBug(){
        xBug = LeftWall + BugSize/2 + Math.random()*(ArenaSize-BugSize/2);
        yBug = TopWall + BugSize/2 + Math.random()*(ArenaSize-BugSize/2);
    }

    /**
     * Create a new FrogGame object (which will set up the interface)
     * and then call the run method on it, which will start the game running
     */
    public static void main(String[] arguments){
        new FrogGame();
    }   

}
