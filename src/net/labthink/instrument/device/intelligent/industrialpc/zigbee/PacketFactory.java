/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.instrument.device.intelligent.industrialpc.zigbee;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.labthink.utils.BytePlus;
import net.labthink.utils.Randomer;

/**
 *
 * @author Mosliu
 */
public class PacketFactory {

    static Randomer r = new Randomer();

    public static ALCpacket ALCStartMechine() {
        byte[] kind = {0};
        byte[] out = BytePlus.assembleBytes(kind, DataFactory.ByteRandomDateProducer(), DataFactory.ByteRandomDateProducer());
//        System.out.println("包体" + BytePlus.bytesToHexString(out));
        ALCpacket pkt = new ALCpacket(out);
//        System.out.println("全部" + BytePlus.bytesToHexString(pkt.getWholePacket()));
        return pkt;
    }

    public static ALCpacket ALCTestStart() {
        byte[] kind = {1};
        byte[] out = BytePlus.assembleBytes(kind, DataFactory.ByteRandomDateProducer());
//        System.out.println("包体" + BytePlus.bytesToHexString(out));
        ALCpacket pkt = new ALCpacket(out);
//        System.out.println("全部" + BytePlus.bytesToHexString(pkt.getWholePacket()));
        return pkt;
    }

    public static ALCpacket ALCTestStop() {
        byte[] kind = {2};
        byte[] out = BytePlus.assembleBytes(kind, DataFactory.ByteRandomDateProducer());
//        System.out.println("包体" + BytePlus.bytesToHexString(out));
        ALCpacket pkt = new ALCpacket(out);
//        System.out.println("全部" + BytePlus.bytesToHexString(pkt.getWholePacket()));
        return pkt;
    }

    public static ALCpacket ALCShortParams() {
        byte[] kind = {3};

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int length = 0;
        int count = 0;
        try {
            byte[] conts = DataFactory.ByteShortParameterProducer();
            while (length + conts.length < 70 | length == 0) {
                length += conts.length;
                count++;
                baos.write(conts);
                conts = DataFactory.ByteShortParameterProducer();
            }
        } catch (IOException ex) {
            Logger.getLogger(PacketFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] counts = new byte[1];
        counts[0]= (byte) count;

        byte[] out = BytePlus.assembleBytes(kind, counts, baos.toByteArray());
//        System.out.println("包体" + BytePlus.bytesToHexString(out));
        ALCpacket pkt = new ALCpacket(out);
//        System.out.println("全部" + BytePlus.bytesToHexString(pkt.getWholePacket()));
        return pkt;
    }

    public static ALCpacket[] ALCLongParams() {
        return ALCLongParams(10000);
    }
    public static ALCpacket[] ALCLongParams(int lengthmax) {
        byte[] kind = {4};
        int totallength = r.nextInt(100, lengthmax);
        ALCpacket[] pkts = new ALCpacket[totallength / 70 + 1];

        byte[] _devicenum = DataFactory.DeviceNumProducer();
        byte[] _testnum = DataFactory.TestNumProducer();
        byte[] _testkind = DataFactory.TestKindProducer();
        byte paramnum = (byte) r.nextInt(0, 255);


        for (int i = 0; i < pkts.length; i++) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                baos.write(kind);
                baos.write(paramnum);//参数号
                baos.write(pkts.length);//分段量
                baos.write(i);//段号
                int seglength = (i < pkts.length - 1) ? 70 : (totallength % 70);
                baos.write(seglength);//本段长
                for (int j = 0; j < seglength; j++) {
                    baos.write(r.nextInt(0, 255));
                }
            } catch (IOException ex) {
            }
            ALCpacket pkt = new ALCpacket(_devicenum, _testnum, _testkind, baos.toByteArray());
            pkts[i] = pkt;
        }

        return pkts;
    }

    public static ALCpacket ALCRealDataAll() {
        byte[] kind = {5};
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int switchbyte = r.nextInt(0, 1);
        int count = r.nextInt(1, 16);

        try {
            baos.write(DataFactory.ByteDateProducer());
            baos.write(switchbyte);
            if (switchbyte == 1) {
                r.resetInt();
                baos.write(BytePlus.int2bytes(Integer.reverseBytes(r.nextInt())));
            }
            baos.write(count);
            for (int i = 0; i < count; i++) {
                baos.write(BytePlus.int2bytes(Integer.reverseBytes(r.nextInt())));
            }

        } catch (IOException ex) {
            Logger.getLogger(PacketFactory.class.getName()).log(Level.SEVERE, null, ex);
        }



        byte[] out = BytePlus.assembleBytes(kind, baos.toByteArray());
//        System.out.println("包体" + BytePlus.bytesToHexString(out));
        ALCpacket pkt = new ALCpacket(out);
//        System.out.println("全部" + BytePlus.bytesToHexString(pkt.getWholePacket()));
        return pkt;
    }

    public static ALCpacket ALCRealDataSpecial() {
        byte[] kind = {6};
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int switchbyte = r.nextInt(0, 1);
        int count = r.nextInt(1, 13);

        try {
            baos.write(DataFactory.ByteDateProducer());



            baos.write(switchbyte);
            if (switchbyte == 1) {
                r.resetInt();
                baos.write(BytePlus.int2bytes(Integer.reverseBytes(r.nextInt())));
            }

            baos.write(count);
            for (int i = 0; i < count; i++) {
                baos.write(r.nextInt(0, 255));//4.	数值编号
                r.resetInt();
                baos.write(BytePlus.int2bytes(Integer.reverseBytes(r.nextInt())));
            }

        } catch (IOException ex) {
            Logger.getLogger(PacketFactory.class.getName()).log(Level.SEVERE, null, ex);
        }



        byte[] out = BytePlus.assembleBytes(kind, baos.toByteArray());
//        System.out.println("包体" + BytePlus.bytesToHexString(out));
        ALCpacket pkt = new ALCpacket(out);
//        System.out.println("全部" + BytePlus.bytesToHexString(pkt.getWholePacket()));
        return pkt;
    }

    public static ALCpacket ALCRealDataSequence() {
        byte[] kind = {7};
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int switchbyte = r.nextInt(0, 1);
        int count = r.nextInt(1, 17);

        try {
            baos.write(DataFactory.ByteDateProducer());
            baos.write(BytePlus.short2bytes(Short.reverseBytes((short) r.nextInt(1, 65535))));//间隔时间
            baos.write(r.nextInt(0, 255));//数值编号
            baos.write(count);
            r.resetInt();
            for (int i = 0; i < count; i++) {
                baos.write(BytePlus.int2bytes(Integer.reverseBytes(r.nextInt())));
            }

        } catch (IOException ex) {
            Logger.getLogger(PacketFactory.class.getName()).log(Level.SEVERE, null, ex);
        }



        byte[] out = BytePlus.assembleBytes(kind, baos.toByteArray());
//        System.out.println("包体" + BytePlus.bytesToHexString(out));
        ALCpacket pkt = new ALCpacket(out);
//        System.out.println("全部" + BytePlus.bytesToHexString(pkt.getWholePacket()));
        return pkt;
    }

    public static ALCpacket ALCResultAll() {
        byte[] kind = {8};
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int switchbyte = r.nextInt(0, 1);
        int count = r.nextInt(1, 16);

        try {
//            baos.write(DataFactory.ByteDateProducer());
            baos.write(switchbyte);
            if (switchbyte == 1) {
                r.resetInt();
                baos.write(BytePlus.int2bytes(Integer.reverseBytes(r.nextInt())));
            }
            baos.write(count);
            for (int i = 0; i < count; i++) {
                baos.write(BytePlus.int2bytes(Integer.reverseBytes(r.nextInt())));
            }

        } catch (IOException ex) {
            Logger.getLogger(PacketFactory.class.getName()).log(Level.SEVERE, null, ex);
        }



        byte[] out = BytePlus.assembleBytes(kind, baos.toByteArray());
//        System.out.println("包体" + BytePlus.bytesToHexString(out));
        ALCpacket pkt = new ALCpacket(out);
//        System.out.println("全部" + BytePlus.bytesToHexString(pkt.getWholePacket()));
        return pkt;
    }
    public static ALCpacket ALCResultSpecial() {
        byte[] kind = {9};
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int switchbyte = r.nextInt(0, 1);
        int count = r.nextInt(1, 15);

        try {
//            baos.write(DataFactory.ByteDateProducer());
            baos.write(switchbyte);
            if (switchbyte == 1) {
                r.resetInt();
                baos.write(BytePlus.int2bytes(Integer.reverseBytes(r.nextInt())));
            }

            baos.write(count);


//            if (switchbyte == 1) {
//                r.resetInt();
//                baos.write(BytePlus.int2bytes(Integer.reverseBytes(r.nextInt())));
//            }
            for (int i = 0; i < count; i++) {
                baos.write(r.nextInt(0, 255));//4.	数值编号
                r.resetInt();
                baos.write(BytePlus.int2bytes(Integer.reverseBytes(r.nextInt())));
            }

        } catch (IOException ex) {
            Logger.getLogger(PacketFactory.class.getName()).log(Level.SEVERE, null, ex);
        }



        byte[] out = BytePlus.assembleBytes(kind, baos.toByteArray());
//        System.out.println("包体" + BytePlus.bytesToHexString(out));
        ALCpacket pkt = new ALCpacket(out);
//        System.out.println("全部" + BytePlus.bytesToHexString(pkt.getWholePacket()));
        return pkt;
    }


    public static void main(String[] args) {
//        PacketFactory.ALCShortParams();
//        PacketFactory.ALCShortParams();
//        PacketFactory.ALCShortParams();
        PacketFactory.ALCRealDataAll();
        //System.out.println(PacketFactory.ALCLongParams().length);
//        for (int i = 0; i < 20; i++) {
//            System.out.println(PacketFactory.r.nextInt(0, 1));
//
//        }
    }
}
