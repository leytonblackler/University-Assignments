/** AdventureGame */
/** Leyton Blackler */

import ecs100.*;
import java.awt.Color;
import java.util.*;
import java.io.*;
import java.text.DecimalFormat;

/** AdventureGame   */
public class AdventureGame{
    private Player player;
    private List <Pod> allPods;
    private Pod currentPod;
    private Pod dockingPod;
    private boolean recoveryKitUsed;
    private boolean gameOver;
    
    /** Construct a new AdventureGame object and initialise the interface */
    public AdventureGame(){
        UI.initialise();
        UI.addButton("List Pack", this::doList);
        UI.addButton("Portal A", ()->{this.goPortal(0);});
        UI.addButton("Portal B", ()->{this.goPortal(1);});
        UI.addButton("Portal C", ()->{this.goPortal(2);});
        UI.addButton("Disable Trap", this::doDisable);
        UI.addButton("Look", this::doLook);
        UI.addButton("Search", this::doSearch);
        UI.addButton("Pick Up Item", this::doPickUpItem);
        UI.addButton("Drop Item", this::doDropItem);
        UI.addButton("Use Kit", this::useRecoveryKit);
        UI.addButton("Check Health", this::checkHealth);
        UI.addButton("Restart", this::restartGame);
        UI.addButton("Quit", UI::quit);

        UI.setDivider(1.0);  //show only the text pane
        this.initialiseGame();
    }

    /** List the items in the player's pack */
    public void doList(){
        if (player==null || gameOver){return;}
        
        //Print the items that are currently in the player's pack.
        player.printItemsInPack();
    }
    
    /** Prints the player's current health level */
    public void checkHealth() {
        if (player==null || gameOver){return;}
        UI.println();
        
        //Print the player's current health level.
        player.printHealth();
    }

    /** Exit the current pod going through the specified portal number */
    public void goPortal(int num){
        if (player==null || gameOver){return;}
        
        //Change the current pod to the pod through the provided portal.
        currentPod = currentPod.getPodThroughPortal(num);
        
        //Reset the field indicating whether the current pod's recovery kit has been used.
        recoveryKitUsed = false;
        
        //Print a description of the current pod.
        currentPod.printDescription();
        
        //If the player is in the docking pod with the data cache, inform the player that they have won and end the game.
        if (currentPod == dockingPod && player.hasDataCache()) {
            UI.println();
            UI.println("===============================================");
            UI.println("===== DATA CACHE RETURNED TO DOCKING POD! =====");
            UI.println("=====              YOU WIN!               =====");
            UI.println("===============================================");
            gameOver = true;
        }
    }

    /** Look around at the pod and report what's there (except for datacache)*/
    public void doLook(){
        if (player==null || checkTrap() || gameOver){return;}
        //'Look around' the pod by requesting a description of the pod.
        currentPod.lookAround();
    }

    /** Search for the data cache, and pick it up if it is found.
     *  If the player has a torch, then there is a higher probability of
     *  finding the datacache (assuming it is in the pod) than if the player
     *  doesn't have a torch.
     */
    public void doSearch(){
        if (player==null || checkTrap() || gameOver){return;}
        UI.println();
        
        /* If the player has the data cache, inform the user that the search is unnecessary.
         * Otherwise, check if the current pod has the data cache. */
        if (player.hasDataCache()) {
            UI.println("You have already found the data cache!");
        } else {
            /* Calculate the probability of finding the cache, provided it exists, based on whether the user has a torch.
             * There will be a 75% chance of finding the cache if the user has a torch.
             * Or a 25% chance if the user does not have a torch. */
            double probability;
            if (player.hasItem("Torch")) {
                probability = 0.75;
            } else {
                probability = 0.25;
            }
            
            /* Search the pod using the calculated probability.
             * If the data cache is found, set the player to have found it.
             * Otherwise, infrom the player that it could not be found. */
            if (currentPod.search(probability)) {
                UI.println("You found the data cache!");
                
                //Set the player to have found the data cache.
                player.collectDataCache();
            } else {
                UI.println("The data cache was not found.");
            }
        }
    }

    /** Attempt to pick up an item from the pod and put it in the pack.
     *  If item makes the pack too heavy, then puts the item back in the pod */
    public void doPickUpItem(){
        if (player==null || checkTrap() || gameOver){return;}
        UI.println();
        
        //Ask the user for the item they would like to pick up.
        String itemType = UI.askString("Enter the item you would like to pick up: ");
        
        /* If the current pod has the specified item, remove the item from the current pod and add it to the player's pack.
         * Otherwise, inform the user that it could not be picked up as the specified item is not in the current pod. */
        if (currentPod.containsItem(itemType)) {
            //Find the specified item in the current pod.
            Item item = currentPod.getItem(itemType);
            
            /* Check if the player is able to pick up the item (based on how heavy their pack is).
             * If picking up the item doesn't exceeed the maximum weight limit, add the item to the player's pack.
             * Otherwise, inform the player that the item will make the pack too heavy. */
            if (player.addItemToPack(item)) {
                //Remove the item from the current pod since it was sucessfully added to the player's pack.
                currentPod.removeItem(itemType);
                UI.println("Succssfully picked item up.");
            } else {
                //Inform the user that the item was not successfullly added to their pack.
                UI.println("Could not pick up item, your pack is too heavy.");
                
                //Find how much excess weight picking up the item would cause.
                double overWeight = player.exceedingWeight(item);
                
                //Format the excess weight value so that there is only 1 decimal place.
                DecimalFormat df = new DecimalFormat("0.#");
                
                //Inform the user how much excess weight picking up the item would cause.
                UI.println("Picking up this item will exceed the capacity of your pack by " + df.format(overWeight) + "kg.");
            }
        } else {
            UI.println("Could not pick up item, the specified item is not in this pod.");
        }
    }

    /** Attempt to drop an item from the pack. */
    public void doDropItem(){
        if (player==null || checkTrap() || gameOver){return;}
        UI.println();
        
        //Ask the user for the item they would like to drop.
        String itemType = UI.askString("Enter the item you would like to drop: ");
        
        /* If the player has the specified item in their pack, remove the item from the player's pack and add it to the current pod.
         * Otherwise, inform the user that it could not be dropped as the specified item is not in their pack. */
        if (player.hasItem(itemType)) {
            //Remove the item from the player's pack and add it to the current pod.
            currentPod.addItem(player.dropItem(itemType));
            UI.println("Successfully dropped item.");
        } else {
            UI.println("You can't drop that item because you don't have it.");
        }
    }                

    /** Attempt to disable the trap in the current pod.
     * If there is no such trap, or it is already disabled, return immediately.
     * If disabling the trap with the players current pack of items doesn't work,
     *  the player is damaged. If their health is now <=0, then the game is over
     */
    public void doDisable(){
        if (player==null || !currentPod.containsActiveTrap() || gameOver){return;}

        /* Try disable the trap in the pod with the items currently in the player's pack.
         * If the trap was successfully disabled, inform the user.
         * Otherwise, trigger the trap in the room. */
        if (currentPod.tryDisable(player.getItemsInPack())) {
            UI.println();
            UI.println("The trap was successfully disabled.");
        } else {
            UI.println("You do not have the required item to disable this trap!");
            checkTrap();
        }
    }

    /** If there is a recovery kit in the pod that hasn't been already used on
     *  this visit, then use it (increase the player's health) and remember that
     *  the kit has now been used.
     */
    public void useRecoveryKit(){
        if (player==null || checkTrap() || gameOver){return;}
        UI.println();
        
        /* If the current pod has a recovery kit, check if it has been used.
         * Otherwise inform the player that there is no recovery kit available. */
        if (currentPod.hasRecoveryKit()) {
            /* If the recovery kit hasn't been used yet, heal the player.
             * Otherwise inform the player that it can't be used again. */
            if (recoveryKitUsed) {
                UI.println("The recovery kit in this pod has already been used this visit.");
            } else {
                /* Increase the player's health and record how much it was increased by.
                 * (random amount, generated from range in Player class.) */
                int increase = player.increaseHealth();
                
                //Inform the user how much health was restored.
                UI.println("The recovery kit restored " + increase + " health points!");
                
                //Print the player's new health level.
                player.printHealth();
                
                //Set the field boolean to indicate that the recovery kit has now been used.
                recoveryKitUsed = true;
            }
        } else {
            UI.println("This pod does not have a recovery kit.");
        }
    }

    /** Check if there is an active trap. If so, set it off and damage the player.
     *  Returns true if the player got damaged. 
     */
    private boolean checkTrap(){
        if (currentPod.containsActiveTrap() && !gameOver) {
            UI.println();
            
            /* Decrease the player's health and record how much it was decreased by.
             * (random amount, generated from range in Player class.) */
            int decrease = player.decreaseHealth();
            
            //Inform the user how much their health was decreased by.
            UI.println("The trap decreased your health points by " + decrease + "!");
            
            //Print the player's new health level.
            player.printHealth();
            
            // If the player is dead (health 0 or below), then end the game.
            if (player.isDead()) {
                UI.println();
                UI.println("=========================");
                UI.println("===== YOU ARE DEAD! =====");
                UI.println("=====  GAME OVER!   =====");
                UI.println("=========================");
                gameOver = true;
            }
            
            //Return true to indicate that there was a trap and the player was damaged.
            return true;
        }
        
        //Return false to indicate that there was no trap.
        return false;
    }
    
    /** Confirm that the user wants to restart the game.
     *  Re-initilaise the game if they do.
     */
    private void restartGame() {
        UI.println();
        
        //Ask the user if they are sure they would like to restart the game.
        UI.println("Are you sure? This will erase the current game.");
        UI.println("Type 'yes' to confirm.");
        
        /* If the user confirms, re-initialise the game.
         * Otherwise, do nothing. */
        if (UI.askString("").equalsIgnoreCase("yes")) {
            initialiseGame();
        } else {
            UI.println("Game was not restarted.");
        }
    }

    // ---------- Initialise -------------------------

    /** SETS UP A NEW GAME */
    public void initialiseGame(){
        UI.clearText();
        allPods = new ArrayList<Pod> ();
        Scanner data = null;
        gameOver = false;
        try{
            //create pods from game-data file: 
            data = new Scanner(new File("game-data.txt"));
            //ignore comment lines, (starting with '# ')
            while (data.hasNext("#")){data.nextLine();}
            //read number of pods
            int numPods = data.nextInt(); data.nextLine();
            //read  name, has-recovery-kit,  has-data-cache
            for (int i=0; i<numPods; i++){
                // ASSUMES a Pod constructor!!!
                String podName = data.nextLine().trim();
                boolean hasRecoveryKit = data.nextBoolean();
                boolean hasDataCache = data.nextBoolean();
                Pod pod = new Pod(podName, hasRecoveryKit, hasDataCache);                //**MAY NEED TO CHANGE***
                allPods.add(pod);
                data.nextLine();
            }
            dockingPod = allPods.get(0);
            // connect them in circle, to ensure that there is a path
            for (int i=0; i<numPods; i++){
                Pod pod1 = allPods.get(i);
                Pod pod2 = allPods.get((i+1)%numPods);
                // ASSUMES one-way connections
                pod1.setPortal(0, pod2);               //**MAY NEED TO CHANGE***
            }
            // connect each pod to two random other pods.
            for (Pod pod : allPods){
                Pod podB = allPods.get((int)(Math.random()*allPods.size()));
                Pod podC = allPods.get((int)(Math.random()*allPods.size()));
                pod.setPortal(1, podB);               //**MAY NEED TO CHANGE***
                pod.setPortal(2, podC);               //**MAY NEED TO CHANGE***
            }
            UI.printf("Created %d pods\n", allPods.size());

            // Read trap name and items to disable trap, to make Traps and Item
            ArrayList<Trap> traps = new ArrayList<Trap>();
            ArrayList<Item> items = new ArrayList<Item>();
            while (data.hasNext()){
                //trap name, followed by number of items to disable trap,
                //followed by items (weight, name)
                String trapName = data.nextLine().trim();
                ArrayList<Item> itemsForTrap = new ArrayList<Item>();
                int numItems = data.nextInt(); data.nextLine();
                for (int i=0; i<numItems; i++){
                    double weight = data.nextDouble();
                    String itemName = data.nextLine().trim();
                    // ASSUMES Item contructor:
                    Item it = new Item(itemName, weight);             //**MAY NEED TO CHANGE***
                    itemsForTrap.add(it);
                }

                // ASSUMES Trap contructor:
                Trap trap = new Trap(trapName);
                traps.add(trap);
                items.addAll(itemsForTrap);
            }
            data.close();

            // ASSUMES Item contructor:
            items.add(new Item("Torch", 0.4));
            //put the traps in random rooms (other than the dockingPod)
            //but not in rooms that already have a trap
            UI.printf("Created %d traps and %d items\n", traps.size(), items.size());
            while (!traps.isEmpty()){
                Pod pod = allPods.get(1+(int) (Math.random()*numPods-1));
                // ASSUMES methods on Pod
                if (pod.getTrap() == null){
                    String trapType = traps.get(0).getType();
                    traps.remove(0);
                    pod.setTrap(trapType);
                }
            }
            //put the Items in random rooms.
            while (!items.isEmpty()){
                //Only allow items to be added to rooms that don't have traps.
                //This avoids the issue of there being a room with a trap that contains the item needed to disarm it.
                Pod pod = allPods.get((int) (Math.random()*numPods));
                while (pod.getTrap() != null) {
                    pod = allPods.get((int) (Math.random()*numPods));
                }
                
                pod.addItem(items.remove(0));
            }
            
            UI.printf("Added traps and items to Pods\n");
            currentPod = dockingPod;
            player = new Player();
            currentPod.printDescription();
        }
        catch(InputMismatchException e){UI.println("Wrong type of data at: " + data.nextLine());}
        catch(IOException e){UI.println("Failed to read data correctly:\n" + e);}
    }

    public static void main(String[] args){
        new AdventureGame();
    }

}
