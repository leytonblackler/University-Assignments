/** AdventureGame - Trap */
/** Leyton Blackler */


import ecs100.*;
import java.util.*;

public class Trap {
    //The type of trap.
    private String type;
    
    //Whether the trap has been disabled.
    private boolean disabled;
    
    //An array of the item types that can be used to disable this trap.
    private String[] requiredItems;

    /**
     * Constructor
     */
    public Trap(String type) {
        //Fixes the capitalisation of the type string.
        type = fixCapitalisation(type);
        
        this.type = type;
        this.disabled = false;
        this.requiredItems = new String[2];
        
        //Specify the items that can be used to disabled the trap based on the type of trap.
        if (type.equals("Bomb")) {
            requiredItems[0] = "Defuser";
        } else if (type.equals("Well Pit")) {
            requiredItems[0] = "Well Cover";
            requiredItems[1] = "Plank";
        } else if (type.equals("Flame Thrower")) {
            requiredItems[0] = "Fire Extinguisher";
            requiredItems[1] = "Fire Blanket";
        } else if (type.equals("Spike Array")) {
            requiredItems[0] = "Remote Gripper";
        } else if (type.equals("Blinding Arc Light")) {
            requiredItems[0] = "Welding Hood";
        } else if (type.equalsIgnoreCase("Ear Splitting Alarm")) {
            requiredItems[0] = "Ear Muffs";
        }
    }
    
    /**
     * Converts the type of the item into a string with proper capitalisation, due to inconsitencies in the game data file.
     */
    public String fixCapitalisation(String type) {
        if (type.equalsIgnoreCase("bomb")) {
            return "Bomb";
        } else if (type.equalsIgnoreCase("well pit")) {
            return "Well Pit";
        } else if (type.equalsIgnoreCase("flame thrower")) {
            return "Flame Thrower";
        } else if (type.equalsIgnoreCase("spike array")) {
            return "Spike Array";
        } else if (type.equalsIgnoreCase("blinding arc light")) {
            return "Blinding Arc Light";
        } else if (type.equalsIgnoreCase("ear splitting alarm")) {
            return "Ear Splitting Alarm";
        }
        return type;
    }
    
    /**
     * Returns whether the trap has been disabled.
     */
    public boolean isDisabled() {
        return disabled;
    }
    
    /**
     * Disables the trap.
     */
    public void disable() {
        disabled = true;
    }
    
    /**
     * Prints the type of trap.
     */
    public void print() {
        UI.println(type);
    }
    
    /**
     * Returns the type of trap.
     */
    public String getType() {
        return type;
    }
    
    /**
     * Returns whether the trap can be disabled based on the items provided in the parameter.
     */
    public boolean canDisable(List<Item> itemsInPack) {
        //For each of the items that were specified as being able to disabled the trap, check if they are contained in the player's pack.
        for (String requiredItem : requiredItems) {
            for (Item itemInPack : itemsInPack) {
                //Get the type of the that is in the player's pack.
                String itemInPackString = itemInPack.getType();
                
                /* Check if the type of item in the player's pack matches one of the required item item types.
                 * If it does, return true to show that that trap can be disabled. */
                if (requiredItem != null && requiredItem.equals(itemInPackString)) {
                    return true;
                }
            }
        }
        
        //Return false to indicate that none of the required items to disable the trap were found in the player's pack.
        return false;
    }
}
