package experiment;

import model.ProblemInstance;
import solver.GeneticSolver;
import solver.GreedySolver;
import util.ChartUtil;
import util.Generator;

public class ProcessingTimeExperiment {
    public static void run(int n, int m, double d, int delta, int k, int meanMin, int meanMax, int meanStep) {
        System.out.println("\nЕксперимент: Дослідження впливу параметрів задачі на ефективність алгоритмів");
        System.out.println("Експеримент розпочато. Це може зайняти декілька хвилин...");

        int steps = ((meanMax - meanMin) / meanStep) + 1;
        double[] x = new double[steps];
        double[] greedyValues = new double[steps];
        double[] geneticValues = new double[steps];

        int index = 0;
        for (int mean = meanMin; mean <= meanMax; mean += meanStep) {
            double totalDifference = 0;
            double greedyTotal = 0;
            double geneticTotal = 0;
            for (int i = 0; i < k; i++) {
                ProblemInstance instance = Generator.generate(n, m, mean - delta, mean + delta, d);
                int[] greedy = GreedySolver.solve(instance);
                int[] genetic = GeneticSolver.solve(instance, 50, 100, 0.1);
                int greedyValue = evaluate(greedy, instance);
                int geneticValue = evaluate(genetic, instance);
                greedyTotal += greedyValue;
                geneticTotal += geneticValue;
                totalDifference += (geneticValue - greedyValue) * 100.0 / greedyValue;
            }
            double averageGreedy = greedyTotal / k;
            double averageGenetic = geneticTotal / k;
            double averageDifference = Math.abs(totalDifference / k);

            x[index] = mean;
            greedyValues[index] = averageGreedy;
            geneticValues[index] = averageGenetic;

            System.out.printf("p̄ = %d: Відносна різниця ЦФ ГА від ЖА = %.2f%%\n", mean, averageDifference);
            index++;
        }

        ChartUtil.showMultipleLineChart("Порівняння алгоритмів за ефективністю",
                "Середній час обробки деталей", "Середнє значення ЦФ",
                new String[]{"Жадібний алгоритм", "Генетичний алгоритм"},
                x, new double[][]{greedyValues, geneticValues}
        );
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
