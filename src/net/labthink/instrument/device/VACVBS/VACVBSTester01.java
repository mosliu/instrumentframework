package net.labthink.instrument.device.VACVBS;

import java.util.Random;
import net.labthink.instrument.device.VACVBS.codec.VACVBSProtocolCodecFactory;
import net.labthink.instrument.device.VACVBS.handler.VACVBSHandler;
import net.labthink.instrument.device.VACVBS.message.VACVBSOutMessage;
import net.labthink.instrument.rs232.RS232Connector;
import net.labthink.utils.BytePlus;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.serial.SerialAddress;
import org.apache.mina.transport.serial.SerialAddress.DataBits;
import org.apache.mina.transport.serial.SerialAddress.FlowControl;
import org.apache.mina.transport.serial.SerialAddress.Parity;
import org.apache.mina.transport.serial.SerialAddress.StopBits;

public class VACVBSTester01 implements Runnable {

    RS232Connector rs232connector;
    private static final String STORE_FILE = "save1.dat";
    public VACVBSTester01(RS232Connector rs232connector) {
        this.rs232connector = rs232connector;
    }

    @Override
    public void run() {
        try {
            sendSample();
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
        VACVBSOutMessage pkt = new VACVBSOutMessage();
        WriteFuture wf;
        Random r = new Random();
        byte bt[] = pkt.getContent();
        bt[0] = 1;
        int temp,humidity,highpressure;
        long starttime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            temp = 380;
            System.arraycopy(BytePlus.int2bytes(temp), 2, bt, 1, 2);
            humidity = 800;
            System.arraycopy(BytePlus.int2bytes(humidity), 2, bt, 3, 2);
            highpressure = 1010;
            System.arraycopy(BytePlus.int2bytes(highpressure), 2, bt, 5, 2);
            System.arraycopy(BytePlus.int2bytes(produceLowPressure(System.currentTimeMillis()-starttime)), 2, bt, 7, 2);
            pkt.setContent(bt);
            wf = session.write(pkt);// 发送消息
            System.out.println("send one packet");
            wf.awaitUninterruptibly();
            Thread.sleep(300);
        }
        
        

        
        
    }

    public static int produceLowPressure(long time){
        time = time /100;
        double rtn = Math.abs(2e-10 * Math.pow(time, 4) - 1e-06 * Math.pow(time, 3) + 0.00278 * Math.pow(time, 2) - 0.34281 * time - 0.59106);;
        return Math.round((float)rtn);
    }

    public static void main(String[] args) {

        SerialAddress portAddress = new SerialAddress("COM2", 9600,
                DataBits.DATABITS_8, StopBits.BITS_1, Parity.NONE,
                FlowControl.NONE);
        VACVBSHandler handler = new VACVBSHandler();
        handler.loadData();
        RS232Connector receiver = RS232Connector.getInstance(handler,
                portAddress);
        receiver.addFilter("logger", new LoggingFilter());
        receiver.addFilter("codec", new ProtocolCodecFilter(
                new VACVBSProtocolCodecFactory())); // 设置编码过滤器
        // receiver.addFilter("codec", new ProtocolCodecFilter(
        // new TextLineCodecFactory(Charset.forName("UTF-8")))); // 设置编码过滤器
        receiver.startListen();
        VACVBSTester01 test = new VACVBSTester01(receiver);
        new Thread(test).start();

    }
}
