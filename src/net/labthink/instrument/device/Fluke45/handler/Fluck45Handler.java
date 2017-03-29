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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.labthink.utils.FilePlus;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesDataItem;

/**
 *
 * @author Moses
 */
public class Fluck45Handler extends IoHandlerAdapter {

    public File outfile;
    private FileWriter fw;
    private TimeSeries ts;
    private boolean isdraw;
    private boolean pausedraw = false;
    private List<TimeSeriesDataItem> itemlist = new ArrayList<TimeSeriesDataItem>();

    public void setPausedraw(boolean pausedraw) {
        this.pausedraw = pausedraw;
    }
    public void switchPausedraw() {
        this.pausedraw = !this.pausedraw;
    }

    public boolean isPausedraw() {
        return pausedraw;
    }

    public TimeSeries getTs() {
        return ts;
    }

    public void setTs(TimeSeries ts) {
        this.ts = ts;
    }

    public boolean isIsdraw() {
        return isdraw;
    }

    public void setIsdraw(boolean isdraw) {
        this.isdraw = isdraw;
    }

    public void initfilelog() {
        try {
            fw = new FileWriter(outfile, true);

        } catch (IOException ex) {
            Logger.getLogger(Fluck45Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void flushfilelog() {
        if (fw != null) {
            try {
//                FileWriter fw = new FileWriter(outfile, true);
                fw.flush();
            } catch (IOException ex) {
                Logger.getLogger(Fluck45Handler.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(Fluck45Handler.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(Fluck45Handler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        // Empty handler
//        System.out.println("messageReceived");
        String s = message.toString();
        long t = System.currentTimeMillis();
        if (isdraw) {
            bufferdrawdata(t, s);
            dodraw();
        }
        System.out.printf("[%1$tY/%1$tm/%1$te %1$tH:%1$tM:%1$tS %1$tL] %2$s", t, s);
        if (fw != null) {
            fw.append(String.format("[%1$tY/%1$tm/%1$te %1$tH:%1$tM:%1$tS %1$tL] %2$s", t, s));
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

    public void bufferdrawdata(Long t, String s) {
        double d = Double.parseDouble(s)*1000;
        TimeSeriesDataItem item = new TimeSeriesDataItem(new Millisecond(new Date(t)), d);
        itemlist.add(item);

    }

    public void dodraw() {
        if(pausedraw){
            return;
        }
//        if (itemlist.size() < ts.getItemCount() / 1000 || itemlist.size() < 5) {
        if (itemlist.size() < 5) {
            //数太少时不处理
            return;
        }
        ts.setNotify(false);
        for (TimeSeriesDataItem item : itemlist) {
            ts.addOrUpdate(item.getPeriod(),item.getValue());
        }
        itemlist.clear();
        ts.setNotify(true);
    }

    public static void main(String[] args) {
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
            Logger.getLogger(Fluck45Handler.class.getName()).log(Level.SEVERE, null, ex);
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
                    Logger.getLogger(Fluck45Handler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
//        System.out.println(System.currentTimeMillis());
    }
}
