package net.labthink.instrument.device.intelligent.ZigbeeSettings.handler;

//~--- non-JDK imports --------------------------------------------------------
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import net.labthink.utils.BytePlus;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

//~--- JDK imports ------------------------------------------------------------


import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Timer;
import java.util.TimerTask;
import net.labthink.instrument.device.intelligent.ZigbeeSettings.ZigbeeSettingsPacket;
import net.labthink.instrument.device.intelligent.ZigbeeSettings.ZigbeeStatus;
import net.labthink.tools.WEXUtil;
import net.labthink.utils.FilePlus;

/**
 * industrialpc's 信号处理
 *
 * @version 1.0.0.0, 2010/08/24
 * @author Moses
 */
public class ZigbeeSettingsHandler extends IoHandlerAdapter {

    boolean debug = false;
    public static long packetcount = 0;
//    //开始时间
//    public long timestart = 0;
//    public long lastcounttime = 0;
//    public boolean printflag = true;
//    public boolean printdetail = false;
//    public boolean allerror = false;
//    int errorcount = 0;
//    private static final String STORE_FILE = "industrialpcsave.dat";
//    boolean startsender = false;
//    long starttime = 0;
//    SenderTask st = null;
//    Timer timer = null;
//    public File outfile;
//    private FileWriter fw;
//    /**
//     * 每次发送后等待时间。
//     */
//    private int waittime = 100;

    /*
     * 仪器
     */
    private ZigbeeStatus device;
    private WEXUtil ui;

    /**
     * 构造函数
     *
     */
    public ZigbeeSettingsHandler(WEXUtil _ui) {
        this.ui = _ui;

    }

    public void initDevice(ZigbeeStatus _device) {
        device = _device;
    }

    public static void main(String[] args) {
        int a = 0xFE;
        System.out.println(a);
        a = a * 0x100;
        System.out.println(a);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        if (debug) {
            System.out.println("发送包：" + message);
        }

//        ZigbeeSettingsPacket msg = (ZigbeeSettingsPacket) message;
        if (((ZigbeeSettingsPacket) message).getZigbeehead() == null) {
            super.messageSent(session, message);
            return;
        }
        ZigbeeSettingsPacket.useflag(((ZigbeeSettingsPacket) message).getZigbeehead()[0]);
        super.messageSent(session, message); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
        super.exceptionCaught(session, cause);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {

        ZigbeeSettingsPacket.releaseflag(((ZigbeeSettingsPacket) message).getZigbeehead()[0]);
        packetcount++;
        ZigbeeSettingsPacket msg = (ZigbeeSettingsPacket) message;
        if (debug) {
            System.out.println("收到包：" + msg.toString());
        }
        byte[] head =msg.getZigbeehead();
        byte[] body = msg.getZigbeebody();
        byte[] tail = msg.getZigbeetail();
        if (msg.getBodyRealLength() == 0) {
            //无内容
            return;
        }
        if (msg.getZigbeeSettingsPacketType() == 2) {
            //命令ACK
            int ackkind = (body[0] + 256) % 256;
            int slotno = msg.getPacketSlot();
            int kindRegister = msg.getRegister(slotno);
            msg.releaseRegister(slotno);
            //发送帧变量
            int regno = -1;
            int waittime = 1000;
            ZigbeeSettingsPacket pkt;
            WriteFuture wf;
            //中间变量
            int transaddr = 0;


            switch (ackkind) {
                case 0x8c://获取版本信息
                    String str = "";
                    for (int i = 1; i < body.length; i++) {
                        byte b = body[i];
                        str = str + b + ".";
                    }
                    ui.setVersion(str);
                    ui.enableSettings();
                    break;
                case 0x86://获取寄存器值
//                    System.out.println(BytePlus.byteArray2String(body));
                    //获取回应条目

                    switch (kindRegister) {
                        case 0x65://波特率
                            ui.setBaudRateSet(body[1]);
                            break;
                        case 0x67://校验位
                            ui.setParity(body[1]);
                            break;
                        case 0x6A://流控
                            ui.setFlowControl(body[1]);
                            break;
                        case 0x70://Power
                            ui.setPower(body[1]);
                            break;
                        case 0x72://Channel
                            ui.setChannel(body[1]);
                            break;
                        case 0x96://NodeType
                            ui.setNodeType(body[1]);
                            break;
                        case 0xA0://Topology
                            ui.setTopology(body[1]);
                            break;
                        case 0xAD://工作模式：0帧、1AT、2透传
//                            ui.setTopology(body[1]);
                            int workmode = body[1];
                            System.out.println("WordMode：" + body[1]);
                            if (workmode == 2) {
                                //要透传地址高字节
                                regno = 0xAE;//174
                                waittime = 1000;
                                pkt = ZigbeeSettingsPacket.getRegisterPacket(regno);
                                ZigbeeSettingsPacket.setRegister(pkt.getPacketSlot(), regno, waittime);
                                wf = session.write(message);
                                wf.awaitUninterruptibly();
                            }
                            break;
                        case 0xAE://透传地址，高字节
                            boolean transparentflag = true;
                            transaddr += body[1] * 0x100;
                            //工作模式：帧、AT、透传
                            //要透传地址低字节
                            regno = 0xAF;//175
                            waittime = 1000;
                            pkt = ZigbeeSettingsPacket.getRegisterPacket(regno);
                            ZigbeeSettingsPacket.setRegister(pkt.getPacketSlot(), regno, waittime);
                            wf = session.write(message);
                            wf.awaitUninterruptibly();
                            break;
                        case 0xAF://透传地址，低字节
                            transaddr = transaddr + body[1];
                            System.out.println("透传地址:" + transaddr);
                            break;
                        case 0xBC://NodeId_Upper_NetLayer
                            ui.setNodeId_Upper(body[1]);
                            break;
                        case 0xBD://NodeId_Lower_NetLayer
                            ui.setNodeId_Lower(body[1]);
                            break;
                        case 0xBE://NodeId_Upper_NetLayer
                            ui.setNetId_Upper(body[1]);
                            break;
                        case 0xBF://NodeId_Lower_NetLayer
                            ui.setNetId_Lower(body[1]);
                            break;
                        case 0xC0://NodeId_Upper_MAC,192
                            ui.setNodeId_Upper(body[1]);
                            break;
                        case 0xC1://NodeId_Lower_MAC,193
                            ui.setNodeId_Lower(body[1]);
                            break;
                        case 0xe7://231 eNABLEio
                            ui.setEnableIO(body[1]);
                            break;
                        case 0xF6://jComboBox_Bootloader,246
                            ui.setBootloader(body[1]);
                            break;
                        default:
//                            throw new AssertionError();
                            System.out.println("0x86未处理的寄存器：" + kindRegister);
                    }
                    break;
                case 0x87://设置寄存器值
//                    System.out.println(BytePlus.byteArray2String(body));
                    //获取回应条目

                    switch (kindRegister) {
                        case 0x65://波特率
//                            ui.setBaudRateSet(body[1]);
                            System.out.println("BaudRate set successful.");
                            break;
                        case 0x67://
//                            ui.setParity(body[1]);
                            System.out.println("Parity set successful.");
                            break;
                        case 0x6A://
//                            ui.setFlowControl(body[1]);
                            System.out.println("FlowControl set successful.");
                            break;
                        case 0x70://
                            System.out.println("Power set successful.");
                            break;
                        case 0x72://
                            System.out.println("Channel set successful.");
                            break;
                        case 0x96://
                            System.out.println("Node Type set successful.");
                            break;
                        case 0xA0://
                            System.out.println("Topology set successful.");
                            break;
                        case 0xBC://
                            break;
                        case 0xBD://
                            break;
                        case 0xBe://
                            System.out.println("NetID_upper set successful.");
                            break;
                        case 0xbf://
                            System.out.println("NetID_lower set successful.");
                            break;
                        case 0xC0://
                            System.out.println("NodeID_upper set successful.");
                            break;
                        case 0xC1://
                            System.out.println("NodeID_lower set successful.");
                            break;
                        case 0xF6://
                            System.out.println("Bootloader set successful.");
                            break;
                        case 0xB0://
                            System.out.println("TransparentMode LoopBack Flag set successful.");
                            break;
                        case 0xE7://
                            System.out.println("EnableIO set successful.Plaese set to yes after test  ");
                            break;
                        default:
//                            throw new AssertionError();
                            System.out.println("0x87未处理的寄存器：" + kindRegister);
                    }
                    break;
                case 0x8F://软重启
                    System.out.println("Zigbee Soft Restart");
                    break;
                case 0x90://恢复出厂
                    System.out.println("Zigbee Reset To Factory Mode");
                    break;
                case 0xAA://路由帧
                    ByteBuffer bb = ByteBuffer.wrap(body);
                    bb.order(ByteOrder.BIG_ENDIAN);

                    bb.get();//第一个AA
                    boolean noflag = true;

                    while (bb.remaining() > 0) {
                        if (bb.get(bb.position()) == (byte) 0xAA && bb.get(bb.position() + 1) == 0x55) {
                            bb.getShort();
                            if (noflag == true) {
                                System.out.print("Send Route：");
                                noflag = false;
                            } else {
                                System.out.print("\r\nBack Route：");
                                noflag = true;
                            }
                        } else {
                            short id = bb.getShort();
                            System.out.print(id + "、");
                        }
                    }
                    System.out.println("\r\nThis function may not working correctly");
                    break;
                case 0xBB://路由帧RSSI
                    bb = ByteBuffer.wrap(body);
                    bb.order(ByteOrder.BIG_ENDIAN);

                    bb.get();//第一个AA
                    noflag = true;

                    while (bb.remaining() > 0) {
                        if (bb.get(bb.position()) == (byte) 0xAA && bb.get(bb.position() + 1) == 0x55) {
                            bb.getShort();
                            if (noflag == true) {
                                System.out.print("Send Route：");
                                noflag = false;
                            } else {
                                System.out.print("\r\nBack Route：");
                                noflag = true;
                            }
                        } else {
                            short id = bb.getShort();
                            System.out.print(id);
                            if(bb.remaining() >1){
                                if (bb.get(bb.position()) == (byte) 0xAA && bb.get(bb.position() + 1) == 0x55) {
                                    continue;
                                } else {
                                    System.out.print("(");
                                    int rssi = bb.get();
                                    System.out.print(rssi + ")、");
                                }
                            }
                        }
                    }
                    System.out.println("\r\nThis function may not working correctly");
                    break;
                case 0xFF://报错
                    switch (body[1]) {
                        case 1:
                            System.out.println("Error:XOR_Error");
                            break;
                        case 2:
                            System.out.println("Error:SendFail");
                            break;
                        case 3:
                            System.out.println("Error:ErrorCommand");
                            break;
                        case 6:
                            System.out.println("Error:ErrorCmdParam");
                            break;
                        default:
                    }
                    break;
                default:
                    // throw new AssertionError();
                    System.out.println("不明指令:" + msg.toString());
            }
        } else if (msg.getZigbeeSettingsPacketType() == 4) {
            //数据请求回应帧
            StringBuilder sb = new StringBuilder();
            sb.append("DataAck From:").append(msg.getDestAddr()).append("    ");
            if(body[0]==(byte)0xFF){
                //错误
                sb.append("错误：");
                switch (body[1]) {
                    case 0x01:
                    sb.append("校验错误");
                        break;
                    case 0x02:
                    sb.append("传输失败");
                        break;
                    case 0x07:
                    sb.append("无效目的地址");
                        break;
                    case 0x09:
                    sb.append("网络繁忙");
                        break;
                    default:
                        sb.append("其他错误:").append(body[1]);
                }
            }else if(body[0]==0&&body[1]==0){
                sb.append("发送成功");
            }else{
                sb.append("其他提示").append(BytePlus.byteArray2String(body));
            }
            
            System.out.println(sb.toString());
            
        } else {
            System.out.println("不明包："+ msg.toString());
        }
        




        byte[] incontent = msg.getAllPacket();
        ZigbeeSettingsPacket outmsg = null;
        byte[] outmsgcontent;
        byte tempbyte;
        byte tempbyte2;
        byte[] tempbytes;

    }

//  byte[] k = BytePlus.int2bytes(r.nextInt(0x77777777));
//
//              // 传感器数据
//              System.arraycopy(k, 0, outmsgcontent, 6, 4);
    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("session closed");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        System.out.println("session idled:" + status.toString());
    }

    /**
     * @return the device
     */
    public ZigbeeStatus getDevice() {
        return device;
    }

    /**
     * @param device the device to set
     */
    public void setDevice(ZigbeeStatus device) {
        this.device = device;
    }
//    public static void main(String[] args) {
//        byte a = (byte) 0x80;
//        int b = a < 0 ? a + 256 : a;
//        System.out.println(b);
//    }
}
