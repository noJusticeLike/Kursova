package util;

import model.ProblemInstance;
import java.io.*;
import java.util.*;

public class InputOutput {
    public static void saveToFile(ProblemInstance instance, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(instance.n + " " + instance.m + "\n");
            for (int p : instance.processingTimes)
                writer.write(p + " ");
            writer.write("\n");
            for (int i = 0; i < instance.n; i++) {
                for (int j = 0; j < instance.n; j++) {
                    writer.write((instance.incompatibilities[i][j] ? 1 : 0) + " ");
                }
                writer.write("\n");
            }
        }
    }

    public static ProblemInstance readFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String[] parts = reader.readLine().split(" ");
            int n = Integer.parseInt(parts[0]);
            int m = Integer.parseInt(parts[1]);
            int[] processingTimes = Arrays.stream(reader.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            boolean[][] incompatibilities = new boolean[n][n];
            for (int i = 0; i < n; i++) {
                String[] line = reader.readLine().split(" ");
                for (int j = 0; j < n; j++)
                    incompatibilities[i][j] = line[j].equals("1");
            }
            return new ProblemInstance(n, m, processingTimes, incompatibilities);
        }
    }
}