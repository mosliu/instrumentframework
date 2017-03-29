/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.instrument.device.Fluke45.utils;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileSystemView;
import net.labthink.instrument.device.*;
import net.labthink.utils.ExtensionFileFilter;
import net.labthink.utils.FilePlus;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.ui.Layer;
import org.jfree.ui.LengthAdjustmentType;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;

/**
 *
 * @author Moses
 */
public class Fluke45Chart extends javax.swing.JFrame {

//    TimeSeries getTimeSeries(1) = new TimeSeries("ppm_shanda", Second.class);
    ArrayList<TimeSeries> timeseriescopylist = new ArrayList<TimeSeries>();
//    TimeSeries timeSeries1_copy;
//    TimeSeries getTimeSeries(2) = new TimeSeries("ppm_dewpoint", Second.class);
//    TimeSeries timeSeries2_copy;
    SimpleDateFormat sdf_shanda = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
    SimpleDateFormat sdf_dewpoint = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
    TimeSeriesCollection trcollection;
    TimeSeries ts1;
    TimeSeries ts2;
//    XYSeries xyseries = new XYSeries("Random Data");
//    XYSeriesCollection xyseriesCollection1 = new XYSeriesCollection(xyseries);
    ChartPanel chartPanel;

    double k;
    double b;

    /**
     * Creates new form Draw
     */
    public Fluke45Chart() {
        initComponents();
        ts1 = new TimeSeries("O2 Sensor Voltage", Millisecond.class);
        ts2 = new TimeSeries("O2 Sensor Voltage -2 ", Millisecond.class);
        trcollection = new TimeSeriesCollection(ts1);
        trcollection.addSeries(ts2);
//        timeseriescopylist.add(getTimeSeries(3).createCopy(0, getTimeSeries(3).getItemCount() - 1));
        JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("曲线", "Time(s)", "Value", trcollection, true, true, false);

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
        valueaxis.setLabelFont(new Font("黑体", Font.BOLD, 14));

        XYLineAndShapeRenderer line0render = (XYLineAndShapeRenderer) xyplot.getRenderer(0);

        line0render.setSeriesPaint(3, Color.ORANGE);
        jfreechart.getTitle().setFont(new Font("黑体", Font.BOLD, 20));//设置标题字体
        jfreechart.getLegend().setItemFont(new Font("宋体", Font.ITALIC, 15));
        xyplot.getRenderer(0).setSeriesToolTipGenerator(0, new StandardXYToolTipGenerator("{1}, {2}mV",
                new SimpleDateFormat("MM-dd HH:mm:ss"),
                new DecimalFormat("0.00")));
        chartPanel = new ChartPanel(jfreechart);

        GetPointAction action = new GetPointAction();
        JMenuItem jmenuitem1 = new JMenuItem("氧气值");
        jmenuitem1.addActionListener(action);
        JMenu fx = new JMenu("分析导出");
        JMenuItem jmenuitem2 = new JMenuItem("选中起点");
        jmenuitem2.addActionListener(action);
        fx.add(jmenuitem2);
        JMenuItem jmenuitem3 = new JMenuItem("选中终点");
        jmenuitem3.addActionListener(action);
        fx.add(jmenuitem3);
        JMenuItem jmenuitem4 = new JMenuItem("输出");
        jmenuitem4.addActionListener(action);
        fx.add(jmenuitem4);
        JMenuItem jmenuitem5 = new JMenuItem("时间差");
        jmenuitem5.addActionListener(action);
        fx.add(jmenuitem5);
        JMenu bx = new JMenu("标线");
        JMenuItem jmenuitem6 = new JMenuItem("绘制起点终点");
        jmenuitem6.addActionListener(action);
        bx.add(jmenuitem6);
        JMenuItem jmenuitem8 = new JMenuItem("移除起点终点");
        jmenuitem8.addActionListener(action);
        bx.add(jmenuitem8);
        JMenuItem jmenuitem7 = new JMenuItem("绘制横标线");
        jmenuitem7.addActionListener(action);
        bx.add(jmenuitem7);
        JMenuItem jmenuitem9 = new JMenuItem("绘制纵标线");
        jmenuitem9.addActionListener(action);
        bx.add(jmenuitem9);
        JMenuItem jmenuitem10 = new JMenuItem("移除所有标线");
        jmenuitem10.addActionListener(action);
        bx.add(jmenuitem10);
        JMenuItem jmenuitem11 = new JMenuItem("特殊操作");
        jmenuitem11.addActionListener(action);
        bx.add(jmenuitem11);

        chartPanel.getPopupMenu().add(jmenuitem1);
//        chartPanel.getPopupMenu().add(jmenuitem2);
//        chartPanel.getPopupMenu().add(jmenuitem3);
//        chartPanel.getPopupMenu().add(jmenuitem4);
//        chartPanel.getPopupMenu().add(jmenuitem5);
        chartPanel.getPopupMenu().add(fx);
        chartPanel.getPopupMenu().add(bx);
//        chartPanel.getPopupMenu().add(jmenuitem2);
//        chartPanel.getPopupMenu().getPopupMenuListeners();
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
        jFormattedTextField_zero = new javax.swing.JFormattedTextField();
        jFormattedTextField_end = new javax.swing.JFormattedTextField();
        jLabel_zero = new javax.swing.JLabel();
        jLabel_end = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea_infos = new javax.swing.JTextArea();
        jButton_Open = new javax.swing.JButton();
        jButton_Open1 = new javax.swing.JButton();
        jCheckBox_AbsTime = new javax.swing.JCheckBox();
        jFormattedTextField_end2 = new javax.swing.JFormattedTextField();
        jFormattedTextField_zero2 = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jFormattedTextField_calc = new javax.swing.JFormattedTextField();
        jLabel_calc = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel_chart.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        javax.swing.GroupLayout jPanel_chartLayout = new javax.swing.GroupLayout(jPanel_chart);
        jPanel_chart.setLayout(jPanel_chartLayout);
        jPanel_chartLayout.setHorizontalGroup(
            jPanel_chartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1045, Short.MAX_VALUE)
        );
        jPanel_chartLayout.setVerticalGroup(
            jPanel_chartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 613, Short.MAX_VALUE)
        );

        jFormattedTextField_zero.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField_zero.setText("0.03");

        jFormattedTextField_end.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField_end.setText("9.6");

        jLabel_zero.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel_zero.setText("Zero");

        jLabel_end.setText("End");

        jTextArea_infos.setColumns(20);
        jTextArea_infos.setRows(5);
        jScrollPane1.setViewportView(jTextArea_infos);

        jButton_Open.setText("Open");
        jButton_Open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_OpenActionPerformed(evt);
            }
        });

        jButton_Open1.setText("Open2");
        jButton_Open1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_Open1ActionPerformed(evt);
            }
        });

        jCheckBox_AbsTime.setText("Abs.Time");

        jFormattedTextField_end2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField_end2.setText("20.90");

        jFormattedTextField_zero2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField_zero2.setText("0");

        jLabel1.setText("mv");

        jLabel2.setText("mv");

        jLabel3.setText("%");

        jLabel4.setText("%");

        jFormattedTextField_calc.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField_calc.setText("0");
        jFormattedTextField_calc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField_calcActionPerformed(evt);
            }
        });
        jFormattedTextField_calc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField_calcKeyReleased(evt);
            }
        });

        jLabel_calc.setText("0");

        javax.swing.GroupLayout DrawPanelLayout = new javax.swing.GroupLayout(DrawPanel);
        DrawPanel.setLayout(DrawPanelLayout);
        DrawPanelLayout.setHorizontalGroup(
            DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DrawPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel_chart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(DrawPanelLayout.createSequentialGroup()
                        .addGroup(DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(DrawPanelLayout.createSequentialGroup()
                                .addGroup(DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel_end, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel_zero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jFormattedTextField_zero, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                                    .addComponent(jFormattedTextField_end))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jFormattedTextField_zero2)
                                    .addComponent(jFormattedTextField_end2, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4)))
                            .addGroup(DrawPanelLayout.createSequentialGroup()
                                .addComponent(jButton_Open)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton_Open1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jCheckBox_AbsTime)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 731, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFormattedTextField_calc, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                            .addComponent(jLabel_calc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        DrawPanelLayout.setVerticalGroup(
            DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DrawPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel_chart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                    .addGroup(DrawPanelLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton_Open)
                            .addComponent(jButton_Open1)
                            .addComponent(jCheckBox_AbsTime))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addGroup(DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jFormattedTextField_zero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_zero)
                            .addComponent(jFormattedTextField_zero2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jFormattedTextField_end, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_end)
                            .addComponent(jFormattedTextField_end2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)))
                    .addGroup(DrawPanelLayout.createSequentialGroup()
                        .addComponent(jFormattedTextField_calc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel_calc)
                        .addGap(0, 0, Short.MAX_VALUE)))
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

    private void jButton_OpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_OpenActionPerformed
        openFiletoCurve(ts1);

    }//GEN-LAST:event_jButton_OpenActionPerformed

    public void openFiletoCurve(TimeSeries thists) throws HeadlessException, NumberFormatException {
        initkb();
        SimpleDateFormat sdfx = new SimpleDateFormat("[yyyy/MM/dd HH:mm:ss SSS]");

        ExtensionFileFilter filter = new ExtensionFileFilter("log,txt", true, true);
        filter.setDescription("命令文件");

        JFileChooser jfc = new JFileChooser();
        FileSystemView fsv = FileSystemView.getFileSystemView();
        //得到桌面路径
        jfc.setCurrentDirectory(fsv.getHomeDirectory());

        jfc.setDialogTitle("选择文件位置");
        jfc.setMultiSelectionEnabled(false);
        jfc.setDialogType(JFileChooser.OPEN_DIALOG);
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        jfc.setFileFilter(filter);
        int result = jfc.showOpenDialog(this);  // 打开"打开文件"对话框
        String content;
        if (result == JFileChooser.APPROVE_OPTION) {
            String filepath = jfc.getSelectedFile().getAbsolutePath();
            content = FilePlus.ReadTextFileToString(new File(filepath), "\r\n", "utf-8");
        } else {
            content = null;
        }
        if (content != null) {
            String[] sa = content.split("\r\n");
            thists.clear();
            thists.setNotify(false);
            int startno = 0;
            while (!sa[startno].startsWith("[")) {
                startno++;
            }
            try {

                Date starttime = sdfx.parse(sa[startno]);
                for (int i = startno; i < sa.length; i++) {
                    String s = sa[i];
                    if (s.startsWith("[")) {
//                    s = s.replaceAll("\\[", "");

//                    String[] sa2 = s.split("]");
                        Date time = sdfx.parse(s);
                        int position = s.replaceAll("E\\+", "E").lastIndexOf('+');
                        position = position == -1 ? s.lastIndexOf(']') + 2 : position;
//                        double d1 = Double.parseDouble(s.substring(position)) * 1000;
                        double d1 = Double.parseDouble(s.substring(position)) * 4; //ma信号
                        if (jCheckBox_AbsTime.isSelected()) {
                            time = new Date(time.getTime() - starttime.getTime());
                        }
                        thists.addOrUpdate(new Millisecond(time), d1);

                    }
                }
            } catch (ParseException ex) {
//                System.out.println(s);
                Logger.getLogger(UITester.class.getName()).log(Level.SEVERE, null, ex);
            }
            thists.setNotify(true);
        }
    }

    private void jButton_Open1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_Open1ActionPerformed
        openFiletoCurve(ts2);
    }//GEN-LAST:event_jButton_Open1ActionPerformed

    private void jFormattedTextField_calcKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField_calcKeyReleased
        initkb();

        String s = jFormattedTextField_calc.getText();
        try {
            double calc = Double.parseDouble(s);
            double calced = calcmv(calc);
            jLabel_calc.setText(String.format("%.2f %%", calced));
        } catch (Exception e) {
            ;
        }
    }//GEN-LAST:event_jFormattedTextField_calcKeyReleased

    private void jFormattedTextField_calcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextField_calcActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextField_calcActionPerformed

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
//                try {
                Fluke45Chart dr = new Fluke45Chart();
                dr.setVisible(true);
//                } catch (CloneNotSupportedException ex) {
//                    Logger.getLogger(Fluke45Chart.class.getName()).log(Level.SEVERE, null, ex);
//                }

            }
        });
    }

    private void initkb() {
        double zero = Double.parseDouble(jFormattedTextField_zero.getText());
        double zero2 = Double.parseDouble(jFormattedTextField_zero2.getText());
        double end = Double.parseDouble(jFormattedTextField_end.getText());
        double end2 = Double.parseDouble(jFormattedTextField_end2.getText());
        k = (end2 - zero2) / (end - zero);
        b = zero2 - k * zero;
//        log("k:" + k);
//        log("b:" + b);
//        log("0:"+calcmv(0));
//        log("10:"+calcmv(9.6));
//        log("20.9:"+calcmv(19.2));
    }

    private double calcmv(double mv) {
        return k * mv + b;
    }

    private void log(String str) {
        jTextArea_infos.append(str);
        jTextArea_infos.append("\r\n");
    }

    private void removeZeroEndmarkers() {
        XYPlot plot = (XYPlot) chartPanel.getChart().getPlot();
        Collection<Marker> c = plot.getRangeMarkers(Layer.FOREGROUND);
        List<Marker> l = new ArrayList<Marker>();
        for (Iterator<Marker> it = c.iterator(); it.hasNext();) {
            Marker marker = it.next();
            if (marker.getLabel().equals("Zero Point") || marker.getLabel().equals("End Point")) {
//                it.remove();
                l.add(marker);
            }
        }
        for (Iterator<Marker> it = l.iterator(); it.hasNext();) {
            Marker marker = it.next();
            plot.removeRangeMarker(marker);

        }
    }

    private void drawZeroEndmarkers() {
        XYPlot plot = (XYPlot) chartPanel.getChart().getPlot();
        double zerod = Double.parseDouble(jFormattedTextField_zero.getText());
        double endd = Double.parseDouble(jFormattedTextField_end.getText());

        final Marker start = new ValueMarker(zerod);
        start.setPaint(Color.MAGENTA);
        start.setLabel("Zero Point");
//        start.setLabel(s);
        start.setLabelAnchor(RectangleAnchor.TOP_LEFT);
        start.setLabelTextAnchor(TextAnchor.BOTTOM_LEFT);
        start.setLabelPaint(Color.yellow);
        plot.addRangeMarker(start);

        final Marker end = new ValueMarker(endd);
        end.setPaint(Color.MAGENTA);
        end.setLabel("End Point");
//        start.setLabel(s);
        end.setLabelAnchor(RectangleAnchor.BOTTOM_LEFT);
        end.setLabelTextAnchor(TextAnchor.TOP_LEFT);
        end.setLabelPaint(Color.yellow);
        plot.addRangeMarker(end);
    }

    private Date exportstarttime;
    private Date exportendtime;

    class GetPointAction implements ActionListener { //菜单项事件处理

        @Override
        public void actionPerformed(ActionEvent event) {

            String command = event.getActionCommand();
//            System.out.println(command);

//            int x = (int) chartPanel.getMousePosition().getX();
//            int y = (int) chartPanel.getMousePosition().getY();
            XYPlot plot = (XYPlot) chartPanel.getChart().getPlot();
            double XValue = plot.getDomainCrosshairValue();
            double YValue = plot.getRangeCrosshairValue();

            Date date = new Date((long) XValue);

            if (command.equals("氧气值")) {
//                jFormattedTextField_zero.setValue(YValue);
                log(String.format("选点：时间[%1$tY/%1$tm/%1$te %1$tH:%1$tM:%1$tS %1$tL],电压%2$f,氧浓度%3$f", date, YValue, calcmv(YValue)));
                log(String.format("%1$tH:%1$tM:%1$tS,%2$.2fmv(%3$.2f%%)", date, YValue, calcmv(YValue)));
//                log(String.format("选点：时间%s,电压%f,氧浓度%f",date, YValue,calcmv(YValue)));

            } else if (command.equals("选中起点")) {
                exportstarttime = date;
                log(String.format("选中起点：时间[%1$tY/%1$tm/%1$te %1$tH:%1$tM:%1$tS %1$tL],电压%2$f", date, YValue));
//                jFormattedTextField_end.setValue(date);
            } else if (command.equals("选中终点")) {
                log(String.format("选中终点：时间[%1$tY/%1$tm/%1$te %1$tH:%1$tM:%1$tS %1$tL],电压%2$f", date, YValue));
                exportendtime = date;

            } else if (command.equals("输出")) {
                if (exportstarttime == null || exportendtime == null) {
                    log("起点或终点未选中");
                    return;
                }
                StringBuilder sb1 = new StringBuilder();

                try {
                    List<TimeSeriesDataItem> l = ts1.createCopy(ts1.getIndex(new Millisecond(exportstarttime)), ts1.getIndex(new Millisecond(exportendtime))).getItems();
                    for (Iterator<TimeSeriesDataItem> it = l.iterator(); it.hasNext();) {
                        TimeSeriesDataItem item = it.next();
                        Date d = item.getPeriod().getStart();
                        double d1 = (double) item.getValue();
                        String s = String.format("+%.2fE-3", d1);
                        sb1.append(String.format("[%1$tY/%1$tm/%1$te %1$tH:%1$tM:%1$tS %1$tL] %2$s%n", d, s));
                    }
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(Fluke45Chart.class.getName()).log(Level.SEVERE, null, ex);
                }

                ExtensionFileFilter filter = new ExtensionFileFilter("log,txt", true, true);
                filter.setDescription("日志文件 ");
                JFileChooser jfc = new JFileChooser();
                FileSystemView fsv = FileSystemView.getFileSystemView();
                //得到桌面路径
                jfc.setCurrentDirectory(fsv.getHomeDirectory());
                jfc.setDialogTitle("选择保存位置");
                jfc.setMultiSelectionEnabled(false);
                jfc.setDialogType(JFileChooser.SAVE_DIALOG);
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                jfc.setFileFilter(filter);
                int result = jfc.showOpenDialog(null);  // 打开"打开文件"对话框
                if (result == JFileChooser.APPROVE_OPTION) {
                    String file = jfc.getSelectedFile().getAbsolutePath();
                    int dotindex = file.indexOf(".");
                    if (dotindex < 0) {
                        file = file + ".txt";
                    }
                    try {
                        FileWriter fw = new FileWriter(file, true);
                        //                FileWriter fw = new FileWriter(jfc.getSelectedFile(), true);
                        fw.append(sb1.toString().replaceAll("\\+-", "-"));
                        fw.close();
                    } catch (IOException ex) {
                        Logger.getLogger(UITester.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

//                double a = (double) ts1.getDataItem(new Millisecond(exportstarttime)).getValue();
//                jTextArea_infos.append("a:" + a);
//                plot.addRangeMarker(marker);
//                plot.addDomainMarker(marker);
            } else if (command.equals("时间差")) {
                long diff = exportstarttime.getTime() - exportendtime.getTime();
                log(String.format("时间差:%.3f s", Math.abs(diff / 1000f)));

            } else if (command.equals("绘制起点终点")) {
                drawZeroEndmarkers();
            } else if (command.equals("移除起点终点")) {
                removeZeroEndmarkers();
            } else if (command.equals("绘制横标线")) {
                plot.getRangeMarkers(Layer.FOREGROUND);
                String s = JOptionPane.showInputDialog("请输入标线含义");
                final Marker start = new ValueMarker(YValue);
                start.setPaint(Color.GREEN);
                start.setLabel(String.format("%s,%.4f", s, YValue));
                start.setLabelAnchor(RectangleAnchor.BOTTOM_LEFT);
                start.setLabelTextAnchor(TextAnchor.TOP_LEFT);
                start.setLabelPaint(Color.red);
                plot.addRangeMarker(start);
            } else if (command.equals("绘制纵标线")) {
                plot.getRangeMarkers(Layer.FOREGROUND);
                String s = JOptionPane.showInputDialog("请输入标线含义");
                final Marker start = new ValueMarker(XValue);
                start.setPaint(Color.GREEN);
                start.setLabel(String.format("%s", s, XValue));
                start.setLabelAnchor(RectangleAnchor.TOP_LEFT);
                start.setLabelTextAnchor(TextAnchor.BOTTOM_RIGHT);
                start.setLabelPaint(Color.red);
                plot.addDomainMarker(start);
            } else if (command.equals("移除所有标线")) {
                int k = JOptionPane.showConfirmDialog(null, "是否清除所有标线？");
                if (k == JOptionPane.YES_OPTION) {
                    plot.clearDomainMarkers();
                    plot.clearRangeMarkers();
                }
            } else if (command.equals("特殊操作")) {

                int starti = 0;//起点
                int rangestart = starti;
                int rangeend = starti;
                int endi = ts1.getItemCount() - 1;
                TimeSeriesDataItem item = ts1.getDataItem(0);
//                log(String.format("[%1$tm/%1$te %1$tH:%1$tM:%1$tS] 开始试验，开始时值为%2$.4fmA", item.getPeriod().getStart(), item.getValue()));
                log(String.format("[%1$tm/%1$te %1$tH:%1$tM:%1$tS],开始试验,%2$.4fmA", item.getPeriod().getStart(), item.getValue()));

                int count = 1;
                for (int i = starti + 5 * 60 * 60; i < endi; i = i + 5 * 60 * 60) {//一秒5个数
                    item = ts1.getDataItem(i);
//                    log(String.format("[%1$tm/%1$te %1$tH:%1$tM:%1$tS] %2$d小时后结果为%3$.4fmA", item.getPeriod().getStart(), count, item.getValue()));
                    rangeend = i;
                    try {
                        TimeSeries tempts = ts1.createCopy(rangestart, rangeend);
                        log(String.format("[%1$tm/%1$te %1$tH:%1$tM:%1$tS],%2$d小时后,%3$.4fmA,%4$.4fmA,%5$.4fmA,%6$.4fmA", item.getPeriod().getStart(), count, item.getValue(), tempts.getMaxY(), tempts.getMinY(), tempts.getMaxY() - tempts.getMinY()));
//                        log(String.format("该时段最大值为：%.4fmA，最小值为：%.4fmA,波动为：%.4fmA", tempts.getMaxY(), tempts.getMinY(), tempts.getMaxY() - tempts.getMinY()));
                    } catch (CloneNotSupportedException ex) {
                        Logger.getLogger(Fluke45Chart.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    rangestart = i;
                    count++;
                }
                item = ts1.getDataItem(endi);
//                log(String.format("[%1$tm/%1$te %1$tH:%1$tM:%1$tS] 结束试验，结束时值为%2$.4fmA", item.getPeriod().getStart(), item.getValue()));
                rangeend = endi;
                try {
                    TimeSeries tempts = ts1.createCopy(rangestart, rangeend);
                    log(String.format("[%1$tm/%1$te %1$tH:%1$tM:%1$tS],结束试验,%2$.4fmA,%3$.4fmA,%4$.4fmA,%5$.4fmA",item.getPeriod().getStart(), item.getValue(), tempts.getMaxY(), tempts.getMinY(), tempts.getMaxY() - tempts.getMinY()));
//                    log(String.format("该时段最大值为：%.4fmA，最小值为：%.4fmA,波动为：%.4fmA", tempts.getMaxY(), tempts.getMinY(), tempts.getMaxY() - tempts.getMinY()));
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(Fluke45Chart.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
            }

//            System.out.println(chartPanel.getMousePosition().getX());
//            System.out.println(chartPanel.getMousePosition().getY());
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel DrawPanel;
    private javax.swing.JTabbedPane MainPane;
    private javax.swing.JButton jButton_Open;
    private javax.swing.JButton jButton_Open1;
    private javax.swing.JCheckBox jCheckBox_AbsTime;
    private javax.swing.JFormattedTextField jFormattedTextField_calc;
    private javax.swing.JFormattedTextField jFormattedTextField_end;
    private javax.swing.JFormattedTextField jFormattedTextField_end2;
    private javax.swing.JFormattedTextField jFormattedTextField_zero;
    private javax.swing.JFormattedTextField jFormattedTextField_zero2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel_calc;
    private javax.swing.JLabel jLabel_end;
    private javax.swing.JLabel jLabel_zero;
    private javax.swing.JPanel jPanel_chart;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea_infos;
    // End of variables declaration//GEN-END:variables
}
