package linemapping.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import linemapping.model.LineMapObject;

public class DetectUnchangedLines {

	private LineMapObject leftFile;
	private LineMapObject rightFile;

	private Map<Integer, Integer> unchangedLines;
	private List<Integer> changedLeft;
	private List<Integer> changedRight;
	private Set<Integer> matchedRightIndices;

	public DetectUnchangedLines(LineMapObject left, LineMapObject right) {
		this.leftFile = left;
		this.rightFile = right;

		unchangedLines = new HashMap<>();
		changedLeft = new ArrayList<>();
		changedRight = new ArrayList<>();
		matchedRightIndices = new HashSet<>();
	}

	// here we detect the unchanged lines
	public void findUnchanged() {

		for (int i = 0; i < leftFile.GetSize(); i++) {

			int leftHash = leftFile.GetLineHash(i);

			List<Integer> matchList = rightFile.GetIndices(leftHash);

			// no match
			if (matchList == null || matchList.isEmpty()) {
				changedLeft.add(i);
				continue;
			}
			// unique quick match
			if (matchList.size() == 1) {
				int uniqueMatch = matchList.get(0);
				if (!matchedRightIndices.contains(uniqueMatch)) {
					unchangedLines.put(i, uniqueMatch);
					matchedRightIndices.add(uniqueMatch);
				}
				continue;
			}

			int finalConfidence = -1;
			int matchedIndex = -1;

			// looping through candidate list
			for (int rightIndex : matchList) {

				if (matchedRightIndices.contains(rightIndex))
					continue;

				int confidenceValue = 0;

				// -1 checking
				if (i - 1 >= 0 && rightIndex - 1 >= 0) {
					String leftPrev = leftFile.GetFixedLine(i - 1);
					String rightPrev = rightFile.GetFixedLine(rightIndex - 1);

					if (leftPrev != null && leftPrev.equals(rightPrev)) {
						confidenceValue++;
					}
				}

				// +1 checking
				if (i + 1 < leftFile.GetSize() && rightIndex + 1 < rightFile.GetSize()) {
					String leftNext = leftFile.GetFixedLine(i + 1);
					String rightNext = rightFile.GetFixedLine(rightIndex + 1);

					if (leftNext != null && leftNext.equals(rightNext)) {
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
				matchedRightIndices.add(matchedIndex);
			}
		}

		// anything in right-side not used is changed
		for (int j = 0; j < rightFile.GetSize(); j++) {
			if (!matchedRightIndices.contains(j)) { 
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

	public Map<String, List<?>> ChangedCandidateList() {

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
				if (leftText == null)
					leftText = "";
				leftcandidates.add("LN: " + (index + 1) + " " + leftText);
			}

			if (changedRight.contains(index)) {
				rightText = rightFile.GetLineString(index);
				if (rightText == null)
					rightText = "";
				rightcandidates.add("LN: " + (index + 1) + " " + rightText);
			}

		}

		for (String key : map.keySet()) {
			System.out.println(key + " -> " + map.get(key));
			System.out.println();
		}
		return map;
	}
}