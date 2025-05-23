package com.calc.internal;

import java.util.Arrays;

public class DiagonalDeterminantCalculator {
    public static Object[] findDeterminant(double[][] matrix) {
        int n = matrix.length;
        double[][] A = Arrays.stream(matrix).map(double[]::clone).toArray(double[][]::new);
        double EPS = 1e-10;

        double det = 1.0;
        int swapCount = 0;

        // приводим к верхнетреугольному виду
        for (int col = 0; col < n; col++) {
            // ищем максимальный по модулю элемент в колонке
            int maxRow = col;
            for (int i = col; i < n; i++) {
                if (Math.abs(A[i][col]) > Math.abs(A[maxRow][col])) {
                    maxRow = i;
                }
            }

            // если весь столбец нулевой, определитель равен 0
            if (Math.abs(A[maxRow][col]) < EPS) {
                det = 0.0;
                break;
            }

            // меняем строки чтобы самый большой элемент оказался на диагонали
            if (maxRow != col) {
                double[] temp = A[col];
                A[col] = A[maxRow];
                A[maxRow] = temp;
                swapCount++;
            }

            // тут все обнуляем под и над текущим элементом (кроме row)
            for (int i = col + 1; i < n; i++) {
                double factor = A[i][col] / A[col][col];
                for (int j = col; j < n; j++) {
                    A[i][j] -= factor * A[col][j];
                }
            }
        }

        // если детерминант не был обнулен
        if (Math.abs(det) > EPS) {
            for (int i = 0; i < n; i++) {
                det *= A[i][i];
            }
            // если было нечётное число перестановок -> меняем знак
            if (swapCount % 2 != 0) {
                det = -det;
            }
        }

        return new Object[]{A, det};
    }
}