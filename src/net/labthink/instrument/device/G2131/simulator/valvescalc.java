/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.labthink.instrument.device.G2131.simulator;

import net.labthink.utils.BytePlus;

/**
 *
 * @author Moses
 */
public class valvescalc {
    public static void main(String[] args) {
        int k = 0x0669;
        byte[] bs = BytePlus.int2bytes(k);
        //System.out.println(Integer.parseInt("0f",16));
        System.out.println(Integer.toBinaryString(k));
//        System.out.println((int)(bs[3]));
//        System.out.println((int)(bs[3]&0x03));
        if((bs[3]&0x03)==0x01){
            System.out.println("一号阀：开");
        }else{
            System.out.println("一号阀：关");
        }
        if((bs[3]&0x0C)==0x04){
            System.out.println("二号阀：开");
        }else{
            System.out.println("二号阀：关");
        }
        if((bs[3]&0x30)==0x10){
            System.out.println("三号阀：开");
        }else{
            System.out.println("三号阀：关");
        }
        if((bs[3]&0x40)==0x40){
            System.out.println("四号阀：开");
        }else{
            System.out.println("四号阀：关");
        }
        if((bs[3]&0x80)==0x80){
            System.out.println("五号阀：开");
        }else{
            System.out.println("五号阀：关");
        }
        if((bs[2]&0x01)==0x01){
            System.out.println("六号阀：开");
        }else{
            System.out.println("六号阀：关");
        }
        if((bs[2]&0x02)==0x02){
            System.out.println("七号阀：开");
        }else{
            System.out.println("七号阀：关");
        }
        if((bs[2]&0x04)==0x04){
            System.out.println("八号阀：开");
        }else{
            System.out.println("八号阀：关");
        }
        if((bs[2]&0x08)==0x08){
            System.out.println("九号阀：开");
        }else{
            System.out.println("九号阀：关");
        }
        if((bs[2]&0x10)==0x10){
            System.out.println("十号阀：开");
        }else{
            System.out.println("十号阀：关");
        }
    }
}
