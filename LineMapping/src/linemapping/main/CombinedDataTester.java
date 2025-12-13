// CombinedDatasetTester.java
package linemapping.main;

import java.io.*;
import java.util.*;

public class CombinedDataTester {
    public static void main(String[] args) throws Exception {
        System.out.println("=== COMPREHENSIVE LHDIFF EVALUATION ===\n");
        
        // Test 1: Professor's Dataset
        System.out.println("1. TESTING PROFESSOR'S DATASET");
        System.out.println("=".repeat(50));
        testProfessorDataset();
        
        // Test 2: Team Dataset  
        System.out.println("\n\n2. TESTING TEAM DATASET (GitHub)");
        System.out.println("=".repeat(50));
        testTeamDataset();
        
        // Summary
        System.out.println("\n\n3. EVALUATION COMPLETE");
        System.out.println("=".repeat(50));
        System.out.println("✓ All tests completed successfully");
        System.out.println("✓ Output files saved in project root");
        System.out.println("✓ Ready for accuracy analysis");
    }
    
    static void testProfessorDataset() throws Exception {
        String datasetPath = "dataset/eclipseTest/";
        File datasetFolder = new File(datasetPath);
        
        System.out.println("Location: " + datasetFolder.getAbsolutePath());
        System.out.println("Files found: " + Arrays.toString(datasetFolder.list()));
        
        // Test ArrayReference (as we've been doing)
        String[] testPairs = {
            "ArrayReference_1.java", "ArrayReference_2.java",
            "asdf_1.java", "asdf_2.java"
        };
        
        for (int i = 0; i < testPairs.length; i += 2) {
            String oldFile = datasetPath + testPairs[i];
            String newFile = datasetPath + testPairs[i+1];
            String baseName = testPairs[i].replace("_1.java", "");
            String output = "prof_output_" + baseName + ".csv";
            
            System.out.println("\n  Testing: " + testPairs[i] + " → " + testPairs[i+1]);
            
            String[] args = {oldFile, newFile, output};
            LHDiff.main(args);
            System.out.println("  ✓ Output: " + output);
        }
    }
    
    static void testTeamDataset() throws Exception {
        String teamDataPath = "dataset/teamdata/";  // Changed to dataset/teamdata/
        File teamDataFolder = new File(teamDataPath);
        
        if (!teamDataFolder.exists()) {
            System.out.println("⚠️  Team dataset not found at: " + teamDataPath);
            System.out.println("Creating sample structure...");
            createSampleTeamData();
            return;
        }
        
        System.out.println("Location: " + teamDataFolder.getAbsolutePath());
        
        File[] categoryFolders = teamDataFolder.listFiles(File::isDirectory);
        if (categoryFolders == null || categoryFolders.length == 0) {
            System.out.println("No categories found. Please add folders with Java files.");
            return;
        }
        
        int totalTests = 0;
        List<String> generatedFiles = new ArrayList<>();
        
        for (File category : categoryFolders) {
            System.out.println("\n  Category: " + category.getName());
            
            File[] javaFiles = category.listFiles((dir, name) -> name.endsWith(".java"));
            if (javaFiles == null || javaFiles.length < 2) {
                System.out.println("    Skipping: Need at least 2 versions");
                continue;
            }
            
            Arrays.sort(javaFiles);
            
            // Test each consecutive pair
            for (int i = 0; i < javaFiles.length - 1; i++) {
                File oldFile = javaFiles[i];
                File newFile = javaFiles[i + 1];
                
                String output = String.format("team_%s_v%d_%d.csv", 
                    category.getName(), i+1, i+2);
                
                System.out.println("    " + oldFile.getName() + " → " + newFile.getName());
                
                try {
                    String[] args = {
                        oldFile.getAbsolutePath(),
                        newFile.getAbsolutePath(),
                        output
                    };
                    LHDiff.main(args);
                    
                    generatedFiles.add(output);
                    totalTests++;
                    System.out.println("    ✓ " + output);
                    
                } catch (Exception e) {
                    System.err.println("    ✗ Error: " + e.getMessage());
                }
            }
        }
        
        System.out.println("\n  Team Dataset Summary:");
        System.out.println("    Categories tested: " + categoryFolders.length);
        System.out.println("    Total file pairs: " + totalTests);
        System.out.println("    Output files: " + generatedFiles.size());
    }
    
    static void createSampleTeamData() throws IOException {
        // Create sample structure if teamdata doesn't exist
        File teamdata = new File("dataset/teamdata");
        teamdata.mkdirs();
        
        File bookingService = new File(teamdata, "BookingService");
        bookingService.mkdir();
        
        // Create sample Java files
        createSampleJavaFile(bookingService, "BookingService_1.java", 
            "public class BookingService {\n  public void bookTicket() {\n    System.out.println(\"Booking...\");\n  }\n}");
        
        createSampleJavaFile(bookingService, "BookingService_2.java",
            "public class BookingService {\n  public void bookTicket() {\n    System.out.println(\"Booking ticket...\");\n    validateUser();\n  }\n  private void validateUser() {\n    // New method\n  }\n}");
        
        System.out.println("✓ Created sample teamdata structure");
        System.out.println("✓ Add your actual GitHub files to dataset/teamdata/");
    }
    
    static void createSampleJavaFile(File folder, String name, String content) throws IOException {
        File file = new File(folder, name);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }
}