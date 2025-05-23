package com.calc.internal;

import static com.calc.internal.DefaultDeterminantCalculator.minor;

public class MatrixInverter {

    public static double[][] inverseMatrix(double[][] matrix) {
        int n = matrix.length;
        // Находим общий определитель матрицы
        double det = DefaultDeterminantCalculator.findDeterminant(matrix);
        if (det == 0) {
            throw new ArithmeticException("Обратная матрица не существует -> det = 0");
        }

        // Находим локальные определители
        double[][] cofactors = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double localDet = DefaultDeterminantCalculator.findDeterminant(minor(matrix, i, j));
                cofactors[i][j] = Math.pow(-1, i + j) * localDet;
            }
        }

        // транспонируем
        double[][] adj = transpose(cofactors);

        // строим обратную матрицу
        double[][] invMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                invMatrix[i][j] = (1 / det) * adj[i][j];
            }
        }

        return invMatrix;
    }

    private static double[][] transpose(double[][] matrix) {
        int n = matrix.length;
        double[][] result = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[j][i] = matrix[i][j];
            }
        }
        return result;
    }
}