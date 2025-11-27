package linemapping.algo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import linemapping.model.LinemapConstructor;
import linemapping.model.Preprocessor;

/**
 * Core line-mapping algorithm.
 *
 * This class:
 *  1) reads two files (old and new),
 *  2) preprocesses the lines using Preprocessor,
 *  3) builds a mapping: old line number -> list of new line numbers
 *
 * Current version: exact string equality after preprocessing.
 * Later you can extend this with similarity, splits, merges, etc.
 */
public class AlgoFuntion {

    // Preprocessor to clean lines (trim, lowercase, remove comments, etc.)
    private final Preprocessor preprocessor = new Preprocessor();

    // Cleaned lines for old and new files
    // Key   = line number (1-based)
    // Value = cleaned line text
    private Map<Integer, String> oldLinesMap;
    private Map<Integer, String> newLinesMap;

    // Mapping: old line number -> list of new line numbers
    // Using a list so we can later support splits/merges (1 -> N)
    private final Map<Integer, List<Integer>> mapping = new HashMap<>();

    /**
     * Constructor using file paths.
     *
     * @param oldFilePath path to the old version of the file
     * @param newFilePath path to the new version of the file
     */
    public AlgoFuntion(String oldFilePath, String newFilePath) {
        loadFiles(oldFilePath, newFilePath);
    }

    /**
     * Static convenience method: do everything in one call.
     */
    public static Map<Integer, List<Integer>> mapFiles(String oldFilePath, String newFilePath) {
        AlgoFuntion algo = new AlgoFuntion(oldFilePath, newFilePath);
        algo.computeExactMatches();
        return algo.getMapping();
    }

    /**
     * Reads the files, converts them to lists of raw lines,
     * and then cleans them using the Preprocessor.
     */
    private void loadFiles(String oldFilePath, String newFilePath) {
        List<String> oldRawLines = LinemapConstructor.FiletoList(oldFilePath);
        List<String> newRawLines = LinemapConstructor.FiletoList(newFilePath);

        // Use Preprocessor to normalize and clean each list
        this.oldLinesMap = preprocessor.cleanList(
                (oldRawLines != null) ? oldRawLines : Collections.emptyList());

        this.newLinesMap = preprocessor.cleanList(
                (newRawLines != null) ? newRawLines : Collections.emptyList());
    }

    /**
     * First version of the mapping algorithm:
     * For each old line, find a new line with the exact same cleaned text.
     * Ignores empty lines ("") after preprocessing.
     *
     * Result is stored in 'mapping'.
     */
    public void computeExactMatches() {
        mapping.clear();

        if (oldLinesMap == null || newLinesMap == null
                || oldLinesMap.isEmpty() || newLinesMap.isEmpty()) {
            return; // nothing to map
        }

        // To avoid mapping the same new line to multiple old lines (for now)
        Set<Integer> usedNewLines = new HashSet<>();

        // For each old line (lineNum -> cleanedText)
        for (Map.Entry<Integer, String> oldEntry : oldLinesMap.entrySet()) {
            int oldLineNum = oldEntry.getKey();
            String oldText = oldEntry.getValue();

            // Skip completely empty lines after preprocessing
            if (oldText == null || oldText.isEmpty()) {
                continue;
            }

            List<Integer> matchedNewLines = new ArrayList<>();

            // Compare with each new line
            for (Map.Entry<Integer, String> newEntry : newLinesMap.entrySet()) {
                int newLineNum = newEntry.getKey();
                String newText = newEntry.getValue();

                // Skip if this new line is already matched or empty
                if (usedNewLines.contains(newLineNum)) {
                    continue;
                }
                if (newText == null || newText.isEmpty()) {
                    continue;
                }

                // Exact match on cleaned text
                if (oldText.equals(newText)) {
                    matchedNewLines.add(newLineNum);
                    usedNewLines.add(newLineNum);
                    break; // for now, we only take one best match per old line
                }
            }

            if (!matchedNewLines.isEmpty()) {
                mapping.put(oldLineNum, matchedNewLines);
            }
        }
    }

    /**
     * Returns the full mapping: old line number -> list of new line numbers.
     */
    public Map<Integer, List<Integer>> getMapping() {
        return mapping;
    }

    /**
     * Helper to get mapped new lines for a given old line.
     *
     * @param oldLineNumber line number in the old file (1-based)
     * @return list of mapped new line numbers (may be empty)
     */
    public List<Integer> getMappedNewLines(int oldLineNumber) {
        return mapping.getOrDefault(oldLineNumber, Collections.emptyList());
    }
}
