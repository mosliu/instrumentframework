package net.labthink.instrument.device.OxygenTransducer.handler;

//~--- non-JDK imports --------------------------------------------------------
import net.labthink.instrument.device.OxygenTransducer.message.OxygenTransducerOxyInMessage;
import net.labthink.instrument.device.OxygenTransducer.message.OxygenTransducerOxyOutMessage;
import net.labthink.instrument.device.OxygenTransducer.simulator.OxygenTransducer;
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
import javax.swing.JLabel;
import net.labthink.instrument.device.OxygenTransducer.message.DataHolder;
import org.jfree.data.time.Second;

/**
 * OxygenTransducer's 信号处理
 *
 * @version        1.0.0.0, 2011/07/29
 * @author         Moses
 */
public class OxygenTransducerOxyHandler extends IoHandlerAdapter {

    public boolean allerror = false;
    int errorcount = 0;
    private static final String STORE_FILE = "OxygenTransducersave.dat";
    DataHolder dh;
    boolean startsender = false;
    long starttime = 0;
    SenderTask st = null;
    Timer timer = null;
    /** 每次发送后等待时间。 */
    private int waittime = 100;

    /* 仪器 */
    private OxygenTransducer device = new OxygenTransducer();

    /**
     * 构造函数
     *
     */
    public OxygenTransducerOxyHandler(DataHolder _dh) {
        this.dh = _dh;
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
        super.exceptionCaught(session, cause);
    }

    public static void main(String[] args) {
        int[][] a = new int[1][3];
        System.out.println(a.length);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        OxygenTransducerOxyInMessage msg = (OxygenTransducerOxyInMessage) message;
        byte[] incontent = msg.getContent();
        OxygenTransducerOxyOutMessage outmsg = new OxygenTransducerOxyOutMessage();
        byte[] outmsgcontent = outmsg.getContent();
//        byte[] tempbytes;
//        tempbytes = new byte[2];
//        //取第一个值，高位在前
//        tempbytes[0] = incontent[1];
//        tempbytes[1] = incontent[0];
//        tempbytes[1] = incontent[0];
//        System.out.println(BytePlus.byteArray2String(tempbytes));
        int temp = BytePlus.littleEndianbBytesToInt(incontent);
//        System.out.println(temp);
        double tempvalue = ((double) temp) / 1000;


        dh.timeSeries2.addOrUpdate(new Second(), tempvalue);
        ((JLabel) dh.show2).setText("" + tempvalue);


//
//        byte tempbyte;
//        byte tempbyte2;
//
//
//	switch(incontent[0]) {
//		default:
//            System.out.println("错误，不能解析的指令");
//	}
//
//		outmsg.setContent(outmsgcontent);
//
//
//		//byte[]  errorpkt1 = {(byte)0x01,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x0B,(byte)0x96,(byte)0x05,(byte)0xA8,(byte)0x06,(byte)0x59,(byte)0xAE};
//		byte[]  errorpkt = {(byte)0x01,(byte)0x00};
//		if(allerror){
//            System.out.println("输出错误帧");
//             outmsg.setContent(errorpkt);
//        }
//		//errorcount ++;
////        if(false&&errorcount%50==0){
////            System.out.println("输出错误的一帧");
////            outmsg.setContent(errorpkt1);
////            WriteFuture wf = session.write(outmsg);
////            wf.awaitUninterruptibly();
////        }
//
//
//
//
//        WriteFuture wf = session.write(outmsg);
//        wf.awaitUninterruptibly();

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
    public OxygenTransducer getDevice() {
        return device;
    }

    /**
     * @param device the device to set
     */
    public void setDevice(OxygenTransducer device) {
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
            Logger.getLogger(OxygenTransducerOxyHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadData() {
        try {
            File f = new File(STORE_FILE);

            if (f.exists()) {
                FileInputStream fis = new FileInputStream(f);
                ObjectInputStream in = new ObjectInputStream(fis);
                device = (OxygenTransducer) in.readObject();
//                device.setCellpresure_high(waittime);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OxygenTransducerOxyHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(OxygenTransducerOxyHandler.class.getName()).log(Level.SEVERE, null, ex);
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
                OxygenTransducerOxyOutMessage pkt = new OxygenTransducerOxyOutMessage();
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
}
