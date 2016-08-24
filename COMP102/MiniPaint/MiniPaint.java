// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP102 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP 102 Assignment 6
 * Name: Leyton Blackler
 * Usercode: blacklleyt
 * ID: 300368625
 */

import ecs100.*;
import java.awt.Color;
import javax.swing.JColorChooser;

public class MiniPaint{

    /** FIELDS */
    private String brush;
    private String imageFile;
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    private double brushSize;
    private boolean filled;
    private Color color;

    private double prevX;
    private double prevY;
    private double width;
    private double height;
    private boolean shapeDone = true;

    /** CONSTRUCTOR */
    public MiniPaint(){
        UI.setMouseMotionListener(this::doMouse);

        UI.addButton("Line", this::setLine);
        UI.addButton("Rectangle", this::setRectangle);
        UI.addButton("Oval", this::setOval);
        UI.addButton("Image", this::setImage);
        UI.addButton("Concentric Circle", this::setConcentricCircle);
        UI.addButton("Pen", this::setPen);
        UI.addButton("Eraser", this::setEraser);
        UI.addButton("Set colour", this::setColor);
        UI.addButton("Set brush size", this::setBrushSize);
        UI.addButton("Toggle fill", this::toggleFill);
        UI.addButton("Clear", this::clear);
        UI.addButton("Quit", UI::quit);

        this.filled = false;
        this.brush = "Line";
        this.brushSize = 20;
        this.color = Color.black;

        displaySettings();
    }

    /**
     * Respond to mouse events
     * When pressed, remember the position.
     * When released, draw the current shape using the pressed position
     *  and the released position.
     * Uses the value in the field to determine which kind of shape to draw.
     * Although you could do all the drawing in this method,
     *  it may be better to call some helper methods for the more
     *  complex actions (and then define the helper methods!)
     */
    public void doMouse(String action, double x, double y) {
        /** Set fields for the start x and y coordinates. */
        if (action.equals("pressed")) {
            this.startX = x;
            this.startY = y;            
        }
        
        if (action.equals("dragged")) {
            /** Execute the drawing tools while the mouse is being dragged. */
            if (brush.equals("Eraser")) {
                drawErase(x, y);      
            } else if (brush.equals("Pen")) {
                drawPen(x, y);
            }
            
            /** Re-invert the previously inverted shape and draw a new inverted shape at the mouse position. */
            if (brush.equals("Line")) {
                if (!shapeDone) {
                    UI.invertLine(this.startX, this.startY, this.prevX, this.prevY);
                }
                drawDraggedShape(x, y);
            } else if (brush.equals("Rectangle")) {
                if (!shapeDone) {
                    UI.invertRect(this.prevX, this.prevY, this.width, this.height);
                }
                drawDraggedShape(x, y);
            } else if (brush.equals("Oval")) {
                if (!shapeDone) {
                    UI.invertOval(this.prevX, this.prevY, this.width, this.height);
                }
                drawDraggedShape(x, y);
            }
        }
        
        /** Draw the given shape/image upon release of the mouse. */
        if (action.equals("released")) {
            this.endX = x;
            this.endY = y;

            if (brush.equals("Line")) {
                drawLine();
            } else if (brush.equals("Rectangle")) {
                drawRectangle();
            } else if (brush.equals("Oval")) {
                drawOval();
            } else if (brush.equals("Image")) {
                drawImage();
            } else if (brush.equals("Concentric Circle")) {
                drawConcentricCircle();
            }

            this.shapeDone = true;
        }
    }
    
    /** Draws the shape as it is being drgged out (using invert functions). */
    public void drawDraggedShape(double currentX, double currentY) {
        double x;
        double y;

        if (this.startX < currentX) {
            width = currentX - startX;
            x = this.startX;
            this.prevX = this.startX;
        } else {
            width = this.startX - currentX;
            x = currentX;
            this.prevX = currentX;
        }

        if (this.startY < currentY) {
            height = currentY - this.startY;
            y = this.startY;
            this.prevY = this.startY;
        } else {
            height = this.startY - currentY;
            y = currentY;
            this.prevY = currentY;
        }

        if (brush.equals("Line")) {
            UI.invertLine(this.startX, this.startY, currentX, currentY);
            this.prevX = currentX;
            this.prevY = currentY;
        } else if (brush.equals("Rectangle")) {
            UI.invertRect(x, y, width, height);
        } else if (brush.equals("Oval")) {
            UI.invertOval(x, y, width, height);
        }

        this.shapeDone = false;
    }
    
    /** Draws a line using the start and end mouse coordinates. */
    public void drawLine() {
        UI.setColor(this.color);
        UI.drawLine(this.startX, this.startY, this.endX, this.endY);
    }
    
    /** Draws a rectangle using the start and end mouse coordinates. */
    public void drawRectangle() {
        double width;
        double height;
        double x;
        double y;

        if (this.startX < this.endX) {
            width = this.endX - this.startX;
            x = this.startX;
        } else {
            width = this.startX - this.endX;
            x = this.endX;
        }

        if (this.startY < this.endY) {
            height = this.endY - this.startY;
            y = this.startY;
        } else {
            height = this.startY - this.endY;
            y = this.endY;
        }

        UI.setColor(this.color);
        if (this.filled) {
            UI.drawRect(x, y, width, height);
            UI.fillRect(x, y, width, height);
        } else {
            UI.drawRect(x, y, width, height);
        }       
    }
    
    /** Draws an image using the start and end mouse coordinates. */
    public void drawImage() {
        double width;
        double height;
        double x;
        double y;

        if (this.startX < this.endX) {
            width = this.endX - this.startX;
            x = this.startX;
        } else {
            width = this.startX - this.endX;
            x = this.endX;
        }

        if (this.startY < this.endY) {
            height = this.endY - this.startY;
            y = this.startY;
        } else {
            height = this.startY - this.endY;
            y = this.endY;
        }
        try {
            UI.drawImage(this.imageFile, x, y, width, height);
        } catch (Exception e) {}

    }

    /** Draws an oval using the start and end mouse coordinates. */
    public void drawOval() {
        double width;
        double height;
        double x;
        double y;

        if (this.startX < this.endX) {
            width = this.endX - this.startX;
            x = this.startX;
        } else {
            width = this.startX - this.endX;
            x = this.endX;
        }

        if (this.startY < this.endY) {
            height = this.endY - this.startY;
            y = this.startY;
        } else {
            height = this.startY - this.endY;
            y = this.endY;
        }

        UI.setColor(this.color);
        if (this.filled) {
            UI.drawOval(x, y, width, height);
            UI.fillOval(x, y, width, height);
        } else {
            UI.drawOval(x, y, width, height);
        }
    }

    /** Draws a concentric using the start and end mouse coordinates. */
    public void drawConcentricCircle() {
        double width;
        double height;
        double x;
        double y;

        if (this.startX < this.endX) {
            width = this.endX - this.startX;
            x = this.startX;
        } else {
            width = this.startX - this.endX;
            x = this.endX;
        }

        if (this.startY < this.endY) {
            height = this.endY - this.startY;
            y = this.startY;
        } else {
            height = this.startY - this.endY;
            y = this.endY;
        }

        UI.setColor(this.color);
        UI.drawOval(x, y, width, height);

        int gap = 5;
        int circleCounter = 1;
        int totalCircles = ((int)width/2)/gap;
        while (circleCounter < totalCircles) {
            x = x+gap;
            y = y+gap;
            width = width-2*gap;
            height = height-2*gap;

            UI.drawOval(x, y, width, height);
            circleCounter++;
        }
    }
    
    /** Draws a circle at the current mouse point. */
    public void drawPen(double x, double y) {
        UI.setColor(this.color);
        UI.fillOval(x-this.brushSize/2, y-this.brushSize/2, this.brushSize, this.brushSize);
    }
    
    /** Draws a white circle at the current mouse point. */
    public void drawErase(double x, double y) {
        UI.setColor(Color.white);
        UI.fillOval(x-this.brushSize/2, y-this.brushSize/2, this.brushSize, this.brushSize);
    }
    
    /** Sets 'Line' as the current drawing tool. */
    public void setLine() {
        this.brush = "Line";
        displaySettings();
    }
    
    /** Sets 'Rectangle' as the current drawing tool. */
    public void setRectangle() {
        this.brush = "Rectangle";
        displaySettings();
    }
    
    /** Sets 'Oval' as the current drawing tool. */
    public void setOval() {
        this.brush = "Oval";
        displaySettings();
    }
    
    /** Sets 'Concentric Circle' as the current drawing tool. */
    public void setConcentricCircle() {
        this.brush = "Concentric Circle";
        displaySettings();
    }
    
    /** Sets 'Pen' as the current drawing tool. */
    public void setPen() {
        this.brush = "Pen";
        displaySettings();
    }
    
    /** Sets 'Eraser' as the current drawing tool. */
    public void setEraser() {
        this.brush = "Eraser";
    }
    
    /** Sets 'Image' as the current drawing tool. */
    public void setImage() {
        this.imageFile = UIFileChooser.open("Please select an image.");
        this.brush = "Image";
        displaySettings();
    }
    
    /** Allows the user to choose and set a drawing colour. */
    public void setColor() {
        this.color = JColorChooser.showDialog(null, "Select a colour.", Color.white);
        displaySettings();
    }
    
    /** Allows the user to choose and set a brush size. */
    public void setBrushSize() {
        UI.clearText();
        this.brushSize = UI.askDouble("Enter brush size:");
        displaySettings();
    }
    
    /** Allows the user to toggle between either filled shapes or unfilled shapes. */
    public void toggleFill() {
        if (this.filled) {
            this.filled = false;
        } else {
            this.filled = true;
        }
        displaySettings();
    }
    
    /** Clears the graphics pane. */
    public void clear() {
        UI.clearGraphics();
    }
    
    /** Displays/updates the current drawing settings. */
    public void displaySettings() {
        /** Clears previously displayed settings. */
        UI.clearText();

        /** Displays the current shape/brush setting. */
        UI.println("Current shape/brush: " + this.brush);

        /** Displays the current shape fill setting. */
        if (this.filled) {
            UI.println("Shapes will be drawn filled.");
        } else {
            UI.println("Shapes will be drawn unfilled.");
        }

        /** Displays the current colour setting. */
        int red = this.color.getRed();
        int green = this.color.getGreen();
        int blue = this.color.getBlue();
        UI.println("Selected colour: " + red + " " + green + " " + blue);

        /** Displays the current brush size setting. */
        UI.println("Brush size: " + this.brushSize);
    }

    // Main:  constructs a new MiniPaint object
    public static void main(String[] arguments){
        new MiniPaint();
    }        

}
