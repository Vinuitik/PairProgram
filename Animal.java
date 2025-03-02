import java.util.List;
import javafx.scene.paint.Color; 
import java.util.Random;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes, Michael Kölling and Jeffery Raphael
 * @version 2025.02.10
 */

public abstract class Animal {
    
    private boolean alive;
    private Location location;
    private Color color = Color.BLACK;
    
    private Gender gender;
    
    protected int age;
    
    protected int BREEDING_AGE;
    protected int MAX_AGE;
    protected double BREEDING_PROBABILITY;
    protected int MAX_LITTER_SIZE;
    protected double DISEASE_PROBABILITY;
    protected double METABOLISM;
    
    protected final double INITIAL_FOOD_LEVEL = 9;
    
    protected boolean initialised = false;
    protected static final Random rand = Randomizer.getRandom();
    protected boolean ill = false;
    protected double foodLevel;
    
    protected int childrenBorn;
    
    protected int MAX_DISEASE_AGE = 1000;
    protected int disease_age;
    
    private final int minBreedAge = 12;
    private final int maxBreedAge = 90;
    private final int minLifeSpan = 10;
    private final int maxLifeSpan = 120;
    private final double minBreedProb = 0;
    private final double maxBreedProb = 0.5;
    private final int minLitterSize = 1;
    private final int maxLitterSize = 12;
    private final double minDiseaseProb = 0;
    private final double maxDiseaseProb = 0.5;
    private final double minMetabolism = 0.25;
    private final double maxMetabolism = 1;
    
    protected void setProperties(int breedingAge, int lifeSpan, double breedingProbability, int litterSize, double diseaseProbability, double metabolism){
        if(initialised){
            return ;
        }
        BREEDING_AGE =  Math.max( minBreedAge, Math.min( maxBreedAge ,  breedingAge) ); // adjusting for boundaries
        MAX_AGE =  Math.max( minLifeSpan, Math.min( maxLifeSpan ,  lifeSpan) );
        BREEDING_PROBABILITY = Math.max( minBreedProb, Math.min( maxBreedProb ,  breedingProbability));
        MAX_LITTER_SIZE =  Math.max( minLitterSize, Math.min( maxLitterSize ,  litterSize) ) ;
        DISEASE_PROBABILITY = Math.max( minDiseaseProb, Math.min( maxDiseaseProb ,  diseaseProbability) ) ;
        METABOLISM = Math.max( minMetabolism, Math.min( maxMetabolism ,  metabolism) ) ;
        
        
        foodLevel = INITIAL_FOOD_LEVEL;
        
        
        initialised = true; // protecting against reinitialising
        
    }
    
    public double getBreedingProbability(){
        return BREEDING_PROBABILITY;
    }
    
    
    public Gender getGender(){
        return gender;
    }
    
   
    public String getGene(){
        String breedingAgeStr = String.format("%02d", BREEDING_AGE); // 2 digits
        String lifeSpanStr = String.format("%03d", MAX_AGE); // 3 digits
        String breedingProbabilityStr = String.format("%02d", (int)(BREEDING_PROBABILITY * 100)); // 2 digits, scaled to 0-50
        String litterSizeStr = String.format("%02d", MAX_LITTER_SIZE); // 2 digits
        String diseaseProbabilityStr = String.format("%02d", (int)(DISEASE_PROBABILITY * 100)); // 2 digits, scaled to 0-50
        String metabolismStr = String.format("%03d", (int)((METABOLISM ) * 100)); // 3 digits, scaled to 25-100

        // Concatenate all parts to form the 14-digit gene string
        return breedingAgeStr + lifeSpanStr + breedingProbabilityStr + litterSizeStr + diseaseProbabilityStr + metabolismStr;
    }
    
    public Animal(Animal mother, String gene){
        
        //System.out.println("Breeding child 2");
        
        //System.out.println(gene);
        
        alive = true;
        Location location = FieldManager.getInstance().getFreeAdjacentLocation( mother.getLocation() );
        
        if(location == null){
            setDead();
            //System.out.println("Lack of SPACE");
            return ;
        }
        
        
        
        setLocation(location);
        setColor(mother.getColor());
        
        // 1. Breeding Age (12-90) - 2 digits
        int breedingAge = Integer.parseInt(gene.substring(0, 2));
        // 2. Life Span (10-120) - 3 digits
        int lifeSpan = Integer.parseInt(gene.substring(2, 5));
        // 3. Breeding Probability (0.0-0.50) - 2 digits
        int breedingProbValue = Integer.parseInt(gene.substring(5, 7)); // 2 digits
        double breedingProbability = breedingProbValue / 100.0; // Scaled to 0.00-0.50
        // 4. Litter Size (1-12) - 2 digits
        int litterSize = Integer.parseInt(gene.substring(7, 9));
        // 5. Disease Probability (0.0-0.50) - 2 digits
        int diseaseProbValue = Integer.parseInt(gene.substring(9, 11));
        double diseaseProbability = diseaseProbValue / 100.0; // Scaled to 0.00-0.50
        // 6. Metabolism (0.25-1) - 3 digits
        int metabolismValue = Integer.parseInt(gene.substring(11, 14));
        double metabolism = (metabolismValue) / 100.0; // Scaled to 0.25-1
        
        
        setProperties(breedingAge,lifeSpan,breedingProbability,litterSize,diseaseProbability,metabolism);
        
        setupProperties();
        
    }
    
    /**
     * Create a new animal at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    
    private void setupProperties(){
        age = 0;
        disease_age = 0;
        childrenBorn = 0;
        
        int randomInt = rand.nextInt(2);
        
        if(randomInt == 0){
            gender = Gender.MALE;
        }
        else{
            gender = Gender.FEMALE;
        }
    }
    
    public Animal(Location location, Color col) {
        alive = true;
        setLocation(location);
        setColor(col);
        
        setupProperties();
    }

    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
    protected boolean isAlive() {
        return alive;
    }

    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
     */
    public void setDead() {
        Field field = FieldManager.getInstance();
        alive = false;
        if(location != null) {
            field.clear(location);
            new Plant(location); 
            location = null;
            field = null;//will not delete object, will remove 1 reference from it
        }
    }

    /**
     * Return the animal's location.
     * @return The animal's location.
     */
    protected Location getLocation() {
        return location;
    }
    
    /**
     * Place the animal at the new location in the given field.
     * @param newLocation The animal's new location.
     */
    protected void setLocation(Location newLocation) {
        Field field = FieldManager.getInstance();
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
    
    /**
     * Changes the color of the animal
     */
    public void setColor(Color col) {
        color = col;
    }

    /**
     * Returns the animal's color
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * Check whether or not this animal is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newAnimals A list to return newly born animals.
     */
    protected abstract void giveBirth(List<Animal> newAnimals);
        
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    protected int breed() {
        
        if(childrenBorn>=MAX_LITTER_SIZE){
            return 0;
        }
        int births = 0;
        if(canBreed()) {
            births = rand.nextInt(MAX_LITTER_SIZE - childrenBorn) + 1;
        }
        return births;
    }

    /**
     * A fox can breed if it has reached the breeding age.
     */
    protected boolean canBreed() {
        return age >= BREEDING_AGE && childrenBorn <= MAX_LITTER_SIZE; // Checks if there is an opposite gender mate nearby
    }
    
    /**
     * Increase the age. This could result in the fox's death.
     */
    protected void incrementAge() {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
        
        
        if(!ill){
            return ;
        }
        
        
        ++disease_age;
        if(disease_age > MAX_DISEASE_AGE){
            setDead();
        }
    }
    
    /**
     * Make this fox more hungry. This could result in the fox's death.
     */
    protected void incrementHunger() {
        foodLevel -= METABOLISM;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * This is what the animal does most of the time: it hunts for
     * food. In the process, it might breed, die of hunger,
     * or die of old age.
     * PREDATORS : Hunt for Prey
     * PREY : Hunt for plants 
     * @param field The field currently occupied.
     * @param newAnimals A list to return newly born animals.
     */
    public void act(List<Animal> newAnimals) {
        
        //System.out.println(this.getClass().toString()+": "+ alive + " "+ age+" "+" "+gender + " " + METABOLISM );
        
        if(isAlive()){
            incrementAge();
            incrementHunger();
            simulateDisease();
        }
        if(isAlive()) {
            
            
            
            if(gender == Gender.FEMALE){
                giveBirth(newAnimals);
            }
            
            if(ill){
                spreadDisease();
            }
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = FieldManager.getInstance().getFreeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }
    
    public void simulateDisease(){
        if(ill){
            return ; //already ill
        }
        ill = rand.nextDouble() <= DISEASE_PROBABILITY;
        disease_age = 0;
    }
    
    protected abstract void spreadDisease();
    
    
    protected abstract Location findFood();
}