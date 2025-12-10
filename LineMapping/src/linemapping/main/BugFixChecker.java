package linemapping.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class BugFixChecker {


    public static boolean isBugFixCommit(String message, String[] keywords) {
        String lower = message.toLowerCase();

        //checks if it exact match
        for (String key : keywords) {
            if (lower.contains(key)) {
                return true;
            }
        }

      
        String[] words = lower.split("\\W+");  

        //checks just in case for typos with keywords
        for (String word : words) {
        	 if (word.isEmpty()) continue; 
            for (String key : keywords) {

                
                int distance = Levenshtein.distance(word, key);

                //decent match
                if (distance <= 1) {
                    return true;
                }
            }
        }

        return false;
    }

   
    public static void main(String[] args) {
        String repoPath = "C:\\Users\\sriram\\git\\LineMapping";

        //keywords for bug changes in commit messages
        String[] keywords = { "fix", "fixed", "bug", "issue", "patch", "error","resolve","prevent", "defect","resolved","repair","corrected","handled" };

        try {
            
            ProcessBuilder pb = new ProcessBuilder(
                    "git", "log", "--pretty=format:%H%n%an%n%B%n%n===END==="
            );

            pb.directory(new File(repoPath));

          
            Process process = pb.start();

            
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {

                StringBuilder commitBlock = new StringBuilder();
                String line;

                System.out.println("Scanning Git commits for bug fixes...\n");

                while ((line = reader.readLine()) != null) {

                    // Separator marking the end of one commit
                    if (line.equals("===END===")) {

                        // Split into commitHash, author, message
                        String[] parts = commitBlock.toString().split("\n", 3);

                        if (parts.length >= 3) {
                            String commitHash = parts[0].trim();
                            String author = parts[1].trim();
                            String fullMessage = parts[2];

                            //  keyword detection
                            if (isBugFixCommit(fullMessage, keywords)) {
                                System.out.println("Bug Fix Commit Found:");
                                System.out.println("Commit: " + commitHash);
                                System.out.println("Author: " + author);
                                System.out.println("Message: " + fullMessage.trim().split("\n")[0]);
                                System.out.println("-------------------------------------");
                            }
                        }

                        // Reset for next commit
                        commitBlock.setLength(0);

                    } else {
                        // Accumulate commit text
                        commitBlock.append(line).append("\n");
                    }
                }
            }

            // Read errors from git
            try (BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8))) {

                String errorLine;
                boolean hasError = false;

                while ((errorLine = errorReader.readLine()) != null) {
                    System.err.println("Git Error: " + errorLine);
                    hasError = true;
                }

                if (hasError) {
                    System.out.println("Git command encountered errors.");
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.out.println("Git command exited with code: " + exitCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error running git command or reading repository.");
        }
    }
}
