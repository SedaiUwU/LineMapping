package linemapping.main;

import java.io.File;
/**
 * TestOutputLocation
 * 
 * Purpose:
 * This class is a small utility program used to determine where
 * output files are created when a relative file path is used.
 * 
 * Functionality:
 * - Prints the absolute path of a relative output file
 * - Displays the current working directory
 * - Displays the user's home directory
*/
public class TestOutputLocation {
    public static void main(String[] args) {
        System.out.println("=== Testing Output Location ===\n");
        
        String relPath = "test_output.csv";
        File relFile = new File(relPath);
        System.out.println("Relative path: " + relPath);
        System.out.println("Absolute path: " + relFile.getAbsolutePath());
        
        System.out.println("\nCurrent working directory:");
        System.out.println("  " + System.getProperty("user.dir"));
        
        System.out.println("\nUser home directory:");
        System.out.println("  " + System.getProperty("user.home"));
    }
}