package linemapping.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineMappingEngine {

    private LineMapObject leftFile;
    private LineMapObject rightFile;

    private Map<Integer, Integer> unchangedLines;
    private List<Integer> changedLeft;
    private List<Integer> changedRight;

    public LineMappingEngine(LineMapObject left, LineMapObject right) {
        this.leftFile = left;
        this.rightFile = right;

        unchangedLines = new HashMap<>();
        changedLeft = new ArrayList<>();
        changedRight = new ArrayList<>();
    }

    //here we first detect the unchanged lines
    public void findUnchanged() {

        for (int i = 0; i < leftFile.GetSize(); i++) {

            String leftText = leftFile.GetLineString(i);
            if (leftText == null) {
                continue;
            }

            int hash = leftText.hashCode();
            List<Integer> matchList = rightFile.GetIndices(hash);

            if (matchList.isEmpty()) {
                changedLeft.add(i);
            } else {
                int rightIndex = matchList.get(0);
                unchangedLines.put(i, rightIndex);
            }
        }

        for (int j = 0; j < rightFile.GetSize(); j++) {
            if (!unchangedLines.containsValue(j)) {
                changedRight.add(j);
            }
        }
    }

    //it return unchanged line pairs
    public Map<Integer, Integer> getUnchanged() {
        return unchangedLines;
    }

    //this stores the lines that changed
    public List<Integer> getChangedLeft() {
        return changedLeft;
    }

    public List<Integer> getChangedRight() {
        return changedRight;
    }
}