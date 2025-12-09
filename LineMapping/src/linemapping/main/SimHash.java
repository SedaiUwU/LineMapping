package linemapping.main;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SimHash {

    public static long compute(String line) {
        if (line == null || line.trim().isEmpty()) return 0;
        
        int[] vector = new int[64];
        String[] tokens = line.split("\\s+");

        for (String token : tokens) {
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
}