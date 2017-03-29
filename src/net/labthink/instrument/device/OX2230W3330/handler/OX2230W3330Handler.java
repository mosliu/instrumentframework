package net.labthink.instrument.device.OX2230W3330.handler;

import java.util.Calendar;
import java.util.Random;

import net.labthink.instrument.device.OX2230W3330.message.OX2230W3330Message;
import net.labthink.instrument.device.OX2230W3330.message.OX2230W3330OutMessage;
import net.labthink.utils.BytePlus;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class OX2230W3330Handler extends IoHandlerAdapter {

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {

        cause.printStackTrace();

        super.exceptionCaught(session, cause);
    }

    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        OX2230W3330Message msg = (OX2230W3330Message) message;
        byte[] incontent = msg.getContent();
        OX2230W3330OutMessage outmsg = new OX2230W3330OutMessage();
        byte[] outmsgcontent = outmsg.getContent();
        // 从主机发给那个设备的信号就由那个设备进行回复
        outmsgcontent[0] = incontent[0];
        // 回传包是发给主机的
        // outmsgcontent[1] = 40;
        outmsgcontent[1] = incontent[1];

        System.out.println(message);
        // 打印仪器类型
        printMechineType(incontent);
        // 打印仪器地址
        printMachineAddress(incontent);
        // 打印命令类型
        switch (incontent[2]) {
            case 0x00:
                System.out.print("系统复位\t");
                // 确认帧
                outmsgcontent[2] = 1;
                break;
            case 0x01:
                System.out.print("设备地址设定\t");
                System.out.print("设备设定地址值：" + incontent[3]);
                // 确认帧
                outmsgcontent[2] = 1;
                // 设备地址码设置成功
                outmsgcontent[3] = 1;
                // 设定地址码
                outmsgcontent[4] = incontent[3];
                break;
            case 0x2:
                System.out.print("温控器地址设定\t");
                String tempcontroler;
                tempcontroler = parseTempControlerName(incontent[3]);
                System.out.print("温控器设定地址：" + tempcontroler);
                // 确认帧
                outmsgcontent[2] = 1;
                // 温控表地址设置成功
                outmsgcontent[3] = 2;
                // 设定地址码
                outmsgcontent[4] = incontent[3];
                break;
            case 0x03:
                System.out.print("试验温度设定\t");
                tempcontroler = parseTempControlerName(incontent[5]);
                System.out.print("温控器设定地址：" + tempcontroler);
                byte[] tempbytearray = new byte[2];
                System.arraycopy(incontent, 3, tempbytearray, 0, 2);
                System.out.print("\t温控器设定值："
                        + BytePlus.parseIntFromByteArray(tempbytearray));
                // 确认帧
                outmsgcontent[2] = 1;
                // 温控表地址设置成功
                outmsgcontent[3] = 4;
                // 设定地址码
                outmsgcontent[4] = incontent[3];
                outmsgcontent[5] = incontent[4];
                break;
            case 0x04:
                // 温控参数设定
                outmsgcontent = temperatureControlerConfig(incontent,
                        outmsgcontent);
                break;
            case 0x05:
                // 传感器标定
                outmsgcontent = sensorCelibration(incontent, outmsgcontent);
                break;
            case 0x06:
                /*
                 * X2=06H 温控开关（是否开启温控单元） X3 =01H 开启；(X4 = 01薄膜 X4 = 02H容器) X3
                 * =02H 关闭；(X4 = 01薄膜 X4 = 02H容器)
                 */
                /*
                 * X2=03H 温控开关确认（是否开启温控单元） X3 =01H 开启确认；(X4 = 01薄膜 X4 = 02H容器)
                 * X3 =02H 关闭确认；(X4 = 01薄膜 X4 = 02H容器)
                 */
                // 温控开关
                System.out.print("温控开关:\t");
                switch (incontent[3]) {
                    case 1:
                        System.out.print("开启\t");
                        break;
                    case 2:
                        System.out.print("关闭\t");
                        break;
                    default:
                        System.out.print("错误的传入参数！！");
                        break;
                }
                switch (incontent[4]) {
                    case 1:
                        System.out.print("薄膜\t");
                        break;
                    case 2:
                        System.out.print("容器\t");
                        break;
                    default:
                        System.out.print("错误的传入参数！！");
                        break;
                }
                // 确认帧
                outmsgcontent[2] = 3;
                // 传感器类别
                outmsgcontent[3] = incontent[3];
                outmsgcontent[4] = incontent[4];
                break;
            case 0x07:
                /*
                 * X2=07H 制冷开关（单加热形式） X3 =01H 开启；(X4 = 01薄膜 X4 = 02H容器) X3 =02H
                 * 关闭；(X4 = 01薄膜 X4 = 02H容器)
                 */
                /*
                 * X2=04H 制冷开关（单加热形式） X3 =01H 开启确认；(X4 = 01薄膜 X4 = 02H容器) X3
                 * =02H 关闭确认；(X4 = 01薄膜 X4 = 02H容器)
                 */
                System.out.print("制冷开关:\t");
                switch (incontent[3]) {
                    case 1:
                        System.out.print("开启\t");
                        break;
                    case 2:
                        System.out.print("关闭\t");
                        break;
                    default:
                        System.out.print("错误的传入参数！！");
                        break;
                }
                switch (incontent[4]) {
                    case 1:
                        System.out.print("薄膜\t");
                        break;
                    case 2:
                        System.out.print("容器\t");
                        break;
                    default:
                        System.out.print("错误的传入参数！！");
                        break;
                }
                // 确认帧
                outmsgcontent[2] = 4;
                // 传感器类别
                outmsgcontent[3] = incontent[3];
                outmsgcontent[4] = incontent[4];
                break;
            case 0x08:
                /*
                 * X2=08H 制冷开启温度点设定（计算机控制） X3X4设定值，高位在前； (X5 = 01薄膜 X5 = 02H容器)
                 */
                /*
                 * X2=09H 制冷开启温度点设定确认 X3X4=设定值，高位在前；(X5 = 01薄膜 X5 = 02H容器)
                 */
                System.out.print("制冷开启温度点设定\t");
                switch (incontent[5]) {
                    case 1:
                        System.out.print("薄膜\t");
                        break;
                    case 2:
                        System.out.print("容器\t");
                        break;
                    default:
                        System.out.print("错误的传入参数！！");
                        break;
                }
                tempbytearray = new byte[2];
                System.arraycopy(incontent, 3, tempbytearray, 0, 2);
                System.out.print("\t设定值："
                        + BytePlus.parseIntFromByteArray(tempbytearray));
                // 确认帧
                outmsgcontent[2] = 9;
                // 传感器类别
                System.arraycopy(incontent, 3, outmsgcontent, 3, 3);
                // outmsgcontent[3] = incontent[3];
                // outmsgcontent[4] = incontent[4];
                break;
            case 0x09:
                /*
                 * X2=09H 试验开始/停止 X3=01开始试验；X3=00H 停止试验
                 */
                /*
                 * X2=01H X3=09H 试验开始/停止确认 X4=01开始试验；X4=00H 停止试验
                 */
                System.out.print("试验开始/停止：\t");
                // 确认帧
                outmsgcontent[2] = 1;
                outmsgcontent[3] = 9;
                outmsgcontent[4] = incontent[3];
                switch (incontent[3]) {
                    case 1:
                        System.out.print("开始试验\t");
                        break;
                    case 0:
                        System.out.print("停止试验\t");
                        break;
                    default:
                        System.out.print("错误的传入参数！！");
                        break;
                }
                break;
            case 0x10:
                System.out.print("确认帧（待用）\t");
                break;
            case 0x11:
                /*
                 * X2=11H 阀控制指令 （X3 X4 X5 X6 X7，对应位为1时打开，0时为关闭）
                 */
                /*
                 * X2=05H 阀控制确认 X3 X4 X5 X6 X7为阀控制字，1=开；0=关闭
                 * （要判断发送的阀控制字与接收到的阀控制字是否相同）
                 */
                System.out.print("阀控制指令:\t");
                // 确认帧
                outmsgcontent[2] = 5;
                for (int k = 3; k < 8; k++) {
                    if (outmsgcontent[k] == 1) {
                        System.out.print((k - 2) + "阀开");
                    } else if (outmsgcontent[k] == 0) {
                        System.out.print((k - 2) + "阀关");
                    } else {
                        System.out.print((k - 2) + "阀错误！");
                    }
                }
                // 复制状态，进行确认
                System.arraycopy(incontent, 3, outmsgcontent, 3, 5);
                break;
            case 0x12:
                //要工程量数据
                outmsgcontent = fetchProjectValueData(incontent, outmsgcontent);

                break;
            case 0x13:
                System.out.print("设备自检\t");
                break;
            case 0x14:
                System.out.print("要保存在设备内的试验数据\t");
                break;
            case 0x15:
                System.out.print("升降透湿杯托盘（无需确认帧，调试）\t");
                break;
            case 0x16:
                System.out.print("透湿杯托盘旋转命令（无需确认帧，顺时针）\t");
                break;
            case 0x17:
                System.out.print("计算机、设备日期同步指令（试验启动时发送）\t");
                break;
            case 0x18:
                System.out.print("试验湿度设定 (湿度控制使用)\t");
                break;
            case 0x19:
                System.out.print("测试间隔设定\t");
                break;
            case 0x1a:
                System.out.print("试验腔编码\t");
                break;
            case 0x1b:
                System.out.print("循环泵控制\t");
                break;
            case 0x1c:
                System.out.print("膜标定系数（存）\t");
                break;
            case 0x1d:
                System.out.print("要膜标定系数（取）\t");
                
//                for (int i = 0; i < incontent.length; i++) {
//                    byte b = incontent[i];
//                    System.out.println("incontent"+i+":"+Integer.toHexString(b));
//                }
                outmsgcontent[2]=0x0c;//X2=0CH 膜标定系数
                switch (incontent[3]) {
                    case 1:
                        System.out.println("腔1膜标定系数");
                        outmsgcontent[3]=01;
                        outmsgcontent[4]=03;
                        outmsgcontent[5]=(byte)0xE7;
                        break;
                    case 2:
                        outmsgcontent[3]=02;
                        outmsgcontent[4]=03;
                        outmsgcontent[5]=(byte)0xE7;
                        System.out.println("腔2膜标定系数");
                        break;
                    case 3:
                        outmsgcontent[3]=03;
                        outmsgcontent[4]=03;
                        outmsgcontent[5]=(byte)0xE7;
                        System.out.println("腔3膜标定系数");
                        break;
                    default:
                        System.out.print("错误数据。。。");
                        break;
                }
                break;
            default:
                System.out.print("发送的指令不正确:\t");
        }
        System.out.println();
        // OX2230W3330Message pkt = new OX2230W3330Message();
        //	
        // byte bt[] = {1,123,93,23,4,-90,43,0,1};
        // pkt.setContent(bt);
        //	
        // WriteFuture wf = session.write(pkt);
        // wf.awaitUninterruptibly();
        outmsg.setContent(outmsgcontent);
        WriteFuture wf = session.write(outmsg);
        wf.awaitUninterruptibly();
    }

    private byte[] fetchProjectValueData(byte[] incontent, byte[] outmsgcontent) {
        /*
         * X3 数据组装模式 X3=01H 要单项工程量 X4 传感器类别 X4=01H 温度（Pt100） X4=02H 湿度（RH）
         * X4=03H 压力（气压） X4=04H 氧浓度 (PPM) X4=05H 电解池 (PPM) X4=06H 流量 X4=07H 重量g
         * （天平标定查看） 。。。 X5 传感器序号 (多个同规格传感器的定位码，从1开始定义)； 序号定义：
         * 温度：序号1，试验腔；序号2，传感器；序号3，环境4，容器。 湿度：序号1，上腔；序号2 下腔
         * 压力：序号1，上腔；序号2，下腔；序号3，补偿压。 流量：序号1，上腔；序号2，下腔。 重量：（天平标定开始指令；天平重量查看时使用）
         * 
         * X3=02H 要工程量组（每组内的工程量排序：从1开始，顺序排列）
         * 
         * X4=01H 温度组（Pt100） X4=02H 湿度组（RH） X4=03H 压力组（气压） X4=04H 氧浓度组 (PPM)
         * X4=05H 电解池组 (PPM) X4=06H 流量组 X4=07H 重量g组
         */
        /*
         * X2=06H 发送工程量数据 X3 工程量发送模式 X3=01H 单项工程量发送 X4 传感器类别 X4=01H 温度（Pt100）
         * X4=02H 湿度（RH） X4=03H 压力（气压） X4=04H 氧浓度 (PPM) X4=05H 电解池 (PPM) X4=06H
         * 流量 X4=07H 重量g （天平标定查看） 。。。
         * 
         * X5 传感器序号 (多个同规格传感器的定位码)； X6 X7 X8 X9 传感器数据，X10 小数点位数m（/10^m） X11 状态标志
         * （要先查询状态标志，再执行相应操作） X11= 00H 无效数据 X11= 01H 数据错误 X11= 02H 欠量程 X11= 03H
         * 超量程 X11= 04H 校验结束 X11= 05H 有效数据 X11= 06H 置砝码 X11= 07H 5秒没收到数据
         * 
         * 
         * X3=02H工程量组发送（蓝色标识字节为小数点位数m（/10^m）） X4工程量组类别(高位在前) X4=01H 温度组（Pt100）
         * X5X6X7 X8X9X10 X11X12X13 X14X15X16 X17X18X19 温度1 温度2 温度3 温度4 温度5
         * X4=02H 湿度组（RH） X5X6X7 X8X9X10 X11X12X13 X14X15X16 X17X18X19 湿度1 湿度2
         * 湿度3 湿度4 湿度5
         * 
         * X4=03H 压力组（气压） X5X6X7 X8X9X10 X11X12X13 X14X15X16 X17X18X19 压力1 压力2
         * 压力3 压力4 压力5
         * 
         * X4=04H 氧浓度组 (PPM) X5X6X7X8 X9X10X11X12 X13X14X15X16 浓度1 浓度2 浓度3
         * 
         * X4=05H 电解池组 (PPM) X5X6X7X8 X9X10X11X12 X13X14X15X16 浓度1 浓度2 浓度3
         * X4=06H 流量组 X5X6X7 X8X9X10 X11X12X13 X14X15X16 流量1 流量2 流量3 流量4
         * 
         * X4=07H 重量g组 X5 X6X7X8X9 X10X11 X12 X13 X14 X15 X16
         * 
         * X5 透湿杯编号（透湿杯编号=0，表示无试验数据或数据未刷新） X6X7X8重量g, X9 重量小数点位数m（/10^m）） X10X11
         * 年 X12 月 X13 日 X14 时 X15 分 X16 秒 X17 状态标志 （要先查询状态标志，再执行相应操作） X17= 00H
         * 无效数据 X17= 01H 数据错误 X17= 02H 欠量程 X17= 03H 超量程 X17= 04H 校验结束 X17= 05H
         * 有效数据 X17= 06H 置砝码 X17= 07H 5秒没收到数据
         */
        Random r = new Random(System.currentTimeMillis());
        System.out.print("要工程量数据:\t");
        // 确认帧
        outmsgcontent[2] = 6;
        // X3 数据组装模式,工程量发送模式
        // outmsgcontent[3] = incontent[3];
        // 传感器类别,传感器序号
        System.arraycopy(incontent, 3, outmsgcontent, 3, 3);
        switch (incontent[3]) {
            case 1:
                System.out.print("要单项工程量\t传感器类别\t");
                // 传感器类别
                switch (incontent[4]) {
                    case 1:
                        System.out.print("温度（Pt100）");
                        break;

                    case 2:
                        System.out.print("湿度（RH）");
                        break;

                    case 3:
                        System.out.print("压力（气压）");
                        break;

                    case 4:
                        System.out.print("氧浓度 (PPM)");
                        break;

                    case 5:
                        System.out.print("电解池 (PPM)");
                        break;

                    case 6:
                        System.out.print("流量");
                        break;

                    case 7:
                        System.out.print("重量g");
                        break;

                    default:
                        System.out.print("错误！！");
                        break;
                }
                /*
                 * X5 传感器序号 (多个同规格传感器的定位码，从1开始定义)； 序号定义：
                 * 温度：序号1，试验腔；序号2，传感器；序号3，环境4，容器。 湿度：序号1，上腔；序号2 下腔
                 * 压力：序号1，上腔；序号2，下腔；序号3，补偿压。 流量：序号1，上腔；序号2，下腔。
                 * 重量：（天平标定开始指令；天平重量查看时使用）
                 */
                System.out.print("\t传感器序号：" + outmsgcontent[5]);

                byte[] k = BytePlus.int2bytes(r.nextInt(0x77777777));
                // 传感器数据
                System.arraycopy(k, 0, outmsgcontent, 6, 4);
                // 小数点位数m
                outmsgcontent[10] = (byte) r.nextInt(20);
                // X11 状态标志,全部以有效数据回复
				/*
                 * X11= 00H 无效数据 X11= 01H 数据错误 X11= 02H 欠量程 X11= 03H 超量程 X11=
                 * 04H 校验结束 X11= 05H 有效数据 X11= 06H 置砝码 X11= 07H 5秒没收到数据
                 */
                outmsgcontent[11] = 5;
                break;
            case 2:
                System.out.print("要工程量组\t传感器类别\t");
                // 传感器类别
                switch (incontent[4]) {

                    case 1:
                        System.out.print("温度组（Pt100）");
                        //温度1
                        k = BytePlus.int2bytes(r.nextInt(0x00ffffff));
                        System.arraycopy(k, 1, outmsgcontent, 5, 3);
                        //温度2
                        k = BytePlus.int2bytes(r.nextInt(0x00ffffff));
                        System.arraycopy(k, 1, outmsgcontent, 8, 3);
                        //温度3
                        k = BytePlus.int2bytes(r.nextInt(0x00ffffff));
                        System.arraycopy(k, 1, outmsgcontent, 11, 3);
                        //温度4
                        k = BytePlus.int2bytes(r.nextInt(0x00ffffff));
                        System.arraycopy(k, 1, outmsgcontent, 14, 3);
                        //温度5
                        k = BytePlus.int2bytes(r.nextInt(0x00ffffff));
                        System.arraycopy(k, 1, outmsgcontent, 17, 3);
                        break;

                    case 2:
                        System.out.print("湿度组（RH）");
                        //湿度1
                        k = BytePlus.int2bytes(r.nextInt(0x00ffffff));
                        System.arraycopy(k, 1, outmsgcontent, 5, 3);
                        //湿度2
                        k = BytePlus.int2bytes(r.nextInt(0x00ffffff));
                        System.arraycopy(k, 1, outmsgcontent, 8, 3);
                        //湿度3
                        k = BytePlus.int2bytes(r.nextInt(0x00ffffff));
                        System.arraycopy(k, 1, outmsgcontent, 11, 3);
                        //湿度4
                        k = BytePlus.int2bytes(r.nextInt(0x00ffffff));
                        System.arraycopy(k, 1, outmsgcontent, 14, 3);
                        //湿度5
                        k = BytePlus.int2bytes(r.nextInt(0x00ffffff));
                        System.arraycopy(k, 1, outmsgcontent, 17, 3);
                        break;

                    case 3:
                        System.out.print("压力组（气压）");
                        //压力1
                        k = BytePlus.int2bytes(r.nextInt(0x00ffffff));
                        System.arraycopy(k, 1, outmsgcontent, 5, 3);
                        //压力2
                        k = BytePlus.int2bytes(r.nextInt(0x00ffffff));
                        System.arraycopy(k, 1, outmsgcontent, 8, 3);
                        //压力3
                        k = BytePlus.int2bytes(r.nextInt(0x00ffffff));
                        System.arraycopy(k, 1, outmsgcontent, 11, 3);
                        //压力4
                        k = BytePlus.int2bytes(r.nextInt(0x00ffffff));
                        System.arraycopy(k, 1, outmsgcontent, 14, 3);
                        //压力5
                        k = BytePlus.int2bytes(r.nextInt(0x00ffffff));
                        System.arraycopy(k, 1, outmsgcontent, 17, 3);
                        break;

                    case 4:
                        System.out.print("氧浓度组 (PPM)");
                        //氧浓度1
                        k = BytePlus.int2bytes(r.nextInt(0x00ffffff));
                        System.arraycopy(k, 0, outmsgcontent, 5, 4);
                        //氧浓度2
                        k = BytePlus.int2bytes(r.nextInt(0x00ffffff));
                        System.arraycopy(k, 0, outmsgcontent, 9, 4);
                        //氧浓度3
                        k = BytePlus.int2bytes(r.nextInt(0x00ffffff));
                        System.arraycopy(k, 0, outmsgcontent, 13, 4);
                        break;

                    case 5:
                        System.out.print("电解池组 (PPM)");
                        //浓度1
                        k = BytePlus.int2bytes(r.nextInt(0x00ffffff));
                        System.arraycopy(k, 0, outmsgcontent, 5, 4);
                        //浓度2
                        k = BytePlus.int2bytes(r.nextInt(0x00ffffff));
                        System.arraycopy(k, 0, outmsgcontent, 9, 4);
                        //浓度3
                        k = BytePlus.int2bytes(r.nextInt(0x00ffffff));
                        System.arraycopy(k, 0, outmsgcontent, 13, 4);
                        break;

                    case 6:
                        System.out.print("流量组");
                        //流量1
                        k = BytePlus.int2bytes(r.nextInt(0x00ffffff));
                        System.arraycopy(k, 1, outmsgcontent, 5, 3);
                        //流量2
                        k = BytePlus.int2bytes(r.nextInt(0x00ffffff));
                        System.arraycopy(k, 1, outmsgcontent, 8, 3);
                        //流量3
                        k = BytePlus.int2bytes(r.nextInt(0x00ffffff));
                        System.arraycopy(k, 1, outmsgcontent, 11, 3);
                        //流量4
                        k = BytePlus.int2bytes(r.nextInt(0x00ffffff));
                        System.arraycopy(k, 1, outmsgcontent, 14, 3);
                        break;

                    case 7:
                        System.out.print("重量g组");
                        //透湿杯编号1
                        k = BytePlus.int2bytes(r.nextInt(4));
                        System.arraycopy(k, 3, outmsgcontent, 5, 1);
                        //重量
                        k = BytePlus.int2bytes(r.nextInt(0x00ffffff));
                        System.arraycopy(k, 1, outmsgcontent, 6, 3);
                        //X9 重量小数点位数m（/10^m））
                        k = BytePlus.int2bytes(r.nextInt(5));
                        System.arraycopy(k, 0, outmsgcontent, 9, 1);
                        Calendar cal = Calendar.getInstance();

                        //流量3
                        k = BytePlus.int2bytes(cal.get(Calendar.YEAR));
                        System.arraycopy(k, 2, outmsgcontent, 11, 2);
                        outmsgcontent[12] = (byte) cal.get(Calendar.MONTH);
                        outmsgcontent[13] = (byte) cal.get(Calendar.DAY_OF_MONTH);
                        outmsgcontent[14] = (byte) cal.get(Calendar.HOUR_OF_DAY);
                        outmsgcontent[15] = (byte) cal.get(Calendar.MINUTE);
                        outmsgcontent[16] = (byte) cal.get(Calendar.SECOND);
                        //流量4
                        k = BytePlus.int2bytes(r.nextInt(0x00ffffff));
                        System.arraycopy(k, 1, outmsgcontent, 14, 3);
                        // X11 状态标志,全部以有效数据回复
						/*
                         * X11= 00H 无效数据 X11= 01H 数据错误 X11= 02H 欠量程 X11= 03H 超量程 X11=
                         * 04H 校验结束 X11= 05H 有效数据 X11= 06H 置砝码 X11= 07H 5秒没收到数据
                         */
                        outmsgcontent[17] = 5;
                        break;

                    default:
                        System.out.print("错误！！");
                        break;
                }
                break;

            default:
                System.out.print("出错！！");
                break;
        }

        return outmsgcontent;
    }

    /**
     * 温控参数设定
     *
     * @param incontent
     * @param outmsgcontent
     */
    private byte[] temperatureControlerConfig(byte[] incontent,
            byte[] outmsgcontent) {
        String tempcontroler;
        byte[] tempbytearray;
        System.out.print("温控参数设定\t");
        tempcontroler = parseTempControlerName(incontent[3]);
        System.out.print("温控器设定地址：" + tempcontroler);
        tempbytearray = new byte[2];
        System.arraycopy(incontent, 5, tempbytearray, 0, 2);
        // 确认帧
        outmsgcontent[2] = 1;
        // 温度设置成功
        outmsgcontent[3] = 4;
        switch (incontent[4]) {
            case 0x01:
                System.out.print("\tP参数设定，值："
                        + BytePlus.parseIntFromByteArray(tempbytearray));
                System.arraycopy(incontent, 4, outmsgcontent, 4, 3);
                break;
            case 0x02:
                System.out.print("\tI参数设定，值："
                        + BytePlus.parseIntFromByteArray(tempbytearray));
                System.arraycopy(incontent, 4, outmsgcontent, 4, 3);
                break;
            case 0x03:
                System.out.print("\tD参数设定，值："
                        + BytePlus.parseIntFromByteArray(tempbytearray));
                System.arraycopy(incontent, 4, outmsgcontent, 4, 3);
                break;
            case 0x04:
                System.out.print("\t自整定设定:");
                if (incontent[5] == 01) {
                    System.out.print("启动");
                } else if (incontent[5] == 02) {
                    System.out.print("关闭");
                } else {
                }
                outmsgcontent[4] = incontent[4];
                break;
            case 0x05:
                System.out.print("\t温度平移修正Sc，值："
                        + BytePlus.parseIntFromByteArray(tempbytearray));
                break;
            default:
                System.out.print("错误的传入参数");
                break;
        }
        return outmsgcontent;
    }

    /**
     * 传感器标定
     *
     * @param incontent
     * @param outmsgcontent
     */
    private byte[] sensorCelibration(byte[] incontent, byte[] outmsgcontent) {
        byte[] tempbytearray;
        System.out.print("传感器标定\t");
        // 确认帧
        outmsgcontent[2] = 2;
        // 传感器类别
        outmsgcontent[3] = incontent[3];
        System.out.print("传感器类别：");
        switch (incontent[3]) {

            case 1:
                System.out.print("温度（Pt100）");
                break;

            case 2:
                System.out.print("湿度（RH）");
                break;

            case 3:
                System.out.print("压力（气压）");
                break;

            case 4:
                System.out.print("氧浓度 (PPM)");
                break;

            case 5:
                System.out.print("电解池 (PPM)");
                break;

            case 6:
                System.out.print("流量");
                break;

            case 7:
                System.out.print("重量g");
                break;

            default:
                System.out.print("错误！！");
                break;
        }
        // 传感器序号
		/*
         * 序号定义： 温度：序号1，试验腔；序号2，传感器；序号3，环境4：容器。 湿度：序号1，上腔；序号2 下腔
         * 压力：序号1，上腔；序号2，下腔；序号3，补偿压。 流量：序号1，上腔；序号2，下腔。 氧量：序号1，0～20ppm;
         * 序号2，20～200ppm; 序号3，200～2000ppm;
         */
        outmsgcontent[4] = incontent[4];
        System.out.print("\t传感器序号：" + outmsgcontent[4]);
        // 标定零点或终点
        outmsgcontent[5] = incontent[5];
        switch (outmsgcontent[5]) {
            case 1:
                System.out.print("\t标定零点：");
                break;
            case 2:
                System.out.print("\t标定终点：");
                break;

            default:
                System.out.print("\t标定点错误！：");
                break;
        }
        tempbytearray = new byte[2];
        System.arraycopy(incontent, 6, tempbytearray, 0, 3);
        System.out.print("\t输入值："
                + BytePlus.parseIntFromByteArray(tempbytearray));
        System.out.print("\t小数点位数：" + BytePlus.parseByteToUInt(incontent[9]));
        return outmsgcontent;
    }

    /**
     * 处理温控器名字
     *
     * @param incontent
     * @return
     */
    private String parseTempControlerName(byte incontent) {
        String tempcontroler;
        switch (incontent) {
            case 1:
                tempcontroler = "试验腔温度";
                break;
            case 2:
                tempcontroler = "传感器温度";
                break;
            case 3:
                tempcontroler = "环境温度";
                break;
            case 4:
                tempcontroler = "容器温度";
                break;
            default:
                tempcontroler = "发送地址错误：" + incontent;
        }
        return tempcontroler;
    }

    private void printMachineAddress(byte[] incontent) {
        switch ((short) incontent[1]) {
            case 0x0:
                System.out.print("设备出厂默认地址\t");
                break;
            case 0x40:
                System.out.print("主机使用地址\t");
                break;
            case 0xFF:
                if (incontent[0] == 0x0d) {
                } else {
                    System.out.print("广播地址\t");
                    break;
                }
            case 0x30:
                if (incontent[0] == 0x0d) {
                    System.out.print("广播地址\t");
                    break;
                }

            default:
                System.out.print("设备地址:" + incontent[0] + "\t");
        }
    }

    private void printMechineType(byte[] incontent) {
        switch (incontent[0]) {
            case 0x01:
                System.out.print("230信息:\t");
                break;
            case 0x02:
                System.out.print("330信息:\t");
                break;
            case 0x03:
                System.out.print("VAC-V1信息:\t");
                break;
            case 0x04:
                System.out.print("VAC-VBS信息:\t");
                break;
            case 0x05:
                System.out.print("VAC-V2信息:\t");
                break;
            case 0x06:
                System.out.print("VAC-V3信息:\t");
                break;
            case 0x07:
                System.out.print("TSY-T1信息:\t");
                break;
            case 0x08:
                System.out.print("TSY-T3信息:\t");
                break;
            case 0x09:
                System.out.print("TSY-W3信息:\t");
                break;
            case 0x0A:
                System.out.print("TSY-W3/3信息:\t");
                break;
            case 0x0B:
                System.out.print("TSY-W1信息:\t");
                break;
            case 0x0C:
                System.out.print("TSY-W2信息:\t");
                break;
            case 0x0D:
                System.out.print("PERME-W3/060信息:\t");
                break;
            default:
                System.out.print("发送信息不正确:\t");
        }

    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("session closed");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception {
        System.out.println("session idled:" + status.toString());
    }
    // public static void main(String[] args) {
    // byte a = (byte) 0x80;
    // int b = a < 0 ? a + 256 : a;
    // System.out.println(b);
    // }
}
