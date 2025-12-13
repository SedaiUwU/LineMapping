package linemapping.main;

import java.io.*;
import java.util.*;

/**
 * TeamDataAccuracyAnalyzer ------------------------- This class evaluates the
 * accuracy of LHDiff results on the team's dataset.
 */
public class TeamDataAccuracyAnalyzer {

	private static final Map<String, Map<Integer, Integer>> GROUND_TRUTH = new HashMap<>();

	static {

		Map<Integer, Integer> sampleTruth = new HashMap<>();
		sampleTruth.put(1, 1);
		sampleTruth.put(5, 6);
		sampleTruth.put(10, -1);
		sampleTruth.put(15, 18);

		GROUND_TRUTH.put("BookingService/BookingService", sampleTruth);
	}

	public static void main(String[] args) throws Exception {
		System.out.println("--- TEAM DATA SET ACCURACY TEST ---\n");

		String testCategory = "BookingService";
		String testFile1 = "dataset/teamdata/" + testCategory + "/BookingService_1.java";
		String testFile2 = "dataset/teamdata/" + testCategory + "/BookingService_2.java";

		if (!new File(testFile1).exists() || !new File(testFile2).exists()) {
			System.out.println("Test files not found. Run CombinedDatasetTester class first.");
			return;
		}

		String outputFile = "accuracy_analysis.csv";
		String[] lhArgs = { testFile1, testFile2, outputFile };
		LHDiff.main(lhArgs);

		Map<Integer, Integer> toolResults = readOutputFile(outputFile);
		Map<Integer, Integer> groundTruth = GROUND_TRUTH.get(testCategory + "/BookingService");

		if (groundTruth == null) {
			System.out.println("No ground truth available for " + testCategory);

			return;
		}

		int correct = 0;
		int total = groundTruth.size();

		System.out.println("Analyzing " + testCategory + ":");
		System.out.println("-".repeat(40));

		for (Map.Entry<Integer, Integer> entry : groundTruth.entrySet()) {
			int oldLine = entry.getKey();
			int expected = entry.getValue();
			Integer actual = toolResults.get(oldLine);

			boolean isCorrect = false;
			if (expected == -1) {
				isCorrect = (actual == null);
			} else {
				isCorrect = (actual != null && actual == expected);
			}

			String status = isCorrect ? "✓" : "✗";
			if (isCorrect)
				correct++;

			System.out.printf("Line %3d → Expected: %3s, Got: %3s %s%n", oldLine, expected == -1 ? "DEL" : expected,
					actual == null ? "---" : actual, status);
		}

		double accuracy = (correct * 100.0) / total;
		System.out.println("-".repeat(40));
		System.out.printf("Accuracy: %d/%d (%.1f%%)%n", correct, total, accuracy);
		System.out.println("Grade: " + getGrade(accuracy));

		saveAnalysisReport(testCategory, correct, total, accuracy, outputFile);
	}

	/*
	 * Purpose:Reads the LHDiff csv output file and extracts line mappings
	 */
	static Map<Integer, Integer> readOutputFile(String csvFile) throws IOException {
		Map<Integer, Integer> mappings = new HashMap<>();

		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			String line;
			boolean firstLine = true;
			while ((line = br.readLine()) != null) {
				if (firstLine) {
					firstLine = false;
					continue;
				}
				if (line.trim().isEmpty())
					continue;

				String[] parts = line.split(",");
				if (parts.length >= 2) {
					try {
						int oldLine = Integer.parseInt(parts[0].trim());
						int newLine = Integer.parseInt(parts[1].trim());
						mappings.put(oldLine, newLine);
					} catch (NumberFormatException e) {
					}
				}
			}
		}
		return mappings;
	}

	static String getGrade(double accuracy) {
		if (accuracy >= 90)
			return "A (Excellent)";
		if (accuracy >= 80)
			return "B (Very Good)";
		if (accuracy >= 70)
			return "C (Good)";
		if (accuracy >= 60)
			return "D (Acceptable)";
		return "F (Needs Improvement)";
	}

	/*
	 * Purpose: saves the accuracy report to a text file
	 */

	static void saveAnalysisReport(String category, int correct, int total, double accuracy, String outputFile)
			throws IOException {
		String reportFile = "accuracy_report_" + category + ".txt";

		try (PrintWriter writer = new PrintWriter(new FileWriter(reportFile))) {
			writer.println("LHDIFF ACCURACY ANALYSIS");
			writer.println("=".repeat(50));
			writer.println("Category: " + category);
			writer.println("Date: " + new Date());
			writer.println();
			writer.println("RESULTS:");
			writer.println("  Correct mappings: " + correct + "/" + total);
			writer.println("  Accuracy: " + String.format("%.1f%%", accuracy));
			writer.println("  Grade: " + getGrade(accuracy));
			writer.println();
			writer.println("OUTPUT FILE: " + outputFile);

		}

		System.out.println("\n Report saved: " + reportFile);
	}
}