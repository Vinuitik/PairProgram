import javafx.scene.paint.Color;
import java.util.List;

/**
 * Rabbit - a prey class animal 
 */
public class Rabbit extends Prey
{
    public Rabbit(Location location){
        super(location, Color.BROWN);
        setFoodValue(9);
        setProperties(12, 60, 0.2, 3, 0.2, 0.4);
    }
    
    public Rabbit(Rabbit mother, String gene) {
        super(mother, gene);
    }
    
    @Override
    protected void giveBirth(List<Animal> newRabbits) {
        // New rabbits are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = FieldManager.getInstance();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        
        List<Animal> potentialFathers = field.getLivingNeighbours(getLocation());
        
        Rabbit father = null;
        
        boolean notFoundFather = true;
        
        for(int i = 0;i<potentialFathers.size()&&notFoundFather;++i){
            if(potentialFathers.get(i) instanceof Rabbit && potentialFathers.get(i).getGender() == Gender.MALE ){
                notFoundFather = false;
                father=(Rabbit)potentialFathers.get(i);
            }
        }
        
        //System.out.println("No Father");
        
        if(notFoundFather){
            return ;
        }
        
        int births = breed();// gives amount of hypothetical births 
        
        //System.out.println("Father Found");
        
        for(int b = 0; b < births && free.size() > 0 && canBreed(); b++) {
            
            Rabbit child = (Rabbit) Breeder.breed(this, father, this.getClass(), father.getClass());
            
            if(child!=null){
                //Location loc = free.remove(0);
                //Rabbit young = new Rabbit(loc);
                newRabbits.add(child);
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
            if(animal instanceof Rabbit){
                ((Rabbit)animal).simulateDisease();
            }
        }
    }
}
