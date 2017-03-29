package net.labthink.instrument.device.LLTEST.handler;

//~--- non-JDK imports --------------------------------------------------------
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.labthink.instrument.device.LLTEST.message.LLTESTMessage;
import net.labthink.instrument.device.LLTEST.simulator.LLTEST;
import net.labthink.instrument.device.intelligent.industrialpc.zigbee.ALCpacket;
import net.labthink.utils.BytePlus;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 * LLTEST's 信号处理
 *
 * @version 1.0.0.0, 2010/08/24
 * @author Moses
 */
public class LLTESTHandler2 extends IoHandlerAdapter {

    public void writeLog(byte[] allmsg, String commment) throws IOException {
        if (fw != null) {
            fw.append(BytePlus.byteArray2String(allmsg));
            fw.append("," + commment);
            fw.append("\r\n");
        }
    }

    public class Node {

        int hashcode;
        byte packet[];
        int receivecount;
        int rejectcount;
        int randnum;

//        private Node() {
//        }
        /*
         * 输入包体内容，形成node
         */
        public Node(byte[] content) {
            if (content == null) {
                hashcode = 0;
            } else {
                hashcode = Arrays.hashCode(content);
                packet = content;
            }
            receivecount = 0;
            rejectcount = 0;
            randnum = rand.nextInt(30) + 1;
        }

        public void tick() {
            receivecount++;
        }

        public void tickReject() {
            receivecount++;
            rejectcount++;
        }
    }
    Random rand = new Random();
    HashMap<Integer, Node> receiveHM = new HashMap<Integer, Node>();
//    private GUIPrintStream outs = new GUIPrintStream(System.out);
    public boolean allerror = false;
    int errorcount = 0;
    private static final String STORE_FILE = "LLTESTsave.dat";
    boolean startsender = false;
    long starttime = 0;
    SenderTask st = null;
    Timer timer = null;
    BitSet bst = new BitSet(10000);
    static int countpackets = 0;
    static int maxpackets = 0;
//    ArrayList<ALCpacket> alcpal = new ArrayList<ALCpacket>();
    HashMap<ALCpacket, Integer> alcpcount = new HashMap<ALCpacket, Integer>();
    public File outfile;
    private FileWriter fw;
    
    int count = 0;
    /**
     * 每次发送后等待时间。
     */
    private int waittime = 100;

    /*
     * 仪器
     */
    private LLTEST device = new LLTEST();

    public LLTESTHandler2() {
        countpackets = 0;
        maxpackets = 0;
        bst = new BitSet(16384);
    }

    /**
     * 构造函数
     *
     */
    public LLTESTHandler2(LLTEST _device) {
        device = _device;
    }

    public void initfilelog() {
        try {
            fw = new FileWriter(outfile, true);

        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void teminatefilelog() {
        if (fw != null) {
            try {
//                FileWriter fw = new FileWriter(outfile, true);
                fw.flush();
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        super.exceptionCaught(session, cause);
    }

    public static void main(String[] args) {
        byte[] a = {1, 5, 3};
        byte[] b = new byte[3];
        b[0] = 1;
        b[1] = 5;
        b[2] = 3;
        System.out.println(new String(a).hashCode());
        System.out.println(new String(b).hashCode());
//        System.out.println(b.hashCode());

    }

    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        count ++;
        if(count%10==0){
            System.out.println("当前已接受"+count+"packets");
        }
        LLTESTMessage msg = (LLTESTMessage) message;
        byte[] allmsg = msg.getAll();
        byte[] contentmsg = msg.getContent();
        if (contentmsg != null) {
            int temphashcode = Arrays.hashCode(contentmsg);
            Node tempnode;
            if (receiveHM.containsKey(temphashcode)) {
                //拒绝 或者接受。
                tempnode = receiveHM.get(temphashcode);

            } else {
                tempnode = new Node(contentmsg);
                receiveHM.put(temphashcode, tempnode);
                writeLog(contentmsg, "第一次出现");
                System.out.println("新包,让它重发上"+tempnode.randnum+"遍");
            }

            if (tempnode.rejectcount < tempnode.randnum) {//回送拒绝
                tempnode.tickReject();
                writeLog(allmsg, "拒绝第" + (tempnode.rejectcount) + "次");

                device.setAcknum(msg.getHeader());
                device.setDestaddr(msg.getAddress());
                device.setResend(true);
                device.setSendfail(true);
//                device.setDesterror(true);
                LLTESTMessage outmsg = device.getPacket();

                WriteFuture wf = session.write(outmsg);
                wf.awaitUninterruptibly();
            } else {
                tempnode.tick();
                writeLog(allmsg, "接受！！");
                device.setAcknum(msg.getHeader());
                device.setDestaddr(msg.getAddress());
                device.setResend(false);
                LLTESTMessage outmsg = device.getPacket();
                WriteFuture wf = session.write(outmsg);
                wf.awaitUninterruptibly();
            }

        } else {
            return;
        }







        // byte[] errorpkt1 =
        // {(byte)0x01,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x0B,(byte)0x96,(byte)0x05,(byte)0xA8,(byte)0x06,(byte)0x59,(byte)0xAE};
//        byte[] errorpkt = {(byte) 0x01, (byte) 0x00};
//        if (allerror) {
//            System.out.println("输出错误帧");
//            outmsg.setContent(errorpkt);
//        }
        // errorcount ++;
        // if(false&&errorcount%50==0){
        // System.out.println("输出错误的一帧");
        // outmsg.setContent(errorpkt1);
        // WriteFuture wf = session.write(outmsg);
        // wf.awaitUninterruptibly();
        // }
//
//        WriteFuture wf = session.write(outmsg);
//        wf.awaitUninterruptibly();

        // wf = session.write(outmsg);
        // wf.awaitUninterruptibly();
        // this.wait(getWaittime());
    }

    // byte[] k = BytePlus.int2bytes(r.nextInt(0x77777777));
    //
    // // 传感器数据
    // System.arraycopy(k, 0, outmsgcontent, 6, 4);
    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("session closed");
        Iterator iter = receiveHM.entrySet().iterator();
        System.out.println("共收到" + receiveHM.size() + "种包");
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Integer key = (Integer) entry.getKey();
            Node val = (Node) entry.getValue();
            System.out.println("************************");
            System.out.println("包体数据:");
            System.out.println(BytePlus.byteArray2String(val.packet));
            System.out.println("解析：");
            ALCpacket pkt = new ALCpacket(val.packet);
            System.out.println(pkt.toReadbleString());
            System.out.println("结果：接收了"
                    + val.receivecount + "包；拒绝了"
                    + val.rejectcount + "包；应拒绝" + val.randnum + "包。");
            System.out.println("========================");
            

            writeLog(val.packet, "结果：接收了" + val.receivecount + "包；拒绝了"
                    + val.rejectcount + "包；应拒绝" + val.randnum + "包。\r\n===================");
        }

//        int k = bst.nextClearBit(0);
////         System.out.println("第一个丢失的包是：第"+k+"号包");
//        System.out.println("共收到：" + countpackets + "包");
//        System.out.println("收到最大包号：" + maxpackets);
//        for (int i = bst.nextClearBit(0); i < maxpackets; i = bst.nextClearBit(i + 1)) {
//            System.out.println("丢失包：" + i);
//            //do something
//        }
//        saveData();
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception {
        System.out.println("session idled:" + status.toString());
//        int k = bst.nextClearBit(0);
//        for(int i = myset.nextClearBit(0);i <number_of_bits;i = myset.nextClearBit(i+1))
//        {
//            //do something
//        }
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
    public LLTEST getDevice() {
        return device;
    }

    /**
     * @param device the device to set
     */
    public void setDevice(LLTEST device) {
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
            Logger.getLogger(LLTESTHandler2.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    public void loadData() {
        try {
            File f = new File(STORE_FILE);

            if (f.exists()) {
                FileInputStream fis = new FileInputStream(f);
                ObjectInputStream in = new ObjectInputStream(fis);
                device = (LLTEST) in.readObject();
                // device.setCellpresure_high(waittime);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LLTESTHandler2.class.getName()).log(Level.SEVERE,
                    null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LLTESTHandler2.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
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
        timer.scheduleAtFixedRate(st, 0, 1 * 1000); // subsequent rate
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
            if (startsender == false) {
                this.cancel();
            } else {
                LLTESTMessage pkt = new LLTESTMessage();
                byte[] outmsgcontent = pkt.getContent();
                // TODO 进行数据包组装
                outmsgcontent[0] = 1;
                int temp, humidity, highpressure;
                temp = 380;
                System.arraycopy(BytePlus.int2bytes(temp), 2, outmsgcontent, 1,
                        2);
                humidity = 800;
                System.arraycopy(BytePlus.int2bytes(humidity), 2,
                        outmsgcontent, 3, 2);
                highpressure = 1010;
                System.arraycopy(BytePlus.int2bytes(highpressure), 2,
                        outmsgcontent, 5, 2);
                System.arraycopy(BytePlus.int2bytes(produceData(System.currentTimeMillis()
                        - starttime)), 2, outmsgcontent, 7, 2);
                pkt.setContent(outmsgcontent);
                WriteFuture wf = session.write(pkt);// 发送消息
                System.out.println("send one data packet");
                wf.awaitUninterruptibly();
            }

        }

        public int produceData(long time) {
            time = time / 500;
            // TODO data生成逻辑
            double rtn = Math.abs(2e-10 * Math.pow(time, 4) - 1e-06
                    * Math.pow(time, 3) + 0.00278 * Math.pow(time, 2) - 0.34281
                    * time - 0.59106);

            return Math.round((float) rtn);
        }
    }
    // public static void main(String[] args) {
    // byte a = (byte) 0x80;
    // int b = a < 0 ? a + 256 : a;
    // System.out.println(b);
    // }
}
