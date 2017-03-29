package net.labthink.instrument.device.OxygenTransducer;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.labthink.instrument.device.G2131.handler.G2131Handler;
import net.labthink.instrument.device.OxygenTransducer.codec.OxygenTransducerProtocolCodecFactory;
import net.labthink.instrument.device.OxygenTransducer.handler.OxygenTransducerHandler;
import net.labthink.instrument.device.OxygenTransducer.message.OxygenTransducerOutMessage;
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

public class OxygenTransducerTester01 implements Runnable {

    RS232Connector rs232connector;
    private static final String STORE_FILE = "save.dat";

    public OxygenTransducerTester01(RS232Connector rs232connector) {
        this.rs232connector = rs232connector;
    }

    @Override
    public void run() {
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                try {
                    sendSample();
                } catch (InterruptedException ex) {
                    Logger.getLogger(OxygenTransducerTester01.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }, 0, 1000);
    }

    public void run1() {
        try {
            sendSample();



        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        while (true) {
        }

    }

    public void sendSample1() throws InterruptedException {
    }

    public void sendSample() throws InterruptedException {
        IoSession session = rs232connector.getReceiverSession();
        // SendOnePacket(session, pkt, nc, 1000);
        OxygenTransducerOutMessage pkt = new OxygenTransducerOutMessage();
        WriteFuture wf;

        byte bt[] = pkt.getContent();
        bt[0] = (byte) 0x81;
        bt[1] = (byte) 0x81;
        bt[2] = 0x52;
        bt[3] = 0x1A;
        bt[4] = 0;
        bt[5] = 0;
        bt[6] = 0x53;
        bt[7] = 0x1a;
        pkt.setContent(bt);
        wf = session.write(pkt);// 发送消息
        wf.awaitUninterruptibly();
        Thread.sleep(1000);

    }

    public static void main(String[] args) {

        SerialAddress portAddress = new SerialAddress("COM1", 9600,
                DataBits.DATABITS_8, StopBits.BITS_1, Parity.NONE,
                FlowControl.NONE);
        OxygenTransducerHandler handler = new OxygenTransducerHandler(null);
        handler.loadData();
        RS232Connector receiver = RS232Connector.getInstance(handler,
                portAddress);
        receiver.addFilter("logger", new LoggingFilter());
        receiver.addFilter("codec", new ProtocolCodecFilter(
                new OxygenTransducerProtocolCodecFactory())); // 设置编码过滤器
        // receiver.addFilter("codec", new ProtocolCodecFilter(
        // new TextLineCodecFactory(Charset.forName("UTF-8")))); // 设置编码过滤器
        receiver.startListen();
        OxygenTransducerTester01 test = new OxygenTransducerTester01(receiver);
        new Thread(test).start();

    }
}
