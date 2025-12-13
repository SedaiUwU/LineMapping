package linemapping.main;

/**
 * RunAllTests ------------------------- This class serves as a central test
 * runner for the project. It executes all major testing steps in sequence:
 *
 * 1. Runs tests on both professor and team datasets 2. Analyzes and reports
 * accuracy of the results 3. Prints a summary of generated output files
 *
 * The class is intended to be run as a standalone program.
 */
public class RunAllTests {
	public static void main(String[] args) throws Exception {
		System.out.println("STARTING TEST \n");

		System.out.println("Testing datasets");
		CombinedDataTester.main(args);

	
		System.out.println("\n\n2. Checking accuracy");
		try {
			TeamDataAccuracyAnalyzer.main(args);
		} catch (Exception e) {

		}

		System.out.println("\n\n3. TEST COMPLETED");
		System.out.println("=".repeat(50));
		System.out.println("Files generated:");
		System.out.println("  prof_output_*.csv - Professor's dataset results");
		System.out.println("  team_*.csv - Team dataset results");
		System.out.println("  accuracy_analysis.csv - Sample accuracy test");
		System.out.println("  accuracy_report_*.txt - Accuracy report");

	}
}