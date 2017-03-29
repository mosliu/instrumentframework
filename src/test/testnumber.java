/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.BitSet;
import net.labthink.utils.FilePlus;

/**
 *
 * @author Moses
 */
public class testnumber {

    public static void main2(String[] args) {
        String a = FilePlus.ReadTextFileToStringLn("d:/number.txt");
        String aa[] = a.split("\\s+");
        System.out.println(aa.length);
//        for (int i = 0; i < 30; i++) {
        for (int i = 0; i < aa.length; i++) {
            String string = aa[i];
            if (string.matches("(19|20)[0-9]{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])")) {
                System.out.println(string);
            }
        }
    }

    public static void main(String[] s) {
//        int n = 17;
        int n = 100000000;
       computePrime(100);
       computePrime(200);
       computePrime(500);
    }

    public static void computePrime(int n){
         long start = System.currentTimeMillis();
        BitSet b = new BitSet(n + 1);
//        int count = 0;
        int i;
        for (i = 2; i <= n; i++) {
            b.set(i);
        }
        i = 2;
        while (i * i <= n) {
            if (b.get(i)) {
//                count++;
                int k = 2 * i;
                while (k <= n) {
                    b.clear(k);
                    k += i;
                }
            }
            i++;
        }
//        while (i <= n) {
//            if (b.get(i)) {
//                count++;
//            }
//            i++;
//        }
        long end = System.currentTimeMillis();
//        System.out.println(count + " primes");
        System.out.println(b.cardinality());
//        System.out.println(b.cardinality()/n*100+"%");
        System.out.println((end - start) + " milliseconds");
    }
}
