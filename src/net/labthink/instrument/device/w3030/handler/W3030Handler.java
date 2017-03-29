package net.labthink.instrument.device.w3030.handler;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.labthink.instrument.device.G2131.handler.G2131Handler;
import net.labthink.instrument.device.G2131.simulator.G2131;

import net.labthink.instrument.device.w3030.message.W3030Message;
import net.labthink.instrument.device.w3030.simulator.W3030Device;
import net.labthink.utils.BytePlus;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class W3030Handler extends IoHandlerAdapter {
    private static final String STORE_FILE = "w3030save.dat";
    /** 每次发送后等待时间。 */
    private int waittime = 100;

    /* 仪器 */
    private W3030Device device = new W3030Device();

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {

        cause.printStackTrace();

        super.exceptionCaught(session, cause);
    }

    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        W3030Message msg = (W3030Message) message;
        byte[] incontent = msg.getContent();
        W3030Message outmsg = new W3030Message();
        byte[] outmsgcontent = outmsg.getContent();

        byte[] tempbytes;
        int tempint;
        Random r = new Random(System.currentTimeMillis());
        System.out.println(message);
        // 打印命令类型
        switch (incontent[0]) {
            case 0x01:
                System.out.print("开始试验\t");
                getDevice().setTesting(true);
                getDevice().cw.times = 0;
                // 确认帧
                outmsgcontent[0] = 1;
                // X2=01 试验开始确认
                outmsgcontent[1] = 1;
                break;
            case 0x02:
                System.out.print("停止试验\t");
                // 确认帧
                outmsgcontent[0] = 1;
                // X2=02 试验停止确认
                outmsgcontent[1] = 2;
                break;

            case 0x03:
                System.out.print("获取参数数据\t");
                // 数 据帧
                outmsgcontent[0] = 2;

                switch (incontent[1]) {
                    case 0:

                        // X2=00H 获取试验过程数据
                        System.out.print("获取试验过程数据\t");

                        // out X2=01H试验数据
                        outmsgcontent[1] = 1;
                        // X3X4试验温度，高位在前，一位小数（通道1）
//                        getDevice().setTestTemperature(700);
                        System.arraycopy(getDevice().getTestTemperatureBS(), 0, outmsgcontent, 2, 2);
                        // X5X6试验湿度，高位在前，一位小数
                        System.arraycopy(getDevice().getTestHumidityBS(), 0, outmsgcontent, 4, 2);
                        // X7X8X9 透湿杯重量 (去皮后的重量)
                        System.arraycopy(getDevice().getCupweightBS(), 0, outmsgcontent, 6, 3);
                        // X10X11 环境温度（通道2）
                        System.arraycopy(getDevice().getAmbientTemperatureBS(), 0, outmsgcontent, 9, 2);
                        // X12X13 环境湿度
                        System.arraycopy(getDevice().getAmbientHumidityBS(), 0, outmsgcontent, 11, 2);
                        // X14透湿杯重量标志X14=01H，有透湿杯重量，X14=02H无透湿杯重量
                        outmsgcontent[13] = getDevice().getCupweightflag();
                        // X15天平量程X15=00H，200g量程，X15=01H，400g量程
                        outmsgcontent[14] = getDevice().getBalanceRange();
                        break;

                    case 1:
                        // X2=01H 获取PID1参数
                        System.out.print("获取PID1参数");
                        // out X2=02H PID1温控参数
                        outmsgcontent[1] = 2;
                        // X3X4 P1值
                        tempbytes = BytePlus.int2bytes(r.nextInt(10000));
                        System.arraycopy(tempbytes, 0, outmsgcontent, 2, 2);
                        // X5X6 I1值
                        tempbytes = BytePlus.int2bytes(r.nextInt(10000));
                        System.arraycopy(tempbytes, 0, outmsgcontent, 4, 2);
                        // X7X8 D1值
                        tempbytes = BytePlus.int2bytes(r.nextInt(10000));
                        System.arraycopy(tempbytes, 0, outmsgcontent, 6, 2);

                        break;

                    case 2:
                        // X2=02H 获取PID2参数
                        System.out.print("获取PID2参数");
                        // out X2=03H PID2温控参数
                        outmsgcontent[1] = 3;
                        // X3X4 P2值
                        tempbytes = BytePlus.int2bytes(r.nextInt(10000));
                        System.arraycopy(tempbytes, 0, outmsgcontent, 2, 2);
                        // X5X6 I2值
                        tempbytes = BytePlus.int2bytes(r.nextInt(10000));
                        System.arraycopy(tempbytes, 0, outmsgcontent, 4, 2);
                        // X7X8 D2值
                        tempbytes = BytePlus.int2bytes(r.nextInt(10000));
                        System.arraycopy(tempbytes, 0, outmsgcontent, 6, 2);
                        break;

                    case 3:
                        // X2=03H 获取PID3参数
                        System.out.print("获取PID3参数");
                        // out X2=04H PID3温控参数
                        outmsgcontent[1] = 4;
                        // X3X4 P3值
                        tempbytes = BytePlus.int2bytes(r.nextInt(10000));
                        System.arraycopy(tempbytes, 0, outmsgcontent, 2, 2);
                        // X5X6 I3值
                        tempbytes = BytePlus.int2bytes(r.nextInt(10000));
                        System.arraycopy(tempbytes, 0, outmsgcontent, 4, 2);
                        // X7X8 D3值
                        tempbytes = BytePlus.int2bytes(r.nextInt(10000));
                        System.arraycopy(tempbytes, 0, outmsgcontent, 6, 2);
                        break;

                    case 4:
                        // X2=04H 获取PID4参数
                        System.out.print("获取PID4参数");
                        // out X2=05H PID4温控参数
                        outmsgcontent[1] = 5;
                        // X3X4 P4值
                        tempbytes = BytePlus.int2bytes(r.nextInt(10000));
                        System.arraycopy(tempbytes, 0, outmsgcontent, 2, 2);
                        // X5X6 I4值
                        tempbytes = BytePlus.int2bytes(r.nextInt(10000));
                        System.arraycopy(tempbytes, 0, outmsgcontent, 4, 2);
                        // X7X8 D4值
                        tempbytes = BytePlus.int2bytes(r.nextInt(10000));
                        System.arraycopy(tempbytes, 0, outmsgcontent, 6, 2);
                        break;

                    case 5:
                        // X2=05H 获取自整定状态字
                        System.out.print("获取自整定状态字");
                        // out X2=06H自整定状态字
                        outmsgcontent[1] = 6;
                        // X3 通道1； X4 通道2 ；X5 通道3 ；X6 通道4
                        outmsgcontent[2] = (byte) r.nextInt(2);
                        outmsgcontent[3] = (byte) r.nextInt(2);
                        outmsgcontent[4] = (byte) r.nextInt(2);
                        outmsgcontent[5] = (byte) r.nextInt(2);

                        break;
                    case 7:
                        // X2=07H 获取标定系数
                        System.out.print("获取标定系数");
                        // out X2=09H 标定系数 (膜标定系数)
                        outmsgcontent[1] = 9;
                        // X3X4 系数值1，高位在前，两位小数
                        tempbytes = BytePlus.int2bytes(r.nextInt(10000));
                        System.arraycopy(tempbytes, 0, outmsgcontent, 2, 2);
                        // X5X6 系数值2，高位在前，两位小数
                        tempbytes = BytePlus.int2bytes(r.nextInt(10000));
                        System.arraycopy(tempbytes, 0, outmsgcontent, 4, 2);
                        // X7X8 系数值3，高位在前，两位小数
                        tempbytes = BytePlus.int2bytes(r.nextInt(10000));
                        System.arraycopy(tempbytes, 0, outmsgcontent, 6, 2);
                        // X9X10 系数值4，高位在前，两位小数
                        tempbytes = BytePlus.int2bytes(r.nextInt(10000));
                        System.arraycopy(tempbytes, 0, outmsgcontent, 8, 2);
                        break;

                    case 8:
                        // X2=08H 获取温度修正系数
                        System.out.print("获取温度修正系数");
                        // out X2=07H 温度修正系数
                        outmsgcontent[1] = 7;
                        // X3X4系数值，高位在前，一位小数
                        tempbytes = BytePlus.int2bytes(r.nextInt(1000));
                        System.arraycopy(tempbytes, 0, outmsgcontent, 2, 2);
                        break;

                    case 9:
                        // X2=09H 获取湿度修正系数
                        System.out.print("获取湿度修正系数");
                        // out X2=08H 湿度修正系数
                        outmsgcontent[1] = 8;
                        // X3X4系数值，高位在前，一位小数
                        tempbytes = BytePlus.int2bytes(r.nextInt(1000));
                        System.arraycopy(tempbytes, 0, outmsgcontent, 2, 2);
                        break;

                    case 0X0A:
                        // 获取系统参数
                        System.out.print("获取系统参数");
                        // out X2=0AH 系统参数
                        outmsgcontent[1] = 0x0A;
                        /*
                         * X3 t1值，单位小时 X4 g1值，单位mg X5 t2值，单位小时 X6 t3值，单位小时 X7
                         * t4值，单位小时 X8 t5值，单位小时 X9 t6值，单位小时 X10 状态1 称重点数 X11 状态2
                         * 称重点数 X12 状态3 称重点数 X13 状态4 称重点数 X14 停风扇时间提前量（距称重出数）
                         * X15 去皮时间（停风扇后开始计时）
                         */
                        outmsgcontent[13] = 20;
                        outmsgcontent[14] = 10;
                        // TODO 模拟设备需要记录的

                        break;

                    case 0X0B:
                        // 获取预热时间
                        System.out.print(" 获取预热时间");
                        // out X2=0BH 预热时间
                        outmsgcontent[1] = 0x0B;
                        // X3X4系数值，预热时间，高位在前，单位小时，一位小数
                        tempbytes = BytePlus.int2bytes(r.nextInt(50));
                        System.arraycopy(tempbytes, 0, outmsgcontent, 2, 2);
                        break;

                    case 0X0C:
                        //(OK)
                        // 获取设备初始化参数
                        // out X2=0CH 设备初始化参数
                        outmsgcontent[1] = 0x0C;
                        System.out.print("获取设备初始化参数");
                        /*
                         * X3 天平量程， X3=00H 200g量程；X3=01H 400g量程； X4X5
                         * 试验间隔，整数。单位：分钟 X6 去皮时间，一位小数，单位：分钟 X7 听风扇提前量，一位小数，单位：分钟
                         * X8X9 标定系数1，两位小数 X10X11 标定系数2，两位小数 X12X13 标定系数3，两位小数
                         * X14X15 标定系数4，两位小数
                         */
                        outmsgcontent[2] = getDevice().getBalanceRange();
                        System.arraycopy(getDevice().getTestGapBS(), 0, outmsgcontent, 3, 2);
                        outmsgcontent[5] = getDevice().getPeelofftime();
                        outmsgcontent[6] = getDevice().getStopfantime();
                        System.arraycopy(getDevice().getCalibrationfactor1BS(), 0, outmsgcontent, 7, 2);
                        System.arraycopy(getDevice().getCalibrationfactor2BS(), 0, outmsgcontent, 9, 2);
                        System.arraycopy(getDevice().getCalibrationfactor3BS(), 0, outmsgcontent, 11, 2);
                        System.arraycopy(getDevice().getCalibrationfactor4BS(), 0, outmsgcontent, 13, 2);
                        break;

                    default:
                        System.out.print("错误！！");
                        break;
                }
                break;
            case 0x04:
                // 温度控制
                switch (incontent[1]) {

                    case 1:
                        // 待用
                        System.out.print("温度（Pt100）");
                        break;

                    case 2:
                        // 待用
                        System.out.print("湿度（RH）");
                        break;

                    case 3:
                        // 启用/关闭温控自整定
						/*
                         * X3=01H开启；X3=02H 关闭；通道1 X4=01H开启；X4=02H 关闭；通道2
                         * X5=01H开启；X5=02H 关闭；通道3 X6=01H开启；X6=02H 关闭；通道4
                         */
                        System.out.print("压力（气压）");
                        break;

                    case 4:
                        // 待用
                        System.out.print("氧浓度 (PPM)");
                        break;

                    default:
                        System.out.print("错误！！");
                        break;
                }
                break;
            case 0x05:
                // 数字开关量IO
                System.out.print("数字开关量IO\t");
                // 确认帧
                outmsgcontent[0] = 1;
                // X2=09H 数字开关量IO确认
                outmsgcontent[1] = 9;
                // TODO 吹干气,吹湿气需要重新设定
                System.arraycopy(incontent, 1, outmsgcontent, 2, 2);
                String openstatus = incontent[2] == 0 ? "关闭" : "打开";
                switch (incontent[1]) {

                    case 1:
                        // X2=01H 气缸； X3=01打开，X3=00关闭
                        System.out.print("气缸" + openstatus);
                        break;

                    case 2:
                        // X2=02H 温度标定开关；X3=01开，X3=00关
                        System.out.print("温度" + openstatus);
                        break;

                    case 3:
                        // X2=03H 湿度标定开关；X3=01开，X3=00关
                        System.out.print("湿度" + openstatus);
                        break;

                    case 4:
                        // X2=04H 风扇；X3=01打开，X3=00关闭
                        System.out.print("风扇" + openstatus);
                        break;

                    case 5:
                        // X2=05H 加热开关；X3=01打开，X3=00关闭
                        System.out.print("加热" + openstatus);
                        break;

                    case 6:
                        // X2=06H 制冷开关；X3=01打开，X3=00关闭
                        System.out.print("制冷" + openstatus);
                        break;

                    case 7:
                        // X2=07H 吹干气；X3=01打开，X3=00关闭
                        System.out.print("吹干气" + openstatus);
                        break;

                    case 8:
                        // X2=08H 吹湿气；X3=01打开，X3=00关闭
                        System.out.print("吹湿气" + openstatus);
                        break;

                    default:
                        System.out.print("错误！！");
                        break;
                }
                break;
            case 0x06:
                // 设备主机复位指令
                outmsgcontent[0] = 4;
                break;
            case 0x07:
                // 设置温度修正系数
                // X2X3 温度修正系数，高位在前，一位小数
                // 确认帧
                outmsgcontent[0] = 1;
                // X2=04H 标定确认帧
                outmsgcontent[1] = 5;

                tempbytes = new byte[2];
                System.arraycopy(incontent, 1, tempbytes, 0, 2);
                tempint = BytePlus.parseIntFromByteArray(tempbytes);
                System.arraycopy(incontent, 1, outmsgcontent, 2, 2);
                System.out.print("温度修正系数:" + tempint);

                break;
            case 0x08:
                // 设置湿度修正系数
                // X2X3 湿度修正系数，高位在前，一位小数
                tempbytes = new byte[2];
                System.arraycopy(incontent, 1, tempbytes, 0, 2);
                tempint = BytePlus.parseIntFromByteArray(tempbytes);
                System.arraycopy(incontent, 1, outmsgcontent, 2, 2);
                System.out.print("湿度修正系数:" + tempint);
                break;
            case 0x09:
                // 标定
                System.out.print("标定:");
                // 确认帧
                outmsgcontent[0] = 1;
                // X2=04H 标定确认帧
                outmsgcontent[1] = 4;
                switch (incontent[1]) {

                    case 1:
                        // X2=01H 湿度零点，X3X4高位在前，一位小数
                        tempbytes = new byte[2];
                        System.arraycopy(incontent, 2, tempbytes, 0, 2);
                        tempint = BytePlus.parseIntFromByteArray(tempbytes);
                        System.arraycopy(incontent, 1, outmsgcontent, 2, 3);
                        System.out.print("湿度零点:" + tempint);
                        break;

                    case 2:
                        // X2=02H 湿度终点，X3X4高位在前，一位小数
                        tempbytes = new byte[2];
                        System.arraycopy(incontent, 2, tempbytes, 0, 2);
                        tempint = BytePlus.parseIntFromByteArray(tempbytes);
                        System.arraycopy(incontent, 1, outmsgcontent, 2, 3);
                        System.out.print("湿度终点" + tempint);
                        break;

                    case 3:
                        // X2=03H 天平去皮
                        outmsgcontent[2] = 3;
                        System.out.print("天平去皮");
                        break;

                    case 4:
                        // X2=04H 校天平
                        outmsgcontent[2] = 4;
                        System.out.print("校验天平");
                        break;

                    case 5:
                        // X2=05H 查询天平状态（读天平数据）
                        // 注：标定过程：发送去皮指令—收到确认帧—读取天平状态----发送校天平指令
                        // ---收到确认帧---读取天平状态。
                        //天平状态
                        System.out.print("查询天平状态");
                        outmsgcontent[0] = 3;
                        outmsgcontent[1] = 5;
                        // X3X4X5天平重量，高位在前
                        tempbytes = BytePlus.int2bytes(r.nextInt(400000));
                        System.arraycopy(tempbytes, 0, outmsgcontent, 2, 3);
                        //X6=00H 200g量程 4位小数 X6=01H 400g 量程3位小数
                        outmsgcontent[5] = 1;

                        break;

                    case 6:
                        // 保存膜标定系数，
						/*
                         * X3X4标定系数1，高位在前，两位小数 X5X6标定系数2，高位在前，两位小数
                         * X7X8标定系数3，高位在前，两位小数 X9X10标定系数4，高位在前，两位小数
                         */
                        outmsgcontent[2] = 5;
                        System.out.print("膜标定系数");

                        tempbytes = new byte[2];
                        System.arraycopy(incontent, 2, tempbytes, 0, 2);
                        tempint = BytePlus.parseIntFromByteArray(tempbytes);
                        System.out.print("1:" + tempint + "\t");
                        System.arraycopy(incontent, 4, tempbytes, 0, 2);
                        tempint = BytePlus.parseIntFromByteArray(tempbytes);
                        System.out.print("2:" + tempint + "\t");
                        System.arraycopy(incontent, 6, tempbytes, 0, 2);
                        tempint = BytePlus.parseIntFromByteArray(tempbytes);
                        System.out.print("3:" + tempint + "\t");
                        System.arraycopy(incontent, 8, tempbytes, 0, 2);
                        tempint = BytePlus.parseIntFromByteArray(tempbytes);
                        System.out.print("4:" + tempint + "\t");

                        System.arraycopy(incontent, 2, outmsgcontent, 3, 8);
                        break;

                    default:
                        System.out.print("错误！！");
                        break;
                }

                break;
            case 0x0A:
                // 参数设置 (参考附录中的蓝色定义)
                // 确认帧
                outmsgcontent[0] = 1;

                switch (incontent[1]) {
                    case 1:
                        System.out.print("系统参数设置\t");

                        // X2=03 系统参数确认
                        outmsgcontent[1] = 3;
                        // X2=01H 系统参数（工程师密码登陆）
						/*
                         * X3 t1值，单位小时 X4 g1值，单位mg X5 t2值，单位小时 X6 t3值，单位小时 X7
                         * t4值，单位小时 X8 t5值，单位小时 X9 t6值，单位小时 X10 状态1 称重点数 X11 状态2
                         * 称重点数 X12 状态3 称重点数 X13 状态4 称重点数 X14
                         * 停风扇时间提前量，一位小数，单位：分钟（距称重出数） X15
                         * 去皮时间，一位小数，单位：分钟（停风扇后开始计时）
                         */
                        // TODO 打印这些参数
                        System.arraycopy(incontent, 2, outmsgcontent, 2, 13);
                        break;

                    case 2:
                        // X2=02H 试验参数
                        System.out.print("试验参数设置\t");

                        /*
                         * X3X4 预热时间，高位在前，单位小时，一位小数（未用） X5X6 试验温度，高位在前，一位小数 X7X8
                         * 试验湿度，高位在前，一位小数 X9X10 称重间隔（整数），高位在前，分钟 X11
                         * 加热开启关闭，1=开启；0=关闭 X12 制冷开启关闭，1=开启；0=关闭
                         */
                        if (incontent[2] != 0 || incontent[3] != 0) {
                            // 预热时间设定确认
                            outmsgcontent[1] = 0x08;
                            System.arraycopy(incontent, 2, outmsgcontent, 2, 2);
                        } else {
                            // X2=0AH 试验参数确认
                            outmsgcontent[1] = 0x0A;
                            System.arraycopy(incontent, 4, outmsgcontent, 2, 6);
                            // TODO 建立对象 储存这些参数
                            outmsgcontent[8] = 1;// X9 传感器量程；0=200.0000g
                            // 1=400.000g
                        }

                        break;

                    case 3:
                        // X2=03H 天平重量范围选择
                        System.out.print("天平重量范围选择:");
                        /*
                         * X3=00H 选择200g天平 X3=01H 选择400g天平
                         */

                        // X2=07H 重量范围确认
                        outmsgcontent[1] = 7;
                        outmsgcontent[2] = incontent[2];
                        switch (incontent[2]) {
                            case 0:
                                System.out.print("200g天平");
                                break;
                            case 1:
                                System.out.print("400g天平");
                                break;

                            default:
                                System.out.print("错误！！");
                                break;
                        }
                        break;

                    case 4:
                        // X2=04H 出厂默认参数设置
                        System.out.print("出厂默认参数设置");

                        // X2=0CH 出厂参数确认
                        outmsgcontent[1] = 0X0C;
                        break;

                    default:
                        System.out.print("错误！！");
                        break;
                }

                break;
            case 0x0B:
                // 指示灯控制指令（同一时间，指示灯只能一个有效）
                // 确认帧
                outmsgcontent[0] = 1;
                // X2=0BH 指示灯确认
                outmsgcontent[1] = 0x0B;

                outmsgcontent[2] = incontent[1];
                switch (incontent[1]) {

                    case 1:
                        // X2=01H 预热灯亮
                        System.out.print("预热灯亮");
                        break;

                    case 2:
                        // X2=02H 判断灯亮
                        System.out.print("判断灯");
                        break;

                    case 3:
                        // X2=03H 试验灯亮
                        System.out.print("试验灯");
                        break;

                    case 4:
                        // X2=04H 结束灯亮
                        System.out.print("结束灯");
                        break;

                    default:
                        System.out.print("错误！！");
                        break;
                }

                break;
            case 0x0c:
                // 待用

                break;
            default:
                System.out.print("发送的指令不正确:\t");
        }
        System.out.println();
        // W3030Message pkt = new W3030Message();
        //
        // byte bt[] = {1,123,93,23,4,-90,43,0,1};
        // pkt.setContent(bt);
        //
        // WriteFuture wf = session.write(pkt);
        // wf.awaitUninterruptibly();
        outmsg.setContent(outmsgcontent);
        WriteFuture wf = session.write(outmsg);
        wf.awaitUninterruptibly();
        WriteFuture wf1 = session.write(outmsg);
        wf1.awaitUninterruptibly();
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        saveData();
        System.out.println("session closed");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception {
        System.out.println("session idled:" + status.toString());
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        //发送复位信号
        W3030Message outmsg = new W3030Message();
        byte[] outmsgcontent = outmsg.getContent();
        outmsgcontent[0] = 4;
        outmsg.setContent(outmsgcontent);
        WriteFuture wf = session.write(outmsg);
        wf.awaitUninterruptibly();
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
	    Logger.getLogger(W3030Handler.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    public void loadData() {
	try {
	    File f = new File(STORE_FILE);

	    if (f.exists()) {
		FileInputStream   fis = new FileInputStream(f);
		ObjectInputStream in  = new ObjectInputStream(fis);

		device = (W3030Device) in.readObject();
	    }
	} catch (ClassNotFoundException ex) {
	    Logger.getLogger(W3030Handler.class.getName()).log(Level.SEVERE, null, ex);
	} catch (IOException ex) {
	    Logger.getLogger(W3030Handler.class.getName()).log(Level.SEVERE, null, ex);
	}
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
    public W3030Device getDevice() {
        return device;
    }

    /**
     * @param device the device to set
     */
    public void setDevice(W3030Device device) {
        this.device = device;
    }
    // public static void main(String[] args) {
    // byte a = (byte) 0x80;
    // int b = a < 0 ? a + 256 : a;
    // System.out.println(b);
    // }
}
