package net.labthink.utils;

/*
 *
 *//*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

/**
 *
 */
/**
 * 输出到文本组件的流。
 *
 *
 */
public class GUIPrintStream extends PrintStream {

    private JTextComponent component = null;
    private boolean logFileFlag = false;
    File logfile = null;
    FileWriter fw;
    private static SimpleDateFormat sdmf = new SimpleDateFormat("yyyy-MM-dd"); //用于格式化成 年月日
    private boolean istextarea = false;
    ;
    private StringBuilder sb = new StringBuilder();
    private boolean autoclear = false;
    private int autoclearcount = 20000;

    public GUIPrintStream(OutputStream out, JTextComponent component) {
        super(out);
        this.component = component;
    }

    public GUIPrintStream(OutputStream out, JTextComponent component, boolean ista) {
        super(out);
        this.component = component;
        istextarea = ista;
    }

    public GUIPrintStream(OutputStream out) {
        super(out);
    }

    /**
     *
     */
    /**
     * 重写write()方法，将输出信息填充到GUI组件。
     *
     * @param buf
     * @param off
     * @param len
     */
    @Override
    public void write(byte[] buf, int off, int len) {
        final String message = new String(buf, off, len);
        try {
            writeLogFile(message);
        } catch (IOException ex) {
            Logger.getLogger(GUIPrintStream.class.getName()).log(Level.SEVERE, null, ex);
        }
        updateTextArea(message);

    }

    public void clear() {
        sb = new StringBuilder();
    }

    public void writeLogFile(String message) throws IOException {
        if (isLogFileFlag()) {
            if (logfile == null) {
                logfile = new File("d:/logs/" + sdmf.format(new Date()) + ".log");
                boolean iscreate = FilePlus.createFile(logfile);
            }
            if (fw == null) {
                fw = new FileWriter(logfile, true);
            }
            fw.append(message);
            fw.flush();

        } else {
            if (fw != null) {
                fw.flush();
                fw.close();
                fw = null;
            }
        }

//        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void updateTextArea(final String text) {
        if (component == null) {
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                sb.append(text);

                if (autoclear) {
                    if (sb.length() > autoclearcount) {
                        sb = new StringBuilder(sb.substring(autoclearcount / 2));
                    }
                    component.setText(sb.toString());
                } else {
                    if (istextarea) {
                        //使用文本框的情况下直接插入
                        ((JTextArea) component).append(text);
                    } else {
//                component.write(fw)
                        component.setText(sb.toString());
                    }
                }

            }
        });
    }

    /**
     * @return the autoclear
     */
    public boolean isAutoclear() {
        return autoclear;
    }

    /**
     * @param autoclear the autoclear to set
     */
    public void setAutoclear(boolean autoclear) {
        this.autoclear = autoclear;
    }

    /**
     * @return the autoclearcount
     */
    public int getAutoclearcount() {
        return autoclearcount;
    }

    /**
     * @param autoclearcount the autoclearcount to set
     */
    public void setAutoclearcount(int autoclearcount) {
        this.autoclearcount = autoclearcount;
    }

    /**
     * @return the logFileFlag
     */
    public boolean isLogFileFlag() {
        return logFileFlag;
    }

    /**
     * @param logFileFlag the logFileFlag to set
     */
    public void setLogFileFlag(boolean logFileFlag) {
        if (logFileFlag == false) {
            System.out.println("停止记录日志文件");
            if (fw != null) {
                try {
                    fw.flush();
                    fw.close();
                } catch (IOException ex) {
                    Logger.getLogger(GUIPrintStream.class.getName()).log(Level.SEVERE, null, ex);
                }
                fw = null;
            }

            logfile = null;
        }
        this.logFileFlag = logFileFlag;
        if (logFileFlag == true) {
            System.out.println("开启记录，日志文件：" + "d:/logs/" + sdmf.format(new Date()) + ".log");
        }

    }
}
