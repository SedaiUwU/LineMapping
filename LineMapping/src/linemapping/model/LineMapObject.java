package linemapping.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineMapObject {
    
    private List<String> rawLines;
    private Map<Integer, List<Integer>> lookupMap;

    public LineMapObject(String fileDir) {

        lookupMap = new HashMap<>();
        rawLines = LinemapConstructor.FiletoList(fileDir);

        Preprocessor pre = new Preprocessor(); 
        //we must run the pre processing here in line mapping, not in the file reader

        for (int i = 0; i < rawLines.size(); i++) {

            String lineContent = rawLines.get(i);

            String fixed = pre.fixLine(lineContent);  
            //this creates the new cleaned version that will be used for hashing

            int hash = fixed.hashCode(); 
            //this is our new hash after preprocessing is applied

            if (!lookupMap.containsKey(hash)) {
                lookupMap.put(hash, new ArrayList<>());
            }

            lookupMap.get(hash).add(i);
        }
    }
    
    public String GetLineString(int lineNumber) {
        if (lineNumber >= 0 && lineNumber < rawLines.size()) {
            return rawLines.get(lineNumber);
        }
        return null;
    }

    public List<Integer> GetIndices(int hash) {
        if (lookupMap.containsKey(hash)) {
            return lookupMap.get(hash);
        }
        return Collections.emptyList();
    }

    public boolean CheckNull() {
        return rawLines == null || rawLines.isEmpty();
    }

    public int GetSize() {
        return rawLines.size();
    }
}
