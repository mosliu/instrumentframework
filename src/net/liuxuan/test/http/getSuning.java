/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.liuxuan.test.http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Moses
 */
public class getSuning implements Runnable {

    int a1;
    int a2;
    int c1;
    int c2;
    public long sleeptime = 1000;

    public getSuning(int a1, int a2,  int c1, int c2) {
        this.a1 = a1;
        this.a2 = a2;
        this.c1 = c1;
        this.c2 = c2;
    }

    public static void main(String[] args) {
//        System.out.println(doBase64Encode("259_6013969198_10424178"));
//        ArrayList<String> sal = produceNo(259, 260, 6013969990L, 6013969999L, 10424990, 10424999);
//        for (Iterator<String> it = sal.iterator(); it.hasNext();) {
//            String string = it.next();
//            String encodecstr = doBase64Encode(string);
////            String encodecstr = doBase64Encode("259_6013969198_10424178");
//            encodecstr = encodecstr.substring(0, encodecstr.length() - 1);
//            judgeURL(encodecstr);
//        }
        getSuning gsn = new getSuning(259, 259, 10420000, 10429999);
        gsn.sleeptime = 2000;
        new Thread(gsn).start();
//        
//            judgeURL("MjU5XzYwMTM5Njk5OTBfMTA0MjQ5OTA");
    }

    public static String doBase64Encode(String s) {
        String rtnstr = Base64.getEncoder().encodeToString(s.getBytes());
        return rtnstr;
    }

    public static String judgeURL(String string) {
        String encodecstr = doBase64Encode(string);
//            String encodecstr = doBase64Encode("259_6013969198_10424178");
        encodecstr = encodecstr.substring(0, encodecstr.length() - 1);
        String urlstr = String.format("http://mts.suning.com/mts-web/ticket/%s.html", encodecstr);
//        System.out.println(encodecstr);
//        System.out.println(urlstr);
        String content = url2Str(urlstr);
        int position = content.indexOf("面值为：");
        int position2 = content.indexOf("的云券");
        if (position > 1 && position2 > 1) {
//            System.out.println("position"+position);
            if (position2 > 1) {
                String output = (String.format("%1$s : %2$s  ,%3$s", content.substring(position, position2 + 3), urlstr,string));
                System.out.println(output);
                return output;
            } else {
                System.out.println("content.substring(position, position + 5)");
            }

        }
        return null;
//        try {
//            URL url = new URL(urlstr);
//            //返回一个 URLConnection 对象，它表示到 URL 所引用的远程对象的连接。 
////            URLConnection uc = url.openConnection();
//            //打开的连接读取的输入流。 
////            InputStream in = uc.getInputStream();
////            in.close();
//            Reader reader = new InputStreamReader(new BufferedInputStream(url.openStream()));
//            int c;
//            while ((c = reader.read()) != -1) {
//                System.out.print((char) c);
//            }
//            reader.close();
//        } catch (IOException ex) {
//            Logger.getLogger(getSuning.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public static ArrayList<String> produceNo(int a1, int a2, int c1, int c2) {
        //259_6013969198_10424178
        ArrayList<String> sal = new ArrayList<String>();
        StringBuilder sb1 = new StringBuilder();
        for (int a = a1; a <= a2; a++) {
            sb1.append(a).append("_6013969198_");
                for (int c = c1; c <= c2; c++) {
                    sb1.append(c);
                    sal.add(sb1.toString());
                    sb1 = new StringBuilder();
                    sb1.append(a).append("_6013969198_");
                }
                sb1 = new StringBuilder();
        }

        return sal;

    }

    /**
     * 抓取网页内容,自动识别编码
     *
     * @param urlString
     * @return
     */
    public static String url2Str(String urlString) {
        try {
            StringBuffer html = new StringBuffer();
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            URLConnection c = url.openConnection();
            c.connect();
            String contentType = c.getContentType();
            String characterEncoding = null;
            int index = contentType.indexOf("charset=");
            if (index == -1) {
                characterEncoding = "UTF-8";
            } else {
                characterEncoding = contentType.substring(index + 8, contentType.length());
            }
            InputStreamReader isr = new InputStreamReader(conn.getInputStream(), characterEncoding);
            BufferedReader br = new BufferedReader(isr);
            String temp;
            while ((temp = br.readLine()) != null) {
                html.append(temp).append("\n");
            }
            br.close();
            isr.close();
            return html.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void run() {
        FileWriter fw = null;
        try {
            ArrayList<String> sal = produceNo(this.a1, this.a2,  this.c1, this.c2);
            fw = new FileWriter(new File("c:/suning.txt"), true);
            int count = 0;
            for (Iterator<String> it = sal.iterator(); it.hasNext();) {
                count++;
                String string = it.next();

                String outstr = judgeURL(string);
                if (outstr != null) {
                    fw.append(outstr);
                }
                if (count % 10 == 0) {
                    fw.flush();
                }
                try {
                    Thread.sleep(sleeptime);
                } catch (InterruptedException ex) {
                    Logger.getLogger(getSuning.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(getSuning.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.flush();
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(getSuning.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
