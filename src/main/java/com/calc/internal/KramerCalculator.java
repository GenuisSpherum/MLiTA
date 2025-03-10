package com.calc.internal;

import java.util.Arrays;

public class KramerCalculator {
    public static double[] solveCramer(double[][] mainMatrix, double[] replacer) {
        int n = mainMatrix.length;
        double detA = DefaultDeterminantCalculator.findDeterminant(mainMatrix);
        if (detA == 0) throw new IllegalArgumentException("Определитель матрицы равен нулю => решений нет");

        double[] result = new double[n];
        for (int i = 0; i < n; i++) {
            double[][] Ai = Arrays.stream(mainMatrix)
                    .map(double[]::clone)
                    .toArray(double[][]::new);
            for (int j = 0; j < n; j++) {
                Ai[j][i] = replacer[j];
            }
            result[i] = DefaultDeterminantCalculator.findDeterminant(Ai) / detA;
        }
        return result;
    }
}
