package linemapping.main;

import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class LHDiffTestRunner {

    public static void main(String[] args) throws Exception {
        System.out.println("Running LHDiff \n");

        // Test with ArrayReference
        String baseName = "ArrayReference";
        String datasetPath = "dataset/eclipseTest/";

        String oldFile = datasetPath + baseName + "_1.java";
        String newFile = datasetPath + baseName + "_2.java";
        String outputFile = "mapping_output.csv";
        
        runLHDiff(oldFile, newFile, outputFile);
        
        System.out.println("\n✓ LHDiff completed successfully.");
        System.out.println("Output saved to: " + outputFile);
    }

    static void runLHDiff(String oldFile, String newFile, String outputFile) throws Exception {
        System.out.println("Input files:");
        System.out.println("  Old file: " + oldFile);
        System.out.println("  New file: " + newFile);
        System.out.println("  Output: " + outputFile + "\n");

        String[] args = { oldFile, newFile, outputFile };
        LHDiff.main(args);
    }
}