package experiment;

import model.ProblemInstance;
import solver.GeneticSolver;
import util.Generator;

public class IterationNumberExperiment {
    public static void run(int n, int m, double d, int mean, int delta, int k, int piMin, int piMax, int piStep) {
        System.out.println("\nЕксперимент: визначення параметра умови завершення роботи алгоритму");
        System.out.println("Експеримент розпочато. Це може зайняти декілька хвилин...");

        for (int piMult = piMin; piMult <= piMax; piMult += piStep) {
            double total = 0;
            for (int i = 0; i < k; i++) {
                ProblemInstance instance = Generator.generate(n, m, mean - delta, mean + delta, d);
                int pi = (int) (piMult * n * (Math.log(n) / Math.log(2)));
                int[] solution = GeneticSolver.solve(instance, 50, pi, 0.1);
                total += evaluate(solution, instance);
            }
            double average = total / k;
            System.out.printf("π = %d * n log2(n): Середнє значення ЦФ = %.2f\n", piMult, average);
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


