import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.scene.paint.Color; 

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing rabbits and foxes.
 * 
 * @author David J. Barnes, Michael Kölling and Jeffery Raphael
 * @version 2025.02.10
 */

public class Simulator {

    private static final double predatorCreationProbability = 0.02;
    private static final double preyCreationProbability = 0.08;    

    private List<Animal> animals;
    private List<Plant> plants;
    private int step;
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width) {
        
        animals = new ArrayList<>();
        plants = new ArrayList<>();
        FieldManager.getInstance(depth, width);

        reset();
    }
    
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * fox and rabbit.
     */
    public void simulateOneStep() {
        step++;
        List<Animal> newAnimals = new ArrayList<>();        

        for(Iterator<Animal> it = animals.iterator(); it.hasNext(); ) {
            Animal animal = it.next();
            animal.act(newAnimals);
            
            if(!animal.isAlive()) {
                it.remove();
                /*if(animal.getLocation()!=null){
                    FieldManager.getInstance().clear(animal.getLocation());
                }// DEBUG REMOVE DEBUG !! REMOVE DEBUG*/
            }
        }
               
        animals.addAll(newAnimals);
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset() {
        step = 0;
        animals.clear();
        populate();
    }
    
    /**
     * Randomly populate the field with different animals of both
     * the prey & predator categories.
     */
    private void populate() {
        
        Random rand = Randomizer.getRandom();
        Field field = FieldManager.getInstance();
        field.clear();
        
        int cntCreated = 0;
        
        // Two total predators exist
        for(int row = 0; row < field.getDepth(); row++) {
            
            for(int col = 0; col < field.getWidth(); col++) {
                
                if(rand.nextDouble() <= predatorCreationProbability) {
                    Location location = new Location(row, col);
                    Predator predator;
                    
                    if(cntCreated % 2 ==0){   // For spawning of 'Fox' or 'Wolf'
                        predator = new Fox(location); // Last 'false' means NOT sick
                    }
                    else{
                        predator = new Wolf(location);
                    }
                    animals.add(predator);
                    ++cntCreated;
                }
                
                // Otherwise, a Prey would be spawned 
                
                else if(rand.nextDouble() <= preyCreationProbability) {
                    
                    Location location = new Location(row, col);
                    Prey prey;
                    
                    if(cntCreated % 3 ==0){                                 // VV  Likewise, Prey will start healthy 
                        prey = new Rabbit(location);
                    }
                    else if(cntCreated % 3 ==1){
                        prey = new Bird(location);
                    }
                    else{
                        prey = new Mouse(location);
                    }
                    animals.add(prey);
                    
                    ++cntCreated;
                }
                // else - location is empty - so occupied by plant
                else if (field.getObjectAt(row, col) == null){
                    Location location = new Location(row, col);
                    Plant plant;
                    plant = new Plant(new Location(row, col)); // Adds a plant in the tile 
                    
                    plants.add(plant);
                }
            }
        }
    }
    
    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    public void delay(int millisec) {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }
    

    public Field getField() {
        return FieldManager.getInstance();
    }

    public int getStep() {
        return step;
    }
}