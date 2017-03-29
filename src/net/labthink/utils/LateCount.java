/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Mosliu
 */
public class LateCount {

    public static void main(String[] args) {
        String intext = FilePlus.ReadTextFileToStringLn("d:/a.ini").replaceAll("：", ":");

        String pstr = "([\u4e00-\u9fa5]+\\s*\\d+(：|:)\\d+)";
//        System.out.println(intext);
        Map<String, ArrayList> latemap = new HashMap<String, ArrayList>();

        Pattern p = Pattern.compile(pstr);
        Matcher m = p.matcher(intext);

        int count = 0;
        int count2 = 0;
        while (m.find()) {
            String a = m.group();
            String sl[] = a.split("\\s+");
            if (sl.length != 2) {
                System.out.println(a);
            }
            if (latemap.containsKey(sl[0])) {
                ArrayList latelist = (ArrayList) latemap.get(sl[0]);
                latelist.add(sl[1]);
            } else {
                ArrayList latelist = new ArrayList();
                latelist.add(sl[1]);
                latemap.put(sl[0], latelist);
            }

            count++;
        }
        System.out.println(count);
        Iterator iter = latemap.entrySet().iterator();
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();

        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            ArrayList val = (ArrayList) entry.getValue();
            int t755 = 0;
            int t800 = 0;
            for (Iterator it = val.iterator(); it.hasNext();) {
                String time = ((String) it.next()).replaceAll(":|：|\\s", "");
                int a = Integer.parseInt(time);
                if (a >= 755) {
                    t755++;
                }
                if (a >= 800) {
                    t800++;
                }
            }
            sb1.append(key + "迟到" + val.size() + "次;");
            sb1.append("超过7：55有" + t755 + "次;" + "超过8：00有" + t800 + "次\r\n");
            sb2.append(key + "," + val.size() + ",");
            sb2.append("" + t755 + "," + "" + t800 + "\r\n");
            count2 += val.size();
        }
        System.out.println(sb1);
        System.out.println(sb2);
//        System.out.println(count2);
//        System.out.println(m.find() ? m.group(1) : "nothing");
//        System.out.println("7：53">"7：55");


    }
}
