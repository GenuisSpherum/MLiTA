package com.calc.UI;

import com.calc.internal.*;
import com.calc.utils.ArrayUtils;
import com.calc.utils.JTableUtils;
import com.calc.utils.SwingUtils;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.Arrays;

import static com.calc.internal.MultiplyMatrix.multiply;
import static com.calc.utils.JTableUtils.*;

public class FrameMain extends JFrame {
    private JPanel panelMain;
    private JTable tableInput;
    private JButton buttonLoadInputFromFile;
    private JButton buttonRandomInput;
    private JButton buttonSaveInputInfoFile;
    private JButton buttonFindDeterminant;
    private JTable tableOutput;
    private JTextArea textArea1;
    private JButton buttonKramerButton;
    private JButton buttonDiagonalMatrix;
    private JButton buttonGaussSolver;
    private JButton reverseMatrixButton;
    private JTable table1;
    private JTable table2;
    private JButton multiplyMatrixes;
    private JButton buttonLoadInputFromFile1;

    private JFileChooser fileChooserOpen;
    private JFileChooser fileChooserSave;
    private JMenuBar menuBarMain;
    private JMenu menuLookAndFeel;

    public FrameMain() {
        this.setTitle("Algebraic Calculator");
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        JTableUtils.initJTableForArray(tableInput, 40, true, true, true, true);
        JTableUtils.initJTableForArray(table1, 40, true, true, true, true);
        JTableUtils.initJTableForArray(table2, 40, true, true, true, true);
        JTableUtils.initJTableForArray(tableOutput, 40, true, true, true, true);
        // если чо - убрать ---
        table2.setAutoResizeMode(4);
        tableInput.setAutoResizeMode(4);
        table1.setAutoResizeMode(4);
        // --------
        tableInput.setRowHeight(25);
        table1.setRowHeight(25);
        table2.setRowHeight(25);
        tableOutput.setRowHeight(25);

        fileChooserOpen = new JFileChooser();
        fileChooserSave = new JFileChooser();
        fileChooserOpen.setCurrentDirectory(new File("."));
        fileChooserSave.setCurrentDirectory(new File("."));
        FileFilter filter = new FileNameExtensionFilter("Text files", "txt");
        fileChooserOpen.addChoosableFileFilter(filter);
        fileChooserSave.addChoosableFileFilter(filter);

        fileChooserSave.setAcceptAllFileFilterUsed(false);
        fileChooserSave.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooserSave.setApproveButtonText("Save");

        menuBarMain = new JMenuBar();
        setJMenuBar(menuBarMain);

        menuLookAndFeel = new JMenu();
        menuLookAndFeel.setText("Вид");
        menuBarMain.add(menuLookAndFeel);
        SwingUtils.initLookAndFeelMenu(menuLookAndFeel);

        textArea1.setEditable(false);

        JTableUtils.writeArrayToJTable(tableInput, new int[][]{
                {0, 1, 2},
                {5, 6, 7},
                {5, 6, 7}
        });

        this.pack();

        buttonLoadInputFromFile.addActionListener(actionEvent -> {
            try {
                if (fileChooserOpen.showOpenDialog(panelMain) == JFileChooser.APPROVE_OPTION) {
                    int[][] arr = ArrayUtils.readIntArray2FromFile(fileChooserOpen.getSelectedFile().getPath());
                    JTableUtils.writeArrayToJTable(tableInput, arr);
                }
            } catch (Exception e) {
                SwingUtils.showErrorMessageBox(e);
            }
        });

        buttonLoadInputFromFile1.addActionListener(actionEvent -> {
            try {
                if (fileChooserOpen.showOpenDialog(panelMain) == JFileChooser.APPROVE_OPTION) {
                    int[][] arr = ArrayUtils.readIntArray2FromFile(fileChooserOpen.getSelectedFile().getPath());
                    JTableUtils.writeArrayToJTable(table2, arr);
                }
            } catch (Exception e) {
                SwingUtils.showErrorMessageBox(e);
            }
        });

        buttonRandomInput.addActionListener(actionEvent -> {
            try {
                int[][] matrix = ArrayUtils.createRandomIntMatrix(
                        tableInput.getRowCount(), tableInput.getColumnCount(), 10);
                JTableUtils.writeArrayToJTable(tableInput, matrix);
            } catch (Exception e) {
                SwingUtils.showErrorMessageBox(e);
            }
        });

        buttonSaveInputInfoFile.addActionListener(actionEvent -> {
            try {
                if (fileChooserSave.showSaveDialog(panelMain) == JFileChooser.APPROVE_OPTION) {
                    int[][] matrix = JTableUtils.readIntMatrixFromJTable(tableInput);
                    String file = fileChooserSave.getSelectedFile().getPath();
                    if (!file.toLowerCase().endsWith(".txt")) {
                        file += ".txt";
                    }
                    ArrayUtils.writeArrayToFile(file, matrix);
                }
            } catch (Exception e) {
                SwingUtils.showErrorMessageBox(e);
            }
        });

//        buttonSaveOutputIntoFile.addActionListener(actionEvent -> {
//            try {
//                if (fileChooserSave.showSaveDialog(panelMain) == JFileChooser.APPROVE_OPTION) {
//                    int[][] matrix = JTableUtils.readIntMatrixFromJTable(tableOutput);
//                    String file = fileChooserSave.getSelectedFile().getPath();
//                    if (!file.toLowerCase().endsWith(".txt")) {
//                        file += ".txt";
//                    }
//                    ArrayUtils.writeArrayToFile(file, matrix);
//                }
//            } catch (Exception e) {
//                SwingUtils.showErrorMessageBox(e);
//            }
//        });

        buttonFindDeterminant.addActionListener(actionEvent -> {
            try {
                double[][] matrix = JTableUtils.readDoubleMatrixFromJTable(tableInput);
                double answer = DefaultDeterminantCalculator.findDeterminant(matrix);
                textArea1.setText(String.valueOf(answer));
            } catch (Exception e) {
                SwingUtils.showErrorMessageBox(e);
            }
        });

        buttonDiagonalMatrix.addActionListener(actionEvent -> {
            try {
                double[][] matrix = JTableUtils.readDoubleMatrixFromJTable(tableInput);
                Object[] result = DiagonalDeterminantCalculator.findDeterminant(matrix);

                double[][] upperTriangleMatrix = (double[][]) result[0];
                double determinant = (double) result[1];

                JTableUtils.writeArrayToJTable(tableInput, upperTriangleMatrix);
                textArea1.setText("Определитель: " + determinant);
            } catch (Exception e) {
                SwingUtils.showErrorMessageBox(e);
            }
        });

        buttonKramerButton.addActionListener(actionEvent -> {
            try {
                Object[] matrices = readMatrixAndVectorFromJTable(tableInput);
                double[][] A = (double[][]) matrices[0];
                double[] B = (double[]) matrices[1];

                double[] answer = KramerCalculator.solveCramer(A, B);
                textArea1.setText(Arrays.toString(answer));
            } catch (Exception e) {
                SwingUtils.showErrorMessageBox(e);
            }
        });

        buttonGaussSolver.addActionListener(actionEvent -> {
            try {
                Object[] matrices = readMatrixAndVectorFromJTable(tableInput);
                double[][] A = (double[][]) matrices[0];
                double[] B = (double[]) matrices[1];

                Object solution = GaussSolver.solveGauss(A, B);

                writeMatrixAndVectorToJTable(tableInput, A, B);

                if (solution instanceof double[]) {
                    textArea1.setText("Единственное решение:\n" + Arrays.toString((double[]) solution));
                } else if (solution instanceof String[]) {
                    StringBuilder sb = new StringBuilder("Бесконечно много решений:\n");
                    String[] expressions = (String[]) solution;
                    for (int i = 0; i < expressions.length; i++) {
                        sb.append("x").append(i + 1).append(" = ").append(expressions[i]).append("\n");
                    }
                    textArea1.setText(sb.toString());
                }
            } catch (Exception e) {
                SwingUtils.showErrorMessageBox(e);
            }
        });

        reverseMatrixButton.addActionListener(actionEvent -> {
            try {
                double[][] matrix = JTableUtils.readDoubleMatrixFromJTable(tableInput);
                double[][] inverse = MatrixInverter.inverseMatrix(matrix);
                // Записываем с округлением до 3 знаков после запятой
                JTableUtils.writeArrayToJTable(table2, inverse);
                // Обновляем размеры таблицы
                JTableUtils.resizeJTable(table2, inverse.length, inverse[0].length);
            } catch (ArithmeticException ex) {
                JOptionPane.showMessageDialog(null, "Ошибка: " + ex.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        multiplyMatrixes.addActionListener(actionEvent -> {
            try {
                // Считываем матрицы из tableInput и table2
                double[][] matrixA = JTableUtils.readDoubleMatrixFromJTable(tableInput);
                double[][] matrixB = JTableUtils.readDoubleMatrixFromJTable(table2);

                // Проверка на возможность умножения (кол-во столбцов A == кол-ву строк B)
                if (matrixA[0].length != matrixB.length) {
                    JOptionPane.showMessageDialog(this,
                            "Нельзя умножить матрицы: число столбцов первой матрицы не равно числу строк второй.",
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Умножение матриц
                double[][] result = multiply(matrixA, matrixB);

                // Вывод результата в table1 с округлением до 3 знаков
                JTableUtils.writeArrayToJTable(table1, result, "%.1f");
                JTableUtils.resizeJTable(table1, result.length, result[0].length);

            } catch (Exception ex) {
                SwingUtils.showErrorMessageBox(ex);
            }
        });
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelMain = new JPanel();
        panelMain.setLayout(new GridLayoutManager(6, 2, new Insets(10, 10, 10, 10), 10, 10));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setVerticalScrollBarPolicy(21);
        panelMain.add(scrollPane1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 200), null, 0, false));
        tableInput = new JTable();
        scrollPane1.setViewportView(tableInput);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.add(panel1, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonLoadInputFromFile = new JButton();
        buttonLoadInputFromFile.setText("Загрузить из файла");
        panel1.add(buttonLoadInputFromFile, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonRandomInput = new JButton();
        buttonRandomInput.setText("Заполнить случайными числами");
        panel1.add(buttonRandomInput, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonSaveInputInfoFile = new JButton();
        buttonSaveInputInfoFile.setText("Сохранить в файл");
        panel1.add(buttonSaveInputInfoFile, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, new Dimension(100, -1), null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        scrollPane2.setVerticalScrollBarPolicy(21);
        panelMain.add(scrollPane2, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 200), null, 0, false));
        tableOutput = new JTable();
        scrollPane2.setViewportView(tableOutput);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.add(panel2, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonFindDeterminant = new JButton();
        buttonFindDeterminant.setText("Найти определитель");
        panel2.add(buttonFindDeterminant, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel2.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.add(panel3, new GridConstraints(5, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        textArea1 = new JTextArea();
        panelMain.add(textArea1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }

}
