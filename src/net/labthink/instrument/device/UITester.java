/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Httl1UITester.java 
 *
 * Created on 2010-4-21, 10:12:06
 */
package net.labthink.instrument.device;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileSystemView;
import net.labthink.instrument.device.CHYCA.codec.CHYCAProtocolCodecFactory;
import net.labthink.instrument.device.CHYCA.handler.CHYCAHandler;
import net.labthink.instrument.device.CHYCA.message.CHYCAOutMessage;
import net.labthink.instrument.device.CHYCA.simulator.CHYCA;
import net.labthink.instrument.device.Coder.codec.CoderProtocolCodecFactory;
import net.labthink.instrument.device.Coder.handler.CoderHandler;
import net.labthink.instrument.device.Coder.message.CoderMessage;
import net.labthink.instrument.device.CommandSender.CommandSenderTester01;
import net.labthink.instrument.device.CommandSender.codec.CommandSenderProtocolCodecFactory;
import net.labthink.instrument.device.CommandSender.handler.CommandSenderHandler;
import net.labthink.instrument.device.FPTF1.codec.FPTF1ProtocolCodecFactory;
import net.labthink.instrument.device.FPTF1.handler.FPTF1Handler;
import net.labthink.instrument.device.Fluke45.codec.Fluke45ProtocolCodecFactory;
import net.labthink.instrument.device.Fluke45.handler.Fluck45Handler;
import net.labthink.instrument.device.Fluke45.handler.SartoriusHandler;
import net.labthink.instrument.device.Fluke45.utils.Fluke45Chart;
import net.labthink.instrument.device.G2131.codec.G2131ProtocolCodecFactory;
import net.labthink.instrument.device.G2131.handler.G2131Handler;
import net.labthink.instrument.device.LLTEST.codec.LLTESTProtocolCodecFactory;
import net.labthink.instrument.device.LLTEST.handler.LLTESTHandler;
import net.labthink.instrument.device.LLTEST.handler.LLTESTHandler2;
import net.labthink.instrument.device.LLTEST.simulator.LLTEST;
import net.labthink.instrument.device.MXD02.MXD02Tester01;
import net.labthink.instrument.device.MXD02.codec.MXD02ProtocolCodecFactory;
import net.labthink.instrument.device.MXD02.handler.MXD02Handler;
import net.labthink.instrument.device.MXD02.simulator.MXD02;
import net.labthink.instrument.device.VACVBS.codec.VACVBSProtocolCodecFactory;
import net.labthink.instrument.device.VACVBS.handler.VACVBSHandler;
import net.labthink.instrument.device.httl1.Httl1Tester01;
import net.labthink.instrument.device.httl1.codec.HttL1ProtocolCodecFactory;
import net.labthink.instrument.device.httl1.handler.Httl1handler;
import net.labthink.instrument.device.httl1.simulator.Httl1testDataProducer;
import net.labthink.instrument.device.intelligent.industrialpc.codec.industrialpcProtocolCodecFactory;
import net.labthink.instrument.device.intelligent.industrialpc.handler.industrialpcHandler;
import net.labthink.instrument.device.intelligent.industrialpc.zigbee.ALCpacket;
import net.labthink.instrument.device.intelligent.industrialpc.zigbee.ZigbeePacket;
import net.labthink.instrument.device.w3030.codec.W3030ProtocolCodecFactory;
import net.labthink.instrument.device.w3030.handler.W3030Handler;
import net.labthink.instrument.rs232.RS232Connector;
import net.labthink.tools.ToolsNavigator;
import net.labthink.utils.BytePlus;
import net.labthink.utils.ExtensionFileFilter;
import net.labthink.utils.FilePlus;
import net.labthink.utils.GUIPrintStream;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.serial.SerialAddress;
import org.apache.mina.transport.serial.SerialAddress.DataBits;
import org.apache.mina.transport.serial.SerialAddress.FlowControl;
import org.apache.mina.transport.serial.SerialAddress.Parity;
import org.apache.mina.transport.serial.SerialAddress.StopBits;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

/**
 *
 * @author Moses
 */
public class UITester extends javax.swing.JFrame {

    int testtype = 1;
    SerialAddress portAddress;
    IoHandlerAdapter handler = null;
    RS232Connector receiver = null;
    GUIPrintStream gpstream;
    Thread t;

    /**
     * Creates new form Httl1UITester
     */
    public UITester() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup_testtype = new javax.swing.ButtonGroup();
        jFrame_computeCRC = new javax.swing.JFrame();
        jPanel2 = new javax.swing.JPanel();
        jLabel_crc1 = new javax.swing.JLabel();
        jTextField_crc1 = new javax.swing.JTextField();
        jTextField_crc2 = new javax.swing.JTextField();
        jLabel_crc2 = new javax.swing.JLabel();
        buttonGroup_lockstatus = new javax.swing.ButtonGroup();
        jTabbedPane_All = new javax.swing.JTabbedPane();
        jPanel_g2131 = new javax.swing.JPanel();
        jToggleButton_g2131 = new javax.swing.JToggleButton();
        jButton_calcValves = new javax.swing.JButton();
        jPanel_leds = new javax.swing.JPanel();
        jLabel_currentstatus = new javax.swing.JLabel();
        jLabel_balanceTime = new javax.swing.JLabel();
        jFormattedTextField_balanceTime = new javax.swing.JFormattedTextField();
        jLabel_multiplier = new javax.swing.JLabel();
        jFormattedTextField_multiplier = new javax.swing.JFormattedTextField();
        jTextField_valvesValue = new javax.swing.JTextField();
        jButton_ErrorPackage = new javax.swing.JButton();
        jPanel_030 = new javax.swing.JPanel();
        jToggleButton_w3030 = new javax.swing.JToggleButton();
        jPanel_calc = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea_datas = new javax.swing.JTextArea();
        jButton_calc = new javax.swing.JButton();
        jComboBox_calctype = new javax.swing.JComboBox();
        jPanel_httl1 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jRadioButton_testtype_renian = new javax.swing.JRadioButton();
        jRadioButton_testtype_boli = new javax.swing.JRadioButton();
        jRadioButton_testtype_kangla = new javax.swing.JRadioButton();
        jSeparator1 = new javax.swing.JSeparator();
        jButton_httl1 = new javax.swing.JButton();
        jPanel_VACVBS = new javax.swing.JPanel();
        jToggleButton_VACVBS = new javax.swing.JToggleButton();
        jPanel_about = new javax.swing.JPanel();
        jLabel_about = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel_fptf1 = new javax.swing.JPanel();
        jToggleButton_fptf1 = new javax.swing.JToggleButton();
        jPanel_chyca = new javax.swing.JPanel();
        jToggleButton_chyca = new javax.swing.JToggleButton();
        jLabel_chyca_maxgen = new javax.swing.JLabel();
        jLabel_chyca_mingen = new javax.swing.JLabel();
        jSeparator_chyca = new javax.swing.JSeparator();
        jLabel_chyca_sendcount = new javax.swing.JLabel();
        jFormattedTextField_chyca_maxgen = new javax.swing.JFormattedTextField();
        jFormattedTextField_chyca_mingen = new javax.swing.JFormattedTextField();
        jFormattedTextField_chyca_sendcount = new javax.swing.JFormattedTextField();
        jButton_chyca_send = new javax.swing.JButton();
        jPanel_ACLPacketRead = new javax.swing.JPanel();
        jToggleButton_ACLPacketRead = new javax.swing.JToggleButton();
        jLabel_gkj_readme = new javax.swing.JLabel();
        jButton_ACLPacketCount = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jCheckBox_printreadble = new javax.swing.JCheckBox();
        jButton_clearcount = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea_ToParse = new javax.swing.JTextArea();
        jButton_ToParse = new javax.swing.JButton();
        jCheckBox_detailPacket = new javax.swing.JCheckBox();
        jLabel_lockid = new javax.swing.JLabel();
        jRadioButton_lock_lock = new javax.swing.JRadioButton();
        jRadioButton_lock_unlock = new javax.swing.JRadioButton();
        jRadioButton_lock_query = new javax.swing.JRadioButton();
        jButton_lockset = new javax.swing.JButton();
        jFormattedTextField_lockid = new javax.swing.JFormattedTextField();
        jButton_tools = new javax.swing.JButton();
        jPanel_lltest = new javax.swing.JPanel();
        jLabel_lladdr = new javax.swing.JLabel();
        jComboBox_llerror = new javax.swing.JComboBox();
        jLabel_llerror = new javax.swing.JLabel();
        jToggleButton_lltest = new javax.swing.JToggleButton();
        jFormattedTextField_lladdr = new javax.swing.JFormattedTextField();
        jToggleButton_lltest2 = new javax.swing.JToggleButton();
        jPanel_Mxd02 = new javax.swing.JPanel();
        jToggleButton_mxd02 = new javax.swing.JToggleButton();
        jFormattedTextField_mxd02_times = new javax.swing.JFormattedTextField();
        jFormattedTextField_mxd02_range = new javax.swing.JFormattedTextField();
        jLabel_mxd02_times = new javax.swing.JLabel();
        jLabel_mxd02_range = new javax.swing.JLabel();
        jPanel_SendTxt = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea_command = new javax.swing.JTextArea();
        jLabel_commanddelay = new javax.swing.JLabel();
        jFormattedTextField_commanddelay = new javax.swing.JFormattedTextField();
        jToggleButton_commandSender = new javax.swing.JToggleButton();
        jButton_opencommandfile = new javax.swing.JButton();
        jCheckBox_opencommandfile_utf8 = new javax.swing.JCheckBox();
        jButton_CRCcompute = new javax.swing.JButton();
        jPanel_Coder = new javax.swing.JPanel();
        jToggleButton_Coder = new javax.swing.JToggleButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel_CurrDeg = new javax.swing.JLabel();
        jLabel_MaxDeg = new javax.swing.JLabel();
        jLabel_MinDeg = new javax.swing.JLabel();
        jPanel_codershow = new javax.swing.JPanel();
        jButton_CoderReset = new javax.swing.JButton();
        jPanel_Fluke45 = new javax.swing.JPanel();
        jToggleButton_Fluke45 = new javax.swing.JToggleButton();
        jButton_flushlog = new javax.swing.JButton();
        jTextField_comment = new javax.swing.JTextField();
        jButtonComment = new javax.swing.JButton();
        jButton_pauseDarw = new javax.swing.JButton();
        jPanel_FlukeShow = new javax.swing.JPanel();
        jCheckBox_flukedraw = new javax.swing.JCheckBox();
        jCheckBox_FlukeClearChart = new javax.swing.JCheckBox();
        jButton2 = new javax.swing.JButton();
        jPanel_Sartorius = new javax.swing.JPanel();
        jToggleButton_sartorius = new javax.swing.JToggleButton();
        jCheckBox_detailout = new javax.swing.JCheckBox();
        jPanel_functions = new javax.swing.JPanel();
        jPanel_comportset = new javax.swing.JPanel();
        jLabel_ComPort = new javax.swing.JLabel();
        jComboBox_ComPort = new javax.swing.JComboBox();
        jLabel_BaudRate = new javax.swing.JLabel();
        jComboBox_BaudRate = new javax.swing.JComboBox();
        jPanel_operates = new javax.swing.JPanel();
        jButton_emptyoutput = new javax.swing.JButton();
        jButton_saveoutput = new javax.swing.JButton();
        jCheckBox_autoclear = new javax.swing.JCheckBox();
        jFormattedTextField_autoclear = new javax.swing.JFormattedTextField();
        jCheckBox_autosave = new javax.swing.JCheckBox();
        jPanel_output = new javax.swing.JPanel();
        jScrollPane_output = new javax.swing.JScrollPane();
        jTextArea_output = new javax.swing.JTextArea();

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("CRC compute"));

        jLabel_crc1.setText("输入：");

        jTextField_crc1.setText("01 02");

        jTextField_crc2.setText("03");

        jLabel_crc2.setText("校验：");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel_crc1)
                    .addComponent(jLabel_crc2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField_crc1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTextField_crc2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 350, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField_crc1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_crc1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField_crc2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_crc2))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jFrame_computeCRCLayout = new javax.swing.GroupLayout(jFrame_computeCRC.getContentPane());
        jFrame_computeCRC.getContentPane().setLayout(jFrame_computeCRCLayout);
        jFrame_computeCRCLayout.setHorizontalGroup(
            jFrame_computeCRCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrame_computeCRCLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jFrame_computeCRCLayout.setVerticalGroup(
            jFrame_computeCRCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrame_computeCRCLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(218, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("辅助测试工具集");

        jTabbedPane_All.setName("showsartorius"); // NOI18N

        jToggleButton_g2131.setText("开始");
        jToggleButton_g2131.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton_g2131ActionPerformed(evt);
            }
        });

        jButton_calcValves.setText("计算");
        jButton_calcValves.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_calcValvesActionPerformed(evt);
            }
        });

        jPanel_leds.setBorder(javax.swing.BorderFactory.createTitledBorder("阀控制指示灯"));

        jLabel_currentstatus.setText(" ");

        javax.swing.GroupLayout jPanel_ledsLayout = new javax.swing.GroupLayout(jPanel_leds);
        jPanel_leds.setLayout(jPanel_ledsLayout);
        jPanel_ledsLayout.setHorizontalGroup(
            jPanel_ledsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_ledsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel_currentstatus, javax.swing.GroupLayout.DEFAULT_SIZE, 670, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel_ledsLayout.setVerticalGroup(
            jPanel_ledsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_ledsLayout.createSequentialGroup()
                .addContainerGap(34, Short.MAX_VALUE)
                .addComponent(jLabel_currentstatus)
                .addContainerGap())
        );

        jLabel_balanceTime.setText("平衡时间（秒）：");

        jFormattedTextField_balanceTime.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0"))));
        jFormattedTextField_balanceTime.setText("500");

        jLabel_multiplier.setText("试验数据产生速率：");

        jFormattedTextField_multiplier.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        jFormattedTextField_multiplier.setText("5");

        jTextField_valvesValue.setText("08ea");
        jTextField_valvesValue.setToolTipText("请输入16进制数字");

        jButton_ErrorPackage.setText("ErrorPackage");
        jButton_ErrorPackage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ErrorPackageActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_g2131Layout = new javax.swing.GroupLayout(jPanel_g2131);
        jPanel_g2131.setLayout(jPanel_g2131Layout);
        jPanel_g2131Layout.setHorizontalGroup(
            jPanel_g2131Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_g2131Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_g2131Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel_leds, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel_g2131Layout.createSequentialGroup()
                        .addComponent(jToggleButton_g2131)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_calcValves)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField_valvesValue, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 329, Short.MAX_VALUE)
                        .addComponent(jButton_ErrorPackage))
                    .addGroup(jPanel_g2131Layout.createSequentialGroup()
                        .addGroup(jPanel_g2131Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel_multiplier, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel_balanceTime, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel_g2131Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFormattedTextField_multiplier, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextField_balanceTime, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel_g2131Layout.setVerticalGroup(
            jPanel_g2131Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_g2131Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_g2131Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jToggleButton_g2131)
                    .addComponent(jButton_calcValves)
                    .addComponent(jTextField_valvesValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_ErrorPackage))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel_leds, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel_g2131Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_balanceTime)
                    .addComponent(jFormattedTextField_balanceTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel_g2131Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_multiplier)
                    .addComponent(jFormattedTextField_multiplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        jTabbedPane_All.addTab("G2-131", jPanel_g2131);

        jToggleButton_w3030.setText("开始");
        jToggleButton_w3030.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton_w3030ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_030Layout = new javax.swing.GroupLayout(jPanel_030);
        jPanel_030.setLayout(jPanel_030Layout);
        jPanel_030Layout.setHorizontalGroup(
            jPanel_030Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_030Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToggleButton_w3030)
                .addContainerGap(603, Short.MAX_VALUE))
        );
        jPanel_030Layout.setVerticalGroup(
            jPanel_030Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_030Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToggleButton_w3030)
                .addContainerGap(154, Short.MAX_VALUE))
        );

        jTabbedPane_All.addTab("W3/030", jPanel_030);

        jTextArea_datas.setColumns(20);
        jTextArea_datas.setRows(5);
        jScrollPane2.setViewportView(jTextArea_datas);

        jButton_calc.setText("计算");
        jButton_calc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_calcActionPerformed(evt);
            }
        });

        jComboBox_calctype.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "比例模式", "WVTR" }));
        jComboBox_calctype.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_calctypeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_calcLayout = new javax.swing.GroupLayout(jPanel_calc);
        jPanel_calc.setLayout(jPanel_calcLayout);
        jPanel_calcLayout.setHorizontalGroup(
            jPanel_calcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_calcLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel_calcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton_calc, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                    .addComponent(jComboBox_calctype, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel_calcLayout.setVerticalGroup(
            jPanel_calcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_calcLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_calcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                    .addGroup(jPanel_calcLayout.createSequentialGroup()
                        .addComponent(jButton_calc)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox_calctype, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jTabbedPane_All.addTab("数据计算", jPanel_calc);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("参数设置"));
        jPanel1.setName(""); // NOI18N

        buttonGroup_testtype.add(jRadioButton_testtype_renian);
        jRadioButton_testtype_renian.setText("热粘试验");
        jRadioButton_testtype_renian.setToolTipText("热粘试验");
        jRadioButton_testtype_renian.setActionCommand("renian");
        jRadioButton_testtype_renian.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButton_testtype_renianItemStateChanged(evt);
            }
        });

        buttonGroup_testtype.add(jRadioButton_testtype_boli);
        jRadioButton_testtype_boli.setText("剥离试验");
        jRadioButton_testtype_boli.setToolTipText("剥离试验");
        jRadioButton_testtype_boli.setActionCommand("boli");
        jRadioButton_testtype_boli.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButton_testtype_boliItemStateChanged(evt);
            }
        });

        buttonGroup_testtype.add(jRadioButton_testtype_kangla);
        jRadioButton_testtype_kangla.setSelected(true);
        jRadioButton_testtype_kangla.setText("抗拉试验");
        jRadioButton_testtype_kangla.setToolTipText("抗拉试验");
        jRadioButton_testtype_kangla.setActionCommand("kangla");
        jRadioButton_testtype_kangla.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButton_testtype_kanglaItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton_testtype_renian)
                    .addComponent(jRadioButton_testtype_boli)
                    .addComponent(jRadioButton_testtype_kangla))
                .addContainerGap(82, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jRadioButton_testtype_kangla)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton_testtype_boli)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton_testtype_renian)
                .addContainerGap(96, Short.MAX_VALUE))
        );

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jButton_httl1.setText("开始");
        jButton_httl1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_httl1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_httl1Layout = new javax.swing.GroupLayout(jPanel_httl1);
        jPanel_httl1.setLayout(jPanel_httl1Layout);
        jPanel_httl1Layout.setHorizontalGroup(
            jPanel_httl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_httl1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton_httl1)
                .addContainerGap(422, Short.MAX_VALUE))
        );
        jPanel_httl1Layout.setVerticalGroup(
            jPanel_httl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_httl1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_httl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_httl1)
                    .addComponent(jSeparator1))
                .addContainerGap())
        );

        jTabbedPane_All.addTab("Htt-L1", jPanel_httl1);

        jToggleButton_VACVBS.setText("开始");
        jToggleButton_VACVBS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton_VACVBSActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_VACVBSLayout = new javax.swing.GroupLayout(jPanel_VACVBS);
        jPanel_VACVBS.setLayout(jPanel_VACVBSLayout);
        jPanel_VACVBSLayout.setHorizontalGroup(
            jPanel_VACVBSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_VACVBSLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToggleButton_VACVBS)
                .addContainerGap(603, Short.MAX_VALUE))
        );
        jPanel_VACVBSLayout.setVerticalGroup(
            jPanel_VACVBSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_VACVBSLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToggleButton_VACVBS)
                .addContainerGap(154, Short.MAX_VALUE))
        );

        jTabbedPane_All.addTab("VACVBS", jPanel_VACVBS);

        jPanel_about.setBorder(javax.swing.BorderFactory.createTitledBorder("属性"));

        jLabel_about.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icon/info.png"))); // NOI18N
        jLabel_about.setText("<html><br/> 如果软件出现任何问题<br/> 或者您有新的需求或者好的建议<br/> 请及时联系 技术中心--刘轩<br/> Tel：8039<br/> Mail：lx0319@gmail.com<br/> </html>");
        jLabel_about.setAutoscrolls(true);
        jLabel_about.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel_about.setDoubleBuffered(true);
        jLabel_about.setFocusCycleRoot(true);
        jLabel_about.setIconTextGap(15);
        jLabel_about.setOpaque(true);
        jLabel_about.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        jLabel1.setText("<html>当前版本：V2.0.17<BR/>更新：<br/>1.增加方差计算</html>");

        javax.swing.GroupLayout jPanel_aboutLayout = new javax.swing.GroupLayout(jPanel_about);
        jPanel_about.setLayout(jPanel_aboutLayout);
        jPanel_aboutLayout.setHorizontalGroup(
            jPanel_aboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_aboutLayout.createSequentialGroup()
                .addComponent(jLabel_about, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel_aboutLayout.setVerticalGroup(
            jPanel_aboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_aboutLayout.createSequentialGroup()
                .addGroup(jPanel_aboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel_about, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel_aboutLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane_All.addTab("About", jPanel_about);

        jToggleButton_fptf1.setText("开始");
        jToggleButton_fptf1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton_fptf1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_fptf1Layout = new javax.swing.GroupLayout(jPanel_fptf1);
        jPanel_fptf1.setLayout(jPanel_fptf1Layout);
        jPanel_fptf1Layout.setHorizontalGroup(
            jPanel_fptf1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_fptf1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToggleButton_fptf1)
                .addContainerGap(603, Short.MAX_VALUE))
        );
        jPanel_fptf1Layout.setVerticalGroup(
            jPanel_fptf1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_fptf1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToggleButton_fptf1)
                .addContainerGap(154, Short.MAX_VALUE))
        );

        jTabbedPane_All.addTab("FPT-F1", jPanel_fptf1);

        jToggleButton_chyca.setText("打开串口");
        jToggleButton_chyca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton_chycaActionPerformed(evt);
            }
        });

        jLabel_chyca_maxgen.setText("数值上限");

        jLabel_chyca_mingen.setText("数值下限");

        jSeparator_chyca.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel_chyca_sendcount.setText("发送数量");

        jFormattedTextField_chyca_maxgen.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        jFormattedTextField_chyca_maxgen.setText("1200");

        jFormattedTextField_chyca_mingen.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        jFormattedTextField_chyca_mingen.setText("1000");

        jFormattedTextField_chyca_sendcount.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        jFormattedTextField_chyca_sendcount.setText("1000");

        jButton_chyca_send.setText("发送数据");
        jButton_chyca_send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_chyca_sendActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_chycaLayout = new javax.swing.GroupLayout(jPanel_chyca);
        jPanel_chyca.setLayout(jPanel_chycaLayout);
        jPanel_chycaLayout.setHorizontalGroup(
            jPanel_chycaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_chycaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_chycaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_chycaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel_chycaLayout.createSequentialGroup()
                            .addComponent(jLabel_chyca_maxgen)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jFormattedTextField_chyca_maxgen, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel_chycaLayout.createSequentialGroup()
                            .addComponent(jLabel_chyca_mingen)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jFormattedTextField_chyca_mingen, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel_chycaLayout.createSequentialGroup()
                            .addComponent(jLabel_chyca_sendcount)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jFormattedTextField_chyca_sendcount, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel_chycaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jButton_chyca_send, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jToggleButton_chyca, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator_chyca, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(528, Short.MAX_VALUE))
        );
        jPanel_chycaLayout.setVerticalGroup(
            jPanel_chycaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_chycaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_chycaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator_chyca, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel_chycaLayout.createSequentialGroup()
                        .addGroup(jPanel_chycaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel_chyca_maxgen)
                            .addComponent(jFormattedTextField_chyca_maxgen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel_chycaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel_chyca_mingen)
                            .addComponent(jFormattedTextField_chyca_mingen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel_chycaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel_chyca_sendcount)
                            .addComponent(jFormattedTextField_chyca_sendcount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addComponent(jToggleButton_chyca)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_chyca_send)))
                .addContainerGap())
        );

        jTabbedPane_All.addTab("CHY-CA", jPanel_chyca);

        jPanel_ACLPacketRead.setBorder(javax.swing.BorderFactory.createTitledBorder("控制"));
        jPanel_ACLPacketRead.setName("showthis"); // NOI18N

        jToggleButton_ACLPacketRead.setText("开始");
        jToggleButton_ACLPacketRead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton_ACLPacketReadActionPerformed(evt);
            }
        });

        jLabel_gkj_readme.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/icon/config.png"))); // NOI18N
        jLabel_gkj_readme.setText("<html> 本功能用于模拟工控机<br/> 处理从设备传送过来的数据<br/> 解析成人可读数据<br/> 新增设备数据计算工具集 <br/> V2.0.20</html>");
        jLabel_gkj_readme.setFocusable(false);

        jButton_ACLPacketCount.setText("显示计数");
        jButton_ACLPacketCount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ACLPacketCountActionPerformed(evt);
            }
        });

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jCheckBox_printreadble.setSelected(true);
        jCheckBox_printreadble.setText("是否打印输出");
        jCheckBox_printreadble.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_printreadbleActionPerformed(evt);
            }
        });

        jButton_clearcount.setText("清空计数");
        jButton_clearcount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_clearcountActionPerformed(evt);
            }
        });

        jTextArea_ToParse.setColumns(20);
        jTextArea_ToParse.setRows(5);
        jScrollPane1.setViewportView(jTextArea_ToParse);

        jButton_ToParse.setText("解析");
        jButton_ToParse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ToParseActionPerformed(evt);
            }
        });

        jCheckBox_detailPacket.setText("detailPacket");

        jLabel_lockid.setText("LockID");

        buttonGroup_lockstatus.add(jRadioButton_lock_lock);
        jRadioButton_lock_lock.setText("Lock");

        buttonGroup_lockstatus.add(jRadioButton_lock_unlock);
        jRadioButton_lock_unlock.setText("Unlock");

        buttonGroup_lockstatus.add(jRadioButton_lock_query);
        jRadioButton_lock_query.setSelected(true);
        jRadioButton_lock_query.setText("Query");

        jButton_lockset.setText("Set");
        jButton_lockset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_locksetActionPerformed(evt);
            }
        });

        jFormattedTextField_lockid.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jFormattedTextField_lockid.setText("1001");

        jButton_tools.setText("Tool");
        jButton_tools.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_toolsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_ACLPacketReadLayout = new javax.swing.GroupLayout(jPanel_ACLPacketRead);
        jPanel_ACLPacketRead.setLayout(jPanel_ACLPacketReadLayout);
        jPanel_ACLPacketReadLayout.setHorizontalGroup(
            jPanel_ACLPacketReadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_ACLPacketReadLayout.createSequentialGroup()
                .addGroup(jPanel_ACLPacketReadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_ACLPacketReadLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jToggleButton_ACLPacketRead)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_ACLPacketCount)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_tools)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox_detailPacket))
                    .addComponent(jLabel_gkj_readme, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 5, Short.MAX_VALUE)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel_ACLPacketReadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel_ACLPacketReadLayout.createSequentialGroup()
                        .addGroup(jPanel_ACLPacketReadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel_ACLPacketReadLayout.createSequentialGroup()
                                .addComponent(jLabel_lockid)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jFormattedTextField_lockid))
                            .addComponent(jCheckBox_printreadble))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel_ACLPacketReadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel_ACLPacketReadLayout.createSequentialGroup()
                                .addComponent(jButton_clearcount)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton_ToParse))
                            .addGroup(jPanel_ACLPacketReadLayout.createSequentialGroup()
                                .addComponent(jRadioButton_lock_lock)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadioButton_lock_unlock)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadioButton_lock_query)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton_lockset)))))
                .addGap(0, 0, 0))
        );
        jPanel_ACLPacketReadLayout.setVerticalGroup(
            jPanel_ACLPacketReadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_ACLPacketReadLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel_ACLPacketReadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_ACLPacketReadLayout.createSequentialGroup()
                        .addGroup(jPanel_ACLPacketReadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel_lockid)
                            .addComponent(jRadioButton_lock_lock)
                            .addComponent(jRadioButton_lock_unlock)
                            .addComponent(jRadioButton_lock_query)
                            .addComponent(jButton_lockset)
                            .addComponent(jFormattedTextField_lockid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel_ACLPacketReadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCheckBox_printreadble)
                            .addComponent(jButton_clearcount)
                            .addComponent(jButton_ToParse))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel_ACLPacketReadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_ACLPacketReadLayout.createSequentialGroup()
                            .addComponent(jLabel_gkj_readme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel_ACLPacketReadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jToggleButton_ACLPacketRead)
                                .addComponent(jButton_ACLPacketCount)
                                .addComponent(jCheckBox_detailPacket)
                                .addComponent(jButton_tools)))))
                .addContainerGap())
        );

        jTabbedPane_All.addTab("传输数据显示", jPanel_ACLPacketRead);

        jPanel_lltest.setBorder(javax.swing.BorderFactory.createTitledBorder("连路层协议测试"));
        jPanel_lltest.setName("showthis"); // NOI18N

        jLabel_lladdr.setText("目标地址：");

        jComboBox_llerror.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "无错误", "校验错误", "传输失败", "无效目的地址", "网络繁忙" }));
        jComboBox_llerror.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_llerrorItemStateChanged(evt);
            }
        });

        jLabel_llerror.setText("错误类型");

        jToggleButton_lltest.setText("开始");
        jToggleButton_lltest.setEnabled(false);
        jToggleButton_lltest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton_lltestActionPerformed(evt);
            }
        });

        jFormattedTextField_lladdr.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        jFormattedTextField_lladdr.setEnabled(false);

        jToggleButton_lltest2.setText("测试");
        jToggleButton_lltest2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton_lltest2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_lltestLayout = new javax.swing.GroupLayout(jPanel_lltest);
        jPanel_lltest.setLayout(jPanel_lltestLayout);
        jPanel_lltestLayout.setHorizontalGroup(
            jPanel_lltestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_lltestLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_lltestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_lltestLayout.createSequentialGroup()
                        .addGroup(jPanel_lltestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel_lladdr)
                            .addComponent(jLabel_llerror))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel_lltestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox_llerror, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jFormattedTextField_lladdr, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)))
                    .addGroup(jPanel_lltestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jToggleButton_lltest2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jToggleButton_lltest, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)))
                .addContainerGap(448, Short.MAX_VALUE))
        );
        jPanel_lltestLayout.setVerticalGroup(
            jPanel_lltestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_lltestLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_lltestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_lladdr)
                    .addComponent(jFormattedTextField_lladdr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel_lltestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox_llerror, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_llerror))
                .addGap(18, 18, 18)
                .addComponent(jToggleButton_lltest)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButton_lltest2)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jTabbedPane_All.addTab("LinkLayer", jPanel_lltest);

        jToggleButton_mxd02.setText("Start");
        jToggleButton_mxd02.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton_mxd02ActionPerformed(evt);
            }
        });

        jFormattedTextField_mxd02_times.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jFormattedTextField_mxd02_times.setText("1000");

        jFormattedTextField_mxd02_range.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jFormattedTextField_mxd02_range.setText("5");

        jLabel_mxd02_times.setText("次数：");

        jLabel_mxd02_range.setText("震荡：");

        javax.swing.GroupLayout jPanel_Mxd02Layout = new javax.swing.GroupLayout(jPanel_Mxd02);
        jPanel_Mxd02.setLayout(jPanel_Mxd02Layout);
        jPanel_Mxd02Layout.setHorizontalGroup(
            jPanel_Mxd02Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_Mxd02Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel_Mxd02Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToggleButton_mxd02)
                    .addGroup(jPanel_Mxd02Layout.createSequentialGroup()
                        .addGroup(jPanel_Mxd02Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel_mxd02_times)
                            .addComponent(jLabel_mxd02_range))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel_Mxd02Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jFormattedTextField_mxd02_range)
                            .addComponent(jFormattedTextField_mxd02_times, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(527, Short.MAX_VALUE))
        );
        jPanel_Mxd02Layout.setVerticalGroup(
            jPanel_Mxd02Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_Mxd02Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel_Mxd02Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedTextField_mxd02_times, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_mxd02_times))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel_Mxd02Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedTextField_mxd02_range, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_mxd02_range))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addComponent(jToggleButton_mxd02)
                .addGap(45, 45, 45))
        );

        jTabbedPane_All.addTab("MXD02定制1000次", jPanel_Mxd02);

        jPanel_SendTxt.setName("CommandSender"); // NOI18N

        jTextArea_command.setColumns(20);
        jTextArea_command.setRows(5);
        jScrollPane3.setViewportView(jTextArea_command);

        jLabel_commanddelay.setText("Send delay(ms):");

        jFormattedTextField_commanddelay.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        jFormattedTextField_commanddelay.setText("1000");

        jToggleButton_commandSender.setText("启动");
        jToggleButton_commandSender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton_commandSenderActionPerformed(evt);
            }
        });

        jButton_opencommandfile.setText("Open File");
        jButton_opencommandfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_opencommandfileActionPerformed(evt);
            }
        });

        jCheckBox_opencommandfile_utf8.setSelected(true);
        jCheckBox_opencommandfile_utf8.setText("UTF-8");

        jButton_CRCcompute.setText("CRC compute");
        jButton_CRCcompute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CRCcomputeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_SendTxtLayout = new javax.swing.GroupLayout(jPanel_SendTxt);
        jPanel_SendTxt.setLayout(jPanel_SendTxtLayout);
        jPanel_SendTxtLayout.setHorizontalGroup(
            jPanel_SendTxtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_SendTxtLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_SendTxtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_SendTxtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel_SendTxtLayout.createSequentialGroup()
                            .addComponent(jLabel_commanddelay)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jFormattedTextField_commanddelay))
                        .addComponent(jToggleButton_commandSender)
                        .addGroup(jPanel_SendTxtLayout.createSequentialGroup()
                            .addComponent(jButton_opencommandfile)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jCheckBox_opencommandfile_utf8)))
                    .addComponent(jButton_CRCcompute))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel_SendTxtLayout.setVerticalGroup(
            jPanel_SendTxtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_SendTxtLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_SendTxtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_SendTxtLayout.createSequentialGroup()
                        .addGroup(jPanel_SendTxtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel_commanddelay)
                            .addComponent(jFormattedTextField_commanddelay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_CRCcompute)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                        .addGroup(jPanel_SendTxtLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton_opencommandfile)
                            .addComponent(jCheckBox_opencommandfile_utf8))
                        .addGap(18, 18, 18)
                        .addComponent(jToggleButton_commandSender))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane_All.addTab("CommandSender", jPanel_SendTxt);

        jPanel_Coder.setName("showcoder"); // NOI18N

        jToggleButton_Coder.setText("开始");
        jToggleButton_Coder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton_CoderActionPerformed(evt);
            }
        });

        jLabel2.setText("当前值:");

        jLabel3.setText("最大值:");

        jLabel4.setText("最小值:");

        jLabel_CurrDeg.setText("0");

        jLabel_MaxDeg.setText("0");

        jLabel_MinDeg.setText("0");

        jPanel_codershow.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel_codershowLayout = new javax.swing.GroupLayout(jPanel_codershow);
        jPanel_codershow.setLayout(jPanel_codershowLayout);
        jPanel_codershowLayout.setHorizontalGroup(
            jPanel_codershowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel_codershowLayout.setVerticalGroup(
            jPanel_codershowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jButton_CoderReset.setText("复位");
        jButton_CoderReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CoderResetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_CoderLayout = new javax.swing.GroupLayout(jPanel_Coder);
        jPanel_Coder.setLayout(jPanel_CoderLayout);
        jPanel_CoderLayout.setHorizontalGroup(
            jPanel_CoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_CoderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_CoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_CoderLayout.createSequentialGroup()
                        .addComponent(jToggleButton_Coder)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton_CoderReset)
                        .addGap(0, 526, Short.MAX_VALUE))
                    .addGroup(jPanel_CoderLayout.createSequentialGroup()
                        .addGroup(jPanel_CoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel_CoderLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel_CurrDeg))
                            .addGroup(jPanel_CoderLayout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel_MaxDeg))
                            .addGroup(jPanel_CoderLayout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel_MinDeg)))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel_codershow, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel_CoderLayout.setVerticalGroup(
            jPanel_CoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_CoderLayout.createSequentialGroup()
                .addGroup(jPanel_CoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_CoderLayout.createSequentialGroup()
                        .addGroup(jPanel_CoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel_CurrDeg, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel_CoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel_MaxDeg))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel_CoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel_MinDeg))
                        .addGap(0, 91, Short.MAX_VALUE))
                    .addComponent(jPanel_codershow, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel_CoderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jToggleButton_Coder)
                    .addComponent(jButton_CoderReset))
                .addContainerGap())
        );

        jLabel_CurrDeg.getAccessibleContext().setAccessibleName("jLabel_CurrDeg");

        jTabbedPane_All.addTab("编码器测试", jPanel_Coder);

        jPanel_Fluke45.setName("showfluke45"); // NOI18N

        jToggleButton_Fluke45.setText("开始");
        jToggleButton_Fluke45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton_Fluke45ActionPerformed(evt);
            }
        });

        jButton_flushlog.setText("savelog");
        jButton_flushlog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_flushlogActionPerformed(evt);
            }
        });

        jTextField_comment.setText("comment");

        jButtonComment.setText("Add comment");
        jButtonComment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCommentActionPerformed(evt);
            }
        });

        jButton_pauseDarw.setText("Pause");
        jButton_pauseDarw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_pauseDarwActionPerformed(evt);
            }
        });

        jPanel_FlukeShow.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jCheckBox_flukedraw.setSelected(true);
        jCheckBox_flukedraw.setText("Draw ");

        jCheckBox_FlukeClearChart.setText("Clear");

        javax.swing.GroupLayout jPanel_FlukeShowLayout = new javax.swing.GroupLayout(jPanel_FlukeShow);
        jPanel_FlukeShow.setLayout(jPanel_FlukeShowLayout);
        jPanel_FlukeShowLayout.setHorizontalGroup(
            jPanel_FlukeShowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_FlukeShowLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel_FlukeShowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox_flukedraw, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jCheckBox_FlukeClearChart, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel_FlukeShowLayout.setVerticalGroup(
            jPanel_FlukeShowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_FlukeShowLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox_flukedraw)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox_FlukeClearChart)
                .addContainerGap(90, Short.MAX_VALUE))
        );

        jButton2.setText("jButton2");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_Fluke45Layout = new javax.swing.GroupLayout(jPanel_Fluke45);
        jPanel_Fluke45.setLayout(jPanel_Fluke45Layout);
        jPanel_Fluke45Layout.setHorizontalGroup(
            jPanel_Fluke45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_Fluke45Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_Fluke45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_Fluke45Layout.createSequentialGroup()
                        .addComponent(jToggleButton_Fluke45)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_flushlog)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_pauseDarw)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonComment)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField_comment, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 25, Short.MAX_VALUE))
                    .addComponent(jPanel_FlukeShow, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel_Fluke45Layout.setVerticalGroup(
            jPanel_Fluke45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_Fluke45Layout.createSequentialGroup()
                .addComponent(jPanel_FlukeShow, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel_Fluke45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jToggleButton_Fluke45)
                    .addComponent(jButton_flushlog)
                    .addComponent(jButtonComment)
                    .addComponent(jButton_pauseDarw)
                    .addComponent(jTextField_comment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        jTabbedPane_All.addTab("Fluke45", jPanel_Fluke45);

        jPanel_Sartorius.setName("showsartorius"); // NOI18N

        jToggleButton_sartorius.setText("开始");
        jToggleButton_sartorius.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton_sartoriusActionPerformed(evt);
            }
        });

        jCheckBox_detailout.setSelected(true);
        jCheckBox_detailout.setText("Detailout");

        javax.swing.GroupLayout jPanel_SartoriusLayout = new javax.swing.GroupLayout(jPanel_Sartorius);
        jPanel_Sartorius.setLayout(jPanel_SartoriusLayout);
        jPanel_SartoriusLayout.setHorizontalGroup(
            jPanel_SartoriusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_SartoriusLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_SartoriusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox_detailout)
                    .addComponent(jToggleButton_sartorius))
                .addContainerGap(581, Short.MAX_VALUE))
        );
        jPanel_SartoriusLayout.setVerticalGroup(
            jPanel_SartoriusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_SartoriusLayout.createSequentialGroup()
                .addContainerGap(113, Short.MAX_VALUE)
                .addComponent(jCheckBox_detailout)
                .addGap(18, 18, 18)
                .addComponent(jToggleButton_sartorius)
                .addContainerGap())
        );

        jTabbedPane_All.addTab("Sartorius", jPanel_Sartorius);

        jPanel_functions.setBorder(javax.swing.BorderFactory.createTitledBorder("功能设置"));

        jPanel_comportset.setBorder(javax.swing.BorderFactory.createTitledBorder("串口设置"));

        jLabel_ComPort.setText("端口设置");

        jComboBox_ComPort.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "COM10", "COM11", "COM12", "COM13", "COM14", "COM15", "COM16", "COM17", "COM18", "COM19", "COM20", "COM21", "COM22", "COM23", "COM24", "COM25", "COM26", "COM27", "COM28", "COM29", "COM30", "COM31", "COM32" }));

        jLabel_BaudRate.setText("速率");

        jComboBox_BaudRate.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "110", "300", "600", "1200", "2400", "4800", "9600", "14400", "19200", "38400", "43000", "56000", "57600", "115200" }));
        jComboBox_BaudRate.setSelectedIndex(13);
        jComboBox_BaudRate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_BaudRateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_comportsetLayout = new javax.swing.GroupLayout(jPanel_comportset);
        jPanel_comportset.setLayout(jPanel_comportsetLayout);
        jPanel_comportsetLayout.setHorizontalGroup(
            jPanel_comportsetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_comportsetLayout.createSequentialGroup()
                .addComponent(jLabel_ComPort)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox_ComPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_BaudRate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox_BaudRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel_comportsetLayout.setVerticalGroup(
            jPanel_comportsetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_comportsetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel_ComPort)
                .addComponent(jComboBox_ComPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel_BaudRate)
                .addComponent(jComboBox_BaudRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel_operates.setBorder(javax.swing.BorderFactory.createTitledBorder("操作"));

        jButton_emptyoutput.setText("清空");
        jButton_emptyoutput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_emptyoutputActionPerformed(evt);
            }
        });

        jButton_saveoutput.setText("保存");
        jButton_saveoutput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_saveoutputActionPerformed(evt);
            }
        });

        jCheckBox_autoclear.setText("AutoClear");
        jCheckBox_autoclear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_autoclearActionPerformed(evt);
            }
        });

        jFormattedTextField_autoclear.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jFormattedTextField_autoclear.setEnabled(false);
        jFormattedTextField_autoclear.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jFormattedTextField_autoclearFocusLost(evt);
            }
        });

        jCheckBox_autosave.setText("AutoSave");
        jCheckBox_autosave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_autosaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_operatesLayout = new javax.swing.GroupLayout(jPanel_operates);
        jPanel_operates.setLayout(jPanel_operatesLayout);
        jPanel_operatesLayout.setHorizontalGroup(
            jPanel_operatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_operatesLayout.createSequentialGroup()
                .addComponent(jButton_emptyoutput, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton_saveoutput, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox_autosave)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox_autoclear)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jFormattedTextField_autoclear, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );
        jPanel_operatesLayout.setVerticalGroup(
            jPanel_operatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_operatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton_emptyoutput)
                .addComponent(jCheckBox_autoclear)
                .addComponent(jFormattedTextField_autoclear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton_saveoutput)
                .addComponent(jCheckBox_autosave))
        );

        javax.swing.GroupLayout jPanel_functionsLayout = new javax.swing.GroupLayout(jPanel_functions);
        jPanel_functions.setLayout(jPanel_functionsLayout);
        jPanel_functionsLayout.setHorizontalGroup(
            jPanel_functionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_functionsLayout.createSequentialGroup()
                .addComponent(jPanel_comportset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel_operates, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel_functionsLayout.setVerticalGroup(
            jPanel_functionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_functionsLayout.createSequentialGroup()
                .addGroup(jPanel_functionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel_comportset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel_operates, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel_output.setBorder(javax.swing.BorderFactory.createTitledBorder("信息输出区"));

        jTextArea_output.setColumns(20);
        jTextArea_output.setRows(5);
        jScrollPane_output.setViewportView(jTextArea_output);

        javax.swing.GroupLayout jPanel_outputLayout = new javax.swing.GroupLayout(jPanel_output);
        jPanel_output.setLayout(jPanel_outputLayout);
        jPanel_outputLayout.setHorizontalGroup(
            jPanel_outputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane_output)
        );
        jPanel_outputLayout.setVerticalGroup(
            jPanel_outputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane_output, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel_output, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel_functions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane_All, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane_All, javax.swing.GroupLayout.PREFERRED_SIZE, 233, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel_functions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel_output, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane_All.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

   
    private void jButton_httl1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_httl1ActionPerformed
        //JOptionPane.showMessageDialog(this, testtype, "TestType Value", JOptionPane.INFORMATION_MESSAGE);
//        JOptionPane.showMessageDialog(this, jComboBox_ComPort.getSelectedItem(), "TestType Value", JOptionPane.INFORMATION_MESSAGE);
//        portAddress = new SerialAddress((String) jComboBox_ComPort.getSelectedItem(), 9600, DataBits.DATABITS_8, StopBits.BITS_1, Parity.NONE,
//                FlowControl.NONE);
        portAddress = getserialport();
        handler = new Httl1handler();
        if (receiver != null) {
            receiver.endListen();
        }
        RS232Connector receiver = RS232Connector.getInstance(handler, portAddress);

        receiver.removeFilter("logger");
        receiver.removeFilter("codec");

        receiver.addFilter("logger", new LoggingFilter());
        receiver.addFilter("codec", new ProtocolCodecFilter(new HttL1ProtocolCodecFactory()));    // 设置编码过滤器

        // receiver.addFilter("codec", new ProtocolCodecFilter(
        // new TextLineCodecFactory(Charset.forName("UTF-8")))); // 设置编码过滤器
        receiver.startListen();

        Httl1Tester01 test = new Httl1Tester01(receiver);
        Httl1testDataProducer.setKind(testtype);
        new Thread(test).start();
}//GEN-LAST:event_jButton_httl1ActionPerformed

    private void jRadioButton_testtype_kanglaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButton_testtype_kanglaItemStateChanged
        testtype = 1;
    }//GEN-LAST:event_jRadioButton_testtype_kanglaItemStateChanged

    private void jRadioButton_testtype_boliItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButton_testtype_boliItemStateChanged
        testtype = 2;
    }//GEN-LAST:event_jRadioButton_testtype_boliItemStateChanged

    private void jRadioButton_testtype_renianItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButton_testtype_renianItemStateChanged
        testtype = 3;

    }//GEN-LAST:event_jRadioButton_testtype_renianItemStateChanged

    private void jButton_calcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_calcActionPerformed
        DecimalFormat decformat = new DecimalFormat("0.0000");

        String rawdatas = jTextArea_datas.getText().trim();
//        String[] _datas = rawdatas.split(System.getProperty("line.separator"));
        String[] _datas = rawdatas.split("\r|\n");
        List<String> datas = new ArrayList<String>();
        for (int i = 0; i < _datas.length; i++) {
            String string = _datas[i].trim();
            if (!string.isEmpty()) {
                datas.add(string);
//                JOptionPane.showMessageDialog(this, ""+i+":"+string,"信息",JOptionPane.INFORMATION_MESSAGE);
            }
        }
        double[] valueArray = new double[datas.size()];

        try {
            int i = 0;
            for (Iterator<String> it = datas.iterator(); it.hasNext();) {
                String string = it.next();
                double k = Double.parseDouble(string);
                valueArray[i++] = k;
                System.out.println(k);
            }
        } catch (NumberFormatException ex) {
            outrow("输入的格式不正确,请查正");
//            ex.printStackTrace();
        }

        int calctype = jComboBox_calctype.getSelectedIndex();
        switch (calctype) {
            case 0://比例模式
                calc_bili(valueArray);
                break;
            case 1:
                calc_wvtr(valueArray);
                break;
            default:
        }


    }//GEN-LAST:event_jButton_calcActionPerformed

    private void jComboBox_calctypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_calctypeActionPerformed
        // TODO add your handling code here:
        int calctype = jComboBox_calctype.getSelectedIndex();
        String info;
        if (calctype == 0) {
            info = "切换至比例计算\r\n请录入数据";
//            jTextArea_datas.setText(info);
            JOptionPane.showMessageDialog(this, info);
            outrow(info);
        } else if (calctype == 1) {
            //wvtr
            info = "切换至WVTR计算\r\n请在第一行填写面积;\r\n第二行填写系数;\r\n第三行填写间隔时间(单位:分);\r\n从第四行开始录入数据。";
//            jTextArea_datas.setText(info);
            JOptionPane.showMessageDialog(this, info);
            outrow(info);
        } else {
//            jTextArea_datas.setText("");
        }
    }//GEN-LAST:event_jComboBox_calctypeActionPerformed

    private void jToggleButton_g2131ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton_g2131ActionPerformed

//        portAddress = new SerialAddress((String) jComboBox_ComPort.getSelectedItem(), 9600, DataBits.DATABITS_8, StopBits.BITS_1, Parity.NONE,
//                FlowControl.NONE);
        portAddress = getserialport();
        final Timer timer = new Timer();
        handler = new G2131Handler();
        int banlencetime = Math.abs(Integer.parseInt(jFormattedTextField_balanceTime.getText()));
        int multiplier = Math.abs(Integer.parseInt(jFormattedTextField_multiplier.getText()));
        ((G2131Handler) handler).loadData();
        ((G2131Handler) handler).getDevice().PreSetSimulatorParameters(banlencetime, multiplier);
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                drawleds();
                jLabel_currentstatus.setText(((G2131Handler) handler).getDevice().getStateString());
//                drawcurrentstat();
//                timer.cancel();
            }

            private void drawleds() {
                byte bs[] = ((G2131Handler) handler).getDevice().getValves();
                int flags[] = new int[bs.length];
                for (int i = 0; i < bs.length; i++) {
                    flags[i] = bs[i] == 1 ? 1 : 0;
                }
                drawled(flags);
                // Start a new thread to play a sound...
            }
//            private void clearcurrentstat() {
//                Graphics g = jPanel_leds.getGraphics();
////                Color black = Color.BLACK;
////                g.setColor(black);
//                int x = 15;
//                int y = 65;
//                g.clearRect(x, y, 100, 20);
//            }
//            private void drawcurrentstat() {
//                clearcurrentstat();
//                String a = ((G2131Handler) handler).getDevice().getStateString();
//                Graphics g = jPanel_leds.getGraphics();
//                Color black = Color.BLACK;
//                g.setColor(black);
//                int x = 15;
//                int y = 65;
//                g.drawString(a, x, y);
//            }
        }, 0, 1000);

        if (receiver != null) {
            receiver.endListen();
        }
        receiver = RS232Connector.getInstance(handler, portAddress);

        receiver.addFilter("logger", new LoggingFilter());
        receiver.addFilter("codec", new ProtocolCodecFilter(new G2131ProtocolCodecFactory()));    // 设置编码过滤器

        // receiver.addFilter("codec", new ProtocolCodecFilter(
        // new TextLineCodecFactory(Charset.forName("UTF-8")))); // 设置编码过滤器
        if (jToggleButton_g2131.isSelected()) {
            receiver.startListen();
            jToggleButton_g2131.setText("关闭");
        } else {
            receiver.endListen();
            timer.cancel();

            jToggleButton_g2131.setText("开始");
        }

    }//GEN-LAST:event_jToggleButton_g2131ActionPerformed

    private void jToggleButton_w3030ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton_w3030ActionPerformed
        // TODO add your handling code here:
//        portAddress = new SerialAddress((String) jComboBox_ComPort.getSelectedItem(), 9600, DataBits.DATABITS_8, StopBits.BITS_1, Parity.NONE,
//                FlowControl.NONE);
        portAddress = getserialport();

        handler = new W3030Handler();
        ((W3030Handler) handler).loadData();

        if (receiver != null) {
            receiver.endListen();
        }
        receiver = RS232Connector.getInstance(handler, portAddress);

        receiver.addFilter("logger", new LoggingFilter());
        receiver.addFilter("codec", new ProtocolCodecFilter(new W3030ProtocolCodecFactory()));    // 设置编码过滤器

        // receiver.addFilter("codec", new ProtocolCodecFilter(
        // new TextLineCodecFactory(Charset.forName("UTF-8")))); // 设置编码过滤器
        if (jToggleButton_w3030.isSelected()) {
            receiver.startListen();
            jToggleButton_w3030.setText("关闭");
        } else {
            receiver.endListen();
            ((W3030Handler) handler).saveData();
            jToggleButton_w3030.setText("开始");
        }

    }//GEN-LAST:event_jToggleButton_w3030ActionPerformed

    private void jButton_calcValvesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_calcValvesActionPerformed
        String value = jTextField_valvesValue.getText();
        int ivalue = 0;
        try {
            ivalue = Integer.parseInt(value, 16);
        } catch (Exception ex) {
        }
        String value_binary = Integer.toBinaryString(ivalue);
        if (value_binary.length() < 13) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 13 - value_binary.length(); i++) {
                sb.append("0");
            }
            sb.append(value_binary);
            value_binary = sb.toString();
        }

        int[] flag = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        if (value_binary.charAt(value_binary.length() - 1) == '1') {
            flag[0] = 1;
        }
        if (value_binary.charAt(value_binary.length() - 3) == '1') {
            flag[1] = 1;
        }
        if (value_binary.charAt(value_binary.length() - 5) == '1') {
            flag[2] = 1;
        }
        for (int i = 0; i < 7; i++) {
            if (value_binary.charAt(i) == '1') {
                flag[9 - i] = 1;
            }
        }
        System.out.println(value_binary);

        drawled(flag);
    }//GEN-LAST:event_jButton_calcValvesActionPerformed

    private void jButton_ErrorPackageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ErrorPackageActionPerformed
        ((G2131Handler) handler).allerror = !((G2131Handler) handler).allerror;
        outrow("" + ((G2131Handler) handler).allerror);
    }//GEN-LAST:event_jButton_ErrorPackageActionPerformed

    private void jToggleButton_VACVBSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton_VACVBSActionPerformed
        // TODO add your handling code here:
//        portAddress = new SerialAddress((String) jComboBox_ComPort.getSelectedItem(), 9600, DataBits.DATABITS_8, StopBits.BITS_1, Parity.NONE,
//                FlowControl.NONE);
        portAddress = getserialport();

        handler = new VACVBSHandler();
        //((VACVBSHandler) handler).loadData();

        if (receiver != null) {
            receiver.endListen();
        }
        receiver = RS232Connector.getInstance(handler, portAddress);

        receiver.addFilter("logger", new LoggingFilter());
        receiver.addFilter("codec", new ProtocolCodecFilter(new VACVBSProtocolCodecFactory()));    // 设置编码过滤器

        // receiver.addFilter("codec", new ProtocolCodecFilter(
        // new TextLineCodecFactory(Charset.forName("UTF-8")))); // 设置编码过滤器
        if (jToggleButton_VACVBS.isSelected()) {
            receiver.startListen();
            jToggleButton_VACVBS.setText("关闭");
        } else {
            receiver.endListen();
            ((VACVBSHandler) handler).saveData();
            jToggleButton_VACVBS.setText("开始");
        }
    }//GEN-LAST:event_jToggleButton_VACVBSActionPerformed

    private void jToggleButton_fptf1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton_fptf1ActionPerformed
        // TODO add your handling code here:
//        portAddress = new SerialAddress((String) jComboBox_ComPort.getSelectedItem(), 9600, DataBits.DATABITS_8, StopBits.BITS_1, Parity.NONE,
//                FlowControl.NONE);
        portAddress = getserialport();

        handler = new FPTF1Handler();
        //((FPTF1Handler) handler).loadData();

        if (receiver != null) {
            receiver.endListen();
        }
        receiver = RS232Connector.getInstance(handler, portAddress);

        receiver.addFilter("logger", new LoggingFilter());
        receiver.addFilter("codec", new ProtocolCodecFilter(new FPTF1ProtocolCodecFactory()));    // 设置编码过滤器

        // receiver.addFilter("codec", new ProtocolCodecFilter(
        // new TextLineCodecFactory(Charset.forName("UTF-8")))); // 设置编码过滤器
        if (jToggleButton_fptf1.isSelected()) {
            receiver.startListen();
            jToggleButton_fptf1.setText("关闭");
        } else {
            receiver.endListen();
            ((FPTF1Handler) handler).saveData();
            jToggleButton_fptf1.setText("开始");
        }
    }//GEN-LAST:event_jToggleButton_fptf1ActionPerformed

    private void jToggleButton_lltestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton_lltestActionPerformed
        //链路层测试。
//        portAddress = new SerialAddress((String) jComboBox_ComPort.getSelectedItem(), 9600, DataBits.DATABITS_8, StopBits.BITS_1, Parity.NONE,
//                FlowControl.NONE);
        portAddress = getserialport();
//        jTextArea_output.ap
        //((FPTF1Handler) handler).loadData();
        LLTEST device = new LLTEST();
        int llerror = jComboBox_llerror.getSelectedIndex();
        device.setResend(llerror != 0);
        device.setCheckerror(llerror == 1);
        device.setSendfail(llerror == 2);
        device.setDesterror(llerror == 3);
        device.setNetbusy(llerror == 4);
//        String addr = jFormattedTextField_lladdr.getText().trim();
//        short iaddr = Short.parseShort(addr);

//        device.setDestaddr(iaddr);
        handler = new LLTESTHandler(device);

//        System.setOut(new GUIPrintStream(System.out, textArea));
//        ((LLTESTHandler) handler).setOuts(new GUIPrintStream(System.out, jTextArea_output));
//        jTextArea_output.getDocument().
        if (receiver != null) {
            receiver.endListen();
        }
        receiver = RS232Connector.getInstance(handler, portAddress);

        receiver.addFilter("logger", new LoggingFilter());
        receiver.addFilter("codec", new ProtocolCodecFilter(new LLTESTProtocolCodecFactory()));    // 设置编码过滤器

        // receiver.addFilter("codec", new ProtocolCodecFilter(
        // new TextLineCodecFactory(Charset.forName("UTF-8")))); // 设置编码过滤器
        if (jToggleButton_lltest.isSelected()) {
            receiver.startListen();
            jToggleButton_lltest.setText("关闭");
        } else {
            receiver.endListen();
            ((LLTESTHandler) handler).saveData();
            jToggleButton_lltest.setText("开始");
        }
    }//GEN-LAST:event_jToggleButton_lltestActionPerformed

    private void jComboBox_llerrorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_llerrorItemStateChanged
        if (handler == null) {
            return;
        }
        int llerror = jComboBox_llerror.getSelectedIndex();
        LLTEST device = ((LLTESTHandler) handler).getDevice();
        device.setResend(llerror != 0);
        device.setCheckerror(llerror == 1);
        device.setSendfail(llerror == 2);
        device.setDesterror(llerror == 3);
        device.setNetbusy(llerror == 4);
    }//GEN-LAST:event_jComboBox_llerrorItemStateChanged

    private void jToggleButton_chycaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton_chycaActionPerformed
//        portAddress = new SerialAddress((String) jComboBox_ComPort.getSelectedItem(), 9600, DataBits.DATABITS_8, StopBits.BITS_1, Parity.NONE,
//                FlowControl.NONE);
        portAddress = getserialport();
//        jTextArea_output.ap
        //((FPTF1Handler) handler).loadData();
        CHYCA device = new CHYCA();

        handler = new CHYCAHandler(device);

        System.setOut(new GUIPrintStream(System.out, jTextArea_output));

//        ((CHYCAHandler) handler).setOuts(new GUIPrintStream(System.out, jTextArea_output));
//        jTextArea_output.getDocument().
        if (receiver != null) {
            receiver.endListen();
        }
        receiver = RS232Connector.getInstance(handler, portAddress);

        receiver.addFilter("logger", new LoggingFilter());
        receiver.addFilter("codec", new ProtocolCodecFilter(new CHYCAProtocolCodecFactory()));    // 设置编码过滤器

        // receiver.addFilter("codec", new ProtocolCodecFilter(
        // new TextLineCodecFactory(Charset.forName("UTF-8")))); // 设置编码过滤器
        if (jToggleButton_chyca.isSelected()) {
            receiver.startListen();
            jToggleButton_chyca.setText("关闭串口");
        } else {
            receiver.endListen();
            ((CHYCAHandler) handler).saveData();
            jToggleButton_chyca.setText("打开串口");
        }

    }//GEN-LAST:event_jToggleButton_chycaActionPerformed

    private void jButton_chyca_sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_chyca_sendActionPerformed
        try {
            jFormattedTextField_chyca_maxgen.commitEdit();
            jFormattedTextField_chyca_mingen.commitEdit();
            jFormattedTextField_chyca_sendcount.commitEdit();
        } catch (ParseException ex) {
            Logger.getLogger(UITester.class.getName()).log(Level.SEVERE, null, ex);
        }
        Long maxgen = (Long) jFormattedTextField_chyca_maxgen.getValue();
        Long mingen = (Long) jFormattedTextField_chyca_mingen.getValue();
        final Long sendcount = (Long) jFormattedTextField_chyca_sendcount.getValue();

//        System.out.println(maxgen);
//        System.out.println(mingen);
//        System.out.println(sendcount);
        if (jToggleButton_chyca.isSelected()) {
            final CHYCA device = new CHYCA();
            device.init(maxgen, mingen);
            int i = device.getCount();

            Thread t1 = new Thread(new Runnable() {
                public void run() {
                    while (device.getCount() < sendcount) {
                        IoSession session = receiver.getReceiverSession();
                        CHYCAOutMessage pkt = new CHYCAOutMessage();
                        byte bt[] = pkt.getContent();
                        bt[0] = 0;
                        byte[] bs = device.getValue();
                        System.arraycopy(bs, 0, bt, 1, 3);
                        pkt.setContent(bt);
                        WriteFuture wf;
                        wf = session.write(pkt);// 发送消息
                        wf.awaitUninterruptibly();
                        try {
                            Thread.sleep(951);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(UITester.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
//                drawcurrentstat();
//                timer.cancel();

                }
            });
            t1.start();

        } else {
            JOptionPane.showMessageDialog(jLabel_ComPort, "串口未打开");
        }


    }//GEN-LAST:event_jButton_chyca_sendActionPerformed

    private void jToggleButton_ACLPacketReadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton_ACLPacketReadActionPerformed

//        portAddress = new SerialAddress((String) jComboBox_ComPort.getSelectedItem(), 9600, DataBits.DATABITS_8, StopBits.BITS_1, Parity.NONE,
//                FlowControl.NONE);
        portAddress = getserialport();

        if (handler == null) {
            handler = new industrialpcHandler();
        }
//        SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        boolean isprintflag = jCheckBox_printreadble.isSelected();
        ((industrialpcHandler) handler).printflag = isprintflag;
        boolean printdetail = jCheckBox_detailPacket.isSelected();
        ((industrialpcHandler) handler).printdetail = printdetail;

        //((FPTF1Handler) handler).loadData();
        if (receiver != null) {
            receiver.endListen();
        }
        receiver = RS232Connector.getInstance(handler, portAddress);

        receiver.addFilter("logger", new LoggingFilter());
        receiver.addFilter("codec", new ProtocolCodecFilter(new industrialpcProtocolCodecFactory()));    // 设置编码过滤器

        // receiver.addFilter("codec", new ProtocolCodecFilter(
        // new TextLineCodecFactory(Charset.forName("UTF-8")))); // 设置编码过滤器
        if (jToggleButton_ACLPacketRead.isSelected()) {
            try {
                receiver.startListen();
            } catch (RuntimeIoException e) {
                System.out.println("串口打开失败");
            }

            if (((industrialpcHandler) handler).outfile == null) {
                File path = new File("c:\\logs\\");
                path.mkdirs();
                ((industrialpcHandler) handler).outfile = new File("c:\\logs\\zigbee" + sdf.format(new Date()) + ".txt");
            }
            ((industrialpcHandler) handler).initfilelog();
            jToggleButton_ACLPacketRead.setText("关闭");
        } else {
            receiver.endListen();

//            ((industrialpcHandler) handler).outfile =null;
            ((industrialpcHandler) handler).teminatefilelog();

            ((industrialpcHandler) handler).saveData();
            jToggleButton_ACLPacketRead.setText("开始");
        }

    }//GEN-LAST:event_jToggleButton_ACLPacketReadActionPerformed

    private void jButton_ACLPacketCountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ACLPacketCountActionPerformed
        // TODO add your handling code here:
        if (handler != null) {
            if (handler.getClass().equals(industrialpcHandler.class)) {
                JOptionPane.showMessageDialog(this, ((industrialpcHandler) handler).packetcount, "收包个数", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("收包共：" + ((industrialpcHandler) handler).packetcount);
            } else {
                System.out.println("有没搞错,尚未开始试验吧？");
            }
        }

    }//GEN-LAST:event_jButton_ACLPacketCountActionPerformed

    private void jToggleButton_lltest2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton_lltest2ActionPerformed
        // TODO add your handling code here:
        //链路层测试。
//        portAddress = new SerialAddress((String) jComboBox_ComPort.getSelectedItem(), 9600, DataBits.DATABITS_8, StopBits.BITS_1, Parity.NONE,
//                FlowControl.NONE);
        portAddress = getserialport();
//        jTextArea_output.ap
        //((FPTF1Handler) handler).loadData();
//        LLTEST device = new LLTEST();
//        int llerror = jComboBox_llerror.getSelectedIndex();
//        device.setResend(llerror != 0);
//        device.setCheckerror(llerror == 1);
//        device.setSendfail(llerror == 2);
//        device.setDesterror(llerror == 3);
//        device.setNetbusy(llerror == 4);
//        String addr = jFormattedTextField_lladdr.getText().trim();
//        short iaddr = Short.parseShort(addr);
//        device.setDestaddr(iaddr);
//        System.setOut(new GUIPrintStream(System.out, textArea));
//        ((LLTESTHandler) handler).setOuts(new GUIPrintStream(System.out, jTextArea_output));
//        jTextArea_output.getDocument().
        if (!jToggleButton_lltest2.isSelected()) {

            if (receiver != null) {
                receiver.endListen();
            }
        } else {
            handler = new LLTESTHandler2();
            receiver = RS232Connector.getInstance(handler, portAddress);
            receiver.addFilter("codec", new ProtocolCodecFilter(new LLTESTProtocolCodecFactory()));    // 设置编码过滤器

        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (((LLTESTHandler2) handler).outfile == null) {
            File path = new File("c:\\logs\\");
            path.mkdirs();
            ((LLTESTHandler2) handler).outfile = new File("c:\\logs\\LLT" + sdf.format(new Date()) + ".txt");
        }

//        receiver.addFilter("logger", new LoggingFilter());
        // receiver.addFilter("codec", new ProtocolCodecFilter(
        // new TextLineCodecFactory(Charset.forName("UTF-8")))); // 设置编码过滤器
        if (jToggleButton_lltest2.isSelected()) {
            receiver.startListen();
            ((LLTESTHandler2) handler).initfilelog();
            jToggleButton_lltest2.setText("停止测试");
        } else {
            receiver.endListen();
            ((LLTESTHandler2) handler).teminatefilelog();
            ((LLTESTHandler2) handler).saveData();
            jToggleButton_lltest2.setText("测试");
        }
    }//GEN-LAST:event_jToggleButton_lltest2ActionPerformed

    private void jCheckBox_printreadbleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_printreadbleActionPerformed
        boolean isprintflag = jCheckBox_printreadble.isSelected();
        ((industrialpcHandler) handler).printflag = isprintflag;
    }//GEN-LAST:event_jCheckBox_printreadbleActionPerformed

    private void jButton_clearcountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_clearcountActionPerformed
        if (JOptionPane.showConfirmDialog(jButton_clearcount, "清0计数，是否继续？") != JOptionPane.YES_OPTION) {
            return;
        }
        ((industrialpcHandler) handler).packetcount = 0;
        System.out.println("清空计数，当前计数：0");
    }//GEN-LAST:event_jButton_clearcountActionPerformed

    private void jToggleButton_mxd02ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton_mxd02ActionPerformed
        //JOptionPane.showMessageDialog(this, testtype, "TestType Value", JOptionPane.INFORMATION_MESSAGE);
//        JOptionPane.showMessageDialog(this, jComboBox_ComPort.getSelectedItem(), "TestType Value", JOptionPane.INFORMATION_MESSAGE);
//        portAddress = new SerialAddress((String) jComboBox_ComPort.getSelectedItem(), 9600, DataBits.DATABITS_8, StopBits.BITS_1, Parity.NONE,
//                FlowControl.NONE);

        portAddress = getserialport();
        handler = new MXD02Handler();
        if (receiver != null) {
            receiver.endListen();
        }
//        RS232Connector receiver = RS232Connector.getInstance(handler, portAddress);
        receiver = RS232Connector.getInstance(handler, portAddress);
        if (jToggleButton_mxd02.isSelected()) {
            receiver.addFilter("logger", new LoggingFilter());
            receiver.addFilter("codec", new ProtocolCodecFilter(new MXD02ProtocolCodecFactory()));    // 设置编码过滤器
        }
        // receiver.addFilter("codec", new ProtocolCodecFilter(
        // new TextLineCodecFactory(Charset.forName("UTF-8")))); // 设置编码过滤器

        try {
            jFormattedTextField_mxd02_times.commitEdit();
            jFormattedTextField_mxd02_range.commitEdit();
        } catch (ParseException ex) {
            Logger.getLogger(UITester.class.getName()).log(Level.SEVERE, null, ex);
        }

        int times = Integer.parseInt(jFormattedTextField_mxd02_times.getText());
        int range = Integer.parseInt(jFormattedTextField_mxd02_range.getText());
        MXD02 device = new MXD02();
        device.setTesttimes(times);
        device.setRange(range);

        MXD02Tester01 test = new MXD02Tester01(receiver);
        test.setDevice(device);
//        MXD02TestDataProducer.setKind(testtype);
        if (jToggleButton_mxd02.isSelected()) {
            receiver.startListen();
            jToggleButton_mxd02.setText("STOP");
            test.setRunflag(true);
            t = new Thread(test);
            t.start();

        } else {
            receiver.endListen();
            ((MXD02Handler) handler).saveData();
            jToggleButton_mxd02.setText("Start");
            test.setRunflag(false);
            t.interrupt();
        }


    }//GEN-LAST:event_jToggleButton_mxd02ActionPerformed

    private void jButton_ToParseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ToParseActionPerformed
        String parsestr = jTextArea_ToParse.getText();
        if (parsestr.length() < 1) {
//            return; 
        } else {
            parsestr = parsestr.replaceAll(" ", "");
            byte[] bs = BytePlus.hexStringToByte(parsestr);
            ALCpacket pkt = new ALCpacket(bs);
            try {
                System.out.println(pkt.toReadbleString());
            } catch (IOException ex) {
                Logger.getLogger(UITester.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_jButton_ToParseActionPerformed

    private void jToggleButton_commandSenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton_commandSenderActionPerformed

        portAddress = getserialport();
        handler = new CommandSenderHandler();
        if (receiver != null) {
            receiver.endListen();
        }
//        RS232Connector receiver = RS232Connector.getInstance(handler, portAddress);
        receiver = RS232Connector.getInstance(handler, portAddress);

        if (jToggleButton_commandSender.isSelected()) {
            receiver.addFilter("logger", new LoggingFilter());
            receiver.addFilter("codec", new ProtocolCodecFilter(new CommandSenderProtocolCodecFactory()));    // 设置编码过滤器
        }
        // receiver.addFilter("codec", new ProtocolCodecFilter(
        // new TextLineCodecFactory(Charset.forName("UTF-8")))); // 设置编码过滤器

//
//        try {
//            jFormattedTextField_commanddelay.commitEdit();
//        } catch (ParseException ex) {
//            Logger.getLogger(UITester.class.getName()).log(Level.SEVERE, null, ex);
//        }
        String InputCommand = jTextArea_command.getText();
        if (InputCommand.length() < 2) {
            if (jToggleButton_commandSender.isSelected()) {
                jToggleButton_commandSender.setSelected(false);
            }
            System.out.println("请检查内容。");
            return;
        }
        try {
            //        int delay = Integer.parseInt(jFormattedTextField_commanddelay.getText());
            jFormattedTextField_commanddelay.commitEdit();
        } catch (ParseException ex) {
            Logger.getLogger(UITester.class.getName()).log(Level.SEVERE, null, ex);
        }
        Long delay = (Long) jFormattedTextField_commanddelay.getValue();

        CommandSenderTester01 test = new CommandSenderTester01(receiver);
        test.content = InputCommand;
        test.delay = delay;
        if (test.parsecontent(InputCommand).size() < 1) {
            if (jToggleButton_commandSender.isSelected()) {
                jToggleButton_commandSender.setSelected(false);
            }
            System.out.println("请检查内容。");
            return;
        }
//        test.setDevice(device);
//        MXD02TestDataProducer.setKind(testtype);
        if (jToggleButton_commandSender.isSelected()) {
            receiver.startListen();
            jToggleButton_commandSender.setText("STOP");
//            test.setRunflag(true);
            t = new Thread(test);
            t.start();

        } else {
            receiver.endListen();
            ((CommandSenderHandler) handler).saveData();
            jToggleButton_commandSender.setText("Start");
//            test.setRunflag(false);
            t.interrupt();
        }


    }//GEN-LAST:event_jToggleButton_commandSenderActionPerformed

    private void jButton_opencommandfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_opencommandfileActionPerformed
        String charset = "GB2312";
        if (jCheckBox_opencommandfile_utf8.isSelected()) {
            charset = "uft-8";
        }
        ExtensionFileFilter filter = new ExtensionFileFilter("log,txt", true);
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
        if (result == JFileChooser.APPROVE_OPTION) {
            String filepath = jfc.getSelectedFile().getAbsolutePath();
            String a = FilePlus.ReadTextFileToString(new File(filepath), "\r\n", charset);
            jTextArea_command.append(a);
        }
    }//GEN-LAST:event_jButton_opencommandfileActionPerformed

    private void jButton_CRCcomputeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CRCcomputeActionPerformed
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();
        int height = 300;
        int width = 400;
        jFrame_computeCRC.setBounds((d.width - width) / 2, (d.height - height) / 2, width, height);

        jTextField_crc1.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                crc();
            }

            public void removeUpdate(DocumentEvent e) {
                crc();
            }

            public void insertUpdate(DocumentEvent e) {
                crc();
            }

            public void crc() {
                String str1 = jTextField_crc1.getText();
                byte[] bytes1 = BytePlus.hexStringToBytes(str1);
                byte crc = 0;
                for (int i = 0; i < bytes1.length; i++) {
                    byte b = bytes1[i];
                    crc += b;
                }
                String out = Integer.toHexString((crc + 256) % 256);
                while (out.length() < 2) {
                    out = "0" + out;
                }
                jTextField_crc2.setText(out);
            }
        });

        jFrame_computeCRC.setVisible(true);
    }//GEN-LAST:event_jButton_CRCcomputeActionPerformed
    Timer timer = null;
    TimerTask tt = null;
    TimeSeriesCollection timeseriescollection;
    ChartPanel chartPanel;
    private void jToggleButton_CoderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton_CoderActionPerformed
        if (timer == null) {
            timer = new Timer();
        }
//        portAddress = new SerialAddress((String) jComboBox_ComPort.getSelectedItem(), 9600, DataBits.DATABITS_8, StopBits.BITS_1, Parity.NONE,
//                FlowControl.NONE);
        portAddress = getserialport();

        if (handler == null) {
            handler = new CoderHandler();
        }

        if (receiver != null) {
            receiver.endListen();
        }
        receiver = RS232Connector.getInstance(handler, portAddress);

        receiver.addFilter("logger", new LoggingFilter());
        receiver.addFilter("codec", new ProtocolCodecFilter(new CoderProtocolCodecFactory()));    // 设置编码过滤器

        //绘图
        TimeSeries ts = new TimeSeries("当前值", Millisecond.class);
        TimeSeries ts1 = new TimeSeries("最大值", Millisecond.class);
        TimeSeries ts2 = new TimeSeries("最小值", Millisecond.class);
        timeseriescollection = new TimeSeriesCollection(ts);
        timeseriescollection.addSeries(ts1);
        timeseriescollection.addSeries(ts2);
        JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("曲线", "Time(s)", "Value", timeseriescollection, true, true, false);
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
        chartPanel = new ChartPanel(jfreechart);
        chartPanel.setSize(800, 360);
        chartPanel.setPreferredSize(new Dimension(600, 502));
        jPanel_codershow.add(chartPanel, BorderLayout.CENTER);

        if (jToggleButton_Coder.isSelected()) {
            try {
                receiver.startListen();
            } catch (RuntimeIoException e) {
                System.out.println("串口打开失败");
                return;
            }
            final IoSession session = receiver.getReceiverSession();

            tt = new TimerTask() {
                public void run() {
                    try {
                        int times = 1;
                        times++;
                        CoderMessage pkt = new CoderMessage();
                        byte[] currentba = {1, 0, 0, 0};
                        byte[] maxba = {2, 0, 0, 0};
                        byte[] minba = {3, 0, 0, 0};

                        pkt.setContent(currentba);
                        pkt.setTail(pkt.calcTail());
                        WriteFuture wf = session.write(pkt);// 发送消息
                        wf.awaitUninterruptibly();
                        Thread.sleep(50);

                        pkt.setContent(maxba);
                        pkt.setTail(pkt.calcTail());
                        wf = session.write(pkt);// 发送消息
                        Thread.sleep(50);
                        wf.awaitUninterruptibly();

                        pkt.setContent(minba);
                        pkt.setTail(pkt.calcTail());
                        wf = session.write(pkt);// 发送消息
                        wf.awaitUninterruptibly();
//                        Thread.sleep(200);
//                            System.out.println("test");
                        if (((CoderHandler) handler).vals.start == true) {
                            int currv = ((CoderHandler) handler).vals.getCurrentdeg();
                            int maxv = ((CoderHandler) handler).vals.getMaxdeg();
                            int minv = ((CoderHandler) handler).vals.getMindeg();
                            jLabel_CurrDeg.setText("  " + currv);
                            jLabel_MaxDeg.setText("  " + maxv);
                            jLabel_MinDeg.setText("  " + minv);
                            System.out.println("当前：" + currv + ",最大：" + maxv + "最小：" + minv);
                            timeseriescollection.getSeries(0).addOrUpdate(new Millisecond(), currv);
                            timeseriescollection.getSeries(1).addOrUpdate(new Millisecond(), maxv);
                            timeseriescollection.getSeries(2).addOrUpdate(new Millisecond(), minv);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(UILogger.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };

            timer.scheduleAtFixedRate(tt, 50, 1000);

            jToggleButton_Coder.setText("关闭");
        } else {
            receiver.endListen();
            if (tt != null) {
                tt.cancel();
            }
//            timer.cancel();
            timer.purge();
            jToggleButton_Coder.setText("开始");
        }

    }//GEN-LAST:event_jToggleButton_CoderActionPerformed

    private void jButton_CoderResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CoderResetActionPerformed
        final IoSession session = receiver.getReceiverSession();
        if (session == null) {
            System.out.println("未开始");
            return;
        }
        CoderMessage pkt = new CoderMessage();
        byte[] restba = {0, 0, 0, 0};
        pkt.setContent(restba);
        pkt.setTail(pkt.calcTail());
        WriteFuture wf = session.write(pkt);// 发送消息
        wf.awaitUninterruptibly();
    }//GEN-LAST:event_jButton_CoderResetActionPerformed

    private void jButton_locksetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_locksetActionPerformed
//         Calendar now = Calendar.getInstance();
//            int year = now.get(Calendar.YEAR);
//            System.out.println(year);
        //public ALCpacket(byte[] _devicenum, byte[] _testnum, byte[] _testkind, byte[] _packetbody) {

        boolean testflag = false;

        if (!(jToggleButton_ACLPacketRead.isSelected() || testflag)) {
            System.out.println("请检查串口");
        } else {
            String command = buttonGroup_lockstatus.getSelection().getActionCommand();
            byte[] cmd = {0, 0};
            if (jRadioButton_lock_lock.isSelected()) {
                cmd[1] = 1;
            } else if (jRadioButton_lock_unlock.isSelected()) {
                cmd[1] = 2;
            } else if (jRadioButton_lock_query.isSelected()) {
            }

            Calendar now = Calendar.getInstance();
            byte year = (byte) (now.get(Calendar.YEAR) - 2000);
            byte month = (byte) now.get(Calendar.MONTH);
            byte day = (byte) now.get(Calendar.DAY_OF_MONTH);
            byte hour = (byte) now.get(Calendar.HOUR_OF_DAY);
            byte minute = (byte) now.get(Calendar.MINUTE);
            byte second = (byte) now.get(Calendar.SECOND);
            //懒得转换了，毫秒直接写0
            byte[] outtime = {year, month, day, hour, minute, second, 0, 0};
            byte[] reserve1 = new byte[8];
            byte[] reserve2 = new byte[48];
            byte[] packetbody = new byte[1 + 8 + 2 + 8 + 48];

            int currpos = 0;
            packetbody[0] = (byte) 0x9C;
            currpos += 1;
            System.arraycopy(outtime, 0, packetbody, currpos, outtime.length);//8位 时间编码
            currpos += outtime.length;
            System.arraycopy(cmd, 0, packetbody, currpos, cmd.length);
            currpos += cmd.length;
            System.arraycopy(reserve1, 0, packetbody, currpos, reserve1.length);
            currpos += reserve1.length;
            System.arraycopy(reserve2, 0, packetbody, currpos, reserve2.length);
            currpos += reserve2.length;
//            System.out.println("pos:"+currpos);
//            System.out.println("packetbody:"+packetbody.length);
//            System.out.println(BytePlus.byteArray2String(packetbody));
//            System.out.println("=============");

            byte[] devicenum = {1, 1, 13, 10, 10};
            byte[] testnum = {10, 0, 0, 0};
            byte[] testkind = {0, 0};
            ALCpacket alcp = new ALCpacket(devicenum, testnum, testkind, packetbody);

            //拼zigbee头
            byte[] zigbeehead = new byte[5];

            byte head_control = (byte) (((new Random()).nextInt() % 15 + 15) % 15);
//            System.out.println(head_control);
            head_control = (byte) (head_control + 0xA0);
            zigbeehead[0] = head_control;

            byte lqi = 0x7F;
            zigbeehead[1] = lqi;

            String id = jFormattedTextField_lockid.getText();
            int tempi = Integer.parseInt(id);
            zigbeehead[2] = (byte) (tempi / 256);
            zigbeehead[3] = (byte) (tempi % 256);
            byte length = (byte) alcp.getWholePacket().length;
            zigbeehead[4] = length;
//            System.out.println(BytePlus.byteArray2String(zigbeehead));
            ZigbeePacket zp = new ZigbeePacket(zigbeehead, alcp);
            byte[] a = zp.getAllPacket();
            System.out.println(BytePlus.byteArray2String(a));
//终于，包拼接的差不多了。
            IoSession session = receiver.getReceiverSession();
            WriteFuture wf = session.write(zp);// 发送消息
            wf.awaitUninterruptibly();

        }

    }//GEN-LAST:event_jButton_locksetActionPerformed

    private void jButton_toolsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_toolsActionPerformed
        // TODO add your handling code here:
        ToolsNavigator rs = new ToolsNavigator();
        rs.setVisible(true);
        rs.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        rs.setLocationRelativeTo(getOwner());

    }//GEN-LAST:event_jButton_toolsActionPerformed

    private void jComboBox_BaudRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_BaudRateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox_BaudRateActionPerformed

    private void jToggleButton_Fluke45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton_Fluke45ActionPerformed
        portAddress = getserialport();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        if (handler == null) {
            handler = new Fluck45Handler();
        }

        if (receiver != null) {
            receiver.endListen();
        }
        receiver = RS232Connector.getInstance(handler, portAddress);

        receiver.addFilter("logger", new LoggingFilter());
        receiver.addFilter("codec", new ProtocolCodecFilter(new Fluke45ProtocolCodecFactory()));    // 设置编码过滤器
        if (chartPanel == null) {
            FlukeDrawInit();
        }
        ((Fluck45Handler) handler).setTs(timeseriescollection.getSeries(0));
        ((Fluck45Handler) handler).setIsdraw(jCheckBox_flukedraw.isSelected());
        timeseriescollection.getSeries(0).setMaximumItemCount(36000);
        // receiver.addFilter("codec", new ProtocolCodecFilter(
        // new TextLineCodecFactory(Charset.forName("UTF-8")))); // 设置编码过滤器
        if (jToggleButton_Fluke45.isSelected()) {
            try {
                receiver.startListen();
            } catch (RuntimeIoException e) {
                System.out.println("串口打开失败");
            }

            if (((Fluck45Handler) handler).outfile == null) {
                File path = new File("c:\\logs\\");
                path.mkdirs();
                ((Fluck45Handler) handler).outfile = new File("c:\\logs\\Fluke45" + sdf.format(new Date()) + ".txt");
            }
            ((Fluck45Handler) handler).initfilelog();
            jToggleButton_Fluke45.setText("关闭");
        } else {
            receiver.endListen();
            if (jCheckBox_FlukeClearChart.isSelected()) {
                timeseriescollection.getSeries(0).clear();
            }
//            ((industrialpcHandler) handler).outfile =null;
            ((Fluck45Handler) handler).teminatefilelog();

            jToggleButton_Fluke45.setText("开始");
        }

    }//GEN-LAST:event_jToggleButton_Fluke45ActionPerformed

    private void jButton_flushlogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_flushlogActionPerformed
        if (handler != null) {
            ((Fluck45Handler) handler).flushfilelog();
        }
    }//GEN-LAST:event_jButton_flushlogActionPerformed

    private void jButtonCommentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCommentActionPerformed
        String commentToWrite = String.format("==================== %s ====================\r\n", jTextField_comment.getText());
        if (handler != null) {
            System.out.println(commentToWrite);
            ((Fluck45Handler) handler).addTextToLog(commentToWrite);
        }
    }//GEN-LAST:event_jButtonCommentActionPerformed

    private void jButton_pauseDarwActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_pauseDarwActionPerformed
        ((Fluck45Handler) handler).switchPausedraw();
        boolean pause = ((Fluck45Handler) handler).isPausedraw();
        if (pause) {
            jButton_pauseDarw.setText("Continue");
        } else {
            jButton_pauseDarw.setText("Pause");

        }

//        ExtensionFileFilter filter = new ExtensionFileFilter("log,txt", true, true);
//        filter.setDescription("命令文件");
//
//        JFileChooser jfc = new JFileChooser();
//        FileSystemView fsv = FileSystemView.getFileSystemView();
//        //得到桌面路径
//        jfc.setCurrentDirectory(fsv.getHomeDirectory());
//        jfc.setDialogTitle("选择文件位置");
//        jfc.setMultiSelectionEnabled(false);
//        jfc.setDialogType(JFileChooser.OPEN_DIALOG);
//        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
//        jfc.setFileFilter(filter);
//        int result = jfc.showOpenDialog(this);  // 打开"打开文件"对话框
//        String content;
//        if (result == JFileChooser.APPROVE_OPTION) {
//            String filepath = jfc.getSelectedFile().getAbsolutePath();
//            content = FilePlus.ReadTextFileToString(new File(filepath), "\r\n", "utf-8");
//        } else {
//            content = null;
//        }
//        if (content != null) {
//            String[] sa = content.split("\r\n");
//
//            FlukeDrawInit();
//
//            timeseriescollection.getSeries(0).setNotify(false);
//            SimpleDateFormat sdfx = new SimpleDateFormat("[yyyy/MM/dd HH:mm:ss SSS]");
//            for (int i = 0; i < sa.length; i++) {
//                String s = sa[i];
//                if (s.startsWith("[")) {
////                    s = s.replaceAll("\\[", "");
//
////                    String[] sa2 = s.split("]");
//                    try {
//                        Date time = sdfx.parse(s);
//                        int position = s.lastIndexOf('+');
//                        position = position == -1 ? s.lastIndexOf(']') + 2 : position;
//                        double d1 = Double.parseDouble(s.substring(position)) * 1000;
//                        timeseriescollection.getSeries(0).addOrUpdate(new Millisecond(time), d1);
//                    } catch (ParseException ex) {
//                        System.out.println(s);
//                        Logger.getLogger(UITester.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//            }
//
//            timeseriescollection.getSeries(0).setNotify(true);
//        }

    }//GEN-LAST:event_jButton_pauseDarwActionPerformed

    public void FlukeDrawInit() {
//            System.out.println(sa.length);
        //绘图
        //绘图
        TimeSeries ts = new TimeSeries("O2 Sensor Voltage", Millisecond.class);
        timeseriescollection = new TimeSeriesCollection(ts);
        JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("曲线", "Time(s)", "Value", timeseriescollection, true, true, false);
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
//        if (chartPanel != null) {
//            jPanel_FlukeShow.remove(chartPanel);
//        }
        chartPanel = new ChartPanel(jfreechart);
        chartPanel.setSize(1000, 280);
        chartPanel.setPreferredSize(new Dimension(1000, 302));
        jPanel_FlukeShow.add(chartPanel, BorderLayout.CENTER);
        xyplot.getRenderer(0).setSeriesToolTipGenerator(0, new StandardXYToolTipGenerator("{1}, {2}mV",
                new SimpleDateFormat("MM-dd HH:mm:ss"),
                new DecimalFormat("0.00")));
    }

    private void jCheckBox_autosaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_autosaveActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        if (jCheckBox_autosave.isSelected()) {
            //            jFormattedTextField_autoclear.setEnabled(true);
            //            jFormattedTextField_autoclear.setValue(20000);
            gpstream.setLogFileFlag(true);
            //            gpstream.setAutoclear(true);
            //            gpstream.setAutoclearcount((Integer) jFormattedTextField_autoclear.getValue());
        } else {
            gpstream.setLogFileFlag(false);
            //            jFormattedTextField_autoclear.setEnabled(false);
            //            jFormattedTextField_autoclear.setText("");
            //            gpstream.setAutoclear(false);
            //            jFormattedTextField_autoclear.setValue(0);
        }
    }//GEN-LAST:event_jCheckBox_autosaveActionPerformed

    private void jFormattedTextField_autoclearFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextField_autoclearFocusLost
        gpstream.setAutoclearcount((Integer) jFormattedTextField_autoclear.getValue());
        System.out.println("设置自动清空上限为：" + (Integer) jFormattedTextField_autoclear.getValue());
    }//GEN-LAST:event_jFormattedTextField_autoclearFocusLost

    private void jCheckBox_autoclearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_autoclearActionPerformed
        // TODO add your handling code here:
        if (jCheckBox_autoclear.isSelected()) {
            jFormattedTextField_autoclear.setEnabled(true);
            jFormattedTextField_autoclear.setValue(20000);
            gpstream.setAutoclear(true);
            gpstream.setAutoclearcount((Integer) jFormattedTextField_autoclear.getValue());
        } else {
            jFormattedTextField_autoclear.setEnabled(false);
            jFormattedTextField_autoclear.setText("");
            gpstream.setAutoclear(false);
            //            jFormattedTextField_autoclear.setValue(0);
        }
    }//GEN-LAST:event_jCheckBox_autoclearActionPerformed

    private void jButton_saveoutputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_saveoutputActionPerformed
        //        JOptionPane.showConfirmDialog(jLabel_ComPort, "本功能暂未实现，留待下一版本想起来再说");
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
        int result = jfc.showOpenDialog(this);  // 打开"打开文件"对话框
        if (result == JFileChooser.APPROVE_OPTION) {
            String file = jfc.getSelectedFile().getAbsolutePath();
            int dotindex = file.indexOf(".");
            if (dotindex < 0) {
                file = file + ".log";
            }
            try {
                FileWriter fw = new FileWriter(file, true);
                //                FileWriter fw = new FileWriter(jfc.getSelectedFile(), true);
                fw.append(jTextArea_output.getText());
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(UITester.class.getName()).log(Level.SEVERE, null, ex);
            }
            //        int ibaudrate = Integer.parseInt((String)jComboBox_BaudRate.getSelectedItem());
            //        int baudrate = (Integer)jComboBox_BaudRate.getSelectedItem();
            //        System.out.println(baudrate);
            //        System.out.println("aaa");
        }

        //        int ibaudrate = Integer.parseInt((String)jComboBox_BaudRate.getSelectedItem());
        //        int baudrate = (Integer)jComboBox_BaudRate.getSelectedItem();
        //        System.out.println(baudrate);
        //        System.out.println("aaa");
    }//GEN-LAST:event_jButton_saveoutputActionPerformed

    private void jButton_emptyoutputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_emptyoutputActionPerformed
        gpstream.clear();
        System.setOut(gpstream);
        //        System.out.flush();
        //        System.out.close();
        jTextArea_output.setText("");
        //        System.setOut(new GUIPrintStream(System.out, ut.jTextArea_output));
    }//GEN-LAST:event_jButton_emptyoutputActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Fluke45Chart rs = new Fluke45Chart();
        rs.setVisible(true);
        rs.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        rs.setLocationRelativeTo(getOwner());

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jToggleButton_sartoriusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton_sartoriusActionPerformed
        portAddress = getserialport();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        if (handler == null) {
            handler = new SartoriusHandler();
        }

        if (receiver != null) {
            receiver.endListen();
        }
        receiver = RS232Connector.getInstance(handler, portAddress);

//        receiver.addFilter("logger", new LoggingFilter());
        receiver.addFilter("codec", new ProtocolCodecFilter(new Fluke45ProtocolCodecFactory()));    // 设置编码过滤器
//        if (chartPanel == null) {
//            FlukeDrawInit();
//        }
//        ((SartoriusHandler)handler).setTs(timeseriescollection.getSeries(0));

        ((SartoriusHandler) handler).setDetailout(jCheckBox_detailout.isSelected());
        ((SartoriusHandler) handler).init();
//        ((SartoriusHandler)handler).setIsdraw(jCheckBox_flukedraw.isSelected());
//        timeseriescollection.getSeries(0).setMaximumItemCount(36000);
        // receiver.addFilter("codec", new ProtocolCodecFilter(
        // new TextLineCodecFactory(Charset.forName("UTF-8")))); // 设置编码过滤器
        if (jToggleButton_sartorius.isSelected()) {
            try {
                receiver.startListen();
            } catch (RuntimeIoException e) {
                System.out.println("串口打开失败");
            }

            if (((SartoriusHandler) handler).outfile == null) {
                File path = new File("c:\\logs\\");
                path.mkdirs();
                ((SartoriusHandler) handler).outfile = new File("c:\\logs\\Fluke45" + sdf.format(new Date()) + ".txt");
            }
            ((SartoriusHandler) handler).initfilelog();
            jToggleButton_sartorius.setText("关闭");
        } else {
            receiver.endListen();
//            if(jCheckBox_FlukeClearChart.isSelected()){
//                timeseriescollection.getSeries(0).clear();
//            }
//            ((industrialpcHandler) handler).outfile =null;
            ((SartoriusHandler) handler).teminatefilelog();

            jToggleButton_sartorius.setText("开始");
        }
    }//GEN-LAST:event_jToggleButton_sartoriusActionPerformed

    private void drawled(int[] flag) {
        Graphics g = jPanel_leds.getGraphics();
        Color white = Color.WHITE;
        Color red = Color.red;
        int x = 15;
        int y = 30;
        int xspan = 20;
        int yspan = 5;
        int fontspan = 4;
        int length = 16;
        String[] tag = {"A", "B", "C", "4", "5", "6", "7", "8", "9", "10"};
        if (flag.length != 10) {
            flag = new int[10];
            for (int i = 0; i < flag.length; i++) {
                flag[i] = 0;
            }
        }
//        int[] flag = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        for (int i = 0; i < flag.length; i++) {
            int j = flag[i];
            if (j == 1) {
                g.setColor(red);
            } else {
                g.setColor(white);
            }
            g.drawString(tag[i], x + i * xspan + (fontspan < 10 ? fontspan : fontspan / -2), y);
            g.fillArc(x + i * xspan, y + yspan, length, length, 0, 360);
        }
    }

    private void outrow(String s) {
        if (jTextArea_output.getLineCount() > 1000) {
            jTextArea_output.setText("");

        }
        jTextArea_output.append(s);
        jTextArea_output.append("\r\n");

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                UITester ut = new UITester();

                Date today = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date afterdate;
                try {
                    afterdate = format.parse("2015-08-02"); // Try catch省略了
                    if (today.after(afterdate)) {
                        ut.dispose();
                    } else {
                        ut.setVisible(true);
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(UITester.class.getName()).log(Level.SEVERE, null, ex);
                }
//                ut.setVisible(true);

                ut.gpstream = new GUIPrintStream(System.out, ut.jTextArea_output, true);
                System.setOut(ut.gpstream);

                Component cs[] = ut.jTabbedPane_All.getComponents();
//                ut.jTabbedPane_All.remove(10);

//只查看一个页面时使用该段代码
//                if (true) {
                if (false) {
                    for (int i = 0; i < cs.length; i++) {
                        Component c = cs[i];
                        String name = c.getName();
                        if (name == null || !name.equals("showsartorius")) {
//                        if (name == null || !name.equals("showcoder")) {
                            ut.jTabbedPane_All.remove(c);
                        }
                    }
                }

//                if (true) {
                if (false) {
                    for (int i = 0; i < cs.length; i++) {
                        Component c = cs[i];
                        String name = c.getName();
                        if (name == null || !name.equals("showfluke45")) {
//                        if (name == null || !name.equals("showcoder")) {
                            ut.jTabbedPane_All.remove(c);
                        }
                    }
                }
//                if (false) {
                if (true) {
                    for (int i = 0; i < cs.length; i++) {
                        Component c = cs[i];
                        String name = c.getName();
                        if (name == null || !name.equals("showthis")) {
                            ut.jTabbedPane_All.remove(c);
                        }
                    }
                }
                if (false) {
                    for (int i = 0; i < cs.length; i++) {
                        Component c = cs[i];
                        String name = c.getName();
                        if (name == null || !name.equals("CommandSender")) {
                            ut.jTabbedPane_All.remove(c);
                        }
                    }
                }

//                ut.repaint();
//
//                System.out.println(ut.jTabbedPane_All.getComponents().length);
            }
        });

    }

    private SerialAddress getserialport() {
        int ibaudrate = Integer.parseInt((String) jComboBox_BaudRate.getSelectedItem());
        String port = (String) jComboBox_ComPort.getSelectedItem();
        portAddress = new SerialAddress(port, ibaudrate, DataBits.DATABITS_8, StopBits.BITS_1, Parity.NONE,
                FlowControl.NONE);
        return portAddress;
    }

    /**
     * 计算比例模式数据
     *
     * @param valueArray
     */
    private void calc_bili(double[] valueArray) {
        outrow("================================================================");
        DecimalFormat decformat = new DecimalFormat("0.0000");
        outrow(
                "------------------------与重量一差值------------------------------");

        double[] diffValueArray = new double[valueArray.length - 1];

        for (int i = 0; i
                < diffValueArray.length; i++) {
            diffValueArray[i] = valueArray[i + 1] - valueArray[0];
            //System.out.println("Δg"+i+":"+decformat.format(diffValueArray[i]).toString());
            outrow(
                    "Δg" + (i + 1) + ":" + decformat.format(diffValueArray[i]).toString());

        }
        decformat = new DecimalFormat("0.0000%");
        outrow(
                "---------------------------比例系数------------------------------");

        for (int i = 2; i
                < diffValueArray.length; i++) {
            double value = 0;

            if (diffValueArray[i - 2] - diffValueArray[i - 1] != 0) {
                value = (diffValueArray[i - 2] + diffValueArray[i] - 2 * diffValueArray[i - 1]) / (diffValueArray[i - 2] - diffValueArray[i - 1]);

            } else {
                value = 0;
                outrow(
                        "比例系数差值0，请增大时间");

            } //System.out.println("∮"+(i-1)+":"+decformat.format(value).toString());
            outrow("∮" + (i - 1) + ":" + decformat.format(value).toString());

        }
        outrow("================================================================");

    }

    /**
     * 计算WVTR数据
     *
     * @param valueArray
     */
    private void calc_wvtr(double[] valueArray) {
        if (valueArray.length < 5) {
            outrow("输入数据不正确，请查正");

            return;

        }
        double area = valueArray[0];

        double factor = valueArray[1];

        double time = valueArray[2];

        if (area == 0) {
            outrow("面积输入错误");

        }
        if (time == 0) {
            outrow("时间输入错误");

        }
        DecimalFormat decformat = new DecimalFormat("0.0000");
        outrow(
                "---------------------------数据差值------------------------------");

        double[] diffValueArray = new double[valueArray.length - 4];

        for (int i = 4; i
                < valueArray.length; i++) {
            diffValueArray[i - 4] = valueArray[i] - valueArray[i - 1];

        }
        for (int i = 0; i
                < diffValueArray.length; i++) {
            double d = diffValueArray[i];
            outrow(
                    "Δg" + (i + 1) + ":" + decformat.format(d).toString());

        }
        outrow("---------------------------WVTR---------------------------------");

        for (int i = 0; i
                < diffValueArray.length; i++) {
            double value = (diffValueArray[i] * 24 * 60) * factor / (area * time * 6 * 0.0001);
            //System.out.println("∮"+(i-1)+":"+decformat.format(value).toString());
            outrow(
                    "wvtr" + (i + 1) + ":" + decformat.format(value).toString());

        }
        outrow("================================================================");

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup_lockstatus;
    private javax.swing.ButtonGroup buttonGroup_testtype;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonComment;
    private javax.swing.JButton jButton_ACLPacketCount;
    private javax.swing.JButton jButton_CRCcompute;
    private javax.swing.JButton jButton_CoderReset;
    private javax.swing.JButton jButton_ErrorPackage;
    private javax.swing.JButton jButton_ToParse;
    private javax.swing.JButton jButton_calc;
    private javax.swing.JButton jButton_calcValves;
    private javax.swing.JButton jButton_chyca_send;
    private javax.swing.JButton jButton_clearcount;
    private javax.swing.JButton jButton_emptyoutput;
    private javax.swing.JButton jButton_flushlog;
    private javax.swing.JButton jButton_httl1;
    private javax.swing.JButton jButton_lockset;
    private javax.swing.JButton jButton_opencommandfile;
    private javax.swing.JButton jButton_pauseDarw;
    private javax.swing.JButton jButton_saveoutput;
    private javax.swing.JButton jButton_tools;
    private javax.swing.JCheckBox jCheckBox_FlukeClearChart;
    private javax.swing.JCheckBox jCheckBox_autoclear;
    private javax.swing.JCheckBox jCheckBox_autosave;
    private javax.swing.JCheckBox jCheckBox_detailPacket;
    private javax.swing.JCheckBox jCheckBox_detailout;
    private javax.swing.JCheckBox jCheckBox_flukedraw;
    private javax.swing.JCheckBox jCheckBox_opencommandfile_utf8;
    private javax.swing.JCheckBox jCheckBox_printreadble;
    private javax.swing.JComboBox jComboBox_BaudRate;
    private javax.swing.JComboBox jComboBox_ComPort;
    private javax.swing.JComboBox jComboBox_calctype;
    private javax.swing.JComboBox jComboBox_llerror;
    private javax.swing.JFormattedTextField jFormattedTextField_autoclear;
    private javax.swing.JFormattedTextField jFormattedTextField_balanceTime;
    private javax.swing.JFormattedTextField jFormattedTextField_chyca_maxgen;
    private javax.swing.JFormattedTextField jFormattedTextField_chyca_mingen;
    private javax.swing.JFormattedTextField jFormattedTextField_chyca_sendcount;
    private javax.swing.JFormattedTextField jFormattedTextField_commanddelay;
    private javax.swing.JFormattedTextField jFormattedTextField_lladdr;
    private javax.swing.JFormattedTextField jFormattedTextField_lockid;
    private javax.swing.JFormattedTextField jFormattedTextField_multiplier;
    private javax.swing.JFormattedTextField jFormattedTextField_mxd02_range;
    private javax.swing.JFormattedTextField jFormattedTextField_mxd02_times;
    private javax.swing.JFrame jFrame_computeCRC;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel_BaudRate;
    private javax.swing.JLabel jLabel_ComPort;
    private javax.swing.JLabel jLabel_CurrDeg;
    private javax.swing.JLabel jLabel_MaxDeg;
    private javax.swing.JLabel jLabel_MinDeg;
    private javax.swing.JLabel jLabel_about;
    private javax.swing.JLabel jLabel_balanceTime;
    private javax.swing.JLabel jLabel_chyca_maxgen;
    private javax.swing.JLabel jLabel_chyca_mingen;
    private javax.swing.JLabel jLabel_chyca_sendcount;
    private javax.swing.JLabel jLabel_commanddelay;
    private javax.swing.JLabel jLabel_crc1;
    private javax.swing.JLabel jLabel_crc2;
    private javax.swing.JLabel jLabel_currentstatus;
    private javax.swing.JLabel jLabel_gkj_readme;
    private javax.swing.JLabel jLabel_lladdr;
    private javax.swing.JLabel jLabel_llerror;
    private javax.swing.JLabel jLabel_lockid;
    private javax.swing.JLabel jLabel_multiplier;
    private javax.swing.JLabel jLabel_mxd02_range;
    private javax.swing.JLabel jLabel_mxd02_times;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel_030;
    private javax.swing.JPanel jPanel_ACLPacketRead;
    private javax.swing.JPanel jPanel_Coder;
    private javax.swing.JPanel jPanel_Fluke45;
    private javax.swing.JPanel jPanel_FlukeShow;
    private javax.swing.JPanel jPanel_Mxd02;
    private javax.swing.JPanel jPanel_Sartorius;
    private javax.swing.JPanel jPanel_SendTxt;
    private javax.swing.JPanel jPanel_VACVBS;
    private javax.swing.JPanel jPanel_about;
    private javax.swing.JPanel jPanel_calc;
    private javax.swing.JPanel jPanel_chyca;
    private javax.swing.JPanel jPanel_codershow;
    private javax.swing.JPanel jPanel_comportset;
    private javax.swing.JPanel jPanel_fptf1;
    private javax.swing.JPanel jPanel_functions;
    private javax.swing.JPanel jPanel_g2131;
    private javax.swing.JPanel jPanel_httl1;
    private javax.swing.JPanel jPanel_leds;
    private javax.swing.JPanel jPanel_lltest;
    private javax.swing.JPanel jPanel_operates;
    private javax.swing.JPanel jPanel_output;
    private javax.swing.JRadioButton jRadioButton_lock_lock;
    private javax.swing.JRadioButton jRadioButton_lock_query;
    private javax.swing.JRadioButton jRadioButton_lock_unlock;
    private javax.swing.JRadioButton jRadioButton_testtype_boli;
    private javax.swing.JRadioButton jRadioButton_testtype_kangla;
    private javax.swing.JRadioButton jRadioButton_testtype_renian;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane_output;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator_chyca;
    private javax.swing.JTabbedPane jTabbedPane_All;
    private javax.swing.JTextArea jTextArea_ToParse;
    private javax.swing.JTextArea jTextArea_command;
    private javax.swing.JTextArea jTextArea_datas;
    private javax.swing.JTextArea jTextArea_output;
    private javax.swing.JTextField jTextField_comment;
    private javax.swing.JTextField jTextField_crc1;
    private javax.swing.JTextField jTextField_crc2;
    private javax.swing.JTextField jTextField_valvesValue;
    private javax.swing.JToggleButton jToggleButton_ACLPacketRead;
    private javax.swing.JToggleButton jToggleButton_Coder;
    private javax.swing.JToggleButton jToggleButton_Fluke45;
    private javax.swing.JToggleButton jToggleButton_VACVBS;
    private javax.swing.JToggleButton jToggleButton_chyca;
    private javax.swing.JToggleButton jToggleButton_commandSender;
    private javax.swing.JToggleButton jToggleButton_fptf1;
    private javax.swing.JToggleButton jToggleButton_g2131;
    private javax.swing.JToggleButton jToggleButton_lltest;
    private javax.swing.JToggleButton jToggleButton_lltest2;
    private javax.swing.JToggleButton jToggleButton_mxd02;
    private javax.swing.JToggleButton jToggleButton_sartorius;
    private javax.swing.JToggleButton jToggleButton_w3030;
    // End of variables declaration//GEN-END:variables
}
