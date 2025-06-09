package model;

public class ProblemInstance {
    public int n;
    public int m;
    public int[] processingTimes;
    public boolean[][] incompatibilities;

    public ProblemInstance(int n, int m, int[] processingTimes, boolean[][] incompatibilities) {
        this.n = n;
        this.m = m;
        this.processingTimes = processingTimes;
        this.incompatibilities = incompatibilities;
    }
}