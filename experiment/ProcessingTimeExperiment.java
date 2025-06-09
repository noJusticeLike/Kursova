package experiment;

import model.ProblemInstance;
import solver.GeneticSolver;
import solver.GreedySolver;
import util.Generator;

public class ProcessingTimeExperiment {
    public static void run(int n, int m, double d, int delta, int k, int meanMin, int meanMax, int meanStep) {
        System.out.println("\nЕксперимент: Дослідження впливу параметрів задачі на ефективність алгоритмів");
        System.out.println("Експеримент розпочато. Це може зайняти декілька хвилин...");

        for (int mean = meanMin; mean <= meanMax; mean += meanStep) {
            double totalDifference = 0;
            for (int i = 0; i < k; i++) {
                ProblemInstance instance = Generator.generate(n, m, mean - delta, mean + delta, d);
                int[] greedy = GreedySolver.solve(instance);
                int[] genetic = GeneticSolver.solve(instance, 50, 100, 0.1);
                int greedyValue = evaluate(greedy, instance);
                int geneticValue = evaluate(genetic, instance);
                totalDifference += (geneticValue - greedyValue) * 100.0 / greedyValue;
            }
            double averageDifference = Math.abs(totalDifference / k);
            System.out.printf("p̄ = %d: Відносна різниця ЦФ ГА від ЖА = %.2f%%\n", mean, averageDifference);
        }
    }

    private static int evaluate(int[] solution, ProblemInstance instance) {
        int[] load = new int[instance.m];
        for (int i = 0; i < instance.n; i++)
            load[solution[i]] += instance.processingTimes[i];
        int max = 0;
        for (int x : load) max = Math.max(max, x);
        return max;
    }
}
