import javafx.scene.paint.Color;
import java.util.List;

/**
 * Wolf - a predator class animal 
 */
public class Wolf extends Predator
{
    public Wolf(Location location){
        super(location, Color.BLACK);
        setProperties(12, 60, 0.4, 3, 0.499, 1);
    }
    
    public Wolf(Wolf mother, String gene) {
        super(mother, gene);
    }
    
    @Override
    protected void giveBirth(List<Animal> newWolves) {
        Field field = FieldManager.getInstance();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        
        List<Animal> potentialFathers = field.getLivingNeighbours(getLocation());
        
        Wolf father = null;
        
        boolean notFoundFather = true;
        
        for(int i = 0;i<potentialFathers.size()&&notFoundFather;++i){
            if(potentialFathers.get(i) instanceof Wolf && potentialFathers.get(i).getGender() == Gender.MALE ){
                notFoundFather = false;
                father=(Wolf)potentialFathers.get(i);
            }
        }
        
        if(notFoundFather){
            return ;
        }
        
        int births = breed();// gives amount of hypothetical births 
        
        for(int b = 0; b < births && free.size() > 0 && canBreed(); b++) {
            
            Wolf child = (Wolf) Breeder.breed(this, father, this.getClass(), father.getClass());
            
            if(child!=null){
                //Location loc = free.remove(0);
                //Rabbit young = new Rabbit(loc);
                newWolves.add(child);
                ++childrenBorn;
            }
        }
    }
    @Override
    protected void spreadDisease() {
        // New rabbits are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = FieldManager.getInstance();
        List<Location> locations = field.adjacentLocations(getLocation());
        
        for(int i = 0;i<locations.size();++i) {
            Location loc = locations.get(i);
            Object animal = field.getObjectAt(loc);
            if(animal instanceof Wolf){
                ((Wolf)animal).simulateDisease();
            }
        }
    }
}
