
/**
 * Write a description of class FieldManager here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class FieldManager
{
    private static Field instance;
    
    private FieldManager(){
    }
    
    public static synchronized Field getInstance(int depth, int width){
        if(instance!=null){
            return instance;
        }
        instance = new Field(depth, width);
        return instance;
    }
    public static synchronized Field getInstance(){
        if(instance!=null){
            return instance;
        }
        return instance;
    }
}
