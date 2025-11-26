package linemapping.algo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class LineMap {
	
	private HashMap<Integer, String> dataFile = new HashMap<Integer, String>();
	private List<String> lines = new ArrayList<>();
	
	
	
	
	
	
	
	
	String GetLine(int Key) {
		
		if (dataFile.containsKey(Key)) {
			return dataFile.get(Key);
		}
		return null;
	}
		
	void GetString() {
		
	}
	
	void GetIndex() {
		
	}
	
	Boolean CheckNull() {
		
		return null;
	}
	
	void Converter() {
		
	}
	
}



