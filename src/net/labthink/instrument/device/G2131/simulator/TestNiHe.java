/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.instrument.device.G2131.simulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Moses
 */
public class TestNiHe {

    private static void ols(double[] x, double[] y, int n, double b1, double c1) {

        int m = x.length;

        double[][] A = new double[n][n];

        double[] b = new double[n];

        double[] store = new double[2 * n];

        for (int i = 0; i < 2 * n; i++) {

            double sum = 0.0D;

            for (int j = 0; j < m; j++) {
                sum += Math.pow(x[j], i);
            }

            store[i] = sum;

        }

        for (int i = 0; i < n; i++) {

            int col = i;

            for (int j = 0; j < n; j++) {

                A[i][j] = store[col];

                col++;

            }

        }

        for (int i = 0; i < n; i++) {

            double sum = 0;

            for (int j = 0; j < m; j++) {
                sum += Math.pow(x[j], i) * y[j];
            }

            b[i] = sum;

        }

        keyWords(A, b);

        gauss(A, b);

        equations(A, b, b1, c1);

    }

    private static void gauss(double[][] A, double[] b) {

        int n = A.length;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {

                double m = A[j][i] / A[i][i];

                for (int k = i; k < n; k++) {
                    A[j][k] -= A[i][k] * m;
                }

                b[j] -= b[i] * m;

            }
        }

    }

    private static void keyWords(double[][] A, double[] b) {

        int n = A.length;

        for (int i = 0; i < n; i++) {

            int max = i;

            for (int j = i + 1; j < n; j++) {

                if (Math.abs(A[max][i]) < Math.abs(A[j][i])) {
                    max = j;
                }

                for (int k = 0; k < n; k++) {

                    double temp = A[max][k];

                    A[max][k] = A[i][k];

                    A[i][k] = temp;

                }

                double temp = b[i];

                b[i] = b[max];

                b[max] = temp;

            }

        }

    }

    private static void equations(double[][] A, double[] b, double b1, double c1) {

        int n = A.length;

        double[] a = new double[n];

        a[n - 1] = b[n - 1] / A[n - 1][n - 1];

        for (int i = n - 2; i >= 0; i--) {

            double temp = 0;

            for (int j = i + 1; j < n; j++) {
                temp += A[i][j] * a[j];
            }

            a[i] = (b[i] - temp) / A[i][i];

        }

        outPut(a);

        min(b1, c1, a);

    }

    private static void outPut(double[] a) {

        int n = a.length - 1;

        System.out.println("最小二乘法求多项式拟合曲线");

        System.out.print("y=");

        for (int i = n; i >= 0; i--) {

            if (i >= 2) {
                if (a[i - 1] > 0) {
                    System.out.print(a[i] + "x^" + i + "+");
                } else if (a[i - 1] < 0) {
                    System.out.print(a[i] + "x^" + i);
                }
                continue;
            }

            if (i == 1) {
                if (a[0] > 0) {
                    System.out.print(a[i] + "x" + "+");
                    continue;
                }
            }

            if (a[0] < 0) {
                System.out.print(a[i] + "x");
                continue;
            }


            if (i == 0) {
                System.out.print(a[i]);
                continue;
            }

        }

    }

    private static double y(double x, double[] a) {

        int n = a.length - 1;

        double sum = 0;

        for (int i = n; i >= 0; i--) {
            sum += a[i] * Math.pow(x, i);
        }

        return sum;

    }

    private static void min(double b1, double c1, double[] a) {

        final int n1 = 100000;

        double m = (c1 - b1) / n1;

        double min1 = y(b1, a);

        double x, xx = 0;

        for (int i = 1; i <= n1; i++) {

            x = b1 + i * (c1 - b1) / n1;

            if (min1 > y(x, a)) {

                xx = x;

                min1 = y(x, a);

            }

        }

        System.out.println();

        System.out.print("最低点的位置" + "\n(x,y)=(" + xx + "," + min1 + ")");

    }

    public static void main(String[] args) {
//
//        double[] x = {0, 696, 1044, 1350, 1559, 1698, 1879, 1920, 2046, 2173, 2282, 2573, 2894};
//
//        double[] y = {0, 1738, 5056, 7741, 9637, 11533, 13903, 15483, 16589, 17536, 19590, 22434, 26384};
//
//        int n = 3;
//
//        double b1 = 1, c1 = 10;
//
//        ols(x, y, n, b1, c1);
    main1();
    }

    public static void main1() {
        String path = "d:/asd.txt";
        File file = new File(path);
//		System.out.println(file.getAbsolutePath());
//		System.out.println(file.getCanonicalPath());
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            // System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                tempString = (tempString.subSequence(0, tempString.length() - 12)).toString();
                sb.append(tempString);
                sb.append("\r\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        System.out.println(sb.toString());
    }
}
