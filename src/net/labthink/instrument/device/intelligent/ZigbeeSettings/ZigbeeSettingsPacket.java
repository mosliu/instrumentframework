/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.instrument.device.intelligent.ZigbeeSettings;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.labthink.utils.BytePlus;

/**
 *
 * @author Mosliu
 */
public class ZigbeeSettingsPacket {

    /**
     * 变量
     */
    public static final int zigbeeheadlength = 5;
    private byte[] zigbeehead = new byte[5];
    private byte[] zigbeebody;
    private byte[] zigbeetail = {0};
    private static int[] sendflags = new int[16];
    private static int[] kind86no = new int[16];
    private static long[] kind86time = new long[16];
    //禁用默认构造函数。
    
    private boolean restartFlag = false;
    /**
     * 命令请求
     */
    public static final byte ZigbeeSettingsHead_CommandASK = (byte) 0x80;//命令请求
    /**
     * 命令应答
     */
    public static final byte ZigbeeSettingsHead_CommandASKRESPONSE = (byte) 0xC0;//命令应答
    /**
     * 数据请求
     */
    public static final byte ZigbeeSettingsHead_DATASEND = (byte) 0xA0;//数据请求
    /**
     * ACK应答
     */
    public static final byte ZigbeeSettingsHead_ACK = (byte) 0xE0;//ACK应答

    public static void initstatic() {
        for (int i = 0; i < sendflags.length; i++) {
            sendflags[i] = 0;
            kind86no[i] = 0;
            kind86time[i] = 0;
        }

    }

    /**
     * 构造函数
     */
    private ZigbeeSettingsPacket() {
    }

    /**
     * 生成一个新帧，
     *
     * @param _packetbody
     */
    public ZigbeeSettingsPacket(byte[] _packetbody) {
        setZigbeebody(_packetbody);
    }

    /**
     *
     * @param kind
     * @param rssi
     * @param addr
     * @param content
     * @return
     */
    public synchronized static ZigbeeSettingsPacket madeZigbeeSettingsPacket(byte kind, int rssi, int addr, byte[] content) {
        ZigbeeSettingsPacket pkt = new ZigbeeSettingsPacket();
        pkt.zigbeehead[0] = (byte) ((getMinSendflag() & 0x0F) | kind);
        pkt.setDestRssi(rssi);
        pkt.setDestAddr(addr);
        if (content == null) {
            pkt.zigbeebody = null;
        } else {
            pkt.zigbeebody = content;
        }
        pkt.setBodyRealLength();


        pkt.recalcZigbeetail();

        //默认发给自己

        return pkt;
    }

    /**
     * 构造一个默认包
     *
     * @param kind 包类型
     * @return
     */
    public static ZigbeeSettingsPacket getDefaultPacket(byte kind) {
        //默认发给自己
        return madeZigbeeSettingsPacket(kind, 0, 0xFFFE, null);
//        ZigbeeSettingsPacket pkt= new ZigbeeSettingsPacket();
//        pkt.zigbeehead[0]=(byte) getMinSendflag();
//        pkt.setDestRssi(0);
//        pkt.setDestAddr(E);
//        pkt.zigbeebody = null;


//        return pkt;
    }

    /**
     * 构造一个默认数据请求包
     *
     * @return
     */
    public static ZigbeeSettingsPacket getDefaultPacket() {

        return getDefaultPacket(ZigbeeSettingsHead_DATASEND);
    }

    /**
     * 获取版本信息
     *
     * @return
     */
    public static ZigbeeSettingsPacket getVersionPacket() {
        ZigbeeSettingsPacket pkt = getDefaultPacket(ZigbeeSettingsHead_CommandASK);
//        byte[] pktbody = new byte[1];
//        pktbody[0] = (byte) 0x8C;
        byte[] pktbody = BytePlus.hexStringToBytes("8C");
        pkt.setZigbeebody(pktbody);
        return pkt;
    }

    /**
     * 获取版本信息
     *
     * @return
     */
    public static ZigbeeSettingsPacket getSoftRestartPacket() {
        ZigbeeSettingsPacket pkt = getDefaultPacket(ZigbeeSettingsHead_CommandASK);
//        byte[] pktbody = new byte[1];
//        pktbody[0] = (byte) 0x8C;
        byte[] pktbody = BytePlus.hexStringToBytes("8F");
        pkt.setZigbeebody(pktbody);
        return pkt;
    }

    /**
     * 获取版本信息
     *
     * @return
     */
    public static ZigbeeSettingsPacket getResetFactoryPacket() {
        ZigbeeSettingsPacket pkt = getDefaultPacket(ZigbeeSettingsHead_CommandASK);
//        byte[] pktbody = new byte[1];
//        pktbody[0] = (byte) 0x8C;
        byte[] pktbody = BytePlus.hexStringToBytes("90");
        pkt.setZigbeebody(pktbody);
        return pkt;
    }
    /**
     * 获取路由信息
     *
     * @return
     */
    public static ZigbeeSettingsPacket getRouteQueryPacket(int dest) {
        ZigbeeSettingsPacket pkt = getDefaultPacket(ZigbeeSettingsHead_CommandASK);
//        byte[] pktbody = new byte[1];
//        pktbody[0] = (byte) 0x8C;
        byte[] destid = BytePlus.short2bytes((short)(dest&0xFFFF));
        byte[] pktbody = new byte[3];
        pktbody[0] = (byte) 0xAA;
        pktbody[1] = destid[0];
        pktbody[2] = destid[1];
        
        pkt.setZigbeebody(pktbody);
        return pkt;
    }
    /**
     * 获取路由信息RSSI
     *
     * @return
     */
    public static ZigbeeSettingsPacket getRouteQueryRSSIPacket(int dest) {
        ZigbeeSettingsPacket pkt = getDefaultPacket(ZigbeeSettingsHead_CommandASK);
//        byte[] pktbody = new byte[1];
//        pktbody[0] = (byte) 0x8C;
        byte[] destid = BytePlus.short2bytes((short)(dest&0xFFFF));
        byte[] pktbody = new byte[3];
        pktbody[0] = (byte) 0xBB;
        pktbody[1] = destid[0];
        pktbody[2] = destid[1];
        
        pkt.setZigbeebody(pktbody);
        return pkt;
    }

    /**
     * 获取路由信息RSSI
     *
     * @return
     */
    public static ZigbeeSettingsPacket getSendTextPacket(int dest,String content) {
        content = content==null?"":content;
        byte[] body = null;
        try {
            body = content.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ZigbeeSettingsPacket.class.getName()).log(Level.SEVERE, null, ex);
        }
        ZigbeeSettingsPacket pkt = madeZigbeeSettingsPacket(ZigbeeSettingsHead_DATASEND,0,dest,body);
        
        return pkt;
    }
    
    /**
     * 退透传
     *
     * @return
     */
    public static ZigbeeSettingsPacket getExitTransprantPacket() {
        ZigbeeSettingsPacket pkt = new ZigbeeSettingsPacket();
        pkt.zigbeehead = null;
        pkt.zigbeebody = new byte[3];
        int t = '=';
        pkt.zigbeebody[0] = (byte) t;
        pkt.zigbeebody[1] = (byte) t;
        pkt.zigbeebody[2] = (byte) t;
        pkt.zigbeetail = null;
        return pkt;
    }
    
    /**
     * 进透传
     *
     * @return
     */
    public static ZigbeeSettingsPacket getEnterTransprantPacket(int destid) {
        ZigbeeSettingsPacket pkt = new ZigbeeSettingsPacket();
        String dest = Integer.toString(destid);
        pkt.zigbeehead = null;
        pkt.zigbeebody = new byte[4+dest.length()];
        int t = '-';
        pkt.zigbeebody[0] = (byte) t;
        pkt.zigbeebody[1] = (byte) t;
        pkt.zigbeebody[2] = (byte) t;
        pkt.zigbeebody[pkt.zigbeebody.length-1] = (byte) t;
        for (int i = 0; i < dest.length(); i++) {
            t= dest.charAt(i);
            pkt.zigbeebody[3+i] =(byte) t;
        }
        pkt.zigbeetail = null;
        return pkt;
    }

    /**
     * 获取寄存器信息
     *
     * @param regid
     * @return
     */
    public static ZigbeeSettingsPacket getRegisterPacket(int regid) {
        ZigbeeSettingsPacket pkt = getDefaultPacket(ZigbeeSettingsHead_CommandASK);
//        byte[] pktbody = new byte[1];
//        pktbody[0] = (byte) 0x8C;
        byte[] pktbody = BytePlus.hexStringToBytes("86" + " " + BytePlus.toHexString(regid));
        pkt.setZigbeebody(pktbody);
        return pkt;
    }

    /**
     * 获取寄存器信息
     *
     * @param regid
     * @return
     */
    public static ZigbeeSettingsPacket getRegisterSetPacket(int regid, int setvalue) {
        ZigbeeSettingsPacket pkt = getDefaultPacket(ZigbeeSettingsHead_CommandASK);
//        byte[] pktbody = new byte[1];
//        pktbody[0] = (byte) 0x8C;
        byte[] pktbody = BytePlus.hexStringToBytes("87" + " " + BytePlus.toHexString(regid) + " " + BytePlus.toHexString(setvalue));
        pkt.setZigbeebody(pktbody);
        return pkt;
    }

    //================================================以下为方法函数 ================================================
    /**
     *
     * @return
     */
    public int getZigbeeSettingsPacketType() {
        byte C = zigbeehead[0];
        if ((C & 0x10) == 0x00) {
            C = (byte) (C >>> 4);
            C = (byte) (C & 0x0f);
            if (C == 8) {
                return 1;
            }
            if (C == 0x0C) {
                return 2;
            }
            if (C == 0x0A) {
                return 3;
            }
            if (C == 0x0E) {
                return 4;
            }
        }
        return 0;
    }

    /**
     * 返回当前的发送监视量.
     *
     * @return
     */
    public static int getMinSendflag() {
        int minno = -1;
        int minvalue = Integer.MAX_VALUE;
        for (int i = 0; i < sendflags.length; i++) {
            if (getRegister(i) > -1) {
                continue;//这个slot有数据在用，则跳过
            }
            if (minvalue > sendflags[i]) {
                minno = i;
                minvalue = sendflags[i];
            }
        }
        if (minno == -1) {
            System.out.println("Error：当前无可用slot，等待200ms");
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(ZigbeeSettingsPacket.class.getName()).log(Level.SEVERE, null, ex);
            }
            return getMinSendflag();
        } else {
            return minno;
        }
    }

    /**
     * 检查校验字段是否正确
     *
     * @return
     */
    public boolean checktail() {
        byte a = calcXor();

        return a == zigbeetail[0];
    }

    /**
     * 计算抑或校验值
     *
     * @return
     */
    public byte calcXor() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            if (zigbeehead != null) {
                baos.write(zigbeehead);
            }
            if (zigbeebody != null) {
                baos.write(zigbeebody);
            }
        } catch (IOException ex) {
            Logger.getLogger(ZigbeeSettingsPacket.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte a = BytePlus.calcXor(baos.toByteArray());
        return a;
    }
    // ===============================================以下为getter 以及 setters================================================

    /**
     * 返回当前的发送监视量.
     *
     * @return
     */
    public int[] getsendflags() {
        return sendflags;
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
     * 获取使用哪个发送槽
     *
     * @return 返回使用的槽
     */
    public int getPacketSlot() {
        byte a = zigbeehead[0];
        return (a & 0x0f);
    }

    /**
     * 设置zigbeebody内容，并更新包头及重新计算校验和
     *
     * @param zigbeebody the zigbeebody to set
     */
    public void setZigbeebody(byte[] zigbeebody) {
        this.zigbeebody = zigbeebody;
        setBodyRealLength();
        recalcZigbeetail();

    }

    /**
     * @return the zigbeetail
     */
    public byte[] getZigbeetail() {
        return zigbeetail;
    }

    /**
     * @param zigbeetail the zigbeetail to set
     */
    public void setZigbeetail(byte zigbeetail) {
        this.zigbeetail[0] = zigbeetail;
    }

    /**
     * 重计算zigbee尾巴
     *
     */
    public void recalcZigbeetail() {
        this.zigbeetail[0] = calcXor();
    }

    /**
     * 设置负载长度
     *
     * @param length
     */
    public void setBodyLength(int length) {
        this.zigbeehead[4] = (byte) length;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        if (zigbeehead != null) {
            b.append("head:").append(BytePlus.byteArray2String(zigbeehead)).append("，");
        }
        if (zigbeebody != null) {
            b.append("body:").append(BytePlus.byteArray2String(zigbeebody)).append("，");
        }
        if (zigbeetail != null) {
            b.append("tail:").append(BytePlus.byteArray2String(zigbeetail)).append("，");
        }
//        b.append("tail:").append(BytePlus.toHexString(zigbeetail)).append("\r\n");
        return b.toString();
    }

    // ===============================================以下为getter 以及 setters的补充================================================
    /**
     *
     * @param i
     */
    public static void useflag(byte i) {
        byte b = (byte) (i & 0x0f);
        sendflags[b]++;
    }

    /**
     *
     * @param i
     */
    public static void releaseflag(byte i) {
        byte b = (byte) (i & 0x0f);
        sendflags[b]--;
    }

    /**
     * 设置RSSI
     *
     * @param rssi 0-255
     */
    public void setDestRssi(int rssi) {
        this.zigbeehead[2] = (byte) rssi;
    }

    /**
     *
     */
    public void setBodyRealLength() {
        setBodyLength(getBodyRealLength());
    }

    /**
     *
     * @return
     */
    public byte getBodyRealLength() {
        if (zigbeebody == null) {
            return 0;
        }
        return (byte) zigbeebody.length;
    }

    /**
     * 读寄存器状态
     *
     * @param slot 格子号
     * @param no 寄存器号
     * @param endtime 存活时间
     */
    public static void setRegister(int slot, int no, long endtime) {
        kind86no[slot] = no;
        kind86time[slot] = System.currentTimeMillis() + endtime;
    }

    /**
     *
     * @param slot
     * @return
     */
    public static int getRegister(int slot) {
        long endtime = System.currentTimeMillis();
        if (endtime > kind86time[slot]) {
            //使用打印需考虑-1情况
//            System.out.println("curr:"+endtime);
//            System.out.println("set:"+kind86time[slot]);
            releaseRegister(slot);
            return -1;
        } else {
            int no = kind86no[slot];
            return no;
        }
    }

    public static void releaseRegister(int slot) {
        //超时
        kind86time[slot] = -1;
        kind86no[slot] = -1;
    }

    /**
     * 设置目的地址
     *
     * @param dest 地址，short型
     */
    public void setDestAddr(short dest) {
        byte[] destbytes = BytePlus.short2bytes(dest);
//        System.out.println(BytePlus.bytesToHexString(destbytes));
        this.zigbeehead[2] = destbytes[0];
        this.zigbeehead[3] = destbytes[1];
    }

    /**
     * 设置目的地址
     *
     * @param dest 地址，int型
     */
    public void setDestAddr(int dest) {
        byte[] destbytes = BytePlus.int2bytes(dest);
        this.zigbeehead[2] = destbytes[2];
        this.zigbeehead[3] = destbytes[3];
//        for (int i = 0; i < destbytes.length; i++) {
//            byte b = destbytes[i];
//            System.out.println(i+":"+b);
//        }
//        System.out.println(BytePlus.bytesToHexString(destbytes));
    }

    /**
     * 得到目的地址
     *
     * @param dest 地址，int型
     */
    public int getDestAddr() {
        int dest = -1;
        byte[] destbytes = new byte[2];
        destbytes[0] = this.zigbeehead[2];
        destbytes[1] = this.zigbeehead[3];
        dest = BytePlus.bytes2short(destbytes);
        return dest;
    }
    
    /**
     *
     * @return
     */
    public int getLength() {
        return getAllPacket().length;
    }

    /**
     *
     * @return
     */
    public byte[] getAllPacket() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            if (zigbeehead != null) {
                baos.write(zigbeehead);
            }
            if (zigbeebody != null) {
                baos.write(zigbeebody);
            }
            if (zigbeetail != null) {
                baos.write(zigbeetail);
            }

        } catch (IOException ex) {
            Logger.getLogger(ZigbeeSettingsPacket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return baos.toByteArray();
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
//        byte a = -11;
//        int b= a&0xff;
//        System.out.println(b);
//        ZigbeeSettingsPacket a = ZigbeeSettingsPacket.getDefaultPacket();
//        a.getAllPacket();
//                a.setDestAddr(333);
//        System.out.println(0xFFFE);
//        byte[] a = BytePlus.hexStringToBytes("8c");
//        for (int i = 0; i < a.length; i++) {
//            byte b = a[i];
//            System.out.println(i + ":" + b);
//        }
//        int k = 0xA0;
//        System.out.println(k);
//        k = k >>> 4;
//        System.out.println(k);

//        byte a = (byte) 0xe8;
//        System.out.println(a);
//        System.out.println(a & 0x0f);
        
        byte[] destid = BytePlus.short2bytes((short)9999);
        for (int i = 0; i < destid.length; i++) {
            byte b = destid[i];
            System.out.println(b);
        }

    }

    /**
     * @return the restartFlag
     */
    public boolean isRestartFlag() {
        return restartFlag;
    }

    /**
     * @param restartFlag the restartFlag to set
     */
    public void setRestartFlag(boolean restartFlag) {
        this.restartFlag = restartFlag;
    }
}
