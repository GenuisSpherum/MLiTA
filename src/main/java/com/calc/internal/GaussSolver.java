package com.calc.internal;

public class GaussSolver {
    public static double[] solveGauss(double[][] A, double[] B) {
        int n = A.length;
        double[][] augmentedMatrix = new double[n][n + 1];

        // Формируем расширенную матрицу (A | B)
        for (int i = 0; i < n; i++) {
            System.arraycopy(A[i], 0, augmentedMatrix[i], 0, n);
            augmentedMatrix[i][n] = B[i];
        }

        // Прямой ход: приведение к верхнетреугольному виду
        for (int col = 0; col < n; col++) {
            // Поиск строки с максимальным элементом в текущем столбце (для устойчивости)
            int maxRow = col;
            for (int i = col + 1; i < n; i++) {
                if (Math.abs(augmentedMatrix[i][col]) > Math.abs(augmentedMatrix[maxRow][col])) {
                    maxRow = i;
                }
            }

            // Переставляем строки
            double[] temp = augmentedMatrix[col];
            augmentedMatrix[col] = augmentedMatrix[maxRow];
            augmentedMatrix[maxRow] = temp;

            // Обнуляем элементы под главной диагональю
            for (int i = col + 1; i < n; i++) {
                double factor = augmentedMatrix[i][col] / augmentedMatrix[col][col];
                for (int j = col; j <= n; j++) {
                    augmentedMatrix[i][j] -= factor * augmentedMatrix[col][j];
                }
            }
        }

        // Обратный ход: находим переменные методом подстановки
        double[] solution = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            solution[i] = augmentedMatrix[i][n];
            for (int j = i + 1; j < n; j++) {
                solution[i] -= augmentedMatrix[i][j] * solution[j];
            }
            solution[i] /= augmentedMatrix[i][i];
        }

        return solution;
    }
}