/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.instrument.device.intelligent.industrialpc;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.labthink.instrument.device.intelligent.industrialpc.zigbee.ALCpacket;
import net.labthink.instrument.device.intelligent.industrialpc.zigbee.PacketFactory;
import net.labthink.utils.Randomer;

/**
 *
 * @author Mosliu
 */
public class TestCaseFileWriter {

    public static void writeSinglePackets() {
        try {
            FileOutputStream fos = new FileOutputStream("d:/tempfile/1-StartMechine.txt");
            fos.write(PacketFactory.ALCStartMechine().getLLPacket());
//            fos.write(PacketFactory.ALCStartMechine().getLLPacket());
//            fos.write(PacketFactory.ALCStartMechine().getLLPacket());
//            fos.write(PacketFactory.ALCStartMechine().getLLPacket());
//            fos.write(PacketFactory.ALCStartMechine().getLLPacket());
//            fos.write(PacketFactory.ALCStartMechine().getLLPacket());
//            fos.write(PacketFactory.ALCStartMechine().getLLPacket());
//            fos.write(PacketFactory.ALCStartMechine().getLLPacket());
//            fos.write(PacketFactory.ALCStartMechine().getLLPacket());
//            fos.write(PacketFactory.ALCStartMechine().getLLPacket());
//            fos.write(PacketFactory.ALCStartMechine().getLLPacket());
            fos.flush();
            fos.close();
            fos = new FileOutputStream("d:/tempfile/2-ALCTestStart.txt");
            fos.write(PacketFactory.ALCTestStart().getLLPacket());
            fos.flush();
            fos.close();
            fos = new FileOutputStream("d:/tempfile/3-ALCTestStop.txt");
            fos.write(PacketFactory.ALCTestStop().getLLPacket());
            fos.flush();
            fos.close();
            fos = new FileOutputStream("d:/tempfile/4-ALCShortParams.txt");
            fos.write(PacketFactory.ALCShortParams().getLLPacket());
            fos.flush();
            fos.close();
            fos = new FileOutputStream("d:/tempfile/5-ALCLongParams.txt");
            ALCpacket[] alcps = PacketFactory.ALCLongParams(400);
            for (int i = 0; i < alcps.length; i++) {
                ALCpacket aLCpacket = alcps[i];
                fos.write(aLCpacket.getLLPacket());
            }
            fos.flush();
            fos.close();
            fos = new FileOutputStream("d:/tempfile/6-ALCRealDataAll.txt");
            fos.write(PacketFactory.ALCRealDataAll().getLLPacket());
            fos.flush();
            fos.close();
            fos = new FileOutputStream("d:/tempfile/7-ALCRealDataSpecial.txt");
            fos.write(PacketFactory.ALCRealDataSpecial().getLLPacket());
            fos.flush();
            fos.close();
            fos = new FileOutputStream("d:/tempfile/8-ALCRealDataSequence.txt");
            fos.write(PacketFactory.ALCRealDataSequence().getLLPacket());
            fos.flush();
            fos.close();
            fos = new FileOutputStream("d:/tempfile/9-ALCResultAll.txt");
            fos.write(PacketFactory.ALCResultAll().getLLPacket());
            fos.flush();
            fos.close();
            fos = new FileOutputStream("d:/tempfile/10-ALCResultSpecial.txt");
            fos.write(PacketFactory.ALCResultSpecial().getLLPacket());
            fos.flush();
            fos.close();

        } catch (IOException ex) {
            Logger.getLogger(TestCaseFileWriter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void writeBatchPackets() {
        int mincount = 10000;
        int maxcount = 100000000;
//        int mincount = 10;
//        int maxcount = 100;
        Randomer r = new Randomer();
        HashSet<ALCpacket> hs = new HashSet<ALCpacket>();

        for (int i = 0; i < r.nextInt(mincount, maxcount); i++) {
            hs.add(PacketFactory.ALCStartMechine());
        }
        for (int i = 0; i < r.nextInt(mincount, maxcount); i++) {
            hs.add(PacketFactory.ALCTestStart());
        }
        for (int i = 0; i < r.nextInt(mincount, maxcount); i++) {
            hs.add(PacketFactory.ALCTestStop());
        }
        for (int i = 0; i < r.nextInt(mincount, maxcount); i++) {
            hs.add(PacketFactory.ALCShortParams());
        }
        for (int i = 0; i < r.nextInt(mincount, maxcount / 10); i++) {
            ALCpacket[] alcps = PacketFactory.ALCLongParams(400);
            
            for (int j = 0; j < alcps.length; j++) {
                hs.add(alcps[j]);
            }
        }
        for (int i = 0; i < r.nextInt(mincount, maxcount); i++) {
            hs.add(PacketFactory.ALCRealDataAll());
        }
        for (int i = 0; i < r.nextInt(mincount, maxcount); i++) {
            hs.add(PacketFactory.ALCRealDataSpecial());
        }
        for (int i = 0; i < r.nextInt(mincount, maxcount); i++) {
            hs.add(PacketFactory.ALCRealDataSequence());
        }
        for (int i = 0; i < r.nextInt(mincount, maxcount); i++) {
            hs.add(PacketFactory.ALCResultAll());
        }
        for (int i = 0; i < r.nextInt(mincount, maxcount); i++) {
            hs.add(PacketFactory.ALCResultSpecial());
        }

        try {
            FileOutputStream fos = new FileOutputStream("d:/tempfile/11-Batch.txt");
            FileWriter fw = new FileWriter("d:/tempfile/11-Batch-humanread.txt");
            

            
            int max = 0;
            for (Iterator<ALCpacket> it = hs.iterator(); it.hasNext();) {
                ALCpacket alcp = it.next();
                byte[] bs = alcp.getLLPacket();
                max = max>bs.length?max:bs.length;//计算最大值
                fw.write(alcp.toString());
                fw.write(alcp.toReadbleString());
                fos.write(bs);
            }
            System.out.println("最长包长："+max);
            if (fw != null) {
                fw.close();
            }
            fos.flush();
            fos.close();
        } catch (IOException ex) {
            Logger.getLogger(TestCaseFileWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("一共写包：" + hs.size() + "个");
    }

    public static void main(String[] args) {
        writeSinglePackets();
        writeBatchPackets();
    }
}
