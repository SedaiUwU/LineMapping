package linemapping.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import linemapping.model.LineMapObject;
import linemapping.model.Preprocessor;

public class DetectUnchangedLines {

    private LineMapObject leftFile;
    private LineMapObject rightFile;

    private Map<Integer, Integer> unchangedLines;
    private List<Integer> changedLeft;
    private List<Integer> changedRight;

    public DetectUnchangedLines(LineMapObject left, LineMapObject right) {
        this.leftFile = left;
        this.rightFile = right;

        unchangedLines = new HashMap<>();
        changedLeft = new ArrayList<>();
        changedRight = new ArrayList<>();
    }
    
    // here we detect the unchanged lines
    public void findUnchanged() {

        Preprocessor pre = new Preprocessor();

        for (int i = 0; i < leftFile.GetSize(); i++) {

            String leftText = leftFile.GetLineString(i);
            if (leftText == null) {
                changedLeft.add(i);
                continue;
            }

            // using preprocessed hash
            int leftHash = pre.fixLine(leftText).hashCode();

            List<Integer> matchList = rightFile.GetIndices(leftHash);

            // no match
            if (matchList == null || matchList.isEmpty()) {
                changedLeft.add(i);
                continue;
            }

            int finalConfidence = -1;
            int matchedIndex = -1;

            // looping through candidate list
            for (int j = 0; j < matchList.size(); j++) {

                int rightIndex = matchList.get(j);

                int confidenceValue = 0;

                // -1 checking
                if (i - 1 >= 0 && rightIndex - 1 >= 0) {
                    String leftPrev = pre.fixLine(leftFile.GetLineString(i - 1));
                    String rightPrev = pre.fixLine(rightFile.GetLineString(rightIndex - 1));

                    if (leftPrev.equals(rightPrev)) {
                        confidenceValue++;
                    }
                }

                // +1 checking
                if (i + 1 < leftFile.GetSize() && rightIndex + 1 < rightFile.GetSize()) {
                    String leftNext = pre.fixLine(leftFile.GetLineString(i + 1));
                    String rightNext = pre.fixLine(rightFile.GetLineString(rightIndex + 1));

                    if (leftNext.equals(rightNext)) {
                        confidenceValue++;
                    }
                }

                // best match
                if (confidenceValue > finalConfidence) {
                    finalConfidence = confidenceValue;
                    matchedIndex = rightIndex;
                }
            }

            // If nothing meaningful, treat as changed
            if (matchedIndex == -1) {
                changedLeft.add(i);
            } else {
                unchangedLines.put(i, matchedIndex);
            }
        }

        // anything in right-side not used is changed
        for (int j = 0; j < rightFile.GetSize(); j++) {
            if (!unchangedLines.containsValue(j)) {
                changedRight.add(j);
            }
        }
    }

    public Map<Integer, Integer> getUnchanged() {
        return unchangedLines;
    }

    public List<Integer> getChangedLeft() {
        return changedLeft;
    }

    public List<Integer> getChangedRight() {
        return changedRight;
    }
    
    public Map<String,List<?>> ChangedCandidateList() {

        List<String> leftcandidates = new ArrayList<>();
        List<String> rightcandidates = new ArrayList<>();
        Set<Integer> allIndices = new TreeSet<>();
        HashMap<String, List<?>> map = new HashMap<>();
        
        allIndices.addAll(changedLeft);
        allIndices.addAll(changedRight);
        map.put("left", leftcandidates);
        map.put("right", rightcandidates);
     
        for (int index : allIndices) {

            String leftText = "";
            String rightText = "";

            if (changedLeft.contains(index)) {
                leftText = leftFile.GetLineString(index);
                if (leftText == null) leftText = "";
                leftcandidates.add("LN: "+(index+1)+" "+leftText);
            }

            if (changedRight.contains(index)) {
                rightText = rightFile.GetLineString(index);
                if (rightText == null) rightText = "";
                rightcandidates.add("LN: "+(index+1)+" "+rightText);
            }

    }
        
        for (String key : map.keySet()) {
            System.out.println(key + " -> " + map.get(key));
            System.out.println();
        }
        return map;
}
}