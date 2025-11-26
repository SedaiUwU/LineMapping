import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Preprocessor {
	
    public Preprocessor() {
    }
    //the below part returns only one fixed line
    public String fixLine(String line) {
        if (line == null) {	//Here we check if the line is null and if it is then we just treat it as empty line
            return "";
        }
        //trim function removes spaces from start and end of the line
        String result = line.trim();
        result = result.replace("\t", " "); //tab space is replaced by single space
        result = result.toLowerCase(); //converts to lower case characters
        
        int posofcomment = result.indexOf("//"); //check for "//" at start for comment lines
        if (posofcomment != -1) {
            result = result.substring(0, posofcomment).trim(); // if there is a comment line remove the comment line from the result
        }
        result = result.replaceAll("\\s+", " "); //a lot of spaces including tab space with normal space is just replaced by one single space
        if (result.length() == 0) {
            return ""; //if after removing spaces and fixing the line, the line ends up empty, then we just return an empty string
        }
        return result;
    }
    
    //fix an remove every entry inside the map
    public Map<Integer, String> cleanMap(Map<Integer, String> inputMap) {
        Map<Integer, String> outputMap = new HashMap<>(); //a new map is created that will store the fixed lines
        	for (Map.Entry<Integer, String> item : inputMap.entrySet()) { //goes through each entry in the original non-fixed map

            int lineNum = item.getKey(); //gets the line number
            String text = item.getValue(); //gets the actual text

            String fixedText = fixLine(text); //fixes the raw lines by calling the fixline() method

            outputMap.put(lineNum, fixedText); //the fixed text is stored in the new map by using line number
        }
        return outputMap;
    }

    //fixes a list of lines an put them in a map
    public Map<Integer, String> cleanList(List<String> lines) {
        Map<Integer, String> fixedLines = new HashMap<>(); //creates a new map that will store the fixed lines
        
        	for (int i = 0; i < lines.size(); i++) { //loops through each line of the list
            String text = lines.get(i); //gets positions of lines
            String cleanedText = fixLine(text); //fixes that line

            fixedLines.put(i + 1, cleanedText); //stores the fixed values
        }
        return fixedLines;
    }

    //checks if the line is empty or if it only contains spaces
    public boolean isLineEmpty(String line) {
        if (line == null) { //checks if line is null/empty
            return true;
        }
        String trimmed = line.trim(); //trims the lines and checks if the the fixed line has any value remained in it
        return trimmed.length() == 0;
    }
}