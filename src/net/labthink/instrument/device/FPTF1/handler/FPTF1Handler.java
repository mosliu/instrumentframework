package net.labthink.instrument.device.FPTF1.handler;

//~--- non-JDK imports --------------------------------------------------------
import net.labthink.instrument.device.FPTF1.message.FPTF1InMessage;
import net.labthink.instrument.device.FPTF1.message.FPTF1OutMessage;
import net.labthink.instrument.device.FPTF1.simulator.FPTF1;
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
import java.util.Timer;
import java.util.TimerTask;

/**
 * FPTF1's 信号处理
 *
 * @version 1.0.0.0, 2010/08/24
 * @author Moses
 */
public class FPTF1Handler extends IoHandlerAdapter {
    //TODO 尚未完成。

    public boolean allerror = false;
    int errorcount = 0;
    private static final String STORE_FILE = "FPTF1save.dat";
    boolean startsender = false;
    long starttime = 0;
    SenderTask st = null;
    Timer timer = null;
    /**
     * 每次发送后等待时间。
     */
    private int waittime = 100;

    /*
     * 仪器
     */
    private FPTF1 device = new FPTF1();

    /**
     * 构造函数
     *
     */
    public FPTF1Handler() {
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
        FPTF1InMessage msg = (FPTF1InMessage) message;
        byte[] incontent = msg.getContent();
        FPTF1OutMessage outmsg = new FPTF1OutMessage();
        byte[] outmsgcontent = outmsg.getContent();
        byte tempbyte;
        byte tempbyte2;
        byte[] tempbytes;


        switch (incontent[1]) {
            case 0x0:
                /*
                 * 启动试验
                 */
                System.out.println("启动试验");
                //04  确认帧（用于对计算机命令的应答）
                outmsgcontent[0] = 0x04;
                // (00  剥离试验启动    01  摩擦试验启动) 
                System.arraycopy(incontent, 0, outmsgcontent, 0, 1);
                getDevice().setDevicepower(true);
                getDevice().setTesting(true);
                //从第1位清空返回包，防止错误数据
                BytePlus.fillcontent(outmsgcontent, 1);
                break;
            case 0x1:
                /*
                 * 停止试验
                 */
                System.out.println("停止试验");
                //X1:02 02  停止试验
                outmsgcontent[0] = 0x02;
                //04  确认帧（用于对计算机命令的应答）
                outmsgcontent[0] = 0x04;
                //从第1位清空返回包，防止错误数据
                BytePlus.fillcontent(outmsgcontent, 1);
                break;
            case 0x2:
                /*
                 * 设定温度（最大值：100.0） D1,D2分别为设定值的高位和低位
                 */
                System.out.println(" 设定温度（最大值：100.0） D1,D2分别为设定值的高位和低位");
                //04  确认帧（用于对计算机命令的应答）
                outmsgcontent[0] = 0x04;
                //设定 温度设定值
                tempbytes = new byte[2];
                System.arraycopy(incontent, 2, tempbytes, 0, 2);
                getDevice().setTemperature(tempbytes);
                //从第1位清空返回包，防止错误数据
                BytePlus.fillcontent(outmsgcontent, 1);
                break;
            case 0x3:
                /*
                 * 设定PID参数 D1,D2 -- P项参数，D3,D4--I项参数,D5,D6--D项参数 高位在前
                 */
                System.out.println(" 设定PID参数 D1,D2 -- P项参数，D3,D4--I项参数,D5,D6--D项参数 高位在前");
                //04  确认帧（用于对计算机命令的应答）
                outmsgcontent[0] = 0x04;
                //设定 PIDP参数
                tempbytes = new byte[2];
                System.arraycopy(incontent, 2, tempbytes, 0, 2);
                getDevice().setPidP(tempbytes);
                //设定 PIDI参数
                tempbytes = new byte[2];
                System.arraycopy(incontent, 4, tempbytes, 0, 2);
                getDevice().setPidI(tempbytes);
                //设定 PIDD参数
                tempbytes = new byte[2];
                System.arraycopy(incontent, 6, tempbytes, 0, 2);
                getDevice().setPidD(tempbytes);
                //从第1位清空返回包，防止错误数据
                BytePlus.fillcontent(outmsgcontent, 1);
                break;
            case 0x4:
                /*
                 * 加热状态设定 D1为00不加热 D1为11 加热；
                 */
                System.out.println(" 加热状态设定 D1为00不加热 D1为11 加热；");
                //04  确认帧（用于对计算机命令的应答）
                outmsgcontent[0] = 0x04;
                //设定 是否加热
                getDevice().setHeatflag(incontent[2]);
                //从第1位清空返回包，防止错误数据
                BytePlus.fillcontent(outmsgcontent, 1);
                break;
            case 0x5:
                /*
                 * 速度设定 D1=01 SPD=25mm/min D1=02 SPD=50mm/min D1=03
                 * SPD=100mm/min D1=04 SPD=150mm/min D1=05 SPD=200mm/min D1=06
                 * SPD=250mm/min D1=07 SPD=300mm/min D1=08  SPD=500mm/min
                 */
                System.out.println(" 速度设定 D1=01  SPD=25mm/min D1=02  SPD=50mm/min D1=03  SPD=100mm/min D1=04  SPD=150mm/min D1=05  SPD=200mm/min D1=06  SPD=250mm/min D1=07  SPD=300mm/min D1=08  SPD=500mm/min");
                //04  确认帧（用于对计算机命令的应答）
                outmsgcontent[0] = 0x04;
                //设定 速度设定 D1=01  SPD=25mm/min D1=02  SPD=50mm/min D1=03  SPD=100mm/min D1=04  SPD=150mm/min D1=05  SPD=200mm/min D1=06  SPD=250mm/min D1=07  SPD=300mm/min D1=08  SPD=500mm/min
                getDevice().setSpeed(incontent[2]);
                //从第1位清空返回包，防止错误数据
                BytePlus.fillcontent(outmsgcontent, 1);
                break;
            case 0x6:
                /*
                 * 系统标定
                 */
                System.out.println(" 系统标定");
                //具体解析该命令
                switch (incontent[2]) {
                    case 0x0:
                        /*
                         * 温度零点 11H    (X3X4高位在前)
                         */
                        System.out.println("温度零点 11H    (X3X4高位在前)");
                        //04  确认帧（用于对计算机命令的应答）
                        outmsgcontent[0] = 0x04;
                        //设定 温度零点
                        tempbytes = new byte[2];
                        System.arraycopy(incontent, 2, tempbytes, 0, 2);
                        getDevice().setTempzero(tempbytes);
                        //从第5位清空返回包，防止错误数据
                        BytePlus.fillcontent(outmsgcontent, 5);
                        break;
                    case 0x1:
                        /*
                         * 温度终点 12H    (X3X4高位在前)
                         */
                        System.out.println("温度终点 12H    (X3X4高位在前)");
                        //04  确认帧（用于对计算机命令的应答）
                        outmsgcontent[0] = 0x04;
                        //设定 温度终点
                        tempbytes = new byte[2];
                        System.arraycopy(incontent, 2, tempbytes, 0, 2);
                        getDevice().setTempend(tempbytes);
                        //从第5位清空返回包，防止错误数据
                        BytePlus.fillcontent(outmsgcontent, 5);
                        break;
                    case 0x2:
                        /*
                         * 低压零点 17H    (X3X4高位在前)
                         */
                        System.out.println("低压零点         17H    (X3X4高位在前)");
                        //04  确认帧（用于对计算机命令的应答）
                        outmsgcontent[0] = 0x04;
                        //设定 压力零点
                        tempbytes = new byte[2];
                        System.arraycopy(incontent, 2, tempbytes, 0, 2);
                        getDevice().setPressurezero(tempbytes);
                        //从第5位清空返回包，防止错误数据
                        BytePlus.fillcontent(outmsgcontent, 5);
                        break;
                    case 0x3:
                        /*
                         * 低压终点 18H    (X3X4高位在前)
                         */
                        System.out.println("低压终点         18H    (X3X4高位在前)");
                        //04  确认帧（用于对计算机命令的应答）
                        outmsgcontent[0] = 0x04;
                        //设定 压力终点
                        tempbytes = new byte[2];
                        System.arraycopy(incontent, 2, tempbytes, 0, 2);
                        getDevice().setPressureend(tempbytes);
                        //从第5位清空返回包，防止错误数据
                        BytePlus.fillcontent(outmsgcontent, 5);
                        break;
                    default:
                        System.out.println("错误，不能解析的指令");
                }
                break;
            case 0x7:
                /*
                 * 07 -- 确认帧
                 */
                System.out.println("07 -- 确认帧");
                //04  确认帧（用于对计算机命令的应答）
                outmsgcontent[0] = 0x04;
                //设定 速度设定 D1=01  SPD=25mm/min D1=02  SPD=50mm/min D1=03  SPD=100mm/min D1=04  SPD=150mm/min D1=05  SPD=200mm/min D1=06  SPD=250mm/min D1=07  SPD=300mm/min D1=08  SPD=500mm/min
                getDevice().setSpeed(incontent[2]);
                //从第1位清空返回包，防止错误数据
                BytePlus.fillcontent(outmsgcontent, 1);
                break;
            case 0x8:
                /*
                 * 08 --时间校正
                 */
                System.out.println("08 --时间校正");
                //04  确认帧（用于对计算机命令的应答）
                outmsgcontent[0] = 0x04;
                //设定 年
                getDevice().setYear(incontent[2]);
                //设定 月
                getDevice().setMonth(incontent[3]);
                //设定 日
                getDevice().setDay(incontent[4]);
                //设定 时
                getDevice().setHour(incontent[5]);
                //设定 分
                getDevice().setMinute(incontent[6]);
                //设定 秒
                getDevice().setSecond(incontent[7]);
                //从第1位清空返回包，防止错误数据
                BytePlus.fillcontent(outmsgcontent, 1);
                break;
            case 0x9:
                /*
                 * 09 -- 归位 D1 - 数据（单位：秒）
                 */
                System.out.println("09 -- 归位 D1 - 数据（单位：秒）");
                //从第1位清空返回包，防止错误数据
                BytePlus.fillcontent(outmsgcontent, 1);
                break;
            case 0x0A:
                /*
                 * 标定查看
                 */
                System.out.println("标定查看  ");
                //05  标定查看(FH FL为显示力值)
                outmsgcontent[0] = 0x05;
                //TODO(FH FL为显示力值),FH
                outmsgcontent[1] = 0x00;
                //(FH FL为显示力值),FL
                outmsgcontent[2] = 0x64;
                //设定 速度设定 D1=01  SPD=25mm/min D1=02  SPD=50mm/min D1=03  SPD=100mm/min D1=04  SPD=150mm/min D1=05  SPD=200mm/min D1=06  SPD=250mm/min D1=07  SPD=300mm/min D1=08  SPD=500mm/min
                getDevice().setSpeed(incontent[2]);
                //从第1位清空返回包，防止错误数据
                BytePlus.fillcontent(outmsgcontent, 1);
                break;
            case 0x0B:
                /*
                 * 0B -- 系统复位
                 */
                System.out.println(" 0B -- 系统复位");
                //08  复位
                outmsgcontent[0] = 0x08;
                //从第1位清空返回包，防止错误数据
                BytePlus.fillcontent(outmsgcontent, 1);
                break;
            case 0x0C:
                /*
                 * 0C -- 工进
                 */
                System.out.println("0C -- 工进");
                //04  确认帧（用于对计算机命令的应答）
                outmsgcontent[0] = 0x04;
                //从第1位清空返回包，防止错误数据
                BytePlus.fillcontent(outmsgcontent, 1);
                break;
            case 0x0D:
                /*
                 * 0D -- 试验完成归位
                 */
                System.out.println("0D -- 试验完成归位");
                //04  确认帧（用于对计算机命令的应答）
                outmsgcontent[0] = 0x04;
                //从第1位清空返回包，防止错误数据
                BytePlus.fillcontent(outmsgcontent, 1);
                break;
            case 0x0E:
                /*
                 * 0E -- 后退
                 */
                System.out.println("0E -- 后退");
                //04  确认帧（用于对计算机命令的应答）
                outmsgcontent[0] = 0x04;
                //从第1位清空返回包，防止错误数据
                BytePlus.fillcontent(outmsgcontent, 1);
                break;
            case 0x0F:
                /*
                 * 0F -- 停止移动
                 */
                System.out.println("0F -- 停止移动");
                //04  确认帧（用于对计算机命令的应答）
                outmsgcontent[0] = 0x04;
                //从第1位清空返回包，防止错误数据
                BytePlus.fillcontent(outmsgcontent, 1);
                break;
            case 0x10:
                /*
                 * 10 -- 传感器量程更换D1=01 5N D1=02 10N D1=03 30N
                 */
                System.out.println("10 -- 传感器量程更换D1=01 5N   D1=02 10N  D1=03 30N ");
                //04  确认帧（用于对计算机命令的应答）
                outmsgcontent[0] = 0x04;
                // D1=01 5N   D1=02 10N  D1=03 30N 
                System.arraycopy(incontent, 2, outmsgcontent, 1, 1);
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
    public FPTF1 getDevice() {
        return device;
    }

    /**
     * @param device the device to set
     */
    public void setDevice(FPTF1 device) {
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
            Logger.getLogger(FPTF1Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadData() {
        try {
            File f = new File(STORE_FILE);

            if (f.exists()) {
                FileInputStream fis = new FileInputStream(f);
                ObjectInputStream in = new ObjectInputStream(fis);
                device = (FPTF1) in.readObject();
//                device.setCellpresure_high(waittime);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FPTF1Handler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FPTF1Handler.class.getName()).log(Level.SEVERE, null, ex);
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

        private SenderTask() {
        }

        SenderTask(IoSession _session) {
            session = _session;
        }

        public void run() {
            if (startsender == false) {
                this.cancel();
            } else {
                FPTF1OutMessage pkt = new FPTF1OutMessage();
                byte[] outmsgcontent = pkt.getContent();
                //TODO 进行数据包组装
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
            //TODO data生成逻辑
            double rtn = Math.abs(2e-10 * Math.pow(time, 4) - 1e-06 * Math.pow(time, 3) + 0.00278 * Math.pow(time, 2) - 0.34281 * time - 0.59106);

            return Math.round((float) rtn);
        }
    }
//    public static void main(String[] args) {
//        byte a = (byte) 0x80;
//        int b = a < 0 ? a + 256 : a;
//        System.out.println(b);
//    }
}
