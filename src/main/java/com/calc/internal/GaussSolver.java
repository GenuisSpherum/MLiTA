package com.calc.internal;

import java.util.*;

public class GaussSolver {
    public static Object solveGauss(double[][] A, double[] B) {
//        n - кол-во уравнений (строки A)
//        m - кол-во переменных (столбцы A)
        int n = A.length;
        int m = A[0].length;
        // тут будет номер строки, в которой переменная xi имеет ненулевой коэффициент и является “главной” в ур-ии
        int[] rowIndex = new int[m];
        // рещервируем индексами -1
        Arrays.fill(rowIndex, -1);
        // чтобы погрешность не влияла на результат
        double EPS = 1e-10;

        // Прямой ход: приводим к верхнетреугольному виду
        for (int col = 0, row = 0; col < m && row < n; col++) {
            // ищем максимальнйыйй по модулю элемент в колонке
            int maxModEl = row;
            for (int i = row; i < n; i++) {
                if (Math.abs(A[i][col]) > Math.abs(A[maxModEl][col])) {
                    maxModEl = i;
                }
            }
            // если нет ненулевых элементов
            if (Math.abs(A[maxModEl][col]) < EPS)
                continue;

            // меняем строки чтобы самый большой элемент оказался на диагонали
            double[] tmp = A[maxModEl];
            A[maxModEl] = A[row];
            A[row] = tmp;

            double t = B[maxModEl];
            B[maxModEl] = B[row];
            B[row] = t;

            rowIndex[col] = row;

            // тут все обнуляем под и над текущим элементом (кроме row)
            for (int i = row + 1; i < n; i++) {
                double factor = A[i][col] / A[row][col];
                for (int j = col; j < m; j++) {
                    A[i][j] -= factor * A[row][j];
                }
                B[i] -= factor * B[row];
            }
            row++;
        }

        // Проверка на несовместность - есть ли среди строк такие,
        // где все коэфы нули, а свободный член не 0 => несовместна
        for (int i = 0; i < n; i++) {
            boolean isAllZero = true;
            for (int j = 0; j < m; j++) {
                if (Math.abs(A[i][j]) > EPS) {
                    isAllZero = false;
                    break;
                }
            }
            if (isAllZero && Math.abs(B[i]) > EPS) {
                throw new IllegalArgumentException("Система несовместна");
            }
        }

        // считаем ранг матриыц
        // надо чтобы понять сколько решений имеет матрица (rk == m => 1 решение, иначе беск)
        int rk = 0;
        for (int i = 0; i < m; i++) {
            // переменная ведущая (нашли для нее строку)
            if (rowIndex[i] != -1) {
                rk++;
            }
        }

        // если решений бесконечно
        if (rk < m) {
            String[] answer = new String[m];
            for (int i = m - 1; i >= 0; i--) {
                if (rowIndex[i] == -1) {
                    answer[i] = "любое вещественное число (R)";
                } else {
                    StringBuilder output = new StringBuilder();
                    output.append(String.format("%.4f", B[rowIndex[i]]));
                    for (int j = i + 1; j < m; j++) {
                        if (Math.abs(A[rowIndex[i]][j]) > EPS) {
                            // начальное значение - свободный член / на коэф переменной + знак
                            double coeffSign = -A[rowIndex[i]][j] / A[rowIndex[i]][i];
                            // плюсуем к нему линейные комбинации свободных переменных
                            if (coeffSign >= 0) {
                                output.append(" + ").append(String.format("%.4f", coeffSign)).append(" * x").append(j + 1);
                            } else {
                                output.append(" - ").append(String.format("%.4f", -coeffSign)).append(" * x").append(j + 1);
                            }
                        }
                    }
                    output.insert(0, "(R): ");
                    answer[i] = output.toString();
                }
            }
            return answer;
        } else {
            // когда одно решение
            // находим переменные, начиная с последней переменной (обратный гаусс)
            double[] x = new double[m];
            for (int i = m - 1; i >= 0; i--) {
                x[i] = B[rowIndex[i]];
                for (int j = i + 1; j < m; j++) {
                    x[i] -= A[rowIndex[i]][j] * x[j];
                }
                x[i] /= A[rowIndex[i]][i];
            }
            return x;
        }
    }
}