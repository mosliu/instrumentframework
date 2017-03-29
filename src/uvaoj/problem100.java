/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uvaoj;

/**
 * 100 - The 3n + 1 problem
 * @author Mosliu
 */
public class problem100 {

    public static int judgenumber(int n) {
        if (n < 1) {
            return 0;
        }
        int count = 1;
        while (n != 1) {
//            System.out.println(n);
            if ((n % 2) == 0) {
                n = n >> 1;
            } else {
                n = 3 * n + 1;
            }
            count++;
        }
        return count;
    }

    public static void judgemax(int start,int end){
        int maxcount = 0;
        int max = start>end?start:end;
        int min = start<end?start:end;
         for (int i = min; i <= max; i++) {
            int curr = judgenumber(i);
            if (curr > maxcount) {
                maxcount = curr;
            }
        }
        System.out.println(start+" "+end+" "+maxcount);
    }

    public static void main(String[] args) {
        judgemax(100,200);
//        judgenumber(22);
    }
}
