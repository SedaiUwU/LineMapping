package linemapping.main;

import java.util.List;
import java.util.Map;

import linemapping.algo.AlgoFuntion;

public class Test {
    public static void main(String[] args) {
        try {
            // assumes old.txt and new.txt are in the project root
            String oldPath = "old.txt";
            String newPath = "new.txt";

            Map<Integer, List<Integer>> result = AlgoFuntion.mapFiles(oldPath, newPath);

            System.out.println("Line Mapping Result:");
            for (Map.Entry<Integer, List<Integer>> e : result.entrySet()) {
                System.out.println("Old line " + e.getKey() + " -> New lines " + e.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
