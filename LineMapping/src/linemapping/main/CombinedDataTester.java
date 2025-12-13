
package linemapping.main;

import java.io.*;
import java.util.*;
/**
 * CombinedDataTester
 * -------------------------
 * This class performs comprehensive testing of Java code datasets using LHDiff.
 */
public class CombinedDataTester {
	public static void main(String[] args) throws Exception {
		System.out.println("---  LHDIFF TESTING ---\n");

		System.out.println("Testing prof's dataset");
		System.out.println("=".repeat(50));
		testProfessorDataset();

		System.out.println("\n\n2. Testing Team dataset from github");
		System.out.println("=".repeat(50));
		testTeamDataset();

		// Summary
		System.out.println("\n\n3. TESTING COMPLETE");
		System.out.println("=".repeat(50));

		System.out.println("Output files saved in project root");

	}

	/*
	 * Purpose: Tests the professor's dataset located in `dataset/eclipseTest/`.
	 * Compares predefined Java file pairs using LHDiff and outputs CSV results.
	 */
	static void testProfessorDataset() throws Exception {
		String datasetPath = "dataset/eclipseTest/";
		File datasetFolder = new File(datasetPath);

		System.out.println("Location: " + datasetFolder.getAbsolutePath());
		System.out.println("Files found: " + Arrays.toString(datasetFolder.list()));

	
		String[] testPairs = { "ArrayReference_1.java", "ArrayReference_2.java", "asdf_1.java", "asdf_2.java" };

		for (int i = 0; i < testPairs.length; i += 2) {
			String oldFile = datasetPath + testPairs[i];
			String newFile = datasetPath + testPairs[i + 1];
			String baseName = testPairs[i].replace("_1.java", "");
			String output = "prof_output_" + baseName + ".csv";

			System.out.println("\n  Testing: " + testPairs[i] + " → " + testPairs[i + 1]);

			String[] args = { oldFile, newFile, output };
			LHDiff.main(args);
			System.out.println("  Output: " + output);
		}
	}

	/**
	 * Purpose: Tests the team's dataset located in `dataset/teamdata/`. Iterates
	 * through category folders and tests all consecutive Java file pairs. Outputs
	 * CSV files for each comparison.
	 */
	static void testTeamDataset() throws Exception {
		String teamDataPath = "dataset/teamdata/"; 
		File teamDataFolder = new File(teamDataPath);

		if (!teamDataFolder.exists()) {
			System.out.println("Team dataset not found at: " + teamDataPath);
			System.out.println("Creating sample structure");
			createSampleTeamData();
			return;
		}

		System.out.println("Location: " + teamDataFolder.getAbsolutePath());

		File[] categoryFolders = teamDataFolder.listFiles(File::isDirectory);
		if (categoryFolders == null || categoryFolders.length == 0) {
			System.out.println("No categories found. add folders with Java files.");
			return;
		}

		int totalTests = 0;
		List<String> generatedFiles = new ArrayList<>();

		for (File category : categoryFolders) {
			System.out.println("\n  Category: " + category.getName());

			File[] javaFiles = category.listFiles((dir, name) -> name.endsWith(".java"));
			if (javaFiles == null || javaFiles.length < 2) {
				System.out.println("    Skipped: Need at least 2 versions");
				continue;
			}

			Arrays.sort(javaFiles);

			for (int i = 0; i < javaFiles.length - 1; i++) {
				File oldFile = javaFiles[i];
				File newFile = javaFiles[i + 1];

				String output = String.format("team_%s_v%d_%d.csv", category.getName(), i + 1, i + 2);

				System.out.println("    " + oldFile.getName() + " → " + newFile.getName());

				try {
					String[] args = { oldFile.getAbsolutePath(), newFile.getAbsolutePath(), output };
					LHDiff.main(args);

					generatedFiles.add(output);
					totalTests++;
					System.out.println("    Correct " + output);

				} catch (Exception e) {
					System.err.println("   Error: " + e.getMessage());
				}
			}
		}

		System.out.println("\n  Team Dataset Summary:");
		System.out.println("    Categories tested: " + categoryFolders.length);
		System.out.println("    Total file pairs: " + totalTests);
		System.out.println("    Output files: " + generatedFiles.size());
	}

	/*
	 * Purpose:Creates a sample team dataset folder structure with a sample Java
	 * class
	 */
	static void createSampleTeamData() throws IOException {
	
		File teamdata = new File("dataset/teamdata");
		teamdata.mkdirs();

		File bookingService = new File(teamdata, "BookingService");
		bookingService.mkdir();

		// Create sample Java files
		createSampleJavaFile(bookingService, "BookingService_1.java",
				"public class BookingService {\n  public void bookTicket() {\n    System.out.println(\"Booking...\");\n  }\n}");

		createSampleJavaFile(bookingService, "BookingService_2.java",
				"public class BookingService {\n  public void bookTicket() {\n    System.out.println(\"Booking ticket...\");\n    validateUser();\n  }\n  private void validateUser() {\n    // New method\n  }\n}");

	
	}

	static void createSampleJavaFile(File folder, String name, String content) throws IOException {
		File file = new File(folder, name);
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(content);
		}
	}
}