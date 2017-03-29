/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.utils;

import java.util.Random;

/**
 * 维护一个最大值最小值，可以获得中间随机值的类。
 * @author Mosliu
 */
public class Randomer extends Random {

    private long max = Long.MAX_VALUE;
    private long min = Long.MIN_VALUE;

    public Randomer() {
    }

    public Randomer(long _max, long _min) {
        max = _max > _min ? _max : _min;
        min = _max > _min ? _min : _max;
    }

    public void resetInt() {
        max = Integer.MAX_VALUE-1;
        min = 0;
    }

    public void resetLong() {
        max = Long.MAX_VALUE-1;
        min = 0;
    }

    @Override
    public int nextInt() {
        int x = super.nextInt((int) max - (int) min + 1);
        return x + (int) min;

    }

    @Override
    public int nextInt(int n) {
        int _max = (int) max > n ? n : (int) max;
        int x = super.nextInt(_max - (int) min + 1);
        return x + (int) min;
    }

    /**
     * 返回一个范围区间内的随机Int
     * 输入的两个数会更新该类的范围设置。（含所设定的最大值）
     * @param a
     * @param b
     * @return
     */
    public int nextInt(int a, int b) {
        setRange(a, b);
        return nextInt();
    }

    public void setRange(long a, long b) {
        max = a > b ? a : b;
        min = a > b ? b : a;
    }

    /**
     * @return the max
     */
    public long getMax() {
        return max;
    }

    /**
     * @param aMax the max to set
     */
    public void setMax(long aMax) {
        max = aMax;
    }

    /**
     * @return the min
     */
    public long getMin() {
        return min;
    }

    /**
     * @param aMin the min to set
     */
    public void setMin(long aMin) {
        min = aMin;
    }
}
