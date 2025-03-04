package com.calc.internal;

import java.util.Arrays;

public class KramerCalculator {
    public static double[] solveCramer(double[][] A, double[] B) {
        if (A.length != A[0].length) {
            throw new IllegalArgumentException("Матрица A должна быть квадратной!");
        }
        if (A.length != B.length) {
            throw new IllegalArgumentException("Размерность B должна совпадать с числом строк A!");
        }

        System.out.println("Размерность A: " + A.length + "x" + A[0].length);
        System.out.println("Длина B: " + B.length);
        int n = A.length;
        double detA = DeterminantCalculator.findDeterminant(A);
        if (detA == 0) throw new IllegalArgumentException("Определитель матрицы равен нулю, система не имеет единственного решения");

        double[] result = new double[n];
        for (int i = 0; i < n; i++) {
            double[][] Ai = Arrays.stream(A).map(double[]::clone).toArray(double[][]::new);
            for (int j = 0; j < n; j++) {
                Ai[j][i] = B[j];
            }
            result[i] = DeterminantCalculator.findDeterminant(Ai) / detA;
        }
        return result;
    }
}
