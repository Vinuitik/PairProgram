import java.util.List;
import java.util.Random;
import javafx.scene.paint.Color;

public class Plant{
    public static final int foodValue = 10;
    
    private boolean alive;
    
    private Location location;
    private Color color = Color.LIGHTGREEN;
    
    
    public Plant(Location location){
        
        
        this.alive = true;
        this.location = location;
        this.color = color;
        FieldManager.getInstance().placePlant(this, location);
    }
    
    /**
     * Indicate that a plant is no longer alive
     * A clear tile is different form a (free) plant tile - as plants can be eaten 
     */
    protected void setDead(){
        alive = false;
        Field field = FieldManager.getInstance();
        if (location != null){
            field.clear(location);
            location = null;
            field = null; 
        }
    }
    
    public int getFoodValue(){
        return foodValue;
    }
    
    public Location getLocation(){
        return location;
    }
    
    public boolean isAlive(){
        return alive;
    }
    
    public Color getColor(){
        return color;
    }
    
}