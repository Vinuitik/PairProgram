import javafx.scene.paint.Color;
import java.util.List;

/**
 * Bird - a prey class animal 
 */
public class Bird extends Prey
{
    public Bird(Location location){
        super(location, Color.BLUE);
        setFoodValue(5);
        setProperties(12, 60, 0.2, 3, 0.2, 0.4);
    }
    
    public Bird(Bird mother, String gene) {
        super(mother, gene);
    }
    
    @Override
    protected void giveBirth(List<Animal> newBirds) {
        // New birds are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = FieldManager.getInstance();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        
        List<Animal> potentialFathers = field.getLivingNeighbours(getLocation());
        
        Bird father = null;
        
        boolean notFoundFather = true;
        
        for(int i = 0;i<potentialFathers.size()&&notFoundFather;++i){
            if(potentialFathers.get(i) instanceof Bird && potentialFathers.get(i).getGender() == Gender.MALE ){
                notFoundFather = false;
                father=(Bird)potentialFathers.get(i);
            }
        }
        
        if(notFoundFather){
            return ;
        }
        
        int births = breed();// gives amount of hypothetical births 
        
        for(int b = 0; b < births && free.size() > 0 && canBreed(); b++) {
            
            Bird child = (Bird) Breeder.breed(this, father, this.getClass(), father.getClass());
            
            if(child!=null){
                //Location loc = free.remove(0);
                //Rabbit young = new Rabbit(loc);
                newBirds.add(child);
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
            if(animal instanceof Bird){
                ((Bird)animal).simulateDisease();
            }
        }
    }
}
