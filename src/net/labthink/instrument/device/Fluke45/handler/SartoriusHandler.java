/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.instrument.device.Fluke45.handler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.IllegalFormatConversionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.labthink.utils.FilePlus;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author Moses
 */
public class SartoriusHandler extends IoHandlerAdapter {

    public File outfile;
    private FileWriter fw;
    long timestart = 0;
    int countint;
    private boolean detailout;
    private boolean over = false;
    public static final int outTime = 40000;
    public static final int overtime = 10000;
//    private TimeSeries ts;
//    private boolean isdraw;
//    private boolean pausedraw = false;
//    private List<TimeSeriesDataItem> itemlist = new ArrayList<TimeSeriesDataItem>();

    public void init() {
        over = false;
        timestart = 0;
        countint = 0;
    }

    public boolean isDetailout() {
        return detailout;
    }

    public void setDetailout(boolean detailout) {
        this.detailout = detailout;
    }

    public void initfilelog() {
        try {
            fw = new FileWriter(outfile, true);

        } catch (IOException ex) {
            Logger.getLogger(SartoriusHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void flushfilelog() {
        if (fw != null) {
            try {
//                FileWriter fw = new FileWriter(outfile, true);
                fw.flush();
            } catch (IOException ex) {
                Logger.getLogger(SartoriusHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void addTextToLog(String towrite) {
        if (fw != null) {
            try {
//                FileWriter fw = new FileWriter(outfile, true);
                fw.append(towrite);
                fw.flush();
            } catch (IOException ex) {
                Logger.getLogger(SartoriusHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void teminatefilelog() {
        if (fw != null) {
            try {
//                FileWriter fw = new FileWriter(outfile, true);
                fw.flush();
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(SartoriusHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        // Empty handler
//        System.out.println("messageReceived");
        long t = System.currentTimeMillis();
        String s = message.toString();
        int dotpos = s.lastIndexOf('.');
        if (dotpos < 5) {
            return;//包不正确，返回
        } else {
            s = s.substring(dotpos - 5, dotpos + 5).replaceAll(" ", "0");//200g
//            s = s.substring(dotpos - 6, dotpos + 5).replaceAll(" ", "0");//200g
        }

        double d = 0;
        try {
            d = Double.parseDouble(s);
        } catch (NumberFormatException ex) {
            //转换错误
            System.out.println("转换错误：" + s);
        }
        if (d > 190) {
            //放上砝码状态
            countint++;
            if (countint == 1) {
                timestart = t;
            } else if (countint > 2) {
                //连续收到两个数
                long templ = t - timestart;//差值
//                if (templ > overtime) {
//                    //超50s，认为换了机器了
//                    countint = 0;
//                    over = true;
//                } else if (templ > outTime) {
                if (templ > outTime) {
                    if (templ > outTime + overtime) {
                        over = true;
                    }
                    if (over == false) {
                        String outstr = String.format("[%1$tY/%1$tm/%1$te %1$tH:%1$tM:%1$tS %1$tL] 第%2$d 秒数据：%3$f", t, templ / 1000, d);
                        System.out.println(outstr);
                        if (fw != null) {
                            fw.append(outstr);
                        }
                        over = true;
                    }
                }
            }
        } else {
            countint = 0;
            timestart = 0;
            over = false;
            //拿下了
        }

        if (detailout) {
            System.out.printf("[%1$tY/%1$tm/%1$te %1$tH:%1$tM:%1$tS %1$tL] %2$s %n", t, s);
        }
        if (fw != null) {
            fw.append(String.format("[%1$tY/%1$tm/%1$te %1$tH:%1$tM:%1$tS %1$tL] %2$s %n", t, s));
        }
    }

    public void messageSent(IoSession session, Object message) throws Exception {
        // Empty handler
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("session closed");
//        saveData();
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        System.out.println("session idled:" + status.toString());
    }

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd HH:mm:ss SSS]");
        String a = "d3-    0.030 g";
//        String a = "d3+   1.0301 g";
        a = a.replaceAll(" ", "0");
        int dotpos = a.lastIndexOf('.');
        if (dotpos < 6) {
            return;//包不正确，返回
        } else {
            a = a.substring(dotpos - 5, dotpos + 5);
            System.out.println(a);
        }
        double d = Double.parseDouble(a);
        System.out.println(d);
        //+  419.817 g \r\n
        //-    0.819 g \r\n

        System.out.println();
    }

    public static void main1(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd HH:mm:ss SSS]");
        String a = "[2014/05/12 11:31:42 350] ++9.72E-3";
        Date time;
        try {
            time = sdf.parse(a);
//            System.out.println("time:" + sdf.format(time));
            System.out.println("time:" + time);
            System.out.println(a.lastIndexOf(']'));
            System.out.println(a.lastIndexOf('+'));
            System.out.println(a.substring(a.lastIndexOf(']') + 2));
            System.out.println(a.substring(a.lastIndexOf('+')));
        } catch (ParseException ex) {
            Logger.getLogger(SartoriusHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main2(String[] args) {
//        System.out.printf("[%1$te/%1$tm/%1$tY %1$tH:%1$tM:%1$tS %1$tL] %2$s",new Date(),"wokao ");
        System.out.printf("[%1$tY/%1$tm/%1$te %1$tH:%1$tM:%1$tS %1$tL] %2$s", System.currentTimeMillis(), "wokao ");
        File f1 = new File("c:/logs/Fluck452014-05-12.txt");
        String content = FilePlus.ReadTextFileToString(f1, "\r\n", "utf-8");
        String[] sa = content.split("\r\n");

        System.out.println(sa.length);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss SSS");

//        for (int i = 0; i < sa.length; i++) {
        for (int i = 0; i < 2; i++) {
            String s = sa[i];
            if (s.startsWith("[")) {
                try {
                    s = s.replaceAll("\\[", "");
                    String[] sa2 = s.split("] ");
                    for (int j = 0; j < sa2.length; j++) {
                        String string = sa2[j];
                        System.out.println(string);
                    }
                    Date d = sdf.parse(sa2[0]);
                    System.out.println(d.getTime());

                    double d1 = Double.parseDouble(sa2[1]);
                    System.out.println(d1);

                } catch (ParseException ex) {
                    Logger.getLogger(SartoriusHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
//        System.out.println(System.currentTimeMillis());
    }
}
