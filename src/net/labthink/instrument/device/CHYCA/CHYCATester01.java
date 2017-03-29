package net.labthink.instrument.device.CHYCA;

import net.labthink.instrument.device.CHYCA.codec.CHYCAProtocolCodecFactory;
import net.labthink.instrument.device.CHYCA.handler.CHYCAHandler;
import net.labthink.instrument.device.CHYCA.message.CHYCAOutMessage;
import net.labthink.instrument.device.CHYCA.simulator.CHYCA;
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

public class CHYCATester01 implements Runnable {

    RS232Connector rs232connector;
    private static final String STORE_FILE = "chycasave.dat";
    public CHYCATester01(RS232Connector rs232connector) {
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
        CHYCAOutMessage pkt = new CHYCAOutMessage();
        WriteFuture wf;
        byte bt[] = pkt.getContent();
        bt[0] = 0;
        
        pkt.setContent(bt);
        wf = session.write(pkt);// 发送消息
        wf.awaitUninterruptibly();
        Thread.sleep(1000);

    }

    public static void main(String[] args) {

        SerialAddress portAddress = new SerialAddress("COM1", 9600,
                DataBits.DATABITS_8, StopBits.BITS_1, Parity.NONE,
                FlowControl.NONE);
        CHYCAHandler handler = new CHYCAHandler(new CHYCA());
        handler.loadData();
        RS232Connector receiver = RS232Connector.getInstance(handler,
                portAddress);
        receiver.addFilter("logger", new LoggingFilter());
        receiver.addFilter("codec", new ProtocolCodecFilter(
                new CHYCAProtocolCodecFactory())); // 设置编码过滤器
        // receiver.addFilter("codec", new ProtocolCodecFilter(
        // new TextLineCodecFactory(Charset.forName("UTF-8")))); // 设置编码过滤器
        receiver.startListen();
        CHYCATester01 test = new CHYCATester01(receiver);
        new Thread(test).start();

    }
}
