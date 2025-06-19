import model.ProblemInstance;
import solver.GreedySolver;
import solver.GeneticSolver;
import util.Generator;
import util.InputOutput;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static ProblemInstance instance = null;
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        while (true) {
            System.out.println("\nГоловне меню");
            System.out.println("Статус задачі: " + (instance == null ? "Задачу не задано." : "Задачу задано."));
            System.out.println("""
                \nДоступні опції:
                1. Внести дані задачі
                2. Розв'язати задачу всіма можливими алгоритмами
                3. Вивести дані задачі
                4. Запустити експерименти
                0. Завершити роботу
                """);

            int choice = readInt("Введіть число: ");
            switch (choice) {
                case 1 -> inputMenu();
                case 2 -> solveMenu();
                case 3 -> outputMenu();
                case 4 -> experimentMenu();
                case 0 -> System.exit(0);
                default -> System.out.println("Невірний вибір.");
            }
        }
    }

    private static void inputMenu(){
        while (true) {
            System.out.println("""
                    \nПідменю для внесення даних задачі.

                    Доступні опції:
                    1. Внести дані вручну
                    2. Згенерувати дані випадковим чином
                    3. Зчитати дані з файлу
                    0. Повернутись в головне меню
                    """);

            int choice = readInt("Введіть число: ");
            switch (choice) {
                case 1 -> {
                    int n = readIntInRange("Кількість деталей(1-1000): ", 1, 1000);
                    int m = readIntInRange("Кількість верстатів(1-100): ", 1, 100);
                    int[] processingTimes = new int[n];
                    System.out.println("Тривалості обробки деталей (1–100):");
                    for (int i = 0; i < n; i++) {
                        processingTimes[i] = readIntInRange("Деталь " + i + ": ", 1, 100);
                    }
                    boolean[][] incompatibilities = new boolean[n][n];
                    System.out.println("Введіть несумісності (спочатку індекс i, потім j, -1 для завершення): ");
                    while (true) {
                        int i = readInt("i: ");
                        if (i == -1) break;
                        int j = readInt("j: ");
                        if (j == -1) break;

                        if (i >= 0 && i < n && j >= 0 && j < n && i != j) {
                            incompatibilities[i][j] = incompatibilities[j][i] = true;
                        } else {
                            System.out.println("Невірні індекси.");
                        }
                    }
                    instance = new ProblemInstance(n, m, processingTimes, incompatibilities);
                    System.out.println("Задачу успішно задано.");
                    return;
                }
                case 2 -> {
                    int n = readIntInRange("Кількість деталей: ", 1, 1000);
                    int m = readIntInRange("Кількість верстатів: ", 1, 100);
                    int minTime = readIntInRange("Мінімальний час обробки: ", 1, 100);
                    int maxTime = readIntInRange("Максимальний час обробки: ", minTime, 1000);
                    double density = readDoubleInRange("Щільність несумісностей (0.0 - 1.0): ", 0.0, 1.0);

                    instance = Generator.generate(n, m, minTime, maxTime, density);
                    System.out.println("Задачу згенеровано випадково.");
                    return;
                }
                case 3 -> {
                    System.out.print("Введіть назву файлу: ");
                    String filename = sc.next();
                    try {
                        instance = InputOutput.readFromFile(filename);
                        System.out.println("Задачу зчитано з файлу.");
                    } catch (java.io.FileNotFoundException e) {
                        System.out.println("Помилка: файл не знайдено. Перевірте назву файлу і повторіть спробу.");
                    } catch (Exception e) {
                        System.out.println("Сталася помилка при зчитуванні з файлу: " + e.getMessage());
                    }
                    return;
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Невірний вибір.");
            }
        }
    }

    private static void solveMenu() {
        if (instance == null) {
            System.out.println("Помилка: задачу не задано.");
            return;
        }

        int[] greedy = GreedySolver.solve(instance);
        int greedyValue = evaluate(greedy, instance);
        System.out.println("Жадібний алгоритм:");
        printSchedule(greedy, instance, true);
        System.out.println("Максимальний час обробки: " + greedyValue + "\n");

        int[] genetic = GeneticSolver.solve(instance, 50, 100, 0.1);
        int geneticValue = evaluate(genetic, instance);
        System.out.println("Генетичний алгоритм:");
        printSchedule(genetic, instance, false);
        System.out.println("Максимальний час обробки: " + geneticValue);
    }

    private static void outputMenu() throws Exception {
        if (instance == null) {
            System.out.println("Помилка: задачу не задано.");
            return;
        }

        while (true) {
            System.out.println("""
                    \nПідменю для виведення даних задачі.

                    Доступні опції:
                    1. Вивести дані на екран
                    2. Записати дані до файлу
                    0. Повернутися в головне меню
                    """);

            int choice = readInt("Введіть число: ");
            switch (choice) {
                case 1 -> printInstance();
                case 2 -> {
                    System.out.print("Введіть назву файлу: ");
                    InputOutput.saveToFile(instance, sc.next());

                    System.out.println("Дані записано у файл.");
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Невірний вибір.");
            }
        }
    }

    private static void experimentMenu() {
        while (true) {
            System.out.println("""
                    \nПідменю для проведення експериментів.

                    Доступні дослідження:
                    1. Визначення параметра умови завершення роботи алгоритму
                    2. Дослідження впливу специфічних параметрів наближеного алгоритму
                    3. Дослідження впливу параметрів задачі на ефективність алгоритмів
                    4. Визначення впливу розмірності задач на точність та час роботи алгоритмів
                    0. Повернутися в головне меню
                    """);

            int choice = readInt("Введіть число: ");
            if (choice == 0) return;
            switch (choice) {
                case 1 -> {
                    int n = readInt("n (кількість деталей): ");
                    int m = readInt("m (кількість верстатів): ");
                    double d = readDoubleInRange("Щільність несумісностей (0-1): ", 0, 1);
                    int mean = readInt("Математичне сподівання тривалості обробки: ");
                    int delta = readInt("Значення напівінтервалу: ");
                    int K = readInt("Кількість прогонів: ");
                    int piMin = readInt("Мінімальний множник для π: ");
                    int piMax = readInt("Максимальний множник  для π: ");
                    int piStep = readInt("Крок: ");

                    experiment.IterationNumberExperiment.run(n, m, d, mean, delta, K, piMin, piMax, piStep);
                }

                case 2 -> {
                    int n = readInt("n (кількість деталей): ");
                    int m = readInt("m (кількість верстатів): ");
                    double d = readDoubleInRange("Щільність несумісностей (0-1): ", 0, 1);
                    int mean = readInt("Математичне сподівання тривалості обробки: ");
                    int delta = readInt("Значення напівінтервалу: ");
                    int K = readInt("Кількість прогонів: ");
                    int minPopulation = readInt("Мінімальний розмір популяції: ");
                    int maxPopulation = readInt("Максимальний розмір популяції: ");
                    int populationStep = readInt("Крок: ");

                    experiment.PopulationSizeExperiment.run(n, m, d, mean, delta, K, minPopulation, maxPopulation, populationStep);
                }

                case 3 -> {
                    int n = readInt("n (кількість деталей): ");
                    int m = readInt("m (кількість верстатів): ");
                    double d = readDoubleInRange("Щільність несумісностей (0-1): ", 0, 1);
                    int delta = readInt("Значення напівінтервалу: ");
                    int K = readInt("Кількість прогонів: ");
                    int meanMin = readInt("Мінімальне математичне сподівання тривалості обробки: ");
                    int meanMax = readInt("Максимальне математичне сподівання тривалості обробки: ");
                    int meanStep = readInt("Крок: ");

                    experiment.ProcessingTimeExperiment.run(n, m, d, delta, K, meanMin, meanMax, meanStep);
                }

                case 4 -> {
                    int m = readInt("m (кількість верстатів): ");
                    double d = readDoubleInRange("Щільність несумісностей (0-1): ", 0, 1);
                    int mean = readInt("Математичне сподівання тривалості обробки: ");
                    int delta = readInt("Значення напівінтервалу: ");
                    int K = readInt("Кількість прогонів: ");
                    int nMin = readInt("Мінімальна кількість деталей: ");
                    int nMax = readInt("Максимальна кількість деталей: ");
                    int nStep = readInt("Крок: ");

                    experiment.TaskSizeExperiment.run(m, d, mean, delta, K, nMin, nMax, nStep);
                }
                default -> System.out.println("Невірний вибір.");
            }
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

    private static void printInstance() {
        System.out.println("\nКількість деталей: " + instance.n);
        System.out.println("Кількість верстатів: " + instance.m);
        System.out.println("Часи обробки деталей:");
        for (int i = 0; i < instance.n; i++) {
            System.out.printf("Деталь %d: %d\n", i, instance.processingTimes[i]);
        }

        System.out.println("Несумісності:");
        boolean hasIncompatibilities = false;
        for (int i = 0; i < instance.n; i++) {
            for (int j = i + 1; j < instance.n; j++) {
                if (instance.incompatibilities[i][j]) {
                    System.out.println(i + " <-> " + j);
                    hasIncompatibilities = true;
                }
            }
        }

        if (!hasIncompatibilities) {
            System.out.println("Немає несумісностей.");
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Помилка: введено неціле число.");
                sc.nextLine();
            }
        }
    }

    private static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt);
            if (value >= min && value <= max)
                return value;
            System.out.println("Число повинно бути в межах від " + min + " до " + max + ".");
        }
    }

    private static double readDoubleInRange(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = sc.nextDouble();
                if (value >= min && value <= max) {
                    return value;
                }
                else {
                    System.out.println("Число повинно бути в межах від " + min + " до " + max + ".");
                }
            } catch (InputMismatchException e) {
                System.out.println("Помилка: введено нечислове значення.");
                sc.nextLine();
            }
        }
    }

    private static void printSchedule(int[] solution, ProblemInstance instance, boolean sortByProcessingTime) {
        List<List<Integer>> machineAssignments = new ArrayList<>();
        for (int i = 0; i < instance.m; i++) {
            machineAssignments.add(new ArrayList<>());
        }

        for (int i = 0; i < instance.n; i++) {
            machineAssignments.get(solution[i]).add(i);
        }

        for (int machine = 0; machine < instance.m; machine++) {
            List<Integer> tasks = machineAssignments.get(machine);

            if (sortByProcessingTime) {
                tasks.sort((a, b) -> Integer.compare(instance.processingTimes[b], instance.processingTimes[a]));
            }

            System.out.print("Верстат " + machine + ": ");
            if (tasks.isEmpty()) {
                System.out.println("немає деталей");
            } else {
                for (int i : tasks) {
                    System.out.print("Деталь " + i + " (" + instance.processingTimes[i] + ") ");
                }
                System.out.println();
            }
        }
    }
}



