/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.run;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import net.labthink.utils.FilePlus;

/**
 *
 * @author Moses
 */
public class worklogtime {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String instr = FilePlus.ReadTextFileToString(new File("d:/temp.log"),"\r\n","GBK");
        instr=instr.replaceAll("杨成芹", "杨成芹 ").replaceAll("冯静芳", "冯静芳 ").replaceAll("申亚博", "申亚博 ").replaceAll("太荣兵", "太荣兵 ").replaceAll("朱军", "朱军 ");
        while(instr.contains("  ")){
            instr = instr.replaceAll("  ", " ");
        }
        String[] strl = instr.split("\r\n");
        HashMap<String, Float> hm = new HashMap<String, Float>();
        for (int i = 0; i < strl.length; i++) {
            String string = strl[i].trim();
            String tokens[] = string.split(" ");
            if (tokens.length > 1) {
                try {
                    float tmpint = Float.parseFloat(tokens[1].replaceAll("工时", "").trim());
                    if (hm.containsKey(tokens[0])) {
                        float a = hm.get(tokens[0]);
                        hm.put(tokens[0], a + tmpint);
                    }else{
                        hm.put(tokens[0], tmpint);
                    }
                    
                } catch (Exception e) {
                    System.out.println(string);
                }


            } else {
                System.out.println(string);
            }
        }
        System.out.println("统计结果：====================================");
        float total = 0;
        for (Map.Entry<String, Float> entry : hm.entrySet()) {
            String string = entry.getKey();
            Float ft = entry.getValue();
            System.out.println(string+":"+ft);
            total += ft;
            
        }
        System.out.println("总共："+total);

    }
}
