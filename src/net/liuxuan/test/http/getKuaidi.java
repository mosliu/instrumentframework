/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.liuxuan.test.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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
public class getKuaidi implements Runnable {

    int startno;
    int endno;
    public long sleeptime = 1000;

    public getKuaidi(int a1, int a2) {
        this.startno = a1;
        this.endno = a2;
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
        getKuaidi gsn = new getKuaidi(1800, 2000);
        gsn.sleeptime = 500;
        new Thread(gsn).start();
//        
//            judgeURL("MjU5XzYwMTM5Njk5OTBfMTA0MjQ5OTA");
    }

    public static String doBase64Encode(String s) {
        String rtnstr = Base64.getEncoder().encodeToString(s.getBytes());
        return rtnstr;
    }

    public static String judgeURL(String string) {
//        String encodecstr = doBase64Encode(string);
//            String encodecstr = doBase64Encode("259_6013969198_10424178");
//        encodecstr = encodecstr.substring(0, encodecstr.length() - 1);
        String urlstr = String.format("http://c2.kuaidadi.com/taxi/web/p/random_index.htm?activityId=%s", string);
        System.out.println(string);
//        System.out.println(encodecstr);
//        System.out.println(urlstr);
        String content = url2Str(urlstr);
        int position = content.indexOf("<title></title>");
        if (position > 1) {
            return null;
        }
        int position2 = content.indexOf("<title>");
        int position3 = content.indexOf("</title>");
        if(position2<0&&position3<0){
            return null;
        }
        String output = (String.format("%1$s : %2$s  ,%3$s", content.substring(position2, position3 + 8), urlstr, string));
        System.out.println(output);
        return output;
    }

    public static ArrayList<String> produceNo(int start, int end) {
        //259_6013969198_10424178
        ArrayList<String> sal = new ArrayList<String>();
        StringBuilder sb1 = new StringBuilder();

        for (int a = start; a <= end; a++) {
            sal.add("" + a);
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
            ArrayList<String> sal = produceNo(this.startno, this.endno);
            fw = new FileWriter(new File("c:/kuaidi.txt"), true);
            int count = 0;
            for (Iterator<String> it = sal.iterator(); it.hasNext();) {
                count++;
                String string = it.next();
                String outstr = judgeURL(string);
                if (outstr != null) {
                    fw.append(outstr).append("\r\n");
                    fw.flush();
                }
//                if (count % 10 == 0) {
//                    fw.flush();
//                }
                try {
                    Thread.sleep(sleeptime);
                } catch (InterruptedException ex) {
                    Logger.getLogger(getKuaidi.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(getKuaidi.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.flush();
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(getKuaidi.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
