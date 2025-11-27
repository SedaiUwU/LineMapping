
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LineMapping {

	
/*
 * Purpose:To read  file and store information into Array List where the index number is the line number
 * Pre:file that contains the code
 * Post:list which contains the lines of code
 */
public static List<String>FiletoList(String file){
	List<String>codelines=new ArrayList<>();
	
try(BufferedReader read=new BufferedReader(new FileReader(file))){
	String line;
	
	
while((line=read.readLine())!=null) {//reads the till eof
codelines.add(line);//adds each line to the list 

}
}catch(IOException e) {//catches any file reading errors or file not found 
	
}
return codelines;//returning list with all lines

	
}
	
	
public static void main(String[] args) {

	//this is a test just to see if the reading works we can delete this later
	//just putting sample java code
	List<String> testlines = FiletoList("C:\\Users\\sriram\\Desktop\\example.txt");
	//just test printing the list
	for (int i = 0; i < testlines.size(); i++) {
	 
	    System.out.println( (i + 1) + " " +testlines.get(i));
	}
	}


}
