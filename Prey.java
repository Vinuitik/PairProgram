import java.util.List;
import java.util.Random;
import javafx.scene.paint.Color; 
import java.util.Iterator;

/**
 * A simple model of a prey.
 * Preys age, move, breed, and die.
 * 
 * @author David J. Barnes, Michael Kölling and Jeffery Raphael
 * @version 2025.02.10
 */

public abstract class Prey extends Animal {
    
    protected double foodValue = 10;
    protected boolean initialisedFood = false;
    
    public void setFoodValue(double foodValue){
        if(initialisedFood){
            return ;
        }
        foodValue = foodValue;
        initialisedFood = true;
    }
    
    public double getFoodValue(){
        return foodValue;
    }

    /**
     * Create a new prey. A prey may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the prey will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Prey( Location location, Color col) {
        super(location, col);
    }
    
    public Prey(Prey mother, String gene) {
        super(mother, gene);
    }
    
    @Override
    protected Location findFood(){
        Field field = FieldManager.getInstance();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Plant) {
                Plant plant = (Plant) animal;
                if(plant.isAlive()) { 
                    plant.setDead();
                    foodLevel += plant.getFoodValue(); // Increase health by the food value of plant 
                    return where;
                }
            }
        }
        return null;
    }
}