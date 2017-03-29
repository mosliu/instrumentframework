package net.labthink.instrument.device.G2131.handler;

//~--- non-JDK imports --------------------------------------------------------
import net.labthink.instrument.device.G2131.message.G2131InMessage;
import net.labthink.instrument.device.G2131.message.G2131OutMessage;
import net.labthink.instrument.device.G2131.simulator.G2131;
import net.labthink.utils.BytePlus;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * G2-131软件 信号处理
 * 标定数据
 * @version       1.0.0.3
 * @author         Moses
 */
public class G2131Handler extends IoHandlerAdapter {
    public boolean allerror = false;
    int errorcount = 0;
    private static final String STORE_FILE = "save.dat";
    /** 每次发送后等待时间。 */
    private int waittime = 100;

    /* 仪器 */
    private G2131 device = new G2131();

    /**
     * 构造函数
     *
     */
    public G2131Handler() {
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
        super.exceptionCaught(session, cause);
    }

    public static void main(String[] args) {
        byte a = 17;
        byte b = -43;
        byte c[] = {17, -43};

        System.out.println(BytePlus.bytes2int(c));

//        byte a = 6;
//        byte b[] = {0x11, 0x22, 0x33, 0x44};
//        byte c[] = new byte[2];
//        short d = (short) (b[1] << 8 | b[2]);
//
//        System.arraycopy(b, 1, c, 0, 2);
//        System.out.println(c[0]);
//        System.out.println(c[1]);
//        System.out.println(d);

//      System.out.println((a & 0x01) == 0x01);
//      System.out.println((a & 0x02) == 0x02);
//      System.out.println((a & 0x04) == 0x04);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        G2131InMessage msg = (G2131InMessage) message;
        byte[] incontent = msg.getContent();
        G2131OutMessage outmsg = new G2131OutMessage();
        byte[] outmsgcontent = outmsg.getContent();
        byte tempbyte;
        byte tempbyte2;
        byte[] tempbytes;

        // System.out.println(message);
        switch (incontent[0]) {
            case 0x0:

                /* 试验(X2代表试验腔选择;最低位D0代表CH1,D1代表CH2,D2代表CH3;对应位为"1"代表试验,为"0"否) */
                tempbyte = incontent[1];

                if ((tempbyte > 0) && (tempbyte < 8)) {
                    getDevice().getCell(1).setDotest((tempbyte & 0x01) == 0x01);
                    getDevice().getCell(2).setDotest((tempbyte & 0x01) == 0x02);
                    getDevice().getCell(2).setDotest((tempbyte & 0x01) == 0x04);
                    getDevice().setTesting(true);
                    System.out.println("开始试验");
                    allerror = false;
                    //设置开始试验的参数
                    getDevice().setCellpresure_high(1005);//设置上腔压力
                    getDevice().setTemprature_ambinent(250);//设置环境温度
                    getDevice().setHumidity_room(400);//设置室内湿度


                } else {
                    getDevice().setTesting(false);
                }

                // TODO 是否需要确认帧？
                break;

            case 0x1:

                // 停止
                // 处理状态
                getDevice().setTesting(false);
                System.out.println("试验停止\t");

                // 设置返回帧内容
                outmsgcontent[0] = 0x02;
                outmsgcontent[1] = 0x21;
                BytePlus.fillcontent(outmsgcontent, 2);

                break;

            case 0x2:

                // 要实验数据
                System.out.println("要实验数据\t");

                // 01H   实时数据(X2X3下腔温度；X4X5环境温度；X6X7室内湿度 X8X9高压腔 XAXB低压腔1  XCXD低压腔2   XEXF低压腔3)
                outmsgcontent[0] = 0x01;
//	    System.arraycopy(getDevice().getTemprature_undercellBA(), 2, outmsgcontent, 1, 2);
                System.arraycopy(getDevice().getTemperatureSetPoint(), 0, outmsgcontent, 1, 2);
                System.arraycopy(getDevice().getTemprature_ambinentBA(), 2, outmsgcontent, 3, 2);
                System.arraycopy(getDevice().getHumidity_roomBA(), 2, outmsgcontent, 5, 2);
                System.arraycopy(getDevice().getCellpresure_highBA(), 2, outmsgcontent, 7, 2);
//                if (getDevice().getValve(1) == 1) {
                System.arraycopy(getDevice().getCell(1).getPressure(), 0, outmsgcontent, 9, 2);
//                }
//                if (getDevice().getValve(2) == 1) {
                System.arraycopy(getDevice().getCell(2).getPressure(), 0, outmsgcontent, 11, 2);
//                }
//                if (getDevice().getValve(3) == 1) {
                System.arraycopy(getDevice().getCell(3).getPressure(), 0, outmsgcontent, 13, 2);
//                }
                //BytePlus.fillcontent(outmsgcontent, 15);
                break;

            case 0x03:

                // 备用
                System.out.println("备用帧");

                break;

            case 0x04:

                // 系统复位
                setDevice(new G2131());
                System.out.println("系统复位\t");

                // 设置返回帧内容
                outmsgcontent[0] = 0x03;    // 系统复位 (看门狗)
                BytePlus.fillcontent(outmsgcontent, 1);

                break;

            case 0x05:

                /*
                 * 阀体控制 (X2X3位控制方式,高位在前,0 关闭；1开启,开位为低位)
                 * (10# 9# 8# 7# 6# 5# 4# 3B 3A 2B 2A 1B 1A  1、2、3 A侧上电为开)
                 *
                 */
                byte temp[] = new byte[2];
                temp[0] = incontent[1];
                temp[1] = incontent[2];
//                short ts = (short) BytePlus.bytes2int(temp);

                getDevice().setValvesWithTwoBytes(temp);
                System.out.println("阀体控制\t");

                // 设置返回帧内容
                outmsgcontent[0] = 0x02;    // 阀体ACK帧(X3X4阀的控制位，即试验状态)
                outmsgcontent[1] = 0x20;    // ACK
                System.arraycopy(incontent, 1, outmsgcontent, 2, 2);
                BytePlus.fillcontent(outmsgcontent, 4);

                break;

            case 0x06:

                // 串口查找
                System.out.println("串口查找\t");

                // 设置返回帧内容
                outmsgcontent[0] = 0x05;    // 串口查找确认
                BytePlus.fillcontent(outmsgcontent, 1);

                break;

            case 0x07:

                // 试验前要系数
                System.out.println("试验前要系数\t");

                // 设置返回帧内容
                outmsgcontent[0] = 0x06;    // 发膜标定系数 X2X3  CH1        X4X5 CH2       X6X7 CH3
                System.arraycopy(getDevice().getCell(1).getFactor(), 0, outmsgcontent, 1, 2);
                System.arraycopy(getDevice().getCell(2).getFactor(), 0, outmsgcontent, 3, 2);
                System.arraycopy(getDevice().getCell(3).getFactor(), 0, outmsgcontent, 5, 2);
                BytePlus.fillcontent(outmsgcontent, 7);

                break;

            case 0x08:

                // 标定数据查看
                tempbyte = incontent[1];

                switch (tempbyte) {
                    case 0x10:

                        // 10H     环境湿度
                        tempbytes = getDevice().getHumidity_ambinentBA();

//              tempbytes = getDevice().getAmbientHumidityZeroSetPoint();
                        outmsgcontent[1] = 0x21;    // 21H         环境湿度(X3X4高位在前)

                        break;

                    case 0x11:

                        // 11H   高压
                        tempbytes = getDevice().getCellpresure_highBA();
                        outmsgcontent[1] = 0x22;    // 22H         高压(X3X4高位在前)

                        break;

                    case 0x12:

                        // 12H    低压1
                        tempbytes = getDevice().getCell1().getPressure();
                        outmsgcontent[1] = 0x23;    // 23H         低压1 (X3X4高位在前)

                        break;

                    case 0x13:

                        // 13H    低压2
                        tempbytes = getDevice().getCell2().getPressure();
                        outmsgcontent[1] = 0x24;    // 24H         低压2 (X3X4高位在前)

                        break;

                    case 0x14:

                        // 14H    低压3
                        tempbytes = getDevice().getCell3().getPressure();
                        outmsgcontent[1] = 0x25;    // 25H         低压3 (X3X4高位在前)

                        break;

                    case 0x15:

                        // 15H   电压
                        tempbytes = getDevice().getVoltageBA();
                        outmsgcontent[1] = 0x26;    // 26H          电压

                        break;

                    case 0x16:

                        // 16H    电流
                        tempbytes = getDevice().getCurrentBA();
                        outmsgcontent[1] = 0x27;    // 27H               电流

                        break;

                    case 0x17:

                        // 17H    时间（年月日）
                        tempbytes = new byte[6];
                        System.arraycopy(getDevice().getDatebyte(), 0, tempbytes, 0, 3);
                        System.arraycopy(getDevice().getTimebyte(), 0, tempbytes, 3, 3);
                        outmsgcontent[1] = 0x28;    // 28H                 时间（X3年X4月X5日X6时X7分X8秒）

                        break;

                    case 0x18:

                        // 18H    时间（时分秒）
                        tempbytes = getDevice().getTimebyte();
                        outmsgcontent[1] = 0x29;    // 29H               时间（X3X4X5时分秒）

                        break;

                    default:
                        tempbytes = new byte[2];
                }

                System.out.println("标定数据查看\t");

                // 设置返回帧内容
                outmsgcontent[0] = 0x04;

                // 统一设定值。
                if (tempbytes.length == 2) {
                    System.arraycopy(tempbytes, 0, outmsgcontent, 2, 2);
                    BytePlus.fillcontent(outmsgcontent, 4);
                } else if (tempbytes.length == 3) {
                    System.arraycopy(tempbytes, 0, outmsgcontent, 2, 3);
                    BytePlus.fillcontent(outmsgcontent, 5);
                } else if (tempbytes.length == 4) {
                    System.arraycopy(tempbytes, 2, outmsgcontent, 2, 2);
                    BytePlus.fillcontent(outmsgcontent, 4);
                } else if (tempbytes.length == 6) {
                    System.arraycopy(tempbytes, 0, outmsgcontent, 2, 6);
                    BytePlus.fillcontent(outmsgcontent, 8);
                }

                break;

            case 0x09:

                // 制冷控制（X2=00 关闭；01开启）
                System.out.println("制冷控制\t");
                tempbyte = incontent[1];

                if (tempbyte == 1) {
                    getDevice().setCooling(true);
                } else if (tempbyte == 0) {
                    getDevice().setCooling(false);
                } else {
                    System.out.println("错误传入");
                }

                // 设置返回帧内容
                // 02H   ACK
                outmsgcontent[0] = 0x02;

                // 31H     制冷控制（X3=00 关闭；01开启）
                outmsgcontent[1] = 0x31;
                outmsgcontent[2] = (byte) ((getDevice().isCooling() == true)
                        ? 1
                        : 0);
                BytePlus.fillcontent(outmsgcontent, 3);

                break;

            case 0x0A:

                // 0AH  温度设定（X2X3温度值，一位小数，高位在前）
                tempbytes = new byte[2];
                System.arraycopy(incontent, 1, tempbytes, 0, 2);
                getDevice().setTemperatureSetPoint(tempbytes);
                System.out.println("温度设定\t");

                // 设置返回帧内容
                // 02H   ACK
                outmsgcontent[0] = 0x02;

                // 32H       温度设定（X3X4温度值，一位小数，高位在前）
                outmsgcontent[1] = 0x32;
                System.arraycopy(tempbytes, 0, outmsgcontent, 2, 2);
                BytePlus.fillcontent(outmsgcontent, 4);

                break;

            case 0x0B:

                // 0BH  加热控制（X2=00 关闭；01开启）
                System.out.println("加热控制\t");
                tempbyte = incontent[1];

                if (tempbyte == 1) {
                    getDevice().setHeating(true);
                } else if (tempbyte == 0) {
                    getDevice().setHeating(false);
                } else {
                    System.out.println("错误传入");
                }

                // 设置返回帧内容
                // 02H   ACK
                outmsgcontent[0] = 0x02;

                // 33H         加热控制（X3=00 关闭；01开启）
                outmsgcontent[1] = 0x33;
                outmsgcontent[2] = (byte) ((getDevice().isHeating() == true)
                        ? 1
                        : 0);
                BytePlus.fillcontent(outmsgcontent, 3);

                break;

            case 0x0C:

                // TODO 麻烦，稍后实现。
                // 0CH      设备日志查看
                System.out.println("设备日志查看\t");

                // 设置返回帧内容
                break;

            case 0x0D:

                // 0DH      标定命令
                System.out.println("标定命令\t");
                tempbyte = incontent[1];
                tempbytes = new byte[2];
                System.arraycopy(incontent, 2, tempbytes, 0, 2);

                switch (tempbyte) {
                    case 0x10:

                        // 10H    环境湿度零点(X3X4高位在前)
                        getDevice().setAmbientHumidityZeroSetPoint(tempbytes);
                        outmsgcontent[1] = 0x22;    // 22H         环境湿度零点 (X3X4高位在前)

                        break;

                    case 0x11:

                        // 11H    环境湿度终点(X3X4高位在前)
                        tempbytes = getDevice().getAmbienthumidityEndSetPoint();
                        getDevice().setAmbienthumidityEndSetPoint(tempbytes);
                        outmsgcontent[1] = 0x23;    // 23H         环境湿度终点 (X3X4高位在前)

                        break;

                    case 0x12:

                        // 12H    高压零点(X3X4高位在前)
                        getDevice().setPressuZeroSetPoint(tempbytes);
                        outmsgcontent[1] = 0x24;    // 24H         高压零点 (X3X4高位在前)

                        break;

                    case 0x13:

                        // 13H    高压终点(X3X4高位在前)
                        getDevice().setPressuEndSetPoint(tempbytes);
                        outmsgcontent[1] = 0x25;    // 25H         高压终点 (X3X4高位在前)

                        break;

                    case 0x14:

                        // 14H    低压1的零点(X3X4高位在前)
                        getDevice().getCell(1).setPressureZeroSetPoint(tempbytes);
                        outmsgcontent[1] = 0x26;    // 26H         低压1的零点(X3X4高位在前)

                        break;

                    case 0x15:

                        // 15H    低压1的终点(X3X4高位在前)
                        getDevice().getCell(1).setPressureEndSetPoint(tempbytes);
                        outmsgcontent[1] = 0x27;    // 27H         低压1的终点(X3X4高位在前)

                        break;

                    case 0x16:

                        // 16H    低压2的零点(X3X4高位在前)
                        getDevice().getCell(2).setPressureZeroSetPoint(tempbytes);
                        outmsgcontent[1] = 0x28;    // 28H         低压2的零点(X3X4高位在前)

                        break;

                    case 0x17:

                        // 17H    低压2的终点(X3X4高位在前)
                        getDevice().getCell(2).setPressureEndSetPoint(tempbytes);
                        outmsgcontent[1] = 0x29;    // 29H         低压2的终点(X3X4高位在前)

                        break;

                    case 0x18:

                        // 18H    低压3的零点(X3X4高位在前)
                        getDevice().getCell(3).setPressureZeroSetPoint(tempbytes);
                        outmsgcontent[1] = 0x2A;    // 2AH         低压3的零点(X3X4高位在前)

                        break;

                    case 0x19:

                        // 19H    低压3的终点(X3X4高位在前)
                        getDevice().getCell(3).setPressureEndSetPoint(tempbytes);
                        outmsgcontent[1] = 0x2B;    // 2BH         低压3的终点(X3X4高位在前)

                        break;

                    case 0x1A:

                        // 1AH    膜标定系数K11(X3X4存储)
                        getDevice().getCell(1).setFactor(tempbytes);
                        outmsgcontent[1] = 0x2C;    //

                        break;

                    case 0x1B:

                        // 1BH    膜标定系数K12(X3X4存储)
                        getDevice().getCell(2).setFactor(tempbytes);
                        outmsgcontent[1] = 0x2D;    //

                        break;

                    case 0x1C:

                        // 1CH   膜标定系数K13(X3X4存储)
                        getDevice().getCell(3).setFactor(tempbytes);
                        outmsgcontent[1] = 0x2E;    //

                        break;

                    case 0x1D:

                        // 1DH    电压零点(X3X4高位在前)
                        getDevice().setVoltageZeroSetPoint(tempbytes);

                        // 35H         电压零点(X3X4高位在前)
                        outmsgcontent[1] = 0x35;

                        break;

                    case 0x1E:

                        // 1EH  电压终点(X3X4高位在前)
                        getDevice().setVoltageEndSetPoint(tempbytes);

                        // 36H           电压终点(X3X4高位在前)
                        outmsgcontent[1] = 0x36;    //

                        break;

                    case 0x1F:

                        // 1FH  电流零点(X3X4高位在前)
                        getDevice().setCurrentZeroSetPoint(tempbytes);

                        // 37H           电流零点(X3X4高位在前)
                        outmsgcontent[1] = 0x37;    //

                        break;

                    case 0x20:

                        // 20H  电流终点(X3X4高位在前)
                        getDevice().setCurrentEndSetPoint(tempbytes);

                        // 38H           电流终点(X3X4高位在前)
                        outmsgcontent[1] = 0x38;    //

                        break;

                    default:
                        tempbytes = new byte[2];
                        System.out.println("ERROR INPUT");
                }

                // 设置返回帧内容
                // 02H   ACK
                outmsgcontent[0] = 0x02;
                System.arraycopy(tempbytes, 0, outmsgcontent, 2, 2);

//          // 统一设定值。
//          if(tempbytes.length==2){
//          System.arraycopy(tempbytes, 0, outmsgcontent, 2, 2);
//          }else{
//              System.arraycopy(tempbytes, 2, outmsgcontent, 2, 2);
//          }
                BytePlus.fillcontent(outmsgcontent, 4);

                break;

            case 0x0E:

//          // 0EH  设定温控参数
//          // X2通道值 X3(1、2、3对应P、I、D) X4X5 设定值
//          System.out.println("设定温控参数\t");
//          tempbyte = incontent[1];    // 通道值
//          tempbytes = new byte[3];
//          System.arraycopy(incontent, 2, tempbytes, 0, 3);
//          getDevice().setPIDs(tempbyte, tempbytes);
//
//          // 设置返回帧内容
//          // 02H   ACK
//          outmsgcontent[0] = 0x02;
//
//          // 2FH         设定温控参数确认
//          outmsgcontent[1] = 0x2F;
//          outmsgcontent[2] = tempbyte;
//          System.arraycopy(tempbytes, 0, outmsgcontent, 3, 3);
//          BytePlus.fillcontent(outmsgcontent, 6);
                break;

            case 0x0F:

//          0FH  启用/关闭温控自整定
//          X2 通道值 X3=01H开启；X3=02H 关闭；
                System.out.println("启用/关闭温控自整定\t");
                tempbyte = incontent[1];    // 通道值
                tempbyte2 = incontent[2];    // 开启关闭

                if (tempbyte2 == 1) {
                    getDevice().setPIDAutoOn(true);
                } else if (tempbyte2 == 2) {
                    getDevice().setPIDAutoOn(false);
                } else {
                    System.out.println("错误传入");
                }

                // 设置返回帧内容
                // 02H   ACK
                outmsgcontent[0] = 0x02;

                // 30H 启用/关闭温控自整定确认 X3 通道值 X4=01H开启；X4=02H 关闭；
                outmsgcontent[1] = 0x30;
                outmsgcontent[2] = tempbyte;
                outmsgcontent[3] = tempbyte2;
                BytePlus.fillcontent(outmsgcontent, 4);

                break;

            case 0x10:

                /*
                 * 10H 获得通道参数
                 * X2 通道值
                 * X3=01H PID  X4（1、2、3对应P、I、D）
                 * X3=02H 获取自整定状态字
                 *
                 */
                System.out.println("获得通道参数\t");
                tempbyte = incontent[1];    // 通道值
                tempbyte2 = incontent[2];    // 控制命令

                // 设置返回帧内容
                // 07H      PID温控参数
                outmsgcontent[0] = 0x07;
                outmsgcontent[1] = tempbyte;     // 通道值
                outmsgcontent[2] = tempbyte2;    // 控制命令

                if (tempbyte2 == 1) {
                    tempbytes = getDevice().getPIDs(tempbyte);
                    System.arraycopy(tempbytes, 0, outmsgcontent, 3, 3);
                    BytePlus.fillcontent(outmsgcontent, 6);
                } else if (tempbyte == 2) {
                    outmsgcontent[3] = (byte) ((getDevice().isPIDAutoOn() == true)
                            ? 1
                            : 2);
                    BytePlus.fillcontent(outmsgcontent, 4);
                }

                break;

            case 0x11:

//          11H 要温控表的设定温度（程序启动时使用，以便使软件和温控表的设定一致）
//          X2 通道值 X3X4 温度值 （1位小数）
                System.out.println("要温控表的设定温度\t");
                tempbyte = incontent[1];    // 通道值
                tempbytes = new byte[2];
                System.arraycopy(incontent, 2, tempbytes, 0, 2);

                // 设置返回帧内容
                // 09H   发送温控表的设定温度
                outmsgcontent[0] = 0x09;

                // X2 通道值 X3X4温度值（1位小数）
                outmsgcontent[1] = tempbyte;
                System.arraycopy(getDevice().getPIDs(tempbyte), 1, outmsgcontent, 2, 2);
                BytePlus.fillcontent(outmsgcontent, 4);

                break;

            case 0x12:

//          12H 要功耗
                System.out.println("要功耗 \t");

                // 设置返回帧内容
                // 0AH  发送功耗   X2X3X4X5功耗值，高位在前
                outmsgcontent[0] = 0x0A;
                tempbytes = getDevice().getPowerConsumptionBA();
                System.arraycopy(tempbytes, 0, outmsgcontent, 1, 4);
                BytePlus.fillcontent(outmsgcontent, 5);

                break;

            case 0x13:

//          13H 要电压电流
                System.out.println("要电压电流 \t");

                // 设置返回帧内容
                // 0BH      实时电压电流 X2X3电压 X4X5电流
                outmsgcontent[0] = 0x0B;
                System.arraycopy(getDevice().getVoltageBA(), 2, outmsgcontent, 1, 2);
                System.arraycopy(getDevice().getCurrentBA(), 2, outmsgcontent, 3, 2);
                BytePlus.fillcontent(outmsgcontent, 5);

                break;

            case 0x14:

//          14H 功耗清零
                System.out.println("功耗清零 \t");
                getDevice().setPowerConsumption(0);

                // 设置返回帧内容
                outmsgcontent[0] = 0x02;
                outmsgcontent[1] = 0x34;
                BytePlus.fillcontent(outmsgcontent, 2);

                break;

            case 0x15:

                // 15H    时间标定（X2X3X4年月日）
                tempbytes = new byte[3];
                System.arraycopy(incontent, 1, tempbytes, 0, 3);
                getDevice().setDatebyte(tempbytes);
                outmsgcontent[1] = 0x39;    // 39H               时间标定（X3X4X5年月日）
                System.arraycopy(incontent, 1, outmsgcontent, 2, 3);

                break;

            case 0x16:

                // 16H    时间标定（X2X3X4时分秒）
                tempbytes = new byte[3];
                System.arraycopy(incontent, 1, tempbytes, 0, 3);
                getDevice().setTimebyte(tempbytes);

                // 40H                   时间标定（X3X4X5时分秒）
                System.arraycopy(incontent, 1, outmsgcontent, 2, 3);
                outmsgcontent[1] = 0x39;    //

                break;

            case 0x17:

                // 17H  通道1设定温控参数
                // X2 (1、2、3对应P、I、D) X3X4 设定值
                System.out.println("设定通道1温控参数\t");
                tempbyte = 1;    // 通道值
                tempbytes = new byte[3];
                System.arraycopy(incontent, 1, tempbytes, 0, 3);
                getDevice().setPIDs(tempbyte, tempbytes);

                // 设置返回帧内容
                // 02H   ACK
                outmsgcontent[0] = 0x02;

                // 2FH         设定温控参数确认
                outmsgcontent[1] = 0x2F;
                outmsgcontent[2] = tempbyte;
                System.arraycopy(tempbytes, 0, outmsgcontent, 3, 3);
                BytePlus.fillcontent(outmsgcontent, 6);

                break;

            case 0x18:

                // 18H  通道2设定温控参数
                // X2 (1、2、3对应P、I、D) X3X4 设定值
                System.out.println("设定通道2温控参数\t");
                tempbyte = 2;    // 通道值
                tempbytes = new byte[3];
                System.arraycopy(incontent, 1, tempbytes, 0, 3);
                getDevice().setPIDs(tempbyte, tempbytes);

                // 设置返回帧内容
                // 02H   ACK
                outmsgcontent[0] = 0x02;

                // 2FH         设定温控参数确认
                outmsgcontent[1] = 0x2F;
                outmsgcontent[2] = tempbyte;
                System.arraycopy(tempbytes, 0, outmsgcontent, 3, 3);
                BytePlus.fillcontent(outmsgcontent, 6);

                break;

            case 0x19:

                // 19H  通道3设定温控参数
                // X2 (1、2、3对应P、I、D) X3X4 设定值
                System.out.println("设定通道3温控参数\t");
                tempbyte = 3;    // 通道值
                tempbytes = new byte[3];
                System.arraycopy(incontent, 1, tempbytes, 0, 3);
                getDevice().setPIDs(tempbyte, tempbytes);

                // 设置返回帧内容
                // 02H   ACK
                outmsgcontent[0] = 0x02;

                // 2FH         设定温控参数确认
                outmsgcontent[1] = 0x2F;
                outmsgcontent[2] = tempbyte;
                System.arraycopy(tempbytes, 0, outmsgcontent, 3, 3);
                BytePlus.fillcontent(outmsgcontent, 6);

                break;

            default:
                System.out.println("设备地址:" + incontent[0] + "\t");
        }

        // G2131InMessage pkt = new G2131InMessage();
        //
        // byte bt[] = {1,123,93,23,4,-90,43,0,1};
        // pkt.setContent(bt);
        //
        // WriteFuture wf = session.write(pkt);
        // wf.awaitUninterruptibly();
        byte[]  errorpkt1 = {(byte)0x01,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x0B,(byte)0x96,(byte)0x05,(byte)0xA8,(byte)0x06,(byte)0x59,(byte)0xAE};
        
        errorcount ++;
//        if(false&&errorcount%50==0){
//            System.out.println("输出错误的一帧");
//            outmsg.setContent(errorpkt1);
//            WriteFuture wf = session.write(outmsg);
//            wf.awaitUninterruptibly();
//        }
        outmsg.setContent(outmsgcontent);
        if(allerror ){
            System.out.println("输出错误的一帧");
             outmsg.setContent(errorpkt1);
        }

        WriteFuture wf = session.write(outmsg);
        wf.awaitUninterruptibly();

//        wf = session.write(outmsg);
//        wf.awaitUninterruptibly();
//      this.wait(getWaittime());
    }

//  byte[] k = BytePlus.int2bytes(r.nextInt(0x77777777));
//
//              // 传感器数据
//              System.arraycopy(k, 0, outmsgcontent, 6, 4);
    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("session closed");
        saveData();
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        System.out.println("session idled:" + status.toString());
    }

    /**
     * @return the waittime
     */
    public int getWaittime() {
        return waittime;
    }

    /**
     * @param waittime the waittime to set
     */
    public void setWaittime(int waittime) {
        this.waittime = waittime;
    }

    /**
     * @return the device
     */
    public G2131 getDevice() {
        return device;
    }

    /**
     * @param device the device to set
     */
    public void setDevice(G2131 device) {
        this.device = device;
    }

    /**
     * 保存g2-131机器数据
     */
    public void saveData() {
        try {
            ObjectOutputStream out = null;

            out = new ObjectOutputStream(new FileOutputStream(STORE_FILE));
            out.writeObject(device);
        } catch (IOException ex) {
            Logger.getLogger(G2131Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadData() {
        try {
            File f = new File(STORE_FILE);

            if (f.exists()) {
                FileInputStream fis = new FileInputStream(f);
                ObjectInputStream in = new ObjectInputStream(fis);
                device = (G2131) in.readObject();
//                device.setCellpresure_high(waittime);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(G2131Handler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(G2131Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//    public static void main(String[] args) {
//        byte a = (byte) 0x80;
//        int b = a < 0 ? a + 256 : a;
//        System.out.println(b);
//    }
}
