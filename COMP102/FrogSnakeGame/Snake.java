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

/** The snake is created at a random position with a random direction.
 * The constructor does not have any parameters.
 * It can move
 *  - makes it go forward one step in its current direction.
 *  - if outside arena boundaries, makes it go backward one step, and then turn to a new (random)
 *         direction.
 *  The walls of the arena are determined by the constants:
 *    FrogSnakeGame.TopWall, FrogSnakeGame.BotWall, FrogSnakeGame.LeftWall and FrogSnakeGame.RightWall
 * It can report its current position (x and y) with the
 *  getX() and getY() methods.
 *  draw() will make it draw itself,
 *  erase() will make it erase itself
 */

public class Snake{
    /** CONSTANTS */
    public static final int SIZE = 60;
    public static final int SPEED = 2;

    /** FIELDS */
    private double xPos;
    private double yPos;
    private double distanceTravelled = 0;
    private double randomDistance = Math.random();
    private String direction;

    /** CONSTRUCTOR */
    public Snake () {
        /** Generates random x-coordinate, y-coordinate and direction. */
        this.xPos = FrogGame.LeftWall + (Math.random() * ((FrogGame.RightWall - this.SIZE) - FrogGame.LeftWall));
        this.yPos = FrogGame.TopWall + (Math.random() * ((FrogGame.BotWall - this.SIZE) - FrogGame.TopWall));
        this.direction = randomDirection();

        this.draw();
    }

    /** Determines random direction based on 1 of 4 cases. */
    public String randomDirection() {
        double randomNum = Math.random();
        if (randomNum >= 0.75) {
            return "Up";
        } else if (randomNum >=0.5 && randomNum < 0.75) {
            return "Down";
        } else if (randomNum >= 0.25 && randomNum < 0.5) {
            return "Left";
        } else if (randomNum < 0.25) {
            return "Right";
        } else {
            return null;
        }
    }

    public void move() {
        /** Find the corner points of the snake image. */
        double top = this.yPos;
        double bottom = this.yPos + this.SIZE;
        double left = this.xPos;
        double right = this.xPos + this.SIZE;
        
        /** Moves the snake upwards or changes direction of the top wall is reached. */
        if (this.direction == "Up" && top > FrogGame.TopWall+2) {               /** If the snake has not hit the top wall. */
            this.yPos = this.yPos - this.SPEED;
            this.distanceTravelled = this.distanceTravelled + this.SPEED;
        } else if (this.direction == "Up" && top < FrogGame.TopWall+2) {        /** If the snake has hit the top wall. */
            this.yPos = this.yPos + this.SPEED;
            while (this.direction == "Up") {
                this.direction = randomDirection();
            }
        }
        
        /** Moves the snake downwards or changes direction of the bottom wall is reached. */
        if (this.direction == "Down" && bottom < FrogGame.BotWall-2) {          /** If the snake has not hit the bottom wall. */
            this.yPos = this.yPos + this.SPEED;
            this.distanceTravelled = this.distanceTravelled + this.SPEED;
        } else if (this.direction == "Down" && bottom < FrogGame.BotWall+2) {      /** If the snake has hit the bottom wall. */
            this.yPos = this.yPos - this.SPEED;
            while (this.direction == "Down") {
                this.direction = randomDirection();
            }
        }
        
        /** Moves the snake left or changes direction of the left wall is reached. */
        if (this.direction == "Left" && left > FrogGame.LeftWall+2) {           /** If the snake has not hit the left wall. */
            this.xPos = this.xPos - this.SPEED;
            this.distanceTravelled = this.distanceTravelled + this.SPEED;
        } else if (this.direction == "Left" && left < FrogGame.LeftWall+2) {     /** If the snake has hit the left wall. */
            this.xPos = this.xPos + this.SPEED;
            while (this.direction == "Left") {
                this.direction = randomDirection();
            }
        }
        
        /** Moves the snake right or changes direction of the right wall is reached. */
        if (this.direction == "Right" && right < FrogGame.RightWall-2) {        /** If the snake has not hit the right wall. */
            this.xPos = this.xPos + this.SPEED;
            this.distanceTravelled = this.distanceTravelled + this.SPEED;
        } else if (this.direction == "Right" && right < FrogGame.RightWall+2) {   /** If the snake has hit the right wall. */
            this.xPos = this.xPos - this.SPEED;
            while (this.direction == "Right") {
                this.direction = randomDirection();
            }
        }
        
        /** Changes direction if a random specified distance has been travelled. */
        String previousDirection = this.direction;
        if (this.distanceTravelled > ((FrogGame.ArenaSize/3) + (this.randomDistance * ((FrogGame.ArenaSize) - (FrogGame.ArenaSize/3))))) {
            while (this.direction == previousDirection) {
                this.direction = randomDirection();
            }
            this.distanceTravelled = 0;
            this.randomDistance = Math.random();
        }
    }
    
    /** Draws the snake. */
    public void draw() {
        UI.drawImage("snake.png", this.xPos, this.yPos, this.SIZE, this.SIZE);
    }
    
    /** Finds the centre x coordinate of the snake. */
    public double getX() {
        return this.xPos + this.SIZE/2;
    }
    
    /** Finds the centre y coordinate of the snake. */
    public double getY() {
        return this.yPos + this.SIZE/2;
    }
}
