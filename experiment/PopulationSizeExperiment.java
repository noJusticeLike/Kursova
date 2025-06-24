package experiment;

import model.ProblemInstance;
import solver.GeneticSolver;
import util.Generator;
import util.ChartUtil;

public class PopulationSizeExperiment {
    public static void run(int n, int m, double d, int mean, int delta, int k, int minPopulation, int maxPopulation, int populationStep) {
        System.out.println("\nЕксперимент: дослідження впливу специфічних параметрів наближеного алгоритму");
        System.out.println("Експеримент розпочато. Це може зайняти декілька хвилин...");

        int steps = ((maxPopulation - minPopulation) / populationStep) + 1;
        double[] x = new double[steps];
        double[] y = new double[steps];

        int index = 0;
        for (int population = minPopulation; population <= maxPopulation; population += populationStep) {
            double total = 0;
            for (int i = 0; i < k; i++) {
                ProblemInstance instance = Generator.generate(n, m, mean - delta, mean + delta, d);
                int[] solution = GeneticSolver.solve(instance, population, 100, 0.1);
                total += evaluate(solution, instance);
            }
            double average = total / k;
            x[index] = population;
            y[index] = average;
            System.out.printf("l = %d: Середнє значення ЦФ = %.2f\n", population, average);
            index++;
        }

        ChartUtil.showLineChart("Вплив розміру популяції на результат",
                "Розмір популяції", "Середнє значення ЦФ",
                x, y);
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

