/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.instrument.device;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Moses
 */
public class DrawChart extends javax.swing.JFrame {

//    TimeSeries getTimeSeries(1) = new TimeSeries("ppm_shanda", Second.class);
//    ArrayList<TimeSeries> timeseriescopylist = new ArrayList<TimeSeries>();
//    TimeSeries timeSeries1_copy;
//    TimeSeries getTimeSeries(2) = new TimeSeries("ppm_dewpoint", Second.class);
//    TimeSeries timeSeries2_copy;
    SimpleDateFormat sdf_shanda = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
    SimpleDateFormat sdf_dewpoint = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
    //TimeSeriesCollection timeseriescollection;
    XYSeries xyseries = new XYSeries("Random Data");
    XYSeriesCollection xyseriesCollection1 = new XYSeriesCollection(xyseries);
    ChartPanel chartPanel;

    /**
     * Creates new form Draw
     */
    public DrawChart() throws CloneNotSupportedException {
        initComponents();

//        timeseriescopylist.add(getTimeSeries(3).createCopy(0, getTimeSeries(3).getItemCount() - 1));
        JFreeChart jfreechart = ChartFactory.createXYLineChart("OX2/231模拟曲线", "Time", "PPM", xyseriesCollection1, PlotOrientation.VERTICAL, true, true, false);

        XYPlot xyplot = jfreechart.getXYPlot();
        xyplot.setDomainCrosshairVisible(true);
        xyplot.setRangeCrosshairVisible(true);
        //纵坐标设定
        ValueAxis valueaxis = xyplot.getDomainAxis();
        //水平底部列表   
        valueaxis.setLabelFont(new Font("黑体", Font.BOLD, 14));
        //水平底部标题
        valueaxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12));

        //自动设置数据轴数据范围
        valueaxis.setAutoRange(true);
        //数据轴固定数据范围 7days
//                valueaxis.setFixedAutoRange(604800000D);
        valueaxis = xyplot.getRangeAxis();
        valueaxis.setAutoRange(false);
        valueaxis.setRange(0, 1);
        valueaxis.setLabelFont(new Font("黑体", Font.BOLD, 14));
//        valueaxis.setAutoRangeMinimumSize(1d);
//        valueaxis.setLowerBound(0);

        XYLineAndShapeRenderer line0render = (XYLineAndShapeRenderer) xyplot.getRenderer(0);

        line0render.setSeriesPaint(3, Color.ORANGE);

//                xyplot.setDataset(1, timeseriescollection);
//                xyplot.getRendererCount();
//                xyplot.getRenderer(0).setSeriesPaint(0, Color.blue);
//                NumberAxis numberaxis = new NumberAxis("ppm");
//                numberaxis.setAutoRangeIncludesZero(false);
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.GREEN);
        renderer.setBaseShapesFilled(false);
        renderer.setBaseShapesVisible(false);
        XYToolTipGenerator tp = renderer.getSeriesToolTipGenerator(0);
//                renderer.setUseFillPaint(false);
//                StandardXYToolTipGenerator tg = new StandardXYToolTipGenerator();

//                xyplot.setRenderer(1, renderer);
//                xyplot.setRangeAxis(1, numberaxis);
        xyplot.setRangeGridlinePaint(Color.BLACK); //纵坐标格线颜色
        xyplot.setDomainGridlinePaint(Color.BLACK); //横坐标格线颜色
        xyplot.setBackgroundPaint(Color.WHITE);
//                xyplot.mapDatasetToRangeAxis(1, 1);
//
//        xyplot.getRenderer(0).setSeriesToolTipGenerator(0, new StandardXYToolTipGenerator("ppm:({1}, {2})",
//                new SimpleDateFormat("MM-dd HH:mm:ss"),
//                new DecimalFormat("0.0")));
////                xyplot.getRenderer(1).setSeriesToolTipGenerator(0, new StandardXYToolTipGenerator("ppm;({1}, {2})",
////                        new SimpleDateFormat("MM-dd HH:mm:ss"),
////                        new DecimalFormat("0.000")));

        jfreechart.getTitle().setFont(new Font("黑体", Font.BOLD, 20));//设置标题字体
        jfreechart.getLegend().setItemFont(new Font("宋体", Font.ITALIC, 15));
        chartPanel = new ChartPanel(jfreechart);
        JMenuItem jmenuitem1 = new JMenuItem("起点");
        GetPointAction action = new GetPointAction();
        //jmenuitem1.addActionListener(action);
        JMenuItem jmenuitem2 = new JMenuItem("终点");
        //jmenuitem2.addActionListener(action);
        chartPanel.getPopupMenu().add(jmenuitem1);
        chartPanel.getPopupMenu().add(jmenuitem2);
        chartPanel.getPopupMenu().getPopupMenuListeners();
        chartPanel.setSize(950, 620);
        chartPanel.setPreferredSize(new Dimension(600, 502));
        jPanel_chart.add(chartPanel, BorderLayout.CENTER);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MainPane = new javax.swing.JTabbedPane();
        DrawPanel = new javax.swing.JPanel();
        jPanel_chart = new javax.swing.JPanel();
        jFormattedTextField_initvalue = new javax.swing.JFormattedTextField();
        jFormattedTextField_amplitude = new javax.swing.JFormattedTextField();
        jLabel_starttime = new javax.swing.JLabel();
        jLabel_endtime = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea_infos = new javax.swing.JTextArea();
        jButton_DrawXyPlot = new javax.swing.JButton();
        jButton_sinx = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel_chart.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        javax.swing.GroupLayout jPanel_chartLayout = new javax.swing.GroupLayout(jPanel_chart);
        jPanel_chart.setLayout(jPanel_chartLayout);
        jPanel_chartLayout.setHorizontalGroup(
            jPanel_chartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel_chartLayout.setVerticalGroup(
            jPanel_chartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 613, Short.MAX_VALUE)
        );

        jFormattedTextField_initvalue.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField_initvalue.setText("-0.4");

        jFormattedTextField_amplitude.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.000"))));
        jFormattedTextField_amplitude.setText("0.01");

        jLabel_starttime.setText("初始");

        jLabel_endtime.setText("幅度");

        jTextArea_infos.setColumns(20);
        jTextArea_infos.setRows(5);
        jScrollPane1.setViewportView(jTextArea_infos);

        jButton_DrawXyPlot.setText("随机数");
        jButton_DrawXyPlot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_DrawXyPlotActionPerformed(evt);
            }
        });

        jButton_sinx.setText("正弦");
        jButton_sinx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_sinxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout DrawPanelLayout = new javax.swing.GroupLayout(DrawPanel);
        DrawPanel.setLayout(DrawPanelLayout);
        DrawPanelLayout.setHorizontalGroup(
            DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DrawPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel_chart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(DrawPanelLayout.createSequentialGroup()
                        .addGroup(DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(DrawPanelLayout.createSequentialGroup()
                                .addComponent(jButton_DrawXyPlot)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton_sinx))
                            .addGroup(DrawPanelLayout.createSequentialGroup()
                                .addGroup(DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel_endtime)
                                    .addComponent(jLabel_starttime))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jFormattedTextField_initvalue)
                                    .addComponent(jFormattedTextField_amplitude))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 907, Short.MAX_VALUE)))
                .addContainerGap())
        );
        DrawPanelLayout.setVerticalGroup(
            DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DrawPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel_chart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                    .addGroup(DrawPanelLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton_DrawXyPlot)
                            .addComponent(jButton_sinx))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jFormattedTextField_initvalue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_starttime))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jFormattedTextField_amplitude, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_endtime))))
                .addContainerGap())
        );

        MainPane.addTab("tab1", DrawPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(MainPane)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(MainPane)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_DrawXyPlotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_DrawXyPlotActionPerformed
        xyseries.clear();

        xyseries.setNotify(false);
        Random r = new Random();
        int k = 0;
        for (int j = 0; j < 50; j++) {
            int tempi = r.nextInt(7) * 10;
            System.out.println(tempi);
            int k2 = k;
            int tempi1 = r.nextInt(7);//0-6
            tempi1++;//1-7
            for (int i = k2; i < k2 + tempi; i++) {
//                System.out.print(tempi1+",");
                double tempd1 = (double) tempi1 / 100;
//                System.out.println(tempd1);
                xyseries.addOrUpdate(k, tempd1);
                k++;
            }

        }
        xyseries.setNotify(true);


    }//GEN-LAST:event_jButton_DrawXyPlotActionPerformed

    private void jButton_sinxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_sinxActionPerformed

        xyseries.clear();

        xyseries.setNotify(false);

        String amplitudeStr = jFormattedTextField_amplitude.getText();
        int amplitudei = 100;//振幅
        try {
            double tempd = Double.parseDouble(amplitudeStr);
            amplitudei = (int) (tempd * 1000);
        } catch (Exception e) {
        }
        String initvalueStr = jFormattedTextField_initvalue.getText();
        double initd = -0.4;//初始值 ppm
        try {
            initd = Double.parseDouble(initvalueStr);
        } catch (Exception e) {
        }
        Random r = new Random();
        double drawd = initd;//绘图值 ppm
        double t = 0;
        for (int i = 0; i < 43200; i++) {//一天的点数
            double tmpd = r.nextInt(amplitudei * 2 + 1) - amplitudei;//随机一个-0.1到0.1以内的数
//            System.out.println(tmpd/100);
            t += tmpd / 1000;
            drawd = drawd + tmpd / 1000;
            double value = 0;
//            if(drawd <=0){
//                value = (Math.sin(drawd) + 1.5) * 0.02;
//            }else{
//                value = drawd;
//            }
            value = (Math.sin(drawd * 10) + 1.5) * 0.02;
            xyseries.addOrUpdate(i, value);
        }
        System.out.println("t:" + t);
        xyseries.setNotify(true);


    }//GEN-LAST:event_jButton_sinxActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UITester.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(UITester.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(UITester.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(UITester.class.getName()).log(Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
//        try {
//            Date d = sdf_shanda.parse("2012:10:24:16:54:02");
////            System.out.println(d);
////            Second sec = new Second(d);
//        } catch (ParseException ex) {
//            Logger.getLogger(Draw.class.getName()).log(Level.SEVERE, null, ex);
//        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    DrawChart dr = new DrawChart();
                    dr.setVisible(true);
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(DrawChart.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
    }

    class GetPointAction implements ActionListener { //菜单项事件处理

        @Override
        public void actionPerformed(ActionEvent event) {

            String command = event.getActionCommand();
            System.out.println(command);

//            int x = (int) chartPanel.getMousePosition().getX();
//            int y = (int) chartPanel.getMousePosition().getY();
            XYPlot plot = (XYPlot) chartPanel.getChart().getPlot();

            double crosshairXValue = plot.getDomainCrosshairValue();
            if (crosshairXValue == 0) {
                return;
            }
            Date date = new Date((long) crosshairXValue);
//            Second sec = new Second(date);
//            TimeSeriesDataItem dataitem = getTimeSeries(1).getDataItem(sec);
//            System.out.println(sec);
//            System.out.println(dataitem.getValue());

            if (command.equals("起点")) {
                jFormattedTextField_initvalue.setValue(date);
            } else if (command.equals("终点")) {
                jFormattedTextField_amplitude.setValue(date);
            } else {
            }

//            System.out.println(chartPanel.getMousePosition().getX());
//            System.out.println(chartPanel.getMousePosition().getY());
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel DrawPanel;
    private javax.swing.JTabbedPane MainPane;
    private javax.swing.JButton jButton_DrawXyPlot;
    private javax.swing.JButton jButton_sinx;
    private javax.swing.JFormattedTextField jFormattedTextField_amplitude;
    private javax.swing.JFormattedTextField jFormattedTextField_initvalue;
    private javax.swing.JLabel jLabel_endtime;
    private javax.swing.JLabel jLabel_starttime;
    private javax.swing.JPanel jPanel_chart;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea_infos;
    // End of variables declaration//GEN-END:variables
}
