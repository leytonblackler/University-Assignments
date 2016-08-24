/** AdventureGame - Player */
/** Leyton Blackler */

import ecs100.*;
import java.util.*;
import java.text.DecimalFormat;

public class Player {
    //The current health level of the player.
    private int health;
    
    //The current weight of the player's pack.
    private double packWeight;
    
    //Whether the player has found the data cache.
    private boolean haveDataCache;
    
    //A list of the items that player is carrying in their pack.
    private List<Item> itemsInPack;
    
    //Constant value for the maximum weight of the player's pack.
    private static double MAX_PACK_WEIGHT = 10;
    
    //Constant value for the maximum health points the player can have.
    private static int MAX_HEALTH_POINTS = 100;

    /**
     * Constructor
     */
    public Player() {
        this.health = 100;
        this.packWeight = 0;
        this.haveDataCache = false;
        itemsInPack = new ArrayList<Item> ();
    }
    
    /**
     * Decreases the player's health by a random amount from 5 to 20 points.
     * Returns the random amount that it was decreased by.
     */
    public int decreaseHealth() {
        //Calculate a random amount to decrease the health by.
        Random rand = new Random();
        int decrease = rand.nextInt(16) + 5;
        
        //Ensure that the health is not decreased below the minimum amount.
        if (health - decrease < 0) {
            decrease = health;
        }
        health -= decrease;
        
        //Return the random amount that it was decreased by.
        return decrease;
    }
    
    /**
     * Increases the player's health by a random amount from 10 to 30 points.
     * Returns the random amount that it was increased by.
     */
    public int increaseHealth() {
        //Calculate a random amount to increase the health by.
        Random rand = new Random();
        int increase = rand.nextInt(21) + 10;
        
        //Ensure that the health is not increased beyond the maximum amount.
        if (health + increase > 100) {
            increase = 100 - health;
        }
        health += increase;
        
        //Return the random amount that it was increased by.
        return increase;
    }
    
    /**
     * Returns the player's current health level.
     */
    public int getHealth() {
        return health;
    }
    
    /**
     * Prints the player's current health level in a form out of the maximum health points.
     */
    public void printHealth() {
        UI.println("Current health: " + health + "/" + MAX_HEALTH_POINTS);
    }
    
    /**
     * Adds the given item to the player's pack.
     * Returns whether the item was successfully added or not based on weight limit.
     */
    public boolean addItemToPack(Item item) {
        /* If the pack will become too heavy with the added item, do not add the item and return false.
         * Otherwise, add the item to the player's pack and increase the current total weight of the pack. */
        if (packWeight + item.getWeight() > MAX_PACK_WEIGHT) {
            return false;
        } else {
            itemsInPack.add(item);
            packWeight += item.getWeight();
            return true;
        }
    }
    
    /**
     * Remove the item specified by the type from the player's pack.
     * Returns the item that was removed.
     */
    public Item dropItem(String itemType) {
        Item droppedItem = null;
        
        /* For each of the items in the pack, check if the item matches the given item type.
         * If it does, remove the item from the pack. */
        for (Item item : itemsInPack) {
            if (item.getType().equalsIgnoreCase(itemType)) {
                droppedItem = item;
                break;
            }
        }
        itemsInPack.remove(droppedItem);
        
        //Decrease the currentl total weight of the player's pack.
        packWeight -= droppedItem.getWeight();
        
        //Return the item that was removed from the player's pack.
        return droppedItem;
    }
    
    /**
     * Returns whether the player's pack contains the item specified by the type.
     */
    public boolean hasItem(String itemType) {
        if (itemsInPack.isEmpty()) {return false;}
        
        /* For each of the items in the player's pack, check if it matches the specified item type.
         * If it does return that item. */
        for (Item item : itemsInPack) {
            if (item.getType().equalsIgnoreCase(itemType)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Set the field indicating the player has found the data cache.
     */
    public void collectDataCache() {
        haveDataCache = true;
    }
    
    /**
     * Returns whether the player has found the data cache.
     */
    public boolean hasDataCache() {
        return haveDataCache;
    }
    
    /**
     * Returns whether the player is dead (health is 0 or below).
     */
    public boolean isDead() {
        return health <= 0;
    }
    
    /**
     * Returns a list of all the items currently in the player's pack.
     */
    public List<Item> getItemsInPack() {
        return itemsInPack;
    }
    
    /**
     * Print a list of all the items in the player's pack, as well as whether the player has the data cache.
     */
    public void printItemsInPack() {
        UI.println();
        
        //Inform the player whether they have the data cache or not.
        if (haveDataCache) {
            UI.println("You have the data cache.");
        } else {
            UI.println("You do not have the data cache.");
        }
        
        //Format the excess weight value so that there is only 1 decimal place.
        DecimalFormat df = new DecimalFormat("0.#");
        
        //Print the total current weight of the pack in a form out of the maximum weight allowed.
        UI.println("Weight of pack: " + df.format(packWeight) + "kg out of a maximum of " + MAX_PACK_WEIGHT + "kg.");
        
        /* If there are no items in the pack, inform the user that the pack is empty.
         * Otherwise, print a list of the items currently in the pack. */
        if (itemsInPack.isEmpty()) {
            UI.println("There are no items in your pack.");
        } else {
            UI.println("Items currently in pack:");
            for (Item item : itemsInPack) {
                item.printItem();
            }
        }
    }
    
    /**
     * Returns how much weight the player's pack will be overweight by if the given item were to be added.
     */    
    public double exceedingWeight(Item item) {
        return packWeight + item.getWeight() - MAX_PACK_WEIGHT;
    }
    
    /**
     * Returns the current weight of the player's pack.
     */
    public double getPackWeight() {
        return packWeight;
    }
}
