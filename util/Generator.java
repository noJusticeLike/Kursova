package util;

import model.ProblemInstance;

import java.util.*;

public class Generator {
    public static ProblemInstance generate(int n, int m, int minTime, int maxTime, double density) {
        Random rand = new Random();
        int[] processingTimes = new int[n];
        boolean[][] incompatibilities = new boolean[n][n];

        for (int i = 0; i < n; i++) {
            processingTimes[i] = rand.nextInt(maxTime - minTime + 1) + minTime;
        }

        int maxPairs = n * (n - 1) / 2;
        int targetIncompatibilities = (int) Math.round(density * maxPairs);

        List<int[]> pairs = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                pairs.add(new int[]{i, j});
            }
        }

        Collections.shuffle(pairs, rand);
        for (int k = 0; k < targetIncompatibilities && k < pairs.size(); k++) {
            int i = pairs.get(k)[0];
            int j = pairs.get(k)[1];
            incompatibilities[i][j] = true;
            incompatibilities[j][i] = true;
        }

        return new ProblemInstance(n, m, processingTimes, incompatibilities);
    }
}
