import javafx.scene.paint.Color;
import java.util.List;

/**
 * Mouse - a prey class animal 
 */
public class Mouse extends Prey
{
    
    public Mouse(Location location){
        super( location, Color.YELLOW);
        setFoodValue(3);//has to be done before setting properties
        setProperties(12, 30, 0.4, 3, 0.499, 0.25);
    }
    
    public Mouse(Mouse mother, String gene) {
        super(mother, gene);
    }
    
    @Override
    protected void giveBirth(List<Animal> newMice) {
        // New birds are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = FieldManager.getInstance();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        
        List<Animal> potentialFathers = field.getLivingNeighbours(getLocation());
        
        Mouse father = null;
        
        boolean notFoundFather = true;
        
        for(int i = 0;i<potentialFathers.size()&&notFoundFather;++i){
            if(potentialFathers.get(i) instanceof Mouse && potentialFathers.get(i).getGender() == Gender.MALE ){
                notFoundFather = false;
                father=(Mouse)potentialFathers.get(i);
            }
        }
        
        if(notFoundFather){
            return ;
        }
        
        int births = breed();// gives amount of hypothetical births 
        
        for(int b = 0; b < births && free.size() > 0 && canBreed(); b++) {
            
            Mouse child = (Mouse) Breeder.breed(this, father, this.getClass(), father.getClass());
            
            if(child!=null){
                //Location loc = free.remove(0);
                //Rabbit young = new Rabbit(loc);
                newMice.add(child);
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
            if(animal instanceof Mouse){
                ((Mouse)animal).simulateDisease();
            }
        }
    }
}
