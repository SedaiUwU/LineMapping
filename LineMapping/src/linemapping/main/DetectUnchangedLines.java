package linemapping.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import linemapping.model.LineMapObject;

public class DetectUnchangedLines {

	private LineMapObject leftFile;
	private LineMapObject rightFile;

	private int[][] unchangedLinesarray;/////
	private Map<Integer, Integer> unchangedLines;
	private List<Integer> changedLeft;
	private List<Integer> changedRight;

	public DetectUnchangedLines(LineMapObject left, LineMapObject right) {
		this.leftFile = left;
		this.rightFile = right;

		unchangedLinesarray = new int[][]; /////

		unchangedLines = new HashMap<>();
		changedLeft = new ArrayList<>();
		changedRight = new ArrayList<>();
	}

	// here we first detect the unchanged lines
	public void findUnchanged() {

		for (int i = 0; i < leftFile.GetSize(); i++) {

			String leftText = leftFile.GetLineString(i);
			if (leftText == null) {
				continue;
			}

			int hash = leftText.hashCode();
			List<Integer> matchList = rightFile.GetIndices(hash);
			
			
			//add error checking for situations when checking if a line exists before you use it as a variable
			
			
			if (matchList.isEmpty()) {
				changedLeft.add(i);
			} else {
				int finalConfidence = 0;
				int matched = 0;
				for (int j = 0; j < matchList.size(); j++) {
					int confidenceValue = 0;

					if (leftFile.GetLineString(i - 1)!=null||((leftFile.GetLineString(i - 1)).hashCode() == matchList.get(j-1).hashCode())) { //added error checking if you forget to see if the line exists 
						confidenceValue += 1;
					}
					if (leftFile.GetLineString(i + 1)!=null||((leftFile.GetLineString(i + 1)).hashCode() == matchList.get(j +1).hashCode())) { //added error checking if you forget to see if the line exists 
						confidenceValue += 1;
					}
					if (confidenceValue > finalConfidence) {
						finalConfidence = confidenceValue;
						matched = j;
					}
					
				}
				
				int rightIndex = matchList.get(matched);
				
				unchangedLines.put(i, rightIndex);
			}
		}

		for (int j = 0; j < rightFile.GetSize(); j++) {
			if (!unchangedLines.containsValue(j)) {
				changedRight.add(j);
			}
		}
	}

	// it return unchanged line pairs
	public Map<Integer, Integer> getUnchanged() {
		return unchangedLines;
	}

	// this stores the lines that changed
	public List<Integer> getChangedLeft() {
    	changedLeft = {[[1][0]],[[1][0]],[[1][0]],[[1][0]],[[1][0]],[[1][0]],}
    	return changedLeft;
    }

	public List<Integer> getChangedRight() {
		return changedRight;
	}
}