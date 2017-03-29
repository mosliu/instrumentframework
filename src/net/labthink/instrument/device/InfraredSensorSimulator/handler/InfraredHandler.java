package net.labthink.instrument.device.InfraredSensorSimulator.handler;

//~--- non-JDK imports --------------------------------------------------------
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.labthink.instrument.device.InfraredSensorSimulator.message.InfraredMessage;

import net.labthink.utils.BytePlus;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 * Coder's 信号处理
 *
 * @version 1.0.0.0,
 * @author Moses
 */
public class InfraredHandler extends IoHandlerAdapter {

    boolean startsender = false;
    long starttime = 0;
    SenderTask st = null;
    Timer timer = null;
    /**
     * 每次发送后等待时间。
     */
    private int waittime = 100;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss\t");
    public InfraredHandler() {
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        super.exceptionCaught(session, cause);
    }

    public static void main(String[] args) {
        
    }

    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        InfraredMessage msg = (InfraredMessage) message;

        byte[] content = msg.getContent();
        byte[] ppmvalues = new byte[2];
        System.arraycopy(content, 0, ppmvalues, 0, 2);
        byte[] ppmvalues2 = new byte[2];
        System.arraycopy(content, 2, ppmvalues, 0, 2);

        System.out.print(sdf.format(new Date()));
        System.out.print("ppm:"+BytePlus.bytes2short(ppmvalues));
        System.out.println(",refer:"+BytePlus.bytes2short(ppmvalues2));

    }

    // byte[] k = BytePlus.int2bytes(r.nextInt(0x77777777));
    //
    // // 传感器数据
    // System.arraycopy(k, 0, outmsgcontent, 6, 4);
    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("session closed");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception {
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

    // TODO 调用逻辑
    private void startsendertimer(IoSession session) {
        // 开始试验
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
        timer.scheduleAtFixedRate(st, 50, 1 * 1000); // subsequent rate
    }

//    /**
//     * @return the outs
//     */
//    public GUIPrintStream getOuts() {
//        return outs;
//    }
//
//    /**
//     * @param outs the outs to set
//     */
//    public void setOuts(GUIPrintStream outs) {
//        this.outs = outs;
//    }
    
    class SenderTask extends TimerTask {

        IoSession session;

        private SenderTask() {
        }

        SenderTask(IoSession _session) {
            session = _session;
        }

        public void run() {
            try {
                if (startsender == false) {
                    this.cancel();
                } else {
                    InfraredMessage pkt = new InfraredMessage();
                    byte[] restba = {0, 0, 0, 0};
                    byte[] currentba = {1, 0, 0, 0};
                    byte[] maxba = {1, 0, 0, 0};
                    byte[] minba = {0, 0, 0, 0};
                    pkt.setContent(currentba);
                    WriteFuture wf = session.write(pkt);// 发送消息
                    pkt.setContent(currentba);
                    wf = session.write(pkt);// 发送消息
                    Thread.sleep(50);
                    pkt.setContent(maxba);
                    wf = session.write(pkt);// 发送消息
                    Thread.sleep(50);
                    pkt.setContent(minba);
                    wf.awaitUninterruptibly();

                }
            } catch (InterruptedException ex) {
                Logger.getLogger(InfraredHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
