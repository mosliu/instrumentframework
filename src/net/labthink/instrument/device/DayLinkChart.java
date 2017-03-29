package net.labthink.instrument.device;


import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class DayLinkChart {

    Date dt;
    long Stcd;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.getContentPane().add(new DayLinkChart().createDemoPanel());
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private static CategoryDataset createDataset() {
        String s = "First";
        String s1 = "Second";
        String s2 = "Category 1";
// 生成defaultcategorydataset数据源对象

        DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
// 向该defaultcategorydataset数据源对象添加数据

        defaultcategorydataset.addValue(1.0D, s, s2);
        defaultcategorydataset.addValue(5D, s1, s2);
        return defaultcategorydataset;
    }

    private static JFreeChart createChart(CategoryDataset categorydataset) {
        JFreeChart jfreechart = ChartFactory.createBarChart("图形标题", "横坐标", "纵坐标", categorydataset/* 数据源 */, PlotOrientation.VERTICAL/* 方向 */, true, true, false);
        jfreechart.setBackgroundPaint(new Color(0xbbbbdd));// 设置背景色

        CategoryPlot categoryplot = jfreechart.getCategoryPlot();
// 得到图形以便精细设置

        NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
// 取得该类图形的范围数字轴,指纵坐标

        numberaxis.setTickLabelFont(new Font("黑体", Font.ITALIC, 18));
// 设置纵坐标的字体,风格，大小

        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
// 设置纵坐标以标准整形为单位

        BarRenderer barrenderer = (BarRenderer) categoryplot.getRenderer();
// barrenderer表示得到所有的柱形集合对象

        barrenderer.setDrawBarOutline(false);// 不显示柱形的外边框

        barrenderer.setMaximumBarWidth(2D);// 设置每个图形的最大宽度

        GradientPaint gradientpaint = new GradientPaint(0.0F, 0.0F, Color.red, 0.0F, 0.0F, Color.black);// 从上到下渐变的颜色

        GradientPaint gradientpaint1 = new GradientPaint(0.0F, 0.0F, Color.green, 0.0F, 0.0F, Color.red);// 从上到下渐变的颜色

        barrenderer.setSeriesPaint(0, gradientpaint);// 第一个柱形

        barrenderer.setSeriesPaint(1, gradientpaint1);// 第二个柱形

        return jfreechart;
    }

    /**
     * 返回一个面板
     *
     * @return JPanel
     */
    public static JPanel createDemoPanel() {
// 生成jfreechart对象

        JFreeChart jfreechart = createChart(createDataset());
        return new ChartPanel(jfreechart);
    }
}
