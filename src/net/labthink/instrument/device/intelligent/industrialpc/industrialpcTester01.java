package net.labthink.instrument.device.intelligent.industrialpc;

import net.labthink.instrument.device.intelligent.industrialpc.codec.industrialpcProtocolCodecFactory;
import net.labthink.instrument.device.intelligent.industrialpc.handler.industrialpcHandler;
import net.labthink.instrument.rs232.RS232Connector;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.serial.SerialAddress;
import org.apache.mina.transport.serial.SerialAddress.DataBits;
import org.apache.mina.transport.serial.SerialAddress.FlowControl;
import org.apache.mina.transport.serial.SerialAddress.Parity;
import org.apache.mina.transport.serial.SerialAddress.StopBits;

public class industrialpcTester01 implements Runnable {

    RS232Connector rs232connector;
    private static final String STORE_FILE = "save.dat";
    public industrialpcTester01(RS232Connector rs232connector) {
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
//        IoSession session = rs232connector.getReceiverSession();
//        // SendOnePacket(session, pkt, nc, 1000);
//        ZigbeePacket pkt = new ZigbeePacket();
//        WriteFuture wf;
//        byte bt[] = pkt.getContent();
//        pkt.setContent(bt);
//        wf = session.write(pkt);// 发送消息
//        wf.awaitUninterruptibly();
//        Thread.sleep(1000);

    }

    public static void main(String[] args) {

        SerialAddress portAddress = new SerialAddress("COM1", 9600,
                DataBits.DATABITS_8, StopBits.BITS_1, Parity.NONE,
                FlowControl.NONE);
        industrialpcHandler handler = new industrialpcHandler();
        handler.loadData();
        RS232Connector receiver = RS232Connector.getInstance(handler,
                portAddress);
        receiver.addFilter("logger", new LoggingFilter());
        receiver.addFilter("codec", new ProtocolCodecFilter(
                new industrialpcProtocolCodecFactory())); // 设置编码过滤器
        // receiver.addFilter("codec", new ProtocolCodecFilter(
        // new TextLineCodecFactory(Charset.forName("UTF-8")))); // 设置编码过滤器
        receiver.startListen();
        industrialpcTester01 test = new industrialpcTester01(receiver);
        new Thread(test).start();

    }
}
