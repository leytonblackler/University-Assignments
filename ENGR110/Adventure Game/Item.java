/** AdventureGame - Item */
/** Leyton Blackler */

import ecs100.*;
import java.util.*;

public class Item {
    //The type of item.
    private String type;
    
    //The weight of the item.
    private double weight;

    /**
     * Constructor
     */
    public Item(String type, double weight) {
        type = fixCapitalisation(type);
        this.type = type;
        this.weight = weight;
    }
    
    /**
     * Converts the type of the item into a string with proper capitalisation, due to inconsitencies in the game data file.
     */
    public String fixCapitalisation(String type) {
        if (type.equalsIgnoreCase("torch")) {
            return "Torch";
        } else if (type.equalsIgnoreCase("remote gripper")) {
            return "Remote Gripper";
        } else if (type.equalsIgnoreCase("defuser")) {
            return "Defuser";
        } else if (type.equalsIgnoreCase("well cover")) {
            return "Well Cover";
        } else if (type.equalsIgnoreCase("fire extinguisher")) {
            return "Fire Extinguisher";
        } else if (type.equalsIgnoreCase("fire blanket")) {
            return "Fire Blanket";
        } else if (type.equalsIgnoreCase("plank")) {
            return "Plank";
        } else if (type.equalsIgnoreCase("welding hood")) {
            return "Welding Hood";
        } else if (type.equalsIgnoreCase("ear muffs")) {
            return "Ear Muffs";
        }
        return type;
    }
    
    /**
     * Prints the type of the item and it's weight in a bullet point list form.
     */
    public void printItem() {
        UI.println(" - " + type + " (weight: " + weight + "kg)");
    }
    
    /**
     * Returns the item type.
     */
    public String getType() {
        return type;
    }
    
    /**
     * Returns the weight of the item.
     */
    public double getWeight() {
        return weight;
    }
}
