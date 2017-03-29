package net.labthink.instrument.device.Coder.handler;

//~--- non-JDK imports --------------------------------------------------------
import java.lang.reflect.Array;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.labthink.instrument.device.Coder.message.CoderMessage;

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
public class CoderHandler extends IoHandlerAdapter {

    public Values vals = new Values();
    boolean startsender = false;
    long starttime = 0;
    SenderTask st = null;
    Timer timer = null;
    /**
     * 每次发送后等待时间。
     */
    private int waittime = 100;

    public CoderHandler() {
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
        CoderMessage msg = (CoderMessage) message;

        byte[] content = msg.getContent();
        byte[] values = new byte[3];
        System.arraycopy(content, 1, values, 0, 3);

        switch (content[0]) {
            case 1:
                vals.setCurrentdeg(values);
                break;
            case 2:
                vals.setMaxdeg(values);
                break;
            case 3:
                vals.setMindeg(values);
                break;
            default:
                return;
        }

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

    public class Values {
        public boolean start = false;
        private int currentdeg;
        private int maxdeg;
        private int mindeg;

        /**
         * @return the currentdeg
         */
        public int getCurrentdeg() {
            return currentdeg;
        }

        /**
         * @param currentdeg the currentdeg to set
         */
        public void setCurrentdeg(int currentdeg) {
            this.currentdeg = currentdeg;
            start = true;
        }

        /**
         * @param currentdeg the currentdeg to set
         */
        public void setCurrentdeg(byte[] currentdeg) {
            this.currentdeg = BytePlus.bytes2int(currentdeg);
            start = true;
        }

        /**
         * @return the maxdeg
         */
        public int getMaxdeg() {
            return maxdeg;
        }

        /**
         * @param maxdeg the maxdeg to set
         */
        public void setMaxdeg(int maxdeg) {
            this.maxdeg = maxdeg;
            start = true;
        }

        /**
         * @param maxdeg the maxdeg to set
         */
        public void setMaxdeg(byte[] maxdeg) {
            this.maxdeg = BytePlus.bytes2int(maxdeg);
            start = true;
        }

        /**
         * @return the mindeg
         */
        public int getMindeg() {
            return mindeg;
        }

        /**
         * @param mindeg the mindeg to set
         */
        public void setMindeg(int mindeg) {
            this.mindeg = mindeg;
            start = true;
        }

        /**
         * @param mindeg the mindeg to set
         */
        public void setMindeg(byte[] mindeg) {
            this.mindeg = BytePlus.bytes2int(mindeg);
            start = true;
        }
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
                    CoderMessage pkt = new CoderMessage();
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
                Logger.getLogger(CoderHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
