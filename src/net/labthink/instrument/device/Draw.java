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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import net.labthink.utils.BytePlus;
import net.labthink.utils.ExtensionFileFilter;
import net.labthink.utils.FilePlus;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;

/**
 *
 * @author Moses
 */
public class Draw extends javax.swing.JFrame {

//    TimeSeries getTimeSeries(1) = new TimeSeries("ppm_shanda", Second.class);
    ArrayList<TimeSeries> timeseriescopylist = new ArrayList<TimeSeries>();
//    TimeSeries timeSeries1_copy;
//    TimeSeries getTimeSeries(2) = new TimeSeries("ppm_dewpoint", Second.class);
//    TimeSeries timeSeries2_copy;
    SimpleDateFormat sdf_shanda = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
    SimpleDateFormat sdf_dewpoint = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
    TimeSeriesCollection timeseriescollection;
    ChartPanel chartPanel;

    /**
     * Creates new form Draw
     */
    public Draw() throws CloneNotSupportedException {
        initComponents();
        timeseriescollection = new TimeSeriesCollection(new TimeSeries("ppm_shanda", Second.class));
        timeseriescopylist.add(getTimeSeries(1));
        timeseriescollection.addSeries(new TimeSeries("ppm_dewpoint", Second.class));
        timeseriescopylist.add(getTimeSeries(2));
        timeseriescollection.addSeries(new TimeSeries("ppm_Amtek", Second.class));
        timeseriescopylist.add(getTimeSeries(3));
        timeseriescollection.addSeries(new TimeSeries("o2", Second.class));
        timeseriescopylist.add(getTimeSeries(4));
        
//        timeseriescopylist.add(getTimeSeries(3).createCopy(0, getTimeSeries(3).getItemCount() - 1));
        JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("曲线", "Time(s)", "PPM", timeseriescollection, true, true, false);


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

        xyplot.getRenderer(0).setSeriesToolTipGenerator(0, new StandardXYToolTipGenerator("ppm:({1}, {2})",
                new SimpleDateFormat("MM-dd HH:mm:ss"),
                new DecimalFormat("0.0")));
//                xyplot.getRenderer(1).setSeriesToolTipGenerator(0, new StandardXYToolTipGenerator("ppm;({1}, {2})",
//                        new SimpleDateFormat("MM-dd HH:mm:ss"),
//                        new DecimalFormat("0.000")));



        jfreechart.getTitle().setFont(new Font("黑体", Font.BOLD, 20));//设置标题字体
        jfreechart.getLegend().setItemFont(new Font("宋体", Font.ITALIC, 15));
        chartPanel = new ChartPanel(jfreechart);
        JMenuItem jmenuitem1 = new JMenuItem("起点");
        GetPointAction action = new GetPointAction();
        jmenuitem1.addActionListener(action);
        JMenuItem jmenuitem2 = new JMenuItem("终点");
        jmenuitem2.addActionListener(action);
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
        jButton_shanda = new javax.swing.JButton();
        jButton_part = new javax.swing.JButton();
        jFormattedTextField_starttime = new javax.swing.JFormattedTextField();
        jFormattedTextField_endtime = new javax.swing.JFormattedTextField();
        jButton_restore = new javax.swing.JButton();
        jLabel_starttime = new javax.swing.JLabel();
        jLabel_endtime = new javax.swing.JLabel();
        jButton_dewpoint = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea_infos = new javax.swing.JTextArea();
        jButton_amtek = new javax.swing.JButton();
        jButton_o2 = new javax.swing.JButton();
        jButton_clear = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1024, 830));

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

        jButton_shanda.setText("载入山大  ");
        jButton_shanda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_shandaActionPerformed(evt);
            }
        });

        jButton_part.setText("截取");
        jButton_part.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_partActionPerformed(evt);
            }
        });

        jFormattedTextField_starttime.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))));

        jFormattedTextField_endtime.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))));

        jButton_restore.setText("恢复");
        jButton_restore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_restoreActionPerformed(evt);
            }
        });

        jLabel_starttime.setText("开始");

        jLabel_endtime.setText("结束");

        jButton_dewpoint.setText("载入露点仪");
        jButton_dewpoint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_dewpointActionPerformed(evt);
            }
        });

        jTextArea_infos.setColumns(20);
        jTextArea_infos.setRows(5);
        jScrollPane1.setViewportView(jTextArea_infos);

        jButton_amtek.setText("载入电解  ");
        jButton_amtek.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_amtekActionPerformed(evt);
            }
        });

        jButton_o2.setText("氧传感器");
        jButton_o2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_o2ActionPerformed(evt);
            }
        });

        jButton_clear.setText("Clear");
        jButton_clear.setEnabled(false);
        jButton_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_clearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout DrawPanelLayout = new javax.swing.GroupLayout(DrawPanel);
        DrawPanel.setLayout(DrawPanelLayout);
        DrawPanelLayout.setHorizontalGroup(
            DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DrawPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(DrawPanelLayout.createSequentialGroup()
                        .addComponent(jPanel_chart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(DrawPanelLayout.createSequentialGroup()
                        .addGroup(DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(DrawPanelLayout.createSequentialGroup()
                                .addComponent(jButton_shanda)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                                .addComponent(jButton_o2))
                            .addGroup(DrawPanelLayout.createSequentialGroup()
                                .addComponent(jButton_amtek)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(DrawPanelLayout.createSequentialGroup()
                                .addComponent(jButton_dewpoint)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton_clear)))
                        .addGap(4, 4, 4)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(DrawPanelLayout.createSequentialGroup()
                                .addComponent(jLabel_starttime)
                                .addGap(2, 2, 2)
                                .addComponent(jFormattedTextField_starttime, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel_endtime)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jFormattedTextField_endtime, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(DrawPanelLayout.createSequentialGroup()
                                .addComponent(jButton_part)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_restore)))
                        .addGap(37, 37, 37))))
        );
        DrawPanelLayout.setVerticalGroup(
            DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DrawPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel_chart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(DrawPanelLayout.createSequentialGroup()
                        .addGroup(DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton_shanda)
                            .addComponent(jFormattedTextField_starttime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextField_endtime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_starttime)
                            .addComponent(jLabel_endtime)
                            .addComponent(jButton_o2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(DrawPanelLayout.createSequentialGroup()
                                .addGroup(DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton_dewpoint)
                                    .addComponent(jButton_clear))
                                .addGap(10, 10, 10)
                                .addComponent(jButton_amtek))
                            .addGroup(DrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton_part)
                                .addComponent(jButton_restore)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
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

    private TimeSeries getTimeSeries(int num) {
        if (timeseriescollection != null) {
            return timeseriescollection.getSeries(num - 1);
        }
        return null;
    }

    private TimeSeries getTimeSeriesCopy(int num) {
        if (timeseriescopylist.size() >= num) {
            return timeseriescopylist.get(num - 1);
        }
        return null;
    }

    private void Statistics() {
        String[] list = {"山大", "露点仪", "AMTEK", "O2"};

        int max = 0;
        int min = Integer.MAX_VALUE;
        long all = 0;
        int avg = 0;
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < list.length+1; i++) {
            String string = list[i-1];
            if (getTimeSeries(i).getItemCount() > 0) {
                List a = getTimeSeries(i).getItems();
                for (Iterator it = a.iterator(); it.hasNext();) {
                    TimeSeriesDataItem item = (TimeSeriesDataItem) it.next();
                    int tmpint = item.getValue().intValue();
                    if (tmpint > max) {
                        max = tmpint;
                    }
                    if (tmpint < min) {
                        min = tmpint;
                    }
                    all += tmpint;
                }
                avg = (int) (all / a.size());
            }
            sb.append(string).append("max:").append(max==0?"无数据":max).append(";");
            sb.append(string).append("min:").append(min==Integer.MAX_VALUE?"无数据":min).append(";");
            sb.append(string).append("avg:").append(avg==0?"无数据":avg).append(";").append("\r\n");
            max = 0;
            min = Integer.MAX_VALUE;
            all = 0;
            avg = 0;
        }

        jTextArea_infos.setText(sb.toString());
    }

    private void jButton_shandaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_shandaActionPerformed
        ExtensionFileFilter filter = new ExtensionFileFilter("txt", false, true);
        filter.setDescription("结果导入");

        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("选择位置");
        jfc.setMultiSelectionEnabled(false);
        jfc.setDialogType(JFileChooser.OPEN_DIALOG);
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.setFileFilter(filter);
        int result = jfc.showOpenDialog(this);  // 打开"打开文件"对话框
        if (result == JFileChooser.APPROVE_OPTION) {
            String filesrc = jfc.getSelectedFile().getAbsolutePath();
            String a = FilePlus.ReadTextFileToStringLn(filesrc);
//            System.out.println(a);
            String[] sl = a.split("\r\n");
            List<String> al = new ArrayList<String>();
            for (int i = 0; i < sl.length; i++) {
//            for (int i = 0; i < 100; i++) {
                String str = sl[i];
                if (str.matches("^\\[.+\\].+")) {
//                    System.out.println(str);
                    al.add(str);
                }
            }
            if (al.isEmpty()) {
                return;
            }
            getTimeSeries(1).setNotify(false);
            for (int i = 0; i < al.size(); i++) {
                String string = al.get(i);

                String strtemp[] = string.replaceAll("\\[|\\]", "").split(" ");
                try {
                    Date d = sdf_shanda.parse(strtemp[0]);
//                    System.out.println(d);
                    int ppm = Integer.parseInt(strtemp[5]);
                    getTimeSeries(1).addOrUpdate(new Second(d), ppm);
                    if (i == 0) {
                        jFormattedTextField_starttime.setValue(d);
                    }
                    if (i == al.size() - 1) {
                        jFormattedTextField_endtime.setValue(d);
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(Draw.class.getName()).log(Level.SEVERE, null, ex);
                }
//                if (1 == 1) {
//                    return;
//                }
            }
            try {
                timeseriescopylist.set(0, getTimeSeries(1).createCopy(0, getTimeSeries(1).getItemCount() - 1));
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(Draw.class.getName()).log(Level.SEVERE, null, ex);
            }
            getTimeSeries(1).setNotify(true);
            Statistics();


        }
    }//GEN-LAST:event_jButton_shandaActionPerformed

    private void jButton_partActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_partActionPerformed
        Date start = (Date) jFormattedTextField_starttime.getValue();
        Date end = (Date) jFormattedTextField_endtime.getValue();

        int size = timeseriescollection.getSeriesCount();
        
        for (int i = 1; i < size + 1; i++) {
            getTimeSeries(i).setNotify(false);
        }
        for (int i = 1; i < size + 1; i++) {
            
            int starti = Math.abs(getTimeSeries(i).getIndex(new Second(start)));
            if (getTimeSeries(i).getItemCount() > 1) {
                getTimeSeries(i).delete(0, starti);
            }

            int endi = Math.abs(getTimeSeries(i).getIndex(new Second(end)));
            int alli = getTimeSeries(i).getItemCount();
            if (getTimeSeries(i).getItemCount() > 1) {
                getTimeSeries(i).delete(endi, alli - 1);
            }
        }
        for (int i = 1; i < size + 1; i++) {
            getTimeSeries(i).setNotify(true);
        }

//
//
//
//
//        int shanda_start = getTimeSeries(1).getIndex(new Second(start));
//        int dewpoint_start = getTimeSeries(2).getIndex(new Second(start));
//
//        shanda_start = Math.abs(shanda_start);
//        dewpoint_start = Math.abs(dewpoint_start);
//        if (getTimeSeries(1).getItemCount() > 1) {
//            getTimeSeries(1).delete(0, shanda_start);
//        }
//        if (getTimeSeries(2).getItemCount() > 1) {
//            getTimeSeries(2).delete(0, dewpoint_start);
//        }
////        System.out.println(a);
////        System.out.println(b);
////        System.out.println(all);
//        int shanda_end = getTimeSeries(1).getIndex(new Second(end));
//        int dewpoint_end = getTimeSeries(2).getIndex(new Second(end));
//
//        int shanda_all = getTimeSeries(1).getItemCount();
//        int dewpoint_all = getTimeSeries(2).getItemCount();
//
//        shanda_end = Math.abs(shanda_end);
//        dewpoint_end = Math.abs(dewpoint_end);
//        if (getTimeSeries(1).getItemCount() > 1) {
//            getTimeSeries(1).delete(shanda_end, shanda_all - 1);
//        }
//        if (getTimeSeries(2).getItemCount() > 1) {
//            getTimeSeries(2).delete(dewpoint_end, dewpoint_all - 1);
//        }

        Statistics();
    }//GEN-LAST:event_jButton_partActionPerformed

    private void jButton_restoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_restoreActionPerformed
        int size = timeseriescollection.getSeriesCount();
        for (int i = 1; i < size + 1; i++) {
            getTimeSeries(i).clear();
            getTimeSeries(i).setNotify(false);
            getTimeSeries(i).addAndOrUpdate(getTimeSeriesCopy(i));
            getTimeSeries(i).setNotify(true);
        }
//        getTimeSeries(1).clear();
//        getTimeSeries(1).setNotify(false);
//        getTimeSeries(1).addAndOrUpdate(getTimeSeriesCopy(1));
//        getTimeSeries(1).setNotify(true);
//        getTimeSeries(2).clear();
//        getTimeSeries(2).setNotify(false);
//        getTimeSeries(2).addAndOrUpdate(getTimeSeriesCopy(2));
//        getTimeSeries(2).setNotify(true);
        Statistics();
    }//GEN-LAST:event_jButton_restoreActionPerformed

    private void jButton_dewpointActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_dewpointActionPerformed
        ExtensionFileFilter filter = new ExtensionFileFilter("txt", false, true);
        filter.setDescription("结果导入");

        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("选择位置");
        jfc.setMultiSelectionEnabled(false);
        jfc.setDialogType(JFileChooser.OPEN_DIALOG);
//        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.setFileFilter(filter);
        int result = jfc.showOpenDialog(this);  // 打开"打开文件"对话框
        if (result == JFileChooser.APPROVE_OPTION) {
            String filesrc = jfc.getSelectedFile().getAbsolutePath();
            String a = FilePlus.ReadTextFileToStringLn(filesrc);
//            System.out.println(a);
            String[] sl = a.split("\r\n");
            List<String> al = new ArrayList<String>();
            for (int i = 0; i < sl.length; i++) {
//            for (int i = 0; i < 100; i++) {
                String str = sl[i];
                if (str.matches("^\\d\\d/\\d\\d/\\d\\d,\\d\\d:\\d\\d:\\d\\d,.+")) {
//                    System.out.println(str);
                    al.add(str);
                }
            }
            if (al.isEmpty()) {
                return;
            }
            getTimeSeries(2).setNotify(false);
            //25/10/12,15:10:46,-53.213,120.000,25.971,3.203,0.016,0.026,0.0,99.0,0.0,173.213, [Mesr]  [Ctrl] 
            for (int i = 0; i < al.size(); i++) {
                String string = al.get(i);
                string = string.replaceFirst(",", " ");
                String strtemp[] = string.split(",");
//                for (int j = 0; j < strtemp.length; j++) {
//                    String string1 = strtemp[j];
//                    System.out.println(string1);
//                }
                try {
                    Date d = sdf_dewpoint.parse(strtemp[0]);
//                    System.out.println(d);
                    double ppm = Double.parseDouble(strtemp[3]);
                    getTimeSeries(2).addOrUpdate(new Second(d), ppm);
                    if (getTimeSeries(1).isEmpty()) {
                        if (i == 0) {
                            jFormattedTextField_starttime.setValue(d);
                        }
                        if (i == al.size() - 1) {
                            jFormattedTextField_endtime.setValue(d);
                        }
                    }

                } catch (ParseException ex) {
                    Logger.getLogger(Draw.class.getName()).log(Level.SEVERE, null, ex);
                }
//                if (1 == 1) {
//                    return;
//                }
            }
            try {
                timeseriescopylist.set(1, getTimeSeries(2).createCopy(0, getTimeSeries(2).getItemCount() - 1));

            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(Draw.class.getName()).log(Level.SEVERE, null, ex);
            }
            getTimeSeries(2).setNotify(true);
            Statistics();
        }
    }//GEN-LAST:event_jButton_dewpointActionPerformed

    private void jButton_amtekActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_amtekActionPerformed
        ExtensionFileFilter filter = new ExtensionFileFilter("txt", false, true);
        filter.setDescription("结果导入");

        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("选择位置");
        jfc.setMultiSelectionEnabled(false);
        jfc.setDialogType(JFileChooser.OPEN_DIALOG);
//        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.setFileFilter(filter);
        int result = jfc.showOpenDialog(this);  // 打开"打开文件"对话框
        if (result == JFileChooser.APPROVE_OPTION) {
            String filesrc = jfc.getSelectedFile().getAbsolutePath();
            String a = FilePlus.ReadTextFileToStringLn(filesrc);
//            System.out.println(a);
            String[] sl = a.split("\r\n");
            List<String> al = new ArrayList<String>();
            for (int i = 0; i < sl.length; i++) {
//            for (int i = 0; i < 100; i++) {
                String str = sl[i];
                //2012-10-26 11:18:50
                if (str.matches("^\\d\\d\\d\\d-\\d\\d-\\d\\d\\s\\d\\d:\\d\\d:\\d\\d.*")) {
//                    System.out.println(str);
                    al.add(str);
                }
            }
            if (al.isEmpty()) {
                return;
            }
            getTimeSeries(3).setNotify(false);
            for (int i = 0; i < al.size(); i++) {
                String string = al.get(i);
//                System.out.println(string);
                string = string.replaceAll("[^a-zA-Z0-9\\.:-]+", " ");
//                System.out.println(string);
                String strtemp[] = string.split("\\s");
//                for (int j = 0; j < strtemp.length; j++) {
//                    String string1 = strtemp[j];
//                    System.out.println(j+":"+string1);
//                }
                try {
                    Date d = sdf_shanda.parse(strtemp[0].replaceAll("-", ":") + ":" + strtemp[1]);
                    double ppm = Double.parseDouble(strtemp[5]);
                    getTimeSeries(3).addOrUpdate(new Second(d), ppm);
                    if (getTimeSeries(1).isEmpty()) {
                        if (i == 0) {
                            jFormattedTextField_starttime.setValue(d);
                        }
                        if (i == al.size() - 1) {
                            jFormattedTextField_endtime.setValue(d);
                        }
                    }

                } catch (ParseException ex) {
                    Logger.getLogger(Draw.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                timeseriescopylist.set(2, getTimeSeries(3).createCopy(0, getTimeSeries(3).getItemCount() - 1));
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(Draw.class.getName()).log(Level.SEVERE, null, ex);
            }
            getTimeSeries(3).setNotify(true);
            Statistics();
        }
    }//GEN-LAST:event_jButton_amtekActionPerformed

    private void jButton_o2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_o2ActionPerformed
        int curvenum = 4;
        SimpleDateFormat tempsdf = sdf_shanda;
        String str_splitline = "\r\n";

        ExtensionFileFilter filter = new ExtensionFileFilter("txt", false, true);
        filter.setDescription("结果导入");

        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("选择位置");
        jfc.setMultiSelectionEnabled(false);
        jfc.setDialogType(JFileChooser.OPEN_DIALOG);
//        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.setFileFilter(filter);
        int result = jfc.showOpenDialog(this);  // 打开"打开文件"对话框
        if (result == JFileChooser.APPROVE_OPTION) {
            String filesrc = jfc.getSelectedFile().getAbsolutePath();
            String a = FilePlus.ReadTextFileToStringLn(filesrc);
//            System.out.println(a);
            String[] sl = a.split(str_splitline);
            List<String> al = new ArrayList<String>();
            for (int i = 0; i < sl.length; i++) {
//            for (int i = 0; i < 100; i++) {
                String str = sl[i];
                //2012-10-26 11:18:50
                if (str.matches("^\\[.+\\].+")) {
//                    System.out.println(str);
                    al.add(str);
                }
            }
            if (al.isEmpty()) {
                return;
            }
            getTimeSeries(curvenum).setNotify(false);
            for (int i = 0; i < al.size(); i++) {
                String string = al.get(i);
//                System.out.println(string);
//                string = string.replaceAll("[^a-zA-Z0-9\\.:-\\]]+", " ");
//                System.out.println(string);
                String strtemp[] = string.split("\\]");
//                for (int j = 0; j < strtemp.length; j++) {
//                    String string1 = strtemp[j];
//                    System.out.println(j+":"+string1);
//                }

                try {
                    Date d = tempsdf.parse(strtemp[0].replaceAll("\\[", ""));
                    byte[] tempbytearray = new byte[4];

//                    String[] strtemp2 = strtemp[1].split(" ");
//                    for (int j = 0; j < (strtemp2.length>4?4:strtemp2.length); j++) {
//                        tempbytearray[0] = Byte.parseByte(strtemp2[j],16);
//                    }
                    tempbytearray = BytePlus.hexStringToBytes(strtemp[1]);
                    int ppm = BytePlus.littleEndianbBytesToInt(tempbytearray);

                    getTimeSeries(4).addOrUpdate(new Second(d), ppm);
                    if (i == 0) {
                        jFormattedTextField_starttime.setValue(d);
                    }
                    if (i == al.size() - 1) {
                        jFormattedTextField_endtime.setValue(d);
                    }

                } catch (ParseException ex) {
                    Logger.getLogger(Draw.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            try {
                timeseriescopylist.set(curvenum - 1, getTimeSeries(curvenum).createCopy(0, getTimeSeries(curvenum).getItemCount() - 1));
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(Draw.class.getName()).log(Level.SEVERE, null, ex);
            }
            getTimeSeries(curvenum).setNotify(true);
            Statistics();
        }
    }//GEN-LAST:event_jButton_o2ActionPerformed

    private void jButton_clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_clearActionPerformed
        for (int i = 1; i < timeseriescollection.getItemCount(i)+1; i++) {
            getTimeSeries(i).clear();
            timeseriescopylist.set(i-1, getTimeSeries(i));
        }
        
        
    }//GEN-LAST:event_jButton_clearActionPerformed

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
                    Draw dr = new Draw();
                    dr.setVisible(true);
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(Draw.class.getName()).log(Level.SEVERE, null, ex);
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
                jFormattedTextField_starttime.setValue(date);
            } else if (command.equals("终点")) {
                jFormattedTextField_endtime.setValue(date);
            } else {
            }

//            System.out.println(chartPanel.getMousePosition().getX());
//            System.out.println(chartPanel.getMousePosition().getY());
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel DrawPanel;
    private javax.swing.JTabbedPane MainPane;
    private javax.swing.JButton jButton_amtek;
    private javax.swing.JButton jButton_clear;
    private javax.swing.JButton jButton_dewpoint;
    private javax.swing.JButton jButton_o2;
    private javax.swing.JButton jButton_part;
    private javax.swing.JButton jButton_restore;
    private javax.swing.JButton jButton_shanda;
    private javax.swing.JFormattedTextField jFormattedTextField_endtime;
    private javax.swing.JFormattedTextField jFormattedTextField_starttime;
    private javax.swing.JLabel jLabel_endtime;
    private javax.swing.JLabel jLabel_starttime;
    private javax.swing.JPanel jPanel_chart;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea_infos;
    // End of variables declaration//GEN-END:variables
}
