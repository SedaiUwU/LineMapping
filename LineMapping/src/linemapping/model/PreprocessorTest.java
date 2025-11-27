package linemapping.model;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

public class PreprocessorTest {
    public static void main(String[] args) {
    	
    	// Create an object for the preprocessor so we can create methods for it
        Preprocessor prep = new Preprocessor();
        
        // Messy example line that has extra spaces, comment and caps
        
        String a = "   INT   X = 10 ;   // comment  ";
        
        // Sending the line to fixLine() and storing a cleaned result
        
        String fixedA = prep.fixLine(a);
        
        // print statement to ensure which test it is
        System.out.println("Example test:");       
        // prints out the cleaned statement
        System.out.println(fixedA);
        // prints an empty line to separate an output
        System.out.println();
        
        // creating a HashMap
        Map<Integer, String> sample = new HashMap<>();
        
        // Some test case entry samples
        sample.put(1, "   public   static   void main(String[] args) {   ");
        sample.put(2, "\tSystem.out.println(\"Hi\"); ");
        sample.put(3, "   // comment   ");
        sample.put(4, "        ");
        sample.put(5, null);
        
        // calling cleanMap() to clean every value in the map
        Map<Integer, String> afterMap = prep.cleanMap(sample);
        
        // print statement for map test
        System.out.println("Map test:");
        
        // loops through each entry of the cleaned map, prints lineNumber and fixed text
        for (Map.Entry<Integer, String> m : afterMap.entrySet()) {
            System.out.println(m.getKey() + ": " + m.getValue());
        }
        
        // print statement produces empty line to separate from the next section
        System.out.println();
        
        // creating a list of mixed lines making use of .aslist function
        var list = Arrays.asList(
                "   INT   A = 5;   ",
                "\treturn A;   ",
                " // comment "
        );
        
        // calling cleanList() to make the list a cleaned map
        Map<Integer, String> afterList = prep.cleanList(list);
        
        // print statement for line test section
        System.out.println("List test:");
        
        // loops through each entry in the cleaned map and prints out line number and fixed text
        for (Map.Entry<Integer, String> m : afterList.entrySet()) {
            System.out.println(m.getKey() + ": " + m.getValue());
        }
    }
}