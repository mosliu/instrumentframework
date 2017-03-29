/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.instrument.device.intelligent.industrialpc.zigbee;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.labthink.utils.BytePlus;

/**
 *
 * @author Mosliu
 */
public class ZigbeePacket {

    private byte[] zigbeehead = new byte[5];
    private byte[] zigbeebody;
    private byte zigbeetail = 0;
    //禁用默认构造函数。

    private ZigbeePacket() {
    }

    /**
     *
     * @param _packetbody
     */
    public ZigbeePacket(byte[] _packetbody) {
        setZigbeebody(_packetbody);
    }

    public int getLength(){
        return getAllPacket().length;
    }
    
    /**
     * 使用ALCpacket 构建正常包。构建时随机挑选发生通道
     * @param _alcp
     */
    public ZigbeePacket(byte[] head,ALCpacket _alcp) {
        zigbeehead = head;
        setZigbeebody(_alcp.getWholePacket());
        zigbeetail = BytePlus.calcxorwithoutlast(getAllPacket());
        
        
    }
    
    /**
     * 
     * @return
     */
    public byte[] getAllPacket() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            baos.write(zigbeehead);
            baos.write(zigbeebody);
            baos.write(zigbeetail);

        } catch (IOException ex) {
            Logger.getLogger(ZigbeePacket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return baos.toByteArray();
    }

    /**
     *
     * @return
     */
    public boolean checktail() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            baos.write(zigbeehead);
            baos.write(zigbeebody);
        } catch (IOException ex) {
            Logger.getLogger(ZigbeePacket.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte a = BytePlus.calcXor(baos.toByteArray());

        return a == zigbeetail;
    }

    /**
     *
     * @return
     */
    public ALCpacket getALCPacket() {
        byte[] packetsyncbyte = new byte[2];//1
        byte[] devicenum = new byte[5];//2
        byte[] testnum = new byte[4];//3
        byte[] testkind = new byte[2]; //4
        byte[] packetbody = new byte[zigbeebody.length-2-5-4-2-1];//5
        byte tail = zigbeebody[zigbeebody.length-1];//6

        ByteArrayInputStream bais = new ByteArrayInputStream(zigbeebody);
        try {
            bais.read(packetsyncbyte);
            bais.read(devicenum);
            bais.read(testnum);
            bais.read(testkind);
            bais.read(packetbody);
        } catch (IOException ex) {
            Logger.getLogger(ZigbeePacket.class.getName()).log(Level.SEVERE, null, ex);
        }

        ALCpacket rtn = new ALCpacket(devicenum,testnum,testkind,packetbody);
        rtn.setPacketsyncbyte(packetsyncbyte);
        rtn.setTail(tail);
        return rtn;
    }

    /**
     * @return the zigbeehead
     */
    public byte[] getZigbeehead() {
        return zigbeehead;
    }

    /**
     * @param zigbeehead the zigbeehead to set
     */
    public void setZigbeehead(byte[] zigbeehead) {
        this.zigbeehead = zigbeehead;
    }

    /**
     * @return the zigbeebody
     */
    public byte[] getZigbeebody() {
        return zigbeebody;
    }

    /**
     * @param zigbeebody the zigbeebody to set
     */
    public void setZigbeebody(byte[] zigbeebody) {
        this.zigbeebody = zigbeebody;
    }

    /**
     * @return the zigbeetail
     */
    public byte getZigbeetail() {
        return zigbeetail;
    }

    /**
     * @param zigbeetail the zigbeetail to set
     */
    public void setZigbeetail(byte zigbeetail) {
        this.zigbeetail = zigbeetail;
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        byte a = -11;
        int b= a&0xff;
        System.out.println(b);
    }
}
