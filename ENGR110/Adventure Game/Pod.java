/** AdventureGame - Pod */
/** Leyton Blackler */

import ecs100.*;
import java.util.*;

public class Pod {
    //The name of the pod.
    private String name;
    
    //Whether the pod contains a recovery kit.
    private boolean haveRecoveryKit;
    
    //Whether the pod contains the data cache.
    private boolean haveDataCache;
    
    //The pods that each of the portals lead to.
    private Pod portalA;
    private Pod portalB;
    private Pod portalC;
    
    //The trap within the pod.
    private Trap trap;
    
    //A list of the items contained in the pod.
    private List<Item> items;

    /**
     * Constructor
     */
    public Pod(String name, boolean haveRecoveryKit, boolean haveDataCache) {
        this.name = name;
        this.haveRecoveryKit = haveRecoveryKit;
        this.haveDataCache = haveDataCache;
        this.items = new ArrayList<Item> ();
    }
    
    /**
     * Set the portal specified by the first parameter to the destination specified by the second parameter.
     *      0 = Portal A
     *      1 = Portal B
     *      2 = Portal C
     */
    public void setPortal(int portal, Pod destination) {
        /* Check if the portal number given matches one of the possible portals.
         * If it does, set the portal to the given destination.
         * Otherwise, throw an exception to indicate that there was an issue setting the portal. */
        if (portal == 0) {
            portalA = destination;
        } else if (portal == 1) {
            portalB = destination;
        } else if (portal == 2) {
            portalC = destination;
        } else {
            //Throw an excetion as the portal number given must be invalid.
            throw new IllegalArgumentException();
        }
    }
    
    /**
     * Returns the pod that the specified potral is connected to.
     *      0 = Portal A
     *      1 = Portal B
     *      2 = Portal C
     */
    public Pod getPodThroughPortal(int portalNumber) {
        /* Check if the portal number given matches one of the possible portals.
         * If it does, set the portal to the given destination.
         * Otherwise, throw an exception to indicate that there was an issue setting the portal. */
        if (portalNumber == 0) {
            return portalA;
        } else if (portalNumber == 1) {
            return portalB;
        } else if (portalNumber == 2) {
            return portalC;
        } else {
            //Throw an excetion as the portal number given must be invalid.
            throw new IllegalArgumentException();
        }
    }
    
    /**
     * Set the trap of a certain type, specified by the parameter.
     */
    public void setTrap(String type) {
        /* Check if the trap type given matches one of the possible trap types.
         * If it does, create a new trap in this pod of the given type.
         * Otherwise, throw an exception to indicate that there was an issue creating the trap. */
        if (type.equalsIgnoreCase("bomb") || 
            type.equalsIgnoreCase("well pit") || 
            type.equalsIgnoreCase("flame thrower") ||
            type.equalsIgnoreCase("spike array") ||
            type.equalsIgnoreCase("blinding arc light") ||
            type.equalsIgnoreCase("ear splitting alarm")) {
            trap = new Trap(type);
        } else {
            //Throw an excetion as the trap type given must be invalid.
            throw new IllegalArgumentException();
        }
    }
    
    public Trap getTrap() {
        return trap;
    }
    
    /**
     * Returns whether the pod contains a trap that is active.
     */
    public boolean containsActiveTrap() {
        /* If there exists a trap, return whether it is disabled or not.
         * If there is no trap in this pod, return false. */
        if (trap != null) {
            return !trap.isDisabled();
        }
        return false;
    }
    
    /**
     * Returns whether the the pod contains a given item, specified by a String of the item type.
     */
    public boolean containsItem(String itemType) {
        if (items.isEmpty()) {return false;}
        
        /* Check each of the items contained in the pod.
         * If the item matches the item specified by the parameter, return true.
         * Otherwise, return false. */
        for (Item item : items) {
            if (item.getType().equalsIgnoreCase(itemType)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Disables the trap in this pod if the items provided in the parameter can be used to disable the trap.
     */
    public boolean tryDisable(List<Item> itemsInPack) {
        /* If the trap can be disabled with the provided items, disable the trap and return true to indicate it was disabled.
         * Otherwise return false to indicate that it could no be disabled. */
        if (trap.canDisable(itemsInPack)) {
            trap.disable();
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Adds the given item to the list of items in the pod.
     */
    public void addItem(Item item) {
        if (item != null) {
            items.add(item);
        }
    }
    
    /**
     * Removes the item from the list of items in the pod.
     */
    public void removeItem(String itemType) {
        Item removedItem = null;
        
        /* Check each of the items in the pod.
         * If the item matches the item type specified, remove the item from the pod. */
        for (Item item : items) {
            if (item.getType().equalsIgnoreCase(itemType)) {
                items.remove(item);
                return;
            }
        }
    }
    
    /**
     * Returns the item in the pod given by the specified item type.
     */
    public Item getItem(String itemType) {
        /* Check each of the items in the pod.
         * If the item matches the item type specified, return that item. */
        for (Item item : items) {
            if (item.getType().equalsIgnoreCase(itemType)) {
                return item;
            }
        }
        return null;
    }
    
    /**
     * Informs the user if there is a recovery kit in the pod and what items are in the pod, if any.
     */
    public void lookAround() {
        UI.println();
        
        /* Checks if the pod has a recovery kit.
         * If it does, inform the user one exists.
         * Otherise, inform the user that it does not exist. */
        if (haveRecoveryKit) {
            UI.println("There is a recovery kit in this pod.");
        } else {
            UI.println("There is no recovery kit in this pod.");
        }
        
        /* Checks if the pod has any items.
         * If it does, print the description of each item.
         * Otherise, inform the user that there are no items in the pod. */
        if (items.isEmpty()) {
            UI.println("There are no items in this pod.");
        } else {
            UI.println("Items in this pod:");
            for (Item item : items) {
                item.printItem();
            }
        }
    }
    
    /**
     * Searches the pod for the data cache.
     * Returns true if it was found or false if it was not found.
     * Will not necessarily find the data cache every time, regardless whether it contains it or not.
     */
    public boolean search(double probability) {
        /* Return true if the pod has the data cache and  the specified probability outcome is true.
         * Otherwise, return false. */
        if (Math.random() < probability && haveDataCache) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Returns whether the pod contains the data cache.
     */
    public boolean hasRecoveryKit() {
        return haveRecoveryKit;
    }
    
    /**
     * Prints a description of the pod.
     * Informs the user:
     *      - The name of the pod.
     *      - If the pod contains a trap.
     *      - If the trap is active.
     */
    public void printDescription() {
        UI.println();
        UI.println("You are currently in: " + name + ".");
        if (trap != null) {
            String status;
            if (trap.isDisabled()) {
                status = " a disabled ";
            } else {
                status = " an active ";
            }
            UI.println("There is" + status + trap.getType().toLowerCase() + " trap in this pod.");
        } else {
            UI.println("There is no trap in this pod.");
        }
    }
}
