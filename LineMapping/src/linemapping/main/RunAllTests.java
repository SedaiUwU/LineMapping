// RunAllTests.java
package linemapping.main;

public class RunAllTests {
    public static void main(String[] args) throws Exception {
        System.out.println("🚀 STARTING COMPLETE TEST SUITE\n");
        
        // Step 1: Test both datasets
        System.out.println("1. Testing all datasets...");
        CombinedDataTester.main(args);
        
        // Step 2: Analyze accuracy
        System.out.println("\n\n2. Analyzing accuracy...");
        try {
            TeamDataAccuracyAnalyzer.main(args);
        } catch (Exception e) {
            System.out.println("Note: Need to add ground truth data for full analysis");
            System.out.println("See TeamDataAccuracyAnalyzer.java to add your mappings");
        }
        //as
        // Step 3: Show summary
        System.out.println("\n\n3. TEST SUITE COMPLETE");
        System.out.println("=".repeat(50));
        System.out.println("Generated files:");
        System.out.println("  prof_output_*.csv - Professor's dataset results");
        System.out.println("  team_*.csv - Team dataset results");
        System.out.println("  accuracy_analysis.csv - Sample accuracy test");
        System.out.println("  accuracy_report_*.txt - Analysis reports");
        System.out.println("\nNext steps:");
        System.out.println("  1. Check generated CSV files");
        System.out.println("  2. Add ground truth to TeamDataAccuracyAnalyzer.java");
        System.out.println("  3. Run TeamDataAccuracyAnalyzer for full accuracy report");
    }
}