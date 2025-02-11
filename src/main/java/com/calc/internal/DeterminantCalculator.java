package com.calc.internal;

public class DeterminantCalculator {
    public static double findDeterminant(double[][] matrix) {
        int n = matrix.length;

        if (n == 1) {
            return matrix[0][0];
        }

        if (n == 2) {
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        }

        double determinant = 0;
        for (int col = 0; col < n; col++) {
            determinant += Math.pow(-1, col) * matrix[0][col] * findDeterminant(minor(matrix, 0, col));
        }

        return determinant;
    }

    // считаем минор
    private static double[][] minor(double[][] matrix, int row, int col) {
        int n = matrix.length;
        double[][] minor = new double[n - 1][n - 1];

        int minorRow = 0;
        for (int i = 0; i < n; i++) {
            if (i == row) continue;

            int minorCol = 0;
            for (int j = 0; j < n; j++) {
                if (j == col) continue;

                minor[minorRow][minorCol] = matrix[i][j];
                minorCol++;
            }
            minorRow++;
        }

        return minor;
    }
}
