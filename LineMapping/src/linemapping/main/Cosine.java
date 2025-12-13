package linemapping.main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import linemapping.model.LineMapObject;

/**
 * Cosine
 * 
 * Purpose: This class provides utilities for computing cosine similarity
 * between two context vectors represented as token-frequency maps. Cosine
 * similarity measures how similar two vectors are based on their direction
 * rather than magnitude.
 * 
 * Functionality: - Computes cosine similarity between two token-frequency maps
 * - Builds a contextual vector for a given line using surrounding lines
 * 
 * 
 * Use Case: Used as part of a hybrid line-matching algorithm to capture
 * contextual similarity between lines of code.
 */
public class Cosine {

	/*
	 * Purpose: Computes the cosine similarity between two context vectors by
	 * treating the maps as numerical vectors Source: Apache Commons Text logic
	 */
	public static double similarity(Map<String, Integer> v1, Map<String, Integer> v2) {
		if (v1 == null || v2 == null)
			return 0.0;

		Set<String> both = new HashSet<>(v1.keySet());
		both.addAll(v2.keySet());

		double dotProduct = 0.0;
		double normA = 0.0;
		double normB = 0.0;

		for (String key : both) {
			double val1 = v1.getOrDefault(key, 0);
			double val2 = v2.getOrDefault(key, 0);

			dotProduct += val1 * val2;
			normA += Math.pow(val1, 2);
			normB += Math.pow(val2, 2);
		}

		if (normA == 0 || normB == 0)
			return 0.0;
		return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
	}

	/*
	 * Purpose: Builds a context vector for the line index given by counting
	 * frequency of tokens and surrounding lines
	 */
	public static Map<String, Integer> getContextVector(int index, LineMapObject file) {
		Map<String, Integer> vector = new HashMap<>();

		for (int i = index - 4; i <= index + 4; i++) {
			if (i == index)
				continue;

			String line = file.GetFixedLine(i);
			if (line != null) {
				for (String token : line.split("\\W+")) {
					if (!token.isEmpty())
						vector.put(token, vector.getOrDefault(token, 0) + 1);
				}
			}
		}
		return vector;
	}
}