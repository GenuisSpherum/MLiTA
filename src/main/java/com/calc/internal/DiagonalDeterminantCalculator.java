package com.calc.internal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DiagonalDeterminantCalculator {
    private static final Map<String, Double> memory = new HashMap<>();

    public static double findDeterminant(double[][] matrix) {
        int n = matrix.length;

        if (n == 1) {
            return matrix[0][0];
        }

        if (n == 2) {
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        }

        String key = matrixToString(matrix);
        if (memory.containsKey(key)) {
            return memory.get(key);
        }

        double[][] diagonalMatrix = toDiagonalMatrix(matrix);
        double determinant = 1.0;
        for (int i = 0; i < n; i++) {
            determinant *= diagonalMatrix[i][i];
        }

        memory.put(key, determinant);
        return determinant;
    }

    private static double[][] toDiagonalMatrix(double[][] matrix) {
        int n = matrix.length;
        double[][] mat = Arrays.stream(matrix).map(double[]::clone).toArray(double[][]::new);

        for (int col = 0; col < n; col++) {
            // Поиск максимального элемента в столбце для выбора главного элемента
            int maxRow = col;
            for (int i = col + 1; i < n; i++) {
                if (Math.abs(mat[i][col]) > Math.abs(mat[maxRow][col])) {
                    maxRow = i;
                }
            }

            // Перестановка строк (если нужно)
            if (maxRow != col) {
                double[] temp = mat[col];
                mat[col] = mat[maxRow];
                mat[maxRow] = temp;
            }

            // Обнуление элементов под главным элементом
            for (int i = col + 1; i < n; i++) {
                double factor = mat[i][col] / mat[col][col];
                for (int j = col; j < n; j++) {
                    mat[i][j] -= factor * mat[col][j];
                }
            }
        }
        return mat;
    }

    private static String matrixToString(double[][] matrix) {
        return Arrays.deepToString(matrix);
    }
}
