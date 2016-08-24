// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP102 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP102 Assignment
 * Name: Leyton Blackler
 * Usercode: blacklleyt
 * ID: 300368625
 */

import ecs100.*;
import java.awt.Color;
import java.awt.Image;

/** Frog
 *  A new frog starts at the given position, with the given direction, and 
 *     has either a "light" or "dark" shade.
 *  Frogs can turn in 4 directions: left, right, up, and down. 
 *  Frogs move around at a constant speed in an arena with an enclosing wall,
 *     following its direction, until it hits a wall. In which case it stops moving.
 *  Frog can grow in size, and (for the completion) can also shrink by resetting their size
 *      to the orginal value.
 *
 *  The walls of the arena are determined by the constants:
 *    FrogGame.TopWall, FrogGame.BotWall, FrogGame.LeftWall and FrogGame.RightWall
 */

public class Frog {
    // Constants
    public static final int INITIAL_SIZE = 40;
    public static final int GROWTH_RATE = 2;
    public static final int SPEED = 2;

    // Fields
    private String direction;
    private double size;
    private double xPos;
    private double yPos;
    private boolean moving = true;
    private String frogShade;

    //Constructor 
    /** 
     * Make a new frog
     * The parameters specify the initial position of the top left corner,
     *   the direction, and the shade of the Frog ("light" or "dark")
     * We assume that the position is within the boundaries of the arena
     */
    public Frog(double left, double top, String dir, String shade)  {
        this.xPos = left;
        this.yPos = top;
        this.direction = dir;
        this.frogShade = shade;
        this.size = this.INITIAL_SIZE;
        this.draw();
    }

    /**
     * Turn right
     */
    public void turnRight(){
        this.direction = "Right";
    }

    /**
     * Turn left
     */
    public void turnLeft(){
        this.direction = "Left";
    }

    /**
     * Turn up
     */
    public void turnUp(){
        this.direction = "Up";
    }

    /**
     * Turn down
     */
    public void turnDown(){
        this.direction = "Down";
    }

    /**
     * Moves the frog: 
     *   use SPEED unit forward in the correct direction
     *   by changing the position of the frog.
     * Make sure that the frog does not go outside the arena, by making sure 
     *  - the top of the frog is never smaller than FrogGame.TopWall
     *  - the bottom of the frog is never greater than FrogGame.BotWall
     *  - the left of the frog is never smaller than FrogGame.LeftWall
     *  - the right of the frog is never smaller than FrogGame.RightWall
     */
    public void move() {
        double top = this.yPos;
        double bottom = this.yPos + this.size;
        double left = this.xPos;
        double right = this.xPos + this.size;

        if (this.direction == "Up" && top > FrogGame.TopWall+2) {
            this.yPos = this.yPos - this.SPEED;
        }

        if (this.direction == "Down" && bottom < FrogGame.BotWall-2) {
            this.yPos = this.yPos + this.SPEED;
        }

        if (this.direction == "Left" && left > FrogGame.LeftWall+2) {
            this.xPos = this.xPos - this.SPEED;
        }

        if (this.direction == "Right" && right < FrogGame.RightWall-2) {
            this.xPos = this.xPos + this.SPEED;
        }
        
        if (this.direction == "Up" && top > FrogGame.TopWall+2 ||
            this.direction == "Down" && bottom < FrogGame.BotWall-2 ||
            this.direction == "Left" && left > FrogGame.LeftWall+2 ||
            this.direction == "Right" && right < FrogGame.RightWall-2) {
            this.moving = true;
        } else {
            this.moving = false;
        }
    }
    
    /**
     * Check whether the frog is touching the given point, eg, whether the
     *   given point is included in the area covered by the frog.
     * Return true if the frog is on the top of the position (x, y)
     * Return false otherwise
     */
    public boolean touching(double x, double y) {
        if (x >= this.xPos && x <= (this.xPos + this.size)
            && y >= this.yPos && y <= (this.yPos + this.size)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * The Frog has just eaten a bug
     * Makes the frog grow larger by GROWTH_RATE.
     */
    public void grow(){
        this.size = this.size + this.GROWTH_RATE;
    }

    /**
     * The Frog has just bumped into a snake
     * Makes the frog size reset to its original size
     * ONLY NEEDED FOR COMPLETION
     */
    public void shrink(){
        this.size = this.INITIAL_SIZE;
    }
    
    /**
     * Returns the current number of mosquitos eaten.
     */
    public int getMosquitoCount(){
        return (int)(this.size - this.INITIAL_SIZE)/2;
    }

    /**
     * Draws the frog at the current position.
     */
    public void draw(){
        /** Draws the frog facing upwards. */
        if (this.direction.equals("Up")) {
            if (this.moving) {
                UI.drawImage(this.frogShade + "FrogUpMoving.png", this.xPos, this.yPos, this.size, this.size);
            } else {
                UI.drawImage(this.frogShade + "FrogUpCrouched.png", this.xPos, this.yPos, this.size, this.size);
            }
        }

        /** Draws the frog facing downwards. */
        if (this.direction.equals("Down")) {
            if (this.moving) {
                UI.drawImage(this.frogShade + "FrogDownMoving.png", this.xPos, this.yPos, this.size, this.size);
            } else {
                UI.drawImage(this.frogShade + "FrogDownCrouched.png", this.xPos, this.yPos, this.size, this.size);
            }
        }

        /** Draws the frog facing left. */
        if (this.direction.equals("Left")) {
            if (this.moving) {
                UI.drawImage(this.frogShade + "FrogLeftMoving.png", this.xPos, this.yPos, this.size, this.size);
            } else {
                UI.drawImage(this.frogShade + "FrogLeftCrouched.png", this.xPos, this.yPos, this.size, this.size);
            }
        }

        /** Draws the frog facing right. */
        if (this.direction.equals("Right")) {
            if (this.moving) {
                UI.drawImage(this.frogShade + "FrogRightMoving.png", this.xPos, this.yPos, this.size, this.size);
            } else {
                UI.drawImage(this.frogShade + "FrogRightCrouched.png", this.xPos, this.yPos, this.size, this.size);
            }
        }
    }
}

