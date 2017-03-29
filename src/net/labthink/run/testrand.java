/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.run;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.labthink.utils.FilePlus;

/**
 *
 * @author Moses
 */
public class testrand {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        test001();
        File  f1 = new File("d:\\temp\\123.log");
        String a = FilePlus.ReadTextFileToString(f1,"\r\n","gb2312");
//        System.out.println(a.length());
        String[] alist = a.split("\r\n");
//        System.out.println(alist.length);
        
        for (int i = 0; i < alist.length; i++) {
//        for (int i = 0; i < 10; i++) {
            String string = alist[i];
            int start = string.indexOf("试验编号:");
            int end = string.indexOf("实验类型:");
            if(start>0){
                if (end<0) {
                    end = start+4;
                }
                String aa = string.substring(start+5, end);//+5表示将试验编号： 这5个字符去除
//                System.out.println(aa);
                FilePlus.appendText("d:/temp/seperated", aa+".txt", string+"\r\n");
            }
            
        }
        
        
        
    }

    public static void test001() {
        int ks[] = {2,3,5,07,8,4,10,15,13,11,19,9,1,17,12,6,14,16,18,0};
        int t =1;
        for (int i = 0; i < 11; i++) {
            t=i%4+1;
            for (int j = 0; j < ks.length; j+=t) {
                int k = ks[j];     
                System.out.println(k);
            }
                        
        }
        // TODO code application logic here
    }
}
