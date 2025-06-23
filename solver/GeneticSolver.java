package solver;

import model.ProblemInstance;

import java.util.*;

public class GeneticSolver {
    private static final Random rand = new Random();

    public static int[] solve(ProblemInstance instance, int populationSize, int maxStallIterations, double mutationProbability) {
        List<int[]> population = initializePopulation(instance, populationSize);
        int[] best = findBest(population, instance);
        int bestFitness = evaluate(best, instance);
        int stall = 0;

        while (stall < maxStallIterations) {
            List<int[]> newPopulation = new ArrayList<>();
            newPopulation.add(best.clone());

            while (newPopulation.size() < populationSize) {
                int[] parent1 = tournamentSelection(population, instance, 5);
                int[] parent2 = tournamentSelection(population, instance, 5);
                int[] child = crossover(parent1, parent2, instance);
                mutate(child, instance, mutationProbability);
                reanimate(child, instance);
                newPopulation.add(child);
            }

            int[] currentBest = findBest(newPopulation, instance);
            int currentFitness = evaluate(currentBest, instance);

            if (currentFitness < bestFitness) {
                bestFitness = currentFitness;
                best = currentBest.clone();
                stall = 0;
            } else {
                stall++;
            }
            population = newPopulation;
        }
        return best;
    }

    private static List<int[]> initializePopulation(ProblemInstance instance, int size) {
        List<int[]> population = new ArrayList<>();
        int[] greedySolution = GreedySolver.solve(instance);
        population.add(greedySolution);

        while (population.size() < size) {
            int[] assignment = new int[instance.n];
            for (int i = 0; i < instance.n; i++) {
                assignment[i] = rand.nextInt(instance.m);
            }
            reanimate(assignment, instance);
            population.add(assignment);
        }
        return population;
    }

    private static int[] crossover(int[] parent1, int[] parent2, ProblemInstance instance) {
        int[] child = new int[instance.n];
        int point = rand.nextInt(instance.n);
        for (int i = 0; i < point; i++) {
            child[i] = parent1[i];
        }
        for (int i = point; i < instance.n; i++) {
            child[i] = parent2[i];
        }
        return child;
    }

    private static void mutate(int[] solution, ProblemInstance instance, double mutationProbability) {
        for (int i = 0; i < solution.length; i++) {
            if (rand.nextDouble() < mutationProbability) {
                solution[i] = rand.nextInt(instance.m);
            }
        }
    }

    private static void reanimate(int[] solution, ProblemInstance instance) {
        boolean changed;
        int maxTries = 100;
        int tries = 0;
        do {
            changed = false;
            for (int m = 0; m < instance.m; m++) {
                for (int i = 0; i < instance.n; i++) {
                    for (int j = i + 1; j < instance.n; j++) {
                        if (solution[i] == m && solution[j] == m &&
                                instance.incompatibilities[i][j]) {
                            if (instance.processingTimes[i] < instance.processingTimes[j])
                                solution[i] = (solution[i] + 1) % instance.m;
                            else
                                solution[j] = (solution[j] + 1) % instance.m;
                            changed = true;
                        }
                    }
                }
            }
            tries++;
        } while (changed && tries < maxTries);
    }

    private static int[] tournamentSelection(List<int[]> population, ProblemInstance instance, int tournamentSize) {
        int[] best = null;
        int bestFitness = Integer.MAX_VALUE;

        for (int i = 0; i < tournamentSize; i++) {
            int[] candidate = population.get(rand.nextInt(population.size()));
            int fitness = evaluate(candidate, instance);
            if (fitness < bestFitness) {
                bestFitness = fitness;
                best = candidate;
            }
        }
        return best;
    }

    private static int[] findBest(List<int[]> population, ProblemInstance instance) {
        int[] best = null;
        int bestFitness = Integer.MAX_VALUE;
        for (int[] p : population) {
            int fitness = evaluate(p, instance);
            if (fitness < bestFitness) {
                bestFitness = fitness;
                best = p;
            }
        }
        return best;
    }

    private static int evaluate(int[] solution, ProblemInstance instance) {
        int[] machineLoad = new int[instance.m];
        for (int i = 0; i < instance.n; i++) {
            machineLoad[solution[i]] += instance.processingTimes[i];
        }
        return Arrays.stream(machineLoad).max().orElse(0);
    }
}

