/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.labthink.instrument.device.Fluke45.packet;

import java.io.Serializable;

/**
 *
 * @author Moses
 */
public class Fluke45Pkt implements Serializable{
    private byte[] pktbyte;
    
    public byte[] getAll(){
        return pktbyte;
    }
    
    public void setPkt(byte[] content){
        pktbyte = content;
    }

    @Override
    public String toString() {
        
        return new String(pktbyte);
    }
    
    
    
}
