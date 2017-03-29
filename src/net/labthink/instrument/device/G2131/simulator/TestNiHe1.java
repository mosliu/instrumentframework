/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.instrument.device.G2131.simulator;

/**
 *
 * @author Moses
 */
public class TestNiHe1 {

    /**
     * <p>函数功能：最小二乘法曲线拟合</p>
     * <p>方程:Y = a(0) + a(1) * (X - X1)+ a(2) * (X - X1)^2 + ..... .+ a(m) * (X - X1)^m X1为X的平均数</p>
     * @param x 实型一维数组,长度为 n. 存放给定 n 个数据点的　X　坐标
     * @param y 实型一维数组,长度为 n.存放给定 n 个数据点的　Y　坐标
     * @param n 变量。给定数据点的个数
     * @param a 实型一维数组，长度为 m.返回 m-1　次拟合多项式的 m 个系数
     * @param m 拟合多项式的项数，即拟合多项式的最高次数为 m-1.
     *          要求 m<=n 且m<=20。若 m>n 或 m>20 ，则本函数自动按 m=min{n,20} 处理.
     * <p>Date:2007-12-25 16:21 PM</p>
     * @author qingbao-gao
     * @return  多项式系数存储数组
     */
    public static double[] PolyFit(double x[], double y[], int n, double a[], int m) {
        int i, j, k;
        double z, p, c, g, q = 0, d1, d2;
        double[] s = new double[20];
        double[] t = new double[20];
        double[] b = new double[20];
        double[] dt = new double[3];
        for (i = 0; i <= m - 1; i++) {
            a[i] = 0.0;
        }
        if (m > n) {
            m = n;
        }
        if (m > 20) {
            m = 20;
        }
        z = 0.0;
        for (i = 0; i <= n - 1; i++) {
            z = z + x[i] / (1.0 * n);
        }
        b[0] = 1.0;
        d1 = 1.0 * n;
        p = 0.0;
        c = 0.0;
        for (i = 0; i <= n - 1; i++) {
            p = p + (x[i] - z);
            c = c + y[i];
        }
        c = c / d1;
        p = p / d1;
        a[0] = c * b[0];
        if (m > 1) {
            t[1] = 1.0;
            t[0] = -p;
            d2 = 0.0;
            c = 0.0;
            g = 0.0;
            for (i = 0; i <= n - 1; i++) {
                q = x[i] - z - p;
                d2 = d2 + q * q;
                c = c + y[i] * q;
                g = g + (x[i] - z) * q * q;
            }
            c = c / d2;
            p = g / d2;
            q = d2 / d1;
            d1 = d2;
            a[1] = c * t[1];
            a[0] = c * t[0] + a[0];
        }
        for (j = 2; j <= m - 1; j++) {
            s[j] = t[j - 1];
            s[j - 1] = -p * t[j - 1] + t[j - 2];
            if (j >= 3) {
                for (k = j - 2; k >= 1; k--) {
                    s[k] = -p * t[k] + t[k - 1] - q * b[k];
                }
            }
            s[0] = -p * t[0] - q * b[0];
            d2 = 0.0;
            c = 0.0;
            g = 0.0;
            for (i = 0; i <= n - 1; i++) {
                q = s[j];
                for (k = j - 1; k >= 0; k--) {
                    q = q * (x[i] - z) + s[k];
                }
                d2 = d2 + q * q;
                c = c + y[i] * q;
                g = g + (x[i] - z) * q * q;
            }
            c = c / d2;
            p = g / d2;
            q = d2 / d1;
            d1 = d2;
            a[j] = c * s[j];
            t[j] = s[j];
            for (k = j - 1; k >= 0; k--) {
                a[k] = c * s[k] + a[k];
                b[k] = t[k];
                t[k] = s[k];
            }
        }
        dt[0] = 0.0;
        dt[1] = 0.0;
        dt[2] = 0.0;
        for (i = 0; i <= n - 1; i++) {
            q = a[m - 1];
            for (k = m - 2; k >= 0; k--) {
                q = a[k] + q * (x[i] - z);
            }
            p = q - y[i];
            if (Math.abs(p) > dt[2]) {
                dt[2] = Math.abs(p);
            }
            dt[0] = dt[0] + p * p;
            dt[1] = dt[1] + Math.abs(p);
        }
        return a;
    }// end

    /**
     * <p>对X轴数据节点球平均值</p>
     * @param x 存储X轴节点的数组
     * <p>Date:2007-12-25 20:21 PM</p>
     * @author qingbao-gao
     * @return  平均值
     */
    public static double average(double[] x) {
        double ave = 0;
        double sum = 0;
        if (x != null) {
            for (int i = 0; i < x.length; i++) {
                sum += x[i];
            }
            ave = sum / x.length;
        }
        return ave;
    }

    /**
     * <p>由X值获得Y值</p>
     * @param x  当前X轴输入值,即为预测的月份
     * @param xx 当前X轴输入值的前X数据点
     * @param a  存储多项式系数的数组
     * @param m  存储多项式的最高次数的数组
     * <p>Date:2007-12-25 PM 20:07</p>
     * @author 高清宝
     * @return  对应X轴节点值的Y轴值
     */
    public static double getY(double x, double[] xx, double[] a, int m) {
        double y = 0;
        double ave = average(xx);

        double l = 0;
        for (int i = 0; i < m; i++) {
            l = a[0];
            if (i > 0) {
                y += a[i] * Math.pow((x - ave), i);
            }
        }
        return (y + l);
    }

    public static void main(String[] args)   {
        //double []x={0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,13};
        //double []y={51,52,53,54,55,56,57,58,59,60,61,62,63,64};//,14,65
        double[] test_x = {200601, 200602, 200603, 200604, 200605, 200606, 200607, 200608, 200609, 200610, 200611, 200612, 200701, 200702};//200703
        double[] test_x_x = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};// 14
        double[] test_xx = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};// 15
        double[] test_y = {63534, 64624, 64947, 64996, 65201, 65318, 65800, 66126, 66238, 66553, 66790, 67125, 67461, 67833};//68064
        double []x1={0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,13};
        double []y1={0,1,4,9,16,25,36,49,64,81,100,121,144,169};//,14,65
        int m =3;
        double[] a = new double[test_xx.length];
        double[] aa = PolyFit(x1, y1, 14, a, m);

        for (int i = 0; i < aa.length; i++) {
            double d = aa[i];
            System.out.println("拟合-->："+d);
        }
//        double yy = 0;
//        System.out.println("拟合-->" + getY(15, test_xx, aa, m));
    }
}
