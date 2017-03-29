package net.labthink.instrument.device.MXD02;

import net.labthink.instrument.device.MXD02.codec.MXD02ProtocolCodecFactory;
import net.labthink.instrument.device.MXD02.handler.MXD02Handler;
import net.labthink.instrument.device.MXD02.message.MXD02OutMessage;
import net.labthink.instrument.device.MXD02.simulator.MXD02;
import net.labthink.instrument.rs232.RS232Connector;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.serial.SerialAddress;
import org.apache.mina.transport.serial.SerialAddress.DataBits;
import org.apache.mina.transport.serial.SerialAddress.FlowControl;
import org.apache.mina.transport.serial.SerialAddress.Parity;
import org.apache.mina.transport.serial.SerialAddress.StopBits;

public class MXD02Tester01 implements Runnable {

    RS232Connector rs232connector;
    private static final String STORE_FILE = "save.dat";
    private MXD02 device = new MXD02();
    private boolean runflag = true;
    int sleeptime = 50;

    public MXD02Tester01(RS232Connector rs232connector) {
        this.rs232connector = rs232connector;
    }

    @Override
    public void run() {
        try {
            if (device.getRange() < 0) {
                sleeptime = -device.getRange();
            }
            sendSample1();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void sendSample1() throws InterruptedException {
        if (runflag == false) {
            return;
        }
        IoSession session = rs232connector.getReceiverSession();
        // SendOnePacket(session, pkt, nc, 1000);
        MXD02OutMessage pkt = new MXD02OutMessage();
        WriteFuture wf;
        int total = device.getTesttimes();
        pkt.setContent(device.getPrePKT());
        wf = session.write(pkt);// 发送消息
        wf.awaitUninterruptibly();
        for (int i = 0; i < total; i++) {
            System.out.println("第" + i + "条曲线。");
            //试验数据
            byte[][] bytesss = device.getDetailsPKTs(i);
            for (int j = 0; runflag && j < bytesss.length; j++) {
                pkt.setContent(bytesss[j]);
                wf = session.write(pkt);// 发送消息
                wf.awaitUninterruptibly();
                Thread.sleep(sleeptime);
            }
            //结果数据
//            //出错
//            if(i%10==0){
//                byte[] tempa = device.getResultPKT();
//                tempa[0] = 6;
//                pkt.setContent(tempa);
//                wf = session.write(pkt);// 发送消息
//                wf.awaitUninterruptibly();
//            }
            pkt.setContent(device.getResultPKT());
            wf = session.write(pkt);// 发送消息
            wf.awaitUninterruptibly();
            Thread.sleep(sleeptime);
            wf = session.write(pkt);// 发送消息
            wf.awaitUninterruptibly();

//            for (int i = 0; runflag && (i < device.getTesttimes()); i++) {
//                wf = session.write(pkt);// 发送消息
//                wf.awaitUninterruptibly();
//                Thread.sleep(500);
//            }
        }
        pkt.setContent(device.getStatPKT());
        wf = session.write(pkt);// 发送消息
        wf.awaitUninterruptibly();
        Thread.sleep(sleeptime);
        pkt.setContent(device.getSDPKT());
        wf = session.write(pkt);// 发送消息
        wf.awaitUninterruptibly();
        Thread.sleep(sleeptime);

                    //结束试验
                byte[] tempa = device.getResultPKT();
                tempa[0] = 4;
                pkt.setContent(tempa);
                wf = session.write(pkt);// 发送消息
                wf.awaitUninterruptibly();


    }

    public void sendSample() throws InterruptedException {
        IoSession session = rs232connector.getReceiverSession();
        // SendOnePacket(session, pkt, nc, 1000);
        MXD02OutMessage pkt = new MXD02OutMessage();
        WriteFuture wf;
        byte bt[] = pkt.getContent();
        pkt.setContent(bt);
        wf = session.write(pkt);// 发送消息
        wf.awaitUninterruptibly();
        Thread.sleep(1000);

    }

    public static void main(String[] args) {

        SerialAddress portAddress = new SerialAddress("COM1", 9600,
                DataBits.DATABITS_8, StopBits.BITS_1, Parity.NONE,
                FlowControl.NONE);
        MXD02Handler handler = new MXD02Handler();
        handler.loadData();
        RS232Connector receiver = RS232Connector.getInstance(handler,
                portAddress);
        receiver.addFilter("logger", new LoggingFilter());
        receiver.addFilter("codec", new ProtocolCodecFilter(
                new MXD02ProtocolCodecFactory())); // 设置编码过滤器
        // receiver.addFilter("codec", new ProtocolCodecFilter(
        // new TextLineCodecFactory(Charset.forName("UTF-8")))); // 设置编码过滤器
        receiver.startListen();
        MXD02Tester01 test = new MXD02Tester01(receiver);
        new Thread(test).start();

    }

    /**
     * @return the device
     */
    public MXD02 getDevice() {
        return device;
    }

    /**
     * @param device the device to set
     */
    public void setDevice(MXD02 device) {
        this.device = device;
    }

    /**
     * @return the runflag
     */
    public boolean isRunflag() {
        return runflag;
    }

    /**
     * @param runflag the runflag to set
     */
    public void setRunflag(boolean runflag) {
        this.runflag = runflag;
    }
}
