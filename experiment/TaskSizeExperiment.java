package experiment;

import model.ProblemInstance;
import solver.GeneticSolver;
import solver.GreedySolver;
import util.Generator;

public class TaskSizeExperiment {
    public static void run(int m, double d, int mean, int delta, int k, int nMin, int nMax, int nStep) {
        System.out.println("\nЕксперимент: Визначення впливу розмірності задач на точність та час роботи алгоритмів");
        System.out.println("Експеримент розпочато. Це може зайняти декілька хвилин...");

        for (int n = nMin; n <= nMax; n += nStep) {
            double greedyTotalValue = 0;
            double geneticTotalValue = 0;
            long greedyTotalTime = 0;
            long geneticTotalTime = 0;

            for (int i = 0; i < k; i++) {
                ProblemInstance instance = Generator.generate(n, m, mean - delta, mean + delta, d);

                long greedyStart = System.nanoTime();
                int[] greedy = GreedySolver.solve(instance);
                long greedyEnd = System.nanoTime();
                int greedyValue = evaluate(greedy, instance);
                greedyTotalValue += greedyValue;
                greedyTotalTime += (greedyEnd - greedyStart);

                long geneticStart = System.nanoTime();
                int[] genetic = GeneticSolver.solve(instance, 50, 100, 0.1);
                long geneticEnd = System.nanoTime();
                int geneticValue = evaluate(genetic, instance);
                geneticTotalValue += geneticValue;
                geneticTotalTime += (geneticEnd - geneticStart);
            }

            double averageGreedyValue = greedyTotalValue / k;
            double averageGeneticValue = geneticTotalValue / k;
            double averageGreedyTime = greedyTotalTime / 1_000_000.0 / k;
            double averageGeneticTime = geneticTotalTime / 1_000_000.0 / k;

            System.out.printf("n = %d\n", n);
            System.out.printf("  ЖА: ЦФ = %.2f, час = %.2f мс\n", averageGreedyValue, averageGreedyTime);
            System.out.printf("  ГА: ЦФ = %.2f, час = %.2f мс\n", averageGeneticValue, averageGeneticTime);
            System.out.printf("  Відносна різниця ЦФ = %.2f%%\n", Math.abs(((averageGeneticValue - averageGreedyValue) * 100.0 / averageGreedyValue)));
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

