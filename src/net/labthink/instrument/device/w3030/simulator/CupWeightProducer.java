/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.labthink.instrument.device.w3030.simulator;

import java.io.Serializable;
import java.util.Random;

/**
 *
 * @author Moses
 */
public class CupWeightProducer implements Serializable {
    int initvalue = 360000;
    int current = initvalue;
    Random rand = new Random(System.currentTimeMillis());
//    public static transient int[] weightlist = {356723,356621,356520,356415,356313,356212,356110,355709};
    public static transient int[] weightlist = {362713,362711,362709,362707,362706,362704,362702,362700};
    public static transient int transrate = 60  ;
    public int times =0;
    public int getWeightList(int testgap){
        times++;

        if(times<weightlist.length*60/transrate){
            current = weightlist[(times/(testgap*60/transrate))%weightlist.length];


        }else{
            current -= rand.nextInt(5)+1;
        }
        System.out.println("当前："+current);
        return current;
    }

    public int getWeightRandom(int testgap){
        return getWeightRandom(testgap,1,5);
    }
    public int getWeightRandom(int testgap,int randstart,int randend){
        if(times%(testgap*60/transrate)==0){
            current -= randstart;
            current -= rand.nextInt(randend-randstart);
        }
        times++;
        return current;
    }

    public int getWeightRandom2(int testgap,int randstart,int randend){

            current -= randstart;
            current -= rand.nextInt(randend-randstart);

        times++;
        return current;
    }


    public byte getWeightFlag(int testgap){
         if(times%(testgap*(80/transrate))==0){
             return 1;
         }else{
             return 2;
         }
    }

    public static void main(String[] args) {
        for (int i = 0; i < weightlist.length; i++) {
            int j = weightlist[i];
            System.out.println(j);

        }
    }

}
