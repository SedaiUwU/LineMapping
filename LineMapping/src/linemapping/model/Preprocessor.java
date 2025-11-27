package linemmapping.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Purpose: Normalize each line by trimming whitespace, replacing tabs with spaces,
 * converting to lowercase, removing inline comments, and squeezing extra spaces into one.
 */
public class Preprocessor {

    public Preprocessor() {
    }

    /**
     * Cleans a single line of text.
     *
     * @param line raw line from file
     * @return cleaned normalized line (or empty string if null/blank)
     */
    public String fixLine(String line) {
        if (line == null) {
            return "";
        }

        String result = line.trim();
        result = result.replace("\t", " ");
        result = result.toLowerCase();

        int posOfComment = result.indexOf("//");
        if (posOfComment != -1) {
            result = result.substring(0, posOfComment).trim();
        }

        result = result.replaceAll("\\s+", " ");

        return (result.length() == 0) ? "" : result;
    }

    /**
     * Cleans a list of lines and puts them into a map using 1-based line numbers.
     *
     * @param lines list of file lines in order
     * @return Map<lineNumber, cleanedText>
     */
    public Map<Integer, String> cleanList(List<String> lines) {
        Map<Integer, String> fixedLines = new HashMap<>();
        for (int i = 0; i < lines.size(); i++) {
            String text = lines.get(i);
            String cleaned = fixLine(text);
            fixedLines.put(i + 1, cleaned);
        }
        return fixedLines;
    }

    /**
     * Cleans map entries (lineNumber → text) using fixLine().
     *
     * @param inputMap unclean map
     * @return cleaned map
     */
    public Map<Integer, String> cleanMap(Map<Integer, String> inputMap) {
        Map<Integer, String> outputMap = new HashMap<>();
        for (Map.Entry<Integer, String> entry : inputMap.entrySet()) {
            int lineNum = entry.getKey();
            String rawText = entry.getValue();
            String fixed = fixLine(rawText);
            outputMap.put(lineNum, fixed);
        }
        return outputMap;
    }

    /**
     * Checks if a line is null or blank after trimming.
     *
     * @param line input text line
     * @return true if null/blank, otherwise false
     */
    public boolean isLineEmpty(String line) {
        if (line == null) {
            return true;
        }
        return line.trim().length() == 0;
    }
}
