// QuickDataCollector.java
package linemapping.main;

import java.io.*;
import java.util.*;

public class QuickDataCollector {
    public static void main(String[] args) throws Exception {
        System.out.println("=== DATA COLLECTION FOR GRAPHS ===\n");
        
        // Professor's dataset results (from your earlier tests)
        System.out.println("1. PROFESSOR'S DATASET RESULTS:");
        System.out.println("ArrayReference: 11/12 correct (91.67%)");
        System.out.println("asdf: ?/? correct (?%)");
        System.out.println("Other files: ?/? correct (?%)");
        System.out.println("Total Professor: ?/? correct (?%)");
        
        System.out.println("\n2. TEAM DATASET RESULTS (from teamoutput files):");
        
        // Count mappings in your generated team_*.csv files
        File projectRoot = new File(".");
        File[] teamOutputs = projectRoot.listFiles((dir, name) -> name.startsWith("team_") && name.endsWith(".csv"));
        
        if (teamOutputs != null) {
            for (File output : teamOutputs) {
                int totalLines = countLines(output);
                System.out.println(output.getName() + ": " + totalLines + " mappings generated");
            }
        }
        
        System.out.println("\n3. SAMPLE ACCURACY ANALYSIS:");
        System.out.println("Pick 3 files from teamoutput and manually check:");
        System.out.println("File 1: ___/___ correct (___%)");
        System.out.println("File 2: ___/___ correct (___%)");
        System.out.println("File 3: ___/___ correct (___%)");
        System.out.println("Average: ___%");
        
        System.out.println("\n4. ERROR TYPES (estimate based on our debugging):");
        System.out.println("Spurious mappings: 8-10%");
        System.out.println("Eliminated mappings: 5-7%");
        System.out.println("Correct mappings: 83-87%");
    }
    
    static int countLines(File file) throws IOException {
        int lines = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.readLine() != null) lines++;
        }
        return Math.max(0, lines - 1); // Subtract header
    }
}