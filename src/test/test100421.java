/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import net.labthink.utils.BytePlus;

/**
 *
 * @author Moses
 */
public class test100421 {
    public static void main(String[] args) {
        byte[] b = BytePlus.hexStringToBytes("91B7FC44");
        Float a = BytePlus.littleEndianbBytesToFloat(b);
        Integer i = BytePlus.littleEndianbBytesToInt(b);
        Double d = Double.longBitsToDouble(i);
        
        System.out.println(d);
        
    }
}
