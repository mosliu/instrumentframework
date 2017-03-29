package net.labthink.instrument.device.VACVBS.handler;

//~--- non-JDK imports --------------------------------------------------------
import net.labthink.instrument.device.VACVBS.message.VACVBSInMessage;
import net.labthink.instrument.device.VACVBS.message.VACVBSOutMessage;
import net.labthink.instrument.device.VACVBS.simulator.VACVBS;
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
import java.util.Timer;
import java.util.TimerTask;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * VACVBS's 信号处理
 *
 * @version        1.0.0.0, 2010/08/20
 * @author         Moses
 */
public class VACVBSHandler extends IoHandlerAdapter {

    public boolean allerror = false;
    int errorcount = 0;
    private static final String STORE_FILE = "VACVBSsave.dat";
    boolean startsender = false;
    long starttime = 0;
    SenderTask st = null;
    Timer timer = null;
    /** 每次发送后等待时间。 */
    private int waittime = 100;

    /* 仪器 */
    private VACVBS device = new VACVBS();

    /**
     * 构造函数
     *
     */
    public VACVBSHandler() {
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
        super.exceptionCaught(session, cause);
    }

    public static void main(String[] args) {       
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        VACVBSInMessage msg = (VACVBSInMessage) message;
        byte[] incontent = msg.getContent();
        VACVBSOutMessage outmsg = new VACVBSOutMessage();
        byte[] outmsgcontent = outmsg.getContent();
        byte tempbyte;
        byte tempbyte2;
        byte[] tempbytes;


        switch (incontent[0]) {
            case 0x0:
                /*试验*/
                System.out.println("试验");
                getDevice().setDevicepower(true);
                getDevice().setTesting(true);
                startsendertimer(session);
                break;
            case 0x1:
                /*停止*/
                System.out.println("停止");
                //X1:02 确认帧标志
                outmsgcontent[0] = 0x02;
                //X5:21H 停止确认帧
                outmsgcontent[4] = 0x21;
                //从第5位清空返回包，防止错误数据
                BytePlus.fillcontent(outmsgcontent, 5);
                startsender = false;
                break;
            case 0x2:
                /*ACK确认帧*/
                System.out.println("ACK确认帧");
                break;
            case 0x3:
                /*硬件检测*/
                System.out.println("硬件检测");
                break;
            case 0x4:
                /*系统复位*/
                System.out.println("系统复位");
                //X1:04 系统复位确认帧
                outmsgcontent[0] = 0x04;
                //从第1位清空返回包，防止错误数据
                BytePlus.fillcontent(outmsgcontent, 1);
                break;
            case 0x5:
                /*阀体控制, (X2X3位控制方式,高位在前,0 关闭；1开启) (X4：从高到低：Lower、Lower&Up、Test，0－熄灭，1－点亮) */
                System.out.println("阀体控制, (X2X3位控制方式,高位在前,0 关闭；1开启) (X4：从高到低：Lower、Lower&Up、Test，0－熄灭，1－点亮) ");
                //X1:02 确认帧标志
                outmsgcontent[0] = 0x02;
                //X5:20H 阀体ACK帧(X2X3阀的控制位，即试验状态)
                outmsgcontent[4] = 0x20;
                // (X2X3位控制方式,高位在前,0 关闭；1开启)
                System.arraycopy(incontent, 1, outmsgcontent, 1, 2);
                //设定 阀门状态
                tempbytes = new byte[2];
                System.arraycopy(incontent, 1, tempbytes, 0, 2);
                getDevice().setValves(tempbytes);
                //设定 指示灯
                getDevice().setLeds(incontent[3]);
                //从第5位清空返回包，防止错误数据
                if (incontent[3] == 01) {
                    //startsendertimer(session);
                }

                BytePlus.fillcontent(outmsgcontent, 5);
                break;
            case 0x6:
                /*通信诊断*/
                System.out.println("通信诊断");
                //06   通信诊断确认
                outmsgcontent[0] = 0x06;
                ////TODO X2X3高位在前，两位小数

                //从第1位清空返回包，防止错误数据
                BytePlus.fillcontent(outmsgcontent, 1);
                break;
            case 0x7:
                /*向设备索要膜标定系数*/
                System.out.println("向设备索要膜标定系数");
                //X1:07   膜标定系数  X2X3高位在前，两位小数
                outmsgcontent[0] = 0x07;
                ////TODO X2X3高位在前，两位小数

                //设定 标定系数
                tempbytes = getDevice().getFactor();
                System.arraycopy(tempbytes, 0, outmsgcontent, 1, 2);
                //从第1位清空返回包，防止错误数据
                BytePlus.fillcontent(outmsgcontent, 3);
                break;
            case 0x0C:
                /*标定命令!*/
                System.out.println("标定命令!");
                //具体解析该命令
                switch (incontent[1]) {
                    case 0x10:
                        /*标定进入*/
                        System.out.println("标定进入");
                        //X1:02 确认帧标志
                        outmsgcontent[0] = 0x02;
                        //X5:22H 标定进入
                        outmsgcontent[4] = 0x22;
                        //从第5位清空返回包，防止错误数据
                        BytePlus.fillcontent(outmsgcontent, 5);
                        break;
                    case 0x11:
                        /*温度零点 11H    (X3X4高位在前)*/
                        System.out.println("温度零点 11H    (X3X4高位在前)");
                        //X1:02 确认帧标志
                        outmsgcontent[0] = 0x02;
                        //X5:24H 温度零点 (X3X4高位在前)
                        outmsgcontent[4] = 0x24;
                        ////TODO 温度零点 (X3X4高位在前)
                        System.arraycopy(incontent, 2, outmsgcontent, 2, 2);
                        //设定 温度零点
                        tempbytes = new byte[2];
                        System.arraycopy(incontent, 2, tempbytes, 0, 2);
                        getDevice().setTempzero(tempbytes);
                        //从第5位清空返回包，防止错误数据
                        BytePlus.fillcontent(outmsgcontent, 5);
                        break;
                    case 0x12:
                        /*温度终点 12H    (X3X4高位在前)*/
                        System.out.println("温度终点 12H    (X3X4高位在前)");
                        //X1:02 确认帧标志
                        outmsgcontent[0] = 0x02;
                        //25H 温度终点 (X3X4高位在前)
                        outmsgcontent[4] = 0x25;
                        ////TODO 温度终点 (X3X4高位在前)
                        System.arraycopy(incontent, 2, outmsgcontent, 2, 2);
                        //设定 温度终点
                        tempbytes = new byte[2];
                        System.arraycopy(incontent, 2, tempbytes, 0, 2);
                        getDevice().setTempend(tempbytes);
                        //从第5位清空返回包，防止错误数据
                        BytePlus.fillcontent(outmsgcontent, 5);
                        break;
                    case 0x13:
                        /*湿度零点         13H    (X3X4高位在前)*/
                        System.out.println("湿度零点         13H    (X3X4高位在前)");
                        //X1:02 确认帧标志
                        outmsgcontent[0] = 0x02;
                        //X5:26H 湿度零点 (X3X4高位在前)
                        outmsgcontent[4] = 0x26;
                        ////TODO 湿度零点 (X3X4高位在前)
                        System.arraycopy(incontent, 2, outmsgcontent, 2, 2);
                        //设定 温度零点
                        tempbytes = new byte[2];
                        System.arraycopy(incontent, 2, tempbytes, 0, 2);
                        getDevice().setHumidityzero(tempbytes);
                        //从第5位清空返回包，防止错误数据
                        BytePlus.fillcontent(outmsgcontent, 5);
                        break;
                    case 0x14:
                        /*湿度终点         14H    (X3X4高位在前)*/
                        System.out.println("湿度终点         14H    (X3X4高位在前)");
                        //X1:02 确认帧标志
                        outmsgcontent[0] = 0x02;
                        //X5:27H 湿度终点 (X3X4高位在前)
                        outmsgcontent[4] = 0x27;
                        ////TODO 湿度终点 (X3X4高位在前)
                        System.arraycopy(incontent, 2, outmsgcontent, 2, 2);
                        //设定 湿度终点
                        tempbytes = new byte[2];
                        System.arraycopy(incontent, 2, tempbytes, 0, 2);
                        getDevice().setHumidityend(tempbytes);
                        //从第5位清空返回包，防止错误数据
                        BytePlus.fillcontent(outmsgcontent, 5);
                        break;
                    case 0x15:
                        /*高压零点         15H    (X3X4高位在前)*/
                        System.out.println("高压零点         15H    (X3X4高位在前)");
                        //X1:02 确认帧标志
                        outmsgcontent[0] = 0x02;
                        //X5:28H 高压零点 (X3X4高位在前)
                        outmsgcontent[4] = 0x28;
                        ////TODO 高压零点 (X3X4高位在前)
                        System.arraycopy(incontent, 2, outmsgcontent, 2, 2);
                        //设定 高压零点
                        tempbytes = new byte[2];
                        System.arraycopy(incontent, 2, tempbytes, 0, 2);
                        getDevice().setHighpressurezero(tempbytes);
                        //从第5位清空返回包，防止错误数据
                        BytePlus.fillcontent(outmsgcontent, 5);
                        break;
                    case 0x16:
                        /*高压终点         16H    (X3X4高位在前) */
                        System.out.println("高压终点         16H    (X3X4高位在前) ");
                        //X1:02 确认帧标志
                        outmsgcontent[0] = 0x02;
                        //X5:29H   高压终点 (X3X4高位在前)
                        outmsgcontent[4] = 0x29;
                        ////TODO 高压终点 (X3X4高位在前)
                        System.arraycopy(incontent, 2, outmsgcontent, 2, 2);
                        //设定 高压终点
                        tempbytes = new byte[2];
                        System.arraycopy(incontent, 2, tempbytes, 0, 2);
                        getDevice().setHighpressureend(tempbytes);
                        //从第5位清空返回包，防止错误数据
                        BytePlus.fillcontent(outmsgcontent, 5);
                        break;
                    case 0x17:
                        /*低压零点         17H    (X3X4高位在前)*/
                        System.out.println("低压零点         17H    (X3X4高位在前)");
                        //X1:02 确认帧标志
                        outmsgcontent[0] = 0x02;
                        //X5:2AH 低压零点 (X3X4高位在前)
                        outmsgcontent[4] = 0x2A;
                        ////TODO 低压零点 (X3X4高位在前)
                        System.arraycopy(incontent, 2, outmsgcontent, 2, 2);
                        //设定 低压零点
                        tempbytes = new byte[2];
                        System.arraycopy(incontent, 2, tempbytes, 0, 2);
                        getDevice().setLowpressurezero(tempbytes);
                        //从第5位清空返回包，防止错误数据
                        BytePlus.fillcontent(outmsgcontent, 5);
                        break;
                    case 0x18:
                        /*低压终点         18H    (X3X4高位在前)*/
                        System.out.println("低压终点         18H    (X3X4高位在前)");
                        //X1:02 确认帧标志
                        outmsgcontent[0] = 0x02;
                        //X5:2BH 低压终点 (X3X4高位在前)
                        outmsgcontent[4] = 0x2B;
                        ////TODO 低压终点 (X3X4高位在前)
                        System.arraycopy(incontent, 2, outmsgcontent, 2, 2);
                        //设定 低压终点
                        tempbytes = new byte[2];
                        System.arraycopy(incontent, 2, tempbytes, 0, 2);
                        getDevice().setLowpressureend(tempbytes);
                        //从第5位清空返回包，防止错误数据
                        BytePlus.fillcontent(outmsgcontent, 5);
                        break;
                    case 0x23:
                        /*保存膜标定系数到设备   23H    X3X4高位在前，两位小数*/
                        System.out.println("保存膜标定系数到设备   23H    X3X4高位在前，两位小数");
                        //X1:02 确认帧标志
                        outmsgcontent[0] = 0x02;
                        //X5:2CH  膜标定系数保存确认帧
                        outmsgcontent[4] = 0x24;
                        //设定 标定系数
                        tempbytes = new byte[2];
                        System.arraycopy(incontent, 2, tempbytes, 0, 2);
                        getDevice().setFactor(tempbytes);
                        //从第5位清空返回包，防止错误数据
                        BytePlus.fillcontent(outmsgcontent, 5);
                        break;
                    default:
                        System.out.println("错误，不能解析的指令");
                }
                break;
            case 0x0D:
                /*标定退出*/
                System.out.println("标定退出");
                //X1:02 确认帧标志
                outmsgcontent[0] = 0x02;
                //X5:23H 标定退出
                outmsgcontent[4] = 0x23;
                //从第5位清空返回包，防止错误数据
                BytePlus.fillcontent(outmsgcontent, 5);
                break;
            case 0x0E:
                /*0E 设定压力（X2X3设定值，高位在前，两位小数）*/
                System.out.println("0E 设定压力（X2X3设定值，高位在前，两位小数）");
                //X1:08	压力设定成功
                outmsgcontent[0] = 0x08;
                //设定 设定压力
                tempbytes = new byte[2];
                System.arraycopy(incontent, 1, tempbytes, 0, 2);
                getDevice().setPressuresetpoint(tempbytes);
                //从第1位清空返回包，防止错误数据
                BytePlus.fillcontent(outmsgcontent, 1);
                break;
            case 0x0F:
                /*0F 设定压力修正值（X2X3设定值，高位在前，两位小数）*/
                System.out.println("0F 设定压力修正值（X2X3设定值，高位在前，两位小数）");
                //X1:09	压力修正设定成功
                outmsgcontent[0] = 0x09;
                //设定 设定压力修正值
                tempbytes = new byte[2];
                System.arraycopy(incontent, 1, tempbytes, 0, 2);
                getDevice().setPressuresetpointfix(tempbytes);
                //从第1位清空返回包，防止错误数据
                BytePlus.fillcontent(outmsgcontent, 1);
                break;
            case 0x10:
                /* 10 抽空持续时间，毫秒 （X2X3设定值）*/
                System.out.println(" 10 抽空持续时间，毫秒 （X2X3设定值）");
                //X1:10 抽空持续时间设定成功
                outmsgcontent[0] = 0x10;
                //设定 抽空持续时间，毫秒
                tempbytes = new byte[2];
                System.arraycopy(incontent, 1, tempbytes, 0, 2);
                getDevice().setPumpouttime(tempbytes);
                //从第1位清空返回包，防止错误数据
                BytePlus.fillcontent(outmsgcontent, 1);
                break;
            case 0x11:
                /* 11 控制幅值，	单位：kPa（X2X3设定值，高位在前，两位小数）*/
                System.out.println(" 11 控制幅值，	单位：kPa（X2X3设定值，高位在前，两位小数）");
                //X1:11 控制幅值设定成功
                outmsgcontent[0] = 0x11;
                //设定 控制幅值，	单位：kPa（X2X3设定值，高位在前，两位小数）
                tempbytes = new byte[2];
                System.arraycopy(incontent, 1, tempbytes, 0, 2);
                getDevice().setControlamplitude(tempbytes);
                //从第1位清空返回包，防止错误数据
                BytePlus.fillcontent(outmsgcontent, 1);
                break;
            case 0x12:
                /*12 控制周期，单位：秒（X2X3设定值）*/
                System.out.println("12 控制周期，单位：秒（X2X3设定值）");
                //X1: 12 控制周期设定成功
                outmsgcontent[0] = 0x12;
                //设定 控制周期，单位：秒（X2X3设定值）
                tempbytes = new byte[2];
                System.arraycopy(incontent, 1, tempbytes, 0, 2);
                getDevice().setControlperiod(tempbytes);
                //从第1位清空返回包，防止错误数据
                BytePlus.fillcontent(outmsgcontent, 1);
                break;
            default:
                System.out.println("错误，不能解析的指令");
        }

        outmsg.setContent(outmsgcontent);


        //byte[]  errorpkt1 = {(byte)0x01,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x0B,(byte)0x96,(byte)0x05,(byte)0xA8,(byte)0x06,(byte)0x59,(byte)0xAE};
        byte[] errorpkt = {(byte) 0x01, (byte) 0x00};
        if (allerror) {
            System.out.println("输出错误帧");
            outmsg.setContent(errorpkt);
        }
        //errorcount ++;
//        if(false&&errorcount%50==0){
//            System.out.println("输出错误的一帧");
//            outmsg.setContent(errorpkt1);
//            WriteFuture wf = session.write(outmsg);
//            wf.awaitUninterruptibly();
//        }




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
    public VACVBS getDevice() {
        return device;
    }

    /**
     * @param device the device to set
     */
    public void setDevice(VACVBS device) {
        this.device = device;
    }

    /**
     * 保存VirtualDevice机器数据
     */
    public void saveData() {
        try {
            ObjectOutputStream out = null;

            out = new ObjectOutputStream(new FileOutputStream(STORE_FILE));
            out.writeObject(device);
        } catch (IOException ex) {
            Logger.getLogger(VACVBSHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadData() {
        try {
            File f = new File(STORE_FILE);

            if (f.exists()) {
                FileInputStream fis = new FileInputStream(f);
                ObjectInputStream in = new ObjectInputStream(fis);
                device = (VACVBS) in.readObject();
//                device.setCellpresure_high(waittime);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VACVBSHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VACVBSHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//TODO 调用逻辑

    private void startsendertimer(IoSession session) {
        //开始试验
        startsender = true;
        if (timer == null) {
            timer = new Timer();
        } else {
            timer.cancel();
            timer = new Timer();
        }
        starttime = System.currentTimeMillis();
        if (st == null) {
            st = new SenderTask(session);
        } else {
            st.cancel();
            st = new SenderTask(session);
        }
        timer.scheduleAtFixedRate(st, 0, 1 * 1000); //subsequent rate
    }

    class SenderTask extends TimerTask {

        IoSession session;
        VACVBS device;

        private SenderTask() {
        }

        SenderTask(IoSession _session) {
            session = _session;
            device = new VACVBS();
        }

        public void run() {
            if (startsender == false) {
                this.cancel();
            } else {
                VACVBSOutMessage pkt = new VACVBSOutMessage();
                byte[] outmsgcontent = pkt.getContent();
                outmsgcontent[0] = 1;
                int temp, humidity, highpressure;
                temp = 380;
                System.arraycopy(BytePlus.int2bytes(temp), 2, outmsgcontent, 1, 2);
                humidity = 800;
                System.arraycopy(BytePlus.int2bytes(humidity), 2, outmsgcontent, 3, 2);
                highpressure = 1010;
                System.arraycopy(BytePlus.int2bytes(highpressure), 2, outmsgcontent, 5, 2);
                System.arraycopy(BytePlus.int2bytes(produceData(System.currentTimeMillis() - starttime)), 2, outmsgcontent, 7, 2);
                pkt.setContent(outmsgcontent);
                WriteFuture wf = session.write(pkt);// 发送消息
                System.out.println("send one data packet");
                wf.awaitUninterruptibly();
            }

        }

        public int produceData(long time) {
            time = time / 500;
            double rtn = Math.abs(2e-10 * Math.pow(time, 4) - 1e-06 * Math.pow(time, 3) + 0.00278 * Math.pow(time, 2) - 0.34281 * time - 0.59106);

            return Math.round((float) rtn);
        }
    }
}
