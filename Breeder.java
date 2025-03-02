import java.util.List;
import java.util.Random;
import java.lang.reflect.InvocationTargetException;

/**
 * A class responsible for handling the breeding logic among the animals
 * It incooporates methods for checking partners and generating offspring 
 *
 */
public class Breeder 
{
    
    public static final Double mutationProbability = 0.2;
    private static final Random rand = Randomizer.getRandom();

    
    /**
     * Breeds two animals if they are of the same species and opposite genders 
     */
    public static synchronized Animal breed(Animal mother, Animal father, Class speciesM, Class speciesF){
        
        //System.out.println("Breeding initiated");
        
        if(!mother.isAlive()||!father.isAlive()){
            //System.out.println("Parent is unavailable");
            return null;
        }
        
        if(!speciesM.equals(speciesF) || mother.getGender().equals( father.getGender()) ){
            return null; // inter-species and same gender is not possible 
        }
        
        double breedingProbA = mother.getBreedingProbability();
        double breedingProbB = father.getBreedingProbability();
        
        if(rand.nextDouble()  <= breedingProbA * breedingProbB ){
            return null;
        }
        
        //System.out.println("Breeding started");
        
        String geneM = mother.getGene();
        String geneF = father.getGene();
        
        String newGene = crossover(geneM, geneF);
        
        //System.out.println("Breeding crossover");
        
        if(newGene == null){
            System.out.println("Gene inconsistency");
            return null;
        }
        
        try {
            //System.out.println("Breeding child 1");
            return (Animal) speciesM.getConstructor(speciesM, String.class).newInstance(mother, newGene);
        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    private static String crossover(String geneM, String geneF) {
        if (geneM.length() != geneF.length()) {
            return null;
        }
    
        // Perform crossover
        StringBuilder builder = new StringBuilder();
        int length = geneM.length();
        
        for (int i = 0; i < length / 2; ++i) {
            builder.append(geneM.charAt(i));
        }
        for (int i = length / 2; i < length; ++i) {
            builder.append(geneF.charAt(i)); // Corrected crossover
        }
    
        // Apply mutation
        return mutate(builder.toString());
    }
    
    /**
     * Mutates a gene by incrementing digits (0-9) in a circular manner.
     */
    private static String mutate(String gene) {
        StringBuilder mutatedGene = new StringBuilder(gene);
        Random rand = Randomizer.getRandom();
    
        for (int i = 0; i < gene.length(); ++i) {
            double random = rand.nextDouble();
            if (random <= mutationProbability) {
                // Get current digit
                char currentChar = gene.charAt(i);
                if (Character.isDigit(currentChar)) {
                    int currentDigit = Character.getNumericValue(currentChar);
        
                    // Determine whether to add or subtract (50% chance)
                    int change = rand.nextBoolean() ? 1 : -1;
        
                    // Apply change and ensure digit wraps around 0-9
                    int newDigit = (currentDigit + change + 10) % 10;
        
                    // Update the mutated gene
                    mutatedGene.setCharAt(i, (char) ('0' + newDigit));
                }
            }
        }

    
        return mutatedGene.toString();
    }
    
    /**
     * Checks if an animal has an opposite-gender mate nearby
     * If true, then it would continue
     */
    public static boolean hasOppositeGenderMate(Animal animal){
        Field field = FieldManager.getInstance();
        List<Location> adjacent = field.adjacentLocations(animal.getLocation());
        
        for (Location loc : adjacent){
            Object nearbyAnimal = field.getObjectAt(loc);
            if ((nearbyAnimal instanceof Animal) && nearbyAnimal.getClass() == animal.getClass()){
                Animal mate = (Animal) nearbyAnimal;
                
                if ((mate.isAlive()) && (mate.getGender() != animal.getGender())){
                    return true;
                }
            }
        }
        return false;
    }

}
