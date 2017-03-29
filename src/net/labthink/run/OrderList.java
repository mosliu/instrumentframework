/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.run;

import java.io.File;
import net.labthink.utils.FilePlus;

/**
 *
 * @author Moses
 */
public class OrderList {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        File a = new File("H:/A/a.txt");
        String all =FilePlus.ReadTextFileToStringLn(a);
        String[] sl = all.split("\r\n");
        for (int i = 0; i < sl.length; i++) {
            String string = sl[i];
            if(string.contains("定制程序")&&(!string.endsWith("定制程序"))){
                if(string.charAt(string.length()-3)=='.'||string.charAt(string.length()-4)=='.'||string.charAt(string.length()-5)=='.'){
                    
                }else{
//                    string = string.replaceAll("H:\\A\\", "");
                    int pos_slash_start= string.indexOf("\\");
                    int pos_slash_end= string.lastIndexOf("\\");
                    System.out.println(string.substring(0, pos_slash_start)+"\t\t"+string.substring(0, pos_slash_end)+"\t\t"+string.substring(pos_slash_end+1));
                }
            }
        }
                
    }
}
