package linemapping.main;

/**
 * Levenshtein
 * 
 * Purpose: This class implements the Levenshtein edit distance algorithm to
 * measure the similarity between two strings. It supports both raw edit
 * distance computation and normalized similarity scoring.
 * 
 * Functionality: - Computes edit distance using a dynamic programming approach
 * - Converts edit distance into a similarity score between 0 and 1 - Provides a
 * utility method for minimum value selection
 * 
 * Use Case: Used as the content-based similarity component in a hybrid
 * line-matching algorithm.
 */
public class Levenshtein {

	// Source: Standard DP Algorithm (GeeksforGeeks / Baeldung)

	public static int editDistance(String str1, String str2) {
		int[][] dp = new int[str1.length() + 1][str2.length() + 1];

		for (int i = 0; i <= str1.length(); i++) {
			for (int j = 0; j <= str2.length(); j++) {
				if (i == 0) {
					dp[i][j] = j;
				} else if (j == 0) {
					dp[i][j] = i;
				} else {
					int cost = (str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1;
					dp[i][j] = min(dp[i - 1][j - 1] + cost, dp[i - 1][j] + 1, dp[i][j - 1] + 1);
				}
			}
		}
		return dp[str1.length()][str2.length()];
	}

	// NEW: Return similarity (0-1) instead of distance
	public static double distance(String str1, String str2) {
		int editDist = editDistance(str1, str2);
		int maxLength = Math.max(str1.length(), str2.length());

		if (maxLength == 0)
			return 1.0; // Both empty strings are identical

		// Convert edit distance to similarity (0-1, where 1 is identical)
		double similarity = 1.0 - ((double) editDist / maxLength);

		return similarity;
	}

	private static int min(int... numbers) {
		int min = Integer.MAX_VALUE;
		for (int num : numbers) {
			if (num < min)
				min = num;
		}
		return min;
	}
}