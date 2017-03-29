/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.run;

import java.util.HashMap;
import net.labthink.utils.BytePlus;
import net.labthink.utils.FilePlus;

/**
 *
 * @author Moses
 */
public class temptest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        test1();
//        test2();
//        System.out.println("");
//        System.out.println("asda");
        String src = FilePlus.ReadTextFileToString("d:\\aaa.txt");
//        System.out.println(src);
        String[] srclist = src.split("\\s");
        for (int i = 0; i < srclist.length; i++) {
            String string = srclist[i];
            try {
                
                Integer a = Integer.parseInt(string);
               String b = Integer.toHexString(a);
               if(b.length()==1){
                   b="0"+b;
               }
                System.out.print(b);
                System.out.print(" ");
            } catch (Exception e) {
            }
            
            
        }
                
        
    }

    public static void test2() {
        HashMap<Character, Integer> hm = new HashMap<Character, Integer>();
        hm.put('a', 2);
        hm.put('b', 2);
        hm.put('c', 2);
        hm.put('d', 3);
        hm.put('e', 3);
        hm.put('f', 3);
        hm.put('g', 4);
        hm.put('h', 4);
        hm.put('i', 4);
        hm.put('j', 5);
        hm.put('k', 5);
        hm.put('l', 5);
        hm.put('m', 6);
        hm.put('n', 6);
        hm.put('o', 6);
        hm.put('p', 7);
        hm.put('q', 7);
        hm.put('r', 7);
        hm.put('s', 7);
        hm.put('t', 8);
        hm.put('u', 8);
        hm.put('v', 8);
        hm.put('w', 9);
        hm.put('x', 9);
        hm.put('y', 9);
        hm.put('z', 9);

        String input = "liuxuan";

        char[] chars = input.toCharArray();
        StringBuilder output = new StringBuilder(input);
        output.append(":");

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            int ccc = hm.get(c);
            output.append(ccc);
        }
        System.out.println(output);
    }

    public static void test1() {
        // TODO code application logic here
        String input = "liuxuan";
        char[] chars = input.toCharArray();
        int a = 0;
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            a += (c - 'a' + 100);
        }
        System.out.println(a);
    }
}
