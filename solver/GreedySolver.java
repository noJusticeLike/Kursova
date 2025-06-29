package solver;

import model.ProblemInstance;
import java.util.Arrays;

public class GreedySolver {
    public static int[] solve(ProblemInstance instance) {
        int[] machineLoad = new int[instance.m];
        int[] assignment = new int[instance.n];
        Arrays.fill(assignment, -1);

        Integer[] indexes = new Integer[instance.n];
        for (int i = 0; i < instance.n; i++) {
            indexes[i] = i;
        }

        Arrays.sort(indexes, (index1, index2) -> Integer.compare(instance.processingTimes[index2], instance.processingTimes[index1]));

        for (int index : indexes) {
            int bestMachine = -1;
            int minLoad = Integer.MAX_VALUE;

            for (int j = 0; j < instance.m; j++) {
                boolean conflict = false;
                for (int k = 0; k < instance.n; k++) {
                    if (assignment[k] == j && instance.incompatibilities[index][k]) {
                        conflict = true;
                        break;
                    }
                }
                if (!conflict && machineLoad[j] < minLoad) {
                    minLoad = machineLoad[j];
                    bestMachine = j;
                }
            }

            if (bestMachine == -1) {
                bestMachine = 0;
                for (int j = 1; j < instance.m; j++) {
                    if (machineLoad[j] < machineLoad[bestMachine]) {
                        bestMachine = j;
                    }
                }
            }
            assignment[index] = bestMachine;
            machineLoad[bestMachine] += instance.processingTimes[index];
        }
        return assignment;
    }
}

