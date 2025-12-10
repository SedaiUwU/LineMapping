package linemapping.main;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SimHash {

	public static long compute(String line) {
		if (line == null || line.trim().isEmpty())
			return 0;

		String normalized = enhancedNormalize(line);

		int[] vector = new int[64];
		String[] tokens = normalized.split("\\s+");

		for (String token : tokens) {
			if (token.isEmpty() || token.length() < 2)
				continue;

			long hash = get64BitHash(token);
			for (int i = 0; i < 64; i++) {
				if ((hash & (1L << i)) != 0) {
					vector[i]++;
				} else {
					vector[i]--;
				}
			}
		}

		long fingerprint = 0;
		for (int i = 0; i < 64; i++) {
			if (vector[i] > 0) {
				fingerprint |= (1L << i);
			}
		}
		return fingerprint;
	}

	private static String enhancedNormalize(String line) {
		if (line == null)
			return "";

		String normalized = line;

		//Convert to lowercase
		normalized = normalized.toLowerCase();

		//Standardize variable names that commonly change
		normalized = normalized.replaceAll("arraytb|arraytype", "ARRAY_VAR");
		normalized = normalized.replaceAll("positiontb|positiontype", "POSITION_VAR");
		normalized = normalized.replaceAll("arrayelementbinding|resolvedtype", "TYPE_BINDING");

		//Remove "return" keyword specifically (it doesn't affect meaning for matching)
		normalized = normalized.replaceAll("\\breturn\\b", "");

		//Remove all punctuation
		normalized = normalized.replaceAll("[^a-zA-Z0-9\\s]", " ");

		//Remove other common Java keywords
		normalized = normalized.replaceAll("\\b(public|private|protected|static|final|void|if|else|for|while)\\b", "");

		//Collapse multiple spaces
		normalized = normalized.replaceAll("\\s+", " ");

		//Trim
		normalized = normalized.trim();

		return normalized;
	}

	private static long get64BitHash(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] digest = md.digest(str.getBytes());
			return new BigInteger(1, digest).longValue();
		} catch (NoSuchAlgorithmException e) {
			return str.hashCode();
		}
	}

	public static int hammingDistance(long hash1, long hash2) {
		long xor = hash1 ^ hash2;
		return Long.bitCount(xor);
	}

	//Compare two lines
	public static void debugCompare(String line1, String line2) {
		System.out.println("Line 1 original: " + line1);
		System.out.println("Line 2 original: " + line2);
		System.out.println("Line 1 normalized: " + enhancedNormalize(line1));
		System.out.println("Line 2 normalized: " + enhancedNormalize(line2));

		long hash1 = compute(line1);
		long hash2 = compute(line2);
		int dist = hammingDistance(hash1, hash2);

		System.out.println("Hamming distance: " + dist);
		System.out.println();
	}
}