package net.labthink.instrument.device.FPTF1;

import net.labthink.instrument.device.FPTF1.codec.FPTF1ProtocolCodecFactory;
import net.labthink.instrument.device.FPTF1.handler.FPTF1Handler;
import net.labthink.instrument.device.FPTF1.message.FPTF1OutMessage;
import net.labthink.instrument.rs232.RS232Connector;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.serial.SerialAddress;
import org.apache.mina.transport.serial.SerialAddress.DataBits;
import org.apache.mina.transport.serial.SerialAddress.FlowControl;
import org.apache.mina.transport.serial.SerialAddress.Parity;
import org.apache.mina.transport.serial.SerialAddress.StopBits;

public class FPTF1Tester01 implements Runnable {

    RS232Connector rs232connector;
    private static final String STORE_FILE = "save.dat";
    public FPTF1Tester01(RS232Connector rs232connector) {
        this.rs232connector = rs232connector;
    }

    @Override
    public void run() {
        try {
            sendSample1();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void sendSample1() throws InterruptedException {
    }

    public void sendSample() throws InterruptedException {
        IoSession session = rs232connector.getReceiverSession();
        // SendOnePacket(session, pkt, nc, 1000);
        FPTF1OutMessage pkt = new FPTF1OutMessage();
        WriteFuture wf;
        byte bt[] = pkt.getContent();
        pkt.setContent(bt);
        wf = session.write(pkt);// 发送消息
        wf.awaitUninterruptibly();
        Thread.sleep(1000);

    }

    public static void main(String[] args) {

        SerialAddress portAddress = new SerialAddress("COM3", 9600,
                DataBits.DATABITS_8, StopBits.BITS_1, Parity.NONE,
                FlowControl.NONE);
        FPTF1Handler handler = new FPTF1Handler();
        handler.loadData();
        RS232Connector receiver = RS232Connector.getInstance(handler,
                portAddress);
        receiver.addFilter("logger", new LoggingFilter());
        receiver.addFilter("codec", new ProtocolCodecFilter(
                new FPTF1ProtocolCodecFactory())); // 设置编码过滤器
        // receiver.addFilter("codec", new ProtocolCodecFilter(
        // new TextLineCodecFactory(Charset.forName("UTF-8")))); // 设置编码过滤器
        receiver.startListen();
        FPTF1Tester01 test = new FPTF1Tester01(receiver);
        new Thread(test).start();

    }
}
