package linemapping.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineMapObject {

	private List<String> rawLines;
	private List<String> fixedLines;
	private List<Integer> lineHashes;
	private Map<Integer, List<Integer>> lookupMap;

	public LineMapObject(String fileDir) {

		lookupMap = new HashMap<>();
		rawLines = LinemapConstructor.FiletoList(fileDir);
		fixedLines = new ArrayList<>();
		lineHashes = new ArrayList<>();

		Preprocessor pre = new Preprocessor();

		for (int i = 0; i < rawLines.size(); i++) {

			String lineContent = rawLines.get(i);

			String fixed = pre.fixLine(lineContent);
			// this creates the new cleaned version that will be used for hashing

			fixedLines.add(fixed);
			// save the line to reuse later

			int hash = fixed.hashCode();
			// this is our new hash after preprocessing is applied

			lineHashes.add(hash);
			// save the hash to reuse later

			if (!lookupMap.containsKey(hash)) {
				lookupMap.put(hash, new ArrayList<>());
			}

			// adds i to the list of numbers found
			lookupMap.get(hash).add(i);
		}
	}

	public String GetLineString(int lineNumber) {
        if (isValidIndex(lineNumber)) {
        	return rawLines.get(lineNumber);
        }
        return null;
    }

	private boolean isValidIndex(int i) {
        return i >= 0 && i < rawLines.size();
    }

	public List<Integer> GetIndices(int hash) {
		if (lookupMap.containsKey(hash)) {
			return lookupMap.get(hash);
		}
		return null;
	}

	public String GetFixedLine(int lineNumber) {
		if (isValidIndex(lineNumber)) {
			return fixedLines.get(lineNumber);
		}
		return null;
	}
	
	public int GetLineHash(int lineNumber) {
        if (isValidIndex(lineNumber)) {
        	return lineHashes.get(lineNumber);
        }
        return 0;
    }

	public boolean CheckNull() {
		return rawLines == null || rawLines.isEmpty();
	}

	public int GetSize() {
		return rawLines.size();
	}
}
